import javax.swing.*;

public class UserInput {
    public static String getUserName() {
        String name = JOptionPane.showInputDialog(null, "Enter your name:", "Welcome to Snake Game", JOptionPane.QUESTION_MESSAGE);
        
        // Handle the case where the user cancels or doesn't enter a name
        if (name == null || name.trim().isEmpty()) {
            name = "Player";
        }
        
        JOptionPane.showMessageDialog(null, "Welcome, " + name + "!", "Snake Game", JOptionPane.INFORMATION_MESSAGE);
        return name;
    }
}
