package com.servlet.verifycode.widget;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

//定义一个验证码视图
public class CodeView extends Component {
	private static final long serialVersionUID = 1L;
	public final static int DOT = 0; // 干扰点
	public final static int LINE = 1; // 干扰线
	private final String number_chars = "0123456789"; // 数字的集合串
	private final String full_chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"; // 字母加数字的集合串
	private String chars; // 验证码字符的集合串

	private String verify_code; // 验证码字符串
	private BufferedImage verify_image; // 验证码图像

	// 设置干扰类型
	public void setCodeType(String char_type, String disturber_type) {
		chars = "1".equals(char_type) ? full_chars : number_chars;
		int disturb_type = "1".equals(disturber_type) ? LINE : DOT;
		int width = getWidth(); // 获取控件的宽度
		int height = getHeight(); // 获取控件的高度
		// 创建默认的缓存图像。图像类型为三个字节的RGB格式，对应WIndows风格的RGB模型
		verify_image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g2d = verify_image.createGraphics(); // 获取缓存图像的画笔
		g2d.setColor(new Color(250, 250, 250)); // 设置画笔的颜色
		g2d.fillRect(0, 0, width, height); // 填充背景颜色

		Random r = new Random(); // 创建一个随机对象
		if (disturb_type == LINE) { // 绘制干扰线
			for (int i = 0; i < 10; i++) { // 循环绘制10根干扰线
				g2d.setColor(new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
				g2d.drawLine(r.nextInt(width), r.nextInt(height), r.nextInt(width), r.nextInt(height));
			}
		} else if (disturb_type == DOT) { // 绘制干扰点
			for (int i = 0; i < 120; i++) { // 循环绘制120个干扰点
				g2d.setColor(new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
				g2d.drawOval(r.nextInt(width) - 2, r.nextInt(height) - 2, 2, 2);
			}
		}

		verify_code = ""; // 清空验证码字符串
		for (int i = 0; i < 4; i++) { // 生成四位字符的随机验证码
			verify_code = verify_code + " " + chars.charAt(r.nextInt(chars.length()));
		}
		g2d.setFont(new Font("斜体", Font.ITALIC, height)); // 设置画笔的字体
		//g2d.translate(width / 10, height / 5); // 移动画笔到指定位置
		g2d.translate(0, height / 5 * 2); // 移动画笔到指定位置
		g2d.drawString(verify_code, 5, 25); // 绘制验证码字符串
		repaint();// 重新绘图，此时会接着执行paint方法
	}
	
	// 获取验证码图像
	public BufferedImage getCodeImage() {
		return verify_image;
	}

	// 获取验证码字符串
	public String getCodeNumber() {
		return verify_code.replace(" ", ""); // 去掉验证码中间的空格
	}

	@Override
	public void paint(Graphics g) { // 绘制控件的方法
		if (verify_image != null) { // 如果验证码图像非空
			g.drawImage(verify_image, 0, 0, null); // 绘制验证码图像
		}
	}

	@Override
	public Dimension getPreferredSize() { // 获取控件的推荐宽高
		if (getWidth() > 0 && getHeight() > 0) { // 有指定宽高
			return new Dimension(getWidth(), getHeight()); // 返回setSize方法指定的宽高
		} else { // 未指定宽高
			return new Dimension(200, 50); // 返回默认的宽高
		}
	}
}
