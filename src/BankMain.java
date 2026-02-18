import java.util.Scanner;
import service.BankServices;
import ui.InputHandler;

public class BankMain {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        BankServices bank = new BankServices();
        InputHandler input = new InputHandler(sc);
        
        String cont;
        do {
            showMenu();
            int choice = input.getInt("Choose (1-8): ", 1, 8);
            
            try {
                switch (choice) {
                    case 1 -> bank.addAccount(input);
                    case 2 -> bank.displayAllAccounts();
                    case 3 -> input.search(bank);
                    case 4 -> input.transaction(bank);
                    case 5 -> input.update(bank);
                    case 6 -> input.delete(bank);
                    case 7 -> bank.addInterestToAllAccounts();
                    case 8 -> input.statement(bank);
                    default -> System.out.println("Invalid choice!");
                }
            } catch (Exception e) {
                System.out.println("❌ " + e.getMessage());
            }
            
            cont = input.getString("Continue? (yes/no): ");
        } while ("yes".equalsIgnoreCase(cont));
        
        sc.close();
        System.out.println("--------------------Thank You--------------------");
    }
    
    private static void showMenu() {
        System.out.println("""
            ---------------------Main Menu--------------------
            1:Add Account
            2:Display All
            3:Search Account
            4:Transaction
            5:Update
            6:Delete
            7:Interest
            8:Statement
            -----------------------------------------------""");
    }
}
