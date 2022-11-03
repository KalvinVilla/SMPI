package fr.kavi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import fr.kavi.KLog.LogSeverity;
import fr.kavi.KLog.LogType;

/**
 * 	Information about KConfig.class
 * 	Created by Kalvin VILLA
 * 	Created 07/07/2021
 * 
 * 	Description :
 *	This class is use for create configuration file for application
 *
 *	Last edit : 09/07/2021
 */
class KConfig {


	
	private String name;
	
	private File file;
	private String path;
	private BufferedWriter writer;
	
	public KConfig(String name) {
		path = "/etc/" + SMPI.getName().toLowerCase() + "/";
		this.name = name;
		if(create() == 0) {
			SMPI.getLog().send(LogSeverity.ERROR, LogType.CONFIG, "Shutdown needed to access or create config file", false);
			System.exit(-1);
		}
		
	}
	
	// Create log file 
	public int create() {
		File pathFile = new File(path);
		if(pathFile.exists() == false) {
			SMPI.getLog().send(LogSeverity.INFO, LogType.CONFIG, "Create path : " + path, false);
			pathFile.mkdirs();
		}
		
		file = new File(path + name.toLowerCase() + ".conf");
		
		if(file.exists() == false) {
			try {
				file.createNewFile();
				SMPI.getLog().send(LogSeverity.INFO, LogType.CONFIG, "Create config file", false);
			} catch (IOException e) {
				SMPI.getLog().send(LogSeverity.ERROR, LogType.CONFIG, "Create config : " + file.toString(), false);
				return 0;
			}
		}
		
		if(file.canWrite() == false) return 0;
		
		return 1;
	}
	
	public int write(String msg) {
	    try {
			writer = new BufferedWriter(new FileWriter(file, true));
		    writer.write(msg);
			writer.newLine();
		    writer.close();
		} catch (IOException e) {
			SMPI.getLog().send(LogSeverity.ERROR, LogType.LOG, "Writing config file error : " + e.toString(), false);
		}
		return 1;
	}
	
	public void set(String path, Object value) {
			int line = getLine(path);
			if(line == -1) {
				write(path + ":" + value);
			} else {
			List<String> lines;
				try {
					lines = Files.readAllLines(file.toPath());
					lines.set(line, path + ":" + value);
					Files.write(file.toPath(), lines); 
				} catch (IOException e) {
					SMPI.getLog().send(LogSeverity.ERROR, LogType.LOG, "Writing config file error : " + e.toString(), false);
				}
			}
	}
	
	public int getLine(String path) {
		int nline = -1;
		try {  
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			
			String line;  
			int i = 0;
			while((line=br.readLine())!=null)  {  
				if(line.contains(path)) {
					if(line.contains(":")) {
						nline = i;
						break;
					}
				}
				i++;
			}  
			fr.close();
		} catch(IOException e) {  
       	    SMPI.getLog().send(LogSeverity.ERROR, LogType.CONFIG, "Reading config file error : " + e.toString(), false);
		}  
		return nline;
	}
	
	public String getString(String path) {
		String value = "";
		try {  
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			
			String line;  
			while((line=br.readLine())!=null)  {  
				if(!(line.contains("#"))) {
					if(line.contains(path)) {
						if(line.contains(":")) {
							String[] parameter = line.split(":");
							value = parameter[1];
							break;
						}
					}
				}
				
			}  
			fr.close();
		} catch(IOException e) {  
       	    SMPI.getLog().send(LogSeverity.ERROR, LogType.CONFIG, "Reading config file error : " + e.toString(), false);
		}  
		return value;
	}
	
	
	public ArrayList<String> getList(String path) {
		boolean add = false;
		ArrayList<String> list = new ArrayList<String>();
		try {  
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			
			String line;  
			while((line=br.readLine())!=null)  {  
				if(!(line.contains("#"))) {
					
					if(add==true) {
						if(line.contains("-")) {
							String s = line.replaceAll("\t", " ");
							s = s.replaceAll(" - ", "");
							list.add(s);
						} else {
							add=false;
						}
					}
					
					if(line.contains(path)) {
						add=true;
					}
				}
				
			}  
			fr.close();
		} catch(IOException e) {  
       	    SMPI.getLog().send(LogSeverity.ERROR, LogType.CONFIG, "Reading config file error : " + e.toString(), false);
		}  
		return list;
	}
	
	public String read() {
		try {  
			FileReader fr = new FileReader(file); 
			BufferedReader br = new BufferedReader(fr);  
			StringBuffer sb = new StringBuffer();  
			String line;  
			while((line=br.readLine())!=null)  {  
				sb.append(line + "\n");     
			}  
			fr.close();
			SMPI.getLog().send(LogSeverity.INFO, LogType.CONFIG, "Getting config file ...", false);
			return sb.toString();
		} catch(IOException e) {  
       	    SMPI.getLog().send(LogSeverity.ERROR, LogType.CONFIG, "Reading config file error : " + e.toString(), false);
		}  
		return "";
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

}
