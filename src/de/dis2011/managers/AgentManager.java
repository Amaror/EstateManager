package de.dis2011.managers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.dis2011.data.DB2ConnectionManager;
import de.dis2011.data.Agent;

public class AgentManager {
	
private Agent[] AgentsArray;
private int NumberOfAgents;
	
	public AgentManager() {
		AgentsArray = new Agent[50];
	}
	
	public Agent getAgent(int id) {
		return AgentsArray[id];
	}
	
	public void addAgent(String name, String adress, String login, String password) {
		Agent Agent = new Agent();
		Agent.setName(name);
		Agent.setAddress(adress);
		Agent.setLogin(login);
		Agent.setPassword(password);
		Agent.save();
		AgentsArray[Agent.getId()-1] = Agent;
		NumberOfAgents++;
	}
	
	public void loadContracts() {
		int count;
		try{
			// Hole Verbindung
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "SELECT COUNT(*) AS NumberOfAgents FROM estateagent";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);

			// Führe Anfrage aus
			ResultSet rs = pstmt.executeQuery();
			
			count = rs.getInt("NumberOfAgents");
			NumberOfAgents = count;
			
			for(int x = 0; x < count; x++) {
				Agent agent = Agent.load(x+1);
				AgentsArray[x] = agent;
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean checkPassword(String login, String password) {
		for(int x = 0; x < NumberOfAgents; x++) {
			if(AgentsArray[x].getLogin().equals(login)){
				if(AgentsArray[x].getPassword().equals(password)) {
					return true;
				}
				return false;
			}
		}
		return false;
	}

}
