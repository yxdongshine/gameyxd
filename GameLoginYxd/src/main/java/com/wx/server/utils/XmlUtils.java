package com.wx.server.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * XML文件的工具类
 * 
 * @author lyh
 */
public class XmlUtils {
	
	/**
	 * 往节点中添加一个Bool类型的属性
	 * 
	 * @param element 目标节点
	 * @param name 属性名称
	 * @param value 属性值
	 */
	public static void addBoolAttribute(Element element, String name, boolean value) {
		element.addAttribute(name, String.valueOf(value));
	}
	
	/**
	 * 往节点中添加一个Bool类型的子节点
	 * 
	 * @param element 目标节点
	 * @param name 子节点名称
	 * @param value 属性值
	 */
	public static void addBoolElement(Element element, String name, boolean value) {
		element.addElement(name).setText(String.valueOf(value));
	}
	
	/**
	 * 往节点中添加一个Byte类型的属性
	 * 
	 * @param element 目标节点
	 * @param name 属性名称
	 * @param value 属性值
	 */
	public static void addByteAttribute(Element element, String name, byte value) {
		element.addAttribute(name, String.valueOf(value));
	}
	
	/**
	 * 往节点中添加一个Byte类型的子节点
	 * 
	 * @param element 目标节点
	 * @param name 子节点名称
	 * @param value 属性值
	 */
	public static void addByteElement(Element element, String name, byte value) {
		element.addElement(name).setText(String.valueOf(value));
	}
	
	/**
	 * 往节点中添加一个Char类型的属性
	 * 
	 * @param element 目标节点
	 * @param name 属性名称
	 * @param value 属性值
	 */
	public static void addCharAttribute(Element element, String name, char value) {
		element.addAttribute(name, String.valueOf(value));
	}
	
	/**
	 * 往节点中添加一个Char类型的子节点
	 * 
	 * @param element 目标节点
	 * @param name 子节点名称
	 * @param value 属性值
	 */
	public static void addCharElement(Element element, String name, char value) {
		element.addElement(name).setText(String.valueOf(value));
	}
	
	/**
	 * 往节点中添加一个Double类型的属性
	 * 
	 * @param element 目标节点
	 * @param name 属性名称
	 * @param value 属性值
	 */
	public static void addDoubleAttribute(Element element, String name, double value) {
		element.addAttribute(name, String.valueOf(value));
	}
	
	/**
	 * 往节点中添加一个Double类型的子节点
	 * 
	 * @param element 目标节点
	 * @param name 子节点名称
	 * @param value 属性值
	 */
	public static void addDoubleElement(Element element, String name, double value) {
		element.addElement(name).setText(String.valueOf(value));
	}
	
	/**
	 * 往节点中添加一个Bool类型的属性
	 * 
	 * @param element 目标节点
	 * @param name 属性名称
	 * @param value 属性值
	 */
	public static void addFloatAttribute(Element element, String name, float value) {
		element.addAttribute(name, String.valueOf(value));
	}
	
	/**
	 * 往节点中添加一个Float类型的子节点
	 * 
	 * @param element 目标节点
	 * @param name 子节点名称
	 * @param value 属性值
	 */
	public static void addFloatElement(Element element, String name, float value) {
		element.addElement(name).setText(String.valueOf(value));
	}
	
	/**
	 * 往节点中添加一个Int类型的属性
	 * 
	 * @param element 目标节点
	 * @param name 属性名称
	 * @param value 属性值
	 */
	public static void addIntAttribute(Element element, String name, int value) {
		element.addAttribute(name, String.valueOf(value));
	}
	
	/**
	 * 往节点中添加一个Int类型的子节点
	 * 
	 * @param element 目标节点
	 * @param name 子节点名称
	 * @param value 属性值
	 */
	public static void addIntElement(Element element, String name, int value) {
		element.addElement(name).setText(String.valueOf(value));
	}
	
	/**
	 * 往节点中添加一个Long类型的属性
	 * 
	 * @param element 目标节点
	 * @param name 属性名称
	 * @param value 属性值
	 */
	public static void addLongAttribute(Element element, String name, long value) {
		element.addAttribute(name, String.valueOf(value));
	}
	
	/**
	 * 往节点中添加一个Long类型的子节点
	 * 
	 * @param element 目标节点
	 * @param name 子节点名称
	 * @param value 属性值
	 */
	public static void addLongElement(Element element, String name, long value) {
		element.addElement(name).setText(String.valueOf(value));
	}
	
	/**
	 * 往节点中添加一个Short类型的属性
	 * 
	 * @param element 目标节点
	 * @param name 属性名称
	 * @param value 属性值
	 */
	public static void addShortAttribute(Element element, String name, short value) {
		element.addAttribute(name, String.valueOf(value));
	}
	
	/**
	 * 往节点中添加一个Short类型的子节点
	 * 
	 * @param element 目标节点
	 * @param name 子节点名称
	 * @param value 属性值
	 */
	public static void addShortElement(Element element, String name, short value) {
		element.addElement(name).setText(String.valueOf(value));
	}
	
