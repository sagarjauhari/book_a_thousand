package edu.ncsu.csc.dbproject.io;

import java.util.Vector;

public class IO {

	public void print(String title,Vector v, String[] cols){
		int width = 22;
		int num = cols.length;
		
		System.out.println("\n"+title);
		
		for(int i = 0; i < num; i++){
			for(int j = 0; j < width; j++){
				System.out.print("-");
			}
		}
		System.out.println();

		for(int i = 0; i < num; i++){
			if(cols[i].length()>width){
				cols[i] = cols[i].substring(0, width);
			}
			System.out.printf("%-"+width+"s", cols[i]);
		}
		System.out.println();
		
		for(int i = 0; i < num; i++){
			for(int j = 0; j < width; j++){
				System.out.print("-");
			}
		}
		for(int i = 0; i<v.size(); i++){
			if(i%num==0){
				System.out.println();
			}
			String s = v.get(i).toString();
			if(s.length()>width){
				s = s.substring(0, width-3);
				s+=".. ";
			}
			System.out.printf("%-"+width+"s", s);
		}
		System.out.println();
		for(int i = 0; i < num; i++){
			for(int j = 0; j < width; j++){
				System.out.print("-");
			}
		}
		System.out.println();
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String[] cols = {"order_date", "status", "quantity","book name","book author"
				,"book ISBN"};
		IO io = new IO();
		//io.print(new Vector(), cols);
		

	}

}
