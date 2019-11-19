package com.robot.center.httpclient;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mrt on 10/22/2019 8:52 PM
 */
@Slf4j
public class CustomeResponseHandler implements ResponseHandler<StanderHttpResponse> {
    public static final CustomeResponseHandler INSTANCE = new CustomeResponseHandler();

    @Override
    public StanderHttpResponse handleResponse(HttpResponse response) throws HttpResponseException, IOException {
        if (null == response) {
            throw new IllegalStateException("数据包：服务器未响应或被中间代理拦截");
        }
        StatusLine statusLine = response.getStatusLine();
        HttpEntity entity = response.getEntity();
        if (statusLine.getStatusCode() >= HttpStatus.SC_BAD_REQUEST) {
            EntityUtils.consume(entity);
            throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
        } else {
            StanderHttpResponse standerHttpResponse = new StanderHttpResponse();
            standerHttpResponse.setStatusLine(response.getStatusLine());
            standerHttpResponse.setHeaders(response.getAllHeaders());
            return entity == null ? standerHttpResponse : this.handleEntity(entity,standerHttpResponse);
        }
    }

    public StanderHttpResponse handleEntity(HttpEntity httpEntity, StanderHttpResponse standerHttpResponse) throws IOException {
        if (httpEntity.getContentLength() > (8 << 20)) {
            throw new IllegalArgumentException("响应大小超过8M");
        }
        standerHttpResponse.setEntityStr(parseToString(httpEntity));
        return standerHttpResponse;
    }

    /**
     * 将HttpEntity的响应转换成正确编码的字符串
     * @param httpEntity
     * @return
     * @throws IOException
     * 注意：只能接收html和json
     */
    private String parseToString(HttpEntity httpEntity) throws IOException {
        StringBuilder content = new StringBuilder(httpEntity.getContentLength() != -1L ? (int) httpEntity.getContentLength() : 16);
        ContentType contentType = ContentType.get(httpEntity);
        String mimeType = null;
        // 最好情况：Content-Type里面有Charset
        if (null != contentType) {
            mimeType = contentType.getMimeType();
            Charset charset = contentType.getCharset();
            if (null != charset) {
                content.append(EntityUtils.toString(httpEntity, charset));
            }
        }
        // 次好情况：虽然Content-Type无Charset，但MimeType为application/json
        if (0 == content.length()) {
            if (!StringUtils.isEmpty(mimeType) && ContentType.APPLICATION_JSON.getMimeType().equalsIgnoreCase(mimeType)) {
                content.append(EntityUtils.toString(httpEntity, StandardCharsets.UTF_8));
            }
        }
        // 最差情况：无Content-Type或Content-Type的MimeType不是application/json（一般来说是html）
        if (0 == content.length()) {
            byte[] bytes = EntityUtils.toByteArray(httpEntity);
            Charset charset = CharsetObtain.getCharset(bytes);
            content.append(new String(bytes, charset));
        }
        return content.toString();
    }


    private static final class CharsetObtain {
        private static final Pattern PATTERN = Pattern.compile("charset=[\"]?([\\w-]*)");
        /**
         * 获取字符编码
         * @param bytes
         * @return
         * @throws IOException
         */
        private static Charset getCharset(byte[] bytes) throws IOException {
            Charset charset = StandardCharsets.UTF_8;
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(byteArrayInputStream));
            try {
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    String charSetString = match(line, 1);
                    if (!StringUtils.isEmpty(charSetString)) {
                        charset = Charset.forName(charSetString);
                        break;
                    }
                }
            } finally {
                if (null != bufferedReader) {
                    bufferedReader.close();
                }
            }
            return charset;
        }

        /**
         * 匹配行字符编码
         * @param line 匹配行
         * @param group 正则匹配组标号
         * @return
         */
        private static String match(String line,int group) {
            String charSet = null;
            if (!StringUtils.hasText(line) || !line.contains("<meta")) {
                return charSet;
            }
            line = line.toLowerCase();
            Matcher matcher = PATTERN.matcher(line);
            if (matcher.find()) {
                charSet = matcher.group(group);
            }
            return charSet;
        }
    }
}
