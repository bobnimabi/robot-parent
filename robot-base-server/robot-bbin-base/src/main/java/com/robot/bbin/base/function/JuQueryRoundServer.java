package com.robot.bbin.base.function;

import com.bbin.common.response.ResponseResult;
import com.bbin.utils.project.DateUtils;
import com.robot.bbin.base.basic.ActionEnum;
import com.robot.bbin.base.dto.OrderNoQueryDTO;
import com.robot.bbin.base.vo.JuQueryVO;
import com.robot.center.execute.IActionEnum;
import com.robot.center.execute.IResultParse;
import com.robot.center.function.FunctionBase;
import com.robot.center.function.ParamWrapper;
import com.robot.center.httpclient.CustomHttpMethod;
import com.robot.center.httpclient.StanderHttpResponse;
import com.robot.center.httpclient.UrlCustomEntity;
import com.robot.center.pool.RobotWrapper;
import com.robot.center.util.MoneyUtil;
import com.robot.code.entity.TenantRobotAction;
import com.robot.code.service.ITenantRobotDictService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * Created by mrt on 11/15/2019 12:29 PM
 * 局查询：场次查询
 * 现在以下电子使用本类：
 *  申博电子（按场次查询，场次即是申博电子的注单）
 */
@Service
public class JuQueryRoundServer extends FunctionBase<OrderNoQueryDTO> {
    @Autowired
    private ITenantRobotDictService dictService;
    // 字典表：获取注单查询的barID的前缀
    private String DICT_BAR_ID = "BBIN:ROUND_QUERY:";

    @Override
    public ResponseResult doFunctionFinal(ParamWrapper<OrderNoQueryDTO> paramWrapper, RobotWrapper robotWrapper, TenantRobotAction action) throws Exception {
        OrderNoQueryDTO queryDTO = paramWrapper.getObj();
        String barId = dictService.getValue(DICT_BAR_ID + queryDTO.getGameCode());
        queryDTO.setBarId(barId);
        action.setActionUrl(action.getActionUrl() + queryDTO.getGameCode());
        // 执行
        StanderHttpResponse response = execute.request(robotWrapper, CustomHttpMethod.GET, action, null,
                createJuQueryParams(queryDTO), null, JuQueryParse.INSTANCE);
        ResponseResult responseResult = response.getResponseResult();
        return responseResult;
    }

    @Override
    public IActionEnum getActionEnum() {
        return ActionEnum.JU_QUERY;
    }

    //组装局查询
    private UrlCustomEntity createJuQueryParams(OrderNoQueryDTO queryDTO) throws Exception{
        return UrlCustomEntity.custom()
                .add("SearchData", "BetQuery")
                .add("BarID", queryDTO.getBarId())
                .add("GameKind", queryDTO.getGameCode()) // 平台编码
                .add("RoundNo", queryDTO.getOrderNo()) // 注单号
                .add("Limit", "50") // 每页大小
                .add("Sort", "DESC"); // 时间倒排
    }

    /**
     * 响应结果转换类
     */
    private static final class JuQueryParse implements IResultParse {
        private static final JuQueryParse INSTANCE = new JuQueryParse();
        private JuQueryParse(){}

        @Override
        public ResponseResult parse(String result) {
            Document doc = Jsoup.parse(result);
            // tbody为空表示没有
            Elements tds = doc.select("table[class=table table-hover text-middle table-bordered] tbody tr td");
            if (CollectionUtils.isEmpty(tds)) {
                return ResponseResult.FAIL("记录不存在");
            }

            // 获取显示值table table-hover text-middle table-bordered
            JuQueryVO juQueryVO = new JuQueryVO();
            juQueryVO.setOrderTime(DateUtils.format(tds.get(0).text()));
            juQueryVO.setPlatFormOrderNo(tds.get(1).text());
            juQueryVO.setGameName(tds.get(2).text());
            juQueryVO.setHall(tds.get(3).text());
            juQueryVO.setUserName(tds.get(4).text());
            juQueryVO.setResult(tds.get(5).text());
            juQueryVO.setRebateAmount(MoneyUtil.formatYuan(tds.get(6).text()));
            juQueryVO.setSendAmount(MoneyUtil.formatYuan(tds.get(7).text()));
            juQueryVO.setRound(tds.get(8).text());
            return ResponseResult.SUCCESS(juQueryVO);
        }
    }

}
