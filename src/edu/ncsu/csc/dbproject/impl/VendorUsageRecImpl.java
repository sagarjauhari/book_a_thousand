package edu.ncsu.csc.dbproject.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Vector;

import edu.ncsu.csc.dbproject.api.InfoProcess;
import edu.ncsu.csc.dbproject.api.VendorUsageRec;
import edu.ncsu.csc.dbproject.io.IO;
import edu.ncsu.csc.dbproject.model.dbDAL;
import edu.ncsu.csc.dbproject.model.dbconnection;
import edu.ncsu.csc.dbproject.util.CustomException;

public class VendorUsageRecImpl implements VendorUsageRec {

	String query;
	String[] bindvars;
	String[] columnNames;
	int queryResponse;
	IO outputProcessor = new IO();

	InfoProcessImpl infoProcessImpl = new InfoProcessImpl();

	@SuppressWarnings("rawtypes")
	Vector result = new Vector();
	private static VendorUsageRecImpl vendorUsageRecord;

	@Override
	public void getVendorPurchaseHistory(String vendorName) {
		query = "SELECT  v.Name, mer.Name, vo.quantity, vo.order_date, vo.status, vo.cost FROM vendor_order vo, vendors v, merchandise mer where "
				+ "vo.vendor_id= (SELECT id FROM vendors WHERE vendors.name = ?) and "
				+ "v.id = vo.vendor_id and " 
				+ "vo.mer_id = mer.id";
		bindvars = new String[1];
		bindvars[0] = vendorName;

		columnNames = new String[6];
		columnNames[0] = "Vendor Name";
		columnNames[1] = "Merchandise Name";
		columnNames[2] = "Quantity";
		columnNames[3] = "Order Date";
		columnNames[4] = "Status";
		columnNames[5] = "Cost";

		try {
			queryResponse = dbDAL.getDBData(query, result, bindvars);
		} catch (SQLException ex) {
			handleError(ex);
		} catch (CustomException ex) {
			handleError(ex);
		}

		if (queryResponse == dbDAL.DAL_SUCCESS)
			outputProcessor.print("Purchase order from vendor: " + vendorName,
					result, columnNames);
		else {
			printConsole(ApplicationConstants.SELECT_COMMAND_ERROR);
		}

	}

	@Override
	public void getVendorPurchases(String vendorName, String beginDate,
			String endDate) {
		query = "SELECT v.Name, mer.Name, vo.quantity, vo.order_date, vo.status, vo.cost "
				+ "FROM vendor_order vo, vendors v, merchandise mer "
				+ "where "
				+ "vo.vendor_id= (SELECT id FROM vendors WHERE vendors.name = ?) and  "
				+ "vo.order_date >= to_date(?, 'dd-MON-yy hh24:mi:ss') AND vo.order_date <= to_date(?,'dd-MON-yy hh24:mi:ss') and "
				+ "vo.vendor_id = v.id and vo.mer_id = mer.id";
		bindvars = new String[3];
		bindvars[0] = vendorName;
		bindvars[1] = beginDate + " 00:00:00";
		bindvars[2] = endDate + " 23.59.59";

		columnNames = new String[6];
		columnNames[0] = "Vendor Name";
		columnNames[1] = "Merchandise Name";
		columnNames[2] = "Quantity";
		columnNames[3] = "Order Date";
		columnNames[4] = "Status";
		columnNames[5] = "Cost";

		result = new Vector();

		try {
			queryResponse = dbDAL.getDBData(query, result, bindvars);
		} catch (SQLException ex) {
			handleError(ex);
		} catch (CustomException ex) {
			handleError(ex);
		}

		if (queryResponse == dbDAL.DAL_SUCCESS)
			outputProcessor.print("Purchase order from vendor: " + vendorName,
					result, columnNames);
		else {
			printConsole(ApplicationConstants.SELECT_COMMAND_ERROR);
		}
	}

	public String getMerchandiseIDFromISBN(String ISBN) {
		String merchandiseId = "";
		query = "Select id from Merchandise mer where mer.ISBN = ?";
		bindvars = new String[1];
		bindvars[0] = ISBN;
		String[] desc1 = { "I" };
		try {
			queryResponse = dbDAL.getDBData(query, result, bindvars, desc1);
		} catch (SQLException e) {
		} catch (CustomException e) {
		}

		if (!result.isEmpty()) {
			merchandiseId = String.valueOf(result.elementAt(0));
		} else {
			System.out.println("No merchandise exists with ISBN " + ISBN);
		}

		return merchandiseId;
	}
	
