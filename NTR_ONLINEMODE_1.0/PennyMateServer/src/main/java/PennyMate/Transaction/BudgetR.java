package PennyMate.Transaction;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import PennyMate.Entity.Budget;
import PennyMate.Entity.BudgetSummary;
import PennyMate.Entity.DateResponseDTO;

@Repository
public class BudgetR {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void insert(Budget budget, int id) {
        String sql = "INSERT INTO budget (id_utente, week, month, year) " +
                     "VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                id,
                budget.getWeek(),
                budget.getMonth(),
                budget.getYear());
                
    }

    public void deleteBudget(int budgetId) {
        String sql = "DELETE FROM budget WHERE id_utente = ?";
        jdbcTemplate.update(sql, budgetId);
    }
    

    public DateResponseDTO getBudgetDates(int userId) {
        String sql = "SELECT wDate, mDate, yDate FROM budget WHERE id_utente = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{userId}, (rs, rowNum) -> {
            return new DateResponseDTO(
                rs.getTimestamp("wDate"),
                rs.getTimestamp("mDate"),
                rs.getTimestamp("yDate")
            );
        });
    }


    public Budget getBudget(int userId) {
        String sql = "SELECT * FROM budget WHERE id_utente = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{userId}, (rs, rowNum) -> {
            Timestamp weekDate = rs.getTimestamp("wDate");
            Timestamp monthDate = rs.getTimestamp("mDate");
            Timestamp yearDate = rs.getTimestamp("yDate");

            BigDecimal week = (weekDate != null) ? rs.getBigDecimal("week") : BigDecimal.ZERO;
            BigDecimal month = (monthDate != null) ? rs.getBigDecimal("month") : BigDecimal.ZERO;
            BigDecimal year = (yearDate != null) ? rs.getBigDecimal("year") : BigDecimal.ZERO;

            return new Budget(week, month, year);
        });
    }


    
    public void updateBudget(Budget budget, int userId) {
        String sql = "UPDATE budget SET week = ?, month = ?, year = ? WHERE id_utente = ?";
        jdbcTemplate.update(sql,
            budget.getWeek(),
            budget.getMonth(),
            budget.getYear(),
            userId);
    }
    
    public void updateWeekDate(Timestamp date, int userId) {
        String sql = "UPDATE budget SET wDate = ? WHERE id_utente = ?";
        jdbcTemplate.update(sql,
            date,
            userId);
    }
    
    public void updateMonthDate(Timestamp date, int userId) {
        String sql = "UPDATE budget SET mDate = ? WHERE id_utente = ?";
        jdbcTemplate.update(sql,
            date,
            userId);
    }
    
    public void updateYearDate(Timestamp date, int userId) {
        String sql = "UPDATE budget SET yDate = ? WHERE id_utente = ?";
        jdbcTemplate.update(sql,
            date,
            userId);
    }
    
    public BudgetSummary calculateSumForUser(int userId) {
        String sql = """
            SELECT 
                (SELECT COALESCE(SUM(t.amount), 0) FROM transactions t
                 JOIN budget b ON t.user_id = b.id_utente
                 WHERE t.user_id = ? AND t.type = FALSE
                 AND t.date BETWEEN b.wDate AND DATE_ADD(b.wDate, INTERVAL (7 - WEEKDAY(b.wDate)) DAY)) AS weeklySum,

                (SELECT COALESCE(SUM(t.amount), 0) FROM transactions t
                 JOIN budget b ON t.user_id = b.id_utente
                 WHERE t.user_id = ? AND t.type = FALSE
                 AND t.date BETWEEN b.mDate AND LAST_DAY(b.mDate)) AS monthlySum,

                (SELECT COALESCE(SUM(t.amount), 0) FROM transactions t
                 JOIN budget b ON t.user_id = b.id_utente
                 WHERE t.user_id = ? AND t.type = FALSE
                 AND t.date BETWEEN b.yDate AND MAKEDATE(YEAR(b.yDate), 1) + INTERVAL 1 YEAR - INTERVAL 1 DAY) AS yearlySum
            """;
        

        return jdbcTemplate.queryForObject(sql, new Object[]{userId, userId, userId}, (rs, rowNum) -> {
            BudgetSummary summary = new BudgetSummary();
            summary.setWeeklySum(rs.getBigDecimal("weeklySum"));
            summary.setMonthlySum(rs.getBigDecimal("monthlySum"));
            summary.setYearlySum(rs.getBigDecimal("yearlySum"));
            return summary;
        });
    }


  
}
