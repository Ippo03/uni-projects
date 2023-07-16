import java.util.ArrayList;

public class BankProductList {
    private ArrayList<BankProduct> _items;

    public BankProductList() {
        _items = new ArrayList<>();
    }

    public void add(BankProduct s) {
        _items.add(s);
    }

    public int size() {
        return _items.size();
    }

    public BankProduct get(int index) {
        return _items.get(index);
    }

    public BankProduct findByProductCode(String bankProductCode) {
        for (BankProduct bp : _items){
            if (bp.getProductCode().equals(bankProductCode)){
                return bp;
            }
        }
        return null;
    }

    public void LoadFromFile(String fileName) {
        System.out.println(String.format("Loading bank products from: [%s]", fileName));
        var reader = new BankProductFileReader();
        try {
            _items = reader.readAll(fileName);    
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void WriteToFile(String fileName) {
        System.out.println(String.format("Saving bank products to: [%s]", fileName));
        var writer = new BankProductFileWriter();
        try {
            writer.writeAll(fileName, _items);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