	/**
	 * Returns merchandise price from merchandise table using merchandise's ISBN
	 * @param merchandiseISBN
	 * @return
	 */
	public double getMerchandisePrice(String merchandiseISBN)
	{
		double merchandiseCostPrice = 0;
		// Get merchandise cost from Merchandise table using Merchandise ISBN
		String query1 = "Select mer.Price from Merchandise mer where mer.ISBN = ?";
		bindvars = new String[1];
		bindvars[0] = merchandiseISBN;
		result.clear(); 
		try {
			queryResponse = dbDAL.getDBData(query1, result, bindvars);
		} catch (Exception e) {
			printConsole("SQL transaction exception" + e.getMessage());
		}

		if (result.elements().hasMoreElements()) {
			merchandiseCostPrice = Double.parseDouble(String.valueOf(result.elementAt(0)));
		}
		return merchandiseCostPrice;
	}

	
	public int addNewVendorPurchaseRecordInternal(String shippingStatus, String quantity, double merchandiseCostPrice, String  vendorName, String merchandiseId)
	{
		int vendorPurchaseOrderQueryResponse = 0;
		bindvars = new String[5];
		bindvars[0] = shippingStatus;
		bindvars[1] = quantity;
		bindvars[2] = String.valueOf(merchandiseCostPrice * (Integer.parseInt(quantity)));
		bindvars[3] = vendorName;
		bindvars[4] = merchandiseId;

		String[] desc = { "S", "I", "F", "S", "S" };
		
		query = "Insert into vendor_order (id, status, order_date, quantity, cost, vendor_id, mer_id)"
				+ " Values (SYS_GUID(), ?, (SELECT to_date(to_char(sysdate,'yyyy/mm/dd hh24:mi:ss'),'yyyy/mm/dd hh24:mi:ss')FROM dual), ?, ?,"
				+ "(SELECT id FROM vendors WHERE vendors.name = ?), ?)";

		try {
			// Set auto commit to false
			vendorPurchaseOrderQueryResponse = dbDAL.doDBOperation(query, bindvars, desc);
		} catch (Exception e) {
			printConsole("SQL transaction exception" + e.getMessage());
		}
		return vendorPurchaseOrderQueryResponse;
	}
	
	@Override
	public int addVendorPurchaseRecord(String shippingStatus,
			String vendorName, String ISBN, String quantity) {
		
		int vendorPurchaseOrderQueryResponse = -1;
		int updateWareHouseQueryResponse = -1;
		
		String merchandiseId = getMerchandiseIDFromISBN(ISBN);
		double merchandiseCostPrice = getMerchandisePrice(ISBN); 

		// Adds new record in Vendor Order table.
		vendorPurchaseOrderQueryResponse = addNewVendorPurchaseRecordInternal(shippingStatus, quantity, merchandiseCostPrice, vendorName, merchandiseId);
		
		// If vendor order successfully placed, update warehouse content for the details of merchandise ordered.
		if (vendorPurchaseOrderQueryResponse == dbDAL.DAL_SUCCESS) {
			printConsole("Vendor purchase order requested.");

			// Check if there exists same merchandise in warehouse content
			query = "Select qty_in_stock from Warehouse_contents where mer_id = ?";
			bindvars = new String[1];
			bindvars[0] = merchandiseId;
			result.clear();
			queryResponse = -1;
			try {
				queryResponse = dbDAL.getDBData(query, result, bindvars);
			}  catch (Exception e) {
				System.out.println("SQL transaction exception" + e.getMessage());
			}

			int existingMerchandiseCount = 0;
			if (!result.isEmpty()) {
				existingMerchandiseCount = Integer.parseInt(String
						.valueOf(result.elementAt(0)));
			}

			if (existingMerchandiseCount == 0) {
				// Add new merchandise in WareHouse content
				updateWareHouseQueryResponse = infoProcessImpl.enterWarehouse(ISBN,
						String.valueOf(merchandiseCostPrice), quantity, "0");
			} else {
				// Update merchandise quantity in stock in warehouse.
				int updatedQuantity = Integer.parseInt(quantity) + existingMerchandiseCount;
				updateWareHouseQueryResponse = infoProcessImpl.updateWarehouse(
						merchandiseId, String.valueOf(merchandiseCostPrice),
						String.valueOf(updatedQuantity), "0");
			}
			
			if (updateWareHouseQueryResponse == dbDAL.DAL_SUCCESS) {
				System.out.println("Order successfully created.");
			} else {
				System.err.println("Error while creating purchase order. Cancelling purchase order request.");
				queryResponse = -1;  // Error while executing command.
			}
			
		} else {
			printConsole(ApplicationConstants.INSERT_ERROR);
		}
		
		return queryResponse;
	}

	@Override
	public void updateVendorOrderShippingStatus(String vendorOrderID,
			String updatedShippingStatus) {

		query = "Update vendor_order SET status = ? where id = ?";
		bindvars = new String[2];
		bindvars[0] = updatedShippingStatus;
		bindvars[1] = vendorOrderID;
		try {
			queryResponse = dbDAL.doDBOperation(query, bindvars);
		} catch (SQLException e) {
		} catch (CustomException e) {
		}

		if (queryResponse == dbDAL.DAL_SUCCESS) {
			printConsole(ApplicationConstants.UPDATE_COMPLETED_SUCCESSFULLY);
		} else {
			printConsole(ApplicationConstants.UPDATE_COMMAND_ERROR);
		}
	}

