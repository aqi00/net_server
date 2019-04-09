package servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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

public class UploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public UploadServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("request.getContentType()="+request.getContentType());
		response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        Writer o = response.getWriter();
		
		//首先判断Content-Type是不是multipart/form-data。同时也判断了form的提交方式是不是post
		if (ServletFileUpload.isMultipartContent(request)) {
			request.setCharacterEncoding("utf-8");
			// 实例化一个硬盘文件工厂,用来配置上传组件ServletFileUpload   
			DiskFileItemFactory factory =  new DiskFileItemFactory();
			//设置最大占用的内存
			factory.setSizeThreshold(1024000);
			//创建ServletFileUpload对象
			ServletFileUpload sfu = new ServletFileUpload(factory);
			sfu.setHeaderEncoding("utf-8");
			//设置单个文件最大值byte 
			sfu.setFileSizeMax(102400000);
			//所有上传文件的总和最大值byte   
			sfu.setSizeMax(204800000);
			
			List<FileItem> items =  null;
			try {
				items = sfu.parseRequest(request);
	    		System.out.println("items.size()="+items.size());
            } catch(Exception e) {   
                e.printStackTrace();   
            }
            
            Iterator<FileItem> iter = items==null?null:items.iterator();
            while (iter!=null && iter.hasNext()) {
            	FileItem item = (FileItem) iter.next();
            	if (item.isFormField()) { //如果传过来的是普通的表单域
            		System.out.print("普通的表单域:");   
                    System.out.print(new String(item.getFieldName()) + "  ");   
                    System.out.println(new String(item.getString("UTF-8")));   
            	} else if (!item.isFormField()) { //文件域
            		System.out.println("源文件:" + item.getName());
            		String fileName = item.getName();
            		if (fileName.indexOf("\\") >= 0) {
            			fileName = item.getName().substring(item.getName().lastIndexOf("\\"));
            		}
            		BufferedInputStream in = new BufferedInputStream(item.getInputStream());
            		String size = in.available()/1024 + "K";
//            		String rootPath=getClass().getResource("/").getFile().toString();
//            		String filePath = String.format("%s../../%s", rootPath, fileName);
            		String filePath = String.format("D:/%s", fileName);
                    BufferedOutputStream out = new BufferedOutputStream(
                            new FileOutputStream(new File(filePath))); 
                    Streams.copy(in, out, true);
                    o.write("文件上传成功，文件大小为" + size);
            		System.out.println("目标文件:" + filePath);
            	}
            }
		} else {
            System.out.println("表单的Content-Type错误");
        } 
	}

}
