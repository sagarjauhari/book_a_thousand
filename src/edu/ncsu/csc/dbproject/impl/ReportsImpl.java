package edu.ncsu.csc.dbproject.impl;

import java.sql.SQLException;
import java.util.Vector;

import edu.ncsu.csc.dbproject.api.Reports;
import edu.ncsu.csc.dbproject.util.CustomException;
import edu.ncsu.csc.dbproject.model.dbDAL;
import edu.ncsu.csc.dbproject.io.IO;

public class ReportsImpl implements Reports {
	dbDAL dal = new dbDAL();
	IO io = new IO();

	@Override
	public void getCustomerHistory(String custName,String phone, String startDate,
			String endDate) {
		/* Get orders */
		int total = getCustomerOrders(custName,phone, startDate, endDate);
		System.out.println("Total cost of orders: USD "+total);
		
		/* Get payments */
		int totalP = getCustomerPayments(custName, phone, startDate, endDate);
		System.out.println("Total payment: USD "+totalP);
		
		/* Get balance */
		int balance = getCustomerBalance(custName, phone);
		Vector v = new Vector<Integer>();
		String[] cols = {
				"Orders (USD)",
				"Payments (USD)",
				"Last balance (USD)",
				"Amount payable (USD)"};
		v.add(total);
		v.add(totalP);
		v.add(balance);
		v.add(total - totalP - balance);
		
		io.print("AMOUNT PAYABLE THIS MONTH", v, cols);
	}
	
