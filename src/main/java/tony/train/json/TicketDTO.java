package tony.train.json;

public class TicketDTO {

	QueryLeftNewDTO queryLeftNewDTO;
	String secretStr;
	String buttonTextInfo;

	/**
	 * @return the queryLeftNewDTO
	 */
	public QueryLeftNewDTO getQueryLeftNewDTO() {
		return queryLeftNewDTO;
	}

	/**
	 * @param queryLeftNewDTO
	 *            the queryLeftNewDTO to set
	 */
	public void setQueryLeftNewDTO(QueryLeftNewDTO queryLeftNewDTO) {
		this.queryLeftNewDTO = queryLeftNewDTO;
	}

	/**
	 * @return the secretStr
	 */
	public String getSecretStr() {
		return secretStr;
	}

	/**
	 * @param secretStr
	 *            the secretStr to set
	 */
	public void setSecretStr(String secretStr) {
		this.secretStr = secretStr;
	}

	/**
	 * @return the buttonTextInfo
	 */
	public String getButtonTextInfo() {
		return buttonTextInfo;
	}

	/**
	 * @param buttonTextInfo
	 *            the buttonTextInfo to set
	 */
	public void setButtonTextInfo(String buttonTextInfo) {
		this.buttonTextInfo = buttonTextInfo;
	}


}
