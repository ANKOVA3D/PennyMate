package PennyMate.Entity;

import java.math.BigDecimal;

public class BudgetSummary {
    private BigDecimal weeklySum;
	private BigDecimal monthlySum;
    private BigDecimal yearlySum;
    
    public BigDecimal getWeeklySum() {
		return weeklySum;
	}
	public void setWeeklySum(BigDecimal weeklySum) {
		this.weeklySum = weeklySum;
	}
	public BigDecimal getMonthlySum() {
		return monthlySum;
	}
	public void setMonthlySum(BigDecimal monthlySum) {
		this.monthlySum = monthlySum;
	}
	public BigDecimal getYearlySum() {
		return yearlySum;
	}
	public void setYearlySum(BigDecimal yearlySum) {
		this.yearlySum = yearlySum;
	}


    // Getters e Setters
}
