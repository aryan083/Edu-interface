import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Traffic_lights implements ActionListener {
    private JFrame f1;
    private JPanel f2;
    private JLabel messageLabel;

    public void box() {
        f1 = new JFrame();
        f1.setSize(450, 550);
        f1.setLayout(null);

        f2 = new JPanel();
        f2.setBackground(Color.BLACK);
        f2.setBounds(100, 100, 250, 400);
        f2.setLayout(null);

        JButton red = createButton("Red", Color.RED);
        JButton green = createButton("Green", Color.GREEN);
        JButton yellow = createButton("Yellow", Color.YELLOW);

        red.setBounds(100, 150, 100, 30);
        green.setBounds(100, 200, 100, 30);
        yellow.setBounds(100, 250, 100, 30);

        red.addActionListener(this);
        green.addActionListener(this);
        yellow.addActionListener(this);

        messageLabel = new JLabel();
        messageLabel.setBounds(100, 100, 250, 30);
        messageLabel.setForeground(Color.WHITE);
        messageLabel.setHorizontalAlignment(JLabel.CENTER);

        f2.add(red);
        f2.add(green);
        f2.add(yellow);
        f1.add(f2);
        f1.add(messageLabel);

        f1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f1.setVisible(true);
    }

    private JButton createButton(String label, Color color) {
        JButton button = new JButton(label);
        button.setBackground(color);
        button.setOpaque(true);
        button.setBorderPainted(false);
        return button;
    }

    public void actionPerformed(ActionEvent e) {
        String message = "";
        Color color = Color.BLACK;

        if (e.getActionCommand().equals("Red")) {
            message = "Stop";
            color = Color.RED;
        } else if (e.getActionCommand().equals("Green")) {
            message = "Go";
            color = Color.GREEN;
        } else if (e.getActionCommand().equals("Yellow")) {
            message = "Ready";
            color = Color.YELLOW;
        }

        JOptionPane.showMessageDialog(f1, message, "Traffic Light", JOptionPane.INFORMATION_MESSAGE);
        messageLabel.setText(message);
        messageLabel.setForeground(color);
    }

    public static void main(String args[]) {
        Traffic_lights b1 = new Traffic_lights();
        b1.box();
    }
}
