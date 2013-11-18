package edu.ncsu.csc.dbproject.impl;
import edu.ncsu.csc.dbproject.model.dbDAL;
import edu.ncsu.csc.dbproject.io.IO;


import java.sql.*;
import java.util.Vector;


import edu.ncsu.csc.dbproject.api.InfoProcess;
import edu.ncsu.csc.dbproject.util.CustomException;



public class InfoProcessImpl implements InfoProcess {
	
	IO io = new IO();
	
	@Override
	public void updateStaff( String name, String jobTitle,
			String deptName, String salary,String address, String phone) {
		// TODO Auto-generated method stub
		
		/* Query for  Updating job title,dept_name,store_id in Staff   */
		String jT="UPDATE STAFF SET job_title =? WHERE pid=(select pid from people NATURAL JOIN staff where name=? AND phone=?)";
		String dN="UPDATE STAFF SET dept_name=? WHERE pid=(select pid from people NATURAL JOIN staff where name=? AND phone=?)";
		String sN="UPDATE STAFF SET salary=? WHERE pid=(select pid from people NATURAL JOIN staff where name=? AND phone=?)";
		
		/* Query address in people in Staff   */
		String add="UPDATE PEOPLE SET address=? WHERE pid=(select pid from people NATURAL JOIN staff where name=? AND phone=?)";
		
		String[] bindVarsjT= new String[3];
		String[] bindVarsdN= new String[3];
		String[] bindVarssN= new String[3];
		String[] bindVarsadd= new String[3];
				
		bindVarsjT[0]=jobTitle;
		bindVarsjT[1]=name;
		bindVarsjT[2]=phone;
		
		bindVarssN[0]=salary;
		bindVarssN[1]=name;
		bindVarssN[2]=phone;
		
		bindVarsdN[0]=deptName;
		bindVarsdN[1]=name;
		bindVarsdN[2]=phone;
		
		
		
		/* ASSUMPTION name,gender,date of Birth,ssn,gender,phone CANNOT BE CHANGED FOR A STAFF" */
		bindVarsadd[0]=address;
		bindVarsadd[1]=name;
		bindVarsadd[2]=phone;
		//bindVars[2]=storeName;
		//bindVars[3]=name;
		
		
		
		int errorcode=0,errorcode1=0,errorcode2=0,errorcode3=0;
		try {
				/* Updating the non-empty paramenter sent */
				if (!jobTitle.equalsIgnoreCase(""))
						errorcode=dbDAL.doDBOperation(jT,bindVarsjT);
				if (!deptName.equalsIgnoreCase(""))
						errorcode1=dbDAL.doDBOperation(dN,bindVarsdN );
				if (!salary.equalsIgnoreCase(""))
					errorcode2=dbDAL.doDBOperation(sN,bindVarssN );
				if (!address.equalsIgnoreCase(""))
					errorcode3=dbDAL.doDBOperation(add,bindVarsadd);
			}
		catch(SQLException e){
			
			System.out.println("System Error:"+e.getMessage());
		}
		catch(CustomException e){
			System.out.println("System Error:"+e.getMessage());
		}
		
		/* Printing appropriate message the non-empty paramenters sent */
		if (errorcode == 0 && errorcode1==0 && errorcode2==0 && errorcode3==0 )
				System.out.println("\n***Update Successful***");
		else
				System.out.println("\n***Update Unsuccessful ***");
		

	}

