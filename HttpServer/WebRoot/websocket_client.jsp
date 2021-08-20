<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>WebSocket测试页面</title>
</head>
<body>
WebSocket测试<br/>
<input id="text" type="text"/>
     <button onclick="send()">发送消息</button>
     <hr/>
     <button onclick="closeWebSocket()">关闭WebSocket连接</button>
     <hr/>
     <div id="message"></div>

</body>

<script type="text/javascript">
    var websocket = null;
    // 判断当前浏览器是否支持websocket
    if('WebSocket' in window) {
        websocket = new WebSocket("ws://localhost:8080/HttpServer/testWebSocket");
    }else {
        alert("not error  不支持websocket")
    }
    // 连接错误时发生的回调方法
    websocket.onerror = function () {
        alert("websocket.onerror")
    }
    // 连接成功时的回调方法
    websocket.onopen = function () {
        alert("websocket.onopen ")
    }
    //接收到消息的回调函数
    websocket.onmessage = function (event) {
        alert("websocket.onmessage "+event.data)
    }
    // 连接关闭时的回调函数
    websocket.onclose = function () {
        alert("websocket.onclose")
    }

	//监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
	window.onbeforeunload = function() {
		closeWebSocket();
	}
	function closeWebSocket() {
		websocket.close();
	}
	function send() {
		var message = document.getElementById('text').value;
		websocket.send(message);
	}
</script>
</html>