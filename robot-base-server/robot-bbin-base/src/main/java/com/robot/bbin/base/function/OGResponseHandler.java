package com.robot.bbin.base.function;

import com.robot.core.http.response.StanderHttpResponse;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;

import java.io.IOException;

/**
 * <p>
 *
 * </p>
 *
 * @author tank
 * @date 2020/7/18
 */
public class OGResponseHandler  implements ResponseHandler<StanderHttpResponse> {
	@Override
	public StanderHttpResponse handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException {
		return null;
	}
}
