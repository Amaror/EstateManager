package de.dis2011.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Apartment extends Estate{
	
	private int Floor;
	private int Rent;
	private int Rooms;
	private boolean Balcony;
	private boolean BuiltInKitchen;
	private int EstateID;
	
	public Apartment() {
		
	}
	
	//Getter and Setter
	public int getFloor() {
		return Floor;
	}
	
	public void setFloor(int floor) {
		Floor = floor;
	}
	
	public int getRent() {
		return Rent;
	}
	
	public void setRent(int rent) {
		Rent = rent;
	}
	
	public int getRooms() {
		return Rooms;
	}
	
	public void setRooms(int rooms) {
		Rooms = rooms;
	}
	
	public boolean getBalcony() {
		return Balcony;
	}
	
	public void setBalcony(boolean balcony) {
		Balcony = balcony;
	}
	
	public boolean getBuiltInKitchen() {
		return BuiltInKitchen;
	}
	
	public void setBuiltInKitchen(boolean BIK) {
		BuiltInKitchen = BIK;
	}
	
	public int getEstate() {
		return EstateID;
	}
	
	public void setEstate(int estate) {
		EstateID = estate;
	}
	
	public static Apartment load(int id) {
		try {
			// Hole Verbindung
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "SELECT * FROM apartment WHERE estateid = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, id);

			// Führe Anfrage aus
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				Apartment ts = new Apartment();
				ts.setFloor(rs.getInt("floor"));
				ts.setRent(rs.getInt("rent"));
				ts.setRooms(rs.getInt("rooms"));
				if(rs.getInt("balcony") == 1){ts.setBalcony(true);}else{ts.setBalcony(false);};
				if(rs.getInt("builtinkitchen") == 1){ts.setBuiltInKitchen(true);}else{ts.setBuiltInKitchen(false);};
				ts.setEstate(rs.getInt("apartmentid"));

				rs.close();
				pstmt.close();
				
				selectSQL = "SELECT * FROM estate WHERE id = ?";
				pstmt = con.prepareStatement(selectSQL);
				pstmt.setInt(1, id);

				// Führe Anfrage aus
				rs = pstmt.executeQuery();
				
				ts.setID(id);
				ts.setCity(rs.getString("city"));
				ts.setPCode(rs.getInt("postalcode"));
				ts.setStreet(rs.getString("street"));
				ts.setStreetNumber(rs.getInt("streetnumber"));
				ts.setSquareArea(rs.getInt("squarearea"));
				ts.setAgent(rs.getInt("agentid"));
				
				return ts;
				}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void save() {
		// Hole Verbindung
		Connection con = DB2ConnectionManager.getInstance().getConnection();

		try {
			// FC<ge neues Element hinzu, wenn das Objekt noch keine ID hat.
			if (getID() == -1) {
				// Achtung, hier wird noch ein Parameter mitgegeben,
				// damit spC$ter generierte IDs zurC<ckgeliefert werden!
				String insertSQL = "INSERT INTO estate(city, postalcode, street, streetnumber, squarearea, agentid) VALUES (?, ?, ?, ?, ?, ?)";

				PreparedStatement pstmt = con.prepareStatement(insertSQL,
						Statement.RETURN_GENERATED_KEYS);

				// Setze Anfrageparameter und fC<hre Anfrage aus
				pstmt.setString(1, getCity());
				pstmt.setInt(2, getPCode());
				pstmt.setString(3, getStreet());
				pstmt.setInt(4, getStreetNumber());
				pstmt.setInt(5, getSquareArea());
				pstmt.setInt(6, getAgent());
				
				pstmt.executeUpdate();

				// Hole die Id des engefC<gten Datensatzes
				ResultSet rs = pstmt.getGeneratedKeys();
				if (rs.next()) {
					setID(rs.getInt(1));
					setEstate(rs.getInt(1));
				}
				
				insertSQL = "INSERT INTO apartment(floor, rent, rooms, balcony, builtinkitchen, apartmentid) VALUES (?, ?, ?, ?, ?, ?)";

				pstmt = con.prepareStatement(insertSQL,
						Statement.RETURN_GENERATED_KEYS);

				// Setze Anfrageparameter und fC<hre Anfrage aus
				pstmt.setInt(1, getFloor());
				pstmt.setInt(2, getRent());
				pstmt.setInt(3, getRooms());
				if(getBalcony()){pstmt.setInt(4, 1);}else{pstmt.setInt(4, 0);};
				if(getBuiltInKitchen()){pstmt.setInt(5, 1);}else{pstmt.setInt(5, 0);};
				pstmt.setInt(6, getEstate());
				
				pstmt.executeUpdate();

				rs.close();
				pstmt.close();
			} else {
				// Falls schon eine ID vorhanden ist, mache ein Update...
				String updateSQL = "UPDATE estate SET city = ?, postalcode = ?, street = ?, streetnumber = ?, squarearea = ?, agentid = ? WHERE id = ?";
				PreparedStatement pstmt = con.prepareStatement(updateSQL);

				// Setze Anfrage Parameter
				pstmt.setString(1, getCity());
				pstmt.setInt(2, getPCode());
				pstmt.setString(3, getStreet());
				pstmt.setInt(4, getStreetNumber());
				pstmt.setInt(5, getSquareArea());
				pstmt.setInt(6, getAgent());
				
				updateSQL = "UPDATE apartment SET floor = ?, Rent = ?, rooms = ?, balcony = ?, builtinkitchen = ? WHERE apartmentid = ?";
				pstmt = con.prepareStatement(updateSQL);
				
				// Setze Anfrage Parameter
				pstmt.setInt(1, getFloor());
				pstmt.setInt(2, getRent());
				pstmt.setInt(3, getRooms());
				if(getBalcony()){pstmt.setInt(4, 1);}else{pstmt.setInt(4, 0);};
				if(getBuiltInKitchen()){pstmt.setInt(5, 1);}else{pstmt.setInt(5, 0);};

				pstmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
