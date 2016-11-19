import gui.Display;

import javax.swing.*;

public class Loader {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Display frame = new Display();
                frame.setVisible(true);
            }
        });
    }
}
