package movieselectionform;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MovieSelectionForm extends JFrame {
    private JComboBox<String> movieComboBox;
    private JComboBox<String> timeComboBox;
    private JButton showSeatsButton;

    public MovieSelectionForm() {
        // Set up the JFrame
        setTitle("Movie Selection");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create components
        JLabel movieLabel = new JLabel("Select Movie:");
        JLabel timeLabel = new JLabel("Select Time:");

        String[] movieOptions = {"Movie 1", "Movie 2", "Movie 3"}; // Add your movie options
        movieComboBox = new JComboBox<>(movieOptions);

        String[] timeOptions = {"10.00 AM", "2.00 PM", "7.00 PM"}; // Add your time options
        timeComboBox = new JComboBox<>(timeOptions);

        showSeatsButton = new JButton("Show Available Seats");

        // Set up layout
        setLayout(new GridLayout(4, 2));

        // Add components to the JFrame
        add(movieLabel);
        add(movieComboBox);
        add(timeLabel);
        add(timeComboBox);
        add(new JLabel()); // Empty label for spacing
        add(showSeatsButton);

        // Add action listener
        showSeatsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openSeatSelectionForm();
            }
        });
    }

    private void openSeatSelectionForm() {
        // Open the SeatSelectionForm with selected movie and time
        String selectedMovie = (String) movieComboBox.getSelectedItem();
        String selectedTime = (String) timeComboBox.getSelectedItem();

        SeatReservationForm seatSelectionForm = new SeatReservationForm(selectedMovie, selectedTime);
        seatSelectionForm.setVisible(true);
    }

    public static void main(String[] args) {
                LoginForm loginForm = new LoginForm();
                loginForm.setVisible(true);
    }
}

