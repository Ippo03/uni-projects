public class SaleFileReader extends ListFileReader<Sale> {
    protected SaleFileReader() {
        super(new SaleFileType());
    }
}
