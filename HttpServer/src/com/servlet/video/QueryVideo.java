package com.servlet.video;

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
import com.servlet.util.DbUtil;
import com.servlet.video.bean.QueryResponse;
import com.servlet.video.bean.VideoInfo;

public class QueryVideo extends HttpServlet {

	public QueryVideo() {
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

		String response_str = getVideoList();
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
	
	private String getVideoList() {
		QueryResponse response = new QueryResponse();
		List<VideoInfo> videoList = new ArrayList<VideoInfo>();
		String selectSQL = "select * from video_info order by create_time desc limit 10";
		System.out.println("selectSQL=" + selectSQL);
		List resultList = DbUtil.queryRecord(selectSQL);
		for (Object obj : resultList) {
			Map map = (Map) obj;
			VideoInfo video = new VideoInfo();
			video.setDate((String) map.get("date"));
			video.setAddress((String) map.get("address"));
			video.setLabel((String) map.get("label"));
			video.setDesc((String) map.get("desc"));
			video.setCover((String) map.get("cover"));
			video.setVideo((String) map.get("video"));
			if (video.getVideo()==null || "null".equals(video.getVideo())) {
				continue;
			}
			videoList.add(video);
		}
		response.setVideoList(videoList);
		return JSONObject.toJSONString(response);
	}

}
