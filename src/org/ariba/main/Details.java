package org.ariba.main;

import java.util.ArrayList;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

public class Details {
	
	public static Display display;
	public static Text logging;
	public static String server;
	public static String user;
	public static String pass;

	public static ArrayList<ArrayList<String>> taskTab = new ArrayList<ArrayList<String>>();
	public static ArrayList<ArrayList<String>> teamTab = new ArrayList<ArrayList<String>>();
	
	public static String path;
	public static String template = "";
	public static String eventType = "";
	
}
