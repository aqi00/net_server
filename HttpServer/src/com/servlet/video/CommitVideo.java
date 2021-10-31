package com.servlet.video;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.servlet.util.FileUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.alibaba.fastjson.JSONObject;
import com.servlet.video.bean.CommitResponse;
import com.servlet.video.bean.VideoInfo;
import com.servlet.util.DbUtil;

public class CommitVideo extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public CommitVideo() {
		super();
	}

	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		System.out.println("request.getContentType()=" + request.getContentType());
		CommitResponse commitResponse = new CommitResponse();

		// 首先判断Content-Type是不是multipart/form-data。同时也判断了form的提交方式是不是post
		if (ServletFileUpload.isMultipartContent(request)) {
			request.setCharacterEncoding("utf-8");
			// 实例化一个硬盘文件工厂,用来配置上传组件ServletFileUpload
			DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setSizeThreshold(1024000); // 设置最大占用的内存
			// 创建ServletFileUpload对象
			ServletFileUpload sfu = new ServletFileUpload(factory);
			sfu.setHeaderEncoding("utf-8"); // 设置包头的字符编码
			sfu.setFileSizeMax(102400000); // 设置单个文件最大值byte
			sfu.setSizeMax(204800000); // 所有上传文件的总和最大值byte

			List<FileItem> itemList = null;
			try {
				itemList = sfu.parseRequest(request);
				System.out.println("itemList.size()=" + itemList.size());
			} catch (Exception e) {
				e.printStackTrace();
			}

			VideoInfo video = new VideoInfo();
			Iterator<FileItem> iter = itemList == null ? null : itemList.iterator();
			while (iter != null && iter.hasNext()) {
				FileItem item = iter.next();
				if (item.isFormField()) { // 如果传过来的是普通的表单域
					String fieldName = item.getFieldName();
					String fieldValue = item.getString("UTF-8");
					if ("date".equals(fieldName)) {
						video.setDate(fieldValue);
					} else if ("address".equals(fieldName)) {
						video.setAddress(fieldValue);
					} else if ("label".equals(fieldName)) {
						video.setLabel(fieldValue);
					} else if ("desc".equals(fieldName)) {
						video.setDesc(fieldValue);
					} else if ("cover".equals(fieldName)) {
						video.setCover(fieldValue);
					} else if ("video".equals(fieldName)) {
						video.setVideo(fieldValue);
					}
				} else { // 文件域
					System.out.println("源文件：" + item.getName()+", 内容类型："+item.getContentType());
					if (item.getContentType().startsWith("image")) {
						String filePath = FileUtil.saveFile(item, "short/");
	            		video.setCover(filePath);
					} else if (item.getContentType().startsWith("video")) {
						String filePath =  FileUtil.saveFile(item, "short/");
	            		video.setVideo(filePath);
					}
				}
			}
			String insertSQL = String.format("insert into `video_info`(date,address,label,`desc`,cover,video,create_time)"
					+ " values('%s','%s','%s','%s','%s','%s',now())", 
					video.getDate(), video.getAddress(), video.getLabel(), video.getDesc(), video.getCover(), video.getVideo());
			System.out.println("insertSQL：" + insertSQL);
			DbUtil.insertRecord(insertSQL);
		} else {
			commitResponse.setCode("-1");
			commitResponse.setDesc("表单的Content-Type错误");
		}
		String respStr = JSONObject.toJSONString(commitResponse);
		response.setContentLength(respStr.getBytes().length);
		response.setContentType("text/plain");
		response.setCharacterEncoding("utf-8");

		Writer out = response.getWriter();
		out.write(respStr);
		out.flush();
		out.close();
	}

}
