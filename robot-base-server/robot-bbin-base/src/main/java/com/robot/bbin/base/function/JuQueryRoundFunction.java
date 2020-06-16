package com.robot.bbin.base.function;

import com.bbin.utils.project.DateUtils;
import com.robot.bbin.base.basic.PathEnum;
import com.robot.bbin.base.ao.JuQueryAO;
import com.robot.bbin.base.bo.JuQueryBO;
import com.robot.center.util.MoneyUtil;
import com.robot.code.dto.Response;
import com.robot.code.service.ITenantRobotDictService;
import com.robot.core.function.base.AbstractFunction;
import com.robot.core.function.base.IPathEnum;
import com.robot.core.function.base.IResultHandler;
import com.robot.core.http.request.IEntity;
import com.robot.core.http.request.UrlEntity;
import com.robot.core.http.response.StanderHttpResponse;
import com.robot.core.robot.manager.RobotWrapper;
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
public class JuQueryRoundFunction extends AbstractFunction<JuQueryAO,String, JuQueryBO> {
    @Autowired
    private ITenantRobotDictService dictService;
    // 字典表：获取注单查询的barID的前缀
    private String DICT_BAR_ID = "BBIN:ROUND_QUERY:";

    @Override
    protected IPathEnum getPathEnum() {
        return PathEnum.JU_QUERY;
    }

    @Override
    protected IEntity getEntity(JuQueryAO queryDTO, RobotWrapper robotWrapper) {
        return UrlEntity.custom(6)
                .add("SearchData", queryDTO.getSearchData())
                .add("BarID", queryDTO.getBarId())
                .add("GameKind", queryDTO.getGameKind()) // 平台编码
                .add("RoundNo", queryDTO.getOrderNo()) // 注单号
                .add("Limit", queryDTO.getLimit()) // 每页大小
                .add("Sort", queryDTO.getSort()); // 时间倒排
    }

    @Override
    protected IResultHandler<String, JuQueryBO> getResultHandler() {
        return null;
    }

    /**
     * 响应结果转换类
     */
    private static final class JuQueryParse implements IResultHandler<String, JuQueryBO> {
        private static final JuQueryParse INSTANCE = new JuQueryParse();
        private JuQueryParse() {}

        @Override
        public Response parse2Obj(StanderHttpResponse<String, JuQueryBO> shr) {
            String result = shr.getOriginalEntity();
            Document doc = Jsoup.parse(result);
            // tbody为空表示没有
            Elements tds = doc.select("table[class=table table-hover text-middle table-bordered] tbody tr td");
            if (CollectionUtils.isEmpty(tds)) {
                return Response.FAIL("记录不存在");
            }

            // 获取显示值table table-hover text-middle table-bordered
            JuQueryBO juQueryVO = new JuQueryBO();
            juQueryVO.setOrderTime(DateUtils.format(tds.get(0).text()));
            juQueryVO.setPlatFormOrderNo(tds.get(1).text());
            juQueryVO.setGameName(tds.get(2).text());
            juQueryVO.setHall(tds.get(3).text());
            juQueryVO.setUserName(tds.get(4).text());
            juQueryVO.setResult(tds.get(5).text());
            juQueryVO.setRebateAmount(MoneyUtil.formatYuan(tds.get(6).text()));
            juQueryVO.setSendAmount(MoneyUtil.formatYuan(tds.get(7).text()));
            juQueryVO.setRound(tds.get(8).text());
            return Response.SUCCESS(juQueryVO);
        }
    }

}
