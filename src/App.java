import javax.swing.*;

public class App {

    public static void main(String[] args) throws Exception {
        while (true) { // Loop to allow restarting the game
            // Get the user's name
            String userName = UserInput.getUserName();

            
            int boardWidth = 600;
            int boardHeight = boardWidth;

            
            JFrame frame = new JFrame("Snake Game - " + userName);
            frame.setVisible(true);
            frame.setSize(boardWidth, boardHeight);
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            
            SnakeGame snakeGame = new SnakeGame(boardWidth, boardHeight);
            frame.add(snakeGame);
            frame.pack(); // Adjust frame size to account for the title bar
            snakeGame.requestFocus(); // Ensure the SnakeGame panel gets focus for key input

            // Wait until the game is over
            while (!snakeGame.gameOver()) {
                Thread.sleep(100);
            }

            // Prompt the user to restart
            int choice = JOptionPane.showConfirmDialog(frame, "Game Over! Restart?", "Snake Game",
                    JOptionPane.YES_NO_OPTION);

            frame.dispose(); // Close the current frame

            if (choice == JOptionPane.NO_OPTION) {
                break; // Exit the loop to end the application
            }
        }
    }
}
