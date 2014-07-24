/******************************************************************************/
/* SYSTEM     : A1 STREAM                                                        */
/*                                                                            */
/* SUBSYSTEM  : MDM                                                           */
/******************************************************************************/
package org.test.servlet;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author  Lin zhanwang
 * @version 1.0
 * @since   1.0
 *
 * <MODIFICATION HISTORY>
 *  (Rev.)		(Date)     	(Name)        (Comment)
 *  1.0    		2014-7-24    	Lin zhanwang       New making
 */
public abstract class DownloadCookieUtil {

	private final static String COOKIE_NAME = "fileDownload";
	private final static String COOKIE_VALUE = "true";
	private final static int COOKIE_EXPIRE_SECS = 10 * 60 ; //10 minutes.

	/**
	 * 
	 * @param request
	 * @param resp
	 */
	public final static void setCookie(HttpServletRequest request, HttpServletResponse resp) {
		
		setCookie(request, resp, null, null);
	}

	/**
	 * @param request
	 * @param resp
	 * @param path
	 * @param domain
	 */
	public final static void setCookie(HttpServletRequest request, HttpServletResponse resp, String path, String domain) {
		
		Cookie c = new Cookie(COOKIE_NAME, COOKIE_VALUE);
		if(domain !=null)
			c.setDomain(domain);
		
		if(path !=null)
			c.setPath(path); 
		
		//Set expire age.
		c.setMaxAge(COOKIE_EXPIRE_SECS);   
		resp.addCookie(c);   
	}
	
	/**
	 * 
	 * @param cookName
	 * @param path
	 * @param domain
	 * @param req
	 * @return
	 */
	private Cookie getCook(String cookName, HttpServletRequest req ) {
		
		Cookie[] cs = req.getCookies();
		if(cs == null) 
			return null;
		
		for (Cookie cookie : cs) {
			if(cookie.getName().equals(cookName)) {
				return cookie;
			}
		}
		return null;
	}
}
