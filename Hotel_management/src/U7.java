import java.util.*;

/**
 * Project: Book My Stay App
 * Use Case 8: Booking History & Reporting
 * Implementation Class: U7
 */
public class U7 {

    // 1. Reservation Model (Core Entity)
    static class Reservation {
        private final String reservationId;
        private final String guestName;
        private final String roomType;
        private final double basePrice;

        public Reservation(String reservationId, String guestName, String roomType, double basePrice) {
            this.reservationId = reservationId;
            this.guestName = guestName;
            this.roomType = roomType;
            this.basePrice = basePrice;
        }

        public String getReservationId() { return reservationId; }
        public double getBasePrice() { return basePrice; }

        @Override
        public String toString() {
            return String.format("ID: %s | Guest: %s | Room: %s | Base: $%.2f",
                    reservationId, guestName, roomType, basePrice);
        }
    }

    // 2. Add-On Service Model (From Use Case 7)
    static class AddOnService {
        private final String name;
        private final double price;

        public AddOnService(String name, double price) {
            this.name = name;
            this.price = price;
        }

        public double getPrice() { return price; }
        @Override
        public String toString() { return name + " ($" + price + ")"; }
    }

    // 3. Booking History & Reporting Service
    static class BookingHistory {
        // List preserves insertion order for chronological tracking
        private final List<Reservation> confirmedBookings = new ArrayList<>();
        private final Map<String, List<AddOnService>> serviceHistory = new HashMap<>();

        // Adds a confirmed booking to history (Persistence Mindset)
        public void recordBooking(Reservation res, List<AddOnService> services) {
            confirmedBookings.add(res);
            if (services != null && !services.isEmpty()) {
                serviceHistory.put(res.getReservationId(), new ArrayList<>(services));
            }
        }

        // Generates a summary report for the Admin
        public void generateOperationalReport() {
            System.out.println("\n========== ADMIN OPERATIONAL REPORT ==========");
            double totalRevenue = 0;

            for (Reservation res : confirmedBookings) {
                double addOnTotal = 0;
                List<AddOnService> extras = serviceHistory.getOrDefault(res.getReservationId(), Collections.emptyList());

                for (AddOnService s : extras) addOnTotal += s.getPrice();

                double grandTotal = res.getBasePrice() + addOnTotal;
                totalRevenue += grandTotal;

                System.out.println(res);
                System.out.println("   + Add-ons: " + extras);
                System.out.printf("   > Total for Booking: $%.2f%n", grandTotal);
                System.out.println("----------------------------------------------");
            }

            System.out.printf("TOTAL SYSTEM REVENUE: $%.2f%n", totalRevenue);
            System.out.println("TOTAL CONFIRMED RESERVATIONS: " + confirmedBookings.size());
            System.out.println("==============================================\n");
        }
    }

    // 4. Main Execution
    public static void main(String[] args) {
        BookingHistory history = new BookingHistory();

        // Simulation 1: Alice Books a Deluxe Room with Spa
        Reservation res1 = new Reservation("RES-101", "Alice", "Deluxe", 200.0);
        List<AddOnService> aliceServices = Arrays.asList(new AddOnService("Spa", 150.0));
        history.recordBooking(res1, aliceServices);

        // Simulation 2: Bob Books a Suite with Breakfast and WiFi
        Reservation res2 = new Reservation("RES-102", "Bob", "Suite", 400.0);
        List<AddOnService> bobServices = Arrays.asList(
                new AddOnService("Breakfast", 25.0),
                new AddOnService("Premium WiFi", 15.0)
        );
        history.recordBooking(res2, bobServices);

        // Simulation