package de.dis2011.managers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.dis2011.data.DB2ConnectionManager;
import de.dis2011.data.Agent;

public class AgentManager {
	
private Agent[] AgentsArray;
private int NumberOfAgents = 0;
	
	public AgentManager() {
		AgentsArray = new Agent[50];
	}
	
	public Agent getAgent(int id) {
		return AgentsArray[id-1];
	}
	
	public int getAgentCount() {
		return NumberOfAgents;
	}
	
	public int addAgent(String name, String adress, String login, String password) {
		Agent agent = new Agent();
		agent.setName(name);
		agent.setAddress(adress);
		agent.setLogin(login);
		agent.setPassword(password);
		agent.save();
		AgentsArray[agent.getId()-1] = agent;
		NumberOfAgents++;
		return agent.getId();
	}
	
	public void changeAgent(String name, String adress, String login, String password, int id) {
		Agent agent = AgentsArray[id];
		agent.setName(name);
		agent.setAddress(adress);
		agent.setLogin(login);
		agent.setPassword(password);
		agent.save();
	}
	
	public void deleteAgent(int id) {
		try{
			// Hole Verbindung
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "DELETE FROM estateagent WHERE aid=?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL, id);

			// Führe Anfrage aus
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void loadAgents() {
		int count;
		try{
			// Hole Verbindung
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "SELECT COUNT(*) AS NumberOfAgents FROM estateagent";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);

			// Führe Anfrage aus
			ResultSet rs = pstmt.executeQuery();
			
			rs.next();
			count = rs.getInt(1);
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
	
	public int checkPassword(String login, String password) {
		for(int x = 0; x < NumberOfAgents; x++) {
			if(AgentsArray[x].getLogin().equals(login)){
				if(AgentsArray[x].getPassword().equals(password)) {
					return x + 1;
				}
				return -1;
			}
		}
		return -1;
	}

}