	@Override
	public void cancelVendorPurchase(String vendorOrderId) {
		query = "Delete from vendor_order where id = ?";
		bindvars = new String[1];
		bindvars[0] = vendorOrderId;
		try {
			dbDAL.doDBOperation(query, bindvars);
		} catch (SQLException e) {
		} catch (CustomException e) {
		}

		if (queryResponse == dbDAL.DAL_SUCCESS) {
			printConsole(ApplicationConstants.DELETE_COMMAND_SUCCESSFULLY);
		} else {
			printConsole(ApplicationConstants.DELETE_COMMAND_ERROR);
		}
	}

	/**
	 * Returns list of all vendors available for purchase.
	 */
	public void getAllVendorNames() {
		query = "SELECT  v.Name, v.Address, v.phone_number from vendors v";

		String[] columnNames1 = { "Vendor Name", "Address", "Phone Number" };

		result.clear();
		try {
			queryResponse = dbDAL.getDBData(query, result);
		} catch (SQLException ex) {
			handleError(ex);
		} catch (CustomException ex) {
			handleError(ex);
		}

		if (queryResponse == dbDAL.DAL_SUCCESS)
			outputProcessor.print("List of available vendors: ", result,
					columnNames1);
		else {
			printConsole(ApplicationConstants.SELECT_COMMAND_ERROR);
		}

	}

	@SuppressWarnings("rawtypes")
	void print(Vector outputVector, String[] columnNames) {
		if (outputVector == null)
			System.out.println("No record exists");
		System.out.println(outputVector);
	}

	void printConsole(String queryResponseString) {
		System.out.println(queryResponseString);
	}

	void handleError(Exception exception) {
		System.out.println("SQL Error with error: " + exception.getMessage());
	}
	
	void printForTestingRollback(String errorString)
	{
			System.err.println(errorString);
	}
	

	public void testCommitTransaction()
	{
		// TEST COMMIT TRANSACTION		
		Connection dummyConnection =  dbconnection.get();
		
		printConsole("Testing Commit transaction. \n Auto commit set to false.");
		try {
			dummyConnection.setAutoCommit(false);
		} catch (SQLException e) {
			printForTestingRollback(e.getMessage());
		}
		
		String vendorUpdateQuery = "Insert into vendor_order (id, status, order_date, quantity, cost, vendor_id, mer_id)"
		+ " Values (SYS_GUID(), 'NOT SHIPPED' , (SELECT to_date(to_char(sysdate,'yyyy/mm/dd hh24:mi:ss'),'yyyy/mm/dd hh24:mi:ss')FROM dual), 2, 10,"
		+ "(SELECT id FROM vendors WHERE vendors.name = 'Ivy House'), (Select id FROM Merchandise where Name = 'Proof of Heaven'))";

		String warehouseUpdateQuery = "INSERT INTO warehouse_contents values ((SELECT id from merchandise WHERE Name = 'Proof of Heaven'), 10, 2, 0)";
		PreparedStatement preparedStatement = null;
		int vendorQueryResponse = -1;
		int warehouseQueryResponse = -1;
		
		try {
			printConsole("Adding vendor record.");
			preparedStatement = dummyConnection.prepareStatement(vendorUpdateQuery);
			vendorQueryResponse = preparedStatement.executeUpdate();
							
		} catch (Exception e) {
			printForTestingRollback(e.getMessage());
			
			try {
				printConsole("Transaction rollback.");
				dummyConnection.rollback();
			} catch (SQLException ex) {
				printForTestingRollback(ex.getMessage());
			}
		}
		
		try {
			printConsole("Adding warehouse update.");
			preparedStatement = dummyConnection.prepareStatement(warehouseUpdateQuery);
			warehouseQueryResponse = preparedStatement.executeUpdate();
							
		} catch (Exception e) {
			printForTestingRollback(e.getMessage());
			
			try {
				printConsole("Transaction rollback.");
				dummyConnection.rollback();
			} catch (SQLException ex) {
				printForTestingRollback(ex.getMessage());
			}
		}
		
		if(vendorQueryResponse >= 0 && warehouseQueryResponse >= 0)
		{
			try {
				printConsole("Transaction committed.");
				dummyConnection.commit();
			} catch (SQLException e) {
				printForTestingRollback(e.getMessage());
			}
		}
		
		try {
			dummyConnection.close();
		} catch (SQLException e) {
			printForTestingRollback(e.getMessage());
		}
	}
	
	public static void main(String[] args) 
	{
		VendorUsageRecImpl vuri = new VendorUsageRecImpl();
		vuri.testCommitTransaction();
	}
	

}
