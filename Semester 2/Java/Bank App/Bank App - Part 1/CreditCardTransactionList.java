import java.util.ArrayList;

public class CreditCardTransactionList {
    private ArrayList<CreditCardTransaction> _items;

    public CreditCardTransactionList() {
        _items = new ArrayList<>();
    }
    
    public void add(CreditCardTransaction s) {
        _items.add(s);
    }

    public ArrayList<CreditCardTransaction> getTransactionsForCreditCard(String creditCardTRansactionCode) {
        ArrayList<CreditCardTransaction> transactions = new ArrayList<>();
        for (CreditCardTransaction cc : _items) {
            if (cc.getProductCode().equals(creditCardTRansactionCode))
                transactions.add(cc);
        }
        return transactions;
    }
    
}

