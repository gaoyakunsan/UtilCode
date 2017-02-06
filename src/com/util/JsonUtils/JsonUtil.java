package com.util.JsonUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

public class JsonUtil {
	
	private static final ObjectMapper objectMapper = new ObjectMapper();

	static {
		objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,false);
	}

	public static <T> List<T> parserJsonList(InputStream instream, Class<T> clsT) {
		List<T> list = new LinkedList<T>();
		try {
			JsonParser parser = objectMapper.getJsonFactory().createJsonParser(
					instream);

			JsonNode nodes = parser.readValueAsTree();

			for (JsonNode node : nodes) {
				list.add(objectMapper.readValue(node, clsT));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				instream.close();
			} catch (Exception ignore) {

			}
		}
		return list;
	}

	public static <T> List<T> parserJsonList(String str, Class<T> clsT) {
		List<T> list = new LinkedList<T>();
		try {
			JsonParser parser = objectMapper.getJsonFactory().createJsonParser(
					str);

			JsonNode nodes = parser.readValueAsTree();

			for (JsonNode node : nodes) {
				list.add(objectMapper.readValue(node, clsT));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return list;
	}

	public static <T> T parserJson(InputStream instream, Class<T> cls) {
		T t = null;
		try {
			JsonParser parser = objectMapper.getJsonFactory().createJsonParser(
					instream);
			t = objectMapper.readValue(parser, cls);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				instream.close();
			} catch (Exception ignore) {

			}
		}
		return t;
	}

	public static <T> T parserJson(String str, Class<T> cls) {
		T t = null;
		try {
			JsonParser parser = objectMapper.getJsonFactory().createJsonParser(
					str);
			t = objectMapper.readValue(parser, cls);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return t;
	}

	public static String getJsonFromObject(Object object) {
		String str = "";
		try {
			str = objectMapper.writeValueAsString(object);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str; 
	}
}