	@Override
	public void enterStaff(String name,String salary, String jobTitle,
			String deptName, String storeName, String dateOfBirth, String ssn,
			String gender, String address, String phone) {
		// TODO Auto-generated method stub
		
		String[] bindVars1=new String [6];
		/* Query for inserting in people  */
		String query_one="INSERT INTO people VALUES (SYS_GUID(),?,?,?,?,?,?)";
		bindVars1[0]=name;
		bindVars1[1]=ssn;
		bindVars1[2]=gender;
		bindVars1[3]=address;
		bindVars1[4]=phone;
		bindVars1[5]=dateOfBirth;
		
		/* Query for inserting in staff   */
		String query_two="Insert into staff values ((SELECT pid FROM people p WHERE p.Name= ? AND p.phone = ?),?,?,?,(SELECT id FROM store WHERE short_name =?))";
		String[] bindVars=new String[6];
		String[] desc={"S","S","I","S","S","S"};
		
		bindVars[0]=name;
		bindVars[1]=phone;
		bindVars[2]=salary;
		bindVars[3]=jobTitle;
		bindVars[4]=deptName;
		bindVars[5]=storeName;
		
		
		int errorcode=-1,errorcode1=-1;
		
		
		try {
			
			errorcode=dbDAL.doDBOperation(query_one,bindVars1);
			errorcode1=dbDAL.doDBOperation(query_two,bindVars,desc);
			
		}
		catch(SQLException e){
			System.out.println("\n***Error while entering the values:"+e.getMessage());
			}
		catch(CustomException e){
			System.out.println("***Error while entering the values:"+e.getMessage());
		}
		/* Printing appropriate message  */
		if (errorcode == 0 && errorcode1==0)
			System.out.println("\n***Insert Successful***");
		else
			System.out.println("\n***Insert Unsuccessful***");
	



	}

	@Override
	public void updateCustomer(String balance,
			String preferredCustomer, String name, 
			String address, String phone) {
		// TODO Auto-generated method stub
		/* Query fot  Updating Customer   */
		String jT="UPDATE CUSTOMER SET preferredCustomer =? WHERE pid=(select pid from people NATURAL JOIN customer where name=? AND phone=?)";
		String dN="UPDATE CUSTOMER SET balance=? WHERE pid=(select pid from people NATURAL JOIN customer where name=? AND phone=?)";
		
		/* Query for updating people    */
		String add="UPDATE PEOPLE SET address=? WHERE pid=(select pid from people NATURAL JOIN customer where name=? AND phone=?)";
		
		
		String[] bindVarsjT= new String[3];
		String[] bindVarsdN= new String[3];
		String[] bindVarsadd= new String[3];
		
		bindVarsjT[0]=preferredCustomer;
		bindVarsjT[1]=name;
		bindVarsjT[2]=phone;
		
		String []desc1 ={"I","S","S"};
		bindVarsdN[0]=balance;
		bindVarsdN[1]=name;
		bindVarsdN[2]=phone;
		
		
		
		/* ASSUMPTIOn name,gender,date of Birth,ssn,gender,phone CANNOT BE CHANGED FOR A CUSTOMER" */
		bindVarsadd[0]=address;
		bindVarsadd[1]=name;
		bindVarsadd[2]=phone;
		//bindVars[2]=storeName;
		//bindVars[3]=name;
		
		int errorcode=0,errorcode1=0,errorcode2=0,errorcode3=0;
		try {
			
				if (!preferredCustomer.equalsIgnoreCase(""))
						errorcode=dbDAL.doDBOperation(jT,bindVarsjT);
				if (!balance.equalsIgnoreCase(""))
						errorcode1=dbDAL.doDBOperation(dN,bindVarsdN,desc1);
				if (!address.equalsIgnoreCase(""))
					errorcode3=dbDAL.doDBOperation(add,bindVarsadd);
			}
		catch(SQLException e){
			System.out.println("\n***Error while entering the values:"+e.getMessage());
			//System.out.println("Exception:"+e);
		}
		catch(CustomException e){
			System.out.println("\n***Error while entering the values:"+e.getMessage());
			//System.out.println("Exception:"+e);
		}

		/* Printing appropriate message */
		if (errorcode == 0 && errorcode1==0 && errorcode2==0 && errorcode3==0 )
				System.out.println("\n***Update Successful***");
		else
				System.out.println("\n***Update Unsuccessful ***");
		

	}
	
	public int checkCustomer(String name , String phone)
	{

		String check="Select * from people where name= ? AND phone=?";
		String[] checkbindVars= {name,phone};
		int flag=0;
		Vector results = new Vector();
		try{
			
		
		dbDAL.getDBData(check, results, checkbindVars);
			if (!results.isEmpty())
						flag= 1;				
			else
						flag= 0;
		}
		catch (Exception e)
		{
			System.out.println("System Error:"+e.getMessage());
		}
		
		return flag;
	}
	
