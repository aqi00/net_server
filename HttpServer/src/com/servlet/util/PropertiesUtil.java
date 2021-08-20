package com.servlet.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

//定义属性文件的工具类
public class PropertiesUtil {
	private Properties mProp; // 属性表
	private String mConfigPath; // 配置文件的路径
	
	public PropertiesUtil(String config_path) {
		mConfigPath = config_path;
		// 根据指定路径构建文件输入流对象
		try (FileInputStream fis = new FileInputStream(mConfigPath)) {
			mProp = new Properties(); // 创建一个属性表对象
			mProp.load(fis); // 把输入流中的数据加载到属性表
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 读取指定名称的属性值
	public String readString(String name, String defaultValue) {
		return mProp.getProperty(name, defaultValue); // 从属性表中获取指定名称的属性值
	}

	// 写入指定名称的属性值
	public void writeString(String name, String value) {
		mProp.setProperty(name, value); // 把指定名称的属性值写入属性表
	}

	// 提交属性表的修改
	public void commit() {
		// 根据指定路径构建文件输出流对象
		try (FileOutputStream fos = new FileOutputStream(mConfigPath)) {
			mProp.store(fos, ""); // 把属性表的数据保存到输出流
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
