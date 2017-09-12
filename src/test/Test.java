package test;

import org.ariba.methods.ParseExcel;

public class Test {
	public static void main(String args[]) {
		
		
		ParseExcel retrieve = new ParseExcel();
		String [] task = retrieve.getTaskInExcel("Test Phase 1", "To Do Task 1").split("~", -1);
		String associatedDocument = task[16].trim();
		System.out.println("Document: " + associatedDocument);
		
		
		}
	}

