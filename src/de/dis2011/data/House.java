package de.dis2011.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class House extends Estate{
	
	private int Floor;
	private int Price;
	private boolean Garden;
	private int EstateID;
	
	//Getter and Setter
	public int getFloor() {
		return Floor;
	}
	
	public void setFloor(int floor) {
		Floor = floor;
	}
	
	public int getPrice() {
		return Price;
	}
	
	public void setPrice(int price) {
		Price = price;
	}
	
	public boolean getGarden() {
		return Garden;
	}
	
	public void setGarden(boolean garden) {
		Garden = garden;
	}
	
	public int getEstate() {
		return EstateID;
	}
	
	public void setEstate(int estate) {
		EstateID = estate;
	}
	
	public static House load(int id) {
		try {
			// Hole Verbindung
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "SELECT * FROM house WHERE estateid = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, id);

			// Führe Anfrage aus
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				House ts = new House();
				ts.setFloor(rs.getInt("floor"));
				ts.setPrice(rs.getInt("price"));
				ts.setGarden(rs.getBoolean("garden"));
				ts.setEstate(rs.getInt("estateid"));

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
				String insertSQL = "INSERT INTO estate(city, postcode, street, streetnumber, squarearea, agentid) VALUES (?, ?, ?, ?, ?, ?)";

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
				
				insertSQL = "INSERT INTO house(floor, price, garden, estateid) VALUES (?, ?, ?, ?)";

				pstmt = con.prepareStatement(insertSQL,
						Statement.RETURN_GENERATED_KEYS);

				// Setze Anfrageparameter und fC<hre Anfrage aus
				pstmt.setInt(1, getFloor());
				pstmt.setInt(2, getPrice());
				pstmt.setBoolean(3, getGarden());
				pstmt.setInt(4, getEstate());
				
				pstmt.executeUpdate();

				rs.close();
				pstmt.close();
			} else {
				// Falls schon eine ID vorhanden ist, mache ein Update...
				String updateSQL = "UPDATE estate SET city = ?, postcode = ?, street = ?, streetnumber = ?, squarearea = ?, agentid = ? WHERE id = ?";
				PreparedStatement pstmt = con.prepareStatement(updateSQL);

				// Setze Anfrage Parameter
				pstmt.setString(1, getCity());
				pstmt.setInt(2, getPCode());
				pstmt.setString(3, getStreet());
				pstmt.setInt(4, getStreetNumber());
				pstmt.setInt(5, getSquareArea());
				pstmt.setInt(6, getAgent());
				
				updateSQL = "UPDATE house SET floor = ?, price = ?, garden = ? WHERE estateid = ?";
				pstmt = con.prepareStatement(updateSQL);
				
				// Setze Anfrage Parameter
				pstmt.setInt(1, getFloor());
				pstmt.setInt(2, getPrice());
				pstmt.setBoolean(3, getGarden());

				pstmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
