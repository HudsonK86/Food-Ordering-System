import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ManageTaskGUI extends JFrame {
    private Delivery delivery;
    private Task currentTask;
    private JLabel taskLabel;
    private JButton acceptButton;
    private JButton declineButton;
    private JButton backButton;

    public ManageTaskGUI(Delivery delivery) {
        this.delivery = delivery;
        this.currentTask = getCurrentTask();

        setTitle("Manage Task");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        taskLabel = new JLabel();
        if (currentTask != null) {
            taskLabel.setText("<html>Task ID: " + currentTask.getTaskID() + "<br>Order ID: " + currentTask.getOrderID() + "<br>Status: " + currentTask.getStatus() + "</html>");
        } else {
            taskLabel.setText("No task assigned.");
        }
        add(taskLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        acceptButton = new JButton("Accept");
        acceptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                acceptTask();
            }
        });
        buttonPanel.add(acceptButton);

        declineButton = new JButton("Decline");
        declineButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                declineTask();
            }
        });
        buttonPanel.add(declineButton);

        backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private Task getCurrentTask() {
        List<Task> tasks = FileHandler.loadTaskData();
        for (Task task : tasks) {
            if (task.getDeliveryID().equals(delivery.getUserID()) && "Pending".equalsIgnoreCase(task.getStatus())) {
                return task;
            }
        }
        return null;
    }

    private void acceptTask() {
        if (currentTask != null) {
            currentTask.setStatus("Accepted");
            List<Task> tasks = FileHandler.loadTaskData();
            for (Task task : tasks) {
                if (task.getTaskID() == currentTask.getTaskID()) {
                    task.setStatus("Accepted");
                    break;
                }
            }
            FileHandler.saveTaskData(tasks);

            Order order = FileHandler.loadOrderData(Integer.parseInt(currentTask.getOrderID()));
            if (order != null) {
                order.setStatus("Delivery On The Way");
                List<Order> orders = FileHandler.loadOrderData();
                for (int i = 0; i < orders.size(); i++) {
                    if (orders.get(i).getOrderID() == order.getOrderID()) {
                        orders.set(i, order);
                        break;
                    }
                }
                FileHandler.saveOrderData(orders);
            }

            // Update delivery status to Busy
            delivery.setStatus("Busy");
            List<User> users = FileHandler.loadUserData();
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getUserID().equals(delivery.getUserID())) {
                    users.set(i, delivery);
                    break;
                }
            }
            FileHandler.saveUserData(users);

            JOptionPane.showMessageDialog(this, "Task accepted. Drive safely.");
            dispose();
        }
    }

    private void declineTask() {
        if (currentTask != null) {
            currentTask.setStatus("Declined");
            List<Task> tasks = FileHandler.loadTaskData();
            for (Task task : tasks) {
                if (task.getTaskID() == currentTask.getTaskID()) {
                    task.setStatus("Declined");
                    break;
                }
            }
            FileHandler.saveTaskData(tasks);

            Order order = FileHandler.loadOrderData(Integer.parseInt(currentTask.getOrderID()));
            if (order != null) {
                order.assignDeliveryToOrder(order);
            }

            JOptionPane.showMessageDialog(this, "Task declined. Reassigning to another delivery.");
            dispose();
        }
    }
}