package test;

import org.ariba.main.Details;
import org.ariba.methods.ParseExcel;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;

public class Test {
	public static void main(String args[]) {
		boolean isExisting = false;
		String executionQuery = "";
		try{
			System.setProperty("ROW", "3");//Table start row
			System.setProperty("COLUMN", "1");//Table start column
			Fillo fillo = new Fillo();
			Connection conn = fillo.getConnection(Details.path);
//			Connection conn = fillo.getConnection("C:\\Users\\jan.dwain.f.domondon\\Documents\\Design Matrix - Template v2.0.XLSM");
			
				executionQuery = "Select * from `Event Content` where `Content` = 'Section' and `Content Name`='Attach signed copy of NDA'";
			
			Recordset rs = conn.executeQuery(executionQuery);
			while (rs.next()){
				isExisting = true;
			}
			rs.close();
			conn.close();
		}catch(FilloException e){
			isExisting = false;
		}
		
		System.out.println(isExisting);
		}
	}

