package edu.ncsu.csc.dbproject.workflow;


import edu.ncsu.csc.dbproject.util.Utils;
import edu.ncsu.csc.dbproject.impl.ReportsImpl;
import edu.ncsu.csc.dbproject.impl.InfoProcessImpl;
import edu.ncsu.csc.dbproject.impl.BillingAccountsImpl;

/*
 * Handles all operations that can be performed by staff belonging to 'Salesperson' department
 */

public class Salesperson {

	String name;
	String deptName;
	String storeName;

	String custName;
	String custPhone;

	
	Salesperson(String login, String dept, String store){
		name = login;
		deptName = dept;
		storeName = store;
	}
	
	/*
	 * Start point for Salesperson. Displays all actions that can be performed by a salesperson and calls appropriate methods.
	 * Invokes appropriate method in the interfaces declared in edu.ncsu.csc.dbproject.api package 
	 */
	protected void initiateSalespersonWorkFlow(){
		String choice = "y";
		System.out.println("User: " + name);
		System.out.println("Department: " + deptName);
		System.out.println("Store: " + storeName);
		
		System.out.println("**************************************************************************************************");
		
		System.out.print("Enter Customer Name: ");
		custName = Utils.readOptionFromCmdLine();
		System.out.println();
		
		System.out.print("Enter Customer Phone Number: ");
		custPhone = Utils.readOptionFromCmdLine();
		System.out.println();
		
		System.out.println("**************************************************************************************************");
		
		InfoProcessImpl ipl = new InfoProcessImpl();
		if (ipl.checkCustomer(custName, custPhone) == 0)
		{
			enterCustomer();
		}
		
		while(choice.equals("y")){

			System.out.println("Select action:");
			System.out.println("1. View Customer orders & payments for a given period");
			System.out.println("2. Update Existing Customer");
			System.out.println("3. Delete Existing Customer");
			System.out.println("4. Order Books for Customer");
			System.out.println("5. Generate Customer Bill for Month");
			System.out.println("6. Make Customer Payment");
			System.out.println("7. Exit");
			
			String option = Utils.readOptionFromCmdLine();
			if(option == null){
				System.out.println("Invalid action for salesperson. Exiting ...");
		    	System.exit(1);
			}
		    
		    int opt = Integer.parseInt(option);

		    switch(opt){
		    
		    	case 1: customerHistoryReport();
		    			break;
		    	case 2: updateCustomer();		
		    			break;
		    	case 3: deleteCustomer();
    					break;
		    	case 4: orderBooks();
		    			break;
		    	case 5: generateCustomerBill();
		    			break;
		    	case 6: customerPayment();
    					break;
		    	case 7: Utils.printAndExit("");
		    	default: System.out.println("Invalid action for salesperson. Exiting ...");
		    			 System.exit(1);
		    
		    }
		    System.out.print("\nDo you want to perform more actions?(y/n) : ");
		    choice = Utils.readOptionFromCmdLine();
		}
		Utils.printAndExit("");
	    
	}
	
	/*
	 * Accepts input for action 'View Customer orders & payments for a given period' and prints results
	 */
	private void customerHistoryReport(){

		System.out.print("Start Date(ex - 01-JAN-12): ");
		String startDate = Utils.readOptionFromCmdLine();
		System.out.println();
		System.out.print("End Date(ex - 01-JAN-12): ");
		String endDate = Utils.readOptionFromCmdLine();
		System.out.println();
		
		System.out.println("Customer Name: " + custName);
		System.out.println("Customer Phone Number: " + custPhone);
		
		ReportsImpl report = new ReportsImpl();
		report.getCustomerHistory(custName, custPhone, startDate, endDate);
	}
	
