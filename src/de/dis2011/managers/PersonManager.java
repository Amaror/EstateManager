package de.dis2011.managers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.dis2011.data.*;

public class PersonManager {
	
private Person[] PersonsArray;
	
	public PersonManager() {
		PersonsArray = new Person[50];
	}
	
	public Person getPerson(int id) {
		return PersonsArray[id];
	}
	
	public void addPerson(String firstname, String name, String adress) {
		Person person = new Person();
		person.setFirstName(firstname);
		person.setName(name);
		person.setAdress(adress);
		person.save();
		PersonsArray[person.getPID()] = person;
	}
	
	public void loadContracts() {
		int count;
		try{
			// Hole Verbindung
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "SELECT COUNT(*) AS NumberOfPersons FROM person";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);

			// Führe Anfrage aus
			ResultSet rs = pstmt.executeQuery();
			
			count = rs.getInt("NumberOfPersons");
			
			for(int x = 1; x < count + 1; x++) {
				Person person = Person.load(x);
				PersonsArray[x] = person;
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
