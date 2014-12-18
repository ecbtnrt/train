package tony.train.handler;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

import tony.train.Ticket;
import tony.train.json.order.IncompleteOrder;

public class IncompletelyOrderHandler implements ResponseHandler<IncompleteOrder> {

	@Override
	public IncompleteOrder handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
		HttpEntity entity = response.getEntity();
		String string = EntityUtils.toString(entity);

		IncompleteOrder order;
		try {
			order = Ticket.mapper.readValue(string, IncompleteOrder.class);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return order;
	}

}
