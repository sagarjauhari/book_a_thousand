package edu.ncsu.csc.dbproject.util;

/*
 * Custom exception class for use in application
 */
public class CustomException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public CustomException(String mesg)
    {	
        super(mesg); 
    }
    
    public CustomException(String mesg, Throwable cause)
    {   
        super(mesg, cause); 
    }

}
