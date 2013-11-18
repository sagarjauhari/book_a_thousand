package edu.ncsu.csc.dbproject.api;

public interface InfoProcess {
	/**
	 * Updated employee record in database
	 * 
	 * @param name
	 * @param jobTitle
	 * @param deptName
	 * @param salary
	 * @param address
	 * @param phone
	 */
	public void updateStaff( String name, String jobTitle,
			String deptName, String salary, String address, String phone);

	/**
	 * New employee record in database
	 * 
	 * @param name
	 * @param salary
	 * @param jobTitle
	 * @param deptName
	 * @param storeName
	 * @param dateOfBirth
	 * @param ssn
	 * @param gender
	 * @param address
	 * @param phone
	 */
	public void enterStaff(String name,String salary, String jobTitle,
			String deptName, String storeName, String dateOfBirth, String ssn,
			String gender, String address, String phone);

	/**
	 * Updated customer record in database
	 * 
	 * @param balance
	 * @param preferredCustomer
	 * @param name
	 * @param address
	 * @param phone

	 */
	public void updateCustomer(String balance,
			String preferredCustomer, String name, 
			String address, String phone);

	/**
	 * New customer record in database
	 * 
	 * @param balance
	 * @param preferredCustomer
	 * @param name
	 * @param ssn
	 * @param gender
	 * @param address
	 * @param phone
	 * @param dateofBirth
	 */
	public void enterCustomer(String balance,
			String preferredCustomer, String name, String ssn, String gender,
			String address, String phone, String dateofBirth);

	/**
	 * 
	 * @param bookName
	 */
	public void availableBookByName(String bookName);

	/**
	 * 
	 * @param author
	 */
	public void availableBookByAuthor(String author);

	/**
	 * 
	 * @param author
	 * @param bookName
	 */
	public void availableBooks(String author, String bookName);

	/**
	 * All available books in the warehouse
	 */
	public void allAvailableBooks();
	
	/**
	 * All available books in the Merchandise
	 */
	public void allAvailableMerchandise();

	
	/**
	 * Successful contract information update
	 * 
	 * @param vendorName
	 * @param startDate
	 * @param endDate
	 * @param contractDetails
	 */
	public void updateContracts(String vendorName, String startDate,
			String endDate, String contractDetails);

	/**
	 * Successful contract information insertion/update
	 * 
	 * @param vendorName
	 * @param endDate
	 * @param contractDetails
	 */
	public void enterContracts(String vendorName, 
			String endDate, String contractDetails);

	/**
	 * New warehouse database
	 * 
	 * @param merID
	 * @param sellingPrice
	 * @param qtyInStock
	 * @param discount
	 */
	public int enterWarehouse(String ISBN,String sellingPrice,
			String qtyInStock, String discount);

	/**
	 * updated warehouse database
	 * 
	 * @param merID
	 * @param sellingPrice
	 * @param qtyInStock
	 * @param discount
	 */
	public int updateWarehouse(String merID, String sellingPrice,
			String qtyInStock, String discount);

	/**
	 * Updated vendor record in database
	 * 
	 * @param id
	 * @param name
	 * @param address
	 * @param phoneNumber
	 */
	public void updateVendor(String id, String name, String address,
			String phoneNumber);

	/**
	 * New vendor record in database
	 * 
	 * @param name
	 * @param address
	 * @param phoneNumber
	 * @param endDate
	 */
	public void enterVendor( String name, String address,
			String phoneNumber, String endDate);

	/**
	 * Order for book placed, billing record added to customer, warehouse
	 * database updated.
	 * 
	 * @param ISBN
	 * @param custName
	 * @param phone
	 * @param salespersonName
	 * @param quantity
	 */
	public void orderBooks(String ISBN, String custName,String phone, String salespersonName, String quantity);
	
	/**
	 * Update billing record for a customer with payment details
	 * 
	 * @param custName
	 * @param phone
	 * @param amount
	 */
	public void customerPayment(String customerName,String phone, String amount);

	/**
	 * Update billing record for a vendor with payment details
	 * 
	 * @param vendorId
	 * @param amount
	 * @param date
	 */
	public void vendorPayment(String vendorName, String amount);

	/*---------- Delete Entries ----------------*/
	
	/**
	 * Delete Merchandise
	 * 
	 * @param ISBN
	 */
	
	public void deleteMerchandise(String ISBN);
	
	/**
	 * Delete Staff
	 * 
	 * @param name
	 * @param phone
	 */
	
	public void deleteStaff(String name,String phone);

	/**
	 * Delete Contract information
	 * 
	 * @param vendorName
	 */
	
	
	public void deleteContract(String vendorName);

	/**
	 * Delete VEndor information
	 * 
	 * @param vendorName
	 */
	
	public void deleteVendor(String name);

	/**
	 * Delete Customer information
	 * 
	 * @param Name
	 * @param phone
	 */
	
	public void deleteCustomer(String name,String phone);

}
