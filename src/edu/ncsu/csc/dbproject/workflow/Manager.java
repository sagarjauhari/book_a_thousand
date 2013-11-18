package edu.ncsu.csc.dbproject.workflow;

import edu.ncsu.csc.dbproject.util.Utils;
import edu.ncsu.csc.dbproject.impl.InfoProcessImpl;
import edu.ncsu.csc.dbproject.impl.ReportsImpl;
import edu.ncsu.csc.dbproject.model.dbconnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/*
 * Handles all operations that can be performed by staff belonging to 'Management' department
 */

public class Manager{

	String name;
	String deptName;
	String storeName;

	
	Manager(String login, String dept, String store){
		name = login;
		deptName = dept;
		storeName = store;
	}
	
	/*
	 * Start point for Manager. Displays all actions that can be performed by a manager and calls appropriate methods.
	 * Invokes appropriate method in the interfaces declared in edu.ncsu.csc.dbproject.api package 
	 */
	protected void initiateManagerWorkFlow(){
		String choice = "y";
		System.out.println("User: " + name);
		System.out.println("Department: " + deptName);
		System.out.println("Store: " + storeName);
		System.out.println("**************************************************************************************************");
		
		while(choice.equals("y")){

			System.out.println("Select action:");
			System.out.println("1.  View Customer orders & payments for a given period");
			System.out.println("2.  View Salesperson Report for a given period");
			System.out.println("3.  View Existing Vendor Details");
			System.out.println("4.  Add Vendor");
			System.out.println("5.  Extend Vendor Contract");
			System.out.println("6.  Delete Existing Vendor");
			System.out.println("7.  View Staff Member by Department");
			System.out.println("8.  Add New Staff Member");
			System.out.println("9.  Update Existing Staff member");
			System.out.println("10. Delete Existing Staff member");
			System.out.println("11. Commit Rollback Demo for Order Book");
			System.out.println("12. Commit Rollback Demo for Extending Vendor Contract");
			System.out.println("13. Exit");
			
			String option = Utils.readOptionFromCmdLine();
			if(option == null){
				System.out.println("Invalid action for manager. Exiting ...");
		    	System.exit(1);
			}
		    
		    int opt = Integer.parseInt(option);
		    switch(opt){
		    
		    	case 1: customerHistoryReportMgr();
		    			break;
		    	case 2: salespersonReport();
		    			break;
		    	case 3: vendorReport();
		    			break;
		    	case 4: addVendor();		
    					break;
		    	case 5: extendVendorContract();		
    					break;
		    	case 6: deleteVendor();	
		    			break;
		    	case 7: searchStaff();
		    			break;
		    	case 8: addStaff();
		    			break;
		    	case 9: updateStaff();
		    			break;
		    	case 10: deleteStaff();
		    			break;
		    	case 11: commitRollbackOrderBook();
		    			break;
		    	case 12: commitRollbackExtendContract();
    					break;
		    	case 13: Utils.printAndExit("");
		    	default: System.out.println("Invalid action for manager. Exiting ...");
		    			 System.exit(1);
		    
		    }
		    System.out.print("\nDo you want to perform more actions?(y/n) : ");
		    choice = Utils.readOptionFromCmdLine();
		}
		Utils.printAndExit("");
	    
	}
	
	/*
	 * Accepts input for action 'Customer orders & payments for a given period' and prints results
	 */
	 private void customerHistoryReportMgr(){

		System.out.println("Enter Customer Details");
		System.out.print("Name: ");
		String name = Utils.readOptionFromCmdLine();
		System.out.println();
		
		System.out.print("Phone Number: ");
		String phone = Utils.readOptionFromCmdLine();
		System.out.println();
		
		System.out.print("Start Date(ex - 01-JAN-12): ");
		String startDate = Utils.readOptionFromCmdLine();
		System.out.println();
		
		System.out.print("End Date(ex - 01-JAN-12): ");
		String endDate = Utils.readOptionFromCmdLine();
		System.out.println();
				
		ReportsImpl report = new ReportsImpl();
		report.getCustomerHistory(name, phone, startDate, endDate);
	}
	
   /*
	* Accepts input for action 'Salesperson Report for a given period' and prints results
	*/ 
	private void salespersonReport(){
		System.out.print("Enter Salesperson Name: ");
		String sName = Utils.readOptionFromCmdLine();
		System.out.println();
		System.out.print("Start Date(ex - 01-JAN-12): ");
		String startDate = Utils.readOptionFromCmdLine();
		System.out.println();
		System.out.print("End Date(ex - 01-JAN-12): ");
		String endDate = Utils.readOptionFromCmdLine();
		System.out.println();
		
		ReportsImpl report = new ReportsImpl();
		report.getSalespersonReport(sName, startDate, endDate);
	}
	
