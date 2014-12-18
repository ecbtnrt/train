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

import tony.train.utils.UUAPI;

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

			int code = rsp.hashCode();
			String filename = "f:/code" + code + ".png";

			File f = new File(filename);
			FileOutputStream fos = new FileOutputStream(f);
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = content.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
			}
			fos.flush();
			fos.close();
			content.close();

			// after file write completely
			retcode = getCheckCode(f.getAbsolutePath());
			
			File to = new File("f:/code/"+retcode + ".png");
			f.renameTo(to);
			f.delete();

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("解析验证码发生错误." + e.getMessage());
		} finally {
			response.close();
		}
		return retcode;
	}

	private String getCheckCode(String picPath) throws IOException {
		// ________________初始化接口类需要的参数，或者直接写到UUAPI。java文件里面________________

		boolean status = UUAPI.checkAPI(); // 校验API，必须调用一次，校验失败，打码不成功   

		if (!status) {
			System.out.print("API文件校验失败，无法使用打码服务");
			return "";
		}

		// ________________初始化参数结束，上面的操作只需要设置一次________________

		// 识别开始
		String result[] = UUAPI.easyDecaptcha(picPath, 1004);// picPath是图片路径,1004是codeType,http://www.uuwise.com/price.html

		if(result !=null && result.length ==2) {
			System.out.println("this img codeID:" + result[0]);
			System.out.println("return recongize Result:" + result[1]);
			
			return result[1];
		} else {
			System.err.println("UU code failed");
			return "";
		}
		
	}

}
