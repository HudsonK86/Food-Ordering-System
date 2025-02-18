public class Review {
    private int orderID;
    private String type; // Review or Complaint
    private String reviewText;
    private int rating;

    public Review(int orderID, String type, String reviewText, int rating) {
        this.orderID = orderID;
        this.type = type;
        this.reviewText = reviewText;
        this.rating = rating;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return orderID + "," + type + "," + reviewText + "," + rating;
    }

    public static Review fromString(String reviewString) {
        String[] parts = reviewString.split(",");
        if (parts.length == 4) {
            int orderID = Integer.parseInt(parts[0]);
            String type = parts[1];
            String reviewText = parts[2];
            int rating = Integer.parseInt(parts[3]);
            return new Review(orderID, type, reviewText, rating);
        } else {
            throw new IllegalArgumentException("Invalid review string: " + reviewString);
        }
    }
}