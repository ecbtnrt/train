package tony.train;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;

import tony.train.cache.CacheServer;
import tony.train.handler.CheckCodeHandler;
import tony.train.handler.PrintResponseHandler;
import tony.train.handler.VerifyCheckCodeHandler;
import tony.train.json.LoginResonse;
import tony.train.json.QueryLeftNewDTO;
import tony.train.json.TicketDTO;
import tony.train.json.TicketInfo;
import tony.train.utils.HttpUtil;
import tony.train.utils.PropertiesUtil;
import tony.train.worker.WorkThread;

import com.google.common.collect.Maps;

public class Ticket {

	public static final String delimeter = ",";

	public static final String root = "https://kyfw.12306.cn";

	public static String value = "1111";

	private static final String index = "https://kyfw.12306.cn/otn/";

	private static final String getCheckCode = "https://kyfw.12306.cn/otn/passcodeNew/getPassCodeNew?module=login&rand=sjrand&0.6583698091562837";

	private static final String checkcodeVerify = "https://kyfw.12306.cn/otn/passcodeNew/checkRandCodeAnsyn";

	private static final String preLoginURL = "https://kyfw.12306.cn/otn/login/loginAysnSuggest";

	private static final String LoginURL = "https://kyfw.12306.cn/otn/login/userLogin";

	public final static ObjectMapper mapper = new ObjectMapper();

	public final static void main(String[] args) throws Exception {
		final CloseableHttpClient httpclient = HttpUtil.buildHttpClient();

		String trainCode = PropertiesUtil.getValue("trainCode");
		/**
		 * get all favorite train
		 */
		final List<String> trains = Arrays.asList(trainCode.split(delimeter));

		ExecutorService threadPool = Executors.newFixedThreadPool(10);
		try {

			login(httpclient);

			Map<String, TicketDTO> queryLeftTicket = queryLeftTicket(httpclient, trains);

			for (String key : queryLeftTicket.keySet()) {
				WorkThread worker = new WorkThread(httpclient, queryLeftTicket.get(key));

				threadPool.execute(worker);
			}

		} catch (Exception e) {

		} finally {
			httpclient.close();
		}
	}

