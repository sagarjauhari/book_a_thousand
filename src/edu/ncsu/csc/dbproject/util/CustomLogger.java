package edu.ncsu.csc.dbproject.util;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.HashMap;

/*
 * Customer logger class used in the application. Uses java.util.logging.Logger.
 * Log levels: ERROR, WARN, INFO, DEBUG, TRACE
 */
public class CustomLogger{
	
	private Logger m_Logger = null;
	
	public enum LOG_LEVEL
    {
        ERROR, WARN, INFO, DEBUG, TRACE
    }
	
	private static HashMap<LOG_LEVEL, Level> s_logLevelMap = null;
	
	static{
		
		s_logLevelMap = new HashMap<LOG_LEVEL, Level>();
        s_logLevelMap.put(LOG_LEVEL.ERROR, Level.SEVERE);
        s_logLevelMap.put(LOG_LEVEL.WARN, Level.WARNING);
        s_logLevelMap.put(LOG_LEVEL.INFO, Level.INFO);
        s_logLevelMap.put(LOG_LEVEL.DEBUG, Level.FINE);
        s_logLevelMap.put(LOG_LEVEL.TRACE, Level.FINEST);
	}
	
	@SuppressWarnings("rawtypes")
	private CustomLogger(Class clazz)
    {
        m_Logger = Logger.getLogger(clazz.getName());
        m_Logger.setLevel(null); 
    }
	
	@SuppressWarnings("rawtypes")
	public static CustomLogger getInstance(Class clazz)
    {
        return new CustomLogger(clazz);
    }
	
	public String getName()
    {
        return m_Logger.getName();
    }
	
	private void log(LOG_LEVEL logLevel, String message, Throwable e)
    {
        if (e == null)
        {
            m_Logger.log(s_logLevelMap.get(logLevel), message);
        } else
        {
            m_Logger.log(s_logLevelMap.get(logLevel), message, e);
        }
    }
	
	public void error(String message) {
		log(LOG_LEVEL.ERROR, message, null);
    }
    
    public void error(String message, Throwable e) {
        log(LOG_LEVEL.ERROR, message, e);
    }

    public void warn(String message) {
        log(LOG_LEVEL.WARN, message, null);
    }
    
    public void warn(String message, Throwable e) {
        log(LOG_LEVEL.WARN, message, e);
    }
    
    public void info(String message) {
        log(LOG_LEVEL.INFO, message, null);
    }
    
    public void info(String message, Throwable e) {
        log(LOG_LEVEL.INFO, message, e);
    }
    
    public void debug(String message) {
        log(LOG_LEVEL.DEBUG, message, null);
    }
    
    public void debug(String message, Throwable e) {
        log(LOG_LEVEL.DEBUG, message, e);
    }
    
    public void trace(String message) {
        log(LOG_LEVEL.TRACE, message, null);
    }
    
    public void trace(String message, Throwable e) {
        log(LOG_LEVEL.TRACE, message, e);
    }
	
}
