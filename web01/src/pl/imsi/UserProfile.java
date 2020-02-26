package pl.imsi;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/**
 * Servlet implementation class UserProfile
 */
@WebServlet("/UserProfile")
public class UserProfile extends HttpServlet {
  private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserProfile() {
        super();
        // TODO Auto-generated constructor stub
    }
  /**
   * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // TODO Auto-generated method stub
    response.getWriter().append("Served at: ").append(request.getContextPath());
  }
  /**
   * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // TODO Auto-generated method stub
    doGet(request, response);
    PrintWriter out = response.getWriter();
    String username = request.getParameter("username");
    String userpass = request.getParameter("userpass");
    try {
      Class.forName("org.h2.Driver");
      Connection con = DriverManager.getConnection("jdbc:h2:~/test","sa","");
      Statement stmt = con.createStatement();
      ResultSet rs = stmt.executeQuery("select uname,upass from user_reg where uname='"+username+"' and upass='"+userpass+"'");
      
      if(rs.next()) {
        response.sendRedirect("http://localhost:8080/web01/WelcomeUser.jsp?name="+rs.getString("uname"));
        HttpSession session1 = request.getSession();
        session1.setAttribute("uname", username);
        
        
      }else {
    	response.setContentType("text/html");
        out.println("<br>Wrong id and password");
        out.println("<br><a href=\"http://localhost:8080/web01/login.jsp\"> Back</a>");
      }
      
      
      
    } catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    
    
  }
}