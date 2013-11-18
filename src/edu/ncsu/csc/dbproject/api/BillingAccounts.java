package edu.ncsu.csc.dbproject.api;

public interface BillingAccounts {
	/**
	 * Generate bill for the specified month(by default last month) for the
	 * specified customer
	 * 
	 * @param customerId
	 * @param phone
	 * @param billingDate
	 */
	public void getCustomerBill(String customerName,String phone, String billingDate);

	/**
	 * Generate bill for the specific month(by default last month) for the
	 * specified vendor
	 * 
	 * @param vendorId
	 * @param billingDate
	 */
	public void getVendorBill(String vendorName, String billingDate);

	/**
	 * Update billing record for a customer with payment details
	 * 
	 * @param custId
	 * @param amount
	 * @param date
	 */
//	public void customerPayment(String customerName, String amount, String date);

	/**
	 * Update billing record for a vendor with payment details
	 * 
	 * @param vendorId
	 * @param amount
	 * @param date
	 */
//	public void vendorPayment(String vendorName, String amount, String date);
}
