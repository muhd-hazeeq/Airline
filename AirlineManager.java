/*
MINI PROJECT
SEMESTER II 2025/2026
SUBJECT CODE : SECJ2154
SUBJECT NAME : OBJECT ORIENTED PROGRAMMING
GROUP : FLYING SPUR
SECTION : 06
*/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AirlineManager{
    private ArrayList<Flight> flights;
    private ArrayList<Passenger> passengers;
    private int bookingCounter;
    private int passengerCounter;
    private int flightCounter;
    private int crewCounter;

    public AirlineManager(){
        flights = new ArrayList<>();
        passengers = new ArrayList<>();
        bookingCounter = 0;
        passengerCounter = 0;
        flightCounter = 0;
        crewCounter = 0;
    }

    public void addFlight(Flight flight){
        if (flight == null){
            System.out.println("Failed to add flight: flight is null.");
            return;
        }

        if (findFlight(flight.getFlightNumber()) != null){
            System.out.println("Failed to add flight: " + flight.getFlightNumber() + " is already registered.");
            return;
        }

        flights.add(flight);
        flight.initializeSeats();
        syncFlightCounterFromId(flight.getFlightNumber());
    }

    public String createFlight(String destination, double baseFare){
        String flightNumber = nextFlightNumber();
        Flight flight = new Flight(flightNumber, destination, baseFare);
        addFlight(flight);
        System.out.println("Flight registered with number: " + flightNumber);
        return flightNumber;
    }

    public void registerPassenger(Passenger passenger){
        if (passenger == null){
            System.out.println("Failed to register passenger: passenger is null.");
            return;
        }

        if (findPassenger(passenger.getUserId()) != null){
            System.out.println("Failed to register passenger: " + passenger.getUserId() + " is already registered.");
            return;
        }

        passengers.add(passenger);
        syncPassengerCounterFromId(passenger.getUserId());
    }

    public String registerBasicMember(String name, String email, String passportNumber){
        String userId = nextPassengerId();
        registerPassenger(new BasicMember(userId, name, email, passportNumber));
        System.out.println("Passenger registered with ID: " + userId);
        return userId;
    }

    public String registerGoldMember(String name, String email, String passportNumber, int maxFlexChanges){
        String userId = nextPassengerId();
        registerPassenger(new GoldMember(userId, name, email, passportNumber, maxFlexChanges));
        System.out.println("Passenger registered with ID: " + userId);
        return userId;
    }

    public String registerPlatinumMember(String name, String email, String passportNumber){
        String userId = nextPassengerId();
        registerPassenger(new PlatinumMember(userId, name, email, passportNumber));
        System.out.println("Passenger registered with ID: " + userId);
        return userId;
    }

    public boolean assignCrewToFlight(String flightNumber, Crew crew){
        if (crew == null){
            System.out.println("Failed to assign crew: crew member is null.");
            return false;
        }

        Flight flight = findFlight(flightNumber);
        if (flight == null){
            System.out.println("Failed to assign crew: flight not found.");
            return false;
        }

        if (!flight.assignCrew(crew)){
            return false;
        }

        syncCrewCounterFromId(crew.getUserId());
        return true;
    }

    public void assignCrewToFlight(String flightNumber, String name, String email, String rank){
        crewCounter++;
        String crewUserId = String.format("C%03d", crewCounter);
        String employeeId = String.format("EMP%03d", crewCounter);
        Crew crew = new Crew(crewUserId, name, email, employeeId, rank);
        if (assignCrewToFlight(flightNumber, crew)){
            System.out.println("Crew assigned. User ID: " + crewUserId + ", Employee ID: " + employeeId);
        } else {
            crewCounter--;
        }
    }

    public Passenger findPassenger(String userId){
        if (userId == null){
            return null;
        }

        for (Passenger passenger : passengers){
            if (userId.equals(passenger.getUserId())){
                return passenger;
            }
        }
        return null;
    }

    public void listAllFlights(){
        if (flights.isEmpty()){
            System.out.println("No flights registered.");
            return;
        }

        System.out.println("--- Flights ---");
        for (Flight flight : flights){
            System.out.println("  " + flight.getFlightNumber()
                + " | " + flight.getDestination()
                + " | RM " + flight.getBaseFare()
                + " | " + flight.getStatus()
                + " | booking: " + (flight.isAvailableForBooking() ? "open" : "closed"));
        }
    }

    public void listAllPassengers(){
        if (passengers.isEmpty()){
            System.out.println("No passengers registered.");
            return;
        }

        System.out.println("--- Passengers ---");
        for (Passenger passenger : passengers){
            System.out.println("  " + passenger.getUserId()
                + " | " + passenger.getName()
                + " | " + passenger.getMembershipLabel()
                + " | " + passenger.getTotalLoyaltyPoints() + " pts"
                + " | flex: " + formatFlexInfo(passenger));
        }
    }

    public void listAllCrew(){
        if (flights.isEmpty()){
            System.out.println("No flights registered.");
            return;
        }

        System.out.println("--- Crew ---");
        boolean found = false;
        for (Flight flight : flights){
            Crew[] crewMembers = flight.getCrewMembers();
            if (crewMembers.length == 0){
                continue;
            }
            found = true;
            System.out.println("Flight " + flight.getFlightNumber() + ":");
            for (Crew member : crewMembers){
                System.out.println("  " + member.getUserId()
                    + " | " + member.getName()
                    + " | " + member.getEmployeeId()
                    + " | " + member.getRank());
            }
        }
        if (!found){
            System.out.println("No crew assigned to any flight.");
        }
    }

    public void listAllBookings(){
        boolean found = false;
        System.out.println("--- Bookings ---");
        for (Passenger passenger : passengers){
            for (Booking booking : passenger.getBookingHistory()){
                found = true;
                System.out.println("  " + booking.getBookingId()
                    + " | " + passenger.getUserId()
                    + " | " + booking.getFlight().getFlightNumber()
                    + " | seat " + booking.getSeat().getSeatNumber()
                    + " (" + booking.getSeat().getType() + ")"
                    + " | RM " + booking.getFinalTicketPrice());
            }
        }
        if (!found){
            System.out.println("No bookings found.");
        }
    }

    private String formatFlexInfo(Passenger passenger){
        if (passenger instanceof PlatinumMember){
            return "unlimited";
        }
        if (passenger.getMaxFlexChanges() > 0){
            return String.valueOf(passenger.getMaxFlexChanges());
        }
        return "none";
    }

    public Flight findFlight(String flightNumber){
        if (flightNumber == null){
            return null;
        }

        for (Flight flight : flights){
            if (flightNumber.equals(flight.getFlightNumber())){
                return flight;
            }
        }
        return null;
    }

    public void createBooking(String passengerId, String flightNumber, String seatNumber){
        Passenger passenger = findPassenger(passengerId);
        Flight flight = findFlight(flightNumber);

        if (passenger == null){
            System.out.println("Booking failed: passenger not found.");
            return;
        }

        if (flight == null){
            System.out.println("Booking failed: flight not found.");
            return;
        }

        if (!flight.isAvailableForBooking()){
            System.out.println("Booking failed: flight is not available for booking (status: " + flight.getStatus() + ").");
            return;
        }

        Seat seat = flight.findSeat(seatNumber);
        if (seat == null){
            System.out.println("Booking failed: seat not found.");
            return;
        }

        String bookingId = "BK" + (bookingCounter + 1);
        Booking booking = new Booking(bookingId, passenger, flight, seat);
        if (booking.confirmTransaction()){
            bookingCounter++;
        }
    }

    public void processCheckIn(String passengerId){
        Passenger passenger = findPassenger(passengerId);
        if (passenger == null){
            System.out.println("Check-in failed: passenger not found.");
            return;
        }

        if (!passenger.hasActiveBooking()){
            System.out.println("Check-in failed: no active booking found for this passenger.");
            return;
        }

        printLoungeStatus(passenger, true);
    }

    public void checkLoungeAccess(String passengerId){
        Passenger passenger = findPassenger(passengerId);
        if (passenger == null){
            System.out.println("Lounge check failed: passenger not found.");
            return;
        }

        printLoungeStatus(passenger, false);
    }

    private void printLoungeStatus(Passenger passenger, boolean isCheckIn){
        if (isCheckIn){
            System.out.println("Check-in successful for " + passenger.getName() + ".");
        } else {
            System.out.println("Lounge status for " + passenger.getName() + ":");
        }
        System.out.println(passenger.getLoungeEligibilityMessage());
    }

    public void requestFlexFlightChange(String passengerId, String bookingId, String newFlightNumber, String newSeatNumber){
        Passenger passenger = findPassenger(passengerId);
        if (passenger == null){
            System.out.println("Flex flight change failed: passenger not found.");
            return;
        }

        if (!passenger.canFlexChange()){
            System.out.println("Flex flight change failed: membership does not include flex changes.");
            return;
        }

        Booking booking = passenger.findBooking(bookingId);
        if (booking == null){
            System.out.println("Flex flight change failed: booking not found.");
            return;
        }

        if (!booking.getFlight().isActiveForPassenger()){
            System.out.println("Flex flight change failed: original flight is no longer active (status: "
                + booking.getFlight().getStatus() + ").");
            return;
        }

        Flight newFlight = findFlight(newFlightNumber);
        if (newFlight == null){
            System.out.println("Flex flight change failed: new flight not found.");
            return;
        }

        if (!newFlight.isAvailableForBooking()){
            System.out.println("Flex flight change failed: new flight is not available for booking (status: " + newFlight.getStatus() + ").");
            return;
        }

        Seat newSeat = newFlight.findSeat(newSeatNumber);
        if (newSeat == null){
            System.out.println("Flex flight change failed: new seat not found.");
            return;
        }

        if (!booking.applyFlightChange(newFlight, newSeat)){
            System.out.println("Flex flight change failed: booking could not be updated.");
            return;
        }

        passenger.decrementFlexChanges();
        System.out.println("Flight change successful for booking " + bookingId + ".");
    }

    public void saveSystemToFile(String filename){
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))){
            writer.println("VERSION|1");

            for (Flight flight : flights){
                writer.println("FLIGHT|" + flight.getFlightNumber() + "|"
                    + flight.getDestination() + "|" + flight.getBaseFare() + "|"
                    + flight.getStatus());

                Seat[][] seats = flight.getSeats();
                if (seats != null){
                    for (Seat[] row : seats){
                        for (Seat seat : row){
                            if (seat != null && seat.isBooked()){
                                writer.println("SEAT|" + flight.getFlightNumber() + "|"
                                    + seat.getSeatNumber());
                            }
                        }
                    }
                }

                for (Crew crew : flight.getCrewMembers()){
                    if (crew != null){
                        writer.println("CREW|" + flight.getFlightNumber() + "|"
                            + crew.getUserId() + "|" + crew.getName() + "|"
                            + crew.getEmail() + "|" + crew.getEmployeeId() + "|"
                            + crew.getRank());
                    }
                }
            }

            for (Passenger passenger : passengers){
                writer.println("PASSENGER|" + passenger.getUserId() + "|"
                    + membershipCode(passenger) + "|" + passenger.getName() + "|"
                    + passenger.getEmail() + "|" + passenger.getPassportNumber() + "|"
                    + passenger.getTotalLoyaltyPoints() + "|" + passenger.getMaxFlexChanges());
            }

            for (Passenger passenger : passengers){
                for (Booking booking : passenger.getBookingHistory()){
                    writer.println("BOOKING|" + booking.getBookingId() + "|"
                        + passenger.getUserId() + "|"
                        + booking.getFlight().getFlightNumber() + "|"
                        + booking.getSeat().getSeatNumber() + "|"
                        + booking.getFinalTicketPrice());
                }
            }

            System.out.println("System data saved to " + filename + ".");
        } catch (IOException e){
            System.out.println("Error saving system data: " + e.getMessage());
        }
    }

    public void loadSystemFromFile(String filename){
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))){
            List<String> lines = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null){
                line = line.trim();
                if (!line.isEmpty()){
                    lines.add(line);
                }
            }

            if (lines.isEmpty()){
                System.out.println("Load failed: file is empty.");
                return;
            }

            if (!lines.get(0).startsWith("VERSION|")){
                System.out.println("Load failed: unsupported file format.");
                return;
            }

            Map<String, String> pendingFlightStatus = new LinkedHashMap<>();
            List<String[]> seatRecords = new ArrayList<>();
            List<String[]> passengerRecords = new ArrayList<>();
            List<String[]> crewRecords = new ArrayList<>();
            List<String[]> bookingRecords = new ArrayList<>();

            clearAllData();

            for (String record : lines){
                String[] parts = record.split("\\|", -1);
                if (parts.length == 0){
                    continue;
                }

                switch (parts[0]){
                    case "VERSION", "COUNTERS" -> {
                    }
                    case "FLIGHT" -> {
                        if (parts.length < 5){
                            System.out.println("Load skipped: invalid flight record.");
                            break;
                        }
                        addFlight(new Flight(parts[1], parts[2], Double.parseDouble(parts[3])));
                        pendingFlightStatus.put(parts[1], parts[4]);
                    }
                    case "SEAT" -> seatRecords.add(parts);
                    case "PASSENGER" -> passengerRecords.add(parts);
                    case "CREW" -> crewRecords.add(parts);
                    case "BOOKING" -> bookingRecords.add(parts);
                    default -> System.out.println("Load skipped: unknown record type " + parts[0] + ".");
                }
            }

            for (String[] parts : seatRecords){
                loadSeatRecord(parts);
            }

            for (String[] parts : passengerRecords){
                loadPassengerRecord(parts);
            }

            for (String[] parts : crewRecords){
                loadCrewRecord(parts);
            }

            for (String[] parts : bookingRecords){
                loadBookingRecord(parts);
            }

            for (Map.Entry<String, String> entry : pendingFlightStatus.entrySet()){
                Flight flight = findFlight(entry.getKey());
                if (flight == null){
                    continue;
                }
                try {
                    flight.restoreStatus(FlightStatus.valueOf(entry.getValue()));
                } catch (IllegalArgumentException e){
                    System.out.println("Load skipped: invalid status for flight " + entry.getKey() + ".");
                }
            }

            System.out.println("System data loaded from " + filename + ".");
        } catch (IOException e){
            System.out.println("Error loading system data: " + e.getMessage());
        } catch (NumberFormatException e){
            System.out.println("Error loading system data: invalid number in file.");
        }
    }

    private void clearAllData(){
        flights.clear();
        passengers.clear();
        bookingCounter = 0;
        passengerCounter = 0;
        flightCounter = 0;
        crewCounter = 0;
    }

    private void loadSeatRecord(String[] parts){
        if (parts.length < 3){
            System.out.println("Load skipped: invalid seat record.");
            return;
        }

        Flight flight = findFlight(parts[1]);
        if (flight == null){
            System.out.println("Load skipped: flight not found for seat " + parts[2] + ".");
            return;
        }

        Seat seat = flight.findSeat(parts[2]);
        if (seat == null){
            System.out.println("Load skipped: seat not found " + parts[2] + " on " + parts[1] + ".");
            return;
        }

        boolean booked;
        if (parts.length >= 5){
            try {
                SeatType savedType = SeatType.valueOf(parts[3]);
                if (!savedType.equals(seat.getType())){
                    System.out.println("Load skipped: seat type mismatch for " + parts[2] + " on " + parts[1] + ".");
                    return;
                }
            } catch (IllegalArgumentException e){
                System.out.println("Load skipped: invalid seat type for " + parts[2] + " on " + parts[1] + ".");
                return;
            }
            booked = "1".equals(parts[4]);
        } else if (parts.length == 4){
            booked = "1".equals(parts[3]);
        } else {
            booked = true;
        }

        seat.restoreBooked(booked);
    }

    private void loadPassengerRecord(String[] parts){
        if (parts.length < 8){
            System.out.println("Load skipped: invalid passenger record.");
            return;
        }

        String userId = parts[1];
        if (findPassenger(userId) != null){
            System.out.println("Load skipped: passenger already exists " + userId + ".");
            return;
        }

        String name = parts[3];
        String email = parts[4];
        String passport = parts[5];
        int loyaltyPoints = Integer.parseInt(parts[6]);
        int maxFlexChanges = Integer.parseInt(parts[7]);

        Passenger passenger = switch (parts[2]){
            case "GOLD" -> new GoldMember(userId, name, email, passport, maxFlexChanges);
            case "PLATINUM" -> new PlatinumMember(userId, name, email, passport);
            default -> new BasicMember(userId, name, email, passport);
        };

        registerPassenger(passenger);
        passenger.restoreLoyaltyPoints(loyaltyPoints);
    }

    private void loadCrewRecord(String[] parts){
        if (parts.length < 7){
            System.out.println("Load skipped: invalid crew record.");
            return;
        }

        assignCrewToFlight(parts[1],
            new Crew(parts[2], parts[3], parts[4], parts[5], parts[6]));
    }

    private void loadBookingRecord(String[] parts){
        if (parts.length < 6){
            System.out.println("Load skipped: invalid booking record.");
            return;
        }

        String bookingId = parts[1];
        String passengerId = parts[2];
        String flightNumber = parts[3];
        String seatNumber = parts[4];
        double price = Double.parseDouble(parts[5]);

        syncBookingCounterFromId(bookingId);

        Passenger passenger = findPassenger(passengerId);
        if (passenger == null){
            System.out.println("Load skipped: passenger not found for booking " + bookingId + ".");
            return;
        }

        if (passenger.findBooking(bookingId) != null){
            System.out.println("Load skipped: booking already exists " + bookingId + ".");
            return;
        }

        Flight flight = findFlight(flightNumber);
        if (flight == null){
            System.out.println("Load skipped: flight not found for booking " + bookingId + ".");
            return;
        }

        Seat seat = flight.findSeat(seatNumber);
        if (seat == null){
            System.out.println("Load skipped: seat not found for booking " + bookingId + ".");
            return;
        }

        if (!seat.isBooked()){
            seat.restoreBooked(true);
        }

        Booking booking = new Booking(bookingId, passenger, flight, seat);
        booking.attachPersisted(price);
    }

    private String membershipCode(Passenger passenger){
        if (passenger instanceof PlatinumMember){
            return "PLATINUM";
        }
        if (passenger instanceof GoldMember){
            return "GOLD";
        }
        return "BASIC";
    }

    private void syncBookingCounterFromId(String bookingId){
        if (bookingId != null && bookingId.startsWith("BK")){
            try {
                int id = Integer.parseInt(bookingId.substring(2));
                bookingCounter = Math.max(bookingCounter, id);
            } catch (NumberFormatException ignored){
            }
        }
    }

    private String nextPassengerId(){
        passengerCounter++;
        return String.format("P%03d", passengerCounter);
    }

    private String nextFlightNumber(){
        flightCounter++;
        return String.format("FL%03d", flightCounter);
    }

    private void syncPassengerCounterFromId(String userId){
        if (userId != null && userId.startsWith("P")){
            try {
                int id = Integer.parseInt(userId.substring(1));
                passengerCounter = Math.max(passengerCounter, id);
            } catch (NumberFormatException ignored){
            }
        }
    }

    private void syncFlightCounterFromId(String flightNumber){
        if (flightNumber != null && flightNumber.startsWith("FL")){
            try {
                int id = Integer.parseInt(flightNumber.substring(2));
                flightCounter = Math.max(flightCounter, id);
            } catch (NumberFormatException ignored){
            }
        }
    }

    private void syncCrewCounterFromId(String crewUserId){
        if (crewUserId != null && crewUserId.startsWith("C")){
            try {
                int id = Integer.parseInt(crewUserId.substring(1));
                crewCounter = Math.max(crewCounter, id);
            } catch (NumberFormatException ignored){
            }
        }
    }
}