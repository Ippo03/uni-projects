import java.util.ArrayList;

public class SalesList {
    private ArrayList<Sale> _items;

    public SalesList() {
        _items = new ArrayList<>();
    }

    public void add(Sale s) {
        _items.add(s);
    }

    public ArrayList<Sale> getSalesForSalesPerson(String salesPersonCode) {
        ArrayList<Sale> sales = new ArrayList<>();
        for (Sale s : _items) {
            if (s.getSalesPersonCode().equals(salesPersonCode))
                sales.add(s);
        }
        return sales;
    }
}
