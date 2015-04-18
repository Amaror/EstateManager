package de.dis2011.managers;

import de.dis2011.Menu;
import de.dis2011.FormUtil;
import de.dis2011.data.*;

public class ApplicationManager {
	
	private AgentManager agentManager;
	private ContractManager contractManager;
	private EstateManager estateManager;
	private PersonManager personManager;
	
	private int loggedInAgent;
	
	public ApplicationManager() {
		agentManager = new AgentManager();
		contractManager = new ContractManager();
		estateManager = new EstateManager();
		personManager = new PersonManager();
		agentManager.loadAgents();
		contractManager.loadContracts();
		estateManager.loadEstates();
		personManager.loadContracts();
		showMainMenu();
	}
	
	
	public void showMainMenu() {
		//Menüoptionen
		final int MENU_AGENT = 0;
		final int MENU_ESTATE = 1;
		final int MENU_CONTRACT = 2;
		final int QUIT = 3;
		
		//Erzeuge Menü
		Menu mainMenu = new Menu("Hauptmenü");
		mainMenu.addEntry("Agenten-Verwaltung", MENU_AGENT);
		mainMenu.addEntry("Estate-Verwaltung", MENU_ESTATE);
		mainMenu.addEntry("Vertrag-Verwaltung", MENU_CONTRACT);
		mainMenu.addEntry("Beenden", QUIT);
		
		//Verarbeite Eingabe
		while(true) {
			int response = mainMenu.show();
			
			switch(response) {
				case MENU_AGENT:
					switchToAgentMenu();
					break;
				case MENU_ESTATE:
					switchToEstateMenu();
					break;
				case MENU_CONTRACT:
					break;
				case QUIT:
					return;
			}
		}
	}
	
	public void switchToAgentMenu(){
		String password = FormUtil.readString("Passwort");
		
		if(password.equals("password")){
			showAgentMenu();
		}
		else{
			System.out.println("Zugriff abgelehnt. Falsches Password.");
		}
		return;
	}
	
	/**
	 * Zeigt die Maklerverwaltung
	 */
	public void showAgentMenu() {
		//Menüoptionen
		final int NEW_AGENT = 0;
		final int CHANGE_AGENT = 1;
		final int BACK = 2;
		
		//Maklerverwaltungsmenü
		Menu agentMenu = new Menu("Agenten-Verwaltung");
		agentMenu.addEntry("Neuer Agent", NEW_AGENT);
		agentMenu.addEntry("Agenten bearbeiten", CHANGE_AGENT);
		agentMenu.addEntry("Zurück zum Hauptmenü", BACK);
		
		//Verarbeite Eingabe
		while(true) {
			int response = agentMenu.show();
			
			switch(response) {
				case NEW_AGENT:
					newAgent();
					break;
				case CHANGE_AGENT:
					if(agentManager.getAgentCount() > 0){changeAgent();}
					else{System.out.println("Keine Agenten gefunden. Erstelle neue Agenten um diese zu bearbeiten.");}
					break;
				case BACK:
					return;
			}
		}
	}
	
	public void switchToEstateMenu(){
		String login = FormUtil.readString("Login");
		String password = FormUtil.readString("Passwort");
		
		if(agentManager.checkPassword(login, password) != -1) {
			loggedInAgent = agentManager.checkPassword(login, password);
			showEstateMenu();
		}
		return;
	}
	
