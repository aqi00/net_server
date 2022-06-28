package com.servlet.office.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.servlet.util.FileUtil;
import org.apache.poi.hslf.usermodel.HSLFSlide;
import org.apache.poi.hslf.usermodel.HSLFSlideShow;
import org.apache.poi.xslf.usermodel.*;

public class PptUtil {

    public static List<String> toImage(String relativePath) {
//        int pos = relativePath.lastIndexOf("/");
//        String dirPath = relativePath.substring(0, pos+1);
//        String fileName = relativePath.substring(pos+1);
//        String destPath = String.format("%s%s%s/", FileUtil.getRootPath(), dirPath, MD5Util.encrypt(fileName));
//        String srcPath = String.format("%s%s", FileUtil.getRootPath(), relativePath);
//        System.out.println("srcPath="+srcPath);
//        System.out.println("destPath="+destPath);
        String srcPath = String.format("%s%s", FileUtil.getRootPath(), relativePath);
        int pos = srcPath.lastIndexOf("/");
        String dirPath = srcPath.substring(0, pos+1);
        String fileName = srcPath.substring(pos+1);
        String destPath = String.format("%s%s/", dirPath, MD5Util.encrypt(fileName));
        if (!new File(destPath).exists()) {
            new File(destPath).mkdirs();
        }
        List<String> pathList;
        if (fileName.endsWith("pptx")) {
            pathList = toImage2007(srcPath, destPath);
        } else {
            pathList = toImage2003(srcPath, destPath);
        }
        for (int i=0; i<pathList.size(); i++) {
            String relativeImagePath = pathList.get(i).replace(FileUtil.getRootPath(), "");
            pathList.set(i, relativeImagePath);
            System.out.println("i="+i+", relativeImagePath="+relativeImagePath);
        }
        return pathList;
    }

    public static List<String> toImage2007(String srcPath, String destPath){
        if (!new File(destPath).exists()) {
            new File(destPath).mkdirs();
        }
        List<String> pathList = new ArrayList<String>();
        try (FileInputStream fis = new FileInputStream(srcPath)) {
            XMLSlideShow ppt = new XMLSlideShow(fis);
            Dimension pageSize = ppt.getPageSize();
            List<XSLFSlide> slideList = ppt.getSlides();

            String tempImagePath = destPath + "temp.png";
            long tempImageLength = saveImage2007(pageSize, slideList.get(slideList.size()-1),tempImagePath);
            String lastImagePath = String.format("%s%03d.png", destPath, slideList.size()-1);
            long lastImageLength = getImageLenth(lastImagePath);
            if (tempImageLength == lastImageLength) {
                for (int i=0; i<slideList.size(); i++) {
                    String imagePath = String.format("%s%03d.png", destPath, i);
                    pathList.add(imagePath);
                }
                return pathList;
            }
            for (int i=0; i<slideList.size(); i++) {
                String imagePath = String.format("%s%03d.png", destPath, i);
                long length = saveImage2007(pageSize, slideList.get(i), imagePath);
                //System.out.println("imagePath="+imagePath+", length="+length);
                pathList.add(imagePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pathList;
    }

    private static long saveImage2007(Dimension pageSize, XSLFSlide slide, String imagePath) {
        // 解决乱码问题
        for(XSLFShape shape : slide.getShapes()){
            if(shape instanceof XSLFTextShape) {
                XSLFTextShape tsh = (XSLFTextShape)shape;
                for(XSLFTextParagraph p : tsh){
                    for(XSLFTextRun r : p){
                        r.setFontFamily("宋体");
                    }
                }
            }
        }
        BufferedImage image = new BufferedImage(pageSize.width, pageSize.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        // 高清
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        // 用最高的精确度和视觉质量执行颜色转换计算。
        graphics.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        // 控制偏向于处理速度还是质量-偏向于质量
        graphics.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        graphics.setPaint(Color.white);
        graphics.fill(new Rectangle(0, 0, pageSize.width, pageSize.height));
        slide.draw(graphics);
        long length = -1;
        try (FileOutputStream fos = new FileOutputStream(imagePath)) {
            ImageIO.write(image, "png", fos);
            length = getImageLenth(imagePath);
        } catch (Exception e) {
        	e.printStackTrace();
        }
    	return length;
    }
    
    public static List<String> toImage2003(String srcPath, String destPath){
        if (!new File(destPath).exists()) {
            new File(destPath).mkdirs();
        }
        List<String> pathList = new ArrayList<String>();
        try (FileInputStream fis = new FileInputStream(srcPath)) {
            HSLFSlideShow ppt = new HSLFSlideShow(fis);
            Dimension pageSize = ppt.getPageSize();
            List<HSLFSlide> slideList = ppt.getSlides();
            
            String tempImagePath = destPath + "temp.png";
            long tempImageLength = saveImage2003(pageSize, slideList.get(slideList.size()-1), tempImagePath);
            String lastImagePath = String.format("%s%03d.png", destPath, slideList.size()-1);
            long lastImageLength = getImageLenth(lastImagePath);
            if (tempImageLength == lastImageLength) {
                for (int i=0; i<slideList.size(); i++) {
                    String imagePath = String.format("%s%03d.png", destPath, i);
                    pathList.add(imagePath);
                }
                return pathList;
            }
            for (int i=0; i<slideList.size(); i++) {
                String imagePath = String.format("%s%03d.png", destPath, i);
                long length = saveImage2003(pageSize, slideList.get(i), imagePath);
                //System.out.println("imagePath="+imagePath+", length="+length);
                pathList.add(imagePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pathList;
    }
    
    private static long saveImage2003(Dimension pageSize, HSLFSlide slide, String imagePath) {
        BufferedImage image = new BufferedImage(pageSize.width, pageSize.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        // 高清
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        // 用最高的精确度和视觉质量执行颜色转换计算。
        graphics.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        // 控制偏向于处理速度还是质量-偏向于质量
        graphics.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        graphics.setPaint(Color.white);
        graphics.fill(new Rectangle(0, 0, pageSize.width, pageSize.height));
        slide.draw(graphics);
        long length = -1;
        try (FileOutputStream fos = new FileOutputStream(imagePath)) {
            ImageIO.write(image, "png", fos);
            length = getImageLenth(imagePath);
        } catch (Exception e) {
        	e.printStackTrace();
        }
    	return length;
    }
    
    private static long getImageLenth(String imagePath) {
        long length = 0;
    	File file = new File(imagePath);
    	if (file.exists()) {
    		length = file.length();
    	}
    	return length;
    }
}
