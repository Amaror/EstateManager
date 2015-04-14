package de.dis2011.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Estate {
	//Estate Variables
	private int ID;
	private String City;
	private int PostalCode;
	private String Street;
	private int StreetNumber;
	private int SquareArea;
	private int AgentID;
	
	public Estate(){
	}

	//Getter Setter
	public int getID(){
		return ID;
	}
	
	public void setID(int id) {
		ID = id;
	}
	
	public String getCity(){
		return City;
	}
	
	public void setCity(String city) {
		City = city;
	}
	
	public int getPCode(){
		return PostalCode;
	}
	
	public void setPCode(int pcode) {
		PostalCode = pcode;
	}
	
	public String getStreet(){
		return Street;
	}
	
	public void setStreet(String street) {
		Street = street;
	}
	
	public int getStreetNumber(){
		return StreetNumber;
	}
	
	public void setStreetNumber(int sn) {
		StreetNumber = sn;
	}
	
	public int getSquareArea(){
		return SquareArea;
	}
	
	public void setSquareArea(int sq) {
		SquareArea = sq;
	}
	
	public int getAgent(){
		return AgentID;
	}
	
	public void setAgent(int agent) {
		AgentID = agent;
	}
	
	//Ladet die Estate
	public static Estate load(int id) {
		try {
			// Hole Verbindung
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "SELECT * FROM estate WHERE id = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, id);

			// Führe Anfrage aus
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				Estate ts = new Estate();
				ts.setID(id);
				ts.setCity(rs.getString("city"));
				ts.setPCode(rs.getInt("postalcode"));
				ts.setStreet(rs.getString("street"));
				ts.setStreetNumber(rs.getInt("streetnumber"));
				ts.setSquareArea(rs.getInt("squarearea"));
				ts.setAgent(rs.getInt("agentid"));

				rs.close();
				pstmt.close();
				return ts;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
