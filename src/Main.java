import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        MysqldbDriver mysqldbDriver = new MysqldbDriver();

        boolean exited = false;

        while(!exited) {
            System.out.print("Login as ");
            System.out.print("1.User ");
            System.out.println("2.Admin");
            System.out.println("3.Sign up");
            System.out.println("4.Exit");
            System.out.print("Enter: ");

            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();

            if (choice == 1) {
                System.out.print("username: ");
                String dummy = scanner.nextLine();
                String username = scanner.nextLine();
                System.out.print("password: ");
                String password = scanner.nextLine();

                User user = new User();
                user.setUsername(username);
                user.setPassword(password);

                User currUser = mysqldbDriver.loginUser(user);

                if (currUser != null) {
                    exited = manageFrontPage(mysqldbDriver, currUser);
                } else {
                    System.out.println("User not found");
                }
            } else if (choice == 2) {
                System.out.print("username: ");
                String dummy = scanner.nextLine();
                String username = scanner.nextLine();
                System.out.print("password: ");
                String password = scanner.nextLine();

                Admin admin = new Admin();
                admin.setUsername(username);
                admin.setPassword(password);

                if (mysqldbDriver.loginAdmin(admin)) {
                    exited = manageAdminPage(mysqldbDriver);
                } else {
                    System.out.println("Admin not found");
                }
            } else if (choice == 3) {
                System.out.print("name: ");
                String dummy = scanner.nextLine();
                String name = scanner.nextLine();
                System.out.print("email: ");
                String email = scanner.nextLine();
                System.out.print("Address: ");
                String address = scanner.nextLine();
                System.out.print("Phone number: ");
                String pno = scanner.nextLine();
                System.out.print("username: ");
                String username = scanner.nextLine();
                System.out.print("password: ");
                String password = scanner.nextLine();

                User user = new User();
                user.setName(name);
                user.setEmail(email);
                user.setAddress(address);
                user.setPhone_number(pno);
                user.setUsername(username);
                user.setPassword(password);

                mysqldbDriver.createNewAccount(user);

                System.out.println("Account created successfully");
            } else if (choice == 4) {
                exited = true;
            }
            else {
                System.out.println("Invalid command. Please type again");
            }
        }

        mysqldbDriver.closeDatabase();
    }

    private static boolean manageAdminPage(MysqldbDriver mysqldbDriver) {
        boolean back = false;
        boolean exited = false;

        while(!back && !exited) {
            System.out.println("1.Update book details");
            System.out.println("2.Confirm order delivery");
            System.out.println("3.Show important statistics");
            System.out.println("4.Back");
            System.out.println("5.Exit");
            System.out.print("Enter: ");

            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();

            if (choice == 1) {
                System.out.print("Enter book id: ");
                int bookId = scanner.nextInt();
                exited = updateBookDetails(mysqldbDriver, bookId);
            } else if (choice == 2) {
                System.out.print("Enter order id of the delivery: ");
                String dummy = scanner.nextLine();
                String orderId = scanner.nextLine();

                if(mysqldbDriver.checkOrderShippedDate(orderId)) {
                    mysqldbDriver.updateOrderShippedDate(orderId);
                    System.out.println("Update successful");
                }
                else {
                    System.out.println("Update unsuccessful");
                }
            } else if (choice == 3) {
                exited = showStatistics(mysqldbDriver);
            } else if (choice == 4) {
                back = true;
            } else if (choice == 5) {
                exited = true;
            } else {
                System.out.println("Invalid command. Please type again");
            }
        }

        return exited;
    }

    private static boolean updateBookDetails(MysqldbDriver mysqldbDriver, int bookId) {
        boolean back = false;
        boolean exited = false;

        while(!back && !exited) {
            System.out.println("Update book information");
            System.out.println("1.Stock quantity");
            System.out.println("2.Price");
            System.out.println("3.Discount");
            System.out.println("4.E-Book availability");
            System.out.println("5.Format");
            System.out.println("6.Back");
            System.out.println("7.Exit");
            System.out.print("Enter: ");

            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();

            if (choice == 1) {
                System.out.print("Enter new stock quantity: ");
                int quantity = scanner.nextInt();
                mysqldbDriver.updateBookStockQuantity(bookId, quantity);
                System.out.println("Update successful");
            } else if (choice == 2) {
                System.out.print("Enter new price: ");
                int price = scanner.nextInt();
                mysqldbDriver.updateBookPrice(bookId, price);
                System.out.println("Update successful");
            } else if (choice == 3) {
                System.out.print("Enter new discount: ");
                float discount = scanner.nextFloat();
                if (discount < 0.0 || discount > 100.0) {
                    System.out.println("Discount is not valid");
                    System.out.println("Update unsuccessful");
                }
                else {
                    mysqldbDriver.updateBookDiscount(bookId, discount);
                    System.out.println("Update successful");
                }
            } else if (choice == 4) {
                System.out.println("1.E-Book available");
                System.out.println("2.E-Book unavailable");
                System.out.print("Enter: ");
                int type = scanner.nextInt();
                if (type == 1 || type == 2) {
                    mysqldbDriver.updateBookEbookAvailability(bookId, type == 1);
                    System.out.println("Update successful");
                } else {
                    System.out.println("Update unsuccessful");
                }
            } else if (choice == 5) {
                System.out.println("1.Paperback");
                System.out.println("2.Hardcover");
                System.out.print("Enter: ");
                int type = scanner.nextInt();
                if (type == 1) {
                    mysqldbDriver.updateBookFormat(bookId, "Paperback");
                    System.out.println("Update successful");
                } else if (type == 2) {
                    mysqldbDriver.updateBookFormat(bookId, "Hardcover");
                    System.out.println("Update successful");
                } else {
                    System.out.println("Update unsuccessful");
                }
            } else if (choice == 6) {
                back = true;
            } else if (choice == 7) {
                exited = true;
            }
        }

        return exited;
    }

    public static boolean showStatistics(MysqldbDriver mysqldbDriver) {
        boolean back = false;
        boolean exited = false;

        while (!back && !exited) {
            System.out.println("1.Books that have ordered multiple times");
            System.out.println("2.Top 3 customers who spent the most");
            System.out.println("3.Percentage of orders that paid through online services");
            System.out.println("4.Books that have not been ordered yet");
            System.out.println("5.Users who have not placed any order yet");
            System.out.println("6.Show the revenue generated by each author");
            System.out.println("7.Show orders that are yet to be shipped");
            System.out.println("8.Back");
            System.out.println("9.Exit");
            System.out.print("Enter: ");

            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();

            if (choice == 1) {
                System.out.print("Enter number: ");
                int number = scanner.nextInt();
                List<Book> books = mysqldbDriver.getBooksOrderedMultipleTime(number);

                for(int i = 0; i < books.size(); i++) {
                    System.out.println(i + 1 + "." + books.get(i).getTitle());
                }
            } else if (choice == 2) {
                List<User> users = mysqldbDriver.getTop3Customers();

                for(int i = 0; i < users.size(); i++) {
                    System.out.println(i + 1 + "." + users.get(i).getName());
                }
            } else if (choice == 3) {
                float percentage = mysqldbDriver.getOnlinePaymentPercentage();

                System.out.println(percentage);
            } else if (choice == 4) {
                List<Book> books = mysqldbDriver.getBooksThatHaveNotBeenOrdered();

                for(int i = 0; i < books.size(); i++) {
                    System.out.println(i + 1 + "." + books.get(i).getTitle());
                }
            } else if (choice == 5) {
                List<User> users = mysqldbDriver.getUsersWhoHaveNotOrdered();

                for(int i = 0; i < users.size(); i++) {
                    System.out.println(i + 1 + "." + users.get(i).getName());
                }
            } else if (choice == 6) {
                List<Revenue> revenues = mysqldbDriver.getAuthorRevenue();

                System.out.println("Author - Revenue");
                for(int i = 0; i < revenues.size(); i++) {
                    System.out.println(i + 1 + "." + revenues.get(i).authorName + " - " + revenues.get(i).revenue);
                }
            } else if (choice == 7) {
                List<OrderHistory> orderHistories = mysqldbDriver.getOrdersNotShipped();

                System.out.println("Order id - order date");
                for(int i = 0; i < orderHistories.size(); i++) {
                    System.out.println(i + 1 + "." + orderHistories.get(i).getOrderId() + " - " + orderHistories.get(i).getOrder_date());
                }
            } else if (choice == 8) {
                back = true;
            } else if (choice == 9) {
                exited = true;
            }
        }

        return exited;
    }

    private static boolean manageFrontPage(MysqldbDriver mysqldbDriver, User user) {
        boolean back = false;
        boolean exited = false;

        while(!back && !exited) {
            System.out.println("1.Authors");
            System.out.println("2.Publishers");
            System.out.println("3.Genres");
            System.out.println("4.E-Books");
            System.out.println("5.Special Offers");
            System.out.println("6.Trending books");
            System.out.println("7.Bestselling books");
            System.out.println("8.Latest books");
            System.out.println("9.Search by book name");
            System.out.println("10.Show books based on price: high to low");
            System.out.println("11.Show books based on price: low to high");
            System.out.println("12.Show my cart");
            System.out.println("13.Order history");
            System.out.println("14.Back");
            System.out.println("15.Exit");
            System.out.print("Enter: ");

            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();

            if (choice == 1) {
                boolean back1 = false;

                while(!back1 && !exited) {
                    System.out.println("1.Trending authors");
                    System.out.println("2.Bestseller authors");
                    System.out.println("3.Show all authors");
                    System.out.println("4.Search by author name");
                    System.out.println("5.Back");
                    System.out.println("6.Exit");
                    System.out.print("Enter: ");

                    int choice1 = scanner.nextInt();

                    if (choice1 == 1) {
                        List<Author> authors = mysqldbDriver.getTrendingAuthors();
                        exited = manageAuthorsPage(mysqldbDriver, authors, "Trending authors", user);
                    } else if (choice1 == 2) {
                        List<Author> authors = mysqldbDriver.getBestSellerAuthors();
                        exited = manageAuthorsPage(mysqldbDriver, authors, "Bestseller authors", user);

                    } else if (choice1 == 3) {
                        List<Author> authors = mysqldbDriver.getAuthors();
                        exited = manageAuthorsPage(mysqldbDriver, authors, "All authors", user);
                    } else if (choice1 == 4) {
                        System.out.println("Search by author name");
                        System.out.println("1.Back");
                        System.out.print("Enter: ");

                        String dummy = scanner.nextLine();
                        String name = scanner.nextLine();
                        if(!name.equals("1")) {
                            List<Author> authors = mysqldbDriver.getAuthorsByName(name);

                            if(authors.isEmpty()) {
                                System.out.println("Sorry! We couldn't find any author named '" + name + "'");
                            } else {
                                exited = manageAuthorsPage(mysqldbDriver, authors, "Results based on searching by: " + name, user);
                            }
                        }

                    } else if (choice1 == 5) {
                        back1 = true;
                    } else if (choice1 == 6) {
                        exited = true;
                    } else {
                        System.out.println("Invalid command. Please type again");
                    }
                }
            } else if (choice == 2) {
                boolean back1 = false;

                while(!back1 && !exited) {
                    System.out.println("1.Trending publishers");
                    System.out.println("2.Popular publishers");
                    System.out.println("3.Show all publishers");
                    System.out.println("4.Search by publisher name");
                    System.out.println("5.Back");
                    System.out.println("6.Exit");
                    System.out.print("Enter: ");

                    int choice1 = scanner.nextInt();

                    if (choice1 == 1) {
                        List<Publisher> publishers = mysqldbDriver.getTrendingPublishers();
                        exited = managePublishersPage(mysqldbDriver, publishers, "Trending publishers", user);
                    } else if (choice1 == 2) {
                        List<Publisher> publishers = mysqldbDriver.getPopularPublishers();
                        exited = managePublishersPage(mysqldbDriver, publishers, "Popular publishers", user);

                    } else if (choice1 == 3) {
                        List<Publisher> publishers = mysqldbDriver.getPublishers();
                        exited = managePublishersPage(mysqldbDriver, publishers, "All publishers", user);
                    } else if (choice1 == 4) {
                        System.out.println("Search by publisher name");
                        System.out.println("1.Back");
                        System.out.print("Enter: ");

                        String dummy = scanner.nextLine();
                        String name = scanner.nextLine();
                        if (!name.equals("1")) {
                            List<Publisher> publishers = mysqldbDriver.getPublishersByName(name);

                            if (publishers.isEmpty()) {
                                System.out.println("Sorry! We couldn't find any publisher named '" + name + "'");
                            } else {
                                exited = managePublishersPage(mysqldbDriver, publishers, "Results based on searching by: " + name, user);
                            }
                        }

                    } else if (choice1 == 5) {
                        back1 = true;
                    } else if (choice1 == 6) {
                        exited = true;
                    } else {
                        System.out.println("Invalid command. Please type again");
                    }
                }
            } else if (choice == 3) {
                boolean back1 = false;

                while(!back1 && !exited) {
                    System.out.println("1.Trending genres");
                    System.out.println("2.Popular genres");
                    System.out.println("3.Show all genres");
                    System.out.println("4.Search by genre name");
                    System.out.println("5.Back");
                    System.out.println("6.Exit");
                    System.out.print("Enter: ");

                    int choice1 = scanner.nextInt();

                    if (choice1 == 1) {
                        List<Genre> genres = mysqldbDriver.getTrendingGenres();
                        exited = manageGenrePage(mysqldbDriver, genres, "Trending genres", user);
                    } else if (choice1 == 2) {
                        List<Genre> genres = mysqldbDriver.getPopularGenres();
                        exited = manageGenrePage(mysqldbDriver, genres, "Popular genres", user);

                    } else if (choice1 == 3) {
                        List<Genre> genres = mysqldbDriver.getGenres();
                        exited = manageGenrePage(mysqldbDriver, genres, "All genres", user);
                    } else if (choice1 == 4) {
                        System.out.println("Search by author name");
                        System.out.println("1.Back");
                        System.out.print("Enter: ");

                        String dummy = scanner.nextLine();
                        String name = scanner.nextLine();
                        if (!name.equals("1")) {
                            List<Genre> genres = mysqldbDriver.getGenresByName(name);

                            if (genres.isEmpty()) {
                                System.out.println("Sorry! We couldn't find any genre named '" + name + "'");
                            } else {
                                exited = manageGenrePage(mysqldbDriver, genres, "Results based on searching by: " + name, user);
                            }
                        }

                    } else if (choice1 == 5) {
                        back1 = true;
                    } else if (choice1 == 6) {
                        exited = true;
                    } else {
                        System.out.println("Invalid command. Please type again");
                    }
                }
            } else if (choice == 4) {
                List<Book> books = mysqldbDriver.getEBooks();
                exited = manageBooksPage(mysqldbDriver, books, "E-Books", user);
            } else if (choice == 5) {
                List<Book> books = mysqldbDriver.getOfferedBooks();
                exited = manageBooksPage(mysqldbDriver, books, "Special offers books", user);
            } else if (choice == 6) {
                List<Book> books = mysqldbDriver.getTrendingBooks();
                exited = manageBooksPage(mysqldbDriver, books, "Trending books", user);
            } else if (choice == 7) {
                List<Book> books = mysqldbDriver.getBestSellingBooks();
                exited = manageBooksPage(mysqldbDriver, books, "Bestselling books", user);
            } else if (choice == 8) {
                List<Book> books = mysqldbDriver.getLatestBooks();
                exited = manageBooksPage(mysqldbDriver, books, "Latest books", user);
            } else if (choice == 9) {
                System.out.println("Search by book name");
                System.out.println("1.Back");
                System.out.print("Enter: ");

                String dummy = scanner.nextLine();
                String name = scanner.nextLine();

                if (!name.equals("1")) {
                    System.out.println(name);
                    List<Book> books = mysqldbDriver.getBooksByName(name);

                    if (books.isEmpty()) {
                        System.out.println("Sorry! We couldn't find any book named '" + name + "'");
                    } else {
                        exited = manageBooksPage(mysqldbDriver, books, "Results based on searching by: " + name, user);
                    }
                }
            } else if (choice == 10) {
                List<Book> books = mysqldbDriver.getBooksBasedOnPrice(true);
                exited = manageBooksPage(mysqldbDriver, books, "Books based on price: high to low", user);
            } else if (choice == 11) {
                List<Book> books = mysqldbDriver.getBooksBasedOnPrice(false);
                exited = manageBooksPage(mysqldbDriver, books, "Books based on price: low to high", user);
            } else if (choice == 12) {
                exited = manageMyCart(mysqldbDriver, user);
            } else if (choice == 13) {
                boolean back1 = false;

                while(!back1 && !exited) {
                    System.out.println("1.All orders");
                    System.out.println("2.Shipped orders");
                    System.out.println("3.Unshipped orders");
                    System.out.println("4.Back");
                    System.out.println("5.Exit");
                    System.out.print("Enter: ");

                    int choice1 = scanner.nextInt();

                    if (choice1 == 1) {
                        List<OrderHistory> orderHistories = mysqldbDriver.getOrderHistoryAll(user.getUserId());
                        exited = manageOrderHistoryPage(orderHistories, "All orders");
                    } else if (choice1 == 2) {
                        List<OrderHistory> orderHistories = mysqldbDriver.getOrderHistoryShipped(user.getUserId());
                        exited = manageOrderHistoryPage(orderHistories, "Shipped orders");
                    } else if (choice1 == 3) {
                        List<OrderHistory> orderHistories = mysqldbDriver.getOrderHistoryNotShipped(user.getUserId());
                        exited = manageOrderHistoryPage(orderHistories, "Unshipped orders");
                    } else if (choice1 == 4) {
                        back1 = true;
                    } else if (choice1 == 5) {
                        exited = true;
                    } else {
                        System.out.println("Invalid command. Please type again");
                    }
                }
            } else if (choice == 14) {
                back = true;
            } else if (choice == 15) {
                exited = true;
            } else {
                System.out.println("Invalid command. Please type again");
            }
        }

        return exited;
    }

    public static boolean manageAuthorsPage(MysqldbDriver mysqldbDriver, List<Author> authors, String title, User user) {
        boolean back = false;
        boolean exited = false;

        while (!back && !exited) {
            System.out.println(title);
            for (int i = 0; i < authors.size(); i++) {
                System.out.println(i + 1 + "." + authors.get(i).getName());
            }
            System.out.println(authors.size() + 1 + ".Back");
            System.out.println(authors.size() + 2 + ".Exit");
            System.out.print("Enter: ");

            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();

            if (choice >= 1 && choice <= authors.size()) {
                exited = showAuthorsDetails(mysqldbDriver, authors.get(choice - 1), user);
            } else if (choice == authors.size() + 1) {
                back = true;
            } else if (choice == authors.size() + 2) {
                exited = true;
            }
        }

        return exited;
    }

    public static boolean showAuthorsDetails(MysqldbDriver mysqldbDriver, Author author, User user) {
        List<Book> books = mysqldbDriver.getAuthorBooks(author.getAuthorId());

        boolean back = false;
        boolean exited = false;

        while (!back && !exited) {
            System.out.println("Author Details");
            System.out.println("Name: " + author.getName());
            System.out.println("Birthdate: " + author.getBirthDate());
            System.out.println("Country: " + author.getCountry());
            System.out.println("Books: ");

            for (int i = 0; i < books.size(); i++) {
                System.out.println(i + 1 + "." + books.get(i).getTitle());
            }
            System.out.println(books.size() + 1 + ".Back");
            System.out.println(books.size() + 2 + ".Exit");
            System.out.print("Enter: ");

            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();

            if (choice >= 1 && choice <= books.size()) {
                exited = showBooksDetails(mysqldbDriver, books.get(choice - 1), user);
            } else if (choice == books.size() + 1) {
                back = true;
            } else if (choice == books.size() + 2) {
                exited = true;
            }
        }

        return exited;
    }

    public static boolean manageBooksPage(MysqldbDriver mysqldbDriver, List<Book> books, String title, User user) {
        boolean back = false;
        boolean exited = false;

        while (!back && !exited) {
            System.out.println(title);
            if(!books.isEmpty()) {
                for (int i = 0; i < books.size(); i++) {
                    System.out.println(i + 1 + "." + books.get(i).getTitle());
                }
            } else {
                System.out.println("Oops! The list is empty");
            }
            System.out.println(books.size() + 1 + ".Back");
            System.out.println(books.size() + 2 + ".Exit");
            System.out.print("Enter: ");

            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();

            if (choice >= 1 && choice <= books.size()) {
                exited = showBooksDetails(mysqldbDriver, books.get(choice - 1), user);
            } else if (choice == books.size() + 1) {
                back = true;
            } else if (choice == books.size() + 2) {
                exited = true;
            }
        }

        return exited;
    }

    public static boolean showBooksDetails(MysqldbDriver mysqldbDriver, Book book, User user) {
        boolean back = false;
        boolean exited = false;

        List<Genre> genres = mysqldbDriver.getBookGenres(book.getBookId());
        List<Author> authors = mysqldbDriver.getBookAuthors(book.getBookId());
        Publisher publisher = mysqldbDriver.getBookPublisher(book.getPublisherId());

        while (!back && !exited) {
            System.out.println("Book details");
            System.out.println("Title: " + book.getTitle());

            System.out.print("Authors: ");
            for(int i = 0; i < authors.size(); i++) {
                System.out.print(authors.get(i).getName());
                if(i < authors.size() - 1) {
                    System.out.print(", ");
                }
                else {
                    System.out.print('\n');
                }
            }

            System.out.println("publisher: " + publisher.getName());

            System.out.print("Genres: ");
            for(int i = 0; i < genres.size(); i++) {
                System.out.print(genres.get(i).getName());
                if(i < genres.size() - 1) {
                    System.out.print(", ");
                }
                else {
                    System.out.print('\n');
                }
            }

            System.out.println("Language: " + book.getLanguage());
            System.out.println("Pages: " + book.getNumberOfPages());
            System.out.println(book.getFormat());
            if(book.getStockQuantity() > 0) {
                System.out.print("In stock(");
                if(book.getStockQuantity() > 50) {
                    System.out.println("50+ copies available)");
                } else {
                    System.out.println(book.getStockQuantity() + " copies available)");
                }
            } else {
                System.out.println("Out of stock");
            }

            int sellsCount = mysqldbDriver.getBookSellsCount(book.getBookId());

            if(sellsCount > 0) {
                System.out.println("Already " + sellsCount + " copies sold");
            }

            int price = (int) (book.getPrice() * (100 -book.getDiscount()) / 100);

            System.out.print("Price: TK." + price);
            if(book.getDiscount() > 0) {
                System.out.print("(After " + book.getDiscount() + "% discount)");
            }
            System.out.print("\n");

            if(book.isEbookAvailable()) {
                System.out.print("E-book available(Price: TK.");
                int ebookPrice = price * 30 / 100;
                System.out.println(ebookPrice + ")");
            } else {
                System.out.println("E-book unavailable");
            }

            System.out.println("1.Add to cart");
            System.out.println("2.Add to cart(E-book)");
            System.out.println("3.Books frequently bought together");
            System.out.println("4.Related books");
            System.out.println("5.Back");
            System.out.println("6.Exit");
            System.out.print("Enter: ");

            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();

            if(choice == 1) {
                System.out.print("Quantity: ");
                int quantity = scanner.nextInt();

                exited = addToCart(mysqldbDriver, user, book, price, quantity);

            } else if (choice == 2) {
                System.out.print("Quantity: ");
                int quantity = scanner.nextInt();

                exited = addToCart(mysqldbDriver, user, book, price * 30 / 100, quantity);

            } else if (choice == 3) {
                List<Book> books = mysqldbDriver.getBooksFrequentlyBoughtTogether(book.getBookId());
                exited = manageBooksPage(mysqldbDriver, books, "Books frequently bought together", user);
            } else if (choice == 4) {
                List<Book> books = new ArrayList<>();
                for(Author auth : authors) {
                    books.addAll(mysqldbDriver.getAuthorBooks(auth.getAuthorId()));
                }
                for(Genre gen : genres) {
                    books.addAll(mysqldbDriver.getGenreBooks(gen.getGenreId()));
                }
                Collections.shuffle(books);

                exited = manageBooksPage(mysqldbDriver, books, "Related books", user);
            } else if (choice == 5) {
                back = true;
            } else if (choice == 6) {
                exited = true;
            } else {

            }
        }

        return exited;
    }

    public static boolean addToCart(MysqldbDriver mysqldbDriver, User user, Book book, int price, int quantity) {
        if(mysqldbDriver.getStockQuantity(book.getBookId()) < quantity) {
            System.out.println("Sorry. Currently we don't have " + quantity + " copies available of the book");
        }
        else {
            String orderId = mysqldbDriver.getCartOrderId(user);

            if (orderId == null) {
                mysqldbDriver.addNewOrder(user, price * quantity);
                mysqldbDriver.addToCart(mysqldbDriver.getCartOrderId(user), book.getBookId(), quantity, price * quantity);
                System.out.println("Added to cart successfully");
            } else {
                if (mysqldbDriver.addToCart(orderId, book.getBookId(), quantity, price * quantity)) {
                    mysqldbDriver.updateOrderPrice(orderId, price * quantity, true);
                    System.out.println("Added to cart successfully");
                }
            }
        }

        boolean back = false;
        boolean exited = false;

        while(!back && !exited) {
            System.out.println("1.Show my cart");
            System.out.println("2.Back");
            System.out.println("3.Exit");
            System.out.print("Enter: ");

            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();

            if(choice == 1) {
                exited = manageMyCart(mysqldbDriver, user);
            } else if(choice == 2) {
                back = true;
            } else if (choice == 3) {
                exited = true;
            }
        }

        return exited;
    }

    public static boolean manageMyCart(MysqldbDriver mysqldbDriver, User user) {
        List<CartBook> cartBooks = mysqldbDriver.getCartBooks(user);

        boolean back = false;
        boolean exited = false;

        while(!back && !exited) {
            if(!cartBooks.isEmpty()) {
                int cnt = 1;
                System.out.println("Book name - Quantity - price");
                int total_price = 0;
                for(CartBook item : cartBooks) {
                    System.out.println(cnt++ + ". " + item.getName() + " - " + item.getQuantity() + " - TK." + item.getPrice());
                    total_price += item.getPrice();
                }
                System.out.println("Total price: TK." + total_price);

                System.out.println("1.Place order");
                System.out.println("2.Delete from cart");
                System.out.println("3.Back");
                System.out.println("4.Exit");
                System.out.print("Enter: ");

                Scanner scanner = new Scanner(System.in);
                int choice = scanner.nextInt();

                if(choice == 1) {
                    boolean flag = false;
                    for(int i = 0; i < cartBooks.size(); i++) {
                        if(cartBooks.get(i).getBookStock() < cartBooks.get(i).getQuantity()) {
                            System.out.println("Sorry. Currently we don't have " + cartBooks.get(i).getQuantity() + " copies available of the book '" + cartBooks.get(i).getName() + "'");
                            flag = true;
                        }
                    }

                    if(flag) {
                        System.out.println("Please update your cart to place order");
                    }
                    else {
                        System.out.print("Address: ");
                        String dummy = scanner.nextLine();
                        String address = scanner.nextLine();
                        System.out.print("Phone number: ");
                        String pno = scanner.nextLine();
                        System.out.println("Choose payment method: 1.Cash 2.Online");
                        System.out.print("Enter: ");
                        int pmtMethod = scanner.nextInt();
                        System.out.println("Confirm order? 1.Yes 2.No");
                        System.out.print("Enter: ");
                        int confirmation = scanner.nextInt();

                        if (confirmation == 1) {
                            for(CartBook item : cartBooks) {
                                mysqldbDriver.updateBookQuantity(item.getBookId(), item.getQuantity());
                            }
                            String orderId = mysqldbDriver.getCartOrderId(user);
                            if (pmtMethod == 1) {
                                mysqldbDriver.placeOrder(orderId, address, pno, "Pending", "Cash");
                            } else {
                                mysqldbDriver.placeOrder(orderId, address, pno, "Paid", "Online");
                            }
                            System.out.println("Order successful");
                            back = true;
                        }
                    }
                } else if (choice == 2) {
                    System.out.print("Enter the number of the book you want to delete: ");
                    int del = scanner.nextInt();

                    if(del >= 1 && del <= cartBooks.size()) {
                        String orderId = mysqldbDriver.getCartOrderId(user);
                        mysqldbDriver.deleteFromCart(orderId, cartBooks.get(del - 1).getBookId());
                        total_price -= cartBooks.get(del - 1).getPrice();

                        if (total_price == 0) {
                            mysqldbDriver.deleteFromOrders(orderId);
                        } else {
                            mysqldbDriver.updateOrderPrice(orderId, cartBooks.get(del - 1).getPrice(), false);
                        }

                        cartBooks.remove(del - 1);

                        System.out.println("Delete successful");
                    } else {
                        System.out.println("Delete unsuccessful");
                    }

                } else if (choice == 3) {
                    back = true;
                } else if (choice == 4) {
                    exited = true;
                } else {
                    System.out.println("Invalid command. Please type again");
                }
            } else {
                System.out.println("Cart is empty");
                System.out.println("1.Back");
                System.out.println("2.Exit");
                System.out.print("Enter: ");

                Scanner scanner = new Scanner(System.in);
                int choice = scanner.nextInt();

                if (choice == 1) {
                    back = true;
                } else if (choice == 2) {
                    exited = true;
                } else {
                    System.out.println("Invalid command. Please type again");
                }
            }
        }

        return exited;
    }

    public static boolean managePublishersPage(MysqldbDriver mysqldbDriver, List<Publisher> publishers, String title, User user) {
        boolean back = false;
        boolean exited = false;

        while (!back && !exited) {
            System.out.println(title);
            for (int i = 0; i < publishers.size(); i++) {
                System.out.println(i + 1 + "." + publishers.get(i).getName());
            }
            System.out.println(publishers.size() + 1 + ".Back");
            System.out.println(publishers.size() + 2 + ".Exit");
            System.out.print("Enter: ");

            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();

            if (choice >= 1 && choice <= publishers.size()) {
                exited = showPublisherDetails(mysqldbDriver, publishers.get(choice - 1), user);
            } else if (choice == publishers.size() + 1) {
                back = true;
            } else if (choice == publishers.size() + 2) {
                exited = true;
            }
        }

        return exited;
    }

    public static boolean showPublisherDetails(MysqldbDriver mysqldbDriver, Publisher publisher, User user) {
        List<Book> books = mysqldbDriver.getPublisherBooks(publisher.getPublisherId());

        boolean back = false;
        boolean exited = false;

        while (!back && !exited) {
            System.out.println("Publisher Details");
            System.out.println("Name: " + publisher.getName());
            System.out.println("Established: " + publisher.getEstablishedDate());
            System.out.println("Country: " + publisher.getCountry());
            System.out.println("Books: ");

            for (int i = 0; i < books.size(); i++) {
                System.out.println(i + 1 + "." + books.get(i).getTitle());
            }
            System.out.println(books.size() + 1 + ".Back");
            System.out.println(books.size() + 2 + ".Exit");
            System.out.print("Enter: ");

            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();

            if (choice >= 1 && choice <= books.size()) {
                exited = showBooksDetails(mysqldbDriver, books.get(choice - 1), user);
            } else if (choice == books.size() + 1) {
                back = true;
            } else if (choice == books.size() + 2) {
                exited = true;
            }
        }

        return exited;
    }

    public static boolean manageGenrePage(MysqldbDriver mysqldbDriver, List<Genre> genres, String title, User user) {
        boolean back = false;
        boolean exited = false;

        while (!back && !exited) {
            System.out.println(title);
            for (int i = 0; i < genres.size(); i++) {
                System.out.println(i + 1 + "." + genres.get(i).getName());
            }
            System.out.println(genres.size() + 1 + ".Back");
            System.out.println(genres.size() + 2 + ".Exit");
            System.out.print("Enter: ");

            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();

            if (choice >= 1 && choice <= genres.size()) {
                exited = showGenreDetails(mysqldbDriver, genres.get(choice - 1), user);
            } else if (choice == genres.size() + 1) {
                back = true;
            } else if (choice == genres.size() + 2) {
                exited = true;
            }
        }

        return exited;
    }

    public static boolean showGenreDetails(MysqldbDriver mysqldbDriver, Genre genre, User user) {
        List<Book> books = mysqldbDriver.getGenreBooks(genre.getGenreId());

        boolean back = false;
        boolean exited = false;

        while (!back && !exited) {
            System.out.println("Books in '" + genre.getName() + "' genre");

            for (int i = 0; i < books.size(); i++) {
                System.out.println(i + 1 + "." + books.get(i).getTitle());
            }
            System.out.println(books.size() + 1 + ".Back");
            System.out.println(books.size() + 2 + ".Exit");
            System.out.print("Enter: ");

            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();

            if (choice >= 1 && choice <= books.size()) {
                exited = showBooksDetails(mysqldbDriver, books.get(choice - 1), user);
            } else if (choice == books.size() + 1) {
                back = true;
            } else if (choice == books.size() + 2) {
                exited = true;
            }
        }

        return exited;
    }

    public static boolean manageOrderHistoryPage(List<OrderHistory> orderHistories, String title) {
        boolean back = false;
        boolean exited = false;

        while (!back && !exited) {
            System.out.println(title);
            System.out.println("Order id - Order date - Book name - Quantity");
            for (int i = 0; i < orderHistories.size(); i++) {
                System.out.println(orderHistories.get(i).getOrderId() + " - " + orderHistories.get(i).getOrder_date() + " - " + orderHistories.get(i).getBookName() + " - " + orderHistories.get(i).getQuantity() + " pcs");
            }
            System.out.println("1.Back");
            System.out.println("2.Exit");
            System.out.print("Enter: ");

            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();

            if (choice == 1) {
                back = true;
            } else if (choice == 2) {
                exited = true;
            }
        }

        return exited;
    }
}