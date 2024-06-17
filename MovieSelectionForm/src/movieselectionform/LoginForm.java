package movieselectionform;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginForm extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginForm() {
        setTitle("Login Form");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create components
        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");

        usernameField = new JTextField();
        passwordField = new JPasswordField();

        JButton loginButton = new JButton("Login");

        // Create layout
        setLayout(new GridLayout(3, 2));
        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(new JLabel()); // Empty label for spacing
        add(loginButton);

        // Add ActionListener for login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String enteredUsername = usernameField.getText();
                char[] enteredPassword = passwordField.getPassword();
                
                if(enteredUsername.isEmpty()||enteredPassword.length==0){
                    JOptionPane.showMessageDialog(null,"Please enter both username and the password.","Validation Error",JOptionPane.ERROR_MESSAGE);
                }
                else{
                    // Insert the data into the users table
                    boolean inserted = insertUser(enteredUsername, new String(enteredPassword));

                    // Check if the data was successfully inserted
                    if (inserted) {
                        JOptionPane.showMessageDialog(null, "User successfully registered", "Success", JOptionPane.INFORMATION_MESSAGE);
                        openMovieDetailsForm();
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to register user", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }
    
    private boolean insertUser(String username, String password) {
        // Implement your logic to insert data into the users table
        // You should use a prepared statement to avoid SQL injection
        // Example code (replace it with your actual logic):
        try {
            String dbloc = "jdbc:mysql://localhost:3306/theatre_db";
            Connection con = DriverManager.getConnection(dbloc,"root","");
            // Construct the SQL query with embedded values
            String insertQuery = "INSERT INTO users (username, password) VALUES ('" + username + "', '" + password + "')";

            // Create a statement and execute the update
            try (Statement statement = con.createStatement()) {
                statement.executeUpdate(insertQuery);
            }

        return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private void openMovieDetailsForm() {
        // After successful login, open the MovieDetailsForm or perform any other action
        MovieSelectionForm movieDetailsForm = new MovieSelectionForm();
        movieDetailsForm.setVisible(true);
        this.dispose(); // Close the login form if needed
    }    
}
