package fr.kavi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;


/**
 * 	Information about KLog.class
 * 	Created by Kalvin VILLA
 * 	Created 07/07/2021
 * 
 * 	Description :
 *	This class is use for create log file for application
 *
 *	Last edit : 12/07/2021
 */
class KLog {


	
	private String name;
	
	private File file;
	private BufferedWriter writer;
	private ArrayList<String> stockedLog = new ArrayList<String>();
	private String path;
	
	public KLog(String name) {
		this.name = name;
		path = "/var/log/" + SMPI.getName().toLowerCase();
		if(create() == 0) {
			send(LogSeverity.ERROR, LogType.LOG, "Shutdown needed to access or create log file", true);
			System.exit(-1);
		}
		
		writeStock();
		
	}
	
	// Create log file 
	public int create() {
		File pathFile = new File(path);
		if(pathFile.exists() == false) {
			send(LogSeverity.INFO, LogType.LOG, "Create path : " + path, true);
			pathFile.mkdirs();
		}
		
		file = new File(path + name.toLowerCase() + ".log");
		
		if(file.exists() == false) {
			try {
				file.createNewFile();
				send(LogSeverity.INFO, LogType.LOG, "Create log file", true);
			} catch (IOException e) {
				send(LogSeverity.ERROR, LogType.LOG, "Create file : " + file.toString(), true);
				return 0;
			}
		}
		
		if(file.canWrite() == false) return 0;
		
		return 1;
	}
	
	public int writeStock() {
		for(String s : stockedLog) {
			write(s);
		}
		stockedLog.clear();
		return 0;
	}
	
	public int write(String msg) {
	    try {
			writer = new BufferedWriter(new FileWriter(file, true));
		    writer.write(msg);
			writer.newLine();
		    writer.close();
		} catch (IOException e) {
       	 	send(LogSeverity.ERROR, LogType.LOG, "Writing log file error : " + e.toString(), false);
		}
		return 1;
	}
	
	public String read() {
		try {  
			FileReader fr = new FileReader(file);   //reads the file  
			BufferedReader br = new BufferedReader(fr);  //creates a buffering character input stream  
			StringBuffer sb = new StringBuffer();    //constructs a string buffer with no characters  
			String line;  
			while((line=br.readLine())!=null)  {  
				sb.append(line + "\n");      //appends line to string buffer  
			}  
			fr.close();    //closes the stream and release the resources  
			send(LogSeverity.INFO, LogType.LOG, "Getting log file", false);
			return sb.toString();
		} catch(IOException e) {  
       	 	send(LogSeverity.ERROR, LogType.LOG, "Reading log file error : " + e.toString(), false);
		}  
		return "";
	}
	
	public void send(LogSeverity severity, LogType type, String message, boolean stock) {
		LocalDateTime date = LocalDateTime.now();
		String log = "[" + date + "] [" + severity + "] [" + type + "] : " + message;
		
		switch(severity) {
			case INFO:
				System.out.println(ConsoleColors.ANSI_WHITE +
						"[SMPI] Info - " + message
						+ ConsoleColors.ANSI_RESET);	
				break;
			case ERROR: 
				System.out.println(ConsoleColors.ANSI_RED +
						"[SMPI] Error - " + message
						+ ConsoleColors.ANSI_RESET);
				break;
				
			case WARNING: 
				System.out.println(ConsoleColors.ANSI_YELLOW +
						"[SMPI] Warning - " + message
						+ ConsoleColors.ANSI_RESET);
				break;
			default:
				break;
		
		};
		
		if(stock == true) {
			stockedLog.add(log);
		} else {
			write(log);
		}
		
		return;
	}
	
	public String toString() {
	    if (file.exists()) {
	        return "\nFile name: " + file.getName() +
	        "\nAbsolute path: " + file.getAbsolutePath() +
	        "\nWriteable: " + file.canWrite() +
	        "\nReadable " + file.canRead() +
	        "\nFile size in bytes " + file.length();
	      } else {
	        return "The file does not exist.";
	      }
	}
	
	public String getName() {
		return name;
	}

	public enum LogSeverity {
		ERROR, INFO, WARNING;
	}
	
	public enum LogType {
		MAIL, LOG, APPLICATION, CONFIG;
	}
	
	
}
