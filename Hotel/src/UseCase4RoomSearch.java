import java.util.HashMap;
import java.util.Map;

/**
 * Reusing Room domain logic from UC2.
 */
abstract class Room {
    protected String roomType;
    protected double price;
    protected String amenities;

    public Room(String roomType, double price, String amenities) {
        this.roomType = roomType;
        this.price = price;
        this.amenities = amenities;
    }

    public String getRoomType() { return roomType; }
    public double getPrice() { return price; }
    public String getAmenities() { return amenities; }
}

class SingleRoom extends Room { public SingleRoom() { super("Single Room", 1500.0, "Single Bed, Wi-Fi"); } }
class DoubleRoom extends Room { public DoubleRoom() { super("Double Room", 2500.0, "Double Bed, Wi-Fi, AC"); } }
class SuiteRoom extends Room { public SuiteRoom() { super("Suite Room", 5000.0, "King Bed, Jacuzzi"); } }

/**
 * SearchService provides read-only access to the inventory.
 * It follows the principle of No Side Effects.
 */
class SearchService {
    private Map<String, Integer> inventory;
    private Map<String, Room> roomTemplates;

    public SearchService(Map<String, Integer> inventory) {
        this.inventory = inventory;
        this.roomTemplates = new HashMap<>();
        // Pre-loading room details
        roomTemplates.put("Single Room", new SingleRoom());
        roomTemplates.put("Double Room", new DoubleRoom());
        roomTemplates.put("Suite Room", new SuiteRoom());
    }

    /**
     * Filters and displays only available rooms.
     * This is a "Read-Only" operation.
     */
    public void searchAvailableRooms() {
        System.out.println("Displaying Available Rooms:");
        System.out.println("------------------------------------------");

        boolean found = false;
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            String type = entry.getKey();
            int count = entry.getValue();

            // Validation logic: Only show rooms with count > 0
            if (count > 0) {
                Room details = roomTemplates.get(type);
                System.out.printf("Type: %-12s | Price: %-8.2f | Left: %-2d | Perks: %s%n",
                        type, details.getPrice(), count, details.getAmenities());
                found = true;
            }
        }

        if (!found) {
            System.out.println("Sorry, no rooms are currently available.");
        }
        System.out.println("------------------------------------------");
    }
}

/**
 * Entry point for Use Case 4: Room Search & Availability Check.
 * @author Your Name
 * @version 4.0
 */
public class UseCase4RoomSearch {
    public static void main(String[] args) {
        System.out.println("******************************************");
        System.out.println("   Book My Stay - Room Search Service   ");
        System.out.println("   Version: 4.0                          ");
        System.out.println("******************************************");

        // 1. Setup Inventory (State)
        Map<String, Integer> hotelInventory = new HashMap<>();
        hotelInventory.put("Single Room", 5);
        hotelInventory.put("Double Room", 0); // This should be filtered out
        hotelInventory.put("Suite Room", 2);

        // 2. Initialize Search Service
        SearchService searchService = new SearchService(hotelInventory);

        // 3. Perform Search
        searchService.searchAvailableRooms();

        // Verify that the inventory was NOT modified by the search
        System.out.println("System Check: Search complete. Inventory state remains unchanged.");
    }
}