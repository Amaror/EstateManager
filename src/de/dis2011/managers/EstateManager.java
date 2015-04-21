package de.dis2011.managers;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.dis2011.data.*;

public class EstateManager {
	
	private Estate[] EstatesArray;
	private int estateCount;
	
	public EstateManager() {
		EstatesArray = new Estate[50];
		estateCount = 0;
	}
	
	public Estate getEstate(int id) {
		return EstatesArray[id];
	}
	
	public int getEstateCount(){
		return estateCount;
	}
	
	public int addHouse(int floor, int price, boolean garden, String city, int postalcode, String street, int streetnumber, int squarearea, int agent) {
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
		estateCount++;
		return newhouse.getID();
	}
	
	public int addApartment(int floor, int rent, int rooms, boolean balcony, boolean builtinkitchen, String city, int postalcode, String street, int streetnumber, int squarearea, int agent) {
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
		estateCount++;
		return newapartment.getID();
	}
	
	public void changeHouse(int floor, int price, boolean garden, String city, int postalcode, String street, int streetnumber, int squarearea, int id) {
		House house = (House)EstatesArray[id];
		house.setFloor(floor);
		house.setPrice(price);
		house.setGarden(garden);
		house.setCity(city);
		house.setPCode(postalcode);
		house.setStreet(street);
		house.setStreetNumber(streetnumber);
		house.setSquareArea(squarearea);
		house.save();
	}
	
	public void changeApartment(int floor, int rent, int rooms, boolean balcony, boolean builtinkitchen, String city, int postalcode, String street, int streetnumber, int squarearea, int id) {
		Apartment apartment = (Apartment)EstatesArray[id];
		apartment.setFloor(floor);
		apartment.setRent(rent);
		apartment.setRooms(rooms);
		apartment.setBalcony(balcony);
		apartment.setBuiltInKitchen(builtinkitchen);
		apartment.setCity(city);
		apartment.setStreet(street);
		apartment.setStreetNumber(streetnumber);
		apartment.setSquareArea(squarearea);
		apartment.save();
	}
	
	public void deleteEstate(int id) {
		try{
			String estate = checkEstate(id);
			
			// Hole Verbindung
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL;
			if(estate.equals("house")){selectSQL = "DELETE FROM house WHERE houseid=?";}
			else{selectSQL = "DELETE FROM apartment WHERE apartmentid=?";}
			
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, id);

			// Führe Anfrage aus
			pstmt.executeUpdate();
			
			selectSQL = "DELETE FROM estate WHERE id=?";
			pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, id);
			EstatesArray[id] = null;
			estateCount--;
		
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
			
			rs.next();
			count = rs.getInt(1);
			estateCount = count;
			
			for(int x = 1; x < count + 1; x++) {
				if(!checkForApartment(x)) {
					House house = House.load(x);
					EstatesArray[x] = house;
				}
				else {
					Apartment apartment = Apartment.load(x);
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
			return "house";
		}
		else if(EstatesArray[id] instanceof Apartment) {
			return "apartment";
		}
		return "No Estate";
	}
	
	public Boolean checkForApartment(int id) {
		try{
			// Hole Verbindung
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "SELECT COUNT(*) FROM apartment WHERE apartmentid = ? ";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, id);
			
			// Führe Anfrage aus
			ResultSet rs = pstmt.executeQuery();
			rs.next();
					
			if(rs.getInt(1) == 1) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
	public boolean isValidEstate(int id) {
		return !(EstatesArray[id] == null);
	}
	
	public boolean checkForAgentReference(int id){
		for(Estate x: EstatesArray){
			if(x.getAgent() == id){
				return true;
			}
		}
		return false;
	}

}
