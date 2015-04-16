package de.dis2011.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class Contract {
	
	private int ContractNo;
	private int Date;
	private int PlaceID;
	
	public int getContractNo() {
		return ContractNo;
	}
	
	public void setContractNo(int contractno) {
		ContractNo = contractno;
	}
	
	public int getDate() {
		return Date;
	}
	
	public void setDate(int date) {
		Date = date;
	}
	
	public int getPlaceID() {
		return PlaceID;
	}
	
	public void setPlaceID(int placeid) {
		PlaceID = placeid;
	}
	
	public void save() {
		// Hole Verbindung
		Connection con = DB2ConnectionManager.getInstance().getConnection();

		try {
			// FC<ge neues Element hinzu, wenn das Objekt noch keine ID hat.
			if (getContractNo() == -1) {
				// Achtung, hier wird noch ein Parameter mitgegeben,
				// damit spC$ter generierte IDs zurC<ckgeliefert werden!
				String insertSQL = "INSERT INTO estate(date, placeid) VALUES (?, ?)";

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

				rs.close();
				pstmt.close();
			} else {
				// Falls schon eine ID vorhanden ist, mache ein Update...
				String updateSQL = "UPDATE estate SET date = ?, placeid = ? WHERE contractno = ?";
				PreparedStatement pstmt = con.prepareStatement(updateSQL);

				// Setze Anfrage Parameter
				pstmt.setInt(1, getDate());
				pstmt.setInt(2, getPlaceID());
				
				pstmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
