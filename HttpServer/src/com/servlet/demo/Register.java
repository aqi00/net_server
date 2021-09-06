package com.servlet.demo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public Register() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		System.out.println("request.getContentType()=" + request.getContentType());
		String respStr = "您的注册信息如下：";

		// 首先判断Content-Type是不是multipart/form-data。同时也判断了form的提交方式是不是post
		if (ServletFileUpload.isMultipartContent(request)) {
			request.setCharacterEncoding("utf-8");
			// 实例化一个硬盘文件工厂,用来配置上传组件ServletFileUpload
			DiskFileItemFactory factory = new DiskFileItemFactory();
			// 设置最大占用的内存
			factory.setSizeThreshold(1024000);
			// 创建ServletFileUpload对象
			ServletFileUpload sfu = new ServletFileUpload(factory);
			sfu.setHeaderEncoding("utf-8");
			// 设置单个文件最大值byte
			sfu.setFileSizeMax(102400000);
			// 所有上传文件的总和最大值byte
			sfu.setSizeMax(204800000);

			List<FileItem> itemList = null;
			try {
				itemList = sfu.parseRequest(request);
				System.out.println("itemList.size()=" + itemList.size());
			} catch (Exception e) {
				e.printStackTrace();
			}

			Iterator<FileItem> iter = itemList == null ? null : itemList.iterator();
			while (iter != null && iter.hasNext()) {
				FileItem item = (FileItem) iter.next();
				if (item.isFormField()) { // 如果传过来的是普通的表单域
					String fieldName = item.getFieldName();
					String fieldValue = new String(item.getString("UTF-8"));
					respStr = String.format("%s字段%s的值为%s，", respStr, fieldName, fieldValue);
					System.out.print("普通的表单域："+fieldName+"="+fieldValue);
				} else { // 文件域
					System.out.println("源文件：" + item.getName());
					String fileName = item.getName();
					if (fileName.contains("\\")) {
						fileName = fileName.substring(fileName.lastIndexOf("\\"));
					}
					fileName = fileName.replace("%", "");
					String rootPath = getClass().getResource("/").getFile();
					rootPath = rootPath.replace("WEB-INF/classes/", "");
					if (rootPath.contains("build/classes/")) {
						rootPath = rootPath.replace("build/classes/", "")+"WebRoot/";
					}
					System.out.println("rootPath：" + rootPath);
					String filePath = String.format("%s%s", rootPath, fileName);
            		filePath = filePath.replace("file:/", "");
            		try (InputStream is = item.getInputStream();
            				FileOutputStream fos = new FileOutputStream(new File(filePath))) {
    					String size = is.available() / 1024 + "K";
    					Streams.copy(is, fos, true);
    					respStr = String.format("%s名叫%s的文件上传成功，文件大小为%s。", 
    							respStr, fileName, size);
    					System.out.println("目标文件：" + filePath);
            		} catch (Exception e) {
            			e.printStackTrace();
            		}
				}
			}
		} else {
			respStr = "表单的Content-Type错误";
		}
		response.setContentLength(respStr.getBytes().length);
		response.setContentType("text/plain");
		response.setCharacterEncoding("utf-8");

		Writer out = response.getWriter();
		out.write(respStr);
		out.flush();
		out.close();
	}

}
