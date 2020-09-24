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
import java.util.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.sql.Timestamp;

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
    LocalDateTime localDateTime = LocalDateTime.now().withNano(0);
    //Timestamp loginTime = Timestamp.valueOf(localDateTime);
    int login_count;
    //LocalDateTime loginTime = localDateTime;

    try {
      Class.forName("org.h2.Driver");
      Connection con = DriverManager.getConnection("jdbc:h2:~/test","sa","");
      Statement stmt = con.createStatement();
      ResultSet rs_check = stmt.executeQuery("select login_count, date_last_failed_login from user_reg where uname='"+username+"'");
      if(rs_check.next()) {
    	  int login_count_check = rs_check.getInt("login_count");
    	  if(login_count_check<3)
    	  {
    	      ResultSet rs = stmt.executeQuery("select uname from user_reg where uname='"+username+"' and upass='"+userpass+"'");
    	      if(rs.next()) {  	    	  
    	    	response.sendRedirect("http://localhost:8080/web01/WelcomeUser.jsp?name="+rs.getString("uname"));
      	        HttpSession session1 = request.getSession();
      	        session1.setAttribute("uname", username);
      	        stmt.executeUpdate("update user_reg set login_count=0 where uname='"+username+"'");
      	        stmt.executeUpdate("update user_reg set date_last_login='"+localDateTime+"' where uname='"+username+"'");    	    	  
    	      }else {
    	    	  response.setContentType("text/html");
    	          out.println("<br>Wrong id or/and password - sorry");
    	          out.println("<br><a href=\"http://localhost:8080/web01/login.jsp\"> Back</a>");
    	          int login_count_to_update = login_count_check+1;
    	          stmt.executeUpdate("update user_reg set login_count='"+ login_count_to_update +"' where uname='"+username+"'");
          	      stmt.executeUpdate("update user_reg set date_last_failed_login='"+localDateTime+"' where uname='"+username+"'");
    	      }
    	  }
    	  else {
    		  Timestamp last_failed_login = rs_check.getTimestamp("date_last_failed_login");
    		  LocalDateTime last_failed_login_LDT = last_failed_login.toLocalDateTime();
    		  LocalDateTime last_failed_login_LDT_plus_blocked_time = last_failed_login_LDT.plusMinutes(1);
    		  if(last_failed_login_LDT_plus_blocked_time.isBefore(localDateTime)) {
    			  int login_count_to_update = 0;
    			  ResultSet rs = stmt.executeQuery("select uname from user_reg where uname='"+username+"' and upass='"+userpass+"'");
        	      if(rs.next()) {  	    	  
        	    	response.sendRedirect("http://localhost:8080/web01/WelcomeUser.jsp?name="+rs.getString("uname"));
          	        HttpSession session1 = request.getSession();
          	        session1.setAttribute("uname", username);
          	        stmt.executeUpdate("update user_reg set login_count=0 where uname='"+username+"'");
          	        stmt.executeUpdate("update user_reg set date_last_login='"+localDateTime+"' where uname='"+username+"'");    	    	  
        	      }else {
        	    	  response.setContentType("text/html");
        	          out.println("<br>Wrong id or/and password - sorry");
        	          out.println("<br><a href=\"http://localhost:8080/web01/login.jsp\"> Back</a>");
        	          login_count_to_update++;
        	          stmt.executeUpdate("update user_reg set login_count='"+ login_count_to_update +"' where uname='"+username+"'");
              	      stmt.executeUpdate("update user_reg set date_last_failed_login='"+localDateTime+"' where uname='"+username+"'");
        	      }
    		  }
    		  else {
    			response.setContentType("text/html");
      	        out.println("<br>Przekoroczono liczbe blednych logowan - poczekaj 1 minutê");
      	        out.println("<br><a href=\"http://localhost:8080/web01/login.jsp\"> Back</a>");
    		  }
    	  }
      }else
      {
    	  response.setContentType("text/html");
          out.println("<br>Wrong id or/and password");
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