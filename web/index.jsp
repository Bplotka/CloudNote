<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Welcome</title>
    <style type="text/css">
        body {
            font-family: verdana, tahoma, sans-serif;
            text-align: center;
        }
        h1 {
            margin-top: 50px;
            color: #cc6666;
        }
        .message_text {
            color: #996666;
        }
    </style>
</head>
<body>
<h1>Welcome!</h1>
<form method=post action="process_name.jsp">
    <p class="message_text">To access our home page, type your name<br />
        in the box underneath and click Log on.</p>
    <p>User name: <input type=text name=username size=20>
        <input type=submit value="Log on"></p>
</form>
</body>
</html>