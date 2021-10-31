package com.servlet.nearby;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.servlet.nearby.bean.PersonInfo;
import com.servlet.nearby.bean.QueryResponse;
import com.servlet.util.DbUtil;

public class QueryNearby extends HttpServlet {

	public QueryNearby() {
		super();
	}

	public void init() {
	}

	public void destroy() {
		super.destroy();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		System.out.println("begin doPost");

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				request.getInputStream()));
		StringBuilder tempStr = new StringBuilder();
		while (reader.read() != -1) {
			tempStr.append(reader.readLine());
		}
		String request_str = tempStr.toString();
		System.out.println("request_str=" + request_str);

		response.setContentType("text/plain");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();

		String response_str = getPersonList();
		System.out.println("response_str=" + response_str);
		try {
			out.println(response_str);
		} catch (Exception e) {
			System.out.println("e.getMessage()=" + e.getMessage());
			out.println(e.getMessage());
		}

		out.flush();
		out.close();
	}
	
	private String getPersonList() {
		QueryResponse response = new QueryResponse();
		List<PersonInfo> personList = new ArrayList<PersonInfo>();
		String selectSQL = "select * from person_info";
		System.out.println("selectSQL=" + selectSQL);
		List resultList = DbUtil.queryRecord(selectSQL);
		for (Object obj : resultList) {
			Map map = (Map) obj;
			PersonInfo person = new PersonInfo();
			person.setName((String) map.get("name"));
			person.setSex((Integer) map.get("sex"));
			person.setFace((String) map.get("face"));
			person.setPhone((String) map.get("phone"));
			person.setLove((String) map.get("love"));
			person.setAddress((String) map.get("address"));
			person.setInfo((String) map.get("info"));
			person.setLatitude((Double) map.get("latitude"));
			person.setLongitude((Double) map.get("longitude"));
			if (person.getName()==null || "null".equals(person.getName())) {
				continue;
			}
			personList.add(person);
		}
		response.setPersonList(personList);
		return JSONObject.toJSONString(response);
	}

}
