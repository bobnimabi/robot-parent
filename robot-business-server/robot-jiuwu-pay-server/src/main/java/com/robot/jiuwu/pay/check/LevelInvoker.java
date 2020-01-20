package com.robot.jiuwu.pay.check;

import com.alibaba.fastjson.JSON;
import com.bbin.common.dto.tag.TagQueryDTO;
import com.bbin.common.response.ResponseResult;
import com.robot.center.chain.FilterChainBase;
import com.robot.center.client.WithdrawClient;
import com.robot.jiuwu.base.vo.WithdrawListRowsData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * Created by mrt on 2020/1/10 0010 12:14
 * 层级过滤
 */
@Slf4j
@Service
public class LevelInvoker extends FilterChainBase.Invoker<WithdrawListRowsData>{
    @Autowired
    private WithdrawClient withdrawClient;

    @Override
    protected void before(int pos, List<WithdrawListRowsData> rows) {
        log.info("==================层级检查==================");
        Set<Long> allLevel = getAllLevel();
        log.info("已选层级：{}", JSON.toJSONString(allLevel));
        Iterator<WithdrawListRowsData> iterator = rows.iterator();
        while (iterator.hasNext()){
            WithdrawListRowsData row = iterator.next();
            if (!allLevel.contains(row.getMemberOrder())) {
                iterator.remove();
                log.info("层级过滤，去除：{}", JSON.toJSONString(row));
            }
        }
    }

    /**
     * 查询所有层级
     */
    private Set<Long> getAllLevel() {
        TagQueryDTO tagQueryDTO = new TagQueryDTO();
        tagQueryDTO.setActivityId(1L);
        ResponseResult activityRuleTags = withdrawClient.getActivityRuleTags(tagQueryDTO);
        List<LinkedHashMap> list = (List<LinkedHashMap>)activityRuleTags.getObj();
        Set<Long> setLevel = new HashSet<>();
        for (LinkedHashMap linkedHashMap : list) {
            setLevel.add(Long.parseLong(String.valueOf(linkedHashMap.get("tagName"))));
        }
        return setLevel;
    }

    @Override
    protected void after(int pos, List<WithdrawListRowsData> rows) {

    }
}
