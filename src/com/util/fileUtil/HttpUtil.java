package com.util.fileUtil;

import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * http 工具
 * 
 * @author yakungao
 *
 */
public class HttpUtil {

	/**
	 * http上传文件，并在url中附带参数
	 */
	public static void httpUploadFile() {
		HttpClient client = new HttpClient();

		String nameParam = "";
		String fileMd5 = "";
		String url = "";

		PostMethod myPost = new PostMethod(url);

		try {
			// logger.debug("upload ipk:" + postUrl);
			StringPart fp = new StringPart("data", "需要上传的文件");
			StringPart name = new StringPart("name", nameParam);
			StringPart md5 = new StringPart("md5", fileMd5);
			Part[] parts = { name, md5, fp };
			MultipartRequestEntity mre = new MultipartRequestEntity(parts, myPost.getParams());
			myPost.setRequestEntity(mre);
			int statusCode = client.executeMethod(myPost);
			// logger.debug("status code" + statusCode);
			if (statusCode == HttpStatus.SC_OK) {
				// isUploaded = true;
			} else if (statusCode == 412) {
				// errorMeg = "Verify md5 failed! rsponse code=>";
				// logger.debug(errorMeg);
			} else {
				// errorMeg = "upload failed! rsponse code=>" + statusCode;
				// logger.debug("upload failed! rsponse code=>" + statusCode);
			}
		} catch (Exception e) {
			// logger.error("upload ipk exception: ", e);
			// errorMeg = "upload ipk file exception";
		} finally {
			try {
				myPost.releaseConnection();
				client.getHttpConnectionManager().closeIdleConnections(0);
			} catch (Exception e) {
				// logger.error("",e);
				e.printStackTrace();
			}

		}
	}

	public static String postJson(String url, String body) {

		// logger.info("begin execute postJson:{}", url);

		String result = "";
		// 创建httpPost远程连接实例
		HttpPost httpPost = new HttpPost(url);
		// 配置请求参数实例
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000)// 设置连接主机服务超时时间
				.setConnectionRequestTimeout(35000)// 设置连接请求超时时间
				.setSocketTimeout(60000)// 设置读取数据连接超时时间
				.build();
		// 为httpPost实例设置配置
		httpPost.setConfig(requestConfig);
		httpPost.addHeader("content-type", "application/json;charset=utf-8");
		httpPost.addHeader("accept", "application/json");
		httpPost.setEntity(new StringEntity(body, Charset.forName("utf-8")));

		CloseableHttpClient httpClient = null;
		CloseableHttpResponse httpResponse = null;
		try {
			httpClient = HttpClients.createDefault();
			httpResponse = httpClient.execute(httpPost);
			int code = httpResponse.getStatusLine().getStatusCode();
			if (HttpStatus.SC_OK == code) {
				// 从响应对象中获取响应内容
				HttpEntity entity = httpResponse.getEntity();
				result = EntityUtils.toString(entity);
			} else {
				// logger.error("postJson error, response code:{} , url:{}", code, url);
			}
		} catch (IOException e) {
			// logger.error("postJson error url:{}, {}", url, e.toString());
		} finally {

			if (httpClient != null) {
				try {
					httpClient.close();
				} catch (IOException e) {
				}
			}
			if (httpResponse != null) {
				try {
					httpResponse.close();
				} catch (IOException e) {
				}
			}
			if (httpPost != null) {
				httpPost.releaseConnection();
			}

		}
		// logger.info("postJsonurl:{}, and response:{}", url, result);
		return result;
	}
}
