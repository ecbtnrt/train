package tony.train.json;

import java.util.List;

public class TicketInfo extends BaseObject{

	List<TicketDTO> data;

	/**
	 * @return the data
	 */
	public List<TicketDTO> getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(List<TicketDTO> data) {
		this.data = data;
	}

}
