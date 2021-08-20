package com.servlet.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DbUtil {

	private static String driver_class; // 数据库的驱动类。从配置文件中读取
	private static String dbUrl; // 数据库的连接地址。从配置文件中读取
	private static String dbUserName; // 数据库的用户名。从配置文件中读取
	private static String dbPassword; // 数据库的密码。从配置文件中读取
	static {
		// 获取当前程序的本地路径。内部兼容了是否打成jar包的两种情况
		String class_path = Utils.getClassPath();
		// 以下拼接数据库配置文件db.properties的完整路径
		String config_path = String.format("%s/%s", class_path, "db.properties");
		// 根据指定的配置文件创建属性工具
		PropertiesUtil prop = new PropertiesUtil(config_path);
		driver_class = prop.readString("jdbc.connection.driver_class", ""); // 读取数据库的驱动
		dbUrl = prop.readString("jdbc.connection.url", ""); // 读取数据库的连接地址
		dbUserName = prop.readString("jdbc.connection.username", ""); // 读取数据库的用户名
		dbPassword = prop.readString("jdbc.connection.password", ""); // 读取数据库的密码
		try {
			Class.forName(driver_class); // 加载指定的数据库驱动
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static int insertRecord(String insertSQL) {
		int count = 0;
		try (Connection conn = DriverManager.getConnection(dbUrl, dbUserName, dbPassword);
				Statement stmt = conn.createStatement();) {
			count = stmt.executeUpdate(insertSQL); // 执行插入语句
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	
	public static List queryRecord(String selectSQL) {
		List resultList = new ArrayList<>(); 
		try (Connection conn = DriverManager.getConnection(dbUrl, dbUserName, dbPassword);
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(selectSQL)) {
			List<String> columnList = new ArrayList<String>();
			ResultSetMetaData rsmd = rs.getMetaData();
			for (int i = 1; i < rsmd.getColumnCount() + 1; i++) {
				String columnName = rsmd.getColumnName(i).toLowerCase();
				columnList.add(columnName);
			}
			while (rs.next()) { // 循环遍历结果集里面的所有记录
				Map recordMap = new HashMap<>();
				for (String column : columnList) {
					Object value = rs.getObject(column);
					recordMap.put(column, value);
				}
				resultList.add(recordMap);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultList;
	}
	
}
