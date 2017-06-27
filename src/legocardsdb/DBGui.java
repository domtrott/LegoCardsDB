package legocardsdb;

public class DBGui {

	public static void main(String[] args) {
		LegoDB db = new LegoDB();
		db.connectToDB();
//		db.createDB();
		db.addCard(1);
		db.getStatus(1);
		db.removeCard(1);
		db.getStatus(1);
		db.disconnect();
	}

}