	/*
	 * Accepts input or action 'Existing Vendor Details' and prints results
	 */
	private void vendorReport(){
		System.out.print("Enter Vendor Name('all' for all vendors): ");
		String vendorName = Utils.readOptionFromCmdLine();

		ReportsImpl report = new ReportsImpl();
		if(vendorName.equals("all")){
			report.getAllVendorsReport();
		}else{
			report.getVendorReport(vendorName);
		}
	}
	
	/*
	 * Accepts input or action 'View Staff Member by Department' and prints results
	 */
	private void searchStaff(){
		System.out.print("Enter Department Name('all' for all departments): ");
		String deptName = Utils.readOptionFromCmdLine();
		//System.out.println("deptName " + deptName);
		ReportsImpl report = new ReportsImpl();
		if(deptName.equals("all")){
			report.getAllStaffGroupedByRole(storeName);
		}else{
			report.getStaffByRole(deptName, storeName);
		}
	}
	
	/*
	 * Accepts input or action 'Add New Staff Member' and prints results
	 */
	private void addStaff(){
		System.out.println("Enter New Staff Member's details");
		System.out.print("Name: ");
		String staffName = Utils.readOptionFromCmdLine();
		System.out.println();
		
		System.out.print("Department Name(Management/Accounts/Sales): ");
		String deptName = Utils.readOptionFromCmdLine();
		System.out.println();
		
		System.out.print("Job Title: ");
		String jobTitle = Utils.readOptionFromCmdLine();
		System.out.println();
		
		System.out.print("Salary: ");
		String salary = Utils.readOptionFromCmdLine();
		System.out.println();

		System.out.print("Date of Birth(ex - 01-JAN-90): ");
		String dob = Utils.readOptionFromCmdLine();
		System.out.println();
		
		System.out.print("SSN: ");
		String ssn = Utils.readOptionFromCmdLine();
		System.out.println();
		
		System.out.print("Gender(Male/Female): ");
		String gender = Utils.readOptionFromCmdLine();
		System.out.println();
		
		System.out.print("Address: ");
		String address = Utils.readOptionFromCmdLine();
		System.out.println();
		
		System.out.print("Phone Number: ");
		String phone = Utils.readOptionFromCmdLine();
		System.out.println();
		
		InfoProcessImpl infoProcess = new InfoProcessImpl();
		infoProcess.enterStaff(staffName, salary, jobTitle, deptName, storeName, dob, ssn, gender, address, phone);
		
	}
	
	/*
	 * Accepts input or action 'Update Existing Staff member' and prints results
	 */
	private void updateStaff(){
		System.out.println("Enter New Staff Member's details");
		
		System.out.print("Name: ");
		String staffName = Utils.readOptionFromCmdLine();
		System.out.println();
		
		System.out.print("Phone Number: ");
		String phone = Utils.readOptionFromCmdLine();
		System.out.println();
		
		System.out.print("Department Name(Management/Accounts/Sales): ");
		String deptName = Utils.readOptionFromCmdLine();
		System.out.println();
		
		System.out.print("Job Title: ");
		String jobTitle = Utils.readOptionFromCmdLine();
		System.out.println();
		
		System.out.print("Salary: ");
		String salary = Utils.readOptionFromCmdLine();
		System.out.println();
		
		System.out.print("Address: ");
		String address = Utils.readOptionFromCmdLine();
		System.out.println();
		
		InfoProcessImpl infoProcess = new InfoProcessImpl();
		infoProcess.updateStaff(staffName, jobTitle, deptName, salary, address, phone);
	}
	
	/*
	 * Accepts input or action 'Delete Existing Staff member' and prints results
	 */
	private void deleteStaff(){
		System.out.println("Enter Details of Staff Member to be deleted");
		System.out.print("Name: ");
		String staffName = Utils.readOptionFromCmdLine();
		System.out.println();
		
		System.out.print("Phone Number: ");
		String phone = Utils.readOptionFromCmdLine();
		System.out.println();
		
		InfoProcessImpl infoProcess = new InfoProcessImpl();
		infoProcess.deleteStaff(staffName, phone);
	}	
	
	/*
	 * Accepts input or action 'Add Vendor' and prints results
	 */
	private void addVendor(){
		System.out.println("Enter New Vendor's details");
		System.out.print("Name: ");
		String vendorName = Utils.readOptionFromCmdLine();
		System.out.println();
		
		System.out.print("Phone Number: ");
		String phone = Utils.readOptionFromCmdLine();
		System.out.println();
		
		System.out.print("Address: ");
		String address = Utils.readOptionFromCmdLine();
		System.out.println();
		
		System.out.println("Vendor Contract starts from today");

		System.out.print("Contract End Date(ex - 01-JAN-90): ");
		String endDate = Utils.readOptionFromCmdLine();
		System.out.println();
		
		InfoProcessImpl infoProcess = new InfoProcessImpl();
		infoProcess.enterVendor(vendorName, address, phone, endDate);
	}
	
