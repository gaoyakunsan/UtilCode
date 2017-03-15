package com.util.fileUtil;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
/**
 * http 工具
 * @author yakungao
 *
 */
public class HttpUtil {
	
	/**
	 * http上传文件，并在url中附带参数
	 */
	public static void httpUploadFile(){
		HttpClient client = new HttpClient();
		
		String nameParam = "";
		String fileMd5 = "";
		String url = "";
		
		PostMethod myPost = new PostMethod(url);
		
		try {
			//logger.debug("upload ipk:" + postUrl);
			StringPart fp = new StringPart("data", "需要上传的文件");
			StringPart name = new StringPart("name", nameParam);
			StringPart md5 = new StringPart("md5", fileMd5);
			Part[] parts = { name, md5, fp };
			MultipartRequestEntity mre = new MultipartRequestEntity(parts,
					myPost.getParams());
			myPost.setRequestEntity(mre);
			int statusCode = client.executeMethod(myPost);
			//logger.debug("status code" + statusCode);
			if (statusCode == HttpStatus.SC_OK) {
				//isUploaded = true;
			} else if (statusCode == 412) {
				//errorMeg = "Verify md5 failed! rsponse code=>";
				//logger.debug(errorMeg);
			} else {
				//errorMeg = "upload failed! rsponse code=>" + statusCode;
				//logger.debug("upload failed! rsponse code=>" + statusCode);
			}
		} catch (Exception e) {
			//logger.error("upload ipk exception: ", e);
			//errorMeg = "upload ipk file exception";
		} finally {
			try {
				myPost.releaseConnection();
				client.getHttpConnectionManager().closeIdleConnections(0);
			} catch (Exception e) {
				//logger.error("",e);
				e.printStackTrace();
			}
			
		}
	}
}
