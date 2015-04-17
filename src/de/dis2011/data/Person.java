package de.dis2011.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Person {
	
	private int PID;
	private String FirstName;
	private String Name;
	private String Adress;
	
	public Person() {
		
	}
	
	//Getter/setter
	
	public int getPID(){
		return PID;
	}
	
	public void setPID(int pid) {
		PID = pid;
	}
	
	public String getFirstName(){
		return FirstName;
	}
	
	public void setFirstName(String firstname) {
		FirstName = firstname;
	}
	
	public String getName(){
		return Name;
	}
	
	public void setName(String name) {
		Name = name;
	}
	
	public String getAdress(){
		return Adress;
	}
	
	public void setAdress(String adress) {
		Adress = adress;
	}
	
	//Ladet die Estate
		public static Person load(int id) {
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
					Person ts = new Person();
					ts.setPID(id);
					ts.setFirstName(rs.getString("firstname"));
					ts.setName(rs.getString("name"));
					ts.setAdress(rs.getString("adress"));

					rs.close();
					pstmt.close();
					//return ts;
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
				if (getPID() == -1) {
					// Achtung, hier wird noch ein Parameter mitgegeben,
					// damit spC$ter generierte IDs zurC<ckgeliefert werden!
					String insertSQL = "INSERT INTO person(pid, firstname, name, adress) VALUES (?, ?, ?, ?)";

					PreparedStatement pstmt = con.prepareStatement(insertSQL,
							Statement.RETURN_GENERATED_KEYS);

					// Setze Anfrageparameter und fC<hre Anfrage aus
					pstmt.setInt(1, getPID());
					pstmt.setString(2, getFirstName());
					pstmt.setString(3, getName());
					pstmt.setString(4, getAdress());
					
					pstmt.executeUpdate();

					// Hole die Id des engefC<gten Datensatzes
					ResultSet rs = pstmt.getGeneratedKeys();
					if (rs.next()) {
						setPID(rs.getInt(1));
					}

					rs.close();
					pstmt.close();
				} else {
					// Falls schon eine ID vorhanden ist, mache ein Update...
					String updateSQL = "UPDATE person SET firstname = ?, name = ?, adress = ? WHERE pid = ?";
					PreparedStatement pstmt = con.prepareStatement(updateSQL);

					// Setze Anfrage Parameter
					pstmt.setString(1, getFirstName());
					pstmt.setString(2, getName());
					pstmt.setString(3, getAdress());

					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

}
