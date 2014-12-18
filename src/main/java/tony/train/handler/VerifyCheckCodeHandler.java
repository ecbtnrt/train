package tony.train.handler;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

public class VerifyCheckCodeHandler implements ResponseHandler<Boolean> {

	public Boolean handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
		try {
			HttpEntity entity = response.getEntity();

			System.out.println("Login form get: " + response.getStatusLine());

			String str = EntityUtils.toString(entity);
			if (null != str && str.contains("randCodeRight")) {
				System.out.println("登陆验证码正确...");
				return true;
			} else {
				System.err.println("验证码错误,重新获取验证码...");
				return false;
			}
		} finally {
			((CloseableHttpResponse) response).close();
		}
	}

}
