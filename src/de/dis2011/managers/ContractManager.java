package de.dis2011.managers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.dis2011.data.*;

public class ContractManager {
	
private Contract[] ContractsArray;
private int contractCount;
	
	public ContractManager() {
		ContractsArray = new Contract[50];
	}
	
	public Contract getContract(int id) {
		return ContractsArray[id];
	}
	
	public int getContractCount(){
		return contractCount;
	}
	
	public int addPurchaseContract(int nooinstallments, int interestrate, int buyer, int date, int placeid) {
		PurchaseContract purchase = new PurchaseContract();
		purchase.setNoOInstallments(nooinstallments);
		purchase.setInterestRate(interestrate);
		purchase.setBuyer(buyer);
		purchase.setDate(date);
		purchase.setPlaceID(placeid);
		purchase.save();
		ContractsArray[purchase.getContractNo()] = purchase;
		contractCount++;
		return purchase.getContractNo();
	}
	
	public int addTenancyContract(int startdate, int duration, int addcost, int buyer, int date, int placeid) {
		TenancyContract tenancy = new TenancyContract();
		tenancy.setStartDate(startdate);
		tenancy.setDuration(duration);
		tenancy.setAddCost(addcost);
		tenancy.setBuyer(buyer);
		tenancy.setDate(date);
		tenancy.setPlaceID(placeid);
		tenancy.save();
		ContractsArray[tenancy.getContractNo()] = tenancy;
		contractCount++;
		return tenancy.getContractNo();
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
			
			rs.next();
			count = rs.getInt(1);
			contractCount = count;
			
			for(int x = 1; x < count + 1; x++) {
				if(!checkForTenancy(x)) {
					PurchaseContract purchase = PurchaseContract.load(x);
					ContractsArray[x] = purchase;
				}
				else{
					TenancyContract tenancy = TenancyContract.load(x);
					ContractsArray[x] = tenancy;
				}
				
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Boolean checkForTenancy(int id) {
		try{
			// Hole Verbindung
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "SELECT COUNT(*) FROM tenancycontract WHERE tcontractid = ? ";
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
	
	public String checkContract(int id) {
		if(ContractsArray[id] instanceof PurchaseContract) {
			return "PurchaseContract";
		}
		else if(ContractsArray[id] instanceof TenancyContract) {
			return "TenancyContract";
		}
		return "No Contract";
	}

}
