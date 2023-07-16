import java.util.ArrayList;

public class CommissionCalculator {
    private SalesList _sales;
    private BankProductList _bankProducts;
    private CreditCardTransactionList _ccTransactions;

    public CommissionCalculator(SalesList sales, BankProductList bankProducts, CreditCardTransactionList ccTransactions) {
        _sales = sales;
        _bankProducts = bankProducts;
        _ccTransactions = ccTransactions;
    }

    public CommissionReport calculateForSalesPerson(SalesPerson salesPerson) {
        ArrayList<Sale> sales = _sales.getSalesForSalesPerson(salesPerson.getCode()); 
        ArrayList<BankProduct> products = new ArrayList<>(); 
        CommissionReport result = new CommissionReport();
        result.setSalesPerson(salesPerson);

        for (Sale sale : sales)
            products.add(_bankProducts.findByProductCode(sale.getBankProductCode()));

        double sumAnnualInterest = 0;
        double sumLoanSalesPerson = 0;
        double ccCommission = 0;

        for (BankProduct product : products) {
            if (product instanceof Loan){
                Loan l = (Loan) product;
                sumLoanSalesPerson += l.getAmount();
                sumAnnualInterest += (l.getAmount() * l.getInterest());

                result.addLoan(new LoanCommission(l.getKey(), l.getAmount()));
            } else {
                double totalTransactionValue = 0;
                CreditCard cc = (CreditCard) product;

                ArrayList<CreditCardTransaction> transactions = 
                    _ccTransactions.getTransactionsForCreditCard(cc.getProductCode());

                for (CreditCardTransaction transaction : transactions) 
                    totalTransactionValue += (transaction.getTransactionAmount());

                ccCommission = (totalTransactionValue * cc.getCommissionPercentage());

                result.addCreditCard(new CreditCardCommission(cc.getKey(), totalTransactionValue, ccCommission));
            }
        }

        double percentageCommision = 0;
        if (sumLoanSalesPerson <= 500_000) {
            percentageCommision = 0.01;
        } else if (sumLoanSalesPerson <= 2_000_000) {
            percentageCommision = 0.02;
        } else {
            percentageCommision = 0.025;
        }

        result.setLoanCommission(percentageCommision);

        double totalSalesPersonCommision = sumLoanSalesPerson * percentageCommision;

        if (totalSalesPersonCommision > sumAnnualInterest) {
            totalSalesPersonCommision = sumAnnualInterest;
        }

        totalSalesPersonCommision += ccCommission;

        result.setTotalCommission(totalSalesPersonCommision);

        return result;
    }
}
