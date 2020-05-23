package com.robot.code.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.bbin.common.util.DateUtils;
import com.robot.code.entity.ResponseRecord;
import com.robot.code.mapper.ResponseRecordMapper;
import com.robot.code.service.IResponseRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/**
 * <p>
 * 响应流水 服务实现类
 * </p>
 *
 * @author admin
 * @since 2020-05-18
 */
@Slf4j
@Service
public class ResponseRecordServiceImpl extends ServiceImpl<ResponseRecordMapper, ResponseRecord> implements IResponseRecordService {

    @Value("${html.output.root}")
    private String htmlOutputRoot;

    @Async
    @Override
    public void addResponseRecord(long requestRecordId, Object html, String parsedJson) {
        long id = IdWorker.getId();
        String htmlPath = "";
        String content = html instanceof String ? (String) html : Base64Utils.encodeToString((byte[]) html);
        if (!StringUtils.isEmpty(html)) {
            htmlPath = saveHtml2Text(id, content, StandardCharsets.UTF_8);
        }
        ResponseRecord responseRecord = new ResponseRecord();
        responseRecord.setId(id);
        responseRecord.setOriginalContent(htmlPath);
        responseRecord.setParsedContent(parsedJson);
        responseRecord.setRequestRecordId(requestRecordId);
        boolean isSave = save(responseRecord);
        if (!isSave) {
            log.error("存储响应流水失败");
        }
    }

    /**
     * 存储并压缩
     */
    private String saveHtml2Text(long id, String html, Charset charset) {
        // 确认文件夹存在
        String suffix = DateUtils.getYyyyMMdd() + "/";
        String directory = htmlOutputRoot + suffix;
        boolean exists = exists(directory);
        if (!exists) {
            return "建立存储目录失败";
        }

        // 存储
        String fileName = id + ".text";
        try (
                InputStream inputStream = IOUtils.toInputStream(html, charset);
                OutputStream outputStream = new FileOutputStream(directory + fileName);) {
            IOUtils.copyLarge(inputStream, outputStream);
            return suffix + fileName;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private boolean exists(String directory ) {
        File file = new File(directory);
        return file.exists() ? true : file.mkdirs();
    }
}
