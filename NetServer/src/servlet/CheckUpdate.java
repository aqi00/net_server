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
			"https://imtt.dd.qq.com/sjy.10001/sjy.00004/16891/apk/F1E6E8850F6F80BD071AE8E319C51759.apk",
			"https://imtt.dd.qq.com/sjy.10001/sjy.00004/16891/apk/52C4F042C7AC91FE1D97EE8699E0191C.apk",
			"https://imtt.dd.qq.com/sjy.10001/sjy.00004/16891/apk/FAE5E860689E67BAEE7038261614B64E.apk",
			"https://imtt.dd.qq.com/sjy.10001/sjy.00004/16891/apk/5F93793C6C5F539487B11418A5D4C902.apk",
			"https://imtt.dd.qq.com/sjy.10001/sjy.00004/16891/apk/99CA4F060773B985797B9EFA0DA0A632.apk",
			"https://imtt.dd.qq.com/sjy.10001/sjy.00004/16891/apk/995F74DF2D12C2325158455865E4CC4C.apk" };
	private static String[] mVersionArray = { "13.7.5", "11.2.6", "9.6.2.0",
			"8.0.25", "10.15.0", "8.9.3" };

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
