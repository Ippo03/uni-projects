import java.util.ArrayList;

public class SalesPersonList {
    private ArrayList<SalesPerson> _items;

    public SalesPersonList() {
        _items = new ArrayList<>();
    }

    public void add(SalesPerson s) {
        _items.add(s);
    }

    public int size() {
        return _items.size();
    }

    public SalesPerson get(int index) {
        return _items.get(index);
    }

    public SalesPerson findByKey(String key) {
        for (SalesPerson sp : _items) {
            if (sp.getKey().equals(key))
                return sp;
        }

        return null;
    }

    public ArrayList<SalesPerson> all() {
        return new ArrayList<SalesPerson>(_items);
    }
}

