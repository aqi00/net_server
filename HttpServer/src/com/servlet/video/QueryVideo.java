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
	private String[] digitalSummitArray = {
			"https://ptgl.fujian.gov.cn:8088/masvod/public/2018/04/17/20180417_162d3639356_r38_1200k.mp4",
			"https://ptgl.fujian.gov.cn:8088/masvod/public/2019/04/15/20190415_16a1ef11c24_r38_1200k.mp4",
			"https://ptgl.fujian.gov.cn:8088/masvod/public/2020/09/26/20200926_174c8f9e4b6_r38_1200k.mp4",
			"https://ptgl.fujian.gov.cn:8088/masvod/public/2021/03/19/20210319_178498bcae9_r38.mp4",
			"https://www.fujian.gov.cn/masvod/public/2022/07/15/20220715_18201603713_r38_1200k.mp4",
			"https://www.fujian.gov.cn/masvod/public/2023/04/25/20230425_187b71018de_r38_1200k.mp4",
			"https://video.zohi.tv/fs/transcode/20240520/8cc/355193-1716184798-transv.mp4",
			"https://www.szzg.gov.cn/2025/xwzx/fhzx/202504/P020250430541564957109.mp4"
	};

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
		for (int i=0; i<digitalSummitArray.length; i++) {
			VideoInfo video = new VideoInfo();
			video.setDate((2018+i)+"年");
			video.setAddress("福州");
			video.setDesc("第"+(i+1)+"届数字中国峰会迎宾曲");
			video.setVideo(digitalSummitArray[i]);
			videoList.add(video);
		}
		response.setVideoList(videoList);
		return JSONObject.toJSONString(response);
	}

}
