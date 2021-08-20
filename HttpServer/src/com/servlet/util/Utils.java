package com.servlet.util;

import java.net.URL;

public class Utils {

	// 获取当前程序的本地路径。内部兼容了是否打成jar包的两种情况
	public static String getClassPath() {
		String class_path;
		URL url = Utils.class.getResource("/"); // 获取class文件的根目录
		if (url != null) {
			// class文件没被打成jar包时，采用下面办法
			class_path = url.getFile();
		} else {
			// class文件被打进jar包之时，采用下面办法。同时要把配置文件放到jar包的同级目录
			class_path = System.getProperty("user.dir");
		}
		return class_path;
	}
}
