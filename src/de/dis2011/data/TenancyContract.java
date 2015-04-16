package de.dis2011.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TenancyContract extends Contract{
	
	private int StartDate;
	private int Duration;
	private int AddCost;
	private int Buyer;
	
	//Getter and Setter
	public int getStartDate() {
		return StartDate;
	}
	
	public void setStartDate(int startdate) {
		StartDate = startdate;
	}
	
	public int getDuration() {
		return Duration;
	}
	
	public void setDuration(int duration) {
		Duration = duration;
	}
	
	public int getAddCost() {
		return AddCost;
	}
	
	public void setAddCost(int addcost) {
		AddCost = addcost;
	}
	
	public int getBuyer() {
		return Buyer;
	}
	
	public void setBuyer(int buyer) {
		Buyer = buyer;
	}
	
	public static TenancyContract load(int id) {
		try {
			// Hole Verbindung
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "SELECT * FROM tenancycontract WHERE contractno = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, id);

			// Führe Anfrage aus
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				TenancyContract ts = new TenancyContract();
				ts.setStartDate(rs.getInt("startdate"));
				ts.setDuration(rs.getInt("duration"));
				ts.setAddCost(rs.getInt("addcost"));
				ts.setBuyer(rs.getInt("buyer"));

				rs.close();
				pstmt.close();
				
				selectSQL = "SELECT * FROM contract WHERE contractno = ?";
				pstmt = con.prepareStatement(selectSQL);
				pstmt.setInt(1, id);

				// Führe Anfrage aus
				rs = pstmt.executeQuery();
				
				ts.setContractNo(id);
				ts.setDate(rs.getInt("date"));
				ts.setPlaceID(rs.getInt("placeid"));
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
			if (getContractNo() == -1) {
				// Achtung, hier wird noch ein Parameter mitgegeben,
				// damit spC$ter generierte IDs zurC<ckgeliefert werden!
				String insertSQL = "INSERT INTO contract(date, placeid) VALUES (?, ?)";

				PreparedStatement pstmt = con.prepareStatement(insertSQL,
						Statement.RETURN_GENERATED_KEYS);

				// Setze Anfrageparameter und fC<hre Anfrage aus
				pstmt.setInt(1, getDate());
				pstmt.setInt(2, getPlaceID());
				
				pstmt.executeUpdate();

				// Hole die Id des engefC<gten Datensatzes
				ResultSet rs = pstmt.getGeneratedKeys();
				if (rs.next()) {
					setContractNo(rs.getInt(1));
				}
				
				insertSQL = "INSERT INTO tenancycontract(startdate, duration, addcost, placeid) VALUES (?, ?, ?, ?)";

				pstmt = con.prepareStatement(insertSQL,
						Statement.RETURN_GENERATED_KEYS);

				// Setze Anfrageparameter und fC<hre Anfrage aus
				pstmt.setInt(1, getStartDate());
				pstmt.setInt(2, getDuration());
				pstmt.setInt(3, getAddCost());
				pstmt.setInt(4, getPlaceID());
				
				pstmt.executeUpdate();

				rs.close();
				pstmt.close();
			} else {
				// Falls schon eine ID vorhanden ist, mache ein Update...
				String updateSQL = "UPDATE contract SET date = ?, placeid = ? WHERE contractno = ?";
				PreparedStatement pstmt = con.prepareStatement(updateSQL);

				// Setze Anfrage Parameter
				pstmt.setInt(1, getDate());
				pstmt.setInt(2, getPlaceID());

				updateSQL = "UPDATE tenancycontract SET startdate = ?, duration = ?, addcost = ?, buyer = ? WHERE contractno = ?";
				pstmt = con.prepareStatement(updateSQL);
				
				// Setze Anfrage Parameter
				pstmt.setInt(1, getStartDate());
				pstmt.setInt(2, getDuration());
				pstmt.setInt(3, getAddCost());
				pstmt.setInt(4, getBuyer());

				pstmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
