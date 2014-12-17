package tony.train.json;

public class LoginResonse extends BaseObject {

	LoginResult data;

	/**
	 * @return the data
	 */
	public LoginResult getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(LoginResult data) {
		this.data = data;
	}

}