	@Override
	public void enterCustomer( String balance,
			String preferredCustomer, String name, String ssn, String gender,
			String address, String phone, String dateofBirth) {
		// TODO Auto-generated method stub
		String query="Insert into customer values ((SELECT pid FROM people p WHERE p.name =? AND p.phone = ?),?,?)";
		String query_one="INSERT INTO people VALUES (SYS_GUID(),?,?,?,?,?,?)";
		
		String[] bindVars= new String[4];
		String[] bindVars1=new String [6];
		
		String [] desc={"S","S","I","S"};
		bindVars[0]=name;
		bindVars[1]=phone;
		bindVars[2]=balance;
		bindVars[3]=preferredCustomer;
		
		bindVars1[0]=name;
		bindVars1[1]=ssn;
		bindVars1[2]=gender;
		bindVars1[3]=address;
		bindVars1[4]=phone;
		bindVars1[5]=dateofBirth;
		int errorcode=-1,errorcode1=-1;
		try {
			
			errorcode1=dbDAL.doDBOperation(query_one, bindVars1);
		    errorcode=dbDAL.doDBOperation(query,bindVars,desc);
		}
						
		catch(SQLException e){
			System.out.println("\n***Error while entering the values:"+e.getMessage());

		}
		catch(CustomException e){
			System.out.println("\n***Error while entering the values:"+e.getMessage());

		}
		if (errorcode == 0 && errorcode1==0)
			System.out.println("\n***Insert Customer Successful***");
		else
			System.out.println("\n***Insert Customer UnSuccessful***");
	}
		
	

	

	@Override
	public void availableBookByName(String bookName) {
		// TODO Auto-generated method stub
		
		String query="Select m.name,m.author,w.selling_price,m.ISBN from merchandise m, warehouse_contents w WHERE m.id=w.mer_id AND name=?";
		String[] bindVars= new String[1];
		bindVars[0]=bookName;
		Vector results=new Vector();
		String []col = {"name","author","selling price","ISBN"};
		try {
			
				dbDAL.getDBData(query, results, bindVars);
					
		}
		catch(SQLException e){
			System.out.println("\n***Error while searching for books:"+e.getMessage());
		}
		catch(CustomException e){
			System.out.println("\n***Error while searching for books:"+e.getMessage());
		}
		
		io.print("AVAILABLE BOOKS BY NAME",results,col);
	}

	@Override
	public void availableBookByAuthor(String author) {
		// TODO Auto-generated method stub
		String query="Select m.name,m.author,w.selling_price,m.ISBN from merchandise m, warehouse_contents w WHERE m.id=w.mer_id AND author=?";
		String[] bindVars= new String[1];
		bindVars[0]=author;
		String []col = {"name","author","selling price","ISBN"};
		Vector results=new Vector();
		try {
			
				dbDAL.getDBData(query, results, bindVars);
					
			
		}
		catch(SQLException e){
			System.out.println("\n***Error while searching for books :"+e.getMessage());
		}
		catch(CustomException e){
			System.out.println("\n***Error while searching for books :"+e.getMessage());
		}
		
		io.print("AVAILABLE BOOKS BY AUTHOR",results,col);
	}

	@Override
	public void availableBooks(String author, String bookName) {
		// TODO Auto-generated method stub
		
		String query="Select m.name,m.author,w.selling_price,m.ISBN from merchandise m, warehouse_contents w WHERE m.id=w.mer_id AND name=? AND author=?";
		String[] bindVars= new String[2];
		bindVars[0]=bookName;
		bindVars[1]=author;
		Vector results=new Vector();
		
		String []col = {"name","author","selling price","ISBN"};
		try {
			
			
			dbDAL.getDBData(query, results, bindVars);
								
			
		}
		catch(SQLException e){
			System.out.println("\n***Error while searching for books:"+e.getMessage());
		}
		catch(CustomException e){
			System.out.println("\n***Error while searching for books:"+e.getMessage());
		}
		
		io.print("AVAILABLE BOOKS BY NAME AND AUTHOR",results,col);
	}

