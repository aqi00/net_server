<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>upload</title>
</head>
<body>
<form action="http://localhost:8080/NetServer/uploadServlet" method="post" enctype="multipart/form-data">
	<table>
	<caption>上传实例</caption>
		<tr>
			<td>姓名</td>
			<td>
				<input type="text" name="name">
			</td>
		</tr>
		<tr>
			<td>年龄</td>
			<td>
				<input type="text" name="age">
			</td>
		</tr>
		<tr>
			<td>照片</td>
			<td>
				<input type="file" name="image">
			</td>
		</tr>
		<tr>
			<td></td>
			<td>
				<input type="submit" value="提交">
			</td>
		</tr>
	</table>
</form>
</body>
</html>