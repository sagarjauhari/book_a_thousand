package edu.ncsu.csc.dbproject.workflow;

import java.sql.SQLException;
import java.util.Vector;

import edu.ncsu.csc.dbproject.model.dbDAL;
import edu.ncsu.csc.dbproject.util.CustomException;
import edu.ncsu.csc.dbproject.util.Utils;

/*
 * The main class for the Book Store application. 
 * Accepts staff name as login and verifies if it is a valid staff member or not.
 * Determines the department of the staff and initializes an appropriate workflow class (Manager, Salesperson, VendorAccount)
 */

public class BookStoreWorkFlow {

	@SuppressWarnings("rawtypes")
	public static void main(String[] args) {

		System.out.print("Enter Name: ");
		String name = Utils.readOptionFromCmdLine();
		if(name == null){
			Utils.printAndExit("Invalid name of staff");
		}

		String query = "SELECT staff.dept_name, store.short_name FROM staff, people, store WHERE people.pid=staff.pid AND staff.store_id=store.id AND people.name=?";
		String bindVars[] = {name};
		Vector results = new Vector();
		
		try {
			dbDAL.getDBData(query, results, bindVars);
		} catch (SQLException e) {
			Utils.printAndExit(e.getMessage());
		} catch (CustomException e) {
			Utils.printAndExit(e.getMessage());
		}
		
		if(results.size() == 0){
			Utils.printAndExit("There is no staff member with name: " + name);
		}
		
	    String deptName = (String)results.elementAt(0);
	    String storeName = (String)results.elementAt(1);
	    if(deptName.equals("Management")){
	    	Manager mgr = new Manager(name, deptName, storeName);
	    	mgr.initiateManagerWorkFlow();
	    }else if(deptName.equals("Accounts")){
	    	VendorAccountant vAcc = new VendorAccountant(name, deptName, storeName);
	    	vAcc.initiateVendorAccountantWorkFlow();
	    }else if(deptName.equals("Sales")){
	    	Salesperson sales = new Salesperson(name, deptName, storeName);
	    	sales.initiateSalespersonWorkFlow();
	    }else{
	    	Utils.printAndExit("Invalid department name " + deptName);
	    }
	}
}
