package com.robot.core.http.protocal;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
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
 * @Author mrt
 * @Date 2020/5/14 20:27
 * @Version 2.0
 * 通用HTML响应解析
 */
public class HtmlResponseHandler extends AbstractResponseHandler {

    @Override
    protected boolean errorStatus(StatusLine statusLine) {
        int statusCode = statusLine.getStatusCode();
        return statusCode >= HttpStatus.SC_BAD_REQUEST && statusCode != HttpStatus.SC_UNAUTHORIZED;
    }

    @Override
    protected StanderHttpResponse handleEntity(HttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        Charset charset = getCharset(entity);
        if (null == charset) {
            charset = CharsetObtain.getCharset(EntityUtils.toByteArray(entity));
            if (null == charset) {
                charset=StandardCharsets.UTF_8;
            }
        }
        String result = EntityUtils.toString(response.getEntity(), charset);
        return new StanderHttpResponse<String>(response, result);
    }

    /**
     * 内部类：从html里面meta头里面获取字符编码,逐行进行
     */
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