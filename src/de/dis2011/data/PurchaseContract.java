package de.dis2011.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PurchaseContract extends Contract{
	
	private int NoOInstallments;
	private int InterestRate;
	private int Buyer;
	
	public PurchaseContract(){
		
	}
	
	//Getter and Setter
	public int getNoOInstallments() {
		return NoOInstallments;
	}
	
	public void setNoOInstallments(int noinstallments) {
		NoOInstallments = noinstallments;
	}
	
	public int getInterestRate() {
		return InterestRate;
	}
	
	public void setInterestRate(int interestrate) {
		InterestRate = interestrate;
	}
	
	public int getBuyer() {
		return Buyer;
	}
	
	public void setBuyer(int buyer) {
		Buyer = buyer;
	}
	
	public static PurchaseContract load(int id) {
		try {
			// Hole Verbindung
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "SELECT * FROM purchasecontract WHERE pcontractid = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, id);

			// Führe Anfrage aus
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				PurchaseContract ts = new PurchaseContract();
				ts.setNoOInstallments(rs.getInt("nooinstallments"));
				ts.setInterestRate(rs.getInt("interestrate"));
				ts.setBuyer(rs.getInt("pbuyer"));

				rs.close();
				pstmt.close();
				
				selectSQL = "SELECT * FROM contract WHERE cid = ?";
				pstmt = con.prepareStatement(selectSQL);
				pstmt.setInt(1, id);

				// Führe Anfrage aus
				rs = pstmt.executeQuery();
				rs.next();
				
				ts.setContractNo(id);
				ts.setDate(rs.getInt("date"));
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
				pstmt.setInt(1, getDate());
				pstmt.setInt(2, getPlaceID());
				
				pstmt.executeUpdate();

				// Hole die Id des engefC<gten Datensatzes
				ResultSet rs = pstmt.getGeneratedKeys();
				if (rs.next()) {
					setContractNo(rs.getInt(1));
				}
				
				insertSQL = "INSERT INTO purchasecontract(nooinstallments, interestrate, pcontractid, pbuyer) VALUES (?, ?, ?, ?)";

				pstmt = con.prepareStatement(insertSQL,
						Statement.RETURN_GENERATED_KEYS);

				// Setze Anfrageparameter und fC<hre Anfrage aus
				pstmt.setInt(1, getNoOInstallments());
				pstmt.setInt(2, getInterestRate());
				pstmt.setInt(3, getContractNo());
				pstmt.setInt(4, getBuyer());
				
				pstmt.executeUpdate();

				rs.close();
				pstmt.close();
			} else {
				// Falls schon eine ID vorhanden ist, mache ein Update...
				String updateSQL = "UPDATE contract SET date = ?, placeid = ? WHERE cid = ?";
				PreparedStatement pstmt = con.prepareStatement(updateSQL);

				// Setze Anfrage Parameter
				pstmt.setInt(1, getDate());
				pstmt.setInt(2, getPlaceID());

				updateSQL = "UPDATE tenancycontract SET startdate = ?, duration = ?, addcost = ?, pbuyer = ? WHERE pcontractid = ?";
				pstmt = con.prepareStatement(updateSQL);
				
				// Setze Anfrage Parameter
				pstmt.setInt(1, getNoOInstallments());
				pstmt.setInt(2, getInterestRate());
				pstmt.setInt(4, getBuyer());

				pstmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
