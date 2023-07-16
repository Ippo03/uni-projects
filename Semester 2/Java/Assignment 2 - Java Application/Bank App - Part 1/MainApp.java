import java.util.*;

class MainApp {
    private SalesList _sales;
    private BankProductList _bankProducts;
    private SalesPersonList _salesPersons;
    private CreditCardTransactionList _ccTransactions;
    private Scanner _in;
    private CommissionCalculator _commissionCalculator;

    private final String ERROR_INVALID_INPUT = "Invalid choice.";

    public MainApp() {
        _sales = new SalesList();
        _bankProducts = new BankProductList();
        _salesPersons = new SalesPersonList();
        _ccTransactions = new CreditCardTransactionList();
        _commissionCalculator = new CommissionCalculator(_sales, _bankProducts, _ccTransactions);

        _in = new Scanner(System.in);
    }

    private void printMenu() {
        System.out.println("=".repeat(72));
        System.out.println("Bank Application");
        System.out.println("-".repeat(72));

        System.out.println("1. Add a New Salesperson");
        System.out.println("2. Add a New Bank Product");
        System.out.println("3. Add a New Bank Product Sale");
        System.out.println("4. Add a New Credit Card Transaction");
        System.out.println("5. Show Loans");
        System.out.println("6. Calculate Salesperson's Commission");
        System.out.println("7. Show Credit Card Transactions of Salesperson");
        System.out.println("8. Calculate All Salesperson Commissions");
        System.out.println("9. Show the Final Amount of Commision of Salesperson");
        System.out.println("0. Exit");

        System.out.println("-".repeat(72));
    }

    private void enterSalesPerson() {
        System.out.println("Please enter the salesperson's details.");
        SalesPerson s = new SalesPerson();

        System.out.print("Last Name: ");
        s.setLastName(_in.nextLine());

        System.out.print("First Name: ");
        s.setFirstName(_in.nextLine());

        System.out.print("Code: ");
        s.setCode(_in.nextLine());

        System.out.print("Tax Code: ");
        s.setTaxCode(_in.nextLine());

        _salesPersons.add(s);
        System.out.println(String.format("Created Salesperson: %s", s));

        System.out.println();
    }

    private void enterBankProduct() {
        boolean done = false;

        while (!done) {
            System.out.println("Choose the Bank Product:");
            System.out.println("1. Loan");
            System.out.println("2. Credit Card");
            System.out.println("0. Return to Previous Menu");
            System.out.print("Enter Choice: ");

             String option = _in.nextLine().toLowerCase();
            
            System.out.println();
            
            if (option.equals("1")) {
                enterLoan();
                done = true;
            } else if (option.equals("2")) {
                enterCreditCard();
                done = true;
            } else if (option.equals("0")) {
                done = true;
            } else { 
                System.out.println(ERROR_INVALID_INPUT);
            }
        }
    }
        
    private void enterLoan() {
        System.out.println("Please enter the Loan details.");
        Loan l = new Loan();
        
        System.out.print("Product Code: ");
        l.setProductCode(_in.nextLine());

        System.out.print("Customer IBAN: ");
        l.setCustomerIBAN(_in.nextLine());

        System.out.print("Customer Tax Code: ");
        l.setCustomerTaxCode(_in.nextLine());

        System.out.print("Amount: ");
        l.setAmount(_in.nextDouble());

        System.out.print("Annual Interest Percentage: ");
        // We need to read the percentage given in the form: 12% as 0.12, 
        // hence the division by 100.
        l.setInterest(_in.nextDouble() / 100);

        _in.nextLine();

        _bankProducts.add(l);
        System.out.println(String.format("Created the Loan Product: %s", l));

        System.out.println();
    }

