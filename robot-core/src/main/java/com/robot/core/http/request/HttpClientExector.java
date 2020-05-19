package com.robot.core.http.request;

import com.alibaba.fastjson.JSON;
import com.robot.core.http.response.StanderHttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpMessage;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
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

/**
 * Created by mrt on 10/17/2019 3:58 PM
 */
@Component
@Slf4j
public class HttpClientExector {
	/**
	 * get请求
	 * @param httpClient
	 * @param url
	 * @param customEntity
	 * @param headers
	 * @return
	 */
	public static StanderHttpResponse get(
			CloseableHttpClient httpClient,
			String url,
			UrlCustomEntity customEntity,
			CustomHeaders headers,
			HttpContext httpContext,
			ResponseHandler<StanderHttpResponse> responseHandler) throws IOException, URISyntaxException {
		StanderHttpResponse result = null;
		HttpGet httpGet = new HttpGet(url);
		try {
			if (null != customEntity && !CollectionUtils.isEmpty(customEntity.getEntity())) {
				String encodeUrl = URLEncodedUtils.format(customEntity.getEntity(), StandardCharsets.UTF_8);
				httpGet.setURI(new URI(httpGet.getURI().toString().indexOf("?") > 0 ? httpGet.getURI().toString() + "&" + encodeUrl : httpGet.getURI().toString() + "?" + encodeUrl));
			}
			setHeaders(httpGet, headers);
			result = httpClient.execute(httpGet, responseHandler, httpContext);
		} catch (Exception e) {
			httpGet.abort();
			throw e;
		}
		return result;
	}

	/**
	 * post请求表单形式
	 * @param httpClient
	 * @param url
	 * @param customEntity
	 * @param headers
	 * @return
	 */
	public static StanderHttpResponse postForm(
			CloseableHttpClient httpClient,
			String url,
			UrlCustomEntity customEntity,
			CustomHeaders headers,
			HttpContext httpContext,
			ResponseHandler<StanderHttpResponse> responseHandler) throws IOException {
		StanderHttpResponse result = null;
		HttpPost httpPost = new HttpPost(url);
		httpPost.setConfig(RequestConfig.custom().setRedirectsEnabled(true).setRelativeRedirectsAllowed(false).build());
		try {
			if (null != customEntity && !CollectionUtils.isEmpty(customEntity.getEntity())) {
				HttpEntity entity = new UrlEncodedFormEntity(customEntity.getEntity(), StandardCharsets.UTF_8);
				httpPost.setEntity(entity);
			}
			setHeaders(httpPost, headers);
			result = httpClient.execute(httpPost, responseHandler, httpContext);
		} catch (Exception e) {
			httpPost.abort();
			throw e;
		}
		return result;
	}

	/**
	 * post 请求JSON格式
	 *
	 * @param httpClient
	 * @param url
	 * @param customEntity
	 * @param headers
	 * @return
	 */
	public static StanderHttpResponse postJson(
			CloseableHttpClient httpClient,
			String url,
			JsonCustomEntity customEntity,
			CustomHeaders headers,
			HttpContext httpContext,
			ResponseHandler<StanderHttpResponse> responseHandler) throws IOException {

		StanderHttpResponse result = null;
		HttpPost httpPost = new HttpPost(url);
		try {
			if (null != customEntity && !CollectionUtils.isEmpty(customEntity.getEntity())) {
				StringEntity entity = new StringEntity(JSON.toJSONString(customEntity.getEntity()), ContentType.APPLICATION_JSON);
				httpPost.setEntity(entity);
			}
			setHeaders(httpPost, headers);
			result = (StanderHttpResponse) httpClient.execute(httpPost, responseHandler, httpContext);
		} catch (Exception e) {
			httpPost.abort();
			throw e;
		}
		return result;
	}

	/**
	 * post请求文件上传
	 *
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

	/**
	 * 头信息设置
	 * @param method 请求方法
	 * @param headers 头信息
	 */
	private static void setHeaders(HttpMessage method, CustomHeaders headers) {
		if (null != headers && !CollectionUtils.isEmpty(headers.getHeaders())) {
			headers.getHeaders().forEach(header->method.setHeader(header));
		}
	}
}