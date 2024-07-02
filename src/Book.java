public class Book {
    private int bookId, publisherId, numberOfPages, stockQuantity, price;
    private float discount;
    private String title, isbn, language, format, publicationDate;
    private boolean ebookAvailable;

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public void setPublisherId(int publisherId) {
        this.publisherId = publisherId;
    }

    public void setNumberOfPages(int numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public void setEbookAvailable(boolean ebookAvailable) {
        this.ebookAvailable = ebookAvailable;
    }

    public int getBookId() {
        return bookId;
    }

    public int getPublisherId() {
        return publisherId;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public float getPrice() {
        return price;
    }

    public float getDiscount() {
        return discount;
    }

    public String getTitle() {
        return title;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getLanguage() {
        return language;
    }

    public String getFormat() {
        return format;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public boolean isEbookAvailable() {
        return ebookAvailable;
    }
}
