package com.example.chat;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	public static String getTimeId() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
		return sdf.format(date);
	}

	public static String getNowTime() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(date);
	}

}
