package com.robot.core.task.execute;

import com.alibaba.fastjson.JSON;
import com.robot.core.http.request.*;
import com.robot.core.http.response.StanderHttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpMessage;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.HttpContext;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Created by mrt on 10/17/2019 3:58 PM
 */
@Component
@Slf4j
public class HttpClientExector {
	/**
	 * 请求实体
	 * @param exe
	 * @return
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static StanderHttpResponse execute(ExecuteProperty exe) throws IOException, URISyntaxException {
		StanderHttpResponse result = null;
		HttpRequestBase httpRequestBase = null;
		try {
			// 请求体+URL
			httpRequestBase = setEntity(exe.getMethod(), exe.getUrl(), exe.getCustomEntity());
			// 请求头
			setHeaders(httpRequestBase, exe.getHeaders());
			// 请求配置
			httpRequestBase.setConfig(exe.getRequestConfig().build());
			// 执行
			result = exe.getHttpClient().execute(httpRequestBase, exe.getResponseHandler(), exe.getHttpContext());
		} catch (Exception e) {
			httpRequestBase.abort();
			throw e;
		}
		return result;
	}

	/**
	 * 组装请求对象
	 * @param method 请求方法
	 * @param url 请求Url
	 * @param customEntity 请求体
	 * @return
	 * @throws URISyntaxException
	 */
	private static HttpRequestBase setEntity(CustomHttpMethod method, URI url, ICustomEntity customEntity) throws URISyntaxException {
		switch (method) {
			case GET:{
				HttpGet httpGet = new HttpGet(url);
				if (null != customEntity && !customEntity.isEmpty()) {
					UrlCustomEntity urlCustomEntity = (UrlCustomEntity) customEntity;
					String encodeUrl = URLEncodedUtils.format(urlCustomEntity.getEntity(), StandardCharsets.UTF_8);
					httpGet.setURI(new URI(httpGet.getURI().toString().indexOf("?") > 0 ? httpGet.getURI().toString() + "&" + encodeUrl : httpRequestBase.getURI().toString() + "?" + encodeUrl));
					return httpGet;
				}
			}
			case POST_FORM:{
				HttpPost httpPost = new HttpPost(url);
				if (null != customEntity && !customEntity.isEmpty()) {
					UrlCustomEntity urlCustomEntity = (UrlCustomEntity)customEntity;
					HttpEntity entity = new UrlEncodedFormEntity(urlCustomEntity.getEntity(), StandardCharsets.UTF_8);
					httpPost.setEntity(entity);
					return httpPost;
				}
			}
			case POST_JSON:{
				HttpPost httpPost = new HttpPost(url);
				if (null != customEntity && !customEntity.isEmpty()) {
					JsonCustomEntity jsonEntity = (JsonCustomEntity) customEntity;
					StringEntity entity = new StringEntity(JSON.toJSONString(jsonEntity.getEntity()), ContentType.APPLICATION_JSON);
					httpPost.setEntity(entity);
					return httpPost;
				}
			}
			default:
				throw new IllegalArgumentException("Method越界");
		}
	}

	/**
	 * 头信息设置
	 * @param method 请求方法
	 * @param headers 头信息
	 * 注意：这里反向填充头的目的是：让接口特殊头覆盖全局头（当同时存在某个头时候）
	 */
	private static void setHeaders(HttpMessage method, CustomHeaders headers) {
		if (null != headers && !CollectionUtils.isEmpty(headers.getHeaders())) {
			List<Header> headersList = headers.getHeaders();
			for (int i = headersList.size() - 1; i >= 0; i--) {
				method.setHeader(headersList.get(i));
			}
		}
	}


	/**
	 * post请求文件上传
	 * （暂时先放着，不用）
	 * @param httpClient
	 * @param url
	 * @param filePath
	 * @param fileName
	 * @param customeEntity
	 * @return
	 */
	public static StanderHttpResponse uploadFile(
			CloseableHttpClient httpClient,
			String url,
			String filePath,
			String fileName,
			UrlCustomEntity customeEntity,
			HttpContext httpContext,
			ResponseHandler<StanderHttpResponse> responseHandler) throws IOException {
		File file = new File(filePath);
		if (!(file.exists() && file.isFile())) {
			throw new RuntimeException("file : file is null");
		}
		StanderHttpResponse result = null;
		HttpPost httpPost = new HttpPost(url);
		try {
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			builder.addBinaryBody(fileName, file, ContentType.DEFAULT_BINARY, file.getName());
			if (CollectionUtils.isEmpty(customeEntity.getEntity())) {
				customeEntity.getEntity().forEach(o -> {
					builder.addTextBody(o.getName(), o.getName(), ContentType.TEXT_PLAIN.withCharset(StandardCharsets.UTF_8));
				});
			}
			HttpEntity requestEntity = builder.build();
			httpPost.setEntity(requestEntity);
			result =  httpClient.execute(httpPost, responseHandler, httpContext);
		} catch (Exception e) {
			httpPost.abort();
			throw e;
		}
		return result;
	}
}