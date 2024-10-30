import java.sql.*;
import java.util.Scanner;

public class Main {

    private static final String URL = "jdbc:mysql://Masha_pc:3360/library";
    private static final String USERNAME = "Meme"; //создала нового юзера с таким ником и паролем
    private static final String PASSWORD = "lol";

    private static Scanner scanner = new Scanner(System.in);
    private static Connection conn;

    public static void main(String[] args) {
        // Подключение к базе данных
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            System.out.println("Успешно подключен к базе данных.");

            Main.conn = conn;

            while (true) {
                printMenu();
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        addBook();
                        break;
                    case 2:
                        listAllBooks();
                        break;
                    case 3:
                        updateBook();
                        break;
                    case 4:
                        deleteBook();
                        break;
                    case 5:
                        System.out.println("Выход");
                        return;
                    default:
                        System.out.println("Ошибка! Неверный выбор!");
                        break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void printMenu() {
        System.out.println("Меню:");
        System.out.println("1. Добавить книгу!");
        System.out.println("2. Перечислить все книги");
        System.out.println("3. Обновить книгу");
        System.out.println("4. Удалить книгу");
        System.out.println("5. Выход");
        System.out.print("Введите свой выбор: ");
    }

    private static void addBook() throws SQLException {
        System.out.print("Введите название: ");
        String title = scanner.nextLine();
        System.out.print("Введите автора: ");
        String author = scanner.nextLine();
        System.out.print("Введите год издания: ");
        int year = Integer.parseInt(scanner.nextLine());

        String query = "INSERT INTO books (title, author, year) VALUES (?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, title);
            statement.setString(2, author);
            statement.setInt(3, year);
            statement.executeUpdate();
            System.out.println("Новая книга успешно добавлена");
        }
    }

    private static void listAllBooks() throws SQLException {
        String query = "SELECT * FROM books";
        try (Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            // Цикл для вывода всех книг
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                int year = resultSet.getInt("year");
                System.out.printf("%d. %s by %s (%d)\n", id, title, author, year);
            }
        }
    }

    private static void updateBook() throws SQLException {
        System.out.print("ID книги для обновления: ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.print("Название: ");
        String title = scanner.nextLine();
        System.out.print("Автор: ");
        String author = scanner.nextLine();
        System.out.print("Год: ");
        int year = Integer.parseInt(scanner.nextLine());

        String query = "UPDATE books SET title = ?, author = ?, year = ? WHERE id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, title);
            statement.setString(2, author);
            statement.setInt(3, year);
            statement.setInt(4, id);
            statement.executeUpdate();
            System.out.println("Книга успешно обновлена.");
        }
    }

    private static void deleteBook() throws SQLException {
        System.out.print("ID книги для удаления: ");
        int id = Integer.parseInt(scanner.nextLine());

        String query = "DELETE FROM books WHERE id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
            System.out.println("Книга успешно удалена");
        }
    }
}