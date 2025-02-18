import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;

public class ViewCurrentTaskGUI extends JFrame {
    private Delivery delivery;
    private Task currentTask;
    private JLabel taskLabel;
    private JButton completeButton;
    private JButton backButton;

    public ViewCurrentTaskGUI(Delivery delivery) {
        this.delivery = delivery;
        this.currentTask = getCurrentTask();

        setTitle("View Current Task");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        taskLabel = new JLabel();
        if (currentTask != null) {
            taskLabel.setText("<html>Task ID: " + currentTask.getTaskID() + "<br>Order ID: " + currentTask.getOrderID() + "<br>Status: " + currentTask.getStatus() + "<br>Assigned Date: " + currentTask.getAssignedDate() + "</html>");
        } else {
            taskLabel.setText("No current task.");
        }
        add(taskLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        completeButton = new JButton("Complete");
        completeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                completeTask();
            }
        });
        buttonPanel.add(completeButton);

        backButton = new JButton("Back");
        backButton.addActionListener(e -> dispose());
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private Task getCurrentTask() {
        List<Task> tasks = FileHandler.loadTaskData();
        for (Task task : tasks) {
            if (task.getDeliveryID().equals(delivery.getUserID()) && "Accepted".equalsIgnoreCase(task.getStatus())) {
                return task;
            }
        }
        return null;
    }

    private void completeTask() {
        if (currentTask != null) {
            currentTask.setStatus("Completed");
            currentTask.setCompletedDate(new Date());
            List<Task> tasks = FileHandler.loadTaskData();
            for (Task task : tasks) {
                if (task.getTaskID() == currentTask.getTaskID()) {
                    task.setStatus("Completed");
                    task.setCompletedDate(new Date());
                    break;
                }
            }
            FileHandler.saveTaskData(tasks);

            Order order = FileHandler.loadOrderData(Integer.parseInt(currentTask.getOrderID()));
            if (order != null) {
                order.setStatus("Successful");
                List<Order> orders = FileHandler.loadOrderData();
                for (int i = 0; i < orders.size(); i++) {
                    if (orders.get(i).getOrderID() == order.getOrderID()) {
                        orders.set(i, order);
                        break;
                    }
                }
                FileHandler.saveOrderData(orders);
            }

            // Update delivery status to Available
            delivery.setStatus("Available");
            List<User> users = FileHandler.loadUserData();
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getUserID().equals(delivery.getUserID())) {
                    users.set(i, delivery);
                    break;
                }
            }
            FileHandler.saveUserData(users);

            JOptionPane.showMessageDialog(this, "Task completed. Order status updated to 'Successful'. Delivery status updated to 'Available'.");
            dispose();
        }
    }
}