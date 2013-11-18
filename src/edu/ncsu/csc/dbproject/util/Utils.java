package edu.ncsu.csc.dbproject.util;

import java.util.Scanner;

/*
 * Utility class contains commonly used functions
 */
public class Utils {

	/*
	 * Reads user input from command line
	 */
	public static String readOptionFromCmdLine(){

		Scanner scan = new Scanner(System.in);
	    String option = null;

	    try {
	    	option = scan.nextLine();
	    } catch (Exception ioe) {
	       System.out.println("IO error trying to read your name!");
	       System.exit(1);
	    }

	    return option;
	}
	
	/*
	 * Prints message and shuts down the application
	 */
	public static void printAndExit(String msg){
		
		System.out.println(msg);
		System.out.println("Exiting...");
		System.exit(1);
	}
}