	@Override
	public void allAvailableBooks() {
		// TODO Auto-generated method stub
		String query="Select m.name,m.author,w.selling_price,m.ISBN, w.qty_in_stock from merchandise m, warehouse_contents w WHERE m.id=w.mer_id ";
		Vector results=new Vector();
		String []col = {"name","author","selling price","ISBN", "quantity in stock"};
		try {
			
				dbDAL.getDBData(query, results);
								
			}
		catch(SQLException e){
			System.out.println("\n***Error while searching for books:"+e.getMessage());
		}
		catch(CustomException e){
			System.out.println("\n***Error while searching for books:"+e.getMessage());
		}
		
		io.print("ALL AVAILABLE BOOKS",results,col);
	}
	
	@Override
	public void allAvailableMerchandise()	{
		String query="Select ISBN,m.name,author, v.name from merchandise m, vendors  v where m.vendor_id=v.id";
		Vector results=new Vector();
		String []col = {"ISBN","BookName","Author","Vendor Name"};
		try {
			
				dbDAL.getDBData(query, results);
								
			}
		catch(SQLException e){
			System.out.println("\n***Error while searching for books:"+e.getMessage());
		}
		catch(CustomException e){
			System.out.println("\n***Error while searching for books:"+e.getMessage());
		}
		
		io.print("MERCHANDISE CATALOGUE",results,col);
	}
	
	@Override
	public void updateContracts(String vendorName, String startDate,
			String endDate, String contractDetails) {
		// TODO Auto-generated method stub
		String query="UPDATE contracts SET startdate=?, enddate=?,details=? WHERE name=?";
		String []bindVars= new String[4];
		bindVars[0]=startDate;
		bindVars[1]=endDate;
		bindVars[2]=contractDetails;
		bindVars[3]=vendorName;
		Vector results= new Vector();
		try {
			
				dbDAL.getDBData(query, results, bindVars);
								
			
		}
		catch(SQLException e){
			System.out.println("\n***Error while updating contracts:"+e.getMessage());
		}
		catch(CustomException e){
			System.out.println("\n***Error while updating contracts:"+e.getMessage());
		}
	}

	@Override
	public void enterContracts(String vendorName, String endDate, String contractDetails) {
		 
		String query="INSERT into contracts values(SYS_GUID(), (SELECT to_date(to_char(sysdate,'yyyy/mm/dd hh24:mi:ss'),'yyyy/mm/dd hh24:mi:ss')FROM dual)," +
				" to_date(?,'dd-MON-yy'),(SELECT v.id FROM vendors v WHERE v.name = ?))" ; 
		String []bindVars= new String[2];
		bindVars[0]=endDate;
		bindVars[1]=vendorName;
		int queryResponse = 0;
		try {
			
			queryResponse = dbDAL.doDBOperation(query, bindVars);
			
		}
		catch(SQLException e){
			System.out.println("\n***Error while updating contracts:"+e.getMessage());
		}
		catch(CustomException e){
			System.out.println("\n***Error while updating contracts:"+e.getMessage());
		}
		
		if (queryResponse == dbDAL.DAL_SUCCESS)
		{
			System.out.println("Contract added successfully.");
		}
		else
		{
			System.out.println("Error while adding a contract between " + vendorName + " and Book A thousand store");
		}

	}
	
	public void updateContractEndDate(String vendorName, String contractEndDate)
	{
		String query = "Update contracts SET ENDDATE = to_date(?, 'dd-MON-yy') where vendorid = (SELECT v.id FROM vendors v WHERE v.name = ?)" ;
		String[] bindvars = new String[2];
		bindvars[0] = contractEndDate;
		bindvars[1] = vendorName;
		int queryResponse = 0;
		try {
			queryResponse = dbDAL.doDBOperation(query, bindvars);
		} catch (SQLException e) {
		} catch (CustomException e) {
		}

		if (queryResponse == dbDAL.DAL_SUCCESS) {
			System.out.println(ApplicationConstants.UPDATE_COMPLETED_SUCCESSFULLY);
		} else {
			System.out.println(ApplicationConstants.UPDATE_COMMAND_ERROR);
		}
	}

