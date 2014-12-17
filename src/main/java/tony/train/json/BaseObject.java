package tony.train.json;

import java.util.List;

public class BaseObject {
	String validateMessagesShowId;
	boolean status;
	int httpstatus;
	
	List<String> messages;
	ValidateMessages validateMessages;

	/**
	 * @return the validateMessagesShowId
	 */
	public String getValidateMessagesShowId() {
		return validateMessagesShowId;
	}

	/**
	 * @param validateMessagesShowId
	 *            the validateMessagesShowId to set
	 */
	public void setValidateMessagesShowId(String validateMessagesShowId) {
		this.validateMessagesShowId = validateMessagesShowId;
	}

	/**
	 * @return the status
	 */
	public boolean isStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(boolean status) {
		this.status = status;
	}

	/**
	 * @return the httpstatus
	 */
	public int getHttpstatus() {
		return httpstatus;
	}

	/**
	 * @param httpstatus
	 *            the httpstatus to set
	 */
	public void setHttpstatus(int httpstatus) {
		this.httpstatus = httpstatus;
	}

	/**
	 * @return the messages
	 */
	public List<String> getMessages() {
		return messages;
	}

	/**
	 * @param messages the messages to set
	 */
	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

	/**
	 * @return the validateMessages
	 */
	public ValidateMessages getValidateMessages() {
		return validateMessages;
	}

	/**
	 * @param validateMessages the validateMessages to set
	 */
	public void setValidateMessages(ValidateMessages validateMessages) {
		this.validateMessages = validateMessages;
	}
	

}
