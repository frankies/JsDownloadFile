/******************************************************************************/
/* SYSTEM     :                                                         */
/*                                                                            */
/* SUBSYSTEM  :                                                            */
/******************************************************************************/
package org.test.servlet;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author  
 * @version 1.0
 * @since   1.0
 *
 * <MODIFICATION HISTORY>
 *  (Rev.)		(Date)     	(Name)        (Comment)
 *  1.0    		2014-7-24    	       New making
 */
public class CookiesServlet extends HttpServlet {

	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doCookies(req, resp);
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		
		doCookies(req, resp);
		
	}
	
	private final static String COOKIE_NAME = "fileDownload";
	private final static String COOKIE_VALUE = "true";
	private final static String COOKIE_PATH = "";
	private final static String COOKIES_DOMAIN = "";
	
	/**
	 * @param req
	 * @param resp
	 */
	private void doCookies(HttpServletRequest req, HttpServletResponse resp) {
		
		int time = ((req.getParameter("times")==null || req.getParameter("times").length()==0) ? 0 : Integer.parseInt(req.getParameter("times")));
		System.out.println("Time " + time);
		
		Cookie ct = this.getCook(COOKIE_NAME, COOKIE_PATH, COOKIES_DOMAIN, req);
		if(ct !=null) {
			System.out.println("Got cookie!" );
		}
		
		Cookie c = new Cookie(COOKIE_NAME, COOKIE_VALUE);
//		c.setDomain(req.getContextPath());
//		c.setPath(COOKIE_PATH);
		c.setMaxAge(10*60); 
		
		resp.addCookie(c); 
		
		req.setAttribute("times", ++time);
		
		 
		
		System.out.println("---------------------------\n");
		
		 try {
				req.getRequestDispatcher("showCookies.jsp").forward(req, resp);
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} 
			
		/*	try {
				resp.sendRedirect("showCookies.jsp");
			} catch (IOException e) {
				e.printStackTrace();
			}*/
	}
	
	/**
	 * @param cookName
	 * @param path
	 * @param domain
	 * @param req
	 * @return
	 */
	private Cookie getCook(String cookName, String path, String domain, HttpServletRequest req ) {
		
		Cookie[] cs = req.getCookies();
		if(cs == null) return null;
		for (Cookie cookie : cs) {
			System.out.println(String.format("name: %s, value:%s", cookie.getName(), cookie.getValue()));
			if(cookie.getName().equals(cookName)) {
				return cookie;
			}
		}
		return null;
	}
	
}