	@Override
	public int getCustomerOrders(String custName, String phone, String startDate,
			String endDate) {
		String query = "SELECT co.order_date,co.status,co.quantity,m.name,m.price," +
				"m.author,m.ISBN " +
				"FROM customer_order co, merchandise m " +
				"WHERE co.customer_pid=(select pid from people where name=? AND phone=?) " +
				"and co.mer_id=m.id AND co.order_date >= to_date(?,'dd-MON-yy hh24:mi:ss') " +
				"AND co.order_date <= to_date(?,'dd-MON-yy hh24:mi:ss')";
		String[] bindvars = { custName,phone,startDate+" 00:00:00", endDate+" 23:59:59" };
		Vector results = new Vector();
		try {
			dal.getDBData(query, results, bindvars);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (CustomException e) {
			e.printStackTrace();
		}
		String[] cols = {"order_date", "status", "quantity","book name","cost(USD)","author"
				,"book ISBN"};
		io.print("ORDERS OF "+custName,results, cols);
		
		/*********** Calculate total order amount ***************/
		assert(results.size()%cols.length==0);
		int total=0;
		int rows = results.size()/cols.length;
		for(int i = 0; i < rows; i++){
			int qty = Integer.parseInt(results.get(i*cols.length+2).toString());
			int cost= Integer.parseInt(results.get(i*cols.length+4).toString());
			total+=(qty*cost);
		}
		return total;
	}
	
	@Override
	public int getCustomerPayments(String custName, String phone, String startDate,
			String endDate) {
		
		String query = "select cp.payment_amount, cp.payment_date from cust_payments cp " +
				"where cp.cust_id=" +
				"(select pid from people where name=? and phone=?) " +
				"and cp.payment_date>=to_date(?,'dd-MON-yy hh24:mi:ss') " +
				"and cp.payment_date<=to_date(?,'dd-MON-yy hh24:mi:ss')";
		String[] bindvars = { custName,phone, startDate+" 00:00:00", endDate+" 23:59:59" };
		Vector results = new Vector();
		try {
			dal.getDBData(query, results, bindvars);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (CustomException e) {
			e.printStackTrace();
		}
		String[] cols = {"amount","date"};
		io.print("PAYMENTS MADE BY "+custName,results, cols);

		/*********** Calculate total payment amount ***************/
		assert(results.size()%cols.length==0);
		int total=0;
		int rows = results.size()/cols.length;
		for(int i = 0; i < rows; i++){
			int cost= Integer.parseInt(results.get(i*cols.length+0).toString());
			total+=cost;
		}
		return total;
	}
	
	@Override
	public void getVendorPayments(String custName, String startDate,
			String endDate) {
		
		String query = "select vp.amount, vp.payment_date from vendor_payments vp " +
				"where vp.vendor_id=" +
				"(select id from vendors where name=?) " +
				"and vp.payment_date>=to_date(?,'dd-MON-yy') " +
				"and vp.payment_date<=to_date(?,'dd-MON-yy hh24:mi:ss')";
		String[] bindvars = { custName, startDate, endDate+" 23:59:59" };
		Vector results = new Vector();
		try {
			dal.getDBData(query, results, bindvars);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (CustomException e) {
			e.printStackTrace();
		}
		String[] cols = {"amount","date"};
		io.print("VENDOR PAYMENTS OF "+custName,results, cols);

	}

	public void getVendorOrders(String vendorName, String startDate, String endDate){
		String query = "SELECT vo.order_date,vo.status,vo.quantity,m.name,m.author,m.ISBN " +
				"FROM vendor_order vo, merchandise m " +
				"WHERE vo.vendor_id=(select id from vendors where name=?) " +
				"and vo.mer_id=m.id AND vo.order_date >= to_date(?,'dd-MON-yy') " +
				"AND vo.order_date <= to_date(?,'dd-MON-yy hh24:mi:ss')";
		String[] bindvars = { vendorName, startDate, endDate+" 23:59:59" };
		Vector results = new Vector();
		try {
			dal.getDBData(query, results, bindvars);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (CustomException e) {
			e.printStackTrace();
		}
		String[] cols = {"order_date", "status", "quantity","book name","book author"
				,"book ISBN"};
		io.print("VENDOR ORDERS OF "+vendorName,results, cols);
		
	}
	
	public void getVendorHistory(String vendorName, String startDate, String endDate){
		getVendorOrders(vendorName, startDate, endDate);
		getVendorPayments(vendorName, startDate, endDate);
	}
	
	@Override
	public void getSalespersonReport(String salesPersonName, String startDate,
			String endDate) {
		String query = "select cp.name,co.order_date,m.name,co.quantity,m.price " +
				"from customer_order co,people cp, people sp, merchandise m " +
				"where cp.pid=co.customer_pid AND co.mer_id=m.id AND " +
				"sp.pid=co.sales_person_pid AND sp.pid=" +
				"(SELECT pid from people where name=?) " +
				"AND co.order_date >= to_date(?,'dd-MON-yy') " +
				"AND co.order_date <= to_date(?,'dd-MON-yy hh24:mi:ss')";
		String[] bindvars = { salesPersonName, startDate, endDate+" 23:59:59" };
		Vector results = new Vector();
		try {
			dal.getDBData(query, results, bindvars);
		} catch (SQLException e) {

		} catch (CustomException e) {

		}
		String[] cols = {"Customer","Order Date","Book","Quantity","Price (USD)"};
		io.print("SALESPERSON REPORT for " + salesPersonName + " from "
				+ startDate + " to " + endDate, results, cols);
		
		/*********** Calculate total order amount ***************/
		assert(results.size()%cols.length==0);
		int total=0;
		int rows = results.size()/cols.length;
		for(int i = 0; i < rows; i++){
			int qty = Integer.parseInt(results.get(i*cols.length+3).toString());
			int cost= Integer.parseInt(results.get(i*cols.length+4).toString());
			total+=(qty*cost);
		}
		System.out.println("TOTAL SALES MADE BY "+salesPersonName+": $"+total);
		System.out.println();
	}

	@Override
	public void getVendorReport(String vendorId) {
		String query = "select name,address,phone_number, con.startdate, con.enddate from vendors, contracts con where name=? and vendors.id = con.vendorid";
		String[] bindvars = { vendorId };
		Vector results = new Vector();
		try {
			dal.getDBData(query, results, bindvars);
		} catch (SQLException e) {

		} catch (CustomException e) {

		}
		String[] cols = {"name","address","phone", "Start Date", "End Date"};
		io.print("VENDOR DETAILS",results,cols);
	}

	@Override
	public void getAllVendorsReport() {
		String query = "SELECT name,address,phone_number, con.startdate, con.enddate  FROM vendors, contracts con where vendors.id = con.vendorid";
		Vector results = new Vector();
		try {
			dal.getDBData(query, results);
		} catch (SQLException e) {

		} catch (CustomException e) {
		}
		String[] cols = {"name","address","phone", "Start date", "End date"};
		io.print("VENDOR LIST",results,cols);
	}

	@Override
	public void getStaffByRole(String dept_name, String store_name) {
		String query = "select people.name, people.ssn, people.gender, "
				+ "people.address, people.phone, people.date_of_birth, "
				+ "staff.job_title, staff.dept_name, store.short_name, "
				+ "store.address, staff.salary from people,staff,store where people.pid=staff.pid "
				+ "and staff.dept_name=? and staff.store_id=store.id and " +
				"staff.store_id=(select id from store where short_name=?)";
		String[] bindvars = { dept_name ,store_name};
		Vector results = new Vector();
		try {
			dal.getDBData(query, results, bindvars);
		} catch (SQLException e) {

		} catch (CustomException e) {

		}
		String[] cols = {"name","ssn","gender","address","phone","dob","job_title",
				"dept_name","short_name","store_address","salary ($)"};
		io.print("STAFF DETAILS",results, cols);
	}

	@Override
	public void getAllStaffGroupedByRole(String store_name) {
		String query = "select people.name, people.gender, "
				+ "people.phone, people.date_of_birth, staff.job_title, "
				+ "staff.dept_name, store.short_name, staff.salary from"
				+ " people,staff,store where people.pid=staff.pid "
				+ "and staff.store_id=store.id and staff.store_id=" +
				"(select id from store where short_name=?) order by staff.job_title";
		Vector results = new Vector();
		String[] bindvars={store_name};
		try {
			dal.getDBData(query, results, bindvars);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} catch (CustomException e) {
			System.out.println(e.getMessage());
		}
		String cols[] = {"name","gender","phone","dob","job_title","dept_name",
				"store_name","salary ($)"};
		io.print("STAFF DETAILS",results,cols);
	}
	
	@Override
	public int getCustomerBalance(String custName, String phone){
		int balance = 0;
		String query = "SELECT c.balance from customer c, people p WHERE " +
				"c.pid = p.pid AND " +
				"p.pid=(select pid from people where name=? and phone=?)";
		String[] bindvars = { custName, phone };
		Vector results = new Vector();
		try {
			dal.getDBData(query, results, bindvars);
		} catch (SQLException e) {

		} catch (CustomException e) {

		}
		balance = Integer.parseInt(results.get(0).toString());
		
		return balance;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ReportsImpl reports = new ReportsImpl();
//		reports.getAllVendorsReport();
//		reports.getVendorReport("Random House");
		reports.getStaffByRole("Management", "Books-A-Thousand-1");
		reports.getAllStaffGroupedByRole("Books-A-Thousand-1");
//		reports.getCustomerHistory("Abhay","2", "01-Jan-11", "01-dec-13");
//		reports.getCustomerPayments("Abhay","2", "01-Jan-11", "01-dec-13");
//		reports.getVendorOrders("Ivy House", "01-Jan-11", "01-dec-12");
//		reports.getVendorPayments("Ivy House", "01-Jan-11", "01-dec-12");
//		reports.getSalespersonReport("George Carlin","01-Jan-11", "01-dec-13");

	}



}
