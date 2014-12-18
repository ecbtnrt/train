package tony.train.json.order;

import java.util.List;

public class OrderData {

	List<OrderDB> orderDBList;

	String to_page;

	/**
	 * @return the orderDBList
	 */
	public List<OrderDB> getOrderDBList() {
		return orderDBList;
	}

	/**
	 * @param orderDBList
	 *            the orderDBList to set
	 */
	public void setOrderDBList(List<OrderDB> orderDBList) {
		this.orderDBList = orderDBList;
	}

	/**
	 * @return the to_page
	 */
	public String getTo_page() {
		return to_page;
	}

	/**
	 * @param to_page
	 *            the to_page to set
	 */
	public void setTo_page(String to_page) {
		this.to_page = to_page;
	}

}
