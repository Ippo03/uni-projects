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
}

