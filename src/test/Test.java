package test;

import org.ariba.methods.parseExcel;

public class Test {
	public static void main(String args[]) {
		
		
		parseExcel retrieve = new parseExcel();
		String [] task = retrieve.getTaskInExcel("Test Phase 1", "To Do Task 1").split("~", -1);
		String associatedDocument = task[16].trim();
		System.out.println("Document: " + associatedDocument);
		
		
		}
	}