	/**
	 * 往节点中添加一个String类型的属性
	 * 
	 * @param element 目标节点
	 * @param name 属性名称
	 * @param value 属性值
	 */
	public static void addStringAttribute(Element element, String name, String value) {
		element.addAttribute(name, value);
	}
	
	/**
	 * 往节点中添加一个String类型的子节点
	 * 
	 * @param element 目标节点
	 * @param name 子节点名称
	 * @param value 属性值
	 */
	public static void addStringElement(Element element, String name, String value) {
		element.addElement(name).setText(value);
	}
	
	/**
	 * 创建一个XML文档对象
	 * 
	 * @return XML文档对象
	 */
	public static Document createDocument() {
		return DocumentHelper.createDocument();
	}
	
	/**
	 * 从节点中取出目标属性，将它作为一个Bool类型的值返回
	 * 
	 * @param element 目标节点
	 * @param name 属性名称
	 * @return 属性值
	 */
	public static boolean getAttributeAsBool(Element element, String name) {
		try {
			return Boolean.parseBoolean(element.attributeValue(name));
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 从节点中取出目标属性，将它作为一个Byte类型的值返回
	 * 
	 * @param element 目标节点
	 * @param name 属性名称
	 * @return 属性值
	 */
	public static byte getAttributeAsByte(Element element, String name) {
		try {
			return Byte.parseByte(element.attributeValue(name));
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return -1;
	}
	
	/**
	 * 从节点中取出目标属性，将它作为一个Char类型的值返回
	 * 
	 * @param element 目标节点
	 * @param name 属性名称
	 * @return 属性值
	 */
	public static char getAttributeAsChar(Element element, String name) {
		try {
			return element.attributeValue(name).charAt(0);
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * 从节点中取出目标属性，将它作为一个Double类型的值返回
	 * 
	 * @param element 目标节点
	 * @param name 属性名称
	 * @return 属性值
	 */
	public static double getAttributeAsDouble(Element element, String name) {
		try {
			return Double.parseDouble(element.attributeValue(name));
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return -1;
	}
	
	/**
	 * 从节点中取出目标属性，将它作为一个Float类型的值返回
	 * 
	 * @param element 目标节点
	 * @param name 属性名称
	 * @return 属性值
	 */
	public static float getAttributeAsFloat(Element element, String name) {
		try {
			return Float.parseFloat(element.attributeValue(name));
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return -1f;
	}
	
	/**
	 * 从节点中取出目标属性，将它作为一个Int类型的值返回
	 * 
	 * @param element 目标节点
	 * @param name 属性名称
	 * @return 属性值
	 */
	public static int getAttributeAsInt(Element element, String name) {
		try {
			return Integer.parseInt(element.attributeValue(name).trim());
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return -1;
	}
	
	/**
	 * 从节点中取出目标属性，将它作为一个Long类型的值返回
	 * 
	 * @param element 目标节点
	 * @param name 属性名称
	 * @return 属性值
	 */
	public static long getAttributeAsLong(Element element, String name) {
		try {
			return Long.parseLong(element.attributeValue(name));
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return -1;
	}
	
	/**
	 * 从节点中取出目标属性，将它作为一个Short类型的值返回
	 * 
	 * @param element 目标节点
	 * @param name 属性名称
	 * @return 属性值
	 */
	public static short getAttributeAsShort(Element element, String name) {
		try {
			return Short.parseShort(element.attributeValue(name));
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return -1;
	}
	
	/**
	 * 从节点中取出目标属性，将它作为一个String类型的值返回
	 * 
	 * @param element 目标节点
	 * @param name 属性名称
	 * @return 属性值
	 */
	public static String getAttributeAsString(Element element, String name) {
		return element.attributeValue(name);
	}
	
	/**
	 * 从节点中取出目标子节点，将它的文本值转换为一个Bool类型的值返回
	 * 
	 * @param parent 父节点
	 * @param name 子节点名称
	 * @return 属性值
	 */
	public static boolean getElementAsBool(Element parent, String name) {
		try {
			return Boolean.parseBoolean(parent.elementTextTrim(name));
		} catch (Exception e) {
		}
		return false;
	}
	
	/**
	 * 从节点中取出目标子节点，将它的文本值转换为一个Byte类型的值返回
	 * 
	 * @param parent 父节点
	 * @param name 子节点名称
	 * @return 属性值
	 */
	public static byte getElementAsByte(Element parent, String name) {
		try {
			return Byte.parseByte(parent.elementTextTrim(name));
		} catch (Exception e) {
		}
		return -1;
	}
	
	/**
	 * 从节点中取出目标子节点，将它的文本值转换为一个Char类型的值返回
	 * 
	 * @param parent 父节点
	 * @param name 子节点名称
	 * @return 属性值
	 */
	public static char getElementAsChar(Element parent, String name) {
		try {
			return parent.elementTextTrim(name).charAt(0);
		} catch (Exception e) {
		}
		return 0;
	}
	
	/**
	 * 从节点中取出目标子节点，将它的文本值转换为一个Double类型的值返回
	 * 
	 * @param parent 父节点
	 * @param name 子节点名称
	 * @return 属性值
	 */
	public static double getElementAsDouble(Element parent, String name) {
		try {
			return Double.parseDouble(parent.elementTextTrim(name));
		} catch (Exception e) {
		}
		return -1;
	}
	
	/**
	 * 从节点中取出目标子节点，将它的文本值转换为一个Float类型的值返回
	 * 
	 * @param parent 父节点
	 * @param name 子节点名称
	 * @return 属性值
	 */
	public static float getElementAsFloat(Element parent, String name) {
		try {
			return Float.parseFloat(parent.elementTextTrim(name));
		} catch (Exception e) {
		}
		return -1f;
	}
	
	/**
	 * 从节点中取出目标子节点，将它的文本值转换为一个Int类型的值返回
	 * 
	 * @param parent 父节点
	 * @param name 子节点名称
	 * @return 属性值
	 */
	public static int getElementAsInt(Element parent, String name) {
		try {
			return Integer.parseInt(parent.elementTextTrim(name));
		} catch (Exception e) {
		}
		return -1;
	}
	
	/**
	 * 从节点中取出目标子节点，将它的文本值转换为一个Long类型的值返回
	 * 
	 * @param parent 父节点
	 * @param name 子节点名称
	 * @return 属性值
	 */
	public static long getElementAsLong(Element parent, String name) {
		try {
			return Long.parseLong(parent.elementTextTrim(name));
		} catch (Exception e) {
		}
		return -1;
	}
	
	/**
	 * 从节点中取出目标子节点，将它的文本值转换为一个Short类型的值返回
	 * 
	 * @param parent 父节点
	 * @param name 子节点名称
	 * @return 属性值
	 */
	public static short getElementAsShort(Element parent, String name) {
		try {
			return Short.parseShort(parent.elementTextTrim(name));
		} catch (Exception e) {
		}
		return -1;
	}
	
	/**
	 * 从节点中取出目标子节点，将它的文本值转换为一个String类型的值返回
	 * 
	 * @param parent 父节点
	 * @param name 子节点名称
	 * @return 属性值
	 */
	public static String getElementAsString(Element parent, String name) {
		try {
			return parent.elementTextTrim(name);
		} catch (Exception e) {
		}
		return null;
	}
	
	/**
	 * 打印节点
	 * 
	 * @param element 来源节点
	 */
	public static void printElement(Element element) {
		String xml = element.asXML();
		String[] str = xml.split(">");
		for (int i = 0; i < str.length; i++) {
			if (i > 0 && i < str.length - 1)
				System.out.print("\t");
			System.out.println(str[i] + ">");
		}
	}
	
	/**
	 * 读取一个XML文件，获取它的文档对象
	 * 
	 * @param xmlFilePath XML文件路径
	 * @return XML文档对象
	 * @throws Exception
	 */
	public static Document readAsDocument(String xmlFilePath) throws Exception {
		SAXReader saxReader = new SAXReader();
		return saxReader.read(new FileInputStream(xmlFilePath));
	}
	
	/**
	 * 读取一个XML文件，并获取它的根节点
	 * 
	 * @param xmlFilePath XML文件路径
	 * @return XML的根节点
	 * @throws Exception
	 */
	public static Element readAsRootElement(String xmlFilePath) throws Exception {
		Document document = XmlUtils.readAsDocument(xmlFilePath);
		return document.getRootElement();
	}
	
	/**
	 * 输出一个XML文档，使之转换为一个byte[]数组
	 * 
	 * @param document XML文档
	 * @return 输出的byte[]数组
	 * @throws Exception
	 */
	public static byte[] writeAsByteArray(Document document) throws Exception {
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("UTF-8");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		XMLWriter output = new XMLWriter(baos, format);
		output.write(document);
		output.flush();
		baos.flush();
		byte[] data = baos.toByteArray();
		output.close();
		baos.close();
		return data;
	}
	
	/**
	 * 输出一个XML文档，将其写入一个文件中
	 * 
	 * @param filePath XML输出文件路径
	 * @param document XML文档
	 * @throws Exception
	 */
	public static void writeAsFile(String filePath, Document document) throws Exception {
		byte[] data = XmlUtils.writeAsByteArray(document);
		RandomAccessFile rfOut = new RandomAccessFile(filePath, "rw");
		rfOut.write(data);
		rfOut.close();
	}
	
	/**
	 * 输出一个XML文档，使之转换为一个输入流
	 * 
	 * @param document XML文档
	 * @return 转换的输入流
	 * @throws Exception
	 */
	public static InputStream writeAsStream(Document document) throws Exception {
		InputStream is = new ByteArrayInputStream(XmlUtils.writeAsByteArray(document));
		return is;
	}
}
