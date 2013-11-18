package edu.ncsu.csc.dbproject.workflow;

import edu.ncsu.csc.dbproject.impl.InfoProcessImpl;
import edu.ncsu.csc.dbproject.impl.ReportsImpl;
import edu.ncsu.csc.dbproject.impl.BillingAccountsImpl;
import edu.ncsu.csc.dbproject.impl.VendorUsageRecImpl;
import edu.ncsu.csc.dbproject.util.Utils;


/*
 * Handles all operations that can be performed by staff belonging to 'VendorAccountant' department
 */
public class VendorAccountant {
	
	String name;
	String deptName;
	String storeName;

	String vendorName;
	
	VendorAccountant(String login, String dept, String store){
		name = login;
		deptName = dept;
		storeName = store;
	}
	
	/*
	 * Start point for Vendor Accountant. Displays all actions that can be performed by a vendor accountant and calls appropriate methods.
	 * Invokes appropriate method in the interfaces declared in edu.ncsu.csc.dbproject.api package 
	 */
	protected void initiateVendorAccountantWorkFlow(){
		String choice = "y";
		System.out.println("User: " + name);
		System.out.println("Department: " + deptName);
		System.out.println("Store: " + storeName);
		
		System.out.println("**************************************************************************************************");
		
		System.out.print("Enter Vendor Name: ");
		vendorName = Utils.readOptionFromCmdLine();
		System.out.println();
		
		System.out.println("**************************************************************************************************");
		
		while(choice.equals("y")){

			System.out.println("Select action:");
			System.out.println("1. View Existing Vendor Details");
			System.out.println("2. View Warehouse Contents/Available Books");
			System.out.println("3. View All Orders to Vendor");
			System.out.println("4. View Vendor orders & payments for a given period");
			System.out.println("5. Place Order to Vendor");
			System.out.println("6. Generate Vendor Bill for Month");
			System.out.println("7. Make Payment to Vendor");	
			System.out.println("8. Exit");
			
			String option = Utils.readOptionFromCmdLine();
			if(option == null){
				System.out.println("Invalid action for manager. Exiting ...");
		    	System.exit(1);
			}
		    
		    int opt = Integer.parseInt(option);
		    System.out.println("User selected option " + option);
		    switch(opt){
		    	
		    	case 1: vendorReport();
		    			break;
		    	case 2: viewWarehouseContents();
		    			break;
		    	case 3: viewAllVendorOrders();
    					break;
		    	case 4: viewVendorOrders();
    					break;
		    	case 5: orderBookFromVendor();
    					break;
		    	case 6: generateVendorBill();
    					break;		
		    	case 7: vendorPayment();
		    			break;
		    	case 8: Utils.printAndExit("");
		    	default: System.out.println("Invalid action for manager. Exiting ...");
		    			 System.exit(1);
		    
		    }
		    System.out.print("\nDo you want to perform more actions?(y/n) : ");
		    choice = Utils.readOptionFromCmdLine();
		}
		Utils.printAndExit("");
	}


	/*
	 * Accepts input for action 'View Warehouse Contents/Available Books' and prints results
	 */
	private void viewWarehouseContents(){
		
		InfoProcessImpl infoProcess = new InfoProcessImpl();
		infoProcess.allAvailableBooks();
	}
	
	/*
	 * Accepts input for action 'View Existing Vendor Details' and prints results
	 */
	private void vendorReport(){
		System.out.print("Report for all Vendors(y/n) : ");
		String choice = Utils.readOptionFromCmdLine();
		ReportsImpl report = new ReportsImpl();
		if(choice.equals("y")){
			report.getAllVendorsReport();
		}else{
			report.getVendorReport(vendorName);
		}
	}
	
	/*
	 * Accepts input for action 'Make Payment to Vendor' and prints results
	 */
	private void vendorPayment(){
		
		System.out.print("Amount in $: ");
		String amount = Utils.readOptionFromCmdLine();
		System.out.println();
		
		InfoProcessImpl infoProcess = new InfoProcessImpl();
		infoProcess.vendorPayment(vendorName, amount);
	}
	
	/*
	 * Accepts input for action 'Place Order to Vendor' and prints results
	 */
	private void orderBookFromVendor(){
		
		InfoProcessImpl infoProcess = new InfoProcessImpl();
		infoProcess.allAvailableMerchandise();
		
		System.out.println("Enter Book details:");
		
		System.out.print("ISBN: ");	
		String bookISBN = Utils.readOptionFromCmdLine();
		System.out.println();

		System.out.print("Quantity: ");
		String quantity = Utils.readOptionFromCmdLine();
		System.out.println();
		
		VendorUsageRecImpl vuri = new VendorUsageRecImpl();
		vuri.addVendorPurchaseRecord("NOT SHIPPED", vendorName, bookISBN, quantity);
	}
	
	/*
	 * Accepts input for action 'View All Orders to Vendor' and prints results
	 */
	private void viewAllVendorOrders(){
		
		VendorUsageRecImpl vuri = new VendorUsageRecImpl();
		vuri.getVendorPurchaseHistory(vendorName);
	}
	
	/*
	 * Accepts input for action 'View Vendor orders & payments for a given period' and prints results
	 */
	private void viewVendorOrders(){

		System.out.print("Start Date(ex - 01-JAN-12): ");
		String startDate = Utils.readOptionFromCmdLine();
		System.out.println();
		
		System.out.print("End Date(ex - 01-JAN-12): ");
		String endDate = Utils.readOptionFromCmdLine();
		System.out.println();
		
		ReportsImpl report = new ReportsImpl();
		report.getVendorHistory(vendorName, startDate, endDate);
	}
	
	/*
	 * Accepts input for action 'Generate Vendor Bill for Month' and prints results
	 */
	private void generateVendorBill(){
		
		System.out.print("Enter Billing Month and Year(ex - JAN-12): ");
		String billingMonth = Utils.readOptionFromCmdLine();
		System.out.println();
		
		String firstDate = "02-";                               /* BILLING CYCLE FROM 2Nd(00:00) of the month to 1st(23:59) of next month    */
		billingMonth = firstDate + billingMonth;
		
		BillingAccountsImpl billingAcc = new BillingAccountsImpl();
		billingAcc.getVendorBill(vendorName, billingMonth);
	}
	
}
