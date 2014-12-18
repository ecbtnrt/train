package tony.train.json.order;

public class StationTrainDTO {
	TrainDTO trainDTO;
	String station_train_code;
	String from_station_telecode;
	String from_station_name;
	String start_time;
	String to_station_telecode;
	String to_station_name;
	String arrive_time;
	String distance;

	/**
	 * @return the trainDTO
	 */
	public TrainDTO getTrainDTO() {
		return trainDTO;
	}

	/**
	 * @param trainDTO
	 *            the trainDTO to set
	 */
	public void setTrainDTO(TrainDTO trainDTO) {
		this.trainDTO = trainDTO;
	}

	/**
	 * @return the station_train_code
	 */
	public String getStation_train_code() {
		return station_train_code;
	}

	/**
	 * @param station_train_code
	 *            the station_train_code to set
	 */
	public void setStation_train_code(String station_train_code) {
		this.station_train_code = station_train_code;
	}

	/**
	 * @return the from_station_telecode
	 */
	public String getFrom_station_telecode() {
		return from_station_telecode;
	}

	/**
	 * @param from_station_telecode
	 *            the from_station_telecode to set
	 */
	public void setFrom_station_telecode(String from_station_telecode) {
		this.from_station_telecode = from_station_telecode;
	}

	/**
	 * @return the from_station_name
	 */
	public String getFrom_station_name() {
		return from_station_name;
	}

	/**
	 * @param from_station_name
	 *            the from_station_name to set
	 */
	public void setFrom_station_name(String from_station_name) {
		this.from_station_name = from_station_name;
	}

	/**
	 * @return the start_time
	 */
	public String getStart_time() {
		return start_time;
	}

	/**
	 * @param start_time
	 *            the start_time to set
	 */
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	/**
	 * @return the to_station_telecode
	 */
	public String getTo_station_telecode() {
		return to_station_telecode;
	}

	/**
	 * @param to_station_telecode
	 *            the to_station_telecode to set
	 */
	public void setTo_station_telecode(String to_station_telecode) {
		this.to_station_telecode = to_station_telecode;
	}

	/**
	 * @return the to_station_name
	 */
	public String getTo_station_name() {
		return to_station_name;
	}

	/**
	 * @param to_station_name
	 *            the to_station_name to set
	 */
	public void setTo_station_name(String to_station_name) {
		this.to_station_name = to_station_name;
	}

	/**
	 * @return the arrive_time
	 */
	public String getArrive_time() {
		return arrive_time;
	}

	/**
	 * @param arrive_time
	 *            the arrive_time to set
	 */
	public void setArrive_time(String arrive_time) {
		this.arrive_time = arrive_time;
	}

	/**
	 * @return the distance
	 */
	public String getDistance() {
		return distance;
	}

	/**
	 * @param distance
	 *            the distance to set
	 */
	public void setDistance(String distance) {
		this.distance = distance;
	}

}

class TrainDTO {

}