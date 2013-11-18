package edu.ncsu.csc.dbproject.api;

public interface Reports {
	/**
	 * 
	 Generate purchase history over a period for a given customer. Calls the
	 * getCustomerHistory and getCustomerPayments successively
	 * 
	 * @param customerId
	 * @param phone
	 * @param startDate
	 *            in "01-JAN-12" format
	 * @param endDate
	 *            in "01-JAN-12" format
	 */
	public void getCustomerHistory(String customerId, String phone,
			String startDate, String endDate);

	/**
	 * Returns total cost of the payments
	 * 
	 * @param custName
	 * @param startDate
	 * @param endDate
	 */
	public int getCustomerPayments(String custName, String phone,
			String startDate, String endDate);

	/**
	 * 
	 * @param custName
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public int getCustomerOrders(String custName, String phone,
			String startDate, String endDate);

	/**
	 * 
	 Generate report of all the customer assisted by a particular salesperson
	 * during a time period
	 * 
	 * @param salesPersonId
	 * @param startDate
	 * @param endDate
	 */
	public void getSalespersonReport(String salesPersonId, String startDate,
			String endDate);

	/**
	 * Generate report of all the vendors details, books-a-thousand has contract
	 * with.
	 * 
	 * @param vendorId
	 */
	public void getVendorReport(String vendorId);

	/**
	 * Generate report of all the vendors;
	 */
	public void getAllVendorsReport();

	/**
	 * Returns details about all the staff with the specified role
	 * 
	 * @param role
	 */
	public void getStaffByRole(String role, String storeName);

	/**
	 * All staff grouped by the role
	 */
	public void getAllStaffGroupedByRole(String storeName);

	/**
	 * 
	 * @param vendorName
	 * @param startDate
	 * @param endDate
	 */
	void getVendorPayments(String vendorName, String startDate, String endDate);

	/**
	 * Get list of all the vendor orders
	 * 
	 * @param vendorName
	 * @param startDate
	 * @param endDate
	 */
	void getVendorOrders(String vendorName, String startDate, String endDate);
	
	/**
	 * 
	 * @param custName
	 * @param phone
	 * @return
	 */
	int getCustomerBalance(String custName, String phone);
}
