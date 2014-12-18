package tony.train.json;

import java.util.List;
import java.util.Map;

public class LimitBuySeatTicketDTO {

	List<CardType> seat_type_codes;
	
	Map<String, List<CardType>> ticket_seat_codeMap;
	
	List<CardType> ticket_type_codes;
	
	/**
	 * @return the seat_type_codes
	 */
	public List<CardType> getSeat_type_codes() {
		return seat_type_codes;
	}

	/**
	 * @param seat_type_codes the seat_type_codes to set
	 */
	public void setSeat_type_codes(List<CardType> seat_type_codes) {
		this.seat_type_codes = seat_type_codes;
	}

	/**
	 * @return the ticket_seat_codeMap
	 */
	public Map<String, List<CardType>> getTicket_seat_codeMap() {
		return ticket_seat_codeMap;
	}

	/**
	 * @param ticket_seat_codeMap the ticket_seat_codeMap to set
	 */
	public void setTicket_seat_codeMap(Map<String, List<CardType>> ticket_seat_codeMap) {
		this.ticket_seat_codeMap = ticket_seat_codeMap;
	}

	/**
	 * @return the ticket_type_codes
	 */
	public List<CardType> getTicket_type_codes() {
		return ticket_type_codes;
	}

	/**
	 * @param ticket_type_codes the ticket_type_codes to set
	 */
	public void setTicket_type_codes(List<CardType> ticket_type_codes) {
		this.ticket_type_codes = ticket_type_codes;
	}

	
	
	
}
