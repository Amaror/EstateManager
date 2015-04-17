package de.dis2011.managers;

import de.dis2011.Menu;

public class ApplicationManager {
	
	private AgentManager agentManager;
	private ContractManager contractManager;
	private EstateManager estateManager;
	private PersonManager personManager;
	
	public ApplicationManager() {
		agentManager = new AgentManager();
		contractManager = new ContractManager();
		estateManager = new EstateManager();
		personManager = new PersonManager();
		showMainMenu();
	}
	
	
	public void showMainMenu() {
		//Men�optionen
		final int MENU_AGENT = 0;
		final int QUIT = 1;
		
		//Erzeuge Men�
		Menu mainMenu = new Menu("Hauptmen�");
		mainMenu.addEntry("Agenten-Verwaltung", MENU_AGENT);
		mainMenu.addEntry("Beenden", QUIT);
		
		//Verarbeite Eingabe
		while(true) {
			int response = mainMenu.show();
			
			switch(response) {
				case MENU_AGENT:
					showAgentMenu();
					break;
				case QUIT:
					return;
			}
		}
	}
	
	/**
	 * Zeigt die Maklerverwaltung
	 */
	public void showAgentMenu() {
		//Men�optionen
		final int NEW_AGENT = 0;
		final int BACK = 1;
		
		//Maklerverwaltungsmen�
		Menu maklerMenu = new Menu("Makler-Verwaltung");
		maklerMenu.addEntry("Neuer Makler", NEW_AGENT);
		maklerMenu.addEntry("Zur�ck zum Hauptmen�", BACK);
		
		//Verarbeite Eingabe
		while(true) {
			int response = maklerMenu.show();
			
			switch(response) {
				case NEW_AGENT:
					newAgent();
					break;
				case BACK:
					return;
			}
		}
	}
	
	/**
	 * Legt einen neuen Makler an, nachdem der Benutzer
	 * die entprechenden Daten eingegeben hat.
	 */
	
	public void newAgent() {
		
		
		System.out.println("Makler mit der ID "+m.getId()+" wurde erzeugt.");
		
	}

}
