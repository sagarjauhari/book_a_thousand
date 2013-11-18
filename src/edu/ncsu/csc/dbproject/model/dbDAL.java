package edu.ncsu.csc.dbproject.model;

import edu.ncsu.csc.dbproject.util.CustomException;
import edu.ncsu.csc.dbproject.util.CustomLogger;
import edu.ncsu.csc.dbproject.model.dbconnection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.Arrays;

public class dbDAL {
	
	private static CustomLogger s_Logger = CustomLogger.getInstance(dbDAL.class);
	
    public static int DAL_SUCCESS = 0;
    public static int DAL_INSERT_ERR = 1;
    public static int DAL_NO_DBNAME_ERR = 2;
    public static int DAL_NO_DBQUERY_ERR = 3;
    public static int DAL_DB_EXISTS_ERR = 4;
    public static int DAL_CHILD_REC_EXISTS_ERR = 5;
    public static int DAL_REC_EXISTS_ERR = 6;
    public static int DAL_REC_NOT_EXISTS_ERR = 7;
    public static int DAL_VALUE_LARGE_ERR = 8;
    public static int DAL_UNKNOWN_ERR = 9;
    public static int DAL_NULL_NOT_ALLOWED = 10;

    public static int errorCode = DAL_UNKNOWN_ERR;
    public static String zeroArr[] = new String[0];
    public static String nullArr[] = null;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		/*	Example 1 for simple select query with no bindVars	*/
		String query = "SELECT * FROM vendors";
		Vector results = new Vector();
		try{
			getDBData(query, results);
		}catch(SQLException e){
			
		}catch(CustomException e){
			
		}
		System.out.println("Results for Simple select query with no bindVars : " + results);
		System.out.println("*******************************************************************************");

		/*	Example 2 for select query with String only bindVars. If all bindVars are of type 'String', then descriptor 'desc' array is not necessary	*/
		query = "SELECT ID FROM vendors WHERE name = ?";
		String[] bindVars = new String[1];
		bindVars[0] = "Harper Collins";
		results = new Vector();
		try{
			getDBData(query, results, bindVars);
		}catch(SQLException e){
			
		}catch(CustomException e){
			
		}
		System.out.println("Results for select query with String only bindVars : " + results);
		System.out.println("*******************************************************************************");
		
		/*	Example 3 for another select query with bindVars	*/
		query = "SELECT count(*) FROM vendor_payments WHERE payment_date > ?";	
		bindVars = new String[1];
		bindVars[0] =  "02-JAN-12";		//should come from input
		results = new Vector();
		try{
			getDBData(query, results, bindVars);
		}catch(SQLException e){
			
		}catch(CustomException e){
			
		}
		System.out.println("Results for select query with String only bindVars : " + results);
		System.out.println("*******************************************************************************");
		
