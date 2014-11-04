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
    String login = request.getParameter("login");
%>
<h1>Rejestracja zakończona sukcesem!</h1>
<p class="message_text">Dziękujemy użytkowniku <%= login %> za chęć korzystania z naszej aplikacji!</p>
</body>
</html>