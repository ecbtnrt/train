package tony.train.cache;

import java.util.Map;
import java.util.Random;

import com.google.common.collect.Maps;

public class CacheServer {

	static Map<Integer, String> cacheServers = Maps.newHashMap();
	static {
		cacheServers.put(1, "https://220.168.132.56");
		cacheServers.put(2, "https://kyfw.12306.cn");
		cacheServers.put(3, "https://222.216.188.24");
		cacheServers.put(4, "https://112.84.105.49");
		cacheServers.put(5, "https://61.156.243.233");
		cacheServers.put(6, "https://123.125.92.23");
		cacheServers.put(7, "https://125.78.240.180");
		cacheServers.put(8, "https://183.57.84.91");
		cacheServers.put(9, "https://220.161.209.37");
		cacheServers.put(10, "https://122.70.142.143");
		cacheServers.put(11, "https://kyfw.12306.cn");
		cacheServers.put(12, "https://kyfw.12306.cn");
		cacheServers.put(13, "https://kyfw.12306.cn");
		cacheServers.put(14, "https://kyfw.12306.cn");
		cacheServers.put(15, "https://kyfw.12306.cn");
	}

	public static String getServer() {
		Random r = new Random(System.currentTimeMillis());
		int nextInt = r.nextInt(16);
		return cacheServers.get(nextInt);
	}
}