		/*	Example 4 for update query using doDBOperation. Check for errorCode returned to determine if query was successful or not	*/
		query = "UPDATE vendors SET phone_number = ? WHERE name = ?";
		bindVars = new String[2];
		bindVars[0] = "919-666-1111";
		bindVars[1] = "Harper Collins";
		int errorCode = -1;
		try{
			errorCode = doDBOperation(query, bindVars);
		}catch(SQLException e){
			
		}catch(CustomException e){
			
		}
		if(errorCode == 0)
			System.out.println("Update was successful");
		else
			System.out.println("Update failed");
		System.out.println("*******************************************************************************");
		
	}
	
    public static int getDBData(String dbQuery,
            Vector displayData) throws SQLException, CustomException
    {
        return getDBData(dbQuery, displayData, null, null, false, 0,
                -1, true);
    }
    
    public static int getDBData(String dbQuery,
            Vector displayData, String[] bindVars) throws SQLException,
            CustomException
    {
        return getDBData(dbQuery, displayData, bindVars, null, false,
                0, -1, true);
    }
    
    public static int getDBData(String dbQuery,
            Vector displayData, String[] bindVars, String[] desc)
            throws SQLException, CustomException
    {
        return getDBData(dbQuery, displayData, bindVars, desc, false,
                0, -1, true);
    }

    public static int getDBData(String dbQuery,
            Vector displayData, String[] bindVars, String[] desc,
            boolean disableSeqScan, int skiprows, int rowlimit,
            boolean displayException) throws SQLException, CustomException


    {
        s_Logger.debug("getDBData: "
                + "dbQuery=[" + dbQuery + "], bindVars=" 
                + Arrays.toString(bindVars));


        Connection prodconn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try
        {
            prodconn = dbconnection.get();

            pst = prodconn.prepareStatement(dbQuery);
            if (bindVars != null)
            {
                if (desc == null)
                    for (int i = 0; i < bindVars.length; i++)
                        pst.setString(i + 1, bindVars[i]);
                else
                    for (int i = 0; i < bindVars.length; i++)
                    {
                        if (desc[i].equals("I"))
                            try
                            {
                                pst.setInt(i + 1,
                                        Integer.valueOf(bindVars[i]).intValue());
                            } catch (NumberFormatException excp)
                            {
                                s_Logger.warn("dbDAL.getDBData:Bind Variable ["
                                        + i + "] = " + bindVars[i]
                                        + " is not an Int");
                                throw excp;
                            }
                        else if (desc[i].equals("L"))
                            try
                            {
                                pst.setLong(i + 1, Long.parseLong(bindVars[i]));
                            } catch (NumberFormatException excp)
                            {
                                s_Logger.warn("dbDAL.getDBData:Bind Variable ["
                                        + i + "] = " + bindVars[i]
                                        + " is not a Long");
                                throw excp;
                            }
                        else if (desc[i].equals("F"))
                        	try
                        	{
                        	pst.setFloat(i + 1, Float.parseFloat(bindVars[i]));
                        	} catch (NumberFormatException excp)
                        	{
                        	s_Logger.warn("dbDAL.getDBData:Bind Variable ["
                        	+ i + "] = " + bindVars[i]
                        	+ " is not a Long");
                        	throw excp;
                        	}
                        else if (desc[i].equals("T"))
                        {
                            try
                            {
                                long ts = Long.parseLong(bindVars[i]);
                                Timestamp timestamp = new Timestamp(ts);
                                pst.setTimestamp(i + 1, timestamp);
                            } catch (NumberFormatException excp)
                            {
                                s_Logger.warn("dbDAL.getDBData:Bind Variable ["
                                        + i + "] = " + bindVars[i]
                                        + " is not a timestamp");
                                throw excp;
                            }
                        } else
                            pst.setString(i + 1, bindVars[i]);
                    }
            }
            rs = pst.executeQuery();

            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            int numRows = 0;

            while (numRows < skiprows)
            {
                if (!rs.next())
                    break;
                numRows++;
            }
            numRows = 0; // Reset numRows if we skipped any rows
            while (rs.next() && ((numRows++ < rowlimit) || (rowlimit == -1)))
            {
                for (int i = 0; i < columnCount; i++)
                    displayData.addElement(rs.getString(i + 1));
            }

            errorCode = DAL_SUCCESS;

        } catch (NumberFormatException excp)
        {
            s_Logger.warn("NumberFormatException for :" + dbQuery);
            if (bindVars != null)
                for (int i = 0; i < bindVars.length; i++)
                    s_Logger.info("bindVar[" + i + "] = " + bindVars[i]);
            // ONLY SHOW ERROR MESSAGE
            //excp.printStackTrace();
            errorCode = DAL_UNKNOWN_ERR;            
            throw excp;
        } catch (SQLException excp)
        {
            if (displayException)
            {
                s_Logger.warn("SQLException for:" + dbQuery);
                if (bindVars != null)
                    for (int i = 0; i < bindVars.length; i++)
                        s_Logger.info("bindVar[" + i + "] = "
                                + bindVars[i]);
                s_Logger.warn("ErrorCode: " + excp.getErrorCode()
                        + " SQLState:" + excp.getSQLState(), excp);;
               // ONLY SHOW ERROR MESSAGE
                        // excp.printStackTrace();
                errorCode = DAL_UNKNOWN_ERR;
            }            
            errorCode = excp.getErrorCode();
            throw excp;
        } /*catch (CustomException e)
        {

            s_Logger.warn("Exception " + e.getMessage()
                    + " in dbDAL.getDBData()");
            throw e;
        } */finally
        {
            if (rs != null)
                try
                {
                    rs.close();
                    rs = null;
                } catch (Exception e)
                {
                    s_Logger.warn("Exception closing DB Result Set for :"
                            + dbQuery, e);
                }
            if (pst != null)
                try
                {
                    pst.close();
                    pst = null;
                } catch (Exception e)
                {
                    s_Logger.warn("Exception closing SQL Statement:"
                            + dbQuery, e);
                }

            if (prodconn != null)
                dbconnection.release(prodconn);
        }
        return errorCode;
    }
    
    
    public static int doDBOperation(String dbQuery)
            throws SQLException, CustomException
    {
        return doDBOperation(dbQuery, zeroArr, nullArr);
    }

    public static int doDBOperation(String dbQuery,
            String[] bindVars) throws SQLException, CustomException
    {
        return doDBOperation(dbQuery, bindVars, nullArr);
    }
    
    public static int doDBOperation(String dbQuery,
            String[] bindVars, String[] desc) throws SQLException,
            CustomException
    {
    	String currentDB = "";
        s_Logger.debug("doDBOperation: DB="
                    + currentDB
                    + ", dbQuery=[" + dbQuery + "], bindVars=" 
                    + Arrays.toString(bindVars));

        PreparedStatement pst = null;

        Connection prodconn = null;
        ResultSet rs = null;

        try
        {
            prodconn = dbconnection.get();

            pst = prodconn.prepareStatement(dbQuery);
            if (desc == null)
                for (int i = 0; i < bindVars.length; i++)
                    pst.setString(i + 1, bindVars[i]);
            else
                for (int i = 0; i < bindVars.length; i++)
                {
                    if (desc[i].equals("I"))
                        try
                        {
                            pst.setInt(i + 1,
                                    Integer.valueOf(bindVars[i]).intValue());
                        } catch (NumberFormatException excp)
                        {
                            s_Logger.warn("dbDAL.doDBOperation:Bind Variable ["
                                    + i + "] = " + bindVars[i]
                                    + " is not an integer");
                            throw excp;
                        }
                    else if (desc[i].equals("L"))
                        try
                        {
                            pst.setLong(i + 1, Long.parseLong(bindVars[i]));
                        } catch (NumberFormatException excp)
                        {
                            s_Logger.warn("dbDAL.doDBOperation:Bind Variable ["
                                    + i + "] = " + bindVars[i]
                                    + " is not a Long");
                            throw excp;
                        }
                    else if (desc[i].equals("F"))
                    	try
                    	{
                    	pst.setFloat(i + 1, Float.parseFloat(bindVars[i]));
                    	} catch (NumberFormatException excp)
                    	{
                    	s_Logger.warn("dbDAL.getDBData:Bind Variable ["
                    	+ i + "] = " + bindVars[i]
                    	+ " is not a Long");
                    	throw excp;
                    	}
                    else if (desc[i].equals("B"))
                        try
                        {
                            FileInputStream nzfis = null;
                            File nzfile = new File(bindVars[i]);
                            nzfis = new FileInputStream(nzfile);
                            pst.setBinaryStream(i + 1, nzfis,
                                    (int) nzfile.length());
                        } catch (FileNotFoundException e)
                        {
                            s_Logger.warn("Db operation to " 
                                    + " insert the wallet in the database "
                                    + "failed , could not find the wallet file");
                        }
                    else
                        pst.setString(i + 1, bindVars[i]);
                }

            int com = pst.executeUpdate();
            if (com < 0)
            {
                // No insert operation done.
                errorCode = DAL_INSERT_ERR;                
            } else
            {
                // Added succesfully
                errorCode = DAL_SUCCESS;               
            }
            pst.close();
            pst = null;

        } catch (NumberFormatException excp)
        {
            s_Logger.warn("NumberFormatException for :" + dbQuery, excp);
            if (bindVars != null)
                for (int i = 0; i < bindVars.length; i++)
                    s_Logger.info("bindVar[" + i + "] = " + bindVars[i]);
            // ONLY SHOW ERROR MESSAGE
            //excp.printStackTrace();
            errorCode = DAL_UNKNOWN_ERR;            
            throw excp;
        } catch (SQLException excp)
        {
            // s_Logger.warn("DB Access Layer SQL Exception:" + dbQuery 
             //       + " ErrorCode: " + excp.getErrorCode()
             //       + " SQLState:" + excp.getSQLState(), excp);
            // ONLY SHOW ERROR MESSAGE
            //excp.printStackTrace();            
            errorCode = DAL_UNKNOWN_ERR;
            throw excp;
        } finally
        {
            if (rs != null)
                try
                {
                    rs.close();
                    rs = null;
                } catch (Exception e)
                {
               //     s_Logger.warn("Exception closing SQL Statement:"
                 //           + dbQuery , e);
                }
            if (pst != null)
                try
                {
                    pst.close();
                    pst = null;
                } catch (Exception e)
                {
               //     s_Logger.warn("Exception closing SQL Statement:"
                 //           + dbQuery, e);
                }
            if (prodconn != null)
                dbconnection.release(prodconn);
        }
        return errorCode;
    }
}
