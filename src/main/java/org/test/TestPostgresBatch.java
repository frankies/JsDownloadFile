/******************************************************************************/
/* SYSTEM     :                                                         */
/*                                                                            */
/* SUBSYSTEM  :                                                            */
/******************************************************************************/
package org.test;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;

import org.postgresql.PGConnection;
import org.postgresql.copy.CopyManager;

/**
 *
 * @author  
 * @version 1.0
 * @since   1.0
 *
 * <MODIFICATION HISTORY>
 *  (Rev.)		(Date)     	(Name)        (Comment)
 *  1.0    		2014-4-1    	       New making
 */
public class TestPostgresBatch {

	private int testSize= 101200;
	private int batchSize= 100;
	
	private Connection conn = this.getConn();
	
	private long[] measurementIds;
	private Timestamp[] timestamps;
	private BigDecimal[] values;
	/*
	CREATE TABLE measurement
	(
	  measurement_id bigint NOT NULL,
	  valid_ts timestamp with time zone NOT NULL,
	  measurement_value numeric(19,4) NOT NULL,
	  CONSTRAINT pk_mv_raw PRIMARY KEY (measurement_id, valid_ts)
	)
	WITH (OIDS=FALSE)
	*/
	
	public static void main(String[] args) throws SQLException, IOException {
		
		TestPostgresBatch b = new TestPostgresBatch();
		b.setup();
		
//		b.test1();
//		b.test2();
		
		b.batchSize = 50;
		b.test3();
		b.test4(false);
		
/*		b.batchSize = 100;
		b.test3();
		b.test4();
		
		b.batchSize = 150;
		b.test3();
		b.test4();
		
		b.batchSize = 200;
		b.test3();
		b.test4(false);*/
		
		b.summary();
	}
	
	private void truncatTable() throws SQLException {
		
		Statement insert = conn.createStatement();
		insert.executeUpdate("truncate table measurement");
	}
	
	/**
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	private Connection getConn() {
		
		String url = "jdbc:postgresql://10.191.5.163:5432/a1stream_db_test",
		user = "a1stream", 
		psw = "a1streampass";
		Connection con =  null;
		try {
			Class.forName("org.postgresql.Driver");
			con = DriverManager.getConnection(url, user, psw);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}
	
	/**
	 * 
	 */
	private void setup() {

		measurementIds = new long[testSize];
		timestamps = new Timestamp[testSize];
		values = new BigDecimal[testSize];

		Timestamp st = new Timestamp(new Date().getTime());
		BigDecimal x = BigDecimal.ONE;
		long id = 1;
		for (int i = 0; i < testSize; i++) {
			measurementIds[i] = id;
			timestamps[i] = st;
			values[i] = x.add(BigDecimal.TEN);
			x = values[i];
			st = new Timestamp(st.getTime() + (1000));
			id += 1;
		}
	}
	

	private StringBuffer logs = new StringBuffer();
	private long startTime = -1;
	
	/**
	 * @param name
	 */
	private void startWatch(String name) {
	
		System.out.println("Start '" + name + "'");
		startTime = new Date().getTime();
		logs.append(name).append(":");
	}
	
	/**
	 * 
	 */
	private void stopWatch() {
		
		long c =  new Date().getTime() - startTime;
		logs.append(c + "ms").append("\n");
	}
	
	/**
	 * @return
	 */
	private void summary() {
		System.out.println(logs.toString());
	}
	
	
	
	
	public void test1() throws SQLException {
		
		conn = this.getConn();
		startWatch("test1");
		try {
			Statement insert = conn.createStatement();
			for (int i=0; i<testSize; i++)
			{
			   String insertSQL = "insert into measurement values ("   + measurementIds[i] +",'"+ timestamps[i] +"',"+values[i]+")";
			  insert.execute(insertSQL);
			}
			stopWatch();
			
			this.truncatTable();
		}finally {
			conn.close();
		}
	}
	
	public void test2() throws SQLException {
		
		conn = this.getConn();
		this.startWatch("test2");
		try {
			for (int i=0; i<testSize; i++)
			{
			  PreparedStatement insert = conn.prepareStatement("insert into measurement values (?,?,?)");
			  insert.setLong(1,measurementIds[i]);
			  insert.setTimestamp(2, timestamps[i]);
			  insert.setBigDecimal(3, values[i]);
			  insert.execute();
			}
			
			stopWatch();		
			this.truncatTable();
		}finally {
			conn.close();
		}
		
	}
	
	public void test3() throws SQLException {
		
		conn = this.getConn();
		try {
			this.startWatch("test3-" + this.batchSize);
			PreparedStatement insert = conn.prepareStatement("insert into measurement values (?,?,?)");
			for (int i=0; i<testSize; i++)
			{
			  insert.setLong(1,measurementIds[i]);
			  insert.setTimestamp(2, timestamps[i]);
			  insert.setBigDecimal(3, values[i]);
			  insert.addBatch();
			  if (i % batchSize == 0) { insert.executeBatch(); }
			}
			insert.executeBatch();
			stopWatch();
			
			this.truncatTable();
		} finally {
			conn.close();
		}
	}
	
/*	public void test5() throws SQLException {
		
		conn = this.getConn();
		int MAX_LEN = 10240;
		
		try {
			//('B6717', 'Tampopo', 110, '1985-02-10', 'Comedy')
			this.startWatch("test5-" + this.batchSize);
			
			StringBuffer sql = new StringBuffer(); 
			sql.append("insert into measurement values ");
			PreparedStatement insert = conn.prepareStatement("insert into measurement values (?,?,?)");
//			Statement insert = conn.createStatement();
			int x = 0;
			for (int i=0; i<testSize; i++)
			{
			  sql.append("(?,?,?),"); 
			  insert.setLong(++x,measurementIds[i]);
			  insert.setTimestamp(++x, timestamps[i]);
			  insert.setBigDecimal(++x, values[i]);
			  if((x + 3) > MAX_LEN) {
				  insert.executeUpdate();
				  x = 0;
				  sql.delete(0, sql.length()-1);
			  }
//			  insert.addBatch();
//			  if (i % batchSize == 0) { insert.executeBatch(); }
			}
			stopWatch();
			
			this.truncatTable();
		} finally {
			conn.close();
		}
	}*/
	
	public void test4() throws SQLException, IOException {
		test4(true);
	}
	
	public void test4(boolean trunc) throws SQLException, IOException {

		conn = this.getConn();
		try {
			this.startWatch("test4-" + this.batchSize);
			StringBuilder sb = new StringBuilder();
			CopyManager cpManager = ((PGConnection)conn).getCopyAPI();
			PushbackReader reader = new PushbackReader( new StringReader(""), 10000 );
			for (int i=0; i<testSize; i++)
			{
			    sb.append(measurementIds[i]).append(",'")
			      .append(timestamps[i]).append("',")
			      .append(values[i]).append("\n");
			    if (i % batchSize == 0)
			    {
			      reader.unread( sb.toString().toCharArray() );
			      cpManager.copyIn("COPY measurement FROM STDIN WITH CSV", reader );
			      sb.delete(0,sb.length());
			    }
			}
			reader.unread( sb.toString().toCharArray() );
			cpManager.copyIn("COPY measurement FROM STDIN WITH CSV", reader );
			stopWatch();		
			
			if(trunc)
			  this.truncatTable();
		} finally {
			conn.close();
		}

	}
	
}
