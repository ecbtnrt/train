package tony.train.handler;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

public class LoginFormHandler implements ResponseHandler<String> {

	public String handleResponse(HttpResponse rsp) throws ClientProtocolException, IOException {
		String ret = "";
		CloseableHttpResponse response = (CloseableHttpResponse) rsp;
		try {
			HttpEntity entity = response.getEntity();

			System.out.println("----------------------------------------");
			System.out.println(response.getStatusLine());
			if (entity != null) {
				System.out.println("Response content length: " + entity.getContentLength());
			}

			String s = EntityUtils.toString(response.getEntity());

			Pattern p = Pattern.compile("/otn/dynamicJs/(\\w+)");
			Matcher m = p.matcher(s);
			if (m.find()) {
				ret = m.group();
			}

		} finally {
			response.close();
		}
		return ret;
	}

}
