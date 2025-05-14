package PennyMate.Transaction;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import PennyMate.Entity.Budget;

@Repository
public class BudgetR {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void insert(Budget budget, int id) {
        String sql = "INSERT INTO budget (user_id, week, month, year) " +
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

    public Budget getBudget(int userId) {
        String sql = "SELECT * FROM budget WHERE id_utente = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{userId}, (rs, rowNum) -> new Budget(       		
            rs.getInt("id"),
            rs.getBigDecimal("week"),
            rs.getBigDecimal("month"),
            rs.getBigDecimal("year")
            
    ));
    }


  
}
