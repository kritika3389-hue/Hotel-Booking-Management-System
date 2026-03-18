import java.util.*;

/**
 * Manages the actual assignment of rooms to guests.
 * Ensures no two guests get the same Room ID (Uniqueness Enforcement).
 */
class AllocationService {
    private Map<String, Integer> inventory;
    private Set<String> allocatedRoomIds;
    private int idCounter = 101; // Starting room numbers

    public AllocationService(Map<String, Integer> inventory) {
        this.inventory = inventory;
        this.allocatedRoomIds = new HashSet<>();
    }

    /**
     * Processes a reservation request: checks stock, assigns unique ID, and updates inventory.
     */
    public void processBooking(ReservationRequest request) {
        String type = request.getRoomType();
        int available = inventory.getOrDefault(type, 0);

        if (available > 0) {
            // Generate a unique Room ID
            String roomId = type.substring(0, 1).toUpperCase() + idCounter++;
            
            // Uniqueness Check using Set
            if (!allocatedRoomIds.contains(roomId)) {
                allocatedRoomIds.add(roomId); // Prevent double-booking
                inventory.put(type, available - 1); // Decrement inventory
                
                System.out.println("SUCCESS: " + request.getGuestName() + 
                                   " confirmed in " + type + " (Room ID: " + roomId + ")");
            } else {
                System.out.println("ERROR: Room ID Collision detected for " + roomId);
            }
        } else {
            System.out.println("FAILED: No availability for " + type + " (Guest: " + request.getGuestName() + ")");
        }
    }
}

/**
 * Entry point for Use Case 6: Reservation Confirmation & Room Allocation.
 * @author Your Name
 * @version 6.0
 */
public class UseCase6RoomAllocationService {
    public static void main(String[] args) {
        System.out.println("******************************************");
        System.out.println("   Book My Stay - Allocation Service    ");
        System.out.println("   Version: 6.0                          ");
        System.out.println("******************************************");

        // 1. Setup initial Inventory
        Map<String, Integer> hotelInventory = new HashMap<>();
        hotelInventory.put("Single Room", 2);
        hotelInventory.put("Suite Room", 1);

        // 2. Setup the Request Queue (from UC5)
        Queue<ReservationRequest> queue = new LinkedList<>();
        queue.offer(new ReservationRequest("Alice", "Suite Room"));
        queue.offer(new ReservationRequest("Bob", "Single Room"));
        queue.offer(new ReservationRequest("Charlie", "Suite Room")); // Should fail (only 1 suite)
        queue.offer(new ReservationRequest("Diana", "Single Room"));

        // 3. Initialize Allocation Service
        AllocationService allocationService = new AllocationService(hotelInventory);

        // 4. Process the Queue in FIFO order
        System.out.println("Processing Queue...");
        System.out.println("------------------------------------------");
        while (!queue.isEmpty()) {
            ReservationRequest currentRequest = queue.poll(); // Dequeue
            allocationService.processBooking(currentRequest);
        }
        System.out.println("------------------------------------------");

        // 5. Final Inventory Check
        System.out.println("Final Inventory State: " + hotelInventory);
    }
}
