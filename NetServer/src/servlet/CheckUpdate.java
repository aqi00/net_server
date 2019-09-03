package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class CheckUpdate extends HttpServlet {

	public CheckUpdate() {
		super();
	}

	public void init() throws ServletException {
	}

	public void destroy() {
		super.destroy();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("begin doPost");

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				request.getInputStream()));
		StringBuilder tempStr = new StringBuilder();
		while (reader.read() != -1) {
			tempStr.append(reader.readLine());
		}
		String request_str = tempStr.toString();
		if (request_str.indexOf("{") != 0) {
			request_str = "{" + request_str;
		}
		System.out.println("request_str=" + request_str);

		String response_str = getJsonStr(request_str);
		System.out.println("response_str=" + response_str);
		// 内容长度不能使用字符串的length方法返回值，该方法获取的是字符数而不是字节数
		// response.setContentLength(response_str.length());
		// 获取字符串的字节数需要先调用getBytes方法获得字节数组，再获取字节数组的length
		response.setContentLength(response_str.getBytes().length);
		response.setContentType("text/plain");
		response.setCharacterEncoding("utf-8");

		PrintWriter out = response.getWriter();
		try {
			out.println(response_str);
		} catch (Exception e) {
			System.out.println("e.getMessage()=" + e.getMessage());
			out.println(e.getMessage());
		}

		out.flush();
		out.close();
	}

	private static String[] mNameArray = { "爱奇艺", "酷狗", "美图秀秀", "微信", "淘宝",
			"手机QQ" };
	private static String[] mPackageArray = { "com.qiyi.video",
			"com.kugou.android", "com.mt.mtxx.mtxx", "com.tencent.mm",
			"com.taobao.taobao", "com.tencent.mobileqq" };
	public static String[] mUrlArray = {
			"https://3g.lenovomm.com/w3g/yydownload/com.qiyi.video/60020",
			"https://3g.lenovomm.com/w3g/yydownload/com.kugou.android/60020",
			"https://3g.lenovomm.com/w3g/yydownload/com.mt.mtxx.mtxx/60020",
			"https://3g.lenovomm.com/w3g/yydownload/com.tencent.mm/60020",
			"https://3g.lenovomm.com/w3g/yydownload/com.taobao.taobao/60020",
			"https://3g.lenovomm.com/w3g/yydownload/com.tencent.mobileqq/60020" };
	private static String[] mVersionArray = { "10.2.0", "9.1.5", "8.4.3.0",
			"7.0.3", "8.5.10", "7.9.9" };

	private String getJsonStr(String request_str) {
		JSONObject response_obj = new JSONObject();
		JSONArray response_array = new JSONArray();
		JSONObject request_obj = JSONObject.parseObject(request_str);
		JSONArray packageArray = request_obj.getJSONArray("package_list");
		for (int i = 0; i < packageArray.size(); i++) {
			JSONObject item = JSONObject.parseObject(packageArray.get(i)
					.toString());
			String packageName = item.getString("package_name");
			response_array.add(getRespItem(packageName));
		}
		response_obj.put("package_list", response_array);
		return response_obj.toString();
	}

	private JSONObject getRespItem(String packageName) {
		int i = 0;
		for (; i < mPackageArray.length; i++) {
			if (mPackageArray[i].equals(packageName)) {
				break;
			}
		}
		JSONObject item = new JSONObject();
		item.put("app_name", mNameArray[i]);
		item.put("package_name", packageName);
		if (i < mPackageArray.length) {
			item.put("new_version", mVersionArray[i]);
			item.put("download_url", mUrlArray[i]);
		} else {
			item.put("new_version", "");
			item.put("download_url", "");
		}
		return item;
	}

}