	private static Map<String, TicketDTO> queryLeftTicket(final CloseableHttpClient httpclient,
			final List<String> trains) throws IOException, ClientProtocolException {
		StringBuilder sb = new StringBuilder();
		StringBuilder log = new StringBuilder();
		// host
		// TODO : use different server to get latest data
		sb.append(CacheServer.getServer());
		log.append(CacheServer.getServer());
		
		sb.append("otn/leftTicket/queryT?");
		log.append("otn/leftTicket/log?");
		
		StringBuilder common = new StringBuilder();
		common.append("leftTicketDTO.train_date=");
		common.append(PropertiesUtil.getValue("start_date"));

		common.append("&leftTicketDTO.from_station=");
		common.append(PropertiesUtil.getValue("start_station"));

		common.append("&leftTicketDTO.to_station=");
		common.append(PropertiesUtil.getValue("to_station"));

		common.append("&purpose_codes=ADULT");
		
		sb.append(common.toString());
		log.append(common.toString());
		
		String logUrl =log.toString();

		HttpUtil.goGet(httpclient, logUrl, HttpHeader.commonHeader(), new PrintResponseHandler());
		
		String ticketQueryUrl = sb.toString();

		final Map<String, TicketDTO> trainsecretStrMap = Maps.newLinkedHashMap();

		do {
			HttpUtil.goGet(httpclient, ticketQueryUrl, HttpHeader.commonHeader(), new ResponseHandler<String>() {

				public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
					String jsonResponse = EntityUtils.toString(response.getEntity());

					TicketInfo ticketInfo = mapper.readValue(jsonResponse, TicketInfo.class);

					if (ticketInfo != null && ticketInfo.getHttpstatus() == 200 && ticketInfo.isStatus()) {

						if (null != ticketInfo.getData() && !ticketInfo.getData().isEmpty()) {
							List<TicketDTO> data = ticketInfo.getData();

							for (TicketDTO ticketDTO : data) {
								QueryLeftNewDTO queryLeftNewDTO = ticketDTO.getQueryLeftNewDTO();
								String trainCode = queryLeftNewDTO.getStation_train_code();
								// if ("Z92".equals(trainCode)) {
								if (trains.contains(trainCode)) {

									if (isAvailiable(queryLeftNewDTO.getYw_num())) {
										ticketDTO.setSeatType("3");
									} else if (isAvailiable(queryLeftNewDTO.getYz_num())) {
										ticketDTO.setSeatType("1");
									} else if (isAvailiable(queryLeftNewDTO.getRw_num())) {
										ticketDTO.setSeatType("4");
									} else if (isAvailiable(queryLeftNewDTO.getZe_num())) {
										ticketDTO.setSeatType("O");
									} else if (isAvailiable(queryLeftNewDTO.getZy_num())) {
										ticketDTO.setSeatType("M");
									}

									trainsecretStrMap.put(trainCode, ticketDTO);
								}
							}
						}
					}
					return null;
				}
			});

			try {
				Thread.sleep(PropertiesUtil.getIntValue("refresh", 1000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		} while (trainsecretStrMap.isEmpty());

		return trainsecretStrMap;
	}

	/**
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isAvailiable(String str) {
		if (StringUtils.isNotBlank(str)) {
			try {
				int count = Integer.parseInt(str);
				return count > 0;
			} catch (Exception e) {

			}
		}
		return false;
	}

	/**
	 * 
	 * @param httpclient
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static boolean login(CloseableHttpClient httpclient) throws ClientProtocolException, IOException,
			URISyntaxException {
		PrintResponseHandler handler = new PrintResponseHandler();
		// DynamicJsHandler dynamicJshandler = new DynamicJsHandler(httpclient);

		boolean islogin = false;

		do {
			// get index page
			HttpUtil.goGet(httpclient, index, HttpHeader.index(), handler);

			boolean checkCodeVerifyResult = false;
			String check_code = "";
			do {
				check_code = HttpUtil.goGet(httpclient, getCheckCode, HttpHeader.getCheckCode(true),
						new CheckCodeHandler());
				System.out.println("验证码为：" + check_code);

				LinkedHashMap<String, String> param = Maps.newLinkedHashMap();
				param.put("randCode", check_code);
				param.put("rand", "sjrand");
				param.put("randCode_validate", "");

				checkCodeVerifyResult = HttpUtil.doPostWithParam(httpclient, checkcodeVerify,
						HttpHeader.postCheckCode(true), param, new VerifyCheckCodeHandler());

				if (!checkCodeVerifyResult) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			} while (!checkCodeVerifyResult);

			System.out.println("开始登陆...");
			HttpUriRequest preLogin = RequestBuilder.post().setUri(new URI(preLoginURL))
					.addParameter("loginUserDTO.user_name", PropertiesUtil.getValue("username"))
					.addParameter("userDTO.password", PropertiesUtil.getValue("pwd"))
					.addParameter("randCode", check_code).addParameter("randCode_validate", "")
					// .addParameter(key, Base32.calculateParam(value,key))
					.addParameter("myversion", "undefined")
					.build();

			CloseableHttpResponse response = httpclient.execute(preLogin);
			String loginRsp = EntityUtils.toString(response.getEntity());
			// {"validateMessagesShowId":"_validatorMessage","status":true,"httpstatus":200,"data":{"loginCheck":"Y"},"messages":[],"validateMessages":{}}

			System.out.println(loginRsp);

			ObjectMapper mapper = new ObjectMapper();
			LoginResonse loginResponse = mapper.readValue(loginRsp, LoginResonse.class);

			if (null != loginResponse && "Y".equalsIgnoreCase(loginResponse.getData().getLoginCheck())) {
				// System.out.println("登陆成功!");

				// final step
				HttpUriRequest loginReq = RequestBuilder.post().setUri(new URI(LoginURL)).addParameter("_json_att", "")
						.build();

				CloseableHttpResponse r = httpclient.execute(loginReq);
				System.out.println(EntityUtils.toString(r.getEntity()));

				r.close();

				islogin = true;
			}

			if (!islogin) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		} while (!islogin);

		System.out.println("登陆成功...");

		return true;
	}

}
