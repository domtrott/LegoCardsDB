package legocardsdb;

import java.util.Observable;

public class LegoModel extends Observable{
	private LegoDB db;
	
	public LegoModel(LegoDB db){
		super();
		this.db = db;
	}
	
	public void connectToDB(){
		db.connectToDB();
	}
	
	public void disconnect(){
		db.disconnect();
	}
	
	public void createDB(){
		db.createDB();
	}
	
	public void getQuantities(){
		db.getQuantities();
	}
	
	public String getStatus(int cid){
		return db.getStatus(cid);
	}
	
	public String getStatus(String name){
		return db.getStatus(name);
	}
	
	public int getID(String name){
		return db.getCID(name);
	}
	
	public boolean checkName(String name){
		return db.checkName(name);
	}
	
	public String missingCards(){
		return db.missingCards();
	}
	
	public void addCard(int cid){
		db.addCard(cid);
	}
	
	public void removeCard(int cid){
		db.removeCard(cid);
	}
	
	public String[] getPeople(){
		return db.getPeople();
	}
	
	public String getPeopleView(){
		return db.getPeopleView();
	}
	
	public int getPID(String name){
		return db.getPID(name);
	}
	
	public void addOrder(String name, String card){
		db.addOrder(name, card);
	}
	
	public void removeOrder(String name, String card){
		db.removeOrder(name, card);
	}
	
	public String[] getCards(){
		return db.getCards();
	}
	
	public void addPerson(String name){
		db.addPerson(name);
	}
	
	public String getOrders(String name){
		return db.getOrders(name);
	}
	
	public String[] checkOrders(int cid){
		return db.checkOrders(cid);
	}
}
