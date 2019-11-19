package com.robot.bbin.activity.function;

import com.bbin.common.response.ResponseResult;
import com.bbin.utils.project.DateUtils;
import com.robot.bbin.activity.dto.GameChild;
import com.robot.bbin.activity.dto.OrderNoQueryDTO;
import com.robot.bbin.activity.dto.TotalBetGameDTO;
import com.robot.bbin.activity.vo.BreakerQueryVO;
import com.robot.bbin.activity.vo.JuQueryVO;
import com.robot.bbin.activity.vo.TotalBetGameVO;
import com.robot.center.execute.IActionEnum;
import com.robot.center.execute.IResultParse;
import com.robot.center.function.FunctionBase;
import com.robot.center.function.ParamWrapper;
import com.robot.center.pool.RobotWrapper;
import com.robot.code.entity.TenantRobotAction;
import com.bbin.utils.project.MyBeanUtils;
import com.robot.bbin.activity.basic.ActionEnum;
import com.robot.bbin.activity.vo.QueryBalanceVO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mrt on 11/15/2019 6:39 PM
 * 消消除
 */
@Service
public class BreakerServer extends FunctionBase<OrderNoQueryDTO> {
    @Autowired
    private JuQueryServer juQueryServer;
    @Autowired
    private TotalBetGame totalBetGame;
    @Autowired
    private QueryBalanceServer queryBalanceServer;
    @Autowired
    private JuQueryDetailServer juQueryDetailServer;

    @Override
    protected ResponseResult doFunctionFinal(ParamWrapper<OrderNoQueryDTO> paramWrapper, RobotWrapper robotWrapper, TenantRobotAction action) throws Exception {
        OrderNoQueryDTO queryDTO = paramWrapper.getObj();

        // 局查询
        ResponseResult juResponse = juQueryServer.doFunctionFinal(paramWrapper,robotWrapper,action);
        if (!juResponse.isSuccess()) {
            return juResponse;
        }
        JuQueryVO juQueryVO = (JuQueryVO) juResponse.getObj();
        if (StringUtils.isEmpty(juQueryVO.getPageId())) {
            return ResponseResult.FAIL("订单已过期");
        }
        // 组装局查询细节所需参数
        ResponseResult addParamsResult = addParams(queryDTO, juQueryVO);
        if (!addParamsResult.isSuccess()) {
            return addParamsResult;
        }
        BreakerQueryVO breakerQueryVO = MyBeanUtils.copyProperties(juQueryVO, BreakerQueryVO.class);

        // 查询闯关情况
        ResponseResult juDetailResult = juQueryDetailServer.doFunctionFinal(paramWrapper, robotWrapper, getAction(ActionEnum.JU_QUERY_DETAIL));
        if (!juDetailResult.isSuccess()) {
            return juDetailResult;
        }
        String result = (String) juDetailResult.getObj();
        ResponseResult parseResult = WinNumParser.INSTANCE.parse(result);
        if (!parseResult.isSuccess()) {
            return parseResult;
        }
        breakerQueryVO.setAccumulativeWins((Integer) parseResult.getObj());

        // 查询余额：查询UserID
        ResponseResult balanceResponse = queryBalanceServer.doFunctionFinal(new ParamWrapper<String>(juQueryVO.getUserName()), robotWrapper, getAction(ActionEnum.QUERY_BALANCE));
        if (!balanceResponse.isSuccess()) {
            return balanceResponse;
        }
        QueryBalanceVO queryBalanceVO = (QueryBalanceVO) balanceResponse.getObj();

        // 查询游戏总投注
        TotalBetGameDTO gameDTO = new TotalBetGameDTO(
                queryDTO.getStartDate().format(DateUtils.DF_3),queryDTO.getEndDate().format(DateUtils.DF_3),
                queryBalanceVO.getUser_id(),queryDTO.getBarId(),queryDTO.getGameCode());
        ResponseResult totoalBetResult = totalBetGame.doFunctionFinal(new ParamWrapper<TotalBetGameDTO>(gameDTO), robotWrapper, getAction(ActionEnum.TOTAL_BET_BY_GAME));
        if (!totoalBetResult.isSuccess()) {
            return totoalBetResult;
        }
        List<TotalBetGameVO> list = (List<TotalBetGameVO>)totoalBetResult.getObj();
        TotalBetGameVO totalBetGameVO = filterByGameName(list, breakerQueryVO.getGameName());
        if (null == totalBetGameVO) {
            return ResponseResult.FAIL("未查询到游戏对应的总投注金额");
        }
        breakerQueryVO.setTotalBetAmount(totalBetGameVO.getTotalBetByGame());
        return juResponse;
    }

    // 附加局查询参数
    private ResponseResult addParams(OrderNoQueryDTO queryDTO,JuQueryVO juQueryVO) {
        queryDTO.setPageId(juQueryVO.getPageId());
        queryDTO.setKey(juQueryVO.getKey());
        String gameName = juQueryVO.getGameName();
        boolean flag = false;
        for (GameChild child : queryDTO.getChildren()) {
            if (gameName.equals(child.getName())) {
                queryDTO.setGameType(child.getGameCode());
                flag = true;
                break;
            }
        }
        if (!flag) {
            return ResponseResult.FAIL("List<GameChild>未包含此游戏：" + gameName);
        }
        return ResponseResult.SUCCESS();
    }

    @Override
    public IActionEnum getActionEnum() {
        return ActionEnum.JU_QUERY;
    }

    /**
     * 响应结果转换类
     * 转换成闯关局数
     */
    private static final class WinNumParser implements IResultParse {
        private static final WinNumParser INSTANCE = new WinNumParser();
        private WinNumParser(){}

        @Override
        public ResponseResult parse(String result) {
            Document doc = Jsoup.parse(result);
            Element table = doc.select("table").get(1);// :eq(1)
            Elements tbody_trs = table.select("tbody tr");
            for (Element tbody_tr : tbody_trs) {
                Elements tds = tbody_tr.getElementsByTag("td");
                if (tds.size() >= 2) {
                    Element td1 = tbody_tr.getElementsByTag("td").get(0);
                    System.out.println(td1);
                    Element td2 = tbody_tr.getElementsByTag("td").get(1);
                    if ("-".equals(td2.text())) {
                        return ResponseResult.SUCCESS(Integer.parseInt(td1.text()) - 1);
                    }
                }
            }
            return ResponseResult.FAIL("未查询到消除层数");
        }
    }

    private Map<String,String> converListToMap(List<GameChild> children) {
        Map<String, String> map = new HashMap<>(10);
        for (GameChild child : children) {
            map.put(child.getName(), child.getGameCode());
        }
        return map;
    }

    private TotalBetGameVO filterByGameName(List<TotalBetGameVO> list, String gameName) {
        for (TotalBetGameVO totalBetGameVO : list) {
            if (gameName.equals(totalBetGameVO.getGameName())) {
                return totalBetGameVO;
            }
        }
        return null;
    }

}
