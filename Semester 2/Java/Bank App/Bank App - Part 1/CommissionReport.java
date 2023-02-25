import java.util.ArrayList;
import java.lang.StringBuilder;

public class CommissionReport {
    private SalesPerson _salesPerson;
    private double _totalCommission;

    private ArrayList<LoanCommission> _loans;
    private ArrayList<CreditCardCommission> _creditCards;

    public CommissionReport() {
        _loans = new ArrayList<>();
        _creditCards = new ArrayList<>();
    }

    public SalesPerson getSalesPerson() {
        return _salesPerson;
    }

    public void setSalesPerson(SalesPerson value) {
        _salesPerson = value;
    }

    public double getTotalCommission() {
        return _totalCommission;
    }

    public void setTotalCommission(double value) {
        _totalCommission = value;
    }

    public void addLoan(LoanCommission lc) {
        _loans.add(lc);
    }

    public void addCreditCard(CreditCardCommission ccc) {
        _creditCards.add(ccc);
    }

    public void setLoanCommission(double commissionPercentage) {
        for(LoanCommission lc: _loans)
            lc.setCommission(lc.getAmount() * commissionPercentage);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("\n");
        sb.append("=".repeat(72) + "\n");
        sb.append("Commission Report\n");
        sb.append("-".repeat(72) + "\n");
        sb.append(String.format("Sales Person: \n%s\n", _salesPerson));
        sb.append("-".repeat(72) + "\n");
        sb.append("Loans:\n");
        sb.append(".".repeat(72) + "\n");
        for(LoanCommission lc : _loans)
            sb.append(String.format("%s\n", lc));
            
        sb.append("-".repeat(72) + "\n");
        sb.append("Credit Cards:\n");
        sb.append(".".repeat(72) + "\n");
        for (CreditCardCommission ccc : _creditCards)     
            sb.append(String.format("%s\n", ccc));
    
        sb.append("-".repeat(72) + "\n");
        sb.append(String.format("Total Commission: %.02f EUR\n", _totalCommission));
        sb.append("=".repeat(72) + "\n");
        
        return sb.toString();    
    }
}