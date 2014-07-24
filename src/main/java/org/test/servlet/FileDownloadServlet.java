/******************************************************************************/
/* SYSTEM     : A1 STREAM                                                        */
/*                                                                            */
/* SUBSYSTEM  : MDM                                                           */
/******************************************************************************/
package org.test.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
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
 *  1.0    		2014-1-24    	Lin zhanwang       New making
 */
public class FileDownloadServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 966265263665260261L;

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		doProcess(req, resp);
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		 doProcess(req, resp);
	}
	
	/**
	 * @param req
	 * @param resp
	 */
	private void doProcess(HttpServletRequest req, HttpServletResponse response) {
		
		boolean isCheckDownload = "CheckDownload".equals(req.getParameter("id"));
		int times = isCheckDownload ? Integer.parseInt(req.getParameter("times")): -1;
		if(isCheckDownload) {
        	DownloadCookieUtil.setCookie(req, response);
        }
		
/*		if(times % 2== 0) {
			throw new RuntimeException("Failure!!");
		}*/
		
        String encodedFileName = encodeFileNameForDownload("test" + (isCheckDownload ? times : "") + ".csv");
        String contentDisposition = "attachment"
        + "; filename=\"" + encodedFileName + "\"";
        response.setHeader("Content-Disposition", contentDisposition);
        response.setContentType("application/octet-stream");
//        response.setCharacterEncoding(UTF8);
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream outputStream = null;
        try {
            bufferedInputStream = new BufferedInputStream(getFileStream());
            outputStream = new BufferedOutputStream(response.getOutputStream());
            outputStream.write(BOM_UTF8);
            byte[] buf = new byte[1024];
            int len;
            while ((len = bufferedInputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.flush();
        } catch (IOException e) {
        	e.printStackTrace();
        } finally {
            try {
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
            	e.printStackTrace();
            } finally {
            }
        }
    }
	
	public static void main(String[] args) throws UnsupportedEncodingException, IOException {
		   //Create bcp file if not exist  
        File bcpFile = new File("d:/test.csv");  
        //bcpFile.delete();  
        byte[] bom ={(byte) 0xEF,(byte) 0xBB,(byte) 0xBF};  
        //boolean newFile = false;  
        FileOutputStream bcpFileWriter = new FileOutputStream(bcpFile);  
        bcpFileWriter.write(bom);  
  //bcpFile.delete();  
        String title = "\"MD5\",\"扫描文件名\",\"扫描时间\"," +  
        "\"是否病毒\",\"安全等级\",\"病毒英文名称\"," +  
        "\"病毒变种\",\"病毒类型\",\"病毒影响\"," +  
        "\"感染系统\",\"传播方式\",\"备注\"";  
        
        String x = "测试,0,,Hậu Giang,Châu Thành A";
          
          
        bcpFileWriter.write(x.getBytes("utf-8"));  
        bcpFileWriter.write("\n".getBytes());  
          
        String appStr = "\""+123+"\","  
        +"\""+123+"\","  
        +123+","  
        +123+","  
        +123+","  
        +"\""+123+"\","  
        +"\"\","  
        +123+","  
        +"\""+123+"\","  
        +"\""+123+"\","  
        +"\""+123+"\","  
        +"\""+123+"\"\n";  
          

        bcpFileWriter.write(appStr.getBytes());  
        bcpFileWriter.close();  
	}
	
	private final static String UTF8 = "utf-8";
	private final static byte[] BOM_UTF8 = new byte[]{(byte)0xEF,(byte)0xBB,(byte)0xBF};
	
	
	private InputStream getFileStream() throws IOException {
		
//		return new FileInputStream("")
		String x = "测试,0,,Hậu Giang,Châu Thành A";
		System.out.println(System.getProperty("file.encoding"));
		
		//
        byte[] contentByte = x.getBytes(UTF8);  
        
//        byte[] allData = new byte[BOM_UTF8.length + contentByte.length];  
//        System.arraycopy(BOM_UTF8, 0, allData, 0, BOM_UTF8.length);  
//        System.arraycopy(contentByte, 0, allData, BOM_UTF8.length,  
//                contentByte.length);  
        
		
		return new ByteArrayInputStream(contentByte);
//		return new FileInputStream("E:/CMM0103_01.csv");
	}
	
    /**
     * @param fileName
     * @return
     */
    private String encodeFileNameForDownload(String fileName) {

        try {
            return URLEncoder.encode(fileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
           e.printStackTrace();
        }
        return "";
    }
}
