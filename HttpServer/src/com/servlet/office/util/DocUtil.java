package com.servlet.office.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import com.servlet.util.FileUtil;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.apache.poi.xwpf.converter.core.BasicURIResolver;
import org.apache.poi.xwpf.converter.core.FileImageExtractor;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.w3c.dom.Document;

public class DocUtil {

	public static String toHtml(String relativePath) {
        int pos = relativePath.lastIndexOf("/");
        String dirPath = relativePath.substring(0, pos+1);
        String fileName = relativePath.substring(pos+1);
        String destPath = String.format("%s%s%s/index.html", FileUtil.getRootPath(), dirPath, MD5Util.encrypt(fileName));
        String srcPath = String.format("%s%s", FileUtil.getRootPath(), relativePath);
        System.out.println("srcPath="+srcPath);
        System.out.println("destPath="+destPath);
		if (fileName.endsWith("docx")) {
			toHtml2007(srcPath, destPath);
		} else {
			toHtml2003(srcPath, destPath);
		}
        return String.format("%s%s/index.html", dirPath, MD5Util.encrypt(fileName));
	}

    public static boolean toHtml2003(String srcPath, String destPath) {
        boolean result = true;
        String imageDir = "image/";
        int pos = destPath.lastIndexOf("/");
        String imagePath = destPath.substring(0, pos+1)+imageDir;
        if (!new File(imagePath).exists()) {
            new File(imagePath).mkdirs();
        }
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                FileOutputStream fos = new FileOutputStream(destPath)) {
            HWPFDocument wordDocument = new HWPFDocument(new FileInputStream(srcPath));
            WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(  
                    DocumentBuilderFactory.newInstance().newDocumentBuilder()  
                            .newDocument());
             wordToHtmlConverter.setPicturesManager( new PicturesManager() {
                 public String savePicture( byte[] content,
                         PictureType pictureType, String suggestedName,
                         float widthInches, float heightInches ) {
                     return imageDir+suggestedName;
                 }
             } );
            wordToHtmlConverter.processDocument(wordDocument);
            // 保存文档中的图片
            List<Picture> pics = wordDocument.getPicturesTable().getAllPictures();
            if (pics != null) {
                for(int i=0; i<pics.size(); i++){
                    Picture pic = pics.get(i);
                    try {
                        pic.writeImageContent(new FileOutputStream(
                                imagePath + pic.suggestFullFileName()));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
            Document htmlDocument = wordToHtmlConverter.getDocument();
            DOMSource domSource = new DOMSource(htmlDocument);
            StreamResult streamResult = new StreamResult(bos);
      
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer serializer = tf.newTransformer();
            serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty(OutputKeys.METHOD, "html");
            serializer.transform(domSource, streamResult);
            fos.write(bos.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    public static boolean toHtml2007(String srcPath, String destPath) {
        boolean result = true;
        int pos = destPath.lastIndexOf("/");
        String imagePath = destPath.substring(0, pos+1)+"image/";
        if (!new File(imagePath).exists()) {
            new File(imagePath).mkdirs();
        }
        //System.out.println("imagePath="+imagePath);
        try (FileOutputStream fos = new FileOutputStream(destPath)) {
              XWPFDocument document = new XWPFDocument(new FileInputStream(srcPath)); 
              XHTMLOptions options = XHTMLOptions.create().indent(4);
              // 存放图片的文件夹 
              options.setExtractor(new FileImageExtractor(new File(imagePath))); 
              // html中图片的路径 
              options.URIResolver(new BasicURIResolver(imagePath));
              XHTMLConverter.getInstance().convert( document, fos, options );
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }
    
}
