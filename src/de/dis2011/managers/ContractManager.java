package de.dis2011.managers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.dis2011.data.*;

public class ContractManager {
	
private Contract[] ContractsArray;
	
	public ContractManager() {
		ContractsArray = new Contract[50];
	}
	
	public Contract getContract(int id) {
		return ContractsArray[id];
	}
	
	public void addPurchaseContract(int nooinstallments, int interestrate, int buyer, int date, int placeid) {
		PurchaseContract purchase = new PurchaseContract();
		purchase.setNoOInstallments(nooinstallments);
		purchase.setInterestRate(interestrate);
		purchase.setBuyer(buyer);
		purchase.setDate(date);
		purchase.setPlaceID(placeid);
		purchase.save();
		ContractsArray[purchase.getContractNo()] = purchase;
	}
	
	public void addTenancyContract(int startdate, int duration, int addcost, int buyer, int date, int placeid) {
		TenancyContract tenancy = new TenancyContract();
		tenancy.setStartDate(startdate);
		tenancy.setDuration(duration);
		tenancy.setAddCost(addcost);
		tenancy.setBuyer(buyer);
		tenancy.setDate(date);
		tenancy.setPlaceID(placeid);
		tenancy.save();
		ContractsArray[tenancy.getContractNo()] = tenancy;
	}
	
	public void loadContracts() {
		int count;
		try{
			// Hole Verbindung
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "SELECT COUNT(*) AS NumberOfContracts FROM contract";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);

			// Führe Anfrage aus
			ResultSet rs = pstmt.executeQuery();
			
			count = rs.getInt("NumberOfContracts");
			
			for(int x = 1; x < count + 1; x++) {
				TenancyContract tenancy = TenancyContract.load(x);
				if(tenancy == null) {
					PurchaseContract purchase = PurchaseContract.load(x);
					ContractsArray[x] = purchase;
				}
				else{
					ContractsArray[x] = tenancy;
				}
				
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public String checkContract(int id) {
		if(ContractsArray[id] instanceof PurchaseContract) {
			return "PurchaseContract";
		}
		else if(ContractsArray[id] instanceof TenancyContract) {
			return "TenancyContract";
		}
		return null;
	}

}
