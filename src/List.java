import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.Scanner;

public class List {
    private String URL = "jdbc:mysql://localhost:3306/goodsalary";
    private String name = "root";
    private String password = "556677";
    private String sql = "INSERT INTO list (id,name,amount) VALUES (?,?,?)";
    private String nameProduct;
    private int amountProduct;

    public void goList() {
        boolean isEXIT = true;
        while (isEXIT) {
            Scanner console = new Scanner(System.in);
            System.out.println("Чтобы добавить продукт нажмите 1: \n" +
                    "Что бы очистить список нажмите 2:\n" +
                    "Что бы показать список продуктов нажмите 3:\n" +
                    "Что бы записать таблицу в текстовый файл нажмите 4:\n" +
                    "Что бы выйти нажмите 5:");
            switch (console.nextInt()) {
                case 1:
                    setNameProduct();
                    SetAmountProduct();
                    addList();
                    break;
                case 2:
                    DeleteList();
                    break;
                case 3:
                    showList();
                    break;
                case 4:
                    WritingToFile();
                    break;
                case 5:
                    isEXIT = false;
                    break;
            }
        }
    }

    private void addList() {
        try (Connection connection = DriverManager.getConnection(URL, name, password);) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, MaxListId());
            preparedStatement.setString(2, nameProduct);
            preparedStatement.setInt(3, amountProduct);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showList() {
        try (Connection connection = DriverManager.getConnection(URL, name, password);
             Statement statement = connection.createStatement()) {
            ResultSet set = statement.executeQuery("SELECT * FROM list");
            System.out.println("Id товара\t\tНазвание\t\tКолличество\n");
            while (set.next()) {
                System.out.print(set.getInt("id"));
                System.out.print("\t\t\t\t");
                System.out.print(set.getString("name"));
                System.out.print("\t\t\t\t");
                System.out.println(set.getInt("amount"));
                System.out.println("-".repeat(45));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void DeleteList() {
        try (Connection connection = DriverManager.getConnection(URL, name, password);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM list");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setNameProduct() {
        Scanner scannerName = new Scanner(System.in);
        System.out.println("Введите название продукта");
        nameProduct = scannerName.nextLine();
    }

    public void SetAmountProduct() {
        Scanner scannerAmount = new Scanner(System.in);
        System.out.println("Введите колличество продукта");
        amountProduct = scannerAmount.nextInt();
    }

    private int MaxListId() {
        int x = 0;
        try (Connection connection = DriverManager.getConnection(URL, name, password);
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT MAX(list.id) FROM list");
            while (resultSet.next()) {
                x = resultSet.getInt(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        System.out.println(x);
        return x + 1;
    }
    private void  WritingToFile(){
        try (Connection connection=DriverManager.getConnection(URL,name,password);
             Statement statement= connection.createStatement()){
            ResultSet set=statement.executeQuery("SELECT * FROM list");
            FileWriter fileWriter=new FileWriter("D:\\Projekt JAVA\\SALARY\\src\\Result");
            fileWriter.write("Id товара\t\tНазвание\t\tКолличество\n");
            while (set.next()) {
                fileWriter.write(set.getInt("id")
                        +"\t\t\t\t"+set.getString("name")
                        +"\t\t\t\t"+set.getInt("amount")+"\n"+"-".repeat(40)+"\n");
            }
            System.out.println("Данные успешно сохранены");
            fileWriter.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}



