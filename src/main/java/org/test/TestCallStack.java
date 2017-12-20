/******************************************************************************/
/* SYSTEM     :                                                         */
/*                                                                            */
/* SUBSYSTEM  :                                                            */
/******************************************************************************/
package org.test;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.List;

/**
 *
 * @author  
 * @version 1.0
 * @since   1.0
 *
 * <MODIFICATION HISTORY>
 *  (Rev.)		(Date)     	(Name)        (Comment)
 *  1.0    		2014-4-2    	       New making
 */
public class TestCallStack {

	public static void main(String[] args) {
		
		final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace(); 
		final String mainClassName = stackTrace[stackTrace.length - 1].getClassName();
		System.out.println("Caller: " + mainClassName);
	}
}
