package tony.train.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

public class PropertiesUtil {

	private static Properties p = new Properties();

	static {
		try {
			InputStream ins = PropertiesUtil.class.getClassLoader().getResourceAsStream("u.properties");
			p.load(ins);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getValue(String key) {
		String value = p.getProperty(key);

		if (StringUtils.isNotBlank(value)) {
			return StringUtils.trim(value);
		} else {
			return "";
		}
	}

	public static int getIntValue(String key, int defaultValue) {
		String value = getValue(key);
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	public static void main(String[] args) {
		// System.out.println(PropertiesUtil.getValue("pwd"));
	}
}
