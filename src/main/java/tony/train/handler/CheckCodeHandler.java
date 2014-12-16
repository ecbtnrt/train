package tony.train.handler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;

public class CheckCodeHandler implements ResponseHandler<String> {

	public String handleResponse(HttpResponse rsp) throws ClientProtocolException, IOException {

		CloseableHttpResponse response = (CloseableHttpResponse) rsp;
		String retcode = "";
		try {
			HttpEntity entity = response.getEntity();

			System.out.println("----------------------------------------");
			System.out.println(response.getStatusLine());
			if (entity != null) {
				System.out.println("Response content length: " + entity.getContentLength());
			}

			InputStream content = entity.getContent();


			// TODO random filename
			File code = new File("F:/code.png");
			FileOutputStream fos = new FileOutputStream(code);
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = content.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
			}
			fos.flush();
			fos.close();
			content.close();
			
			//TODO use UU cloud OCR
			retcode = "";

		} finally {
			response.close();
		}
		return retcode;
	}

}
