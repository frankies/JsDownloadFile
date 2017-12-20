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
public class TestJvmArguments {

	public static void main(String[] args) {
		
		
		RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
		List<String> arguments = runtimeMxBean.getInputArguments();
		for (String ag : arguments) {
			System.out.println(ag);
		}
	}
}
