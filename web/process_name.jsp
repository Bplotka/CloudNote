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
<%
    String headingText = "Oops!";
    String messageText = "You forgot to specify your name.";
    String linkText = "Back to logon page";
    String name = request.getParameter( "username" );
    if (name != "") {
        headingText = "Hi, " + name + "!";
        messageText = "Thank you for visiting our home page.";
        linkText = "Log out";
    }
%>
<h1><%= headingText %></h1>
<p class="message_text"><%= messageText %></p>
<p><a href="index.jsp"><%= linkText %></a></p>
</body>
</html>