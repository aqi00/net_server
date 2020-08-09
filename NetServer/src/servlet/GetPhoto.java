package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class GetPhoto extends HttpServlet {

	public GetPhoto() {
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
		if (request_str!=null && request_str.indexOf("{") != 0) {
			request_str = "{" + request_str;
		}
		System.out.println("request_str=" + request_str);

		String response_str = getJsonStr(request_str);
		// 获取字符串的字节数需要先调用getBytes方法获得字节数组，再获取字节数组的length
		response.setContentLength(response_str.getBytes().length);
		response.setContentType("text/plain");
		response.setCharacterEncoding("utf-8");

		PrintWriter out = response.getWriter();
		try {
			out.println(response_str);
			System.out.println("response_str=" + response_str);
		} catch (Exception e) {
			e.printStackTrace();
			out.println(e.getMessage());
		}
		out.flush();
		out.close();
	}

	private String getJsonStr(String request_str) {
		Set<String> titleSet = new HashSet<String>();
		JSONObject response_obj = new JSONObject();
		JSONArray response_array = new JSONArray();
		for (int i = 0; i < 6; i++) {
			int pos = new Random().nextInt(titleArray.length);
			if (titleSet.contains(titleArray[pos])) {
				i--;
				continue;
			} else {
				titleSet.add(titleArray[pos]);
			}
			JSONObject item = new JSONObject();
			item.put("title", titleArray[pos]);
			item.put("image_url", urlArray[pos]);
			response_array.add(item);
		}
		response_obj.put("photo_list", response_array);
		return response_obj.toString();
	}
	
	private String[] titleArray = {
			"香山",
			"恭王府",
			"玉龙雪山",
			"丽江古城",
			"大理古城",
			"洱海",
			"滇池",
			"柯岩",
			"绍兴东湖",
			"沈园",
			"西塘",
			"乌镇",
			"北海公园",
			"菽庄花园",
			"天坛",
			"故宫",
			"颐和园",
			"八达岭",
			"都江堰",
			"大雁塔",
			"兵马俑",
			"趵突泉",
			"八大关",
			"网师园",
			"拙政园",
			"留园",
			"杭州西湖",
			"雷峰塔",
			"鼓浪屿",
			"罗星塔",
	};
	private String[] urlArray = {
			"http://b247.photo.store.qq.com/psb?/V11ZojBI0KVlp5/Hs8YbkaNJRYoAjUXDM2B4YLlGcUYRpVSUz4TzaBF9kA!/b/dHKKSJOHBQAA",
			"http://b258.photo.store.qq.com/psb?/V11ZojBI3rAuoL/.f*o4gxFqwPi1Syzzg3t0VgWxt2tyQwogV1S0rvKtig!/b/dPaL1ZmyHwAA",
			"http://b255.photo.store.qq.com/psb?/V11ZojBI3z5kJW/MKykomT6yXC5PDhP2LRqpvrvfdgtT3B.tYZ19.N55TQ!/b/dLkVB5iDIwAA",
			"http://b256.photo.store.qq.com/psb?/V11ZojBI1t3Ft2/rtu.UTCGQAGPybb*ZE4PxrXt79rcQesQOYkDV5O1ZuE!/b/dAa9n5hmIwAA",
			"http://b255.photo.store.qq.com/psb?/V11ZojBI3rAT5l/QyvH1wAheQaNCS2NaJyJzjM8j34Q9Oq48YZ5wwQ70VI!/b/dG0rB5haHwAA",
			"http://b255.photo.store.qq.com/psb?/V11ZojBI0Zz6pV/m4Rjx20D9iFL0D5emuYqMMDji*HGQ2w2BWqv0zK*tRk!/b/dGp**5dYEAAA",
			"http://b255.photo.store.qq.com/psb?/V11ZojBI1miu0c/4RliDO4zAL5l6LFfWU8kJIQyAFEiDn8zSTlR6MyN0Ts!/b/dERGDZgpEAAA",
			"http://b253.photo.store.qq.com/psb?/V11ZojBI46HKdL/4qIKiXrqVR7pRr*Y*oAdN*8sGHO7dTS.Ks8eL7Ry2EU!/b/dEUM2ZY9FwAA",
			"http://b250.photo.store.qq.com/psb?/V11ZojBI2uyNjr/PlUBKqvl57rjlLytnkAg.jCFaqw9eKjSlJ.g8PYv.6M!/b/dB1OD5XTIwAA",
			"http://b254.photo.store.qq.com/psb?/V11ZojBI3JQK16/ktGKEj1pVUrFtCZyVYxCv0SzYt3yQJsVwdEaCsFPAAw!/b/dI6ocZdjBQAA",
			"http://b89.photo.store.qq.com/psb?/V11ZojBI0j3g9Y/djXXVkgvj.RfDraDH5U9csaeEZM9Yk7vCM27EwWrj0g!/b/dB8NFzUyVwAA",
			"http://b90.photo.store.qq.com/psb?/V11ZojBI0iLs6K/gDfTlE2dUiwmkBMSdcNw.VRGgxdGFqskkYgFXd7r9*0!/b/dBYhsTXhGwAA",
			"http://b87.photo.store.qq.com/psb?/V11ZojBI28E7Zc/otHMJb6bPOS1t9bQEf4Z57uc42HLl06V71ISZiKWAJU!/b/YZCx3DN1ZQAAYghx6jM2ZwAA",
			"http://b88.photo.store.qq.com/psb?/V11ZojBI25zdM8/Mc2eBqp7UZxVv2OC7djEG8zIex0FuXYLFXgEWD.xD18!/b/YWbzfzT0KgAAYoSEgTRxKgAA",
			"http://m.qpic.cn/psb?/V11ZojBI3S9u77/E6VoJ3m*dSkR9HJn6bESxIP6hEY*FBOvn6W.qr8S0Ww!/b/YZL7FTLdfQAAYiv.FTL3ewAA",
			"http://b89.photo.store.qq.com/psb?/V11ZojBI3dOE5c/RhQit93unFzntzQUZao4vifa2YumgTKhJcMThmUKRus!/b/YSCiGzUtAwAAYiC7ITVeAwAA",
			"http://b82.photo.store.qq.com/psb?/V11ZojBI0Pg0bt/COL3nsv35fHRT*CjbPHfqqsgky25oZxAoY4QHzdK9Is!/b/YRBx7DBzlAAAYldJ4zDYkwAA",
			"http://b82.photo.store.qq.com/psb?/V11ZojBI2j3vOK/SParhNfc*gzURBmVyb3hSysfr92b2shWyFhA9v3Q2xo!/b/YfQU9DCwnQAAYnIX9DDVmwAA",
			"http://b83.photo.store.qq.com/psb?/V11ZojBI0bKF81/gYDenEdnyy7uOZZt9Nz2y4I.acEELDKLjkGksjMR2dU!/b/YQ.diTEnUgAAYh6AgzHXUQAA",
			"http://b82.photo.store.qq.com/psu?/41bbe7f7-19d8-40d8-b4a1-3f97d4b3c973/OIJqZ134PeaoVewv2BaLWh8DMq.JY8TbgFDGwLci*Vk!/b/YSCV9TCxMgAAYth17DD8MgAA",
			"http://b68.photo.store.qq.com/psu?/85edf2ab-a163-4369-9210-3ce6477f8ea8/4SeIL7EsBGvkAMov1tif*Yt0knq.HJPb0a*gzsIyLSg!/b/YXQfjihfkwAAYnxMmiiklwAA",
			"http://b77.photo.store.qq.com/psu?/4dc95c29-9379-4311-a8f9-fdc7a7f6a765/B2R2fQcGA3V5kWL864UwlH.TNBVYFH*ntZwZys70s1A!/b/YfES9i15YAAAYjXm6S1KYQAA",
			"http://b69.photo.store.qq.com/psu?/7c568aa2-7a9d-4a8c-bb1e-31965b37d9ac/Zpj*3Qf0yvtHVvj8L4OgO6oavp4cJm3hD.3i*SO8TS8!/b/YdTFLCloWgAAYtZYLimYWQAA",
			"http://b56.photo.store.qq.com/psu?/9fc4d155-87fb-429e-ba02-72bc70f42ef7/zhXUhuCFEGIkcL3QBz5jc744t1KstDRwq6JJ0.Ec07A!/b/YV.DZSEisQAAYsPXdyG1rwAA",
			"http://b69.photo.store.qq.com/psu?/8d4dbd97-191e-4c19-9175-fe7bb56a8b0e/9jWlOWBknLKn1RFTynCxAWNtPQ72QtHU.1sjaYY2gdo!/b/YSZULimSHQAAYqUuJSlFHgAA",
			"http://b69.photo.store.qq.com/psu?/0664e98e-f40a-4899-b6d6-4f4f1d94ef3c/GVlMwrs014vQdZicUb31gzIhh5TSOtkrR6E7oX7wVuc!/b/YVUoJSkYCwAAYmCzJinQCgAA",
			"http://b48.photo.store.qq.com/psu?/4f964eae-c7aa-4c31-98c6-ead9c4a7e822/yzMLkqJ0vRctVGrohvWsuvTafceRvtLywUStgP1I10c!/b/YR.ErRl1rgAAYhmIqxzeIwAA",
			"http://b64.photo.store.qq.com/psu?/b57067db-438d-4a64-972c-8b529ba9b3ea/huMmo*Sxx5xwQkVfSDyXE*Ut7lL6H1dxNdp.rftc.MU!/b/YZPJLiZIagAAYphEKiYhawAA",
			"http://b86.photo.store.qq.com/psb?/V11ZojBI3Em2xO/a0Zi2BbczFva1uz1NO4OihU.vDEDVPB2UaaO2SBD9dw!/b/YSNZUDPQWAAAYpO.SzO.WAAA",
			"http://b11.photo.store.qq.com/psu?/65ff58c8-bc15-4fab-a9b7-a51e41a5aacc/OruvaQV.JABjUG16KqJuAPxwIKC6pNA1WA9bSwHuI.0!/b/Yf66CQYLNQAAYp3UoAYVQgAA",
	};

}