	public void showEstateMenu(){
		final int CREATE_ESTATE = 0;
		final int CHANGE_ESTATE = 1;
		final int BACK = 2;
		
		Menu estateMenu = new Menu("Estate-Verwaltung");
		estateMenu.addEntry("Neue Estate", CREATE_ESTATE);
		estateMenu.addEntry("Estate bearbeiten", CHANGE_ESTATE);
		estateMenu.addEntry("Zurück zum Hauptmenü", BACK);
		
		//Verarbeite Eingabe
		while(true) {
			int response = estateMenu.show();
			
			switch(response) {
				case CREATE_ESTATE:
					newEstate();
					break;
				case CHANGE_ESTATE:
					if(estateManager.getEstateCount() > 0){changeAgent();}
					else{System.out.println("Keine Estates gefunden. Erstelle neue Estates um diese zu bearbeiten.");}
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
		
		String name = FormUtil.readString("Name");
		String address = FormUtil.readString("Adresse");
		String login = FormUtil.readString("Login");
		String password = FormUtil.readString("Passwort");
		
		int id = agentManager.addAgent(name, address, login, password);
		
		
		System.out.println("Makler mit der ID "+id+" wurde erzeugt.");
	}
	
	public void changeAgent() {
		for(int x = 1; x < (agentManager.getAgentCount()+1); x++){
			Agent a = agentManager.getAgent(x);
			System.out.println("Agent-ID: " + a.getId() + " Name: " + a.getName() + " Adresse: " + a.getAddress() + " Login: " + a.getLogin());
		}
		int id = FormUtil.readInt("Zu ändernde Agenden-ID");
		
		final int CHANGE_AGENT = 0;
		final int DELETE_AGENT = 1;
		final int BACK = 2;
		
		Menu agentMenu = new Menu("Agenten berarbeiten");
		agentMenu.addEntry("Agenten bearbeiten", CHANGE_AGENT);
		agentMenu.addEntry("Agenten löschen", DELETE_AGENT);
		agentMenu.addEntry("Abbrechen", BACK);
		
		Agent b = agentManager.getAgent(id);
		
		while(true) {
			int response = agentMenu.show();
			
			switch(response) {
				case CHANGE_AGENT:
					String name = FormUtil.readString("Alter Name: " + b.getName() + " Neuer Name");
					String address = FormUtil.readString("Alte Adresse: " + b.getAddress() + " Neue Adresse");
					String login = FormUtil.readString("Alter Login: " + b.getLogin() + " Neuer Login");
					String password = FormUtil.readString("Altes Password: " + b.getPassword() + " Neues Password");
					
					agentManager.changeAgent(name, address, login, password, id);
					break;
				case DELETE_AGENT:
					agentManager.deleteAgent(b.getId());
					break;
				case BACK:
					return;
			}
			
		}
		
		
	}
	
	//Methoden der Estate Verwaltung
	
	public void newEstate(){
		final int APARTMENT = 0;
		final int HOUSE = 1;
		final int BACK = 2;
		
		Menu estateMenu = new Menu("Neue Estate erstellen");
		estateMenu.addEntry("Neues Apartment", APARTMENT);
		estateMenu.addEntry("Neues Haus", HOUSE);
		estateMenu.addEntry("Abbrechen", BACK);
		
		int floor;
		String city;
		int postalcode;
		String street;
		int streetnumber;
		int squarearea;
		int id;
		
		
		//Verarbeite Eingabe
		while(true) {
			int response = estateMenu.show();
			
			switch(response) {
				case APARTMENT:
					floor = FormUtil.readInt("Floor");
					int rent = FormUtil.readInt("Rent");
					int rooms = FormUtil.readInt("Rooms");
					Boolean balcony= (FormUtil.readInt("Balcony") == 1);
					Boolean builtinkitchen = (FormUtil.readInt("BuiltInKitchen") == 1);
					city = FormUtil.readString("City");
					postalcode = FormUtil.readInt("Postal Code");
					street = FormUtil.readString("Street");
					streetnumber = FormUtil.readInt("Street Name");
					squarearea = FormUtil.readInt("Square Area");
					id = estateManager.addApartment(floor, rent, rooms, balcony, builtinkitchen, city, postalcode, street, streetnumber, squarearea, loggedInAgent);
					System.out.println("Apartment mit der ID "+id+" wurde erzeugt.");
					break;
				case HOUSE:
					floor = FormUtil.readInt("Floor");
					int price = FormUtil.readInt("Price");
					Boolean garden = (FormUtil.readInt("Garden") == 1);
					city = FormUtil.readString("City");
					postalcode = FormUtil.readInt("Postal Code");
					street = FormUtil.readString("Street");
					streetnumber = FormUtil.readInt("Street Name");
					squarearea = FormUtil.readInt("Square Area");
					id = estateManager.addHouse(floor, price, garden, city, postalcode, street, streetnumber, squarearea, loggedInAgent);
					System.out.println("Haus mit der ID "+id+" wurde erzeugt.");
					break;
				case BACK:
					return;
			}
		}
	}
	
	public void changeEstate() {
		for(int x = 1; x < (estateManager.getEstateCount()+1); x++){
			if(estateManager.checkEstate(x).equals("apartment")){
				Apartment a = Apartment.load(x);
				System.out.println("Estate-ID: " + a.getID() + " Floor: " + a.getFloor() + " Rent: " + a.getRent() + " Rooms: " + a.getRooms() + " Balcony: " + a.getBalcony() + " Built-in-Kitchen: " + a.getBuiltInKitchen() + " City: " + a.getCity() + " Postal Code: " + a.getPCode() + " Street: " + a.getStreet() + " Street Number: " + a.getStreetNumber() + " Square Area: " + a.getSquareArea() + " Agent-ID: " + a.getAgent());
			}
			else{
				House a = House.load(x);
				System.out.println("Estate-ID: " + a.getID() + " Floors: " + a.getFloor() + " Price: " + a.getPrice() + " Garden: " + a.getGarden() + " City: " + a.getCity() + " Postal Code: " + a.getPCode() + " Street: " + a.getStreet() + " Street Number: " + a.getStreetNumber() + " Square Area: " + a.getSquareArea() + " Agent-ID: " + a.getAgent());
			}
		}
		int id = FormUtil.readInt("Zu ändernde Estate-ID");
		
		final int CHANGE_ESTATE = 0;
		final int DELETE_ESTATE = 1;
		final int BACK = 2;
		
		Menu agentMenu = new Menu("Estates berarbeiten");
		agentMenu.addEntry("Estate bearbeiten", CHANGE_ESTATE);
		agentMenu.addEntry("Estate löschen", DELETE_ESTATE);
		agentMenu.addEntry("Abbrechen", BACK);
		
		if(estateManager.checkEstate(id).equals("apartment")){
			Apartment b = Apartment.load(id);
		}
		else{
			House b = House.load(id);
		}
		
		while(true) {
			int response = agentMenu.show();
			
			switch(response) {
				case CHANGE_ESTATE:
					String city = FormUtil.readString("Alter Name: " + b.getName() + " Neuer Name");
					String  = FormUtil.readString("Alte Adresse: " + b.getAddress() + " Neue Adresse");
					String login = FormUtil.readString("Alter Login: " + b.getLogin() + " Neuer Login");
					String password = FormUtil.readString("Altes Password: " + b.getPassword() + " Neues Password");
					if(b instanceof Apartment){
						
					}
					String name = FormUtil.readString("Alter Name: " + b.getName() + " Neuer Name");
					String address = FormUtil.readString("Alte Adresse: " + b.getAddress() + " Neue Adresse");
					String login = FormUtil.readString("Alter Login: " + b.getLogin() + " Neuer Login");
					String password = FormUtil.readString("Altes Password: " + b.getPassword() + " Neues Password");
					
					agentManager.changeAgent(name, address, login, password, id);
					break;
				case DELETE_ESTATE:
					agentManager.deleteAgent(b.getId());
					break;
				case BACK:
					return;
			}
			
		}
		
		
	}
	
}
