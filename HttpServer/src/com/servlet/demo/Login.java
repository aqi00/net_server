package com.servlet.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;

public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public Login() {
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
		String respStr = "";
		
		Map<String, String[]> map = request.getParameterMap();
		if (map!=null && map.size()>0) {
			respStr = "您输入的表单信息如下：";
			for (Map.Entry<String, String[]> item : map.entrySet()) {
				// 表单数据默认采用iso-8859-1编码，需要由服务端转成utf-8
				String value = new String(item.getValue()[0].getBytes("iso-8859-1"), "utf-8");
				respStr = String.format("%s字段%s的值为%s，", respStr, item.getKey(), value);
			}
		} else {
			// 读取请求内容
			BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(),"utf-8"));
			String line = null;
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			String reqStr = sb.toString();
			System.out.println("reqStr=" + reqStr);
			JSONObject json = JSONObject.parseObject(reqStr);
			respStr = String.format("您的用户名是%s，密码是%s", 
					json.getString("username"), json.getString("password"));
		}
		
		System.out.println("respStr=" + respStr);
		// 内容长度不能使用字符串的length方法返回值，该方法获取的是字符数而不是字节数
		// response.setContentLength(response_str.length());
		// 获取字符串的字节数需要先调用getBytes方法获得字节数组，再获取字节数组的length
		response.setContentLength(respStr.getBytes().length);
		response.setContentType("text/plain");
		response.setCharacterEncoding("utf-8");

		Writer out = response.getWriter();
		out.write(respStr);
		out.flush();
		out.close();
	}

}
