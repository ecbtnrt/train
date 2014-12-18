package tony.train.handler;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

public class SubmitOrderRequest implements ResponseHandler<Boolean> {

	@Override
	public Boolean handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
		System.out.println("SubmitOrderRequest: " + EntityUtils.toString(response.getEntity()));
		return true;
	}

}
