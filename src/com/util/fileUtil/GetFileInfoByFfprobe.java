package com.util.fileUtil;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import net.sf.json.JSONObject;

public class GetFileInfoByFfprobe {

	public static void getMediaInfo(){
		
		String cmdStr = "D:\\anzhuang\\ffmpeg\\bin\\ffprobe.exe -v quiet -print_format json -show_format -show_streams -i D:\\filme\\test.mp4";
		
		int height = 0;
		int width = 0;
		int bitrate = 0;
		long size = 0l;
		int duration = 0;
		
		
		Runtime run = Runtime.getRuntime();
		Process pres = null;
		try {
			pres = run.exec(cmdStr);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		BufferedInputStream buf = new BufferedInputStream(pres.getInputStream());
		BufferedReader br = new BufferedReader(new InputStreamReader(buf));
		try {
			
			StringBuffer sb = new StringBuffer();
			String lineStr;
			
			while((lineStr = br.readLine()) != null){
				sb.append(lineStr);
			}
			if(pres.waitFor() != 0){
				if(pres.exitValue() == 1){
					System.out.println("命令执行错误！");
				}
			}
			buf.close();
			br.close();
			JSONObject obj = JSONObject.fromObject(sb.toString());
			@SuppressWarnings("unchecked")
			List<JSONObject> list = (List<JSONObject>)obj.get("streams");
			for(JSONObject jsObj: list){
				if("video".equals(jsObj.get("codec_type"))){
					if(jsObj.containsKey("width")){
						width = (int)jsObj.get("width");
					}
					if(jsObj.containsKey("height")){
						height = (int)jsObj.get("height");
					}
					if(jsObj.containsKey("bit_rate")){
						bitrate = (Integer.valueOf((String) jsObj.get("bit_rate")))/1024;
					}
					if(jsObj.containsKey("duration")){
						String durationStr =  jsObj.get("duration").toString();
						if(durationStr.contains(".")){
							duration = Integer.valueOf(durationStr.substring(0, durationStr.indexOf(".")));
						}
					}
					break;
				}
			}
			JSONObject jsObj= (JSONObject)obj.get("format");
			if(bitrate == 0){
				if(jsObj.containsKey("bitrate")){
					bitrate = (Integer.valueOf((String) jsObj.get("bit_rate")))/1024;
				}
			}
			if(duration == 0){
				if(jsObj.containsKey("duration")){
					String durationStr =  jsObj.get("duration").toString();
					if(durationStr.contains(".")){
						duration = Integer.valueOf(durationStr.substring(0, durationStr.indexOf(".")));
					}
				}
			}
			size = Integer.valueOf((String)jsObj.get("size"));
			
			System.out.println(sb.toString());
			System.out.println();
			System.out.println();
			System.out.println();
			System.out.println();
			System.out.println("height:" + height + "****" + "width:" + width + "****" + "bitrate:" + bitrate + "****" +  "duration:" + duration + "****" + "size:" + size);
			
		} catch (IOException e) {
			e.printStackTrace();
			try {
				buf.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				br.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				buf.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				br.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	public static void main(String args[]){
		GetFileInfoByFfprobe.getMediaInfo();
	}
	
	
}
