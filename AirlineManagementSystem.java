/*
MINI PROJECT
SEMESTER II 2025/2026
SUBJECT CODE : SECJ2154
SUBJECT NAME : OBJECT ORIENTED PROGRAMMING
GROUP : FLYING SPUR
SECTION : 06
*/

import java.awt.BorderLayout;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class AirlineManagementSystem {
    private static final AirlineManager manager = new AirlineManager();
    private static final JTextArea outputArea = new JTextArea(20, 55);
    private static Scanner scanner;

    public static void main(String[] args) {
        openOutputWindow();
        scanner = new Scanner(System.in);

        showToOutput("=== FLYING SPUR Airline Management System ===");
        showToOutput("Enter choices in the console. Results appear in this window.\n");

        loadSampleData();

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Choice: ");
            System.out.println();

            switch (choice) {
                // case 1 -> loadSampleData();
                case 1 -> addFlight();
                case 2 -> setFlightStatus();
                case 3 -> assignCrew();
                case 4 -> registerPassenger();
                case 5 -> viewProfile();
                case 6 -> createBooking();
                case 7 -> checkIn();
                case 8 -> loungeAccess();
                case 9 -> flexChange();
                case 10 -> saveSystemData();
                case 11 -> loadSystemData();
                case 12 -> viewFlights();
                case 13 -> viewPassengers();
                case 14 -> viewCrews();
                case 15 -> viewBookings();
                case 0 -> running = false;
                default -> System.out.println("Invalid choice. Please try again.\n");
            }
        }

        showToOutput("System closed.");
        System.out.println("Goodbye.");
        scanner.close();
        System.exit(0);
    }

    private static void openOutputWindow() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("FLYING SPUR - Output");
            outputArea.setEditable(false);
            frame.add(new JScrollPane(outputArea), BorderLayout.CENTER);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
        try {
            Thread.sleep(300);
        } catch (InterruptedException ignored) {
        }
    }

    private static void printMenu() {
        System.out.println("========== MENU ==========");
        // System.out.println(" 1. Load sample data");
        System.out.println(" 1. Add flight");
        System.out.println(" 2. Set flight status");
        System.out.println(" 3. Assign crew");
        System.out.println(" 4. Register passenger");
        System.out.println(" 5. View passenger profile");
        System.out.println(" 6. Create booking");
        System.out.println(" 7. Process check-in");
        System.out.println(" 8. Check lounge access");
        System.out.println(" 9. Flex flight change");
        System.out.println("10. Save system data to file");
        System.out.println("11. Load system data from file");
        System.out.println("12. View all flights");
        System.out.println("13. View all passengers");
        System.out.println("14. View all crew");
        System.out.println("15. View all bookings");
        System.out.println(" 0. Exit");
        System.out.println("==========================");
    }

    private static void loadSampleData() {
        runOperation("Load Sample Data", () -> {
            if (manager.findFlight("KL101") == null) {
                manager.addFlight(new Flight("KL101", "Tokyo", 500.0));
            }
            if (manager.findFlight("KL202") == null) {
                manager.addFlight(new Flight("KL202", "London", 450.0));
            }
            if (manager.findFlight("KL101") != null) {
                manager.assignCrewToFlight("KL101",
                    new Crew("C001", "Alice Tan", "alice@airline.com", "EMP001", "Captain"));
            }
            if (manager.findPassenger("P001") == null) {
                manager.registerPassenger(new BasicMember("P001", "Siti Aminah", "siti@email.com", "A1234567"));
            }
            if (manager.findPassenger("P002") == null) {
                manager.registerPassenger(new GoldMember("P002", "Ahmad Razak", "ahmad@email.com", "B7654321", 2));
            }
            if (manager.findPassenger("P003") == null) {
                manager.registerPassenger(new PlatinumMember("P003", "Lee Wei Ming", "lee@email.com", "C9988776"));
            }
            System.out.println("Sample data ready: KL101, KL202, P001-P003.");
        });
    }

    private static void addFlight() {
        String dest = readLine("Destination: ");
        double fare = readPositiveDouble("Base fare (RM): ");
        runOperation("Add Flight", () -> manager.createFlight(dest, fare));
    }

    private static void setFlightStatus() {
        printContext(manager::listAllFlights);
        String flightNo = readLine("Flight number: ");
        System.out.println("Statuses: SCHEDULED, BOARDING, DEPARTED, ARRIVED, DELAYED, CANCELLED");
        String statusText = readLine("New status: ");
        runOperation("Set Flight Status", () -> {
            Flight flight = manager.findFlight(flightNo);
            if (flight == null) {
                System.out.println("Flight not found: " + flightNo);
                return;
            }
            try {
                FlightStatus status = FlightStatus.valueOf(statusText.trim().toUpperCase());
                flight.setStatus(status);
                System.out.println("Flight " + flightNo + " status set to " + status + ".");
            } catch (IllegalArgumentException ex) {
                System.out.println("Invalid flight status.");
            }
        });
    }

    private static void assignCrew() {
        printContext(manager::listAllFlights);
        String flightNo = readLine("Flight number: ");
        String name = readLine("Crew name: ");
        String email = readLine("Crew email: ");
        String rank = readLine("Rank: ");
        runOperation("Assign Crew", () -> manager.assignCrewToFlight(flightNo, name, email, rank));
    }

    private static void registerPassenger() {
        System.out.println("Membership: 1=Basic  2=Gold  3=Platinum (any other number registers as Basic)");
        int tier = readInt("Membership choice: ");
        String name = readLine("Name: ");
        String email = readLine("Email: ");
        String passport = readLine("Passport: ");

        if (tier == 2) {
            int flex = readNonNegativeInt("Max flex changes: ");
            runOperation("Register Passenger", () ->
                manager.registerGoldMember(name, email, passport, flex));
        } else if (tier == 3) {
            runOperation("Register Passenger", () ->
                manager.registerPlatinumMember(name, email, passport));
        } else {
            runOperation("Register Passenger", () ->
                manager.registerBasicMember(name, email, passport));
        }
    }

    private static void viewProfile() {
        printContext(manager::listAllPassengers);
        String passengerId = readLine("Passenger ID: ");
        Passenger passenger = manager.findPassenger(passengerId);
        if (passenger == null) {
            showToOutput("Passenger not found: " + passengerId);
            return;
        }
        runOperation("Passenger Profile", passenger::displayProfile);
    }

    private static void createBooking() {
        printContext(manager::listAllPassengers);
        String passengerId = readLine("Passenger ID: ");
        printContext(manager::listAllFlights);
        String flightNo = readLine("Flight number: ");
        printContext(() -> manager.listAvailableSeats(flightNo));
        String seat = readLine("Seat (e.g. 12A): ");
        runOperation("Create Booking", () -> manager.createBooking(passengerId, flightNo, seat));
    }

    private static void checkIn() {
        printContext(manager::listAllPassengers);
        String passengerId = readLine("Passenger ID: ");
        runOperation("Check-in", () -> manager.processCheckIn(passengerId));
    }

    private static void loungeAccess() {
        printContext(manager::listAllPassengers);
        String passengerId = readLine("Passenger ID: ");
        runOperation("Lounge Access", () -> manager.checkLoungeAccess(passengerId));
    }

    private static void flexChange() {
        printContext(manager::listAllPassengers);
        String passengerId = readLine("Passenger ID: ");
        printContext(() -> manager.listPassengerBookings(passengerId));
        String bookingId = readLine("Booking ID: ");
        printContext(manager::listAllFlights);
        String newFlight = readLine("New flight number: ");
        printContext(() -> manager.listAvailableSeats(newFlight));
        String newSeat = readLine("New seat: ");
        runOperation("Flex Change", () ->
            manager.requestFlexFlightChange(passengerId, bookingId, newFlight, newSeat));
    }

    private static void saveSystemData() {
        String path = readLine("File path (e.g. airline_data.txt): ");
        runOperation("Save System Data", () -> manager.saveSystemToFile(path));
    }

    private static void loadSystemData() {
        String path = readLine("File path (e.g. airline_data.txt): ");
        runOperation("Load System Data", () -> manager.loadSystemFromFile(path));
    }

    private static void viewFlights() {
        runOperation("All Flights", manager::listAllFlights);
    }

    private static void viewPassengers() {
        runOperation("All Passengers", manager::listAllPassengers);
    }

    private static void viewCrews() {
        runOperation("All Crew", manager::listAllCrew);
    }

    private static void viewBookings() {
        runOperation("All Bookings", manager::listAllBookings);
    }

    private static void runOperation(String title, Runnable action) {
        showToOutput(">> " + title);
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        PrintStream oldOut = System.out;
        try (PrintStream capture = new PrintStream(buffer, true, StandardCharsets.UTF_8)) {
            System.setOut(capture);
            action.run();
        } finally {
            System.setOut(oldOut);
            String result = buffer.toString(StandardCharsets.UTF_8).trim();
            if (!result.isEmpty()) {
                showToOutput(result);
            }
            showToOutput("");
        }
    }

    private static void printContext(Runnable listing) {
        System.out.println();
        listing.run();
        System.out.println();
    }

    private static void showToOutput(String text) {
        SwingUtilities.invokeLater(() -> {
            outputArea.append(text + "\n");
            outputArea.setCaretPosition(outputArea.getDocument().getLength());
        });
    }

    private static String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }

    private static int readNonNegativeInt(String prompt) {
        while (true) {
            int value = readInt(prompt);
            if (value >= 0) {
                return value;
            }
            System.out.println("Value cannot be negative.");
        }
    }

    private static double readPositiveDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                double value = Double.parseDouble(scanner.nextLine().trim());
                if (value > 0) {
                    return value;
                }
                System.out.println("Value must be greater than zero.");
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}