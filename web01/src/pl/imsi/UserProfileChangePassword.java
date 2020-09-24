package pl.imsi;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class UserProfileChangePassword
 */


@WebServlet("/UserProfileChangePassword")
public class UserProfileChangePassword extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserProfileChangePassword() {
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
	    String useroldpass = request.getParameter("useroldpass");
	    String usernewpass = request.getParameter("usernewpass");
	    LocalDateTime localDateTime = LocalDateTime.now().withNano(0);
	    //Timestamp loginTime = Timestamp.valueOf(localDateTime);
	    int login_count;
	    //LocalDateTime loginTime = localDateTime;

	    try {
	      Class.forName("org.h2.Driver");
	      Connection con = DriverManager.getConnection("jdbc:h2:~/test","sa","");
	      Statement stmt = con.createStatement();
	      ResultSet rs = stmt.executeQuery("select uid,uname,upass,login_count,date_last_failed_login from user_reg where uname='"+username+"' and upass='"+useroldpass+"'");
	      //ResultSet rs1 = stmt.executeQuery("select uname,upass,login_count,date_last_failed_login from user_reg where uname='"+username+"'");
	      //Timestamp last_failed_login = rs1.getTimestamp("date_last_failed_login");
	      //long time = rs.findColumn("date_last_failed_login");
	     
	      if(rs.next())
	      {
	    	  Timestamp last_failed_login = rs.getTimestamp("date_last_failed_login");
	    	  LocalDateTime last_failed_login_LDT = last_failed_login.toLocalDateTime();
	    	  LocalDateTime last_failed_login_LDT_plus_10_mins = last_failed_login_LDT.plusMinutes(5);
	    	  login_count = rs.getInt("login_count");
	    	  ResultSet rs2 = stmt.executeQuery("select uid, create_date from user_reg_passwword where uid='"+rs.getInt("uid")+"' and upass='"+usernewpass+"'");
	    	  
	    	  if(login_count<=3||(last_failed_login_LDT_plus_10_mins.isBefore(localDateTime)))
	    	  {	    		  	
	    		  if(rs2.next())
					{
	    			  	response.setContentType("text/html");
		    	        out.println("<br>Podane nowe has³o by³o ju¿ przez Ciebie u¿ywane - zaproponuj inne");
		    	        out.println("<br><a href=\"http://localhost:8080/web01/changePassword.jsp\"> Back</a>");
					}
	    		  else
	    		  {
	    			  	response.sendRedirect("http://localhost:8080/web01/WelcomeUser.jsp?name="+rs.getString("uname"));
		    	        //HttpSession session1 = request.getSession();
		    	        //session1.setAttribute("uname", username);
		    	        stmt.executeUpdate("insert into user_reg_password(uid,upass,create_date) values ("+rs.getInt("uid")+",'"+useroldpass+"',"+localDateTime+")");
		    	        stmt.executeUpdate("update user_reg set upass='"+usernewpass+"' where uname='"+username+"'");
		    	        stmt.executeUpdate("update user_reg set date_last_login='"+localDateTime+"' where uname='"+username+"'");
	    		  }

	    	  }
	    	  else
	    	  {
	    		  	response.setContentType("text/html");
	    	        out.println("<br>Przekoroczono liczbe blednych logowan - poczekaj 10 minut");
	    	        out.println("<br><a href=\"http://localhost:8080/web01/changePassword.jsp\"> Back</a>");
	    	  }
	      }
	      else
	      {
	    	ResultSet rs1 = stmt.executeQuery("select uid,uname,login_count,date_last_failed_login from user_reg where uname='"+username+"'");
	    	if(rs1.next())
	    	{
	    		Timestamp last_failed_login = rs1.getTimestamp("date_last_failed_login");
	      	  	LocalDateTime last_failed_login_LDT = last_failed_login.toLocalDateTime();
	      	  	LocalDateTime last_failed_login_LDT_plus_10_mins = last_failed_login_LDT.plusMinutes(5);
	      	  	login_count = rs1.getInt("login_count");
	      	  	if(login_count<=3||(last_failed_login_LDT_plus_10_mins.isBefore(localDateTime)))
	      	  	{
	            	//out.println(rs1.toString());
	            	login_count = rs1.getInt("login_count");
	            	login_count++;
	            	response.setContentType("text/html");
	                out.println("<br>Wrong id or/and password");
	                out.println("<br><a href=\"http://localhost:8080/web01/login.jsp\"> Back</a>");
	                stmt.executeUpdate("update user_reg set login_count='"+ login_count +"' where uname='"+username+"'");
	                stmt.executeUpdate("update user_reg set date_last_failed_login='"+localDateTime+"' where uname='"+username+"'");
	      	  	}
	      	  	else
	      	  	{
	    		  	response.setContentType("text/html");
	    	        out.println("<br>Przekoroczono liczbe blednych logowan - poczekaj 10 minut");
	    	        out.println("<br><a href=\"http://localhost:8080/web01/changePassword.jsp\"> Back to change password</a>");
	      	  	}

	    	}
	    	else {
	    		response.setContentType("text/html");
	            out.println("<br>Wrong id or/and password");
	            out.println("<br><a href=\"http://localhost:8080/web01/login.jsp\"> Back</a>");
	    	}   		

	      }

	      
	    }catch (ClassNotFoundException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
  } catch (SQLException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
  }
}
}