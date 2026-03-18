import java.util.*;

/**
 * Project: Book My Stay App
 * Use Case 10: Booking Cancellation & Inventory Rollback
 * Implementation Class: U7
 */
public class U7 {

    // --- Custom Exceptions ---
    static class BookingNotFoundException extends Exception {
        public BookingNotFoundException(String message) { super(message); }
    }

    // 1. Reservation Model (Enhanced with RoomID for tracking)
    static class Reservation {
        private final String resId;
        private final String guestName;
        private final String roomType;
        private final String assignedRoomId;

        public Reservation(String resId, String guestName, String roomType, String assignedRoomId) {
            this.resId = resId;
            this.guestName = guestName;
            this.roomType = roomType;
            this.assignedRoomId = assignedRoomId;
        }

        public String getResId() { return resId; }
        public String getRoomType() { return roomType; }
        public String getAssignedRoomId() { return assignedRoomId; }

        @Override
        public String toString() {
            return String.format("[%s] %s - %s (Room: %s)", resId, guestName, roomType, assignedRoomId);
        }
    }

    // 2. Cancellation Service & Inventory Manager
    static class BookingManager {
        private final Map<String, Integer> inventory = new HashMap<>();
        private final Map<String, Reservation> activeBookings = new HashMap<>();

        // Stack for LIFO Rollback: Tracks recently released room IDs
        private final Stack<String> releasedRoomsStack = new Stack<>();

        public BookingManager() {
            inventory.put("DELUXE", 5);
            inventory.put("SUITE", 2);
        }

        public void createBooking(String id, String name, String type, String roomId) {
            Reservation res = new Reservation(id, name, type, roomId);
            activeBookings.put(id, res);
            inventory.put(type, inventory.get(type) - 1);
            System.out.println("Confirmed: " + res);
        }

        /**
         * State Reversal & Inventory Rollback Logic
         */
        public void cancelBooking(String reservationId) throws BookingNotFoundException {
            // 1. Validation of Cancellation Request
            if (!activeBookings.containsKey(reservationId)) {
                throw new BookingNotFoundException("Cancellation Failed: Reservation " + reservationId + " not found.");
            }

            // 2. Retrieve booking details for rollback
            Reservation resToCancel = activeBookings.remove(reservationId);
            String roomType = resToCancel.getRoomType();
            String roomId = resToCancel.getAssignedRoomId();

            // 3. LIFO Rollback: Record released room ID in stack
            releasedRoomsStack.push(roomId);

            // 4. Inventory Restoration: Increment count immediately
            inventory.put(roomType, inventory.get(roomType) + 1);

            System.out.println("SUCCESS: Cancelled " + reservationId + ". Room " + roomId + " returned to pool.");
        }

        public void displayStatus() {
            System.out.println("\n--- System Status ---");
            System.out.println("Active Bookings: " + activeBookings.values());
            System.out.println("Inventory Levels: " + inventory);
            System.out.println("Recently Released Rooms (Stack): " + releasedRoomsStack);
            System.out.println("---------------------\n");
        }
    }

    // 3. Main Execution
    public static void main(String[] args) {
        BookingManager manager = new BookingManager();

        System.out.println("=== Use Case 10: Booking Cancellation & Rollback ===\n");

        // Initial Bookings
        manager.createBooking("RES-001", "Alice", "DELUXE", "D-101");
        manager.createBooking("RES-002", "Bob", "SUITE", "S-501");
        manager.displayStatus();

        // Scenario 1: Valid Cancellation
        try {
            manager.cancelBooking("RES-001");
        } catch (BookingNotFoundException e) {
            System.err.println(e.getMessage());
        }

        // Scenario 2: Duplicate/Invalid Cancellation (Validation check)
        try {
            System.out.println("Attempting to cancel RES-001 again...");
            manager.cancelBooking("RES-001");
        } catch (BookingNotFoundException e) {
            System.err.println(e.getMessage());
        }

        // Scenario 3: Another Booking and Cancellation
        manager.createBooking("RES-003", "Charlie", "DELUXE", "D-102");
        try {
            manager.cancelBooking("RES-003");
        } catch (BookingNotFoundException e) {
            System.err.println(e.getMessage());
        }

        manager.displayStatus();
    }
}