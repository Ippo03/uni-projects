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
    
    public void LoadFromFile(String fileName) {
        System.out.println(String.format("Loading credit card transactions from: [%s]", fileName));
        var reader = new CreditCardTransactionFileReader();
        try {
            _items = reader.readAll(fileName);    
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void WriteToFile(String fileName) {
        System.out.println(String.format("Saving credit card transactions to: [%s]", fileName));
        var writer = new CreditCardTransactionFileWriter();
        try {
            writer.writeAll(fileName, _items);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

