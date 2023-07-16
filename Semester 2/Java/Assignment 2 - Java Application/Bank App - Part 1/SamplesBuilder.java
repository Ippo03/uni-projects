public class SamplesBuilder {
    private SalesList _sales;
    private BankProductList _bankProducts;
    private SalesPersonList _salesPersons;
    private CreditCardTransactionList _ccTransactions;

    public SamplesBuilder(SalesList sales, BankProductList bankProducts, SalesPersonList salesPersons, CreditCardTransactionList ccTransactions) {
        _sales = sales;
        _bankProducts = bankProducts;
        _salesPersons = salesPersons;
        _ccTransactions = ccTransactions;
    }

    private void addSalesPersons() {
        _salesPersons.add(new SalesPerson("Doe", "Jane", "12345", "123456789"));
        _salesPersons.add(new SalesPerson("Koukis", "Nikos", "34567", "123400089"));
        _salesPersons.add(new SalesPerson("Nteliou", "Eleni", "98765", "098765432"));
    }

    private void addLoans() {
        _bankProducts.add(new Loan("1", "K87363", "765432109", 100000.00, 13.00/100));
        _bankProducts.add(new Loan("2", "K78664", "765432109", 2000000.00, 15.00/100));
        _bankProducts.add(new Loan("3", "K06237", "765432109", 3300000.00, 11.00/100));
    }

    private void addCreditCards() {
        _bankProducts.add(new CreditCard("4", "K36479", "201242070", 15.00/100, 150000.00, 500000));
        _bankProducts.add(new CreditCard("5", "K43023", "131242111", 12.00/100, 300000.00, 1000000));
        _bankProducts.add(new CreditCard("6", "K69401", "205248573", 17.00/100, 600000.00, 5500000));
    }

    private void addSales() {
        _sales.add(new Sale("12345", "1", "Consumer Loan"));
        _sales.add(new Sale("12345", "4", "Consumer Card"));
        _sales.add(new Sale("34567", "2", "Residential Loan"));
        _sales.add(new Sale("34567", "5", "Debit Card"));
        _sales.add(new Sale("98765", "3", "Investment Loan"));
        _sales.add(new Sale("98765", "6", "Business Card"));
    }

    private void findAndUpdateCCBalance(String ccProductCode, double amount) {
        CreditCard cc = (CreditCard) _bankProducts.findByProductCode(ccProductCode);
        cc.updateBalance(amount);
    }

    private void addTransactions() {
        _ccTransactions.add(new CreditCardTransaction("4", 20000, "Buy Car"));
        findAndUpdateCCBalance("4", 20000);
        _ccTransactions.add(new CreditCardTransaction("4", 10000, "Payments"));
        findAndUpdateCCBalance("4", 10000);
        _ccTransactions.add(new CreditCardTransaction("4", 50000, "Buy Land"));        
        findAndUpdateCCBalance("4", 50000);
        _ccTransactions.add(new CreditCardTransaction("4", 35000, "Buy Boat"));
        findAndUpdateCCBalance("4", 35000);

        _ccTransactions.add(new CreditCardTransaction("5", 100000, "Renovation"));
        findAndUpdateCCBalance("5", 100000);
        _ccTransactions.add(new CreditCardTransaction("5", 55000, "Holidays"));
        findAndUpdateCCBalance("5", 55000);
        _ccTransactions.add(new CreditCardTransaction("5", 70000, "Buy Car"));
        findAndUpdateCCBalance("5", 70000);
        _ccTransactions.add(new CreditCardTransaction("5", 20000, "Buy Painting"));
        findAndUpdateCCBalance("5", 20000);
        
        _ccTransactions.add(new CreditCardTransaction("6", 40000, "Business Trip"));
        findAndUpdateCCBalance("6", 40000);
        _ccTransactions.add(new CreditCardTransaction("6", 200000, "Open a new Store"));
        findAndUpdateCCBalance("6", 200000);
        _ccTransactions.add(new CreditCardTransaction("6", 150000, "Payments"));
        findAndUpdateCCBalance("6", 150000);
        _ccTransactions.add(new CreditCardTransaction("6", 30000, "Bills"));
        findAndUpdateCCBalance("6", 30000);
    }

    public void build() {
        addSalesPersons();
        addLoans();
        addCreditCards();
        addSales();
        addTransactions();
    }
}
