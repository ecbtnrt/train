package tony.train.json.order;

import tony.train.json.BaseObject;

public class IncompleteOrder extends BaseObject{

	OrderData data;

	/**
	 * @return the data
	 */
	public OrderData getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(OrderData data) {
		this.data = data;
	}
	
	
}
