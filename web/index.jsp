<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Rejestracja - CloudNote</title>
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
        .reg-form{
            text-align: center;
            margin: auto;
            width: 500px;
        }
        .inputs{
            width: 200px;
        }
        td.error_container > div {
            width: 100%;
            height: 100%;
            overflow:hidden;
            color: #F71313;
            text-align:center;
        }
        td.error_container {
            height: 10px;
        }

    </style>
</head>
<body>
<%

    String error_header = "";
    Map<String, String> errors = new HashMap<String, String>();
    errors.put("err-login","");
    errors.put("err-password","");
    String login = request.getParameter("login");
    if (login == null){
        login = "";
    }
    if ("true".equals(request.getParameter("error"))){
        error_header = "";
        for (Map.Entry<String, String> entry : errors.entrySet()) {
            String val = request.getParameter(entry.getKey());
            if (val != null && val != ""){
                entry.setValue(" <TABLE  cellspacing=\"0\" width=\"400\">\n" +
                        "            <tr >\n" +
                        "                <td class=\"error_container\">\n" +
                        "                    <div>" + val + "</div>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "        </TABLE>");
            }

        }
    }

%>
<h1>Witaj w rejestracji produktu CloudNote</h1>
<div style="color: #F71313;"><%= error_header %></div>
<form method=post action="register">
    <p class="message_text">Zapraszamy do zarejestrowania się do naszej aplikacji. <br />Pośpiesz się bo niedługo wprowadzamy opłatę!
    </p>
    <div class="reg-form" >

        <%= errors.get("err-login") %>
        <TABLE cellpadding="3" cellspacing="0" width="400">
            <tr>
                <td width="40%" align="right">
                    <font face="verdana" color="purple">Login:</font>
                </td>
                <td width="60%" align="center">
                    <input class="inputs" type=text name=login value="<%= login %>">
                </td>
            </tr>
        </TABLE>
        <%= errors.get("err-password") %>
        <TABLE cellpadding="3" cellspacing="0" width="400">
            <tr>
                <td width="40%" align="right">
                    <font face="verdana" color="purple">Hasło:</font>
                </td>
                <td width="60%" align="center">
                   <input class="inputs" type=password name=password >
                </td>

            </tr>
        </TABLE>
        <TABLE cellpadding="3" cellspacing="0" width="400">
            <tr>
                <td width="40%" align="right">
                    <font face="verdana" color="purple">Powtórz hasło:</font>
                </td>
                <td width="60%" align="center">
                    <input class="inputs" type=password name=password2 >
                </td>
            </tr>
        </TABLE>
        <TABLE cellpadding="3" cellspacing="0" width="400">
            <tr>
                <td width="40%" align="right">
                </td>
                <td width="60%" align="center">
                    <input class="inputs" type=submit value="Rejestruj się" width="200px">
                </td>
            </tr>
        </TABLE>

    </div>
</form>
</body>
</html>