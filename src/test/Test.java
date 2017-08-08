package test;

import java.util.List;

import org.ariba.main.Details;
import org.ariba.methods.parseExcel;

public class Test {
	public static void main(String args[]) {
		
		
//		parseExcel retrieve = new parseExcel();
//		
//		List<String> test = retrieve.getDocumentsTab();
//		for (String t : test){
//			System.out.println(t);
//		}
		
		/*Details.eventType = "RFI";
		parseExcel retrieve = new parseExcel();
		
		String [] showParticipantResponsesToOtherParticipants = retrieve.getEventRules("Show participant responses to other participants").split(";",-1);
		System.out.println(showParticipantResponsesToOtherParticipants[0]);
		System.out.println(showParticipantResponsesToOtherParticipants[1]);*/
		
		String x = "HVE Contract Workspace Template – Resour3";
		System.out.println(x.length());
		
		if (x.length() > 40){
			x = x.substring(0, 40);
			System.out.println("truncate");
		}
		
		System.out.println(x);
		
		/*List <String> tasks = retrieve.getTasksTab();
		
		
		
		List <String> documents = retrieve.getDocumentsTab();
		
		
		for(String d : documents){
//			String [] document = d.split("~", -1);
//			String folderName = document[0].trim();
//			String documentName = document[1].trim();
//			String owner = document[2].trim();
//			String conditions = document[3].trim();
//			String documentPath = document[4].trim();
			
			
			
			
			
			System.out.println(d);
			
		}
		
		System.exit(0);
		
		
		for(String t : tasks){
			String [] task = t.split("~", -1);
			String type = task[9].trim();
			String phase = task[0].trim();
			String subPhase1 = task[1].trim();
			String subPhase2 = task[2].trim();
			
			//Tasks
			String title = task[6].trim();
			String description =  task[7].trim();
			String owner = task[11].trim();
			String observers = task[15].trim();
			String isMilestone = task[10].trim();
			String required = task[8].trim();
			String predecessors = task[23].trim();
			
			//Notification
			String recipients = task[17].trim();
			String notificationDays = task[18].trim();
			String notificationFrequency = task[19].trim();
			String autoStart = task[20].trim();
			String manualCompletion = task[21].trim();
			
			String associatedDocument = task[22].trim();
			
			
			System.out.println(associatedDocument);*/
			
			
			
			
			/*
			if (type.isEmpty()){
				
				
				
				
				
				
				if (!phase.isEmpty() && subPhase1.isEmpty()){
					
					//Create Phase
					System.out.println("Create Phase:" + phase);

				}else if(!phase.isEmpty() && !subPhase1.isEmpty() && subPhase2.isEmpty()){
					
					//Create Sub Phase 1
					System.out.println("Create Phase:" + subPhase1 + " in " + phase);
					
				}else if(!phase.isEmpty() && !subPhase1.isEmpty() && !subPhase2.isEmpty()){
					
					//Create Sub Phase 2					
					System.out.println("Create Phase:" + subPhase2 + " in " + subPhase1);
				}
			}else{
				
				if (phase.isEmpty()){
					
					//Create Task outside Phase
					System.out.println("Create Task outside Phase: " + taskName);
					
				}else if (!phase.isEmpty() && subPhase1.isEmpty()){
					
					//Create Task in Phase
					System.out.println("Create Task in Phase: " + taskName);
					
				}else if(!phase.isEmpty() && !subPhase1.isEmpty() && subPhase2.isEmpty()){
					
					//Create Task in Sub Phase 1
					System.out.println("Create Task in Sub Phase 1: " + taskName);
					
				}else if(!phase.isEmpty() && !subPhase1.isEmpty() && !subPhase2.isEmpty()){
					
					//Create Task in Sub Phase 2
					System.out.println("Create Task in Sub Phase 2: " + taskName);
					
				}
				
			}
			*/
		}
	}