	@Override
	public int enterWarehouse(String ISBN,String sellingPrice,
			String qtyInStock, String discount) {
		// TODO Auto-generated method stub
		String query="INSERT INTO warehouse_contents values ((SELECT id from merchandise WHERE ISBN=?),?,?,?) ";
		String []bindVars= new String[4];
		bindVars[0]=ISBN;
		bindVars[1]=sellingPrice;
		bindVars[2]=qtyInStock;
		bindVars[3]=discount;
		String []desc={"S","F","I","I"};
		
		int errorcode=-1;
		try {
			
			errorcode=dbDAL.doDBOperation(query,bindVars,desc);
			
		}
		catch(SQLException e){
		
			System.out.println("\n***Error while Inserting into Warehouse:"+e.getMessage());
		}
		catch(CustomException e){
			System.out.println("\n***Error while Inserting into Warehouse:"+e.getMessage());
		}
		
		if (errorcode==0)
				System.out.println("\n Warehouse  contents added successfully");
		else
			System.out.println("\n Warehouse  contents addition un-successful");
		
		return errorcode;
	}

	@Override
	public int updateWarehouse(String merID, String sellingPrice,
			String qtyInStock, String discount) {
		// TODO Auto-generated method stub
		String query="UPDATE warehouse_contents SET selling_Price=?, qty_In_Stock=?,discount=? WHERE mer_id=? ";
		String []bindVars= new String[4];
		bindVars[0]=sellingPrice;
		bindVars[1]=qtyInStock;
		bindVars[2]=discount;
		bindVars[3]=merID;
		String []desc={"F","I","I","S"};
		int errorcode=-1;
		 
		try {
			
					errorcode=dbDAL.doDBOperation(query, bindVars,desc);
								
			
		}
		catch(SQLException e){
		
			System.out.println("\n***Error while updating into Warehouse:"+e.getMessage());
		}
		catch(CustomException e){
			System.out.println("\n***Error while updating into Warehouse:"+e.getMessage());
		}

		if (errorcode==0)
				System.out.println("\n warehouse contents added Successful");
		else
			System.out.println("\n warehouse contents addition un-Successful");
		
		return errorcode;
	}

	@Override
	public void updateVendor(String id, String name, String address,
			String phoneNumber) {
		// TODO Auto-generated method stub
		
		String query="UPDATE vendors SET name=?, address=?,phone_number=? WHERE id=? ";
		String []bindVars= new String[4];
		bindVars[0]=name;
		bindVars[1]=address;
		bindVars[2]=phoneNumber;
		bindVars[3]=id;
		String []desc={"S","S","I","S"};
		int errorcode=-1;
		Vector results = new Vector(); 
		try {
			
			errorcode=dbDAL.doDBOperation(query,bindVars,desc);
								
			
		}
		catch(SQLException e){
		
			System.out.println("\n***Error while updating vendor:"+e.getMessage());
		}
		catch(CustomException e){
			System.out.println("\n***Error while updating vendor:"+e.getMessage());
		}

		if (errorcode==0){
			System.out.println("\n Vendor updated successfully");
			
		}			
		else
			System.out.println("\n Vendor updation un-Successful");
	}

		
	

	@Override
	public void enterVendor( String name, String address,
			String phoneNumber, String contractEndDate) {
		// TODO Auto-generated method stub

		String query="INSERT INTO vendors VALUES (SYS_GUID(), ?, ? ,?) ";		
		String []bindVars= new String[3];
		bindVars[0]=name;
		bindVars[1]=address;
		bindVars[2]=phoneNumber;
		int errorcode=-1;
		 
		try {
			
			errorcode=dbDAL.doDBOperation(query,bindVars);
		
		}
		catch(SQLException e){
		
			System.out.println("\n***Error while entering vendor:"+e.getMessage());
		}
		catch(CustomException e){
			System.out.println("\n***Error while entering vendor:"+e.getMessage());
		}

		if (errorcode==0){
			System.out.println("\n Vendor added successfully");
			enterContracts(name, contractEndDate, name + " will supply all books requested by Book-A-Thousand store.");
		}			
		else
			System.out.println("\n Vendor addition un-Successful");
	}

