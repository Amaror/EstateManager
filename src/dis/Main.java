package dis;

import dis.service.*;

/**
 * Hauptklasse
 */
public class Main {
	
	/**
	 * Startet die Anwendung
	 */
	public static void main(String[] args) {
		System.out.println("Transactions Small:");
		AprioriService.getInstance().apriori("./src/dis/transactions.txt");
		//System.out.println("Transactions Large:");
		//AprioriService.getInstance().apriori("./src/dis/transactionslarge.txt");
	}
	
	/**
	 * Zeigt das Hauptmenü
	 */
	
}
