package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class process_005fname_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.tomcat.InstanceManager _jsp_instancemanager;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_instancemanager = org.apache.jasper.runtime.InstanceManagerFactory.getInstanceManager(getServletConfig());
  }

  public void _jspDestroy() {
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html;charset=UTF-8");
      response.addHeader("X-Powered-By", "JSP/2.2");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\r\n");
      out.write("<html>\r\n");
      out.write("<head>\r\n");
      out.write("    <title>Welcome</title>\r\n");
      out.write("    <style type=\"text/css\">\r\n");
      out.write("        body {\r\n");
      out.write("            font-family: verdana, tahoma, sans-serif;\r\n");
      out.write("            text-align: center;\r\n");
      out.write("        }\r\n");
      out.write("        h1 {\r\n");
      out.write("            margin-top: 50px;\r\n");
      out.write("            color: #cc6666;\r\n");
      out.write("        }\r\n");
      out.write("        .message_text {\r\n");
      out.write("            color: #996666;\r\n");
      out.write("        }\r\n");
      out.write("    </style>\r\n");
      out.write("</head>\r\n");
      out.write("<body>\r\n");

    String headingText = "Oops!";
    String messageText = "You forgot to specify your name.";
    String linkText = "Back to logon page";
    String name = request.getParameter( "username" );
    if (name != "") {
        headingText = "Hi, " + name + "!";
        messageText = "Thank you for visiting our home page.";
        linkText = "Log out";
    }

      out.write("\r\n");
      out.write("<h1>");
      out.print( headingText );
      out.write("</h1>\r\n");
      out.write("<p class=\"message_text\">");
      out.print( messageText );
      out.write("</p>\r\n");
      out.write("<p><a href=\"index.jsp\">");
      out.print( linkText );
      out.write("</a></p>\r\n");
      out.write("</body>\r\n");
      out.write("</html>");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try { out.clearBuffer(); } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
