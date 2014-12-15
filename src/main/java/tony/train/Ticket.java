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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

import tony.train.handler.CheckCodeHandler;
import tony.train.handler.LoginFormHandler;
import tony.train.handler.PrintResponseHandler;
import tony.train.utils.Base32;

import com.google.common.collect.Maps;

public class Ticket {

	private static final String root = "https://kyfw.12306.cn";

	private static final String login = "https://kyfw.12306.cn/otn/login/init";
	private static final String index = "https://kyfw.12306.cn/otn/";

	private static final String getCheckCode = "https://kyfw.12306.cn/otn/passcodeNew/getPassCodeNew?module=login&rand=sjrand&0.6583698091562837";

	private static final String checkcodeVerify = "https://kyfw.12306.cn/otn/passcodeNew/checkRandCodeAnsyn";

	private static final String preLoginURL = "https://kyfw.12306.cn/otn/login/loginAysnSuggest";
	private static final String LoginURL = "https://kyfw.12306.cn/otn/login/userLogin";

	private static final String indexInit = "https://kyfw.12306.cn/otn/index/init";

	public final static void main(String[] args) throws Exception {
		CloseableHttpClient httpclient = buildHttpClient();

		PrintResponseHandler handler = new PrintResponseHandler();
		LoginFormHandler loginHandler = new LoginFormHandler();
		try {
			goGet(httpclient, index, HttpHeader.index(), handler);

			// get dynamicJS URL
			String dynamicJsUrl = goGet(httpclient, login, HttpHeader.loginInitHearder(), loginHandler);

			dynamicJsUrl = root + dynamicJsUrl;

			String jsContent = doPost(httpclient, dynamicJsUrl, handler);

			Pattern p = Pattern.compile("var(\\s*)key(\\s*)=(\\s*)'(\\w*)';");
			Matcher m = p.matcher(jsContent);
			String key = "";
			String value = "1111";
			if (m.find()) {
				key = m.group();
				key = key.split("=")[1];
				key = key.replace("[ ]*", "").replace("'", "").replace(";", "");
			}

			String check_code = goGet(httpclient, getCheckCode, HttpHeader.getCheckCode(true), new CheckCodeHandler());

			LinkedHashMap<String, String> param = Maps.newLinkedHashMap();
			param.put("randCode", check_code);
			param.put("rand", "sjrand");
			param.put("randCode_validate", "");

			doPostWithParam(httpclient, checkcodeVerify, HttpHeader.postCheckCode(true), param,
					new ResponseHandler<String>() {

						public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
							try {
								HttpEntity entity = response.getEntity();

								System.out.println("Login form get: " + response.getStatusLine());

								String str = EntityUtils.toString(entity);
								if (null != str && str.contains("randCodeRight")) {
									System.out.println("verify success");
									return "Y";
								} else {
									System.err.println("验证码错误!");
									return "N";
								}
							} finally {
								((CloseableHttpResponse) response).close();
							}
						}
					});

			// pre login
			HttpUriRequest preLogin = RequestBuilder.post().setUri(new URI(preLoginURL))
					.addParameter("loginUserDTO.user_name", "").addParameter("userDTO.password", "")
					.addParameter("randCode", check_code).addParameter("randCode_validate", "")
					.addParameter(key, Base32.calculateParam(value, key)).addParameter("myversion", "undefined")
					.build();

			CloseableHttpResponse rsp3 = httpclient.execute(preLogin);
			System.out.println(EntityUtils.toString(rsp3.getEntity()));
			rsp3 = httpclient.execute(preLogin);
			System.out.println(EntityUtils.toString(rsp3.getEntity()));

			rsp3.close();

			HttpUriRequest loginReq = RequestBuilder.post().setUri(new URI(LoginURL)).addParameter("_json_att", "")
					.build();

			CloseableHttpResponse loginRsp = httpclient.execute(loginReq);
			System.out.println(EntityUtils.toString(loginRsp.getEntity()));

			loginRsp.close();

			System.out.println(goGet(httpclient, indexInit,HttpHeader.index(), handler));
			
			
			

		} finally {
			httpclient.close();
		}
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
	private static String doPost(CloseableHttpClient httpclient, String url, ResponseHandler<String> handler)
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