    private void enterCreditCard() {
        System.out.println("Please enter the Credit Card details.");
        CreditCard cc = new CreditCard();

        System.out.print("Product Code: ");
        cc.setProductCode(_in.nextLine());

        System.out.print("Customer IBAN: ");
        cc.setCustomerIBAN(_in.nextLine());

        System.out.print("Customer Tax Code: ");
        cc.setCustomerTaxCode(_in.nextLine());

        System.out.print("Commission Percentage: ");
        // We need to read the percentage given in the form: 12% as 0.12, 
        // hence the division by 100.
        cc.setCommissionPercentage(_in.nextDouble() / 100);

        _in.nextLine();

        System.out.print("Max Transaction Amount: ");
        cc.setMaxTransactionAmount(_in.nextDouble());

        System.out.print("Max Annual Amount: ");
        cc.setMaxAnnualAmount(_in.nextDouble());

        _in.nextLine();

        _bankProducts.add(cc);
        System.out.println(String.format("Created the Credit Card Product: %s", cc));

        System.out.println();
    }

    private void enterSale() {
        System.out.println("Please enter the Bank Product Sale details.");
        Sale s = new Sale();

        System.out.print("Salesperson Code: ");
        s.setSalesPersonCode(_in.nextLine());

        System.out.print("Bank Product Code: ");
        s.setBankProductCode(_in.nextLine());

        System.out.print("Reason: ");
        s.setReason(_in.nextLine());

        _sales.add(s);
        System.out.println(String.format("Created Bank Product Sale: %s", s));

        System.out.println();
    }

    private void enterCrediCardTransaction() {
        System.out.println("Please enter the Bank Product Sale details.");
        
        CreditCardTransaction cct = new CreditCardTransaction();
        BankProduct bp;
        CreditCard cc;
        
        while(true) {
            System.out.print("Bank Product Code: ");
            cct.setProductCode(_in.nextLine());

            bp = _bankProducts.findByProductCode(cct.getProductCode());
            if (bp != null && bp instanceof CreditCard) {
                cc = (CreditCard) bp;
                break;
            }

            System.out.println("The product code you entered could not be matched to an existing credit card.");
            System.out.print("Press (1) to try again, or any other key to return to the previous menu: ");
            if (!_in.nextLine().equals("1")) {
                return;
            }
        }

        while(true) {
            System.out.print("Transaction Amount: ");
            cct.setTransactionAmount(_in.nextDouble());

            if (cct.getTransactionAmount() > cc.getMaxTransactionAmount()) {
                System.out.println("The amount entered exceeds the maximum transaction amount for this credit card.");
            } else if (cct.getTransactionAmount() > (cc.getMaxAnnualAmount() - cc.getBalance())) {
                System.out.println("The amount entered will make the balance exceed the maximum annual amount for this credit card.");
            } else {
                break;
            }

            System.out.print("Press (1) to enter another amount, or any other key to return to the previous menu: ");
            if (!_in.nextLine().equals("1")) {
                return;
            }
        }

        _in.nextLine();

        System.out.print("Reason: ");
        cct.setReason(_in.nextLine());

        _ccTransactions.add(cct);
        System.out.println(String.format("Created Credit Card Transaction: %s", cct));

        if (cc != null) {
            cc.updateBalance(cct.getTransactionAmount());
        }

        System.out.println();
    }
    private void showLoans() {
        int counter = 0;
        for (int i=0; i < _bankProducts.size(); i++) {
            BankProduct p = _bankProducts.get(i);
            if (p instanceof Loan) {
                counter ++;
                System.out.println(String.format("%d. %s", counter, (Loan) p));
            }
        }
        System.out.println();
    }

    private void showSalesPersons() {
        int counter = 0;
        for (int i=0; i < _salesPersons.size(); i++) {
            SalesPerson sp = _salesPersons.get(i);
            counter ++;
            System.out.println(String.format("%d. %s", counter, sp));
        }
        System.out.println();
    }

    private void selectSPAndCalculateCommission() {
        SalesPerson sp;

        while (true) {
            showSalesPersons();

            System.out.print("Please enter the key of the Salesperson to calculate commision for: ");
            String key = _in.nextLine();
            sp = _salesPersons.findByKey(key);
            
            if (sp != null) break;

            System.out.println("There is no Salesperson with that key.");
            System.out.print("Press (1) to try again, or any other key to return to the previous menu: ");
            if (!_in.nextLine().equals("1")) {
                return;
            }
        }

        CommissionReport report = _commissionCalculator.calculateForSalesPerson(sp);
        System.out.println(report);
        System.out.println();
    }

