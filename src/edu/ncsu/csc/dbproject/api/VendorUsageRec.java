package edu.ncsu.csc.dbproject.api;

public interface VendorUsageRec {
	
	/**
	 * Shows all purchases made by a vendor
	 * @param vendorName
	 */
	public void getVendorPurchaseHistory(String vendorName);
	
	/**
	 * shows the purchases made from the specified vendor over a specified time
	 * period
	 * 
	 * @param vendorId
	 * @param beginDate
	 * @param endDate
	 */
	public void getVendorPurchases(String vendorName, String beginDate,
			String endDate);

	/**
	 * 
	 Order added to Vendor billing information. Returns query response
	 * 
	 * @param vendorId
	 * @param merchandiseId
	 * @param quantity
	 */
	public int addVendorPurchaseRecord(String shippingStatus, String vendorName,
			String bookName, String quantity);

	/**
	 * Vendor billing record updated. *INCOMPLETE*
	 * 
	 * @param id
	 */
	//public void updateVendorPurchaseRecord(String id, String shippingStatus, String vendorName,
		//	String merchandiseId, String quantity);
	
	/**
	 * Only shipping status update is allowed. Provide vendor id by listing all order made by a particular vendor
	 * @param vendorOrderID
	 * @param updatedShippingStatus
	 */
	public void updateVendorOrderShippingStatus(String vendorOrderID, String updatedShippingStatus);

	/**
	 * Deletes the specified vendor purchase order.
	 * 
	 * @param id
	 */
	public void cancelVendorPurchase(String vendorOrderId);
}
