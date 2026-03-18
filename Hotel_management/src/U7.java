import java.util.*;

/**
 * Use Case 7: Add-On Service Selection
 * Implementation Class: U7
 */
public class U7 {

    // 1. Add-On Service Model
    static class AddOnService {
        private final String serviceId;
        private final String name;
        private final double price;

        public AddOnService(String serviceId, String name, double price) {
            this.serviceId = serviceId;
            this.name = name;
            this.price = price;
        }

        public double getPrice() { return price; }
        public String getName() { return name; }

        @Override
        public String toString() {
            return String.format("%s ($%.2f)", name, price);
        }
    }

    // 2. Add-On Service Manager
    static class AddOnServiceManager {
        // Map<ReservationID, List<Services>> - Efficient lookup and one-to-many mapping
        private final Map<String, List<AddOnService>> reservationServices = new HashMap<>();

        // Adds a service to a specific reservation
        public void addServiceToReservation(String reservationId, AddOnService service) {
            reservationServices
                    .computeIfAbsent(reservationId, k -> new ArrayList<>())
                    .add(service);
        }

        // Cost Aggregation - Calculates total cost for selected services
        public double calculateTotalAddOnCost(String reservationId) {
            return reservationServices.getOrDefault(reservationId, Collections.emptyList())
                    .stream()
                    .mapToDouble(AddOnService::getPrice)
                    .sum();
        }

        public List<AddOnService> getServicesForReservation(String reservationId) {
            return reservationServices.getOrDefault(reservationId, Collections.emptyList());
        }
    }

    // 3. Main Execution Logic
    public static void main(String[] args) {
        AddOnServiceManager manager = new AddOnServiceManager();

        // Instantiate available services
        AddOnService massage = new AddOnService("S1", "Deep Tissue Massage", 85.0);
        AddOnService miniBar = new AddOnService("S2", "Mini Bar Access", 40.0);
        AddOnService valet = new AddOnService("S3", "Valet Parking", 25.0);

        // Scenario: Guest with Reservation "RES-99" selects multiple services
        String resId = "RES-99";

        System.out.println("=== Use Case 7: Add-On Service Selection ===");
        System.out.println("Processing for Reservation ID: " + resId);

        // Add services to the mapping
        manager.addServiceToReservation(resId, massage);
        manager.addServiceToReservation(resId, valet);
        manager.addServiceToReservation(resId, miniBar);

        // Retrieve and Display Results
        List<AddOnService> selected = manager.getServicesForReservation(resId);
        double totalCost = manager.calculateTotalAddOnCost(resId);

        System.out.println("\nSelected Services:");
        selected.forEach(s -> System.out.println(" >> " + s));

        System.out.println("-------------------------------------------");
        System.out.printf("Total Additional Cost: $%.2f%n", totalCost);
        System.out.println("-------------------------------------------");
        System.out.println("Status: Core booking/allocation logic untouched.");
    }
}