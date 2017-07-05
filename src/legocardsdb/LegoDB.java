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
		connectToDB();
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
        			+ "quantity smallint DEFAULT 0);"
        			+ "CREATE TABLE People("
        			+ "pid serial PRIMARY KEY,"
        			+ "name varchar(255) NOT NULL);"
        			+ "CREATE TABLE Orders("
        			+ "cid serial references Cards(cid),"
        			+ "pid serial references People(pid));");
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
        } catch (SQLException sqlE){
        	System.out.println("SQL code is broken");
        	sqlE.printStackTrace();
        }
    }
    
    public String getStatus(int cid){
    	try {
        	PreparedStatement quantityQuery = conn.prepareStatement(
        		"SELECT name, quantity FROM Cards WHERE cid = " + cid + ";");
        
        	ResultSet rs = quantityQuery.executeQuery();
        	String result = "<html>";
        	
        	while (rs.next()) {
        		String name = rs.getString("name");
        		String quantity = rs.getString("quantity");
        		result += "No: " + cid + "<br>";
        		result += "Name: " + name + "<br>";
        		result += "Quantity: " + quantity + "</html>";
        		return result;
        	}
        } catch (SQLException sqlE){
        	System.out.println("Can't find card " + cid);
			sqlE.printStackTrace();
        }
    	return "Can't find card " + cid;
    }
    
    public String getStatus(String name){
    	for(int i = 0; i < cards.length; i++){
    		if(cards[i].equals(name)){
    			return getStatus(i+1);
    		}
    	}
    	return "Card not found";
    }
    
    public int getCID(String name){
    	for(int i = 0; i < cards.length; i++){
    		if(cards[i].equals(name)){
    			return i+1;
    		}
    	}
    	return -1;
    }
    
    public boolean checkName(String name){
    	for(int i = 0; i < cards.length; i++){
    		if(cards[i].equals(name)){
    			return true;
    		}
    	}
    	return false;
    }
    
    public String missingCards(){
    	try {
        	PreparedStatement quantityQuery = conn.prepareStatement(
        		"SELECT name, cid FROM Cards WHERE quantity = 0 ORDER BY cid ASC;");
        	ResultSet rs = quantityQuery.executeQuery();
        	String result = "<html>";
        	
        	while (rs.next()) {
        		String name = rs.getString("name");
        		String cid = rs.getString("cid");
        		result += cid + ": " + name + "<br>";
        	}
        	result += "</html>";
        	return result;
        	
        } catch (SQLException sqlE){
        	System.out.println("Something went bad");
			sqlE.printStackTrace();
        }
    	return "";
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
    
    public String[] getPeople(){
    	try {
    		PreparedStatement quantityQuery = conn.prepareStatement(
    			"SELECT COUNT(name) AS total FROM People;");
    		
    		ResultSet rs1 = quantityQuery.executeQuery();
    		rs1.next();
    		int total = rs1.getInt("total");
    		String[] people = new String[total];
    		
        	PreparedStatement namesQuery = conn.prepareStatement(
        		"SELECT name FROM People;");
        
        	ResultSet rs2 = namesQuery.executeQuery();
        	
        	int counter = 0;
        	while (rs2.next()) {
        		String name = rs2.getString("name");
        		people[counter] = name;
        		counter++;
        	}
        	
        	return people;
        			
        } catch (SQLException sqlE){
        	System.out.println("It's all gone wrong");
			sqlE.printStackTrace();
        }
    	return new String[0];
    }
    
    public String getPeopleView(){
    	try {
    		PreparedStatement peopleQuery = conn.prepareStatement(
        		"SELECT pid, name FROM People;");
        
        	ResultSet rs = peopleQuery.executeQuery();
        	
        	String result = "<html>";
        	
        	while (rs.next()) {
        		String name = rs.getString("name");
        		String pid = rs.getString("pid");
        		result += pid + ": " + name + "<br>";
        	}
        	result += "</html>";
        	return result;
        			
        } catch (SQLException sqlE){
        	System.out.println("It's all gone wrong");
			sqlE.printStackTrace();
        }
    	return "";
    }
    
    public int getPID(String name){
    	try {
        	PreparedStatement nameQuery = conn.prepareStatement(
        		"SELECT pid FROM People WHERE name = '" + name + "';");
        
        	ResultSet rs = nameQuery.executeQuery();
        	rs.next();
        	int pid = rs.getInt("pid");
        	
        	return pid;
        			
        } catch (SQLException sqlE){
        	System.out.println("Could not find " + name);
			sqlE.printStackTrace();
        }
    	return -1;
    }
    
    public void addOrder(String name, String card){
    	try {
    		int pid = getPID(name);
    		int cid = getCID(card);
    		
        	PreparedStatement insertQuery = conn.prepareStatement(
        		"INSERT INTO Orders (cid, pid) VALUES ('" + cid + "', '" + pid + "');");
        	insertQuery.execute();
        	
        } catch (SQLException sqlE){
        	System.out.println("Could not insert " + name + " and " + card);
			sqlE.printStackTrace();
        }
    }
    
    public void removeOrder(String name, String card){
    	try {
    		int pid = getPID(name);
    		int cid = getCID(card);
    		
        	PreparedStatement removeQuery = conn.prepareStatement(
        		"DELETE FROM Orders WHERE cid = " + cid + " AND pid = " + pid + ";");
        	removeQuery.execute();
        	
        } catch (SQLException sqlE){
        	System.out.println("Could not remove " + name + " and " + card);
			sqlE.printStackTrace();
        }
    }
    
    public String[] getCards(){
    	return cards;
    }
    
    public void addPerson(String name){
    	try {
			PreparedStatement insertPerson = conn.prepareStatement(
					"INSERT INTO People (name) VALUES ('" + name + "');");
			insertPerson.execute();
		} catch (SQLException e) {
			System.out.println("Insertion of person " + name + " failed");
			e.printStackTrace();
		}
    }
    
    public String getOrders(String name){
    	try {
    		PreparedStatement orderQuery = conn.prepareStatement(
        		"SELECT Cards.cid, Cards.name, quantity FROM Cards "
        		+ "INNER JOIN Orders ON Cards.cid = Orders.cid "
        		+ "INNER JOIN People ON Orders.pid = People.pid "
        		+ "WHERE People.name = '" + name + "';");
        
        	ResultSet rs = orderQuery.executeQuery();
        	
        	String result = "<html>";
        	
        	while (rs.next()) {
        		String cid = rs.getString("cid");
        		String cname = rs.getString("name");
        		String quantity = rs.getString("quantity");
        		result += cid + ": " + cname + " [" + quantity + "]<br>";
        	}
        	result += "</html>";
        	return result;
        			
        } catch (SQLException sqlE){
        	System.out.println("It's all gone wrong");
			sqlE.printStackTrace();
        }
    	return "";
    }
    
    public String[] checkOrders(int cid){
    	try {
    		PreparedStatement amountQuery = conn.prepareStatement(
    			"SELECT count(pid) FROM Orders WHERE cid =" + cid + ";");
    		
    		ResultSet rs1 = amountQuery.executeQuery();
    		rs1.next();
    		int count = rs1.getInt("count");
    		
    		if(count == 0){
    			return new String[0];
    		}
    		else{
	    		PreparedStatement orderQuery = conn.prepareStatement(
	        		"SELECT name FROM People "
	        		+ "INNER JOIN Orders ON People.pid = Orders.pid "
	        		+ "WHERE cid =" + cid + ";");
	        
	        	ResultSet rs2 = orderQuery.executeQuery();
	        	String[] people = new String[count];
	        	int i = 0;
	        	
	        	while(rs2.next()){
	        		people[i] = rs2.getString("name");
	        		i++;
	        	}
	        	return people;
    		}	
        } catch (SQLException sqlE){
        	System.out.println("It's all gone wrong");
			sqlE.printStackTrace();
        }
    	return new String[0];
    }
}