	/*
	 * Accepts input for adding a customer and prints results
	 */
	private void enterCustomer(){
		
		InfoProcessImpl ipl = new InfoProcessImpl();
		if (ipl.checkCustomer(custName,custPhone )==1)
		{
			System.out.println("The customer already exists");
		}
		else
		{
			System.out.println("Enter details for Customer " + custName);
			//System.out.print("Is Preferred Customer(y/n): ");
			//String isPref = Utils.readOptionFromCmdLine();
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
			
			System.out.print("Date of Birth(ex - 01-JAN-90): ");
			String dob = Utils.readOptionFromCmdLine();
			System.out.println();
			
			InfoProcessImpl infoProcess = new InfoProcessImpl();
			infoProcess.enterCustomer("0", "0", custName, ssn, gender, address, custPhone, dob);
		}
	}
	
	
	/*
	 * Accepts input for action 'Update Existing Customer' and prints results
	 */
	private void updateCustomer(){
		System.out.println("Enter details for Customer " + custName);
		
		System.out.print("Address: ");
		String address = Utils.readOptionFromCmdLine();
		System.out.println();
		
		/*System.out.print("Is Preferred Customer(y/n): ");
		String isPref = Utils.readOptionFromCmdLine();
		System.out.println();*/
		
		System.out.print("Balance in $: ");
		String balance = Utils.readOptionFromCmdLine();
		System.out.println();
		
		InfoProcessImpl infoProcess = new InfoProcessImpl();
		infoProcess.updateCustomer(balance, "", custName, address, custPhone);
		
	}
	
	/*
	 * Accepts input for action 'Delete Existing Customer' and prints results
	 */
	private void deleteCustomer(){
		System.out.println("Enter details of Customer to be deleted");
		System.out.print("Name: ");
		String cname = Utils.readOptionFromCmdLine();
		System.out.println();
		
		System.out.print("Phone Number: ");
		String phone = Utils.readOptionFromCmdLine();
		System.out.println();
		
		InfoProcessImpl infoProcess = new InfoProcessImpl();
		infoProcess.deleteCustomer(cname, phone);

		if(cname.equals(custName) && phone.equals(custPhone)){
			Salesperson ob = new Salesperson(name,deptName,storeName);
			ob.initiateSalespersonWorkFlow();
			System.exit(1);
		}
	}
	
	/*
	 * Accepts input for action 'Order Books for Customer' and prints results
	 */
	private void orderBooks(){
		
		InfoProcessImpl infoProcess = new InfoProcessImpl();
		infoProcess.allAvailableBooks();
		
		System.out.print("Enter Book ISBN: ");
		String bookISBN = Utils.readOptionFromCmdLine();
		System.out.println();
		
		System.out.print("Enter Quantity: ");
		String quantity = Utils.readOptionFromCmdLine();
		System.out.println();
		
		System.out.println("Ordering books for " + custName + " ...");
		
		infoProcess.orderBooks(bookISBN, custName, custPhone, name, quantity);
	}
	
	/*
	 * Accepts input for action 'Generate Customer Bill for Month' and prints results
	 */
	private void generateCustomerBill(){

		System.out.print("Enter Billing Month and Year(ex - JAN-12): ");
		String billingMonth = Utils.readOptionFromCmdLine();
		System.out.println();
		
		String firstDate = "02-";                               /* BILLING CYCLE FROM 2Nd(00:00) of the month to 1st(23:59) of next month    */
		billingMonth = firstDate + billingMonth;
		
		System.out.println("Customer Name: " + custName);
		System.out.println("Customer Phone Number: " + custPhone);
		
		
		BillingAccountsImpl billingAcc = new BillingAccountsImpl();
		billingAcc.getCustomerBill(custName, custPhone, billingMonth);
	}
	
	
	
	/*
	 * Accepts input for action 'Make Customer Payment' and prints results
	 */
	private void customerPayment(){

		System.out.print("Amount in $: ");
		String amount = Utils.readOptionFromCmdLine();
		System.out.println();

		System.out.println("Making payment for Customer " + custName + " ...");

		InfoProcessImpl infoProcess = new InfoProcessImpl();
		infoProcess.customerPayment(custName, custPhone, amount);
	}
}
