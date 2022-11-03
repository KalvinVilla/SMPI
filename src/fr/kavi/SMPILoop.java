package fr.kavi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import fr.kavi.KLog.LogSeverity;
import fr.kavi.KLog.LogType;


/**
 * 	Information about SMPI.class
 * 	Created by Kalvin VILLA
 * 	Created 05/07/2021
 * 
 * 	Description :
 *	This class is the main class of programs
 *
 *	Last edit : 09/07/2021
 */
class SMPILoop extends Thread{
	
	private boolean running = true;
	
	public ArrayList<String> mails;
	public String ip;
	
	/**
	 * SMPI run used for launch process of send mail
	 */
	@Override
	public void run() {
		while(running) {
			mails = SMPI.getConfig().getList("mails");
			String last_ip = SMPI.getConfig().getString("ip");
			String new_ip = getPublicIP();
			SMPI.getLog().send(LogSeverity.INFO, LogType.APPLICATION, "Verify ip ...", false);
			if(!(last_ip.equals(new_ip))) {
				SMPI.getConfig().set("ip", new_ip);
				SMPI.getLog().send(LogSeverity.INFO, LogType.APPLICATION, "Last ip : " + last_ip, false);
				SMPI.getLog().send(LogSeverity.INFO, LogType.APPLICATION, "New IP :  " + new_ip, false);
				for(String mail : mails) {
					SMPI.getMail().sendMail(mail, "New IP", "https://" + new_ip + "");
				}
				
			}
			
			try {
				Thread.sleep(1000 * 1000);
			} catch (InterruptedException e) {
				SMPI.getLog().send(LogSeverity.ERROR, LogType.APPLICATION, "Thread error :  " + e.toString(), false);
				System.exit(-1);
			}
		}
		
	}
	
	public String getPublicIP() {
		URL whatismyip = null;
		try {
			whatismyip = new URL("https://ifconfig.me/ip");
		} catch (MalformedURLException e) {
			SMPI.getLog().send(LogSeverity.ERROR, LogType.APPLICATION, "Network error :  " + e.toString(), false);
		}
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(
			                whatismyip.openStream()));
		} catch (IOException e) {
			SMPI.getLog().send(LogSeverity.ERROR, LogType.APPLICATION, "Network error :  " + e.toString(), false);
		}

		String ip = "";
		try {
			ip = in.readLine();
		} catch (IOException e) {
			SMPI.getLog().send(LogSeverity.ERROR, LogType.APPLICATION, "Network error :  " + e.toString(), false);
		} 
		return ip;
	}

}
