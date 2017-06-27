package legocardsdb;

import java.sql.*;

public class LegoDB {
	private final String url = "jdbc:postgresql://legocards.c1kv8psvikoo.eu-west-2.rds.amazonaws.com/legocardswaps";
	private final String username = "domtrott";
	private final String pass = "castle72";
	private Connection conn = null;
	
	public String[] cards = {"Lily", "Sam", "Janitor", "Rapper", "Carpenter",
			"Wacky Witch", "Create! Windmill", "Sleepyhead", "Cave Woman", "Alien Avenger",
			"Boxer", "Ocean King", "Create! Routemaster", "Baseball Player", "Create! Moose",
			"Holiday Elf", "Kimono Girl", "Fortune Teller", "Create! Ibex", "Snowboarder",
			"Tennis Player", "Create! Tree", "Plumber", "Surfer Girl", "Create! Lighthouse",
			"Tiger Woman", "Fitness Instructor", "Butcher", "Yeti", "Flamenco Dancer",
			"Create! Letters", "Explorer", "Create! Chameleon", "Alien Villainess", "Ringmaster",
			"Gnome", "Create! Sandcastle", "Spooky Girl", "Hot Dog Man", "Lizard Man",
			"Race Car Driver", "Weightlifter", "Create! Off-Roader", "Chicken Suit Guy", "Waiter",
			"Mountain Climber", "Skier", "Disco Diva", "Create! Monkey", "Welder",
			"Square Foot", "Banana Guy", "Mermaid", "Sea Captain", "Create! Dragon",
			"Diner Waitress", "Create! Ghost House", "Vampire Bat", "Decorator", "Pizza Delivery Man",
			"Create! Skyline", "Unicorn Girl", "Frightening Knight", "Businessman", "Jewel Thief",
			"Sad Clown", "Create! Panda", "Hollywood Starlet", "Gangster", "Create! Water Lily Flower",
			"Nurse", "Create! Asian House", "Create! Hot Air Balloon", "Rockstar", "Bagpiper",
			"Shark Suit Guy", "Evil Dwarf", "Santa", "Create! Koala", "Artist",
			"Mummy", "Heroic Knight", "Create! Island", "Crazy Scientist", "Wolf Guy",
			"Bumblebee Girl", "Create! Submarine", "Skeleton Guy", "Alien Trooper", "Grandma",
			"Create! Kangaroo", "Queen", "Cowgirl", "Create! Robot", "Deep Sea Diver",
			"Create! Snowmobile", "Create! Polar Bear", "Skater Girl", "Trendsetter", "Paintball Player",
			"Small Clown", "Fisherman", "Create! Toucan", "Clumsy Guy", "Gingerbread Man",
			"Create! Hotel", "Create! Plane", "Hippie", "Traffic Cop", "Grandpa",
			"Create! Dinosaur", " Fairytale Princess", "Create! Parrot", "Spider Lady", "Mechanic",
			"Gargoyle", "Saxaphone Player", "Lady Robot", "Farmer", "Mime",
			"Create! Hot Dog Stand", "Medusa", "Prospector", "Leprechaun", "DJ",
			"Create! Statue Of Liberty", "Create! Spaceship", "Lady Cyclops", "Thespian", "Snake Charmer",
			"Piggy Guy", "Bride", "Create! Bear", "Plant Monster", "Create! Motorboat",
			"Pirate Captain", "Hula Dancer", "Create! Ancient Ship", "Lily", "Sam"};
	
	public LegoDB(){
		
	}

    public void connectToDB() {

        try {
            //Load the PostgreSQL JDBC driver
            Class.forName("org.postgresql.Driver");

        } catch (ClassNotFoundException ex) {
            System.out.println("Driver not found");
            System.exit(1);
        }

        System.out.println("PostgreSQL driver registered.");

        try {
            conn = DriverManager.getConnection(url, username, pass);
        } catch (SQLException ex) {
            System.out.println("Ooops, couldn't get a connection");
            System.out.println("Check that <username> & <password> are right");
            System.exit(1);
        }

        if (conn != null) {
            System.out.println("Database accessed!");
        } else {
            System.out.println("Failed to make connection");
            System.exit(1);
        }
    }
    
    public void disconnect(){
    	try {
            conn.close();
            System.out.println("Connection Closed");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void createDB(){
    	try{
        	PreparedStatement deleteTables = conn.prepareStatement(
        			"DROP SCHEMA public CASCADE;"
        			+ "CREATE SCHEMA public;");
        	deleteTables.execute();
        	System.out.println("Database Wiped");
        } catch (SQLException sqlE){
        	System.out.println("Deletion of tables failed");
        	sqlE.printStackTrace();
        	System.exit(1);
        }
    	
    	try{
        	PreparedStatement createTables = conn.prepareStatement(
        			"CREATE TABLE Cards("
        			+ "cid serial PRIMARY KEY,"
        			+ "name varchar(255) NOT NULL,"
        			+ "quantity smallint DEFAULT 0);");
        	createTables.execute();
        	System.out.println("Table created");
        } catch (SQLException sqlE){
        	System.out.println("Creation of table failed");
        	sqlE.printStackTrace();
        	System.exit(1);
        }
    	
    	for(int i = 0; i < cards.length; i++){
    		try {
				PreparedStatement insertCard = conn.prepareStatement(
						"INSERT INTO Cards (name) VALUES ('" + cards[i] + "');");
				insertCard.execute();
			} catch (SQLException e) {
				System.out.println("Insertion of card " + (i+1) + " failed");
				e.printStackTrace();
			}
    	}
    }
    
    public void getQuantities(){
    	int count = 1;
		try {
        	PreparedStatement quantityQuery = conn.prepareStatement(
        		"SELECT quantity FROM Cards ORDER By cid ASC;");
        
        	ResultSet rs = quantityQuery.executeQuery();
        
        	while (rs.next()) {
        		String quantity = rs.getString("quantity");
        		System.out.println(count + ": " + quantity);
        		count++;
        	}
        } catch (SQLException sqlE)
        { System.out.println("SQL code is broken");
        
        }
    }
    
    public void getStatus(int cid){
    	try {
        	PreparedStatement quantityQuery = conn.prepareStatement(
        		"SELECT name, quantity FROM Cards WHERE cid = " + cid + ";");
        
        	ResultSet rs = quantityQuery.executeQuery();
        
        	while (rs.next()) {
        		String name = rs.getString("name");
        		String quantity = rs.getString("quantity");
        		System.out.println("No: " + cid);
        		System.out.println("Name: " + name);
        		System.out.println("Quantity: " + quantity);
        	}
        } catch (SQLException sqlE)
        { System.out.println("SQL code is broken");
        
        }
    }
    
    public void addCard(int cid){
    	try {
			PreparedStatement addCard = conn.prepareStatement(
					"UPDATE Cards SET quantity = quantity+1 WHERE cid = " + cid);
			addCard.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Failed to add card");
			e.printStackTrace();
		}
    }
    
    public void removeCard(int cid){
    	try {
			PreparedStatement addCard = conn.prepareStatement(
					"UPDATE Cards SET quantity = quantity-1 WHERE cid = " + cid);
			addCard.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Failed to remove card");
			e.printStackTrace();
		}
    }
}
