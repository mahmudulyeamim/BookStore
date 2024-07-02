import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MysqldbDriver {

    private Connection connection;

    static final int special_offer_percentage = 15;

    public MysqldbDriver() {
        String url = "jdbc:mysql://localhost:3306/BookStore";
        String username = "root";
        String password = "iamgroot";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
            // System.out.println("Database connected successfully");
        } catch (Exception e) {
            System.out.println("Database connection failed");
            System.out.println(e.getMessage());
        }
    }

    public User loginUser(User user) {
        boolean flag = false;
        User user1 = new User();
        String sql = "SELECT * FROM User WHERE username = ? AND password = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());

            ResultSet resultSet = preparedStatement.executeQuery();

            // Check if the result set has any rows
            if (resultSet.next()) {
                String name = resultSet.getString(2);
                System.out.println("Welcome " + name);

                user1.setUserId(resultSet.getInt(1));
                user1.setName(resultSet.getString(2));
                user1.setEmail(resultSet.getString(3));
                user1.setUsername(resultSet.getString(4));
                user1.setPassword(resultSet.getString(5));
                user1.setAddress(resultSet.getString(6));
                user1.setPhone_number(resultSet.getString(7));

                flag = true;

            } else {
                System.out.println("Login failed. Invalid username or password.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(flag) return user1;
        else return null;
    }

    public boolean loginAdmin(Admin admin) {
        boolean flag = false;
        String sql = "SELECT * FROM Admin WHERE username = ? AND password = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, admin.getUsername());
            preparedStatement.setString(2, admin.getPassword());

            ResultSet resultSet = preparedStatement.executeQuery();

            // Check if the result set has any rows
            if (resultSet.next()) {
                String name = resultSet.getString(2);
                System.out.println("Welcome " + name);
                flag = true;
            } else {
                System.out.println("Login failed. Invalid username or password.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return flag;
    }

    public List<Author> getAuthors() {
        List<Author> authorList = new ArrayList<>();
        String sql = "SELECT * FROM Author ORDER BY name";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Author author = new Author();
                author.setAuthorId(resultSet.getInt(1));
                author.setName(resultSet.getString(2));
                author.setBirthDate(resultSet.getString(3));
                author.setCountry(resultSet.getString(4));
                author.setEmail(resultSet.getString(5));

                authorList.add(author);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return authorList;
    }

    public List<Author> getAuthorsByName(String name) {
        List<Author> authorList = new ArrayList<>();
        String sql = "SELECT * FROM Author WHERE name LIKE ? ORDER BY name";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, "%" + name + "%");

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Author author = new Author();
                author.setAuthorId(resultSet.getInt(1));
                author.setName(resultSet.getString(2));
                author.setBirthDate(resultSet.getString(3));
                author.setCountry(resultSet.getString(4));
                author.setEmail(resultSet.getString(5));

                authorList.add(author);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return authorList;
    }

    public List<Book> getBooksByName(String name) {
        List<Book> bookList = new ArrayList<>();
        String sql = "SELECT * FROM Book WHERE title LIKE ? ORDER BY title";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, "%" + name + "%");

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Book book = new Book();
                book.setBookId(resultSet.getInt(1));
                book.setTitle(resultSet.getString(2));
                book.setPublisherId(resultSet.getInt(3));
                book.setIsbn(resultSet.getString(4));
                book.setNumberOfPages(resultSet.getInt(5));
                book.setLanguage(resultSet.getString(6));
                book.setFormat(resultSet.getString(7));
                book.setStockQuantity(resultSet.getInt(8));
                book.setPrice(resultSet.getInt(9));
                book.setDiscount(resultSet.getFloat(10));
                book.setEbookAvailable(resultSet.getBoolean(11));
                book.setPublicationDate(resultSet.getString(12));

                bookList.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bookList;
    }

    public List<Author> getTrendingAuthors() {
        List<Author> authorList = new ArrayList<>();
        String sql = "SELECT a.*\n" +
                "FROM OrderBook ob\n" +
                "JOIN Orders od ON od.order_id = ob.order_id\n" +
                "JOIN AuthorBook ab ON ob.book_id = ab.book_id\n" +
                "JOIN Author a ON ab.author_id = a.author_id\n" +
                "WHERE od.order_date < CURDATE() AND od.order_date >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)\n" +
                "GROUP BY a.author_id\n" +
                "ORDER BY COUNT(*) DESC\n";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Author author = new Author();
                author.setAuthorId(resultSet.getInt(1));
                author.setName(resultSet.getString(2));
                author.setBirthDate(resultSet.getString(3));
                author.setCountry(resultSet.getString(4));
                author.setEmail(resultSet.getString(5));

                authorList.add(author);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return authorList;
    }

    public List<Author> getBestSellerAuthors() {
        List<Author> authorList = new ArrayList<>();
        String sql = "SELECT a.*\n" +
                "FROM OrderBook ob\n" +
                "JOIN Orders od ON od.order_id = ob.order_id\n" +
                "JOIN AuthorBook ab ON ob.book_id = ab.book_id\n" +
                "JOIN Author a ON ab.author_id = a.author_id\n" +
                "WHERE YEAR(od.order_date) = YEAR(NOW()) - 1 OR YEAR(od.order_date) = YEAR(NOW())\n" +
                "GROUP BY a.author_id\n" +
                "ORDER BY COUNT(*) DESC;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Author author = new Author();
                author.setAuthorId(resultSet.getInt(1));
                author.setName(resultSet.getString(2));
                author.setBirthDate(resultSet.getString(3));
                author.setCountry(resultSet.getString(4));
                author.setEmail(resultSet.getString(5));

                authorList.add(author);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return authorList;
    }

    public List<Book> getAuthorBooks(int authorId) {
        List<Book> bookList = new ArrayList<>();
        String sql = "SELECT *\n" +
                "FROM Book\n" +
                "JOIN AuthorBook ON Book.book_id = AuthorBook.book_id\n" +
                "WHERE AuthorBook.author_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, authorId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Book book = new Book();
                book.setBookId(resultSet.getInt(1));
                book.setTitle(resultSet.getString(2));
                book.setPublisherId(resultSet.getInt(3));
                book.setIsbn(resultSet.getString(4));
                book.setNumberOfPages(resultSet.getInt(5));
                book.setLanguage(resultSet.getString(6));
                book.setFormat(resultSet.getString(7));
                book.setStockQuantity(resultSet.getInt(8));
                book.setPrice(resultSet.getInt(9));
                book.setDiscount(resultSet.getFloat(10));
                book.setEbookAvailable(resultSet.getBoolean(11));
                book.setPublicationDate(resultSet.getString(12));

                bookList.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bookList;
    }

    public List<Book> getPublisherBooks(int publisherId) {
        List<Book> bookList = new ArrayList<>();
        String sql = "SELECT * FROM BOOK WHERE publisher_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, publisherId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Book book = new Book();
                book.setBookId(resultSet.getInt(1));
                book.setTitle(resultSet.getString(2));
                book.setPublisherId(resultSet.getInt(3));
                book.setIsbn(resultSet.getString(4));
                book.setNumberOfPages(resultSet.getInt(5));
                book.setLanguage(resultSet.getString(6));
                book.setFormat(resultSet.getString(7));
                book.setStockQuantity(resultSet.getInt(8));
                book.setPrice(resultSet.getInt(9));
                book.setDiscount(resultSet.getFloat(10));
                book.setEbookAvailable(resultSet.getBoolean(11));
                book.setPublicationDate(resultSet.getString(12));

                bookList.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bookList;
    }

    public List<Book> getGenreBooks(int genreId) {
        List<Book> bookList = new ArrayList<>();
        String sql = "SELECT b.*\n" +
                "FROM Book b\n" +
                "JOIN GenreBook gb ON b.book_id = gb.book_id\n" +
                "WHERE gb.genre_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, genreId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Book book = new Book();
                book.setBookId(resultSet.getInt(1));
                book.setTitle(resultSet.getString(2));
                book.setPublisherId(resultSet.getInt(3));
                book.setIsbn(resultSet.getString(4));
                book.setNumberOfPages(resultSet.getInt(5));
                book.setLanguage(resultSet.getString(6));
                book.setFormat(resultSet.getString(7));
                book.setStockQuantity(resultSet.getInt(8));
                book.setPrice(resultSet.getInt(9));
                book.setDiscount(resultSet.getFloat(10));
                book.setEbookAvailable(resultSet.getBoolean(11));
                book.setPublicationDate(resultSet.getString(12));

                bookList.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bookList;
    }

    public List<Genre> getBookGenres(int bookId) {
        List<Genre> genreList = new ArrayList<>();
        String sql = "SELECT g.*\n" +
                "FROM Genre g\n" +
                "JOIN GenreBook gb ON g.genre_id = gb.genre_id\n" +
                "WHERE gb.book_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, bookId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Genre genre = new Genre();

                genre.setGenreId(resultSet.getInt(1));
                genre.setName(resultSet.getString(2));

                genreList.add(genre);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return genreList;
    }

    public List<Author> getBookAuthors(int bookId) {
        List<Author> authorList = new ArrayList<>();
        String sql = "SELECT a.*\n" +
                "FROM Author a\n" +
                "JOIN AuthorBook ab ON ab.author_id = a.author_id\n" +
                "WHERE ab.book_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, bookId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Author author = new Author();
                author.setAuthorId(resultSet.getInt(1));
                author.setName(resultSet.getString(2));
                author.setBirthDate(resultSet.getString(3));
                author.setCountry(resultSet.getString(4));
                author.setEmail(resultSet.getString(5));

                authorList.add(author);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return authorList;
    }

    public Publisher getBookPublisher(int publisherId) {
        Publisher publisher = new Publisher();
        String sql = "SELECT * FROM Publisher WHERE publisher_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, publisherId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                publisher.setPublisherId(resultSet.getInt(1));
                publisher.setName(resultSet.getString(2));
                publisher.setCountry(resultSet.getString(3));
                publisher.setEstablishedDate(resultSet.getString(4));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return publisher;
    }

    public int getBookSellsCount(int bookId) {
        int cnt = 0;
        String sql = "SELECT SUM(quantity) AS total_sales \n" +
                "FROM OrderBook ob\n" +
                "JOIN Orders o ON ob.order_id = o.order_id\n" +
                "WHERE ob.book_id = ? AND order_date IS NOT NULL\n" +
                "GROUP BY book_id";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, bookId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                cnt = resultSet.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return cnt;
    }

    public String getCartOrderId(User user) {
        String orderId = null;
        String sql = "SELECT order_id\n" +
                "FROM Orders\n" +
                "WHERE user_id = ? AND order_date IS NULL;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, user.getUserId());

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                orderId = resultSet.getString(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return orderId;
    }

    public void addNewOrder(User user, int price) {
        String sql = "INSERT INTO Orders (user_id, payment_status, payment_method, total_price) VALUES (?, 'Pending', 'Cash', ?);";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, user.getUserId());
            preparedStatement.setInt(2, price);

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateOrderPrice(String orderId, int price, boolean flag) {
        String sql = null;
        if (flag) {
            sql = "UPDATE Orders SET total_price = total_price + ? WHERE order_id = ?";
        } else {
            sql = "UPDATE Orders SET total_price = total_price - ? WHERE order_id = ?";
        }
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(2, orderId);
            preparedStatement.setInt(1, price);

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkCartBook(String orderId, int bookId) {
        boolean flag = false;
        String sql = "SELECT * FROM OrderBook WHERE order_id = ? AND book_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, orderId);
            preparedStatement.setInt(2, bookId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                flag = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return flag;
    }

    public boolean addToCart(String orderId, int bookId, int quantity, int price) {
        if(checkCartBook(orderId, bookId)) {
            System.out.println("The book is already in your cart");
            return false;
        }

        String sql = "INSERT INTO OrderBook (order_id, book_id, quantity, price) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, orderId);
            preparedStatement.setInt(2, bookId);
            preparedStatement.setInt(3, quantity);
            preparedStatement.setInt(4, price);

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    public List<CartBook> getCartBooks(User user) {
        List<CartBook> cartBookList = new ArrayList<>();
        String sql = "SELECT b.title, b.stock_quantity, ob.quantity, ob.price, ob.book_id\n" +
                "FROM Orders o\n" +
                "JOIN OrderBook ob ON o.order_id = ob.order_id\n" +
                "JOIN Book b ON ob.book_id = b.book_id\n" +
                "WHERE o.user_id = ? AND o.order_date IS NULL";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, user.getUserId());

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                CartBook cartBook = new CartBook();
                cartBook.setName(resultSet.getString(1));
                cartBook.setBookStock(resultSet.getInt(2));
                cartBook.setQuantity(resultSet.getInt(3));
                cartBook.setPrice(resultSet.getInt(4));
                cartBook.setBookId(resultSet.getInt(5));

                cartBookList.add(cartBook);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cartBookList;
    }

    public void updateBookQuantity(int bookId, int quantity) {
        String sql = "UPDATE Book\n" +
                "SET stock_quantity = stock_quantity - ?\n" +
                "WHERE book_id = ?;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(2, bookId);
            preparedStatement.setInt(1, quantity);

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getStockQuantity(int bookId) {
        int cnt = 0;
        String sql = "SELECT stock_quantity FROM Book WHERE book_id = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, bookId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                cnt = resultSet.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return cnt;
    }

    public void placeOrder(String orderId, String address, String pno, String pmtStatus, String pmtMethod) {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);

        String sql = "UPDATE Orders SET order_date = ?, address = ?, phone_number = ?, payment_status = ?, payment_method = ? WHERE order_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, formattedDate);
            preparedStatement.setString(2, address);
            preparedStatement.setString(3, pno);
            preparedStatement.setString(4, pmtStatus);
            preparedStatement.setString(5, pmtMethod);
            preparedStatement.setString(6, orderId);

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteFromOrders(String orderId) {
        String sql = "DELETE FROM Orders WHERE order_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, orderId);

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteFromCart(String orderId, int bookId) {
        String sql = "DELETE FROM OrderBook WHERE order_id = ? AND book_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, orderId);
            preparedStatement.setInt(2, bookId);

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Book> getBooksFrequentlyBoughtTogether(int bookId) {
        List<Book> bookList = new ArrayList<>();
        String sql = "SELECT b.*\n" +
                "FROM OrderBook ob1\n" +
                "JOIN OrderBook ob2 ON ob1.order_id = ob2.order_id AND ob1.book_id <>\n" +
                "ob2.book_id\n" +
                "JOIN Book b ON ob2.book_id = b.book_id\n" +
                "WHERE ob1.book_id = ?\n" +
                "GROUP BY b.book_id";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, 1);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Book book = new Book();
                book.setBookId(resultSet.getInt(1));
                book.setTitle(resultSet.getString(2));
                book.setPublisherId(resultSet.getInt(3));
                book.setIsbn(resultSet.getString(4));
                book.setNumberOfPages(resultSet.getInt(5));
                book.setLanguage(resultSet.getString(6));
                book.setFormat(resultSet.getString(7));
                book.setStockQuantity(resultSet.getInt(8));
                book.setPrice(resultSet.getInt(9));
                book.setDiscount(resultSet.getFloat(10));
                book.setEbookAvailable(resultSet.getBoolean(11));
                book.setPublicationDate(resultSet.getString(12));

                bookList.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bookList;
    }

    public List<Book> getTrendingBooks() {
        List<Book> bookList = new ArrayList<>();
        String sql = "SELECT b.*\n" +
                "FROM OrderBook ob\n" +
                "JOIN Orders od ON od.order_id = ob.order_id\n" +
                "JOIN Book b ON ob.book_id = b.book_id\n" +
                "WHERE od.order_date < CURDATE() AND od.order_date >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)\n" +
                "GROUP BY b.book_id\n" +
                "ORDER BY COUNT(*) DESC";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Book book = new Book();
                book.setBookId(resultSet.getInt(1));
                book.setTitle(resultSet.getString(2));
                book.setPublisherId(resultSet.getInt(3));
                book.setIsbn(resultSet.getString(4));
                book.setNumberOfPages(resultSet.getInt(5));
                book.setLanguage(resultSet.getString(6));
                book.setFormat(resultSet.getString(7));
                book.setStockQuantity(resultSet.getInt(8));
                book.setPrice(resultSet.getInt(9));
                book.setDiscount(resultSet.getFloat(10));
                book.setEbookAvailable(resultSet.getBoolean(11));
                book.setPublicationDate(resultSet.getString(12));

                bookList.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bookList;
    }

    public List<Book> getBestSellingBooks() {
        List<Book> bookList = new ArrayList<>();
        String sql = "SELECT b.*\n" +
                "FROM OrderBook ob\n" +
                "JOIN Book b ON ob.book_id = b.book_id\n" +
                "JOIN Orders od ON od.order_id = ob.order_id\n" +
                "WHERE YEAR(od.order_date) = YEAR(NOW()) - 1 OR YEAR(od.order_date) = YEAR(NOW())\n" +
                "GROUP BY ob.book_id\n" +
                "ORDER BY COUNT(*) DESC";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Book book = new Book();
                book.setBookId(resultSet.getInt(1));
                book.setTitle(resultSet.getString(2));
                book.setPublisherId(resultSet.getInt(3));
                book.setIsbn(resultSet.getString(4));
                book.setNumberOfPages(resultSet.getInt(5));
                book.setLanguage(resultSet.getString(6));
                book.setFormat(resultSet.getString(7));
                book.setStockQuantity(resultSet.getInt(8));
                book.setPrice(resultSet.getInt(9));
                book.setDiscount(resultSet.getFloat(10));
                book.setEbookAvailable(resultSet.getBoolean(11));
                book.setPublicationDate(resultSet.getString(12));

                bookList.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bookList;
    }

    public List<Book> getLatestBooks() {
        List<Book> bookList = new ArrayList<>();
        String sql = "SELECT * FROM Book ORDER BY publication_date DESC LIMIT 10";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Book book = new Book();
                book.setBookId(resultSet.getInt(1));
                book.setTitle(resultSet.getString(2));
                book.setPublisherId(resultSet.getInt(3));
                book.setIsbn(resultSet.getString(4));
                book.setNumberOfPages(resultSet.getInt(5));
                book.setLanguage(resultSet.getString(6));
                book.setFormat(resultSet.getString(7));
                book.setStockQuantity(resultSet.getInt(8));
                book.setPrice(resultSet.getInt(9));
                book.setDiscount(resultSet.getFloat(10));
                book.setEbookAvailable(resultSet.getBoolean(11));
                book.setPublicationDate(resultSet.getString(12));

                bookList.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bookList;
    }

    public List<Book> getOfferedBooks() {
        List<Book> bookList = new ArrayList<>();
        String sql = "SELECT * FROM Book WHERE discount > ? ORDER BY discount DESC";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, special_offer_percentage);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Book book = new Book();
                book.setBookId(resultSet.getInt(1));
                book.setTitle(resultSet.getString(2));
                book.setPublisherId(resultSet.getInt(3));
                book.setIsbn(resultSet.getString(4));
                book.setNumberOfPages(resultSet.getInt(5));
                book.setLanguage(resultSet.getString(6));
                book.setFormat(resultSet.getString(7));
                book.setStockQuantity(resultSet.getInt(8));
                book.setPrice(resultSet.getInt(9));
                book.setDiscount(resultSet.getFloat(10));
                book.setEbookAvailable(resultSet.getBoolean(11));
                book.setPublicationDate(resultSet.getString(12));

                bookList.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bookList;
    }

    public List<Book> getEBooks() {
        List<Book> bookList = new ArrayList<>();
        String sql = "SELECT * FROM Book WHERE ebook_available = true;";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Book book = new Book();
                book.setBookId(resultSet.getInt(1));
                book.setTitle(resultSet.getString(2));
                book.setPublisherId(resultSet.getInt(3));
                book.setIsbn(resultSet.getString(4));
                book.setNumberOfPages(resultSet.getInt(5));
                book.setLanguage(resultSet.getString(6));
                book.setFormat(resultSet.getString(7));
                book.setStockQuantity(resultSet.getInt(8));
                book.setPrice(resultSet.getInt(9));
                book.setDiscount(resultSet.getFloat(10));
                book.setEbookAvailable(resultSet.getBoolean(11));
                book.setPublicationDate(resultSet.getString(12));

                bookList.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bookList;
    }

    public List<Publisher> getTrendingPublishers() {
        List<Publisher> publisherList = new ArrayList<>();
        String sql = "SELECT p.*\n" +
                "FROM OrderBook ob\n" +
                "JOIN Orders od ON od.order_id = ob.order_id\n" +
                "JOIN Book b ON ob.book_id = b.book_id\n" +
                "JOIN Publisher p ON p.publisher_id = b.publisher_id\n" +
                "WHERE od.order_date < CURDATE() AND od.order_date >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)\n" +
                "GROUP BY p.publisher_id\n" +
                "ORDER BY COUNT(*) DESC";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Publisher publisher = new Publisher();

                publisher.setPublisherId(resultSet.getInt(1));
                publisher.setName((resultSet.getString(2)));
                publisher.setCountry(resultSet.getString(3));
                publisher.setEstablishedDate(resultSet.getString(4));

                publisherList.add(publisher);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return publisherList;
    }

    public List<Publisher> getPopularPublishers() {
        List<Publisher> publisherList = new ArrayList<>();
        String sql = "SELECT p.*\n" +
                "FROM OrderBook ob\n" +
                "JOIN Orders od ON od.order_id = ob.order_id\n" +
                "JOIN Book b ON ob.book_id = b.book_id\n" +
                "JOIN Publisher p ON p.publisher_id = b.publisher_id\n" +
                "WHERE YEAR(od.order_date) = YEAR(NOW()) - 1 OR YEAR(od.order_date) = YEAR(NOW())\n" +
                "GROUP BY p.publisher_id\n" +
                "ORDER BY COUNT(*) DESC";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Publisher publisher = new Publisher();

                publisher.setPublisherId(resultSet.getInt(1));
                publisher.setName((resultSet.getString(2)));
                publisher.setCountry(resultSet.getString(3));
                publisher.setEstablishedDate(resultSet.getString(4));

                publisherList.add(publisher);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return publisherList;
    }

    public List<Publisher> getPublishers() {
        List<Publisher> publisherList = new ArrayList<>();
        String sql = "SELECT * FROM Publisher ORDER BY name";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Publisher publisher = new Publisher();

                publisher.setPublisherId(resultSet.getInt(1));
                publisher.setName((resultSet.getString(2)));
                publisher.setCountry(resultSet.getString(3));
                publisher.setEstablishedDate(resultSet.getString(4));

                publisherList.add(publisher);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return publisherList;
    }

    public List<Publisher> getPublishersByName(String name) {
        List<Publisher> publisherList = new ArrayList<>();
        String sql = "SELECT * FROM Publisher WHERE name LIKE ? ORDER BY name";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, "%" + name + "%");

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Publisher publisher = new Publisher();

                publisher.setPublisherId(resultSet.getInt(1));
                publisher.setName((resultSet.getString(2)));
                publisher.setCountry(resultSet.getString(3));
                publisher.setEstablishedDate(resultSet.getString(4));

                publisherList.add(publisher);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return publisherList;
    }

    public List<Genre> getTrendingGenres() {
        List<Genre> genreList = new ArrayList<>();
        String sql = "SELECT g.*\n" +
                "FROM OrderBook ob\n" +
                "JOIN Orders od ON od.order_id = ob.order_id\n" +
                "JOIN GenreBook gb ON ob.book_id = gb.book_id\n" +
                "JOIN Genre g ON gb.genre_id = g.genre_id\n" +
                "WHERE od.order_date < CURDATE() AND od.order_date >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)\n" +
                "GROUP BY g.genre_id\n" +
                "ORDER BY COUNT(*) DESC";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Genre genre = new Genre();

                genre.setGenreId(resultSet.getInt(1));
                genre.setName(resultSet.getString(2));

                genreList.add(genre);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return genreList;
    }

    public List<Genre> getPopularGenres() {
        List<Genre> genreList = new ArrayList<>();
        String sql = "SELECT g.*\n" +
                "FROM OrderBook ob\n" +
                "JOIN Orders od ON od.order_id = ob.order_id\n" +
                "JOIN GenreBook gb ON ob.book_id = gb.book_id\n" +
                "JOIN Genre g ON gb.genre_id = g.genre_id\n" +
                "WHERE YEAR(od.order_date) = YEAR(NOW()) - 1 OR YEAR(od.order_date) = YEAR(NOW())\n" +
                "GROUP BY g.genre_id\n" +
                "ORDER BY COUNT(*) DESC";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Genre genre = new Genre();

                genre.setGenreId(resultSet.getInt(1));
                genre.setName(resultSet.getString(2));

                genreList.add(genre);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return genreList;
    }

    public List<Genre> getGenres() {
        List<Genre> genreList = new ArrayList<>();
        String sql = "SELECT * FROM Genre ORDER BY name";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Genre genre = new Genre();

                genre.setGenreId(resultSet.getInt(1));
                genre.setName(resultSet.getString(2));

                genreList.add(genre);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return genreList;
    }

    public List<Genre> getGenresByName(String name) {
        List<Genre> genreList = new ArrayList<>();
        String sql = "SELECT * FROM Genre WHERE name LIKE ? ORDER BY name";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, "%" + name + "%");

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Genre genre = new Genre();

                genre.setGenreId(resultSet.getInt(1));
                genre.setName(resultSet.getString(2));

                genreList.add(genre);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return genreList;
    }

    private int getMaxUserId() {
        int userId = 0;
        String sql = "SELECT MAX(user_id) FROM User";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                userId = resultSet.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return userId;
    }

    public void createNewAccount(User user) {
        int maxUserId = getMaxUserId();
        String sql = "INSERT INTO User VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, maxUserId + 1);
            preparedStatement.setString(2, user.getName());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getUsername());
            preparedStatement.setString(5, user.getPassword());
            preparedStatement.setString(6, user.getAddress());
            preparedStatement.setString(7, user.getPhone_number());

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Book> getBooksBasedOnPrice(boolean highToLow) {
        List<Book> bookList = new ArrayList<>();

        String sql = null;
        if(highToLow) {
            sql = "SELECT * FROM Book ORDER BY price DESC";
        } else {
            sql = "SELECT * FROM Book ORDER BY price";
        }

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Book book = new Book();
                book.setBookId(resultSet.getInt(1));
                book.setTitle(resultSet.getString(2));
                book.setPublisherId(resultSet.getInt(3));
                book.setIsbn(resultSet.getString(4));
                book.setNumberOfPages(resultSet.getInt(5));
                book.setLanguage(resultSet.getString(6));
                book.setFormat(resultSet.getString(7));
                book.setStockQuantity(resultSet.getInt(8));
                book.setPrice(resultSet.getInt(9));
                book.setDiscount(resultSet.getFloat(10));
                book.setEbookAvailable(resultSet.getBoolean(11));
                book.setPublicationDate(resultSet.getString(12));

                bookList.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bookList;
    }

    public List<OrderHistory> getOrderHistoryAll(int userId) {
        List<OrderHistory> orderHistoryList = new ArrayList<>();

        String sql = "SELECT o.order_id, o.order_date, b.title, ob.quantity\n" +
                "FROM Orders o\n" +
                "JOIN OrderBook ob ON o.order_id = ob.order_id\n" +
                "JOIN Book b ON ob.book_id = b.book_id\n" +
                "WHERE o.user_id = ? AND order_date IS NOT NULL";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                OrderHistory orderHistory = new OrderHistory();

                orderHistory.setOrderId(resultSet.getString(1));
                orderHistory.setOrder_date(resultSet.getString(2));
                orderHistory.setBookName(resultSet.getString(3));
                orderHistory.setQuantity(resultSet.getInt(4));

                orderHistoryList.add(orderHistory);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return orderHistoryList;
    }

    public List<OrderHistory> getOrderHistoryShipped(int userId) {
        List<OrderHistory> orderHistoryList = new ArrayList<>();

        String sql = "SELECT o.order_id, o.order_date, b.title, ob.quantity\n" +
                "FROM Orders o\n" +
                "JOIN OrderBook ob ON o.order_id = ob.order_id\n" +
                "JOIN Book b ON ob.book_id = b.book_id\n" +
                "WHERE o.user_id = ? AND order_date IS NOT NULL AND shipped_date IS NOT NULL";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                OrderHistory orderHistory = new OrderHistory();

                orderHistory.setOrderId(resultSet.getString(1));
                orderHistory.setOrder_date(resultSet.getString(2));
                orderHistory.setBookName(resultSet.getString(3));
                orderHistory.setQuantity(resultSet.getInt(4));

                orderHistoryList.add(orderHistory);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return orderHistoryList;
    }

    public List<OrderHistory> getOrderHistoryNotShipped(int userId) {
        List<OrderHistory> orderHistoryList = new ArrayList<>();

        String sql = "SELECT o.order_id, o.order_date, b.title, ob.quantity\n" +
                "FROM Orders o\n" +
                "JOIN OrderBook ob ON o.order_id = ob.order_id\n" +
                "JOIN Book b ON ob.book_id = b.book_id\n" +
                "WHERE o.user_id = ? AND order_date IS NOT NULL AND shipped_date IS NULL";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                OrderHistory orderHistory = new OrderHistory();

                orderHistory.setOrderId(resultSet.getString(1));
                orderHistory.setOrder_date(resultSet.getString(2));
                orderHistory.setBookName(resultSet.getString(3));
                orderHistory.setQuantity(resultSet.getInt(4));

                orderHistoryList.add(orderHistory);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return orderHistoryList;
    }

    public boolean checkOrderShippedDate(String orderId) {
        boolean flag = false;
        String sql = "SELECT * FROM Orders WHERE order_id = ? AND shipped_date IS NULL";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, orderId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return flag;
    }

    public void updateOrderShippedDate(String orderId) {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);

        String sql = "UPDATE Orders SET shipped_date = ?, payment_status = 'Paid' WHERE order_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, formattedDate);
            preparedStatement.setString(2, orderId);

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateBookStockQuantity(int bookId, int quantity) {
        String sql = "UPDATE Book\n" +
                "SET stock_quantity = stock_quantity + ?\n" +
                "WHERE book_id = ?;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(2, bookId);
            preparedStatement.setInt(1, quantity);

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateBookPrice(int bookId, int price) {
        String sql = "UPDATE Book\n" +
                "SET price = ?\n" +
                "WHERE book_id = ?;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(2, bookId);
            preparedStatement.setInt(1, price);

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateBookDiscount(int bookId, float discount) {
        String sql = "UPDATE Book\n" +
                "SET discount = ?\n" +
                "WHERE book_id = ?;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(2, bookId);
            preparedStatement.setFloat(1, discount);

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateBookEbookAvailability(int bookId, boolean status) {
        String sql = "UPDATE Book\n" +
                "SET ebook_available = ?\n" +
                "WHERE book_id = ?;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(2, bookId);
            preparedStatement.setBoolean(1, status);

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateBookFormat(int bookId, String format) {
        String sql = "UPDATE Book\n" +
                "SET format = ?\n" +
                "WHERE book_id = ?;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(2, bookId);
            preparedStatement.setString(1, format);

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Book> getBooksOrderedMultipleTime(int number) {
        List<Book> bookList = new ArrayList<>();
        String sql = "SELECT b.*\n" +
                "FROM Book b\n" +
                "JOIN OrderBook ob ON b.book_id = ob.book_id\n" +
                "GROUP BY b.book_id\n" +
                "HAVING COUNT(ob.order_id) > ?;";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, number);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Book book = new Book();
                book.setBookId(resultSet.getInt(1));
                book.setTitle(resultSet.getString(2));
                book.setPublisherId(resultSet.getInt(3));
                book.setIsbn(resultSet.getString(4));
                book.setNumberOfPages(resultSet.getInt(5));
                book.setLanguage(resultSet.getString(6));
                book.setFormat(resultSet.getString(7));
                book.setStockQuantity(resultSet.getInt(8));
                book.setPrice(resultSet.getInt(9));
                book.setDiscount(resultSet.getFloat(10));
                book.setEbookAvailable(resultSet.getBoolean(11));
                book.setPublicationDate(resultSet.getString(12));

                bookList.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bookList;
    }

    public List<User> getTop3Customers() {
        List<User> userList = new ArrayList<>();
        String sql = "SELECT u.*, SUM(o.total_price) as total_spent\n" +
                "FROM User u\n" +
                "JOIN Orders o ON u.user_id = o.user_id\n" +
                "GROUP BY u.user_id\n" +
                "ORDER BY total_spent DESC\n" +
                "LIMIT 3";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                User user1 = new User();

                user1.setUserId(resultSet.getInt(1));
                user1.setName(resultSet.getString(2));
                user1.setEmail(resultSet.getString(3));
                user1.setUsername(resultSet.getString(4));
                user1.setPassword(resultSet.getString(5));
                user1.setAddress(resultSet.getString(6));
                user1.setPhone_number(resultSet.getString(7));

                userList.add(user1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return userList;
    }

    public float getOnlinePaymentPercentage() {
        float percentage = 0;
        String sql = "SELECT (COUNT(CASE WHEN o.payment_method = 'Online' THEN 1 END) * 100.0 / COUNT(*)) AS percentage_online_payments\n" +
                "FROM Orders o";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                percentage = resultSet.getFloat(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return percentage;
    }

    public List<Book> getBooksThatHaveNotBeenOrdered() {
        List<Book> bookList = new ArrayList<>();
        String sql = "SELECT b.book_id, b.title\n" +
                "FROM Book b\n" +
                "LEFT JOIN OrderBook ob ON b.book_id = ob.book_id\n" +
                "WHERE ob.book_id IS NULL";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Book book = new Book();
                book.setBookId(resultSet.getInt(1));
                book.setTitle(resultSet.getString(2));
                book.setPublisherId(resultSet.getInt(3));
                book.setIsbn(resultSet.getString(4));
                book.setNumberOfPages(resultSet.getInt(5));
                book.setLanguage(resultSet.getString(6));
                book.setFormat(resultSet.getString(7));
                book.setStockQuantity(resultSet.getInt(8));
                book.setPrice(resultSet.getInt(9));
                book.setDiscount(resultSet.getFloat(10));
                book.setEbookAvailable(resultSet.getBoolean(11));
                book.setPublicationDate(resultSet.getString(12));

                bookList.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bookList;
    }

    public List<User> getUsersWhoHaveNotOrdered() {
        List<User> userList = new ArrayList<>();
        String sql = "SELECT u.*\n" +
                "FROM User u\n" +
                "LEFT JOIN Orders o ON u.user_id = o.user_id\n" +
                "WHERE o.user_id IS NULL";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                User user1 = new User();

                user1.setUserId(resultSet.getInt(1));
                user1.setName(resultSet.getString(2));
                user1.setEmail(resultSet.getString(3));
                user1.setUsername(resultSet.getString(4));
                user1.setPassword(resultSet.getString(5));
                user1.setAddress(resultSet.getString(6));
                user1.setPhone_number(resultSet.getString(7));

                userList.add(user1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return userList;
    }

    public List<Revenue> getAuthorRevenue() {
        List<Revenue> revenueList = new ArrayList<>();
        String sql = "SELECT a.author_id, a.name AS author_name, SUM(ob.price) AS total_revenue\n" +
                "FROM Author a\n" +
                "JOIN AuthorBook ab ON a.author_id = ab.author_id\n" +
                "JOIN Book b ON ab.book_id = b.book_id\n" +
                "JOIN OrderBook ob ON b.book_id = ob.book_id\n" +
                "JOIN Orders o ON ob.order_id = o.order_id\n" +
                "WHERE o.payment_status = 'Paid'\n" +
                "GROUP BY a.author_id, a.name\n" +
                "ORDER BY total_revenue DESC;";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Revenue revenue = new Revenue(resultSet.getInt(1), resultSet.getString(2), resultSet.getInt(3));

                revenueList.add(revenue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return revenueList;
    }

    public List<OrderHistory> getOrdersNotShipped() {
        List<OrderHistory> orderHistoryList = new ArrayList<>();

        String sql = "SELECT o.order_id, o.order_date, b.title, ob.quantity\n" +
                "FROM Orders o\n" +
                "JOIN OrderBook ob ON o.order_id = ob.order_id\n" +
                "JOIN Book b ON ob.book_id = b.book_id\n" +
                "WHERE order_date IS NOT NULL AND shipped_date IS NULL";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                OrderHistory orderHistory = new OrderHistory();

                orderHistory.setOrderId(resultSet.getString(1));
                orderHistory.setOrder_date(resultSet.getString(2));
                orderHistory.setBookName(resultSet.getString(3));
                orderHistory.setQuantity(resultSet.getInt(4));

                orderHistoryList.add(orderHistory);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return orderHistoryList;
    }

    public void closeDatabase() {
        try {
            connection.close();
            // System.out.println("Database closed successfully");
        } catch (Exception e) {
            System.out.println("Database close failed");
            System.out.println(e.getMessage());
        }
    }
}