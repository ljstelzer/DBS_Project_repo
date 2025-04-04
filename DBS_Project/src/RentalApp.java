import java.util.Scanner;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class RentalApp {

	private static String DATABASE = "DBS_Project_DB.db";
	
	public static Connection conn = null;
	
	private static PreparedStatement ps;
	
    // Array of entity names.
    static String[] entities = {"Customer", "Warehouse"};
    
    /**
     * Connects to the database if it exists, creates it if it does not, and returns the connection object.
     * 
     * @param databaseFileName the database file name
     * @return a connection object to the designated database
     */
    public static Connection initializeDB(String databaseFileName) {
    	/**
    	 * The "Connection String" or "Connection URL".
    	 * 
    	 * "jdbc:sqlite:" is the "subprotocol".
    	 * (If this were a SQL Server database it would be "jdbc:sqlserver:".)
    	 */
        String url = "jdbc:sqlite:" + databaseFileName;
        Connection conn = null; // If you create this variable inside the Try block it will be out of scope
        try {
            conn = DriverManager.getConnection(url);
            if (conn != null) {
            	// Provides some positive assurance the connection and/or creation was successful.
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("The connection to the database was successful.");
            } else {
            	// Provides some feedback in case the connection failed but did not throw an exception.
            	System.out.println("Null Connection");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("There was a problem connecting to the database.");
            e.printStackTrace();
        }
        return conn;
    }

    // HashMap to store attributes for corresponding entities.
    static HashMap<String, String[]> entityAttributes = new HashMap<String, String[]>() {{
        put("Customer", new String[]{"User_ID", "First_Name", "Last_Name", "Address", "Phone", "Email", "Start_Date", "Warehouse_ID"});
        put("Warehouse", new String[]{"Warehouse_ID", "City", "Address", "Phone", "Manager_SSN", "Storage_Capacity", "Drone_Capacity"});
    }};
    
    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(System.in);
        
        conn = initializeDB(DATABASE);
        
        
        // Welcome message.
        System.out.println("Welcome to the Rental Management System!");

        // Calls appropriate function for user's choice.
        int action = 0;
        while (action != -1) {
            System.out.println("\n(1) Update records.");
            System.out.println("(2) Manage equipment rentals and returns.");
            System.out.println("(3) Manage equipment delivery and pickup.");
            System.out.println("(4) View useful reports.");
            System.out.print("Enter the letter of the option you would like to select (-1 to quit): "); 
            action = in.nextInt();
            switch (action) {
                case -1:
                    System.out.println("Goodbye!");
                    break;
                case 1:
                    updateRecords(in);
                    break;
                case 2:
                    manageEquipRentals(in);
                    break;
                case 3:
                    manageEquipTransportation(in);
                    break;
                case 4:
                    usefulReports(in);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        in.close();
        try {
			conn.close();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
    }

    public static void updateRecords(Scanner in) {
        
        // Lists entities for user to choose from.
        for (int i = 0; i < entities.length; i++) {
            System.out.println("(" + (i + 1) + ") " + entities[i]);
        }
        System.out.print("Please choose the entity you would like to update: ");
        int entity = in.nextInt();

        // Lists actions for user to choose from.
        System.out.println("(1) Add a new " + entities[entity - 1] + " record.");
        System.out.println("(2) Edit an existing " + entities[entity - 1] + " record.");
        System.out.println("(3) Delete a " + entities[entity - 1] + " record.");
        System.out.println("(4) Search for a " + entities[entity - 1] + " record.");
        System.out.print("Please choose the action you would like to perform: ");
        int action = in.nextInt();

        // Calls appropriate function for user's choice.
        switch (action) {
            case 1:
                addRecord(in, entities[entity - 1]);
                break;
            case 2:
                editRecord(in, entities[entity - 1]);
                break;
            case 3:
                deleteRecord(in, entities[entity - 1]);
                break;
            case 4:
                searchRecord(in, entities[entity - 1]);
                break;
            default:
                System.out.println("Invalid action. Please try again.");

        }

    }

    public static void manageEquipRentals(Scanner in) {
        System.out.println("(1) Rent out equipment.");
        System.out.println("(2) Return equipment.");
        System.out.print("Please choose the action you would like to perform: ");
        int action = in.nextInt();
        switch (action) {
            case 1:
                rentOutEquipment(in);
                break;
            case 2:
                returnEquipment(in);
                break;
            default:
                System.out.println("Invalid action. Please try again.");
        }

    }

    public static void manageEquipTransportation(Scanner in) {
        System.out.println("(1) Schedule equipment delivery.");
        System.out.println("(2) Schedule equipment pickup.");
        System.out.print("Please choose the action you would like to perform: ");
        int action = in.nextInt();
        switch (action) {
            case 1:
                scheduleDelivery(in);
                break;
            case 2:
                schedulePickup(in);
                break;
            default:
                System.out.println("Invalid action. Please try again.");
        }
    }

    public static void usefulReports(Scanner in) {
        
    }
    
    public static void addRecord(Scanner in, String entity) {
       
        // Prompts user for attributes for corresponding entity.
        System.out.println("Adding a new " + entity + " record.");
        switch (entity) {
        	case "Customer":
        		addCustomer(in);
        		break;
        }
        
    }

    public static void editRecord(Scanner in, String entity) {
        
        switch (entity) {
        	case "Customer":
        		editCustomer(in);
        		break;
        }
        
    }

    public static void deleteRecord(Scanner in, String entity) {
        
        // Prompts user for identifying attribute of chosen entity.
        switch (entity) {
        	case "Customer":
        		deleteCustomer(in);
        		break;
        }
        // Add logic to delete the record.
    }

    public static void searchRecord(Scanner in, String entity) {
        
        // Prompts user for identifying attribute of chosen entity.
        System.out.println("Enter the ID of the " + entity + " to search: ");
        int id = in.nextInt();
        System.out.println(entity + " record found successfully.");
        // Add logic to search for the record.
    }


    public static void rentOutEquipment(Scanner in) {
        System.out.print("Enter the the ID of the user you would like to rent to: ");
        int userID = in.nextInt();
        System.out.print("Enter the the Serial Number of the equipment you would like to rent out: ");
        int serialNum = in.nextInt();
        // Add logic to rent out the equipment.
        System.out.println("Equipment rented out successfully.");

    }

    public static void returnEquipment(Scanner in) {
        System.out.print("Enter the Serial Number of the equipment you would like to return: ");
        int serialNum = in.nextInt();
        // Add logic to return the equipment.
        System.out.println("Equipment returned successfully.");
    }

    public static void scheduleDelivery(Scanner in) {
        System.out.print("Enter the Serial Number of the equipment you would like to deliver: ");
        int equipSerialNum = in.nextInt();
        System.out.print("Enter the Serial Number of the drone you would like to complete the delivery: ");
        int droneSerialNum = in.nextInt();
        System.out.print("Enter the date you would like to schedule the delivery: ");
        String date = in.next();
        // Add logic to schedule delivery.
        System.out.println("Delivery scheduled successfully.");
    }

    public static void schedulePickup(Scanner in) {
        System.out.print("Enter the Serial Number of the equipment you would like to pick up: ");
        int equipSerialNum = in.nextInt();
        System.out.print("Enter the Serial Number of the drone you would like to complete the pickup: ");
        int droneSerialNum = in.nextInt();
        System.out.print("Enter the date you would like to schedule the pickup: ");
        String date = in.next();
        // Add logic to schedule pickup.
        System.out.println("Pickup scheduled successfully.");
    }
    
    public static void addCustomer(Scanner in) {
    	try {
    		String sql = "INSERT INTO Customer VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			ps = conn.prepareStatement(sql);
			System.out.print("Enter UserID(integer): ");
			ps.setInt(1, in.nextInt());
			in.nextLine();
			System.out.print("Enter First Name: ");
			ps.setString(2, in.nextLine());
			System.out.print("Enter Last Name: ");
			ps.setString(3, in.nextLine());
			System.out.print("Enter Address: ");
			ps.setString(4, in.nextLine());
			System.out.print("Enter Phone: ");
			ps.setString(5, in.nextLine());
			System.out.print("Enter Email: ");
			ps.setString(6, in.nextLine());
			System.out.print("Enter Start Date: ");
			ps.setString(7, in.nextLine());
			System.out.print("Enter WarehouseID: ");
			ps.setInt(8, in.nextInt());
			in.nextLine();
			System.out.print("Enter Status: ");
			ps.setString(9, in.nextLine());
			System.out.print("Enter Warehouse Distance: ");
			ps.setInt(10, in.nextInt());
			in.nextLine();
			ps.executeUpdate();
			System.out.print("Customer added correctly");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static void editCustomer(Scanner in) {
    	try {
			System.out.print("Enter the UserID of the customer you would like to edit: ");
			int userID = in.nextInt();
			in.nextLine();
			System.out.print("Enter the attribute of the customer you would like to edit: ");
			String attribute = in.nextLine();
			String sql = "UPDATE Customer SET " + attribute + " = ? WHERE UserID = ?";
			ps = conn.prepareStatement(sql);
			ps.setInt(2, userID);
			System.out.print("Enter the " + attribute + " value you would like to input: ");
			switch (attribute) {
				case "UserID":
					ps.setInt(1, in.nextInt());
					in.nextLine();
					break;
				case "WarehouseID":
					ps.setInt(1, in.nextInt());
					in.nextLine();
					break;
				case "Warehouse_distance":
					ps.setInt(1, in.nextInt());
					in.nextLine();
					break;
				default:
					ps.setString(1, in.nextLine());
					break;
			}
			ps.executeUpdate();
			System.out.print("Customer updated correctly");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
    public static void deleteCustomer(Scanner in) {
    	try {
			System.out.print("Enter the UserID of the customer you would like to delete: ");
			int userID = in.nextInt();
			in.nextLine();
			String sql = "DELETE FROM Customer WHERE UserID = ?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, userID);
			ps.executeUpdate();
			System.out.print("Customer deleted correctly");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static void searchCustomer(Scanner in) {
    	try {
			System.out.print("Enter the UserID of the customer you would like to search for: ");
			int userID = in.nextInt();
			in.nextLine();
			String sql = "SELECT FROM Customer WHERE UserID = " + Integer.toString(userID);
			Statement stmt = conn.createStatement();
			
			System.out.print("Customer deleted correctly");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    

}
