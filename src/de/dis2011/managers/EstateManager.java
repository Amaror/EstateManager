package de.dis2011.managers;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.dis2011.data.*;

public class EstateManager {
	
	private Estate[] EstatesArray;
	
	public EstateManager() {
		EstatesArray = new Estate[50];
	}
	
	public Estate getEstate(int id) {
		return EstatesArray[id];
	}
	
	public void addHouse(int floor, int price, boolean garden, String city, int postalcode, String street, int streetnumber, int squarearea, int agent) {
		House newhouse = new House();
		newhouse.setFloor(floor);
		newhouse.setPrice(price);
		newhouse.setGarden(garden);
		newhouse.setCity(city);
		newhouse.setPCode(postalcode);
		newhouse.setStreet(street);
		newhouse.setStreetNumber(streetnumber);
		newhouse.setSquareArea(squarearea);
		newhouse.setAgent(agent);
		newhouse.save();
		EstatesArray[newhouse.getID()] = newhouse;
	}
	
	public void addApartment(int floor, int rent, int rooms, boolean balcony, boolean builtinkitchen, String city, int postalcode, String street, int streetnumber, int squarearea, int agent) {
		Apartment newapartment = new Apartment();
		newapartment.setFloor(floor);
		newapartment.setRent(rent);
		newapartment.setRooms(rooms);
		newapartment.setBalcony(balcony);
		newapartment.setBuiltInKitchen(builtinkitchen);
		newapartment.setCity(city);
		newapartment.setPCode(postalcode);
		newapartment.setStreet(street);
		newapartment.setStreetNumber(streetnumber);
		newapartment.setSquareArea(squarearea);
		newapartment.setAgent(agent);
		newapartment.save();
		EstatesArray[newapartment.getID()] = newapartment;
	}
	
	public void loadEstates() {
		int count;
		try{
			// Hole Verbindung
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "SELECT COUNT(*) AS NumberOfEstates FROM estate";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);

			// Führe Anfrage aus
			ResultSet rs = pstmt.executeQuery();
			
			count = rs.getInt("NumberOfEstates");
			
			for(int x = 1; x < count + 1; x++) {
				Apartment apartment = Apartment.load(x);
				if(apartment == null) {
					House house = House.load(x);
					EstatesArray[x] = house;
				}
				else{
					EstatesArray[x] = apartment;
				}
				
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public String checkEstate(int id) {
		if(EstatesArray[id] instanceof House) {
			return "House";
		}
		else if(EstatesArray[id] instanceof Apartment) {
			return "Apartment";
		}
		return null;
	}

}
