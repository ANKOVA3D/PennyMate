package PennyMate.Transaction;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import PennyMate.Entity.Transaction;

@Repository

public class TransactionRepos {
	
        @Autowired
        private JdbcTemplate jdbcTemplate;

        // Inserisci una nuova transazione
        public void insert(Transaction t) {
            String sql = "INSERT INTO transactions (user_id, amount, type, category, description, date) VALUES (?, ?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql, t.getUserId(), t.getAmount(), t.getType(), t.getCategory(), t.getDescription(), t.getDate());
        }
        
     // Totale generale (indipendentemente dal tipo)
        public BigDecimal totalAll(int userId) {
            String sql = "SELECT SUM(amount) FROM transactions WHERE user_id = ?";
            return jdbcTemplate.queryForObject(sql, new Object[]{userId}, BigDecimal.class);
        }


        // Ottieni tutte le transazioni per un utente
        public List<Transaction> findByUserId(int userId) {
            String sql = "SELECT * FROM transactions WHERE user_id = ?";
            return jdbcTemplate.query(sql, new Object[]{userId}, (rs, rowNum) -> new Transaction(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getBigDecimal("amount"),
                    rs.getBoolean("type"),
                    rs.getString("category"),
                    rs.getString("description"),
                    rs.getTimestamp("date")
            ));
        }

        // Modifica transazione
        public void update(Transaction t) {
            String sql = "UPDATE transactions SET amount = ?, type = ?, category = ?, description = ?, date = ? WHERE id = ?";
            jdbcTemplate.update(sql, t.getAmount(), t.getType(), t.getCategory(), t.getDescription(), t.getDate(), t.getId());
        }

        // Elimina una transazione
        public void delete(int id) {
            String sql = "DELETE FROM transactions WHERE id = ?";
            jdbcTemplate.update(sql, id);
        }

        // Calcola totale entrate/spese per utente
        public BigDecimal totalByType(int userId, boolean type) {
            String sql = "SELECT SUM(amount) FROM transactions WHERE user_id = ? AND type = ?";
            return jdbcTemplate.queryForObject(sql, new Object[]{userId, type}, BigDecimal.class);
        }
        
        
    }



