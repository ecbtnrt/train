package tony.train.Maps;

import java.util.Map;

import com.google.common.collect.Maps;

public class Station {

	static Map<String, String> stationMap = Maps.newHashMap();

	static Map<String, String> stationMap_ = Maps.newHashMap();

	static {
		stationMap.put("SHH", "上海");
		stationMap.put("XAY", "西安");
		stationMap.put("WHN", "武汉");

		for (Map.Entry<String, String> entry : stationMap.entrySet()) {
			stationMap_.put(entry.getValue(), entry.getKey());
		}
	}

	public static String getName(String code) {
		return stationMap.get(code);
	}

	public static String getCode(String name) {
		return stationMap_.get(name);
	}

}
