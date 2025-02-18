import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Task {
    private int taskID;
    private String orderID;
    private String deliveryID;
    private String status;
    private Date assignedDate;
    private Date completedDate;

    // Constructor
    public Task(int taskID, String orderID, String deliveryID, String status, Date assignedDate, Date completedDate) {
        this.taskID = taskID;
        this.orderID = orderID;
        this.deliveryID = deliveryID;
        this.status = status;
        this.assignedDate = assignedDate;
        this.completedDate = completedDate;
    }

    // Getters and Setters
    public int getTaskID() {
        return taskID;
    }
    
    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getDeliveryID() {
        return deliveryID;
    }

    public void setDeliveryID(String deliveryID) {
        this.deliveryID = deliveryID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getAssignedDate() {
        return assignedDate;
    }

    public void setAssignedDate(Date assignedDate) {
        this.assignedDate = assignedDate;
    }

    public Date getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Date completedDate) {
        this.completedDate = completedDate;
    }
    
    @Override
    public String toString() {
        return taskID + "," + orderID + "," + deliveryID + "," + status + "," + assignedDate + "," + completedDate;
    }

    // Method to generate a unique 6-digit taskID
    public static int generateUniqueTaskID() {
        Set<Integer> existingTaskIDs = new HashSet<>();
        List<Task> existingTasks = FileHandler.loadTaskData();
        for (Task task : existingTasks) {
            existingTaskIDs.add(task.getTaskID());
        }

        Random random = new Random();
        int taskID;
        do {
            taskID = 100000 + random.nextInt(900000); // Generate a 6-digit number
        } while (existingTaskIDs.contains(taskID));
        return taskID;
    }


}