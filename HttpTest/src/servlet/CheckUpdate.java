package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		System.out.println("request_str=" + request_str);

		response.setContentType("text/plain");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();

		String response_str = getJsonStr();
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

	private String getJsonStr() {
		String str = "{\"app_name\":\"爱奇艺\",\"package_name\":\"com.qiyi.video\",\"version_code\":100200,\"version_name\":\"10.2.0\",\"need_update\":true,\"download_url\":\"https://3g.lenovomm.com/w3g/yydownload/com.qiyi.video/60020\"}";
		return str;
	}

}
