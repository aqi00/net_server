package tool;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class ExportMac {
	private static ArrayList<String> lineArray;
	private static HashMap<String, String> deviceMap;
	private static ArrayList<String> deviceArray;

	public static void main(String[] arg) {
		initDeviceMap();
		String content = openTxtFile("E:\\oui.txt");
		System.out.println("content.length="+content.length());
		String[] spiltLine = content.split("\\n");
		System.out.println("spiltLine.length="+spiltLine.length);
		
		lineArray = new ArrayList<String>();
		for (int i=0; i<spiltLine.length; i++) {
			if (spiltLine[i].indexOf("(hex)") > 0) {
				lineArray.add(spiltLine[i]);
			}
		}
		System.out.println("lineArray.length="+lineArray.size());
		deviceArray = new ArrayList<String>();
		for (int j=0; j<lineArray.size(); j++) {
			String line = lineArray.get(j).replace("	", " ");
			String[] splitItem = line.split(" +");
//			if (splitItem[0].equals("00-CD-FE")) {
//				System.out.println("splitItem.length="+splitItem.length+", splitItem[2]="+splitItem[2]);
//			}
			if (splitItem.length < 4) {
				continue;
			}
			if (deviceMap.containsKey(splitItem[2].replace(",", "").toUpperCase())) {
				deviceArray.add(splitItem[0]+","+splitItem[2].replace(",", ""));
			} else if (deviceMap.containsKey(splitItem[3].replace(",", "").toUpperCase())) {
				deviceArray.add(splitItem[0]+","+splitItem[3].replace(",", ""));
			}
		}
		System.out.println("deviceArray.length="+deviceArray.size());
		String result = "";
		for (int k=0; k<deviceArray.size(); k++) {
			result = String.format("%s%s\n", result, deviceArray.get(k));
		}
		saveTxtFile("E:\\mac_device.txt", result);
	}

	private static void saveTxtFile(String path, String txt) {
		try {
			FileOutputStream fos = new FileOutputStream(path);
			fos.write(txt.getBytes());
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String openTxtFile(String path) {
		String readStr = "";
		try {
			FileInputStream fis = new FileInputStream(path);
			byte[] b = new byte[fis.available()];
			fis.read(b);
			fis.close();
			readStr = new String(b);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return readStr;
	}
	
	private static void initDeviceMap() {
		deviceMap = new HashMap<String, String>();
		deviceMap.put("Apple".toUpperCase(),"1");
		deviceMap.put("Huawei".toUpperCase(),"1");
		deviceMap.put("Smartisan".toUpperCase(),"1");
		deviceMap.put("Dell".toUpperCase(),"1");
		deviceMap.put("Samsung".toUpperCase(),"1");
		deviceMap.put("Lenovo".toUpperCase(),"1");
		deviceMap.put("Vivo".toUpperCase(),"1");
		deviceMap.put("Gionee".toUpperCase(),"1");
		deviceMap.put("Asus".toUpperCase(),"1");
		deviceMap.put("Motorola".toUpperCase(),"1");
		deviceMap.put("Fujitsu".toUpperCase(),"1");
		deviceMap.put("OnePlus".toUpperCase(),"1");
		deviceMap.put("Philips".toUpperCase(),"1");
		deviceMap.put("Yulong".toUpperCase(),"1");
		deviceMap.put("Toshiba".toUpperCase(),"1");
		deviceMap.put("Haier".toUpperCase(),"1");
		deviceMap.put("Coship".toUpperCase(),"1");
		deviceMap.put("Intel".toUpperCase(),"1");
		deviceMap.put("Hewlett".toUpperCase(),"1");
		deviceMap.put("D-Link".toUpperCase(),"1");
		deviceMap.put("Cisco".toUpperCase(),"1");
		deviceMap.put("Microsoft".toUpperCase(),"1");
		deviceMap.put("HTC".toUpperCase(),"1");
		deviceMap.put("Google".toUpperCase(),"1");
		deviceMap.put("TINNO".toUpperCase(),"1");
		deviceMap.put("Qiku".toUpperCase(),"1");
		deviceMap.put("zte".toUpperCase(),"1");
		deviceMap.put("OPPO".toUpperCase(),"1");
		deviceMap.put("TP-LINK".toUpperCase(),"1");
		deviceMap.put("Feixun".toUpperCase(),"1");
		deviceMap.put("Xiaomi".toUpperCase(),"1");
		deviceMap.put("LG".toUpperCase(),"1");
		deviceMap.put("Sony".toUpperCase(),"1");
		deviceMap.put("NEC".toUpperCase(),"1");
		deviceMap.put("Hitachi".toUpperCase(),"1");
		deviceMap.put("Hisense".toUpperCase(),"1");
		deviceMap.put("TCL".toUpperCase(),"1");
		deviceMap.put("Acer".toUpperCase(),"1");
		deviceMap.put("Meizu".toUpperCase(),"1");
		deviceMap.put("SHARP".toUpperCase(),"1");
		deviceMap.put("BlackBerry".toUpperCase(),"1");
		deviceMap.put("Nokia".toUpperCase(),"1");
		deviceMap.put("Alcatel".toUpperCase(),"1");
		deviceMap.put("Doov".toUpperCase(),"1");
		deviceMap.put("Newmine".toUpperCase(),"1");
		deviceMap.put("Uniscope".toUpperCase(),"1");
		deviceMap.put("Letv".toUpperCase(),"1");
		deviceMap.put("DAXIAN".toUpperCase(),"1");
		deviceMap.put("Changhong".toUpperCase(),"1");
		deviceMap.put("Konka".toUpperCase(),"1");
		deviceMap.put("BIRD".toUpperCase(),"1");
		deviceMap.put("Siemens".toUpperCase(),"1");
		deviceMap.put("Ericsson".toUpperCase(),"1");
		deviceMap.put("Panasonic".toUpperCase(),"1");
		deviceMap.put("MediaTek".toUpperCase(),"1");
	}
	
}
