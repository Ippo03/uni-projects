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
    
    public void LoadFromFile(String fileName) {
        System.out.println(String.format("Loading sales from: [%s]", fileName));
        var reader = new SaleFileReader();
        try {
            _items = reader.readAll(fileName);    
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void WriteToFile(String fileName) {
        System.out.println(String.format("Saving sales to: [%s]", fileName));
        var writer = new SaleFileWriter();
        try {
            writer.writeAll(fileName, _items);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