	@Override
	public void orderBooks(String ISBN, String custName,String phone, String salespersonName, String quantity){
		
			// TODO Auto-generated method stub
		String query="INSERT INTO customer_order VALUES (SYS_GUID(), 'NOT SHIPPED', " +
				"(SELECT to_date(to_char(sysdate,'yyyy/mm/dd hh24:mi:ss'),'yyyy/mm/dd hh24:mi:ss') FROM dual)," +
				"(SELECT pid FROM people WHERE  name=? ),(SELECT pid FROM people WHERE name=? AND phone =?),?," +
				"(SELECT id from merchandise WHERE ISBN=?))";
		String []bindVars= new String[5];
		
		
		
		bindVars[0]=salespersonName;
		bindVars[1]=custName;
		bindVars[2]=phone;
		bindVars[3]=quantity;
		bindVars[4]=ISBN;
		String[] desc={"S","S","S","I","S"};
		
		String udpdateWarehouse="Update warehouse_contents SET qty_in_stock=qty_in_stock-? where mer_id=(select id FROM merchandise where ISBN=?)";
		
		String []desupdate={"I","S"};
		int errorcode=-1,errorcode1=-1;
		String []bindVarsUpdate={quantity,ISBN};
		
		try {
			
			
			errorcode=dbDAL.doDBOperation(query,bindVars,desc);
			errorcode1=dbDAL.doDBOperation(udpdateWarehouse,bindVarsUpdate,desupdate);
			
		}
		catch(SQLException e){
		
			System.out.println("\n***Error while updating warehouse contents**"+e.getMessage());
		}
		catch(CustomException e){
			System.out.println("\n***Error while updating warehouse contents**"+e.getMessage());
		}

		if (errorcode==0 && errorcode1==0)
			System.out.println("\n Order books Successful and Warehouse updated");
		else
			System.out.println("\n Order un-Successful");

			
	}

	@Override
	public void deleteMerchandise(String ISBN) {
		// TODO Auto-generated method stub
		
		String query="DELETE FROM merchandise WHERE ISBN=?";
		String []bindVars =new String [1];
		bindVars[0]=ISBN;
		int errorcode=-1; 
		try {
			
				errorcode=dbDAL.doDBOperation(query,bindVars);
	
		}
		catch(SQLException e){
			System.out.println("\n***Error while deleting merchandise:"+e.getMessage());
			
		}
		catch(CustomException e){
			System.out.println("\n***Error while deleting merchandise:"+e.getMessage());
		}

		if (errorcode==0)
				System.out.println("\n Deletion of merchandise Successful");
		else
			System.out.println("\n Delete of merchandise un-Successful");
	}

	@Override
	public void deleteStaff(String name,String phone) {
		// TODO Auto-generated method stub
		String query="DELETE FROM staff WHERE pid=(SELECT pid FROM people p WHERE p.Name= ? AND p.phone = ?)";
		String query1="DELETE FROM people WHERE name=? AND phone =?";
		String []bindVars =new String [2];
		bindVars[0]=name;
		bindVars[1]=phone;
		int errorcode=-1,errorcode1=-1;
		 
		try {
			
		
			errorcode=dbDAL.doDBOperation(query,  bindVars);
			errorcode1=dbDAL.doDBOperation(query1, bindVars);
			
		}
		catch(SQLException e){
		
			System.out.println("\n Delete from staff un-Successful"+e.getMessage());
		}
		catch(CustomException e){
			System.out.println("\n Delete from staff un-Successful"+e.getMessage());
		}

		if (errorcode==0 && errorcode1==0)
				System.out.println("\n Delete staff Successful");
		else
			System.out.println("\n Delete staff un-Successful");
	}

	@Override
	public void deleteContract(String vendorName) {
		// TODO Auto-generated method stub
		String query="DELETE FROM contracts WHERE vendorid=(select id from vendors where name=?)";
		String []bindVars =new String [1];
		bindVars[0]=vendorName;
		int errorcode=-1;
		 
		try {
			
				errorcode=dbDAL.doDBOperation(query,  bindVars);
								
			
		}
		catch(SQLException e){
		
			System.out.println("\n Delete from contracts un-Successful"+e.getMessage());
		}
		catch(CustomException e){
			System.out.println("\n Delete from contracts un-Successful"+e.getMessage());
		}

		if (errorcode==0)
			System.out.println("\n Contract deleted");
		else
			System.out.println("\n Deletion of contracts un-Successful");
	}

