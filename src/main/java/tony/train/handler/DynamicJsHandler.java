package tony.train.handler;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import tony.train.Ticket;
import tony.train.utils.HttpUtil;

public class DynamicJsHandler implements ResponseHandler<String> {

	private CloseableHttpClient httpclient;

	public DynamicJsHandler() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DynamicJsHandler(CloseableHttpClient httpclient) {
		super();
		this.httpclient = httpclient;
	}

	public String handleResponse(HttpResponse rsp) throws ClientProtocolException, IOException {
		String dynamicJsUrl = "";
		String key = "";
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
				dynamicJsUrl = m.group();

				dynamicJsUrl = Ticket.root + dynamicJsUrl;

				String jsContent = HttpUtil.doPost(httpclient, dynamicJsUrl, new PrintResponseHandler());

				Pattern jsPattern = Pattern.compile("var(\\s*)key(\\s*)=(\\s*)'(\\w*)';");
				Matcher jsMatcher = jsPattern.matcher(jsContent);
				if (jsMatcher.find()) {
				//	key = jsMatcher.group();
					key = jsMatcher.group(4);
				//	key = key.split("=")[1];
				//	key = key.replace("[ ]*", "").replace("'", "").replace(";", "");
				}

			}

		} finally {
			response.close();
		}
		return key;
	}

}