	/*
	 * Accepts input or action 'Extend Vendor Contract' and prints results
	 */
	private void extendVendorContract(){
		
		System.out.print("Vendor Name: ");
		String vendorName = Utils.readOptionFromCmdLine();
		System.out.println();
		
		System.out.print("New Contract End Date(ex - 01-JAN-90): ");
		String endDate = Utils.readOptionFromCmdLine();
		System.out.println();
		
		InfoProcessImpl infoProcess = new InfoProcessImpl();
		infoProcess.updateContractEndDate(vendorName, endDate);
		
	}
	
	/*
	 * Accepts input or action 'Delete Existing Vendor' and prints results
	 */
	private void deleteVendor(){
		System.out.print("Enter Name of Vendor to be deleted: ");
		String vendorName = Utils.readOptionFromCmdLine();
		System.out.println();
		
		InfoProcessImpl infoProcess = new InfoProcessImpl();
		infoProcess.deleteVendor(vendorName);
	}
	
	private void commitRollbackOrderBook(){
		
		InfoProcessImpl infoProcess = new InfoProcessImpl();
		infoProcess.allAvailableBooks();
		
		System.out.print("Enter Book ISBN: ");
		String bookISBN = Utils.readOptionFromCmdLine();
		System.out.println();
		
		System.out.print("Enter Quantity: ");
		String quantity = Utils.readOptionFromCmdLine();
		System.out.println();
		
		String custName = "Bob";
		String custPhone = "392-82-1942";
		String salespersonName = "George Carlin";
		
		
		String query="INSERT INTO customer_order VALUES (SYS_GUID(), 'NOT SHIPPED', " +
				"(SELECT to_date(to_char(sysdate,'yyyy/mm/dd hh24:mi:ss'),'yyyy/mm/dd hh24:mi:ss') FROM dual)," +
				"(SELECT pid FROM people WHERE  name=? ),(SELECT pid FROM people WHERE name=? AND phone =?),?," +
				"(SELECT id from merchandise WHERE ISBN=?))";
		
		Connection conn = null;
		PreparedStatement pst = null;
		try
        {
			conn = dbconnection.get();
			conn.setAutoCommit(false);
			pst = conn.prepareStatement(query);
            pst.setString(1, salespersonName);
            pst.setString(2, custName);
            pst.setString(3, custPhone);
            pst.setInt(4, Integer.valueOf(quantity).intValue());
            pst.setString(5, bookISBN);
            pst.executeUpdate();
        }catch(SQLException e){
        	System.out.println("Transaction failed!");
        	try {
				conn.rollback();
				System.out.println("Connection Rolled Back");
				return;
			} catch (SQLException e1) {
				System.out.println("\nSystem Error: "+e.getMessage());
				return;
			}
        }finally{
            if (pst != null)
                try
                {
                    pst.close();
                    pst = null;
                } catch (Exception e)
                {
                	System.out.println("\nSystem Error: "+e.getMessage());
                	return;
                }
        }
        	
		System.out.println("Transaction passed!");
    	try {
			conn.commit();
			System.out.println("Connection Committed");
		} catch (SQLException e) {
			System.out.println("\nSystem Error: "+e.getMessage());
		}
    	dbconnection.release(conn);

	}
	
	private void commitRollbackExtendContract(){
		
		System.out.print("Vendor Name: ");
		String vendorName = Utils.readOptionFromCmdLine();
		System.out.println();
		
		System.out.print("New Contract End Date(ex - 01-JAN-90): ");
		String endDate = Utils.readOptionFromCmdLine();
		System.out.println();
		
		String query = "Update contracts SET ENDDATE = to_date(?, 'dd-MON-yy') where vendorid = (SELECT v.id FROM vendors v WHERE v.name = ?)" ;
		
		Connection conn = null;
		PreparedStatement pst = null;
		try
        {
			conn = dbconnection.get();
			conn.setAutoCommit(false);
			pst = conn.prepareStatement(query);
            pst.setString(1, endDate);
            pst.setString(2, vendorName);
            pst.executeUpdate();
        }catch(SQLException e){
        	System.out.println("Transaction failed!");
        	try {
				conn.rollback();
				System.out.println("Connection Rolled Back");
				return;
			} catch (SQLException e1) {
				System.out.println("\nSystem Error: "+e.getMessage());
				return;
			}
        }finally{
            if (pst != null)
                try
                {
                    pst.close();
                    pst = null;
                } catch (Exception e)
                {
                	System.out.println("\nSystem Error: "+e.getMessage());
                	return;
                }
        }
        	
		System.out.println("Transaction passed!");
    	try {
			conn.commit();
			System.out.println("Connection Committed");
		} catch (SQLException e) {
			System.out.println("\nSystem Error: "+e.getMessage());
		}
    	dbconnection.release(conn);
	}

}
