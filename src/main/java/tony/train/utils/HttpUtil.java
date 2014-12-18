package tony.train.utils;

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

public class HttpUtil {
	/**
	 * 
	 * @param <T>
	 * @param httpclient
	 * @param url
	 * @param header
	 * @param param
	 * @param handler
	 * @return
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 */
	public static <T> T doPostWithParam(CloseableHttpClient httpclient, String url, Map<String, String> header,
			LinkedHashMap<String, String> param, ResponseHandler<T> handler) throws IOException,
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
	 * @param <T>
	 * @param httpclient
	 * @param url
	 * @param handler
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static <T> T doPost(CloseableHttpClient httpclient, String url, ResponseHandler<T> handler)
			throws IOException, ClientProtocolException {
		HttpPost post = new HttpPost(url);

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
	public static CloseableHttpClient buildHttpClient() throws KeyStoreException, FileNotFoundException, IOException,
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

	/**
	 * 
	 * @param <T>
	 * @param httpclient
	 * @param url
	 * @param header
	 * @param handler
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static <T> T goGet(CloseableHttpClient httpclient, String url, Map<String, String> header,
			ResponseHandler<T> handler) throws IOException, ClientProtocolException {
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
}
