/******************************************************************************/
/* SYSTEM     : A1 STREAM                                                        */
/*                                                                            */
/* SUBSYSTEM  : MDM                                                           */
/******************************************************************************/
package org.test;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PushbackReader;
import java.nio.Buffer;

/**
 *
 * @author  Lin zhanwang
 * @version 1.0
 * @since   1.0
 *
 * <MODIFICATION HISTORY>
 *  (Rev.)		(Date)     	(Name)        (Comment)
 *  1.0    		2014-4-1    	Lin zhanwang       New making
 */

public class PushbackReaderDemo
{
	public static void main(String[] args) 
	{
		char[] symbols={'＜', '＞', 
			  '≦', '≧',
			  '≠', '＝'}; 

		try
		{
			
            PushbackReader pushbackReader =
				new PushbackReader(new FileReader(PushbackReaderDemo.class.getClassLoader().getResource("in.txt").getFile()));

//            FileWriter fileWriter = new FileWriter("math_"+args[0]);
            
//            BufferedWriter bw = new BufferedWriter(System.)
            BufferedOutputStream fileWriter = new BufferedOutputStream(System.err);

			int c = 0;

			while((c=pushbackReader.read()) != -1)
			{
                int poss = -1;

				switch(c)
				{
					case '<' :
						poss = 0;break;
					case '>' :
						poss = 1;break;
					case '!':
						poss = 2;break;
					case '=':
						poss = 5;break;
					default:
						fileWriter.write(c);
				}

				if(poss != -1)
				{
					//如果再往下读入一个字符是'='号的话，比如说<=,>=,这里不能在原文件中出现'=='，如果有这种情况，则需要在下面的if段中再加一个if判断
					if((c = pushbackReader.read()) == '=')
					{
						fileWriter.write(symbols[poss+2]);
						fileWriter.write(' ');
					}
					else
					{
						//如果不是'='，则把刚才读入的那个字符再推回流的前端
						pushbackReader.unread(c);
						fileWriter.write(symbols[poss]);
					}
				}
			}
			pushbackReader.close();
			fileWriter.flush();
			fileWriter.close();
		}
		catch(ArrayIndexOutOfBoundsException e)
        {
             System.out.println("请指定文件");
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