	@Override
	public void deleteVendor(String name) {
		// TODO Auto-generated method stub
		String query="DELETE FROM vendors WHERE name=?";		//TODO: Delete vendor from contracts as well 
		String []bindVars =new String [1];
		bindVars[0]=name;
		int errorcode=-1;
		
		try {
			
			
			errorcode=dbDAL.doDBOperation(query, bindVars);
								
			
		}
		catch(SQLException e){
		
			System.out.println("\n Delete from vendor un-Successful"+e.getMessage());
		}
		catch(CustomException e){
			System.out.println("\n Delete from vendor un-Successful"+e.getMessage());
		}
		

		if (errorcode==0){
			System.out.println("\n Delete from vendor Successful");
			deleteContract(name);
		}
		else
			System.out.println("\n Delete from vendor un-Successful");

	}

	@Override
	public void deleteCustomer(String name,String phone) {
		// TODO Auto-generated method stub

		String query1="DELETE FROM people WHERE name=? AND phone=?";
		String []bindVars =new String [2];
		bindVars[0]=name;
		bindVars[1]=phone;
		int errorcode=-1;
		 
		try {
			
			errorcode=dbDAL.doDBOperation(query1,bindVars);
			
		}
		catch(SQLException e){
		
			System.out.println("\n Delete from customer un-Successful"+e.getMessage());
		}
		catch(CustomException e){
			System.out.println("\n Delete from customer un-Successful"+e.getMessage());
		}

		if (errorcode==0)
				System.out.println("\n Delete Successful");
		else
			System.out.println("\n Delete un-Successful");

	}
	@Override
	public void customerPayment(String customerName,String phone, String amount) {
		// TODO Auto-generated method stub
		
		String query="INSERT INTO cust_payments VALUES (SYS_GUID(), (SELECT pid FROM people p WHERE p.name = ? AND p.phone = ?), ?, (SELECT to_date(to_char(sysdate,'yyyy/mm/dd hh24:mi:ss'),'yyyy/mm/dd hh24:mi:ss') FROM dual))";
		String []bindVars =new String [3];
		bindVars[0]=customerName;
		bindVars[1]=phone;
		bindVars[2]=amount;
		int errorcode=-1;
		String[] desc={"S","S","I"};
		Vector results = new Vector(); 
		try {
			
			errorcode=dbDAL.doDBOperation(query,bindVars,desc);
			
			
		}
		catch(SQLException e){
		
			System.out.println("\nSystem Error:"+e.getMessage());
		}
		catch(CustomException e){
			System.out.println("\nSystem Error:"+e.getMessage());
		}

		if (errorcode==0)
				System.out.println("\nCustomer payment Successful");
		else
			System.out.println("\nCstomer payment un-Successful");

		
	}

	@Override
	public void vendorPayment(String vendorName, String amount) {
		// TODO Auto-generated method stub
		String query="INSERT INTO vendor_payments VALUES (SYS_GUID(),(SELECT to_date(to_char(sysdate,'yyyy/mm/dd hh24:mi:ss'),'yyyy/mm/dd hh24:mi:ss') FROM dual),?, (SELECT id FROM vendors where name=?))";
		String []bindVars =new String [2];
		bindVars[0]=amount;
		bindVars[1]=vendorName;
		
		int errorcode=-1;
		String[] desc={"I","S"};
		
		try {
			
			errorcode=dbDAL.doDBOperation(query,bindVars,desc);
			
			
		}
		catch(SQLException e){
		
			System.out.println("\nSystem Error"+e.getMessage());
		}
		catch(CustomException e){
			System.out.println("\nSystem Error"+e.getMessage());
		}

		if (errorcode==0)
			System.out.println("\nVendor payment Successful");
		else
			System.out.println("\nVendor payment un-Successful");



	}
	/**
	 * @param args
	 */
		

}