    private void selectSPandShowCreditCardTransactions() {
        SalesPerson sp;

        while (true) {
            showSalesPersons();

            System.out.print("Please enter the key of the Salesperson to show CC transactions for: ");
            String key = _in.nextLine();
            sp = _salesPersons.findByKey(key);
            
            if (sp != null) break;

            System.out.println("There is no Salesperson with that key.");
            System.out.print("Press (1) to try again, or any other key to return to the previous menu: ");
            if (!_in.nextLine().equals("1")) {
                return;
            }
        }

        ArrayList<Sale> sales = _sales.getSalesForSalesPerson(sp.getCode()); 
        ArrayList<BankProduct> products = new ArrayList<>(); 
        ArrayList<CreditCardTransaction> creditCardTransactions = new ArrayList<>();
        
        for (Sale sale : sales)
            products.add(_bankProducts.findByProductCode(sale.getBankProductCode()));
        
        for (BankProduct product : products) {
            if (product instanceof CreditCard) {
                creditCardTransactions.addAll(
                    _ccTransactions.getTransactionsForCreditCard(((CreditCard) product).getProductCode()));
            }
        }

        int counter = 0;
        for (CreditCardTransaction transaction : creditCardTransactions) {
            counter ++;
            System.out.println(String.format("%d. %s", counter, transaction));
        }
        System.out.println();
    }

    private void calculateTotalCommision() {
        int counter = 0;
        for (SalesPerson sp : _salesPersons.all()) {
            counter ++;
            CommissionReport cr = _commissionCalculator.calculateForSalesPerson(sp);
            System.out.println(String.format("%d. Key: %s, Last Name: %s, First Name: %s, Total Commission: %.02f EUR",
                counter, sp.getKey(), sp.getLastName(), sp.getFirstName(), cr.getTotalCommission()));
        }
        System.out.println();
    }

    private void showFinalAmountCommision() {
        int counter = 0;
        double sumFinalCommission = 0;
        for (SalesPerson sp : _salesPersons.all()) {
            counter ++;
            CommissionReport cr = _commissionCalculator.calculateForSalesPerson(sp);
            System.out.println(String.format("%d. Code: %s, Last Name: %s, First Name: %s, Total Commission: %.02f EUR",
                counter, sp.getCode(), sp.getLastName(), sp.getFirstName(), cr.getTotalCommission()));
                sumFinalCommission +=  cr.getTotalCommission();
        }
        System.out.printf("Total Salesperson Commissions: %.02f EUR\n", sumFinalCommission);

        System.out.println();
    }


    private boolean handleMenuChoice(int choice) {
        switch (choice) {
            case 1:
                enterSalesPerson();
                return false;
            case 2: 
                enterBankProduct();
                return false;
            case 3:
                enterSale();
                return false;
            case 4:
                enterCrediCardTransaction();
                return false;
            case 5:
                showLoans();
                return false;
            case 6:
                selectSPAndCalculateCommission();
                return false;
            case 7:
                selectSPandShowCreditCardTransactions();
                return false;
            case 8:
                calculateTotalCommision();
                return false;
            case 9:
                showFinalAmountCommision();
                return false;

            default:
                if (choice == 0) {
                    return true;
                } else {
                    System.out.println(ERROR_INVALID_INPUT);
                    return false;
                }
        }
    }
    
    public void menu() {
        boolean done = false;
        String menuSelection;

        while (!done) {
            printMenu();

            System.out.print("Enter choice: ");
            menuSelection = _in.nextLine();

            System.out.println();
            int choice = 0;
            try {
                choice = Integer.parseInt(menuSelection);
            } catch (Exception ex) {
                System.out.println(ERROR_INVALID_INPUT);
                continue;
            }

            done = handleMenuChoice(choice);
        }
    }

    private void createSamples() {
        SamplesBuilder builder = new SamplesBuilder(_sales, _bankProducts, _salesPersons, _ccTransactions);
        builder.build();
    }

    public static void main(String[] args) {
        MainApp app = new MainApp();
        app.createSamples();
        app.menu();
    }
}