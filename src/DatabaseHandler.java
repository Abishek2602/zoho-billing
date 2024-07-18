import java.sql.*;

public class DatabaseHandler {
    private Connection con;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public DatabaseHandler() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");  
            con=DriverManager.getConnection("jdbc:mysql://localhost:3306/abishek","root","Abishek@2602");  
            System.out.println("Database connection established successfully.");
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver class not found. Ensure you have added the JDBC library to your project.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Failed to establish a database connection. Please check the connection details.");
            e.printStackTrace();
        }
    }
    

    public void insertCustomer(long phn_no, String name) {
        try {
            String sql = "INSERT INTO customer (c_phn_no, c_name) VALUES (?, ?)";
            preparedStatement = con.prepareStatement(sql);
            preparedStatement.setLong(1, phn_no);
            preparedStatement.setString(2, name);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getCustomerName(long phn_no) {
        try {
            String sql = "SELECT c_name FROM customer WHERE c_phn_no = ?";
            preparedStatement = con.prepareStatement(sql);
            preparedStatement.setLong(1, phn_no);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("c_name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public int getNextBillId(){
        try{
        String sql="select b_id from bill order by b_id desc limit 1";
        preparedStatement = con.prepareStatement(sql);
        resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getInt("b_id");
        }   catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void createBill(int b_id, long phn_no, String date) {
        try {
            String sql = "INSERT INTO bill (b_id, c_phn_no, b_date, tot_price, payment_status) VALUES (?, ?, ?, ?, ?)";
            preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, b_id);
            preparedStatement.setLong(2, phn_no);
            preparedStatement.setDate(3, java.sql.Date.valueOf(date));
            preparedStatement.setInt(4, 0);
            preparedStatement.setString(5, "up");
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertBillItem(int b_id, int p_id, int qty) {
        try {
            String sql = "SELECT price FROM product WHERE p_id = ?";
            preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, p_id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int price = resultSet.getInt("price");
                sql = "INSERT INTO bill_items (b_id, p_id, qty, p_price) VALUES (?, ?, ?, ?)";
                preparedStatement = con.prepareStatement(sql);
                preparedStatement.setInt(1, b_id);
                preparedStatement.setInt(2, p_id);
                preparedStatement.setInt(3, qty);
                preparedStatement.setInt(4, qty * price);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateTotalPrice(int b_id) {
        try {
            String sql = "SELECT SUM(p_price) AS total FROM bill_items WHERE b_id = ?";
            preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, b_id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int totalPrice = resultSet.getInt("total");
                sql = "UPDATE bill SET tot_price = ? WHERE b_id = ?";
                preparedStatement = con.prepareStatement(sql);
                preparedStatement.setInt(1, totalPrice);
                preparedStatement.setInt(2, b_id);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void printBill(int b_id) {
        try {
            String sql = "SELECT b.b_id, b.c_phn_no, b.b_date, b.tot_price, c.c_name FROM bill b natural join customer c where b_id=?";
            preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, b_id);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            int bId = resultSet.getInt("b.b_id");
            long cPhnNo = resultSet.getLong("b.c_phn_no");
            java.sql.Date bDate = resultSet.getDate("b.b_date");
            int totPrice = resultSet.getInt("b.tot_price");
            String Cname=resultSet.getString("c.c_name");
            System.out.println("\n\n\t\t\tCustomer Name : "+Cname+"\t\t\t\tBill id : "+bId);
            System.out.println("\t\t\tCustomer phone number : "+cPhnNo+"\t\tDate : "+bDate);
            sql = "SELECT bi.p_id, p.p_name, p.price, bi.qty, bi.p_price " +"FROM bill_items bi " +"NATURAL JOIN product p " +"WHERE bi.b_id = ?";
            preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, b_id); // Setting the parameter
            resultSet = preparedStatement.executeQuery();
            System.out.printf("\n\n\t\t\t%-10s %-20s %-10s %-10s %-10s%n", "Product ID", "Product Name", "Price", "Quantity", "Total Price");
            System.out.println("\t\t\t---------------------------------------------------------------");
            while (resultSet.next()) {
                int productId = resultSet.getInt("bi.p_id");
                String productName = resultSet.getString("p.p_name");
                int productPrice = resultSet.getInt("bi.p_price");
                int quantity = resultSet.getInt("bi.qty");
                int totalPrice = resultSet.getInt("p.price");

                System.out.printf("\t\t\t%-10d %-20s %-10d %-10d %-10d%n", productId, productName, totalPrice, quantity, productPrice);
            }
            System.out.println("\t\t\t---------------------------------------------------------------");
            System.out.println("\t\t\t\t\t\t\t\tBill Amount : "+totPrice);
            System.out.println("\t\t\t---------------------------------------------------------------");        
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Implement the remaining methods for each case
    public void reportBetweenDates(String fromDate, String toDate) {
        try {
            String sql = "SELECT * FROM bill WHERE b_date BETWEEN ? AND ?";
            preparedStatement = con.prepareStatement(sql);
            preparedStatement.setDate(1, java.sql.Date.valueOf(fromDate));
            preparedStatement.setDate(2, java.sql.Date.valueOf(toDate));
            resultSet = preparedStatement.executeQuery();
            printBillReport(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void reportOnDate(String date) {
        try {
            String sql = "SELECT * FROM bill WHERE b_date = ?";
            preparedStatement = con.prepareStatement(sql);
            preparedStatement.setDate(1, java.sql.Date.valueOf(date));
            resultSet = preparedStatement.executeQuery();
            printBillReport(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void reportOnMonth(int month) {
        try {
            String sql = "SELECT * FROM bill WHERE MONTH(b_date) = ?";
            preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, month);
            resultSet = preparedStatement.executeQuery();
            printBillReport(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void reportOnYear(int year) {
        try {
            String sql = "SELECT * FROM bill WHERE YEAR(b_date) = ?";
            preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, year);
            resultSet = preparedStatement.executeQuery();
            printBillReport(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void reportOnCustomer(long phn_no) {
        try {
            String sql = "SELECT * FROM bill WHERE c_phn_no = ?";
            preparedStatement = con.prepareStatement(sql);
            preparedStatement.setLong(1, phn_no);
            resultSet = preparedStatement.executeQuery();
            printBillReport(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void reportOnProduct(int pid) {
        try {
            String sql ="select b.b_id,b.b_date,b.c_phn_no,bi.qty,bi.p_price from (bill b natural join bill_items bi) natural join product p where p.p_id=bi.p_id and b.b_id=bi.b_id and bi.p_id=?";
            preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, pid);
            resultSet = preparedStatement.executeQuery();
            printProductReport(resultSet,pid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void generateInvoice(int b_id) {
        try {
            String sql = "SELECT * FROM bill WHERE b_id = ?";
            preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, b_id);
            resultSet = preparedStatement.executeQuery();
            printBillReport(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void reportOnBillAmount(int amount) {
        try {
            String sql = "SELECT * FROM bill WHERE tot_price > ?";
            preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, amount);
            resultSet = preparedStatement.executeQuery();
            printBillReport(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertProduct(int pid, String pname, int price) {
        try {
            String sql = "INSERT INTO product (p_id, p_name, price) VALUES (?, ?, ?)";
            preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, pid);
            preparedStatement.setString(2, pname);
            preparedStatement.setInt(3, price);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void listProducts() {
        try {
            String sql = "SELECT * FROM product";
            preparedStatement = con.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            System.out.printf("\n\n\t\t\t%-10s %-20s %-10s%n", "Product ID", "Product Name", "Price");
            System.out.println("\t\t\t-------------------------------------------");
            while (resultSet.next()) {
                int pid = resultSet.getInt("p_id");
                String pname = resultSet.getString("p_name");
                int price = resultSet.getInt("price");
                System.out.printf("\t\t\t%-10d %-20s %-10d%n", pid, pname, price);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void listCustomerDetails() {
        try {
            String sql = "SELECT * FROM customer";
            preparedStatement = con.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            System.out.printf("\n\n\t\t\t%-20s %-20s%n", "Phone Number", "Customer Name");
            System.out.println("\t\t\t-------------------------------------------");
            while (resultSet.next()) {
                long phn_no = resultSet.getLong("c_phn_no");
                String name = resultSet.getString("c_name");
                System.out.printf("\t\t\t%-20d %-20s%n", phn_no, name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void listAllBills() {
        try {
            String sql = "SELECT * FROM bill";
            preparedStatement = con.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            printBillReport(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void listPaidBills() {
        try {
            String sql = "SELECT * FROM bill WHERE payment_status='p'";
            preparedStatement = con.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            printBillReport(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void customerBalance() {
        try {
            String sql = "select upper(c.c_name) as c_name,b.c_phn_no,sum(tot_price) as customer_balance from bill b natural join customer c where payment_status='up' group by c_phn_no";
            preparedStatement=con.prepareCall(sql);
            resultSet=preparedStatement.executeQuery();
            System.out.printf("\n\n\t\t\t%-20s %-15s %-15s%n", "Customer Name", "Phone Number", "Customer Balance");
            System.out.println("\t\t\t---------------------------------------------------------");
            while (resultSet.next()) {
                String customerName = resultSet.getString("c_name");
                long phoneNumber = resultSet.getLong("c_phn_no");
                int customerBalance = resultSet.getInt("customer_balance");

                System.out.printf("\t\t\t%-20s %-15d %-15d%n", customerName, phoneNumber, customerBalance);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateBill(int b_id) {
        try {
            updateTotalPrice(b_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printBillReport(ResultSet resultSet) throws SQLException {
        System.out.printf("\n\n\t\t\t%-10s %-20s %-20s %-20s%n", "Bill ID", "Customer Phone", "Date", "Total Price");
        System.out.println("\t\t\t-----------------------------------------------------------");
        while (resultSet.next()) {
            int b_id = resultSet.getInt("b_id");
            long phn_no = resultSet.getLong("c_phn_no");
            String date = resultSet.getString("b_date");
            int totalPrice = resultSet.getInt("tot_price");
            System.out.printf("\t\t\t%-10d %-20d %-20s %-20d%n", b_id, phn_no, date, totalPrice);
        }
    }

    private void printProductReport(ResultSet resultSet,int pid) throws SQLException {
        String sql1="select p_name,price from product p where p_id=?";
        preparedStatement=con.prepareStatement(sql1);
        preparedStatement.setInt(1, pid);
        ResultSet resultSet1=preparedStatement.executeQuery();
        while(resultSet1.next()){
                String n=resultSet1.getString("p_name");
                int p=resultSet1.getInt("price");
                System.out.print("\n\t\t\tProduct Name : "+n+"\t\t\t\t\t  Product Price : ");
                System.out.print(p+"\n\n");
        }
        System.out.print("\n\t\t\tBill id         Billing date\t\tCustomer phn no         Quantity      \t\tPrice on Bill\n\n");
        while(resultSet.next()){
                int bId = resultSet.getInt("b.b_id");
                java.sql.Date bDate = resultSet.getDate("b_date");
                long cPhnNo = resultSet.getLong("b.c_phn_no");
                int qty = resultSet.getInt("bi.qty");
                int price = resultSet.getInt("bi.p_price");
                System.out.print("\t\t\t"+bId+"\t\t");
                System.out.print(bDate+"\t\t");
                System.out.print(cPhnNo+"\t\t");
                System.out.print(qty+"\t\t\t");
                System.out.print(price+"\n");
        }
    }

    public void updatePaymentStatus(int b_id){
        try{
        String sql="update bill set payment_status='p' where b_id="+b_id;
        preparedStatement=con.prepareStatement(sql);
        preparedStatement.executeUpdate();
        System.out.println("\n\t\t\tBill id "+b_id+" is Updated to Paid");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
