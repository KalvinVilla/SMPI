package fr.kavi;

class SMPI {
	
	private static String name = "SMPI";
	private static String version = "1.0";
	
	private static KLog log;
	private static KConfig config;
	private static KMail mail;
	
	private static SMPILoop loop;

	private static String motd = ConsoleColors.ANSI_GREEN + "\n"
			+ "   _____       __  __    _____    _____ \r\n"
			+ "  / ____|     |  \\/  |  |  __ \\  |_   _|\r\n"
			+ " | (___       | \\  / |  | |__) |   | |  \r\n"
			+ "  \\___ \\      | |\\/|  | |  ___/    | |  \r\n"
			+ "  ____) |     | |  | |  | |       _| |_ \r\n"
			+ " |_____/end   |_|  |_|y |_|ublic |_____|P v" + version;
			;
	
	public static void main(String[] args) {
		System.out.println(motd);
		initialize();
	}
	
	public static void initialize() {
		log = new KLog(name);
		config = new KConfig(name);
		mail = new KMail();
		
		System.out.println((config.getList("mails").size()));
		for(String s : config.getList("mails")) {
			
			System.out.println(s);
		}
		
		loop = new SMPILoop();
		loop.run();
		
		System.exit(0);
		
	}
	
	public static String getName() {
		return name;
	}

	public static KLog getLog() {
		return log;
	}

	public static KMail getMail() {
		return mail;
	}

	public static KConfig getConfig() {
		return config;
	}
	
}
