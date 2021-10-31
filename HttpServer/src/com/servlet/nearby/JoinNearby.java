package com.servlet.nearby;

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
import com.servlet.nearby.bean.JoinResponse;
import com.servlet.nearby.bean.PersonInfo;
import com.servlet.util.DbUtil;

public class JoinNearby extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public JoinNearby() {
		super();
	}

	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		System.out.println("request.getContentType()=" + request.getContentType());
		JoinResponse joinResponse = new JoinResponse();

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

			PersonInfo person = new PersonInfo();
			Iterator<FileItem> iter = itemList == null ? null : itemList.iterator();
			while (iter != null && iter.hasNext()) {
				FileItem item = iter.next();
				if (item.isFormField()) { // 如果传过来的是普通的表单域
					String fieldName = item.getFieldName();
					String fieldValue = item.getString("UTF-8");
					if ("name".equals(fieldName)) {
						person.setName(fieldValue);
					} else if ("sex".equals(fieldName)) {
						person.setSex(Integer.parseInt(fieldValue));
					} else if ("phone".equals(fieldName)) {
						person.setPhone(fieldValue);
					} else if ("love".equals(fieldName)) {
						person.setLove(fieldValue);
					} else if ("address".equals(fieldName)) {
						person.setAddress(fieldValue);
					} else if ("info".equals(fieldName)) {
						person.setInfo(fieldValue);
					} else if ("latitude".equals(fieldName)) {
						person.setLatitude(Double.parseDouble(fieldValue));
					} else if ("longitude".equals(fieldName)) {
						person.setLongitude(Double.parseDouble(fieldValue));
					}
				} else { // 文件域
					System.out.println("源文件：" + item.getName());
					String filePath = FileUtil.saveFile(item, "nearby/");
            		person.setFace(filePath);
				}
			}
			String insertSQL = String.format("insert into `person_info`(name,sex,face,phone,love,address,info,latitude,longitude,create_time)"
					+ " values('%s',%d,'%s','%s','%s','%s','%s',%f,%f,now())", 
					person.getName(), person.getSex(), person.getFace(), person.getPhone(), person.getLove(),
					person.getAddress(), person.getInfo(), person.getLatitude(), person.getLongitude());
			System.out.println("insertSQL：" + insertSQL);
			DbUtil.insertRecord(insertSQL);
		} else {
			joinResponse.setCode("-1");
			joinResponse.setDesc("表单的Content-Type错误");
		}
		String respStr = JSONObject.toJSONString(joinResponse);
		response.setContentLength(respStr.getBytes().length);
		response.setContentType("text/plain");
		response.setCharacterEncoding("utf-8");

		Writer out = response.getWriter();
		out.write(respStr);
		out.flush();
		out.close();
	}

}
