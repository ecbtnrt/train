package tony.train.handler;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

public class PrintResponseHandler implements ResponseHandler<String> {

	public String handleResponse(HttpResponse rsp) throws ClientProtocolException, IOException {
		CloseableHttpResponse response = (CloseableHttpResponse) rsp;
		try {
			HttpEntity entity = response.getEntity();

			System.out.println("----------------------------------------");
			System.out.println(response.getStatusLine());
			if (entity != null) {
				System.out.println("Response content length: " + entity.getContentLength());
			}
			return EntityUtils.toString(entity);
		} finally {
			response.close();
		}
	}

}
