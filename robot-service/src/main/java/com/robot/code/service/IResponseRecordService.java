package com.robot.code.service;

import com.robot.code.entity.ResponseRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.nio.charset.Charset;

/**
 * <p>
 * 响应流水 服务类
 * </p>
 *
 * @author admin
 * @since 2020-05-18
 */
public interface IResponseRecordService extends IService<ResponseRecord> {
    /**
     * 添加响应流水
     *
     * @param requestRecordId 请求流水的id
     * @param html            响应的原始HTML内容
     * @Charset charset       编码集
     * @param parsedJson      转换过的JSON
     */
    void addResponseRecord(long requestRecordId, String html, Charset charset, String parsedJson);


}
