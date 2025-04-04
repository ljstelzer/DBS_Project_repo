import java.util.Scanner;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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
        switch (entity) {
        	case "Customer":
        		searchCustomer(in);
        	break;
        }
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
			String sql = "SELECT * FROM Customer WHERE UserID = ?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, userID);
			ResultSet rs = ps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
        	int columnCount = rsmd.getColumnCount();
        	for (int i = 1; i <= columnCount; i++) {
        		String value = rsmd.getColumnName(i);
        		System.out.print(value);
        		if (i < columnCount) System.out.print(",  ");
        	}
			System.out.print("\n");
        	while (rs.next()) {
        		for (int i = 1; i <= columnCount; i++) {
        			String columnValue = rs.getString(i);
            		System.out.print(columnValue);
            		if (i < columnCount) System.out.print(",  ");
        		}
    			System.out.print("\n");
        	}
        	rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static void usefulReports(Scanner in) {
    	System.out.println("(1) Renting checkouts. ");
    	System.out.println("(2) Popular item. ");
    	System.out.println("(3) Popular manufacturer. ");
    	System.out.println("(4) Popular drone. ");
    	System.out.println("(5) Items checked out. ");
    	System.out.println("(6) Equipment by type of equipment. ");
    	System.out.print("Please choose the action you would like to perform: ");
    	int action = in.nextInt();
    	in.nextLine();
        switch (action) {
            case 1:
                rentingCheckouts(in);
                break;
            case 2:
                popularItem(in);
                break;
            case 3:
                popularManufacturer(in);
                break;
            case 4:
                popularDrone(in);
                break;
            case 5:
                itemsCheckedOut(in);
                break;
            case 6:
            	equipmentType(in);
            	break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    	
    }
    
    public static void rentingCheckouts(Scanner in) {
    	String sql = "SELECT Equipment_serial_number, Check_out\r\n"
    			+ "FROM Rental, Rental_equipment\r\n"
    			+ "WHERE Rental_equipment.Rental_number = Rental.Rental_number AND CustomerID = ?;\r\n";
    	try {
			ps = conn.prepareStatement(sql);
			System.out.print("Enter the UserID of the customer whose rentals you would like to see: ");
			int userID = in.nextInt();
			in.nextLine();
			ps.setInt(1, userID);
			ResultSet rs = ps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
        	int columnCount = rsmd.getColumnCount();
        	for (int i = 1; i <= columnCount; i++) {
        		String value = rsmd.getColumnName(i);
        		System.out.print(value);
        		if (i < columnCount) System.out.print(",  ");
        	}
			System.out.print("\n");
        	while (rs.next()) {
        		for (int i = 1; i <= columnCount; i++) {
        			String columnValue = rs.getString(i);
            		System.out.print(columnValue);
            		if (i < columnCount) System.out.print(",  ");
        		}
    			System.out.print("\n");
        	}
        	rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static void popularItem(Scanner in) {
    	String sql = "SELECT Serial_number, SUM(julianday(Returns) - julianday(Check_out)) AS Running_rental_time, COUNT(Rental.Rental_number)\r\n"
    			+ "FROM Equipment JOIN Rental_equipment ON Serial_number = Equipment_serial_number\r\n"
    			+ "   JOIN Rental ON Rental_equipment.Rental_number = Rental.Rental_number\r\n"
    			+ "GROUP BY Serial_number\r\n"
    			+ "ORDER BY Running_rental_time DESC, COUNT(Rental.Rental_number)\r\n"
    			+ "LIMIT 1;";
    	try {
			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
        	int columnCount = rsmd.getColumnCount();
        	for (int i = 1; i <= columnCount; i++) {
        		String value = rsmd.getColumnName(i);
        		System.out.print(value);
        		if (i < columnCount) System.out.print(",  ");
        	}
			System.out.print("\n");
        	while (rs.next()) {
        		for (int i = 1; i <= columnCount; i++) {
        			String columnValue = rs.getString(i);
            		System.out.print(columnValue);
            		if (i < columnCount) System.out.print(",  ");
        		}
    			System.out.print("\n");
        	}
        	rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static void popularManufacturer(Scanner in) {
    	String sql = "SELECT M.ManufacturerID, M.City, M.Phone\r\n"
    			+ "FROM Manufacturer M\r\n"
    			+ "JOIN (\r\n"
    			+ "    SELECT ManufacturerID, COUNT(RE.Rental_number) AS Count_rental_number\r\n"
    			+ "    FROM Manufacturer M2\r\n"
    			+ "    LEFT JOIN Equipment E ON E.Manufacturer = M2.ManufacturerID\r\n"
    			+ "    LEFT JOIN Rental_equipment RE ON E.Serial_number = RE.Equipment_serial_number\r\n"
    			+ "    GROUP BY M2.ManufacturerID\r\n"
    			+ ") AS Manufacturer_count_rental ON M.ManufacturerID = Manufacturer_count_rental.ManufacturerID\r\n"
    			+ "JOIN (\r\n"
    			+ "    SELECT MAX(Count_rental_number) AS Max_count_rental_number\r\n"
    			+ "    FROM (\r\n"
    			+ "        SELECT ManufacturerID, COUNT(RE.Rental_number) AS Count_rental_number\r\n"
    			+ "        FROM Manufacturer M3\r\n"
    			+ "        LEFT JOIN Equipment E ON E.Manufacturer = M3.ManufacturerID\r\n"
    			+ "        LEFT JOIN Rental_equipment RE ON E.Serial_number = RE.Equipment_serial_number\r\n"
    			+ "        GROUP BY M3.ManufacturerID\r\n"
    			+ "    ) AS Counted\r\n"
    			+ ") AS Max_count ON Manufacturer_count_rental.Count_rental_number = Max_count.Max_count_rental_number\r\n"
    			+ "LIMIT 1;\r\n";
    	try {
			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
        	int columnCount = rsmd.getColumnCount();
        	for (int i = 1; i <= columnCount; i++) {
        		String value = rsmd.getColumnName(i);
        		System.out.print(value);
        		if (i < columnCount) System.out.print(",  ");
        	}
			System.out.print("\n");
        	while (rs.next()) {
        		for (int i = 1; i <= columnCount; i++) {
        			String columnValue = rs.getString(i);
            		System.out.print(columnValue);
            		if (i < columnCount) System.out.print(",  ");
        		}
    			System.out.print("\n");
        	}
        	rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static void popularDrone(Scanner in) {
    	String sql = "SELECT \r\n"
    			+ "    Drone.Serial_number, \r\n"
    			+ "    SUM(Warehouse_distance) AS Total_Warehouse_Distance, \r\n"
    			+ "    COUNT(Rental_equipment.Equipment_serial_number) AS Equipment_Count\r\n"
    			+ "FROM \r\n"
    			+ "    Drone\r\n"
    			+ "    LEFT JOIN Rental ON Drone.Serial_number = Rental.Drone_number\r\n"
    			+ "    LEFT JOIN Customer ON Rental.CustomerID = Customer.UserID\r\n"
    			+ "    LEFT JOIN Rental_equipment ON Rental.Rental_number = Rental_equipment.Rental_number\r\n"
    			+ "GROUP BY \r\n"
    			+ "    Drone.Serial_number\r\n"
    			+ "ORDER BY \r\n"
    			+ "    Equipment_Count DESC, \r\n"
    			+ "    Total_Warehouse_Distance DESC\r\n"
    			+ "LIMIT 1;";
    	try {
			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
        	int columnCount = rsmd.getColumnCount();
        	for (int i = 1; i <= columnCount; i++) {
        		String value = rsmd.getColumnName(i);
        		System.out.print(value);
        		if (i < columnCount) System.out.print(",  ");
        	}
			System.out.print("\n");
        	while (rs.next()) {
        		for (int i = 1; i <= columnCount; i++) {
        			String columnValue = rs.getString(i);
            		System.out.print(columnValue);
            		if (i < columnCount) System.out.print(",  ");
        		}
    			System.out.print("\n");
        	}
        	rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static void itemsCheckedOut(Scanner in) {
    	String sql = "SELECT CustomerID, COUNT(Equipment_serial_number)\r\n"
    			+ "FROM Rental,Rental_equipment\r\n"
    			+ "WHERE Rental.Rental_number=Rental_equipment.Rental_number\r\n"
    			+ "GROUP BY CustomerID\r\n"
    			+ "HAVING COUNT(Equipment_serial_number) IN (\r\n"
    			+ "  SELECT MAX(Count_equipment)\r\n"
    			+ "  FROM(\r\n"
    			+ "    SELECT COUNT(Equipment_serial_number) AS Count_equipment\r\n"
    			+ "    FROM Rental,Rental_equipment\r\n"
    			+ "    WHERE Rental.Rental_number=Rental_equipment.Rental_number\r\n"
    			+ "    GROUP BY CustomerID \r\n"
    			+ "    ))\r\n"
    			+ "LIMIT 1;";
    	try {
			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
        	int columnCount = rsmd.getColumnCount();
        	for (int i = 1; i <= columnCount; i++) {
        		String value = rsmd.getColumnName(i);
        		System.out.print(value);
        		if (i < columnCount) System.out.print(",  ");
        	}
			System.out.print("\n");
        	while (rs.next()) {
        		for (int i = 1; i <= columnCount; i++) {
        			String columnValue = rs.getString(i);
            		System.out.print(columnValue);
            		if (i < columnCount) System.out.print(",  ");
        		}
    			System.out.print("\n");
        	}
        	rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    
    public static void equipmentType(Scanner in) {
    	String sql = "SELECT Type, Description\r\n"
    			+ "FROM Equipment\r\n"
    			+ "WHERE YEAR < ?\r\n"
    			+ "GROUP BY Type\r\n"
    			+ "ORDER BY Year DESC;";
    	try {
			ps = conn.prepareStatement(sql);
			System.out.print("Enter the year you would like to see equipment manufactured before: ");
			String type = in.nextLine();
			ps.setString(1, type);
			ResultSet rs = ps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
        	int columnCount = rsmd.getColumnCount();
        	for (int i = 1; i <= columnCount; i++) {
        		String value = rsmd.getColumnName(i);
        		System.out.print(value);
        		if (i < columnCount) System.out.print(",  ");
        	}
			System.out.print("\n");
        	while (rs.next()) {
        		for (int i = 1; i <= columnCount; i++) {
        			String columnValue = rs.getString(i);
            		System.out.print(columnValue);
            		if (i < columnCount) System.out.print(",  ");
        		}
    			System.out.print("\n");
        	}
        	rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    
    

}
