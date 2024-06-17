package movieselectionform;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;
import java.sql.*;

public class SeatReservationForm extends JFrame {
    private String selectedMovie;
    private String selectedTime;
    private List<String> reservedSeats;
    private List<String> selectedSeats = new ArrayList<>();
    private List<Double> seatPrices = new ArrayList<>();
    
    private static final String DB_URL = "jdbc:mysql://localhost:3306/theatre_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public SeatReservationForm(String selectedMovie, String selectedTime) {
        this.selectedMovie = selectedMovie;
        this.selectedTime = selectedTime;
        reservedSeats = getReservedSeats(selectedMovie, selectedTime);
        initializeSeatPrices();
        
        // Set up the JFrame
        setTitle("Seat Reservation");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create components
        JLabel titleLabel = new JLabel("Selected Movie: " + selectedMovie);
        JLabel timeLabel = new JLabel("Selected Time: " + selectedTime);
        JLabel seatsLabel = new JLabel("Available Seats:");

        JPanel seatPanel = new JPanel(new GridLayout(5, 10));

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 10; j++) {
                int seatNumber = i * 10 + j + 1;
                String seatIdentifier = selectedMovie + "_" + selectedTime + "_Seat" + seatNumber;
                JButton seatButton = new JButton("s"+seatNumber);

                // Check if the seat is reserved
                if (reservedSeats.contains(seatIdentifier)) {
                    seatButton.setEnabled(false);
                } else {
                    // Add action listener to each available seat button
                    seatButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            reserveSeat(seatIdentifier);
                            seatButton.setEnabled(false); // Disable the button after reservation
                        }
                    });
                }

                seatPanel.add(seatButton);
            }
        }
        
        // Confirmation button
        JButton confirmButton = new JButton("Confirm Booking");
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showConfirmationDialog();
            }
        });
        
        JPanel titlePanel = new JPanel(new GridLayout(2,1));
        titlePanel.add(titleLabel);
        titlePanel.add(timeLabel);

        // Set up layout
        setLayout(new BorderLayout());

        // Add components to the JFrame
        add(titlePanel, BorderLayout.NORTH);
        add(seatsLabel, BorderLayout.WEST);
        add(seatPanel, BorderLayout.CENTER);
        add(confirmButton, BorderLayout.SOUTH);
    }

    private void reserveSeat(String seatIdentifier) {
        try{
        // Implement seat reservation logic here
        // You might want to associate the reservation with the current user
        // For simplicity, this example does not include user authentication
        // You should enhance this logic in a real-world scenario
            insertReservation(1, 1, seatIdentifier);
            JOptionPane.showMessageDialog(this, "Seat " + seatIdentifier + " reserved.");
            selectedSeats.add(seatIdentifier);
        }
        catch (SQLException e) {
            handleSQLException(e);
        } 
        catch (Exception e) {
            handleGenericException(e);
        }
    }
    
    private void insertReservation(int userId, int movieId, String seatIdentifier) throws SQLException {
        // Implement the logic to insert a reservation into the database
        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD))  {
            // Construct the SQL query with embedded values
            String insertQuery = "INSERT INTO reservations (user_id, movie_id, seat_number)" + "VALUES (" + userId + ", " + movieId + ", '" + seatIdentifier + "')";
            try (Statement statement = con.createStatement()) {
                statement.executeUpdate(insertQuery);
            }
        }
    }
    
    private List<String> getReservedSeats(String selectedMovie, String selectedTime) {
    List<String> reservedSeats = new ArrayList<>();

    try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
        String selectQuery = "SELECT r.seat_number, m.movie_name, m.movie_time" +
                     " FROM reservations r " +
                     "JOIN movies m ON r.movie_id = m.movie_id " +
                     "WHERE m.movie_name = '" + selectedMovie + "' AND m.movie_time = '" + selectedTime + "'";
        try (Statement statement = con.createStatement();
             ResultSet resultSet = statement.executeQuery(selectQuery)) {

            while (resultSet.next()) {
                String seatNumber = resultSet.getString("seat_number");
                String movieName = resultSet.getString("movie_name");
                String movieTime = resultSet.getString("movie_time");

                // Create a unique identifier by combining seat_number, movie_name, and movie_time
                String seatIdentifier = movieName + "_" + movieTime + "_Seat" + seatNumber;
                
                reservedSeats.add(seatIdentifier);
            }
        }
    } 
    catch (SQLException e) {
        handleSQLException(e);
    }

        return reservedSeats;
    }
    
    private void initializeSeatPrices() {
    double seatPrice = 1000.0;

        for (int i = 1; i <= 50; i++) {
         seatPrices.add(seatPrice);
        }
    }

    private void handleSQLException(SQLException e) {
        // Handle specific SQLExceptions, e.g., foreign key constraint violation
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "SQL Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
    
    private void handleGenericException(Exception e) {
        // Handle other exceptions
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
    
    private void showConfirmationDialog() {
        // Display confirmation dialog with movie details and total price
        int numberOfSeats = reservedSeats.size();
        
        if (selectedSeats.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select at least one seat.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        double totalPrice = calculateTotalPrice();
        
        if (totalPrice <= 0) {
            JOptionPane.showMessageDialog(this, "Total price must be greater than zero.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append("Movie: ").append(selectedMovie).append("\n")
                .append("Time: ").append(selectedTime).append("\n")
                .append("Selected Seats:\n");

        for (String seat : selectedSeats) {
            messageBuilder.append(seat).append("\n");
        }

        messageBuilder.append("Total Price: Rs.").append(totalPrice);

        JOptionPane.showMessageDialog(this, messageBuilder.toString(), "Booking Confirmation", JOptionPane.INFORMATION_MESSAGE);  
    }

    private double calculateTotalPrice(){
        // implement pricing logic here
        double totalPrice = 0.0;
        
        for (String seat : selectedSeats){
            int seatNumber = Integer.parseInt(seat.substring(seat.lastIndexOf("_Seat") + 5));
            totalPrice += seatPrices.get(seatNumber - 1); // Adjust index
        }
        return totalPrice;
    }
}
