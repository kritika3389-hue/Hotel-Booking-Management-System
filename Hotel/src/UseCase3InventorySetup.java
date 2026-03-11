import java.util.HashMap;
import java.util.Map;

/**
 * RoomInventory class centralizes the management of room availability.
 * It replaces scattered variables with a single HashMap for scalability.
 */
class RoomInventory {
    // Single Source of Truth: Map<RoomType, AvailableCount>
    private Map<String, Integer> inventory;

    public RoomInventory() {
        this.inventory = new HashMap<>();
    }

    /**
     * Registers or updates a room type in the inventory.
     */
    public void updateInventory(String roomType, int count) {
        inventory.put(roomType, count);
    }

    /**
     * Retrieves the current count for a specific room type.
     */
    public int getAvailableCount(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    /**
     * Displays the entire inventory state.
     */
    public void displayInventory() {
        System.out.println("Current Room Inventory:");
        System.out.println("-----------------------");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " available");
        }
        System.out.println("-----------------------");
    }
}

/**
 * Entry point for Use Case 3: Centralized Room Inventory Management.
 * @author Your Name
 * @version 3.0
 */
public class UseCase3InventorySetup {
    public static void main(String[] args) {
        System.out.println("******************************************");
        System.out.println("   Book My Stay - Centralized Inventory   ");
        System.out.println("   Version: 3.0                          ");
        System.out.println("******************************************");

        // 1. Initialize the Centralized Inventory
        RoomInventory hotelInventory = new RoomInventory();

        // 2. Register Room Types (Dynamic Entry)
        hotelInventory.updateInventory("Single Room", 10);
        hotelInventory.updateInventory("Double Room", 5);
        hotelInventory.updateInventory("Suite Room", 2);

        // 3. Display initial state
        hotelInventory.displayInventory();

        // 4. Demonstrate a controlled update (e.g., after a manual adjustment)
        System.out.println("Action: Updating Suite Room inventory...");
        hotelInventory.updateInventory("Suite Room", 1);

        // 5. Verify O(1) Lookup
        String searchType = "Double Room";
        System.out.println("Quick Check: " + searchType + " has " +
                hotelInventory.getAvailableCount(searchType) + " spots.");

        // Final State
        hotelInventory.displayInventory();
    }
}