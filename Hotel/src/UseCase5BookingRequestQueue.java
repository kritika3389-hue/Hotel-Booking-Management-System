import java.util.LinkedList;
import java.util.Queue;

/**
 * Represents a Guest's request to book a specific room type.
 */
class ReservationRequest {
    private String guestName;
    private String roomType;

    public ReservationRequest(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }

    @Override
    public String toString() {
        return "Request [Guest: " + guestName + ", Room: " + roomType + "]";
    }
}

/**
 * Manages incoming booking requests using a Queue to ensure fairness.
 */
class BookingQueue {
    // FIFO structure to store requests in order of arrival
    private Queue<ReservationRequest> requestQueue;

    public BookingQueue() {
        this.requestQueue = new LinkedList<>();
    }

    /**
     * Adds a new request to the end of the line.
     */
    public void addRequest(ReservationRequest request) {
        requestQueue.offer(request);
        System.out.println("Enqueued: " + request);
    }

    /**
     * Displays all requests currently waiting to be processed.
     */
    public void showQueue() {
        System.out.println("\nCurrent Booking Request Queue (FIFO Order):");
        System.out.println("------------------------------------------");
        if (requestQueue.isEmpty()) {
            System.out.println("No pending requests.");
        } else {
            for (ReservationRequest req : requestQueue) {
                System.out.println(">> " + req);
            }
        }
        System.out.println("------------------------------------------");
    }

    // Helper method to get the queue for the next Use Case
    public Queue<ReservationRequest> getInternalQueue() {
        return requestQueue;
    }
}

/**
 * Entry point for Use Case 5: Booking Request (First-Come-First-Served).
 * @author Your Name
 * @version 5.0
 */
public class UseCase5BookingRequestQueue {
    public static void main(String[] args) {
        System.out.println("******************************************");
        System.out.println("   Book My Stay - Booking Intake System  ");
        System.out.println("   Version: 5.0                          ");
        System.out.println("******************************************");

        // 1. Initialize the Booking Queue
        BookingQueue bookingOffice = new BookingQueue();

        // 2. Simulate guests submitting requests at different times
        bookingOffice.addRequest(new ReservationRequest("Alice", "Suite Room"));
        bookingOffice.addRequest(new ReservationRequest("Bob", "Single Room"));
        bookingOffice.addRequest(new ReservationRequest("Charlie", "Suite Room"));
        bookingOffice.addRequest(new ReservationRequest("Diana", "Double Room"));

        // 3. Display the queue to verify order preservation
        bookingOffice.showQueue();

        System.out.println("Status: Requests collected and ordered. Ready for allocation.");
    }
}
