import java.util.Scanner;

public class BillingSystem{
    private final DatabaseHandler dbHandler;
    private Scanner in;

    public BillingSystem() {
        dbHandler = new DatabaseHandler();
        in = new Scanner(System.in);
    }

    public static void main(String[] args) {
        BillingSystem app = new BillingSystem();
        app.run();
    }

    public void run() {
        System.out.println("\n\n\t\t\tZOHO BILLING SYSTEM");
        int choice = 0;
        while (true) {
            System.out.println("\n\n\t\t\t1.Create a bill\n\t\t\t2.Report based on time\n\t\t\t3.Report based on Customer\n\t\t\t4.Report based on product\n\t\t\t5.Generate an Invoice\n\t\t\t6.Report based on bill amount\n\t\t\t7.Insert a Product or List products\n\t\t\t8.List Customer details\n\t\t\t9.List All Bills\n\t\t\t10.Paid Bills\n\t\t\t11.Customer Balance\n\t\t\t12.Update the Bill\n\t\t\t13.Exit\n\n\n");
            System.out.print("\t\t\tYour choice : ");
            choice = in.nextInt();
            switch (choice) {
                case 1:
                    createBill();
                    break;
                case 2:
                    reportBasedOnTime();
                    break;
                case 3:
                    reportBasedOnCustomer();
                    break;
                case 4:
                    reportBasedOnProduct();
                    break;
                case 5:
                    generateInvoice();
                    break;
                case 6:
                    reportBasedOnBillAmount();
                    break;
                case 7:
                    insertOrListProducts();
                    break;
                case 8:
                    listCustomerDetails();
                    break;
                case 9:
                    listAllBills();
                    break;
                case 10:
                    listPaidBills();
                    break;
                case 11:
                    customerBalance();
                    break;
                case 12:
                    updateBill();
                    break;
                case 13:
                    return;
                default:
                    System.out.println("\n\t\t\tEnter a valid number!\n\n");
            }
        }
    }

    private void createBill() {
        System.out.print("\t\t\tAre you a new customer? If yes enter 1 or 2 : ");
        int isNewCustomer = in.nextInt();
        long phn_no;
        String name = "";
        if (isNewCustomer == 1) {
            System.out.print("\n\n\t\t\tCustomer phone number : ");
            phn_no = in.nextLong();
            System.out.print("\t\t\tCustomer name : ");
            name = in.next();
            dbHandler.insertCustomer(phn_no, name);
        } else {
            System.out.print("\n\n\t\t\tCustomer phone number : ");
            phn_no = in.nextLong();
            name = dbHandler.getCustomerName(phn_no);
        }

        int bid = 1+dbHandler.getNextBillId();
        System.out.print("\n\t\t\tEnter the date (YYYY-mm-DD) : ");
        String date = in.next();
        dbHandler.createBill(bid, phn_no, date);

        while (true) {
            System.out.print("\n\t\t\tEnter the Product id : ");
            int p_id = in.nextInt();
            System.out.print("\t\t\tEnter the Quantity : ");
            int qty = in.nextInt();
            dbHandler.insertBillItem(bid, p_id, qty);

            System.out.print("\t\t\tIf you have next product enter 1 or anything : ");
            int next = in.nextInt();
            if (next != 1) {
                break;
            }
        }

        dbHandler.updateTotalPrice(bid);
        dbHandler.printBill(bid);
    }

    private void reportBasedOnTime() {
        System.out.print("\n\n\t\t\t1.Between dates\n\t\t\t2.On a Date\n\t\t\t3.On a Month\n\t\t\t4.On a Year\n\n\t\t\tEnter your choice : ");
        int choice = in.nextInt();
        switch (choice) {
            case 1:
                System.out.print("\n\t\t\tEnter the from date(YYYY-mm-DD) : ");
                String fromDate = in.next();
                System.out.print("\n\t\t\tEnter the to date(YYYY-mm-DD) : ");
                String toDate = in.next();
                dbHandler.reportBetweenDates(fromDate, toDate);
                break;
            case 2:
                System.out.print("\n\t\t\tEnter the date(YYYY-mm-DD) : ");
                String date = in.next();
                dbHandler.reportOnDate(date);
                break;
            case 3:
                System.out.print("\n\t\t\tEnter the Month(Number) : ");
                int month = in.nextInt();
                dbHandler.reportOnMonth(month);
                break;
            case 4:
                System.out.print("\t\t\tEnter the Year(YYYY) : ");
                int year = in.nextInt();
                dbHandler.reportOnYear(year);
                break;
        }
    }

    private void reportBasedOnCustomer() {
        System.out.print("\n\n\t\t\tEnter the Customer number : ");
        long phn_no = in.nextLong();
        dbHandler.reportOnCustomer(phn_no);
    }

    private void reportBasedOnProduct() {
        System.out.print("\n\n\t\t\tEnter the Product id : ");
        int pid = in.nextInt();
        dbHandler.reportOnProduct(pid);
    }

    private void generateInvoice() {
        System.out.print("\n\n\t\t\tEnter the Bill id : ");
        int bid = in.nextInt();
        dbHandler.printBill(bid);
    }

    private void reportBasedOnBillAmount() {
        System.out.print("\n\n\t\t\tEnter the amount to search to bill which has Bill amount more than that : ");
        int amount = in.nextInt();
        dbHandler.reportOnBillAmount(amount);
    }

    private void insertOrListProducts() {
        System.out.print("\n\n\t\t\t1.Insert Product\n\t\t\t2.List Products\n\n\t\t\tChoice : ");
        int choice = in.nextInt();
        switch (choice) {
            case 1:
                System.out.print("\n\t\t\tEnter the Product id : ");
                int pid = in.nextInt();
                System.out.print("\t\t\tEnter the Product name : ");
                String pname = in.next();
                System.out.print("\t\t\tEnter the Product price : ");
                int price = in.nextInt();
                dbHandler.insertProduct(pid, pname, price);
                break;
            case 2:
                dbHandler.listProducts();
                break;
        }
    }

    private void listCustomerDetails() {
        dbHandler.listCustomerDetails();
    }

    private void listAllBills() {
        dbHandler.listAllBills();
    }

    private void listPaidBills() {
        dbHandler.listPaidBills();
    }

    private void customerBalance() {
        dbHandler.customerBalance();
    }

    private void updateBill() {
        System.out.print("\n\t\t\tEnter the Bill id to Update(Paid) : ");
        int bid = in.nextInt();
        dbHandler.updatePaymentStatus(bid);
    }
}
