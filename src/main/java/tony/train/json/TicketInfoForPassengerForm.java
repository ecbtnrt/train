package tony.train.json;

import java.util.List;

public class TicketInfoForPassengerForm {

	List<CardType> cardTypes;

	String isAsync;

	String key_check_isChange;

	List<String> leftDetails;
	String leftTicketStr;

	LimitBuySeatTicketDTO limitBuySeatTicketDTO;

	String maxTicketNum;

	OrderRequestDTO orderRequestDTO;

	String purpose_codes;

	QueryLeftNewDetailDTO queryLeftNewDetailDTO;

	QueryLeftTicketRequestDTO queryLeftTicketRequestDTO;

	String tour_flag;
	String train_location;

	/**
	 * @return the cardTypes
	 */
	public List<CardType> getCardTypes() {
		return cardTypes;
	}

	/**
	 * @param cardTypes
	 *            the cardTypes to set
	 */
	public void setCardTypes(List<CardType> cardTypes) {
		this.cardTypes = cardTypes;
	}

	/**
	 * @return the isAsync
	 */
	public String getIsAsync() {
		return isAsync;
	}

	/**
	 * @param isAsync
	 *            the isAsync to set
	 */
	public void setIsAsync(String isAsync) {
		this.isAsync = isAsync;
	}

	/**
	 * @return the key_check_isChange
	 */
	public String getKey_check_isChange() {
		return key_check_isChange;
	}

	/**
	 * @param key_check_isChange
	 *            the key_check_isChange to set
	 */
	public void setKey_check_isChange(String key_check_isChange) {
		this.key_check_isChange = key_check_isChange;
	}

	/**
	 * @return the leftDetails
	 */
	public List<String> getLeftDetails() {
		return leftDetails;
	}

	/**
	 * @param leftDetails
	 *            the leftDetails to set
	 */
	public void setLeftDetails(List<String> leftDetails) {
		this.leftDetails = leftDetails;
	}

	/**
	 * @return the leftTicketStr
	 */
	public String getLeftTicketStr() {
		return leftTicketStr;
	}

	/**
	 * @param leftTicketStr
	 *            the leftTicketStr to set
	 */
	public void setLeftTicketStr(String leftTicketStr) {
		this.leftTicketStr = leftTicketStr;
	}

	/**
	 * @return the limitBuySeatTicketDTO
	 */
	public LimitBuySeatTicketDTO getLimitBuySeatTicketDTO() {
		return limitBuySeatTicketDTO;
	}

	/**
	 * @param limitBuySeatTicketDTO
	 *            the limitBuySeatTicketDTO to set
	 */
	public void setLimitBuySeatTicketDTO(LimitBuySeatTicketDTO limitBuySeatTicketDTO) {
		this.limitBuySeatTicketDTO = limitBuySeatTicketDTO;
	}

	/**
	 * @return the maxTicketNum
	 */
	public String getMaxTicketNum() {
		return maxTicketNum;
	}

	/**
	 * @param maxTicketNum
	 *            the maxTicketNum to set
	 */
	public void setMaxTicketNum(String maxTicketNum) {
		this.maxTicketNum = maxTicketNum;
	}

	/**
	 * @return the orderRequestDTO
	 */
	public OrderRequestDTO getOrderRequestDTO() {
		return orderRequestDTO;
	}

	/**
	 * @param orderRequestDTO
	 *            the orderRequestDTO to set
	 */
	public void setOrderRequestDTO(OrderRequestDTO orderRequestDTO) {
		this.orderRequestDTO = orderRequestDTO;
	}

	/**
	 * @return the purpose_codes
	 */
	public String getPurpose_codes() {
		return purpose_codes;
	}

	/**
	 * @param purpose_codes
	 *            the purpose_codes to set
	 */
	public void setPurpose_codes(String purpose_codes) {
		this.purpose_codes = purpose_codes;
	}

	/**
	 * @return the queryLeftNewDetailDTO
	 */
	public QueryLeftNewDetailDTO getQueryLeftNewDetailDTO() {
		return queryLeftNewDetailDTO;
	}

	/**
	 * @param queryLeftNewDetailDTO
	 *            the queryLeftNewDetailDTO to set
	 */
	public void setQueryLeftNewDetailDTO(QueryLeftNewDetailDTO queryLeftNewDetailDTO) {
		this.queryLeftNewDetailDTO = queryLeftNewDetailDTO;
	}

	/**
	 * @return the queryLeftTicketRequestDTO
	 */
	public QueryLeftTicketRequestDTO getQueryLeftTicketRequestDTO() {
		return queryLeftTicketRequestDTO;
	}

	/**
	 * @param queryLeftTicketRequestDTO
	 *            the queryLeftTicketRequestDTO to set
	 */
	public void setQueryLeftTicketRequestDTO(QueryLeftTicketRequestDTO queryLeftTicketRequestDTO) {
		this.queryLeftTicketRequestDTO = queryLeftTicketRequestDTO;
	}

	/**
	 * @return the tour_flag
	 */
	public String getTour_flag() {
		return tour_flag;
	}

	/**
	 * @param tour_flag
	 *            the tour_flag to set
	 */
	public void setTour_flag(String tour_flag) {
		this.tour_flag = tour_flag;
	}

	/**
	 * @return the train_location
	 */
	public String getTrain_location() {
		return train_location;
	}

	/**
	 * @param train_location
	 *            the train_location to set
	 */
	public void setTrain_location(String train_location) {
		this.train_location = train_location;
	}

}
