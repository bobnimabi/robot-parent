package com.robot.center.client;

import com.bbin.common.client.PromotionServiceList;
import com.bbin.common.client.TenantActivityTagDTO;
import com.bbin.common.dto.tag.TagDTO;
import com.bbin.common.dto.tag.TagQueryDTO;
import com.bbin.common.response.ResponseResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = PromotionServiceList.BBIN_ACTIVE, path = "promotion/back/rules")
public interface WithdrawClient {

    @ApiOperation("层级：全部添加（更新时用）")
    @PostMapping("/addTags")
    public ResponseResult addTags(List<TenantActivityTagDTO> tenantActivityTagDTOList);

    @ApiOperation("层级：获取全部")
    @PostMapping("/getTags")
    public ResponseResult getTags();

    @ApiOperation("层级：获取已选中的")
    @PostMapping("/getActivityRuleTags")
    public ResponseResult getActivityRuleTags(@RequestBody TagQueryDTO tagQueryDTO);

    @ApiOperation("层级：更新已选中的")
    @PostMapping("/updateTag")
    public ResponseResult updateTag(@RequestBody TagDTO tagDTO);

}