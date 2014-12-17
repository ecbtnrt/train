package tony.train;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;

import tony.train.handler.CheckCodeHandler;
import tony.train.handler.DynamicJsHandler;
import tony.train.handler.PrintResponseHandler;
import tony.train.json.LoginResonse;
import tony.train.json.QueryLeftNewDTO;
import tony.train.json.TicketDTO;
import tony.train.json.TicketInfo;
import tony.train.utils.PropertiesUtil;

import com.google.common.collect.Maps;

public class Ticket {

	public static final String root = "https://kyfw.12306.cn";

	public static String value = "1111";

	private static final String login = "https://kyfw.12306.cn/otn/login/init";
	private static final String index = "https://kyfw.12306.cn/otn/";

	private static final String getCheckCode = "https://kyfw.12306.cn/otn/passcodeNew/getPassCodeNew?module=login&rand=sjrand&0.6583698091562837";

	private static final String checkcodeVerify = "https://kyfw.12306.cn/otn/passcodeNew/checkRandCodeAnsyn";

	private static final String preLoginURL = "https://kyfw.12306.cn/otn/login/loginAysnSuggest";
	private static final String LoginURL = "https://kyfw.12306.cn/otn/login/userLogin";

	// private static final String indexInit = "https://kyfw.12306.cn/otn/index/init";

	public final static void main(String[] args) throws Exception {
		final CloseableHttpClient httpclient = buildHttpClient();

		final DynamicJsHandler dynamicJshandler = new DynamicJsHandler(httpclient);
		try {

			login(httpclient);

			// System.out.println(goGet(httpclient, indexInit, HttpHeader.index(), handler));

			// left ticket

			//String leftTicketUrl = "https://kyfw.12306.cn/otn/leftTicket/init";

			// key = goGet(httpclient, leftTicketUrl, HttpHeader.commonHeader(),
			// dynamicJshandler);

			StringBuilder sb = new StringBuilder();
			// host
			// TODO : use different server to get latest data
			sb.append(root);
			sb.append("otn/leftTicket/queryT?");
			sb.append("leftTicketDTO.train_date=");
			
			
			// https://kyfw.12306.cn/otn/leftTicket/queryT?leftTicketDTO.train_date=2015-02-13&leftTicketDTO.from_station=SHH&leftTicketDTO.to_station=XAY&purpose_codes=ADULT
			
			
			
			String ticketQueryUrl = "https://kyfw.12306.cn/otn/leftTicket/queryT?leftTicketDTO.train_date=2015-02-12&leftTicketDTO.from_station=SHH&leftTicketDTO.to_station=JMN&purpose_codes=ADULT";

			String secretStr = goGet(httpclient, ticketQueryUrl, HttpHeader.commonHeader(),
					new ResponseHandler<String>() {

						public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
							ObjectMapper mapper = new ObjectMapper();

							String jsonResponse = EntityUtils.toString(response.getEntity());

							TicketInfo ticketInfo = mapper.readValue(jsonResponse, TicketInfo.class);

							if (ticketInfo != null && ticketInfo.getHttpstatus() == 200 && ticketInfo.isStatus()) {

								if (null != ticketInfo.getData() && !ticketInfo.getData().isEmpty()) {
									// TODO
									TicketDTO ticketDTO = ticketInfo.getData().get(0);
									QueryLeftNewDTO queryLeftNewDTO = ticketDTO.getQueryLeftNewDTO();
									String trainCode = queryLeftNewDTO.getStation_train_code();
									if ("K253".equals(trainCode)) {

										return ticketDTO.getSecretStr();

									}
								}
							}

							return null;
						}

					});

			// // submit order
			String submitOrderUrl = "https://kyfw.12306.cn/otn/leftTicket/submitOrderRequest";

			LinkedHashMap<String, String> orderParam = Maps.newLinkedHashMap();
			// orderParam.put(key, Base32.calculateParam(value, key));
			orderParam.put("myversion", "undefined");
			orderParam.put("secretStr", secretStr);
			orderParam.put("train_date", "2015-01-28");
			orderParam.put("back_train_date", "2014-12-15");
			orderParam.put("tour_flag", "dc");
			orderParam.put("purpose_codes", "ADULT");
			orderParam.put("query_from_station_name", "上海");
			orderParam.put("query_to_station_name", "武汉");

			// post

			// order
			String orderInit = "https://kyfw.12306.cn/otn/confirmPassenger/initDc";
			String key1 = goGet(httpclient, login, HttpHeader.loginInitHearder(), dynamicJshandler);

		} finally {
			httpclient.close();
		}
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
			goGet(httpclient, index, HttpHeader.index(), handler);

			// get dynamicJS URL
			// String key = goGet(httpclient, login,
			// HttpHeader.loginInitHearder(), dynamicJshandler);
			String checkCodeVerifyResult = "N";
			String check_code = "";
			do {
				check_code = goGet(httpclient, getCheckCode, HttpHeader.getCheckCode(true), new CheckCodeHandler());
				System.out.println("验证码为：" + check_code);

				LinkedHashMap<String, String> param = Maps.newLinkedHashMap();
				param.put("randCode", check_code);
				param.put("rand", "sjrand");
				param.put("randCode_validate", "");

				checkCodeVerifyResult = doPostWithParam(httpclient, checkcodeVerify, HttpHeader.postCheckCode(true),
						param, new ResponseHandler<String>() {

							public String handleResponse(HttpResponse response) throws ClientProtocolException,
									IOException {
								try {
									HttpEntity entity = response.getEntity();

									System.out.println("Login form get: " + response.getStatusLine());

									String str = EntityUtils.toString(entity);
									if (null != str && str.contains("randCodeRight")) {
										System.out.println("登陆验证码正确,准备开始登陆...");
										return "Y";
									} else {
										System.err.println("验证码错误,重新获取验证码...");
										return "N";
									}
								} finally {
									((CloseableHttpResponse) response).close();
								}
							}
						});

			} while ("N".equals(checkCodeVerifyResult));

			System.out.println("开始登陆...");
			HttpUriRequest preLogin = RequestBuilder.post().setUri(new URI(preLoginURL))
					.addParameter("loginUserDTO.user_name", PropertiesUtil.getValue("username"))
					.addParameter("userDTO.password", PropertiesUtil.getValue("pwd"))
					.addParameter("randCode", check_code).addParameter("randCode_validate", "")
					// .addParameter(key, Base32.calculateParam(value,key))
					// .addParameter("myversion", "undefined")
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

		} while (!islogin);

		System.out.println("登陆成功...");

		return true;
	}

	/**
	 * 
	 * @param httpclient
	 * @param url
	 * @param header
	 * @param handler
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	private static String goGet(CloseableHttpClient httpclient, String url, Map<String, String> header,
			ResponseHandler<String> handler) throws IOException, ClientProtocolException {
		HttpGet httpget = new HttpGet(url);
		if (null != header && !header.isEmpty()) {
			for (Map.Entry<String, String> entry : header.entrySet()) {
				httpget.addHeader(entry.getKey(), entry.getValue());
			}
		}

		System.out.println("executing request" + httpget.getRequestLine());

		CloseableHttpResponse response = httpclient.execute(httpget);

		return handler.handleResponse(response);
	}

	/**
	 * 
	 * @param httpclient
	 * @param url
	 * @param handler
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static String doPost(CloseableHttpClient httpclient, String url, ResponseHandler<String> handler)
			throws IOException, ClientProtocolException {
		HttpPost post = new HttpPost(url);

		System.out.println("executing request" + post.getRequestLine());

		CloseableHttpResponse response = httpclient.execute(post);

		return handler.handleResponse(response);
	}

	/**
	 * 
	 * @param httpclient
	 * @param url
	 * @param header
	 * @param param
	 * @param handler
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 */
	public static String doPostWithParam(CloseableHttpClient httpclient, String url, Map<String, String> header,
			LinkedHashMap<String, String> param, ResponseHandler<String> handler) throws IOException,
			ClientProtocolException, URISyntaxException {

		RequestBuilder builder = RequestBuilder.post().setUri(new URI(url));

		if (null != header && !header.isEmpty()) {
			for (Map.Entry<String, String> entry : header.entrySet()) {
				builder.addHeader(entry.getKey(), entry.getValue());
			}
		}

		if (null != param && !param.isEmpty()) {
			Iterator<String> iterator = param.keySet().iterator();
			while (iterator.hasNext()) {
				String key = iterator.next();
				builder.addParameter(key, param.get(key));
			}
		}

		HttpUriRequest post = builder.build();

		System.out.println("executing request" + post.getRequestLine());

		CloseableHttpResponse response = httpclient.execute(post);

		return handler.handleResponse(response);
	}

	/**
	 * 
	 * @return
	 * @throws KeyStoreException
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws CertificateException
	 * @throws KeyManagementException
	 */
	private static CloseableHttpClient buildHttpClient() throws KeyStoreException, FileNotFoundException, IOException,
			NoSuchAlgorithmException, CertificateException, KeyManagementException {
		KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
		FileInputStream instream = new FileInputStream(new File("f:/train.keystore"));
		try {
			trustStore.load(instream, "123456".toCharArray());
		} finally {
			instream.close();
		}

		// Trust own CA and all self-signed certs
		SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(trustStore, new TrustSelfSignedStrategy())
				.build();
		// Allow https protocol only
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null,
				SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
		BasicCookieStore cookieStore = new BasicCookieStore();

		CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf)
				.setDefaultCookieStore(cookieStore).build();

		return httpclient;
	}

}
