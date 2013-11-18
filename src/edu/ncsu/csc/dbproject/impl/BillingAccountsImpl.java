package edu.ncsu.csc.dbproject.impl;

import edu.ncsu.csc.dbproject.api.BillingAccounts;
import edu.ncsu.csc.dbproject.impl.ReportsImpl;

public class BillingAccountsImpl implements BillingAccounts {
	ReportsImpl rep = new ReportsImpl();

	@Override
	public void getCustomerBill(String customerName,String phone, String billingDate) {
		String[] sd = billingDate.split("-");
		String startDate = billingDate;
		int year = Integer.parseInt(sd[2]);
		if(sd[1].equalsIgnoreCase("DEC")){
			sd[2]=""+(year+1);
		}
		String endDate = sd[0]+"-"+nextMonth(sd[1])+"-"+sd[2];
		System.out.println("Generating bill for "+customerName+".\n" +
				"Billing cycle: "+startDate+" 00:00:00 to "+endDate+" 23:59:59");
		rep.getCustomerHistory(customerName,phone, startDate, endDate);
	}

	@Override
	public void getVendorBill(String vendorName, String billingDate) {
		String[] sd = billingDate.split("-");
		String startDate = billingDate;
		int year = Integer.parseInt(sd[2]);
		if(sd[1].equalsIgnoreCase("DEC")){
			sd[2]=""+(year+1);
		}
		String endDate = sd[0]+"-"+nextMonth(sd[1])+"-"+sd[2];
		System.out.println("Generating bill for "+vendorName+".\n" +
				"Billing cycle: "+startDate+" 00:00:00 to "+endDate+" 23:59:59");
		rep.getVendorHistory(vendorName, startDate, endDate);

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BillingAccounts ba = new BillingAccountsImpl();
		ba.getCustomerBill("Abhay", "2","02-Dec-12");

	}
	
	String nextMonth(String s){
		if(s.equalsIgnoreCase("JAN")) return "FEB";
		else if (s.equalsIgnoreCase("FEB")) return "MAR";
		else if (s.equalsIgnoreCase("MAR")) return "APR";
		else if (s.equalsIgnoreCase("APR")) return "MAY";
		else if (s.equalsIgnoreCase("MAY")) return "JUN";
		else if (s.equalsIgnoreCase("JUN")) return "JUL";
		else if (s.equalsIgnoreCase("JUL")) return "AUG";
		else if (s.equalsIgnoreCase("AUG")) return "SEP";
		else if (s.equalsIgnoreCase("SEP")) return "OCT";
		else if (s.equalsIgnoreCase("OCT")) return "NOV";
		else if (s.equalsIgnoreCase("NOV")) return "DEC";
		else if (s.equalsIgnoreCase("DEC")) return "JAN";
		else return "JAN";
	}

}
