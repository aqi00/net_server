package com.servlet.office;

import com.alibaba.fastjson.JSONObject;
import com.servlet.office.bean.ParseResponse;
import com.servlet.util.FileUtil;
import com.servlet.office.util.PptUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;

public class ParsePpt extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ParsePpt() {
        super();
    }

    @SuppressWarnings("unchecked")
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws IOException {
        System.out.println("request.getContentType()=" + request.getContentType());
        ParseResponse parseResponse = new ParseResponse();

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

            Iterator<FileItem> iter = itemList == null ? null : itemList.iterator();
            while (iter != null && iter.hasNext()) {
                FileItem item = iter.next();
                if (item.isFormField()) { // 如果传过来的是普通的表单域
                    String fieldName = item.getFieldName();
                    String fieldValue = item.getString("UTF-8");
                    if ("fileName".equals(fieldName)) {
                    }
                } else { // 文件域
                    System.out.println("源文件：" + item.getName()+", 内容类型："+item.getContentType());
                    if (item.getContentType().startsWith("application/*")) { // 支持ppt和pptx
                        String filePath = FileUtil.saveFile(item, "ppt/");
                        List<String> pathList = PptUtil.toImage(filePath);
                        parseResponse.setPathList(pathList);
                    }
                }
            }
        } else {
            parseResponse.setCode("-1");
            parseResponse.setDesc("表单的Content-Type错误");
        }
        String respStr = JSONObject.toJSONString(parseResponse);
        response.setContentLength(respStr.getBytes().length);
        response.setContentType("text/plain");
        response.setCharacterEncoding("utf-8");

        Writer out = response.getWriter();
        out.write(respStr);
        out.flush();
        out.close();
    }

}
