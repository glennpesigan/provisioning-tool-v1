package test;

import org.ariba.methods.parseExcel;

public class Test {
	public static void main(String args[]) {
		
		
		parseExcel retrieve = new parseExcel();
		System.out.println(retrieve.getDocumentInExcel("Sourcing Documents","Sourcing Plan"));
		String [] doc = retrieve.getDocumentInExcel("Sourcing Documents","Sourcing Plan").split("~",-1);
		String title = doc[2].trim();
		System.out.println(title);
		String description = doc[3].trim();
		String owner = doc[5].trim();
		String editors = doc[6].trim();
		String accessControl = doc[7].trim();
		String isPublishRequired = doc[8].trim();
		String conditions = doc[9].trim();
		System.out.println(doc[4].trim());
		}
	}

