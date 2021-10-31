package com.servlet.audio;

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
import com.servlet.audio.bean.QueryResponse;
import com.servlet.audio.bean.AudioInfo;

public class QueryAudio extends HttpServlet {

	public QueryAudio() {
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

		String response_str = getAudioList();
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
	
	private String getAudioList() {
		QueryResponse response = new QueryResponse();
		List<AudioInfo> audioList = new ArrayList<AudioInfo>();
		String selectSQL = "select * from audio_info order by create_time desc limit 10";
		System.out.println("selectSQL=" + selectSQL);
		List resultList = DbUtil.queryRecord(selectSQL);
		for (Object obj : resultList) {
			Map map = (Map) obj;
			AudioInfo audio = new AudioInfo();
			audio.setArtist((String) map.get("artist"));
			audio.setTitle((String) map.get("title"));
			audio.setDesc((String) map.get("desc"));
			audio.setCover((String) map.get("cover"));
			audio.setAudio((String) map.get("audio"));
			if (audio.getAudio()==null || "null".equals(audio.getAudio())) {
				continue;
			}
			audioList.add(audio);
		}
		response.setAudioList(audioList);
		return JSONObject.toJSONString(response);
	}

}
