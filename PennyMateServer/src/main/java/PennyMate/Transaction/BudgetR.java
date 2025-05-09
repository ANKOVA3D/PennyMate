package PennyMate.Transaction;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import PennyMate.Entity.Budget;

@Repository
public class BudgetR {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Budget> budgetRowMapper = new RowMapper<>() {
        @Override
        public Budget mapRow(ResultSet rs, int rowNum) throws SQLException {
            Budget budget = new Budget();
            budget.setId(rs.getInt("id"));
            budget.setUserId(rs.getInt("user_id"));
            budget.setAmount(rs.getBigDecimal("amount"));
            budget.setCategory(rs.getString("category"));
            budget.setDescription(rs.getString("description"));
            budget.setStartDate(rs.getDate("start_date").toLocalDate());
            budget.setEndDate(rs.getDate("end_date").toLocalDate());
            budget.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            return budget;
        }
    };

    public void insert(Budget budget) {
        String sql = "INSERT INTO budget (user_id, amount, category, description, start_date, end_date) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                budget.getUserId(),
                budget.getAmount(),
                budget.getCategory(),
                budget.getDescription(),
                budget.getStartDate(),
                budget.getEndDate());
    }

    public void delete(int budgetId) {
        String sql = "DELETE FROM budget WHERE id = ?";
        jdbcTemplate.update(sql, budgetId);
    }

    public List<Budget> findAllByUserId(int userId) {
        String sql = "SELECT * FROM budget WHERE user_id = ?";
        return jdbcTemplate.query(sql, new Object[]{userId}, budgetRowMapper);
    }

  
}
