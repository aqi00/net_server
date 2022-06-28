package com.servlet.util;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.util.Streams;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class FileUtil {

    public static String getRootPath() {
        String rootPath = FileUtil.class.getResource("/").getFile();
        rootPath = rootPath.replace("WEB-INF/classes/", "");
        if (rootPath.contains("build/classes/")) {
            rootPath = rootPath.replace("build/classes/", "")+"WebRoot/";
        }
        System.out.println("rootPath：" + rootPath);
        return rootPath;
    }

    public static String saveFile(FileItem item, String prefix) {
        String fileName = item.getName();
        if (fileName.contains("\\")) {
            fileName = fileName.substring(fileName.lastIndexOf("\\"));
        }
        String relativePath = prefix.concat(fileName).replace("%", "").replace("//", "/");
        String filePath = String.format("%s%s", getRootPath(), relativePath);
        File localFile = new File(filePath);
        if (!localFile.getParentFile().exists()) { // 如果文件的目录不存在
            localFile.getParentFile().mkdirs(); // 创建目录
        }
        filePath = filePath.replace("file:/", "");
        try (InputStream is = item.getInputStream();
             FileOutputStream fos = new FileOutputStream(new File(filePath))) {
            Streams.copy(is, fos, true);
            System.out.println("目标文件：" + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return relativePath;
    }

}
