import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PizzaGUIFrame extends JFrame {
    private JRadioButton thinCrust, regularCrust, deepDish;
    private JComboBox<String> sizeBox;
    private JCheckBox[] toppings;
    private JTextArea receiptArea;
    private JButton orderButton, clearButton, quitButton;
    private final double[] sizePrices = {8.00, 12.00, 16.00, 20.00};
    private final double toppingPrice = 1.00;
    private final double taxRate = 0.07;

    public PizzaGUIFrame() {
        setTitle("Pizza Order Form");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Crust
        JPanel crustPanel = new JPanel();
        crustPanel.setBorder(new TitledBorder("Crust Type"));
        thinCrust = new JRadioButton("Thin");
        regularCrust = new JRadioButton("Regular");
        deepDish = new JRadioButton("Deep-Dish");
        ButtonGroup crustGroup = new ButtonGroup();
        crustGroup.add(thinCrust);
        crustGroup.add(regularCrust);
        crustGroup.add(deepDish);
        crustPanel.add(thinCrust);
        crustPanel.add(regularCrust);
        crustPanel.add(deepDish);

        // Size
        JPanel sizePanel = new JPanel();
        sizePanel.setBorder(new TitledBorder("Pizza Size"));
        String[] sizes = {"Small", "Medium", "Large", "Super"};
        sizeBox = new JComboBox<>(sizes);
        sizePanel.add(sizeBox);

        // Toppings
        JPanel toppingPanel = new JPanel();
        toppingPanel.setBorder(new TitledBorder("Toppings"));
        String[] toppingOptions = {"Pepperoni", "Mushrooms", "Onions", "Sausage", "Bacon", "Olives"};
        toppings = new JCheckBox[toppingOptions.length];
        for (int i = 0; i < toppingOptions.length; i++) {
            toppings[i] = new JCheckBox(toppingOptions[i]);
            toppingPanel.add(toppings[i]);
        }

        // Receipt
        JPanel receiptPanel = new JPanel();
        receiptPanel.setBorder(new TitledBorder("Order Receipt"));
        receiptArea = new JTextArea(10, 30);
        receiptArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(receiptArea);
        receiptPanel.add(scrollPane);

        // Buttons
        JPanel buttonPanel = new JPanel();
        orderButton = new JButton("Order");
        clearButton = new JButton("Clear");
        quitButton = new JButton("Quit");
        buttonPanel.add(orderButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(quitButton);

        // comps
        add(crustPanel, BorderLayout.NORTH);
        add(sizePanel, BorderLayout.WEST);
        add(toppingPanel, BorderLayout.EAST);
        add(receiptPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // listener
        orderButton.addActionListener(new OrderListener());
        clearButton.addActionListener(e -> clearForm());
        quitButton.addActionListener(e -> quitApplication());
    }

    private class OrderListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String crust = "";
            if (thinCrust.isSelected()) crust = "Thin";
            if (regularCrust.isSelected()) crust = "Regular";
            if (deepDish.isSelected()) crust = "Deep-Dish";
            if (crust.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please select a crust type.");
                return;
            }

            int sizeIndex = sizeBox.getSelectedIndex();
            double basePrice = sizePrices[sizeIndex];

            StringBuilder toppingsList = new StringBuilder();
            double toppingsCost = 0.0;
            for (JCheckBox topping : toppings) {
                if (topping.isSelected()) {
                    toppingsList.append(topping.getText()).append("\n");
                    toppingsCost += toppingPrice;
                }
            }
            if (toppingsCost == 0) {
                JOptionPane.showMessageDialog(null, "Please select at least one topping.");
                return;
            }

            double subtotal = basePrice + toppingsCost;
            double tax = subtotal * taxRate;
            double total = subtotal + tax;

            receiptArea.setText("=========================================" +
                    "\nType of Crust & Size\t\tPrice" +
                    "\n" + crust + " " + sizeBox.getSelectedItem() + "\t\t$" + basePrice +
                    "\nIngredients\t\tPrice" +
                    "\n" + toppingsList.toString() + "\t\t$" + toppingsCost +
                    "\n-----------------------------------------" +
                    "\nSub-total:\t\t$" + String.format("%.2f", subtotal) +
                    "\nTax:\t\t$" + String.format("%.2f", tax) +
                    "\n-----------------------------------------" +
                    "\nTotal:\t\t$" + String.format("%.2f", total) +
                    "\n=========================================");
        }
    }

    private void clearForm() {
        thinCrust.setSelected(false);
        regularCrust.setSelected(false);
        deepDish.setSelected(false);
        sizeBox.setSelectedIndex(0);
        for (JCheckBox topping : toppings) {
            topping.setSelected(false);
        }
        receiptArea.setText("");
    }

    private void quitApplication() {
        int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to quit?", "Exit", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PizzaGUIFrame().setVisible(true));
    }
}
