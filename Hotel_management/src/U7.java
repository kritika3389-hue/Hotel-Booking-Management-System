import java.util.*;

/**
 * Project: Book My Stay App
 * Use Case 9: Error Handling & Validation
 * Implementation Class: U7
 */
public class U7 {

    // --- Custom Exceptions for Domain-Specific Errors ---
    static class InvalidRoomTypeException extends Exception {
        public InvalidRoomTypeException(String message) { super(message); }
    }

    static class InsufficientInventoryException extends Exception {
        public InsufficientInventoryException(String message) { super(message); }
    }

    // 1. Reservation Model
    static class Reservation {
        private final String reservationId;
        private final String guestName;
        private final String roomType;

        public Reservation(String reservationId, String guestName, String roomType) {
            this.reservationId = reservationId;
            this.guestName = guestName;
            this.roomType = roomType;
        }

        @Override
        public String toString() {
            return String.format("ResID: %s | Guest: %s | Room: %s", reservationId, guestName, roomType);
        }
    }

    // 2. Booking Validator & Manager
    static class BookingManager {
        private final Map<String, Integer> inventory = new HashMap<>();
        private final List<Reservation> history = new ArrayList<>();

        public BookingManager() {
            // Initialize system state
            inventory.put("DELUXE", 2);
            inventory.put("SUITE", 1);
        }

        /**
         * Core Logic with Guard Clauses and Fail-Fast Design
         */
        public void processBooking(String id, String name, String type)
                throws InvalidRoomTypeException, InsufficientInventoryException {

            String roomTypeKey = type.toUpperCase();

            // Rule 1: Validate Room Type
            if (!inventory.containsKey(roomTypeKey)) {
                throw new InvalidRoomTypeException("Error: Room type '" + type + "' does not exist in our system.");
            }

            // Rule 2: Validate Inventory (Prevent negative state)
            if (inventory.get(roomTypeKey) <= 0) {
                throw new InsufficientInventoryException("Error: No " + roomTypeKey + " rooms available.");
            }

            // If validation passes, update system state
            inventory.put(roomTypeKey, inventory.get(roomTypeKey) - 1);
            Reservation newRes = new Reservation(id, name, roomTypeKey);
            history.add(newRes);

            System.out.println("SUCCESS: Booking confirmed for " + name + " [" + roomTypeKey + "]");
        }

        public void showReport() {
            System.out.println("\n--- Final System State ---");
            System.out.println("Remaining Inventory: " + inventory);
            System.out.println("Confirmed Bookings: " + history.size());
        }
    }

    // 3. Main Execution with Graceful Failure Handling
    public static void main(String[] args) {
        BookingManager manager = new BookingManager();

        // Array of booking attempts: [ID, Guest, RoomType]
        String[][] attempts = {
                {"R1", "Alice", "Deluxe"},   // Valid
                {"R2", "Bob", "Penthouse"},  // Invalid Room Type (Exception)
                {"R3", "Charlie", "Suite"},  // Valid
                {"R4", "David", "Suite"},    // Insufficient Inventory (Exception)
                {"R5", "Eve", "Deluxe"}      // Valid
        };

        System.out.println("=== Use Case 9: Validation & Error Handling ===\n");

        for (String[] a : attempts) {
            try {
                manager.processBooking(a[0], a[1], a[2]);
            } catch (InvalidRoomTypeException | InsufficientInventoryException e) {
                // Graceful Failure Handling: Log error and keep the app running
                System.err.println("VALIDATION FAILED: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("UNKNOWN ERROR: " + e.getMessage());
            }
        }

        manager.showReport();
    }
}