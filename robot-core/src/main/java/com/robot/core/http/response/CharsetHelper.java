package com.robot.core.http.response;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
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
 * 从Html从获取charset
 * @Author mrt
 * @Date 2020/6/15 10:42
 * @Version 2.0
 */
public class CharsetHelper {
    /**
     * 匹配html里面的meta头里的charset专用正则
     */
    private static final Pattern PATTERN = Pattern.compile("charset=[\"]?([\\w-]*)");

    /**
     * 获取htmlCharset
     * @param response
     * @return
     * @throws IOException
     */
    public static Charset getCharset(HttpResponse response, byte[] bytes) throws IOException {
        Charset charset = getCharsetByConentType(response.getEntity());
        if (null == charset) {
            charset = getCharsetByHtml(bytes);
        }
        return null == charset ? StandardCharsets.UTF_8 : charset;
    }

    /**
     * 从响应头Content-type中获取Charset，如果没有，获取默认Charset
     * @param entity
     * @param defaultCharset
     * @return
     * @throws IOException
     */
    private static final Charset getCharsetOrDefault(HttpEntity entity, Charset defaultCharset) throws IOException {
        Charset charset = getCharsetByConentType(entity);
        return null == charset ? defaultCharset : charset;
    }

    /**
     * 从响应头Content-type中获取Charset
     * @param httpEntity
     * @return
     * null 未获取到Charset
     */
    private static Charset getCharsetByConentType(HttpEntity httpEntity) {
        ContentType contentType = ContentType.get(httpEntity);
        return null != contentType ? contentType.getCharset() : null;
    }

    /**
     * 获取Html里的meta头里的字符编码
     * @param bytes
     * @return
     * @throws IOException
     */
    private static Charset getCharsetByHtml(byte[] bytes) throws IOException {
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
