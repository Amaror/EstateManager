package de.dis2011.managers;

import de.dis2011.Menu;
import de.dis2011.FormUtil;
import de.dis2011.data.*;

public class ApplicationManager {
	
	private AgentManager agentManager;
	private ContractManager contractManager;
	private EstateManager estateManager;
	private PersonManager personManager;
	
	private int loggedInAgent = 0;
	
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
					showContractMenu();
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
		if(loggedInAgent == 0){
			System.out.println("Kein Agent zur Estate-Verwaltung vorhanden.");
			return;
		}
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
					if(estateManager.getEstateCount() > 0){changeEstate();}
					else{System.out.println("Keine Estates gefunden. Erstelle neue Estates um diese zu bearbeiten.");}
					break;
				case BACK:
					return;
			}
		}
	}
	
	public void showContractMenu(){
		final int CREATE_PERSON = 0;
		final int CREATE_CONTRACT = 1;
		final int SHOW_CONTRACT = 2;
		final int BACK = 3;
		
		Menu contractMenu = new Menu("Vertrag-Verwaltung");
		contractMenu.addEntry("Neue Person", CREATE_PERSON);
		contractMenu.addEntry("Vertrag unterzeichnen", CREATE_CONTRACT);
		contractMenu.addEntry("Verträge zeigen", SHOW_CONTRACT);
		contractMenu.addEntry("Zurück zum Hauptmenü", BACK);
		
		//Verarbeite Eingabe
		while(true) {
			int response = contractMenu.show();
			
			switch(response) {
				case CREATE_PERSON:
					newPerson();
					break;
				case CREATE_CONTRACT:
					newContract();
					break;
				case SHOW_CONTRACT:
					if(contractManager.getContractCount() > 0){showContracts();}
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
		
		
		System.out.println("Agent mit der ID "+id+" wurde erzeugt.");
	}
	
	public void changeAgent() {
		for(int x = 1; x < (agentManager.getAgentCount()+1); x++){
			if(agentManager.checkForAgent(x)){
				Agent a = agentManager.getAgent(x);
				System.out.println("Agent-ID: " + a.getId() + " Name: " + a.getName() + " Adresse: " + a.getAddress() + " Login: " + a.getLogin());
			} else{
				System.out.println("Kein Agent mit ID " + x + " vorhanden");
			}
		}
		int id = FormUtil.readInt("Zu ändernde Agenden-ID");
		if(!agentManager.checkForAgent(id)){
			System.out.println("Kein gültiger Agent");
			return;
		}
		
		final int CHANGE_AGENT = 0;
		final int DELETE_AGENT = 1;
		final int LOGIN_AGENT = 2;
		final int BACK = 3;
		
		Menu agentMenu = new Menu("Agenten berarbeiten");
		agentMenu.addEntry("Agenten bearbeiten", CHANGE_AGENT);
		agentMenu.addEntry("Agenten löschen", DELETE_AGENT);
		agentMenu.addEntry("Agenten anmelden", LOGIN_AGENT);
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
					if(!estateManager.checkForAgentReference(b.getId())){
						if(b.getId() == loggedInAgent){
							loggedInAgent = 0;
							System.out.println("Angemeldeter Agent wurde gelöscht. Kein Agent im Moment angemeldet.");
						}
						agentManager.deleteAgent(b.getId());
					} else{
						System.out.println("Agent verwaltet zurzeit ein oder mehrere Häuser und kann nicht entfernt werden.");
						return;
					}
					break;
				case LOGIN_AGENT:
					loggedInAgent=b.getId();
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
				System.out.println("Apartment-ID: " + a.getID() + " Floor: " + a.getFloor() + " Rent: " + a.getRent() + " Rooms: " + a.getRooms() + " Balcony: " + a.getBalcony() + " Built-in-Kitchen: " + a.getBuiltInKitchen() + " City: " + a.getCity() + " Postal Code: " + a.getPCode() + " Street: " + a.getStreet() + " Street Number: " + a.getStreetNumber() + " Square Area: " + a.getSquareArea() + " Agent-ID: " + a.getAgent());
			}
			else if(estateManager.checkEstate(x).equals("house")){
				House a = House.load(x);
				System.out.println("Haus-ID: " + a.getID() + " Floors: " + a.getFloor() + " Price: " + a.getPrice() + " Garden: " + a.getGarden() + " City: " + a.getCity() + " Postal Code: " + a.getPCode() + " Street: " + a.getStreet() + " Street Number: " + a.getStreetNumber() + " Square Area: " + a.getSquareArea() + " Agent-ID: " + a.getAgent());
			}
			else{
				System.out.println("Keine Estate mit der ID: " + x);
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
		
		while(true) {
			int response = agentMenu.show();
			
			switch(response) {
				case CHANGE_ESTATE:
					if(estateManager.checkEstate(id).equals("apartment")){
						Apartment b = Apartment.load(id);
						String city = FormUtil.readString("Alter Name: " + b.getCity() + " Neuer Name");
						int postalcode = FormUtil.readInt("Alte Adresse: " + b.getPCode() + " Neue Adresse");
						String street = FormUtil.readString("Alter Login: " + b.getStreet() + " Neuer Login");
						int streetnumber = FormUtil.readInt("Altes Password: " + b.getStreetNumber() + " Neues Password");
						int squarearea = FormUtil.readInt("Alter Login: " + b.getSquareArea() + " Neuer Login");
						int floor = FormUtil.readInt("Alter Name: " + b.getFloor() + " Neuer Name");
						int rent = FormUtil.readInt("Alte Adresse: " + b.getRent() + " Neue Adresse");
						int rooms = FormUtil.readInt("Alter Name: " + b.getRooms() + " Neuer Name");
						boolean balcony = (FormUtil.readInt("Alte Adresse: " + b.getBalcony() + " Neue Adresse") == 1);
						boolean builtinkitchen = (FormUtil.readInt("Alter Name: " + b.getBuiltInKitchen() + " Neuer Name") == 1);
						estateManager.changeApartment(floor, rent, rooms, balcony, builtinkitchen, city, postalcode, street, streetnumber, squarearea, id);
					}else{
						House b = House.load(id);
						String city = FormUtil.readString("Alter Name: " + b.getCity() + " Neuer Name");
						int postalcode = FormUtil.readInt("Alte Adresse: " + b.getPCode() + " Neue Adresse");
						String street = FormUtil.readString("Alter Login: " + b.getStreet() + " Neuer Login");
						int streetnumber = FormUtil.readInt("Altes Password: " + b.getStreetNumber() + " Neues Password");
						int squarearea = FormUtil.readInt("Alter Login: " + b.getSquareArea() + " Neuer Login");
						int floor = FormUtil.readInt("Alter Name: " + b.getFloor() + " Neuer Name");
						int price = FormUtil.readInt("Alte Adresse: " + b.getPrice() + " Neue Adresse");
						boolean garden = (FormUtil.readInt("Alter Name: " + b.getGarden() + " Neuer Name") == 1);
						estateManager.changeHouse(floor, price, garden, city, postalcode, street, streetnumber, squarearea, id);
					}	
					break;
				case DELETE_ESTATE:
					estateManager.deleteEstate(id);
					break;
				case BACK:
					return;
			}
		}
	}
	
	public void newPerson(){
		String firstname = FormUtil.readString("Vorname");
		String lastname = FormUtil.readString("Nachname");
		String adress = FormUtil.readString("Adresse");
		
		int id = personManager.addPerson(firstname, lastname, adress);
		
		System.out.println("Person mit der ID "+id+" wurde erzeugt.");
	}
	
	public void newContract(){	
		if(personManager.getPersonCount() == 0){
			System.out.println("Keine Käufer vorhanden. Verträge können nicht ohne Käufer unterschrieben werden.");
			return;
		}
		int buyer = 0;
		boolean searchBuyer = true;
		while(searchBuyer) {
			buyer = FormUtil.readInt("Es stehen Käufer der ID's von 1 bis " + personManager.getPersonCount() + " zur Verfügung. Wähle:");
			if(!(buyer > personManager.getPersonCount() || buyer < 1)){
				searchBuyer = false;
			} else {
				System.out.println("Käufer existiert nicht. Bitte gültige Käufer eingeben.");
			}
		}
		
		int estate = 0;
		boolean searchEstate = true;
		while(searchEstate) {
			estate = FormUtil.readInt("Es stehen Häuser der ID's von 1 bis " + estateManager.getEstateCount() + " zur Verfügung. Wähle:");
			if(estateManager.isValidEstate(estate)){
				searchEstate = false;
			} else {
				System.out.println("Estate nicht gültig. Bitte gültige Estate eingeben.");
			}
		}
		
		if(estateManager.checkEstate(estate).equals("apartment")){
			String date = FormUtil.readString("Datum");
			String startdate = FormUtil.readString("Startdatum");
			int duration = FormUtil.readInt("Dauer");
			int addcost = FormUtil.readInt("Zusatzkosten");
			int id = contractManager.addTenancyContract(startdate, duration, addcost, buyer, date, estate);
			System.out.println("Mietvertrag mit der Nummer "+id+" wurde unterschrieben.");			
		} else {
			String date = FormUtil.readString("Datum");
			int nooinstallments = FormUtil.readInt("Anzahl an Zahlungen");
			int interestrate = FormUtil.readInt("Zinssatz");
			int id = contractManager.addPurchaseContract(nooinstallments, interestrate, buyer, date, estate);
			System.out.println("Kaufvertrag mit der Nummer "+id+" wurde unterschrieben.");
		}
		
	}
	
	public void showContracts(){
		for(int x = 1; x < (contractManager.getContractCount()+1); x++){
			if(contractManager.checkContract(x).equals("TenancyContract")){
				TenancyContract a = TenancyContract.load(x);
				System.out.println("Mietvertragnummer: " + a.getContractNo() +  " Datum: " + a.getDate() +  " Startdarum: " + a.getStartDate() +  " Dauer: " + a.getDuration() +  " Zusatzkosten: " + a.getAddCost() +  " Estate-ID: " + a.getPlaceID() +  " Käufer-ID: " + a.getBuyer());
			}
			else if(contractManager.checkContract(x).equals("PurchaseContract")){
				PurchaseContract a = PurchaseContract.load(x);
				System.out.println("Kaufvertragnummer: " + a.getContractNo() +  " Datum: " + a.getDate() +  " Anzahl an Zahlungen: " + a.getNoOInstallments() +  " Zinssatz: " + a.getInterestRate() +  " Estate-ID: " + a.getPlaceID() +  " Käufer-ID: " + a.getBuyer());
			}
			else{
				System.out.println("Kein Vertrag mit der ID: " + x + "gefunden.");
			}
		}
	}
	
}
