package de.dis2011.data;

import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

public class TenancyContract extends Contract{
	
	private String StartDate;
	private int Duration;
	private int AddCost;
	private int Buyer;
	
	public TenancyContract(){
		
	}
	
	//Getter and Setter
	public String getStartDate() {
		return StartDate;
	}
	
	public void setStartDate(String startdate) {
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
			String selectSQL = "SELECT * FROM tenancycontract WHERE tcontractid = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, id);
			
			final SimpleDateFormat out = new SimpleDateFormat("yyyy-MM-dd");

			// Führe Anfrage aus
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				TenancyContract ts = new TenancyContract();
				ts.setStartDate(rs.getString("startdate"));
				ts.setDuration(rs.getInt("duration"));
				ts.setAddCost(rs.getInt("addcosts"));
				ts.setBuyer(rs.getInt("tbuyer"));

				rs.close();
				pstmt.close();
				
				selectSQL = "SELECT * FROM contract WHERE cid = ?";
				pstmt = con.prepareStatement(selectSQL);
				pstmt.setInt(1, id);

				// Führe Anfrage aus
				rs = pstmt.executeQuery();
				rs.next();
				
				ts.setContractNo(id);
				ts.setDate(rs.getString("date"));
				ts.setPlaceID(rs.getInt("placeid"));
				
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
			if (getContractNo() == -1) {
				// Achtung, hier wird noch ein Parameter mitgegeben,
				// damit spC$ter generierte IDs zurC<ckgeliefert werden!
				String insertSQL = "INSERT INTO contract(date, placeid) VALUES (?, ?)";

				PreparedStatement pstmt = con.prepareStatement(insertSQL,
						Statement.RETURN_GENERATED_KEYS);

				// Setze Anfrageparameter und fC<hre Anfrage aus
				pstmt.setString(1, getDate());
				pstmt.setInt(2, getPlaceID());
				
				pstmt.executeUpdate();

				// Hole die Id des engefC<gten Datensatzes
				ResultSet rs = pstmt.getGeneratedKeys();
				if (rs.next()) {
					setContractNo(rs.getInt(1));
				}
				
				insertSQL = "INSERT INTO tenancycontract(startdate, duration, addcosts, tbuyer, tcontractid) VALUES (?, ?, ?, ?, ?)";

				pstmt = con.prepareStatement(insertSQL,
						Statement.RETURN_GENERATED_KEYS);

				// Setze Anfrageparameter und fC<hre Anfrage aus
				pstmt.setString(1, getStartDate());
				pstmt.setInt(2, getDuration());
				pstmt.setInt(3, getAddCost());
				pstmt.setInt(4, getBuyer());
				pstmt.setInt(5, getContractNo());
				
				pstmt.executeUpdate();

				rs.close();
				pstmt.close();
			} else {
				// Falls schon eine ID vorhanden ist, mache ein Update...
				String updateSQL = "UPDATE contract SET date = ?, placeid = ? WHERE cid = ?";
				PreparedStatement pstmt = con.prepareStatement(updateSQL);

				// Setze Anfrage Parameter
				pstmt.setString(1, getDate());
				pstmt.setInt(2, getPlaceID());

				updateSQL = "UPDATE tenancycontract SET startdate = ?, duration = ?, addcosts = ?, tbuyer = ? WHERE tcontractid = ?";
				pstmt = con.prepareStatement(updateSQL);
				
				// Setze Anfrage Parameter
				pstmt.setString(1, getStartDate());
				pstmt.setInt(2, getDuration());
				pstmt.setInt(3, getAddCost());
				pstmt.setInt(4, getBuyer());
				
				pstmt.executeUpdate();

				pstmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
