package legocardsdb;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LegoComp extends JPanel implements ActionListener{
	
	private final int CARDS = 0, PEOPLE = 1, ORDERS = 2;
	private int mode = CARDS;
	
	private JPanel cardsTop, cardsBottom;
	private JPanel peopleTop, peopleBottom;
	private JPanel ordersTop, ordersBottom;
	private JPanel centre;
	
	private LegoModel model;
	private JTextField searchField, personField, searchField2;
	private JComboBox<String> selectPerson;
	private JLabel view;
	private int currentID;
	private String status = "";
	
	private Color red = new Color(184, 13, 0);
	private Color orange = new Color(235, 107, 13);
	private Color blue = new Color(18, 183, 227);
	private Color green = new Color(12, 176, 10);
	private Color yellow = new Color(237, 218, 0);
	
	public LegoComp(LegoDB db){
		super();
		model = new LegoModel(db);
		
		cardsTop = new JPanel();
		cardsBottom = new JPanel();
		
		peopleTop = new JPanel();
		peopleBottom = new JPanel();
		
		ordersTop = new JPanel();
		ordersBottom = new JPanel();
		
		centre = new JPanel();
		
		searchField = new JTextField("Search for a card by name or number");
		searchField.setColumns(20);
		searchField.setFont(new Font("Assistant", Font.BOLD, 18));
		
		JButton searchButton = new JButton("Search");
		searchButton.setActionCommand("search");
		searchButton.addActionListener(this);
		setButtonLook(searchButton, orange);
		
		view = new JLabel();
		view.setForeground(Color.BLACK);
		view.setBackground(yellow);
		view.setFont(new Font("Assistant", Font.BOLD, 18));
		
		JButton addCardButton = new JButton("Add");
		addCardButton.setActionCommand("add");
		addCardButton.addActionListener(this);
		setButtonLook(addCardButton, blue);
		
		JButton removeCardButton = new JButton("Remove");
		removeCardButton.setActionCommand("remove");
		removeCardButton.addActionListener(this);
		setButtonLook(removeCardButton, red);
		
		JButton missingButton = new JButton("Missing Cards");
		missingButton.setActionCommand("missing");
		missingButton.addActionListener(this);
		setButtonLook(missingButton, green);
		
		personField = new JTextField("Name of Person");
		personField.setColumns(20);
		personField.setFont(new Font("Assistant", Font.BOLD, 18));
		
		JButton addPersonButton = new JButton("Add");
		addPersonButton.setActionCommand("addPerson");
		addPersonButton.addActionListener(this);
		setButtonLook(addPersonButton, blue);
		
		selectPerson = new JComboBox<String>(model.getPeople());
		selectPerson.setActionCommand("person");
		selectPerson.addActionListener(this);
		selectPerson.setFont(new Font("Assistant", Font.BOLD, 18));
		
		searchField2 = new JTextField("Card");
		searchField2.setColumns(10);
		searchField2.setFont(new Font("Assistant", Font.BOLD, 18));
		
		JButton addOrderButton = new JButton("Add");
		addOrderButton.setActionCommand("addOrder");
		addOrderButton.addActionListener(this);
		setButtonLook(addOrderButton, blue);
		
		JButton removeOrderButton = new JButton("Remove");
		removeOrderButton.setActionCommand("removeOrder");
		removeOrderButton.addActionListener(this);
		setButtonLook(removeOrderButton, red);
		
		JButton ordersButtonLeft = new JButton("< Orders");
		ordersButtonLeft.setActionCommand("orders");
		ordersButtonLeft.addActionListener(this);
		setButtonLook(ordersButtonLeft, orange);
		
		JButton ordersButtonRight = new JButton("Orders >");
		ordersButtonRight.setActionCommand("orders");
		ordersButtonRight.addActionListener(this);
		setButtonLook(ordersButtonRight, orange);
		
		JButton peopleButtonLeft = new JButton("< People");
		peopleButtonLeft.setActionCommand("people");
		peopleButtonLeft.addActionListener(this);
		setButtonLook(peopleButtonLeft, orange);
		
		JButton peopleButtonRight = new JButton("People >");
		peopleButtonRight.setActionCommand("people");
		peopleButtonRight.addActionListener(this);
		setButtonLook(peopleButtonRight, orange);
		
		JButton cardsButtonLeft = new JButton("< Cards");
		cardsButtonLeft.setActionCommand("cards");
		cardsButtonLeft.addActionListener(this);
		setButtonLook(cardsButtonLeft, orange);
		
		JButton cardsButtonRight = new JButton("Cards >");
		cardsButtonRight.setActionCommand("cards");
		cardsButtonRight.addActionListener(this);
		setButtonLook(cardsButtonRight, orange);
		
		cardsTop.add(searchField);
		cardsTop.add(searchButton);
		cardsBottom.add(peopleButtonLeft);
		cardsBottom.add(addCardButton);
		cardsBottom.add(removeCardButton);
		cardsBottom.add(missingButton);
		cardsBottom.add(ordersButtonRight);
		
		peopleTop.add(personField);
		peopleTop.add(addPersonButton);
		peopleBottom.add(ordersButtonLeft);
		peopleBottom.add(cardsButtonRight);
		
		ordersTop.add(selectPerson);
		ordersTop.add(searchField2);
		ordersTop.add(addOrderButton);
		ordersTop.add(removeOrderButton);
		ordersBottom.add(cardsButtonLeft);
		ordersBottom.add(peopleButtonRight);
		
		centre.add(view);
		
		setLayout(new BorderLayout());
		
		cardsTop.setBackground(yellow);
		cardsBottom.setBackground(yellow);
		peopleTop.setBackground(yellow);
		peopleBottom.setBackground(yellow);
		ordersTop.setBackground(yellow);
		ordersBottom.setBackground(yellow);
		centre.setBackground(yellow);
		
		add(cardsTop, BorderLayout.NORTH);
		add(cardsBottom, BorderLayout.SOUTH);
		add(centre, BorderLayout.CENTER);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if("search".equals(e.getActionCommand())){
			String searchText = searchField.getText();
			if(isNumeric(searchText) && Integer.parseInt(searchText) <= 140){
				currentID = Integer.parseInt(searchText);
				status = model.getStatus(currentID);
				view.setText(status);
			}
			else if(model.checkName(searchText)){
				status = model.getStatus(searchText);
				currentID = model.getID(searchText);
				view.setText(status);
			}
		}
		else if("add".equals(e.getActionCommand())){
			model.addCard(currentID);
			status = model.getStatus(currentID);
			view.setText(status);
			String[] orders = model.checkOrders(currentID);
			if(orders.length > 0){
				for(String name : orders){
					JOptionPane.showMessageDialog(this, name + " needs this card!");
				}
			}
		}
		else if("remove".equals(e.getActionCommand())){
			model.removeCard(currentID);
			status = model.getStatus(currentID);
			view.setText(status);
		}
		else if("missing".equals(e.getActionCommand())){
			view.setText(model.missingCards());
		}
		else if("addPerson".equals(e.getActionCommand())){
			String name = personField.getText();
			model.addPerson(name);
			selectPerson.addItem(name);
			view.setText(model.getPeopleView());
		}
		else if("addOrder".equals(e.getActionCommand())){
			String person = (String) selectPerson.getSelectedItem();
			String card = searchField2.getText();
			if(isNumeric(card) && Integer.parseInt(card) <= 140){
				model.addOrder(person, model.getCards()[Integer.parseInt(card)-1]);
			}
			else if(model.checkName(card)){
				model.addOrder(person, card);
			}
			else{
				System.out.println("Order not added");
			}
			String name = (String) selectPerson.getSelectedItem();
			view.setText(model.getOrders(name));
		}
		else if("removeOrder".equals(e.getActionCommand())){
			String person = (String) selectPerson.getSelectedItem();
			String card = searchField2.getText();
			if(isNumeric(card) && Integer.parseInt(card) <= 140){
				model.removeOrder(person, model.getCards()[Integer.parseInt(card)-1]);
			}
			else if(model.checkName(card)){
				model.removeOrder(person, card);
			}
			else{
				System.out.println("Order not removed");
			}
			String name = (String) selectPerson.getSelectedItem();
			view.setText(model.getOrders(name));
		}
		else if("person".equals(e.getActionCommand())){
			String name = (String) selectPerson.getSelectedItem();
			view.setText(model.getOrders(name));
		}
		else if("orders".equals(e.getActionCommand())){
			String name = (String) selectPerson.getSelectedItem();
			view.setText(model.getOrders(name));
			mode = ORDERS;
			removeAll();
			add(ordersTop, BorderLayout.NORTH);
			add(ordersBottom, BorderLayout.SOUTH);
			add(centre, BorderLayout.CENTER);
			repaint();
		}
		else if("people".equals(e.getActionCommand())){
			view.setText(model.getPeopleView());
			mode = PEOPLE;
			removeAll();
			add(peopleTop, BorderLayout.NORTH);
			add(peopleBottom, BorderLayout.SOUTH);
			add(centre, BorderLayout.CENTER);
			repaint();
		}
		else if("cards".equals(e.getActionCommand())){
			view.setText("");
			mode = CARDS;
			removeAll();
			add(cardsTop, BorderLayout.NORTH);
			add(cardsBottom, BorderLayout.SOUTH);
			add(centre, BorderLayout.CENTER);
			repaint();
		}
	}
	
	public static boolean isNumeric(String str)
	{
	    for (char c : str.toCharArray())
	    {
	        if (!Character.isDigit(c)) return false;
	    }
	    return true;
	}
	
	private void setButtonLook(JButton b, Color c){
		b.setOpaque(true);
		b.setBackground(c);
		b.setBorderPainted(false);
		b.setForeground(Color.WHITE);
		b.setFont(new Font("Assistant", Font.BOLD, 18));
	}
}
