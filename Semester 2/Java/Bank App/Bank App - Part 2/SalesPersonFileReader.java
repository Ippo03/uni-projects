public class SalesPersonFileReader extends ListFileReader<SalesPerson> {
    protected SalesPersonFileReader() {
        super(new SalesPersonFileType());
    }
}