package com.lib.utils;

import java.io.File;
import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

/**
 * ClassName:JsonOjbectMapper <br/>
 * Function: TODO (JSON快速读取). <br/>
 * Reason: TODO (). <br/>
 * Date: 2014-4-22 下午4:09:04 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public class JsonObjectMapper {
	private final static ObjectMapper mapper = new ObjectMapper();
	
	/**
	 * readMapperObject:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author lyh 文件读取JSON
	 * @param fileName
	 * @param c
	 * @return
	 */
	public static <T> T readMapperObject(String fileName, Class<T> c) {
		T map = null;
		try {
			map = mapper.readValue(new File(fileName), c);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * readMapperObject:(). <br/>
	 * TODO().<br/>
	 * 转化有集合类型的json
	 * 
	 * @author lyh
	 * @param fileName
	 * @param jt
	 * @return
	 */
	public static <T> T readMapperObject(String fileName, JavaType jt) {
		T map = null;
		try {
			
			map = mapper.readValue(new File(fileName), jt);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * readMapperObjectByContent:(). <br/>
	 * TODO().<br/>
	 * 通过读取内容,变为对象
	 * 
	 * @author lyh
	 * @param content
	 * @param c
	 * @return
	 */
	public static <T> T readMapperObjectByContent(String content, Class<T> c) {
		T map = null;
		try {
			map = mapper.readValue(content, c);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * readMapperObject:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author lyh 文件读取JSON
	 * @param file 文件
	 * @param c
	 * @return
	 */
	public static <T> T readMapperObject(File file, Class<T> c) {
		T map = null;
		try {
			map = mapper.readValue(file, c);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return map;
	}
	
	public static JavaType getJavaType(Class<?> collectionClass, Class<?> valueType) {
		JavaType javaType = mapper.getTypeFactory().constructParametricType(collectionClass, valueType);
		return javaType;
	}
	
	/**
	 * writeMapperObject:(). <br/>
	 * TODO().<br/>
	 * 转成字符串
	 * 
	 * @author lyh
	 * @param obj
	 * @return
	 */
	public static <T> String writeMapperObject(Object obj) {
		try {
			return mapper.writeValueAsString(obj);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
