public class OrderHistory {
    private String orderId;
    private String order_date;
    private String bookName;
    private int quantity;

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getOrder_date() {
        return order_date;
    }

    public String getBookName() {
        return bookName;
    }

    public int getQuantity() {
        return quantity;
    }
}
