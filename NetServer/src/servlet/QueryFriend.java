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
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

public class QueryFriend extends HttpServlet {

	public QueryFriend() {
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
		System.out.println("request_str=" + request_str);

		String response_str = getJsonStr();
		System.out.println("response_str=" + response_str);
		response.setContentLength(response_str.length());
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

	private String[] mGroupArray = { "亲戚", "朋友", "同学", "同事", "客户" };

	private String[] mNameArray = { "小明", "小红", "小军", "小丽", "小白", "小燕", "小武",
			"小玉", "小强", "小月", "大伟", "大宝", "大文", "大壮", "大华", "大黑", "大牛", "大麦",
			"大田", "大雁", "阿姐", "阿哥", "阿绣", "阿欢", "阿磊", "阿南", "阿毛", "阿英", "阿杜",
			"阿紫", "老张", "老王", "老赵", "老李", "老刘", "老陈", "老郑", "老林", "老吴", "老马", };

	private String getJsonStr() {
		String str = "";
		JSONObject obj = new JSONObject();
		try {
			obj.put("title", "好友列表");
			JSONArray array = new JSONArray();
			for (int i = 0; i < mGroupArray.length; i++) {
				JSONObject item = new JSONObject();
				item.put("title", mGroupArray[i]);
				item.put("friend_list", getFriendList());
				array.add(item);
			}
			obj.put("group_list", array);
			str = obj.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return str;
	}

	private JSONArray getFriendList() {
		JSONArray array = new JSONArray();
		int count = (int) (Math.random() * 100 % 8 + 5);
		for (int i = 0; i < count; i++) {
			JSONObject item = new JSONObject();
			item.put("nick_name", mNameArray[(int) (Math.random() * 500 % 40)]);
			array.add(item);
		}
		return array;
	}

}
