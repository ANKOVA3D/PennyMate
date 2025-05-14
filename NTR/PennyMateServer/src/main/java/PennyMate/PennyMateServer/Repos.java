package PennyMate.PennyMateServer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import PennyMate.Entity.User;

@Repository
public class Repos {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Metodo per ottenere tutti gli utenti
    public List<User> getAllUsers() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new User( 
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("email"),
                rs.getString("password_hash")
        ));
    }

    // Metodo per ottenere un singolo utente per ID
    public User getUserById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> new User(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("email"),
                rs.getString("password_hash")
        ));
    }

    public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{username}, (rs, rowNum) -> new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("password_hash")
            ));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    // Metodo per aggiungere un nuovo utente
    public String addUser(User user) {
        // Controllo se l'username esiste già
        String checkUsernameSql = "SELECT COUNT(*) FROM users WHERE username = ?";
        int usernameCount = jdbcTemplate.queryForObject(checkUsernameSql, Integer.class, user.getName());

        if (usernameCount > 0) {
            return "Username già in uso!";
        }

        // Controllo se l'email esiste già
        String checkEmailSql = "SELECT COUNT(*) FROM users WHERE email = ?";
        int emailCount = jdbcTemplate.queryForObject(checkEmailSql, Integer.class, user.getEmail());

        if (emailCount > 0) {
            return "Email già in uso!";
        }

        // Se l'username e l'email non sono già usati, inserisco il nuovo utente
        String insertSql = "INSERT INTO users (username, email, password_hash) VALUES (?, ?, ?)";
        int rowsAffected = jdbcTemplate.update(insertSql, user.getName(), user.getEmail(), user.getPassword());

        return rowsAffected > 0 ? "Registrazione avvenuta con successo!" : "Errore durante la registrazione.";
    }

 // Metodo per aggiornare un utente esistente con controlli
    public String updateUser(User user, int id) {
        // Controllo se un altro utente ha già lo stesso username
        String checkUsernameSql = "SELECT COUNT(*) FROM users WHERE username = ? AND id <> ?";
        int usernameCount = jdbcTemplate.queryForObject(checkUsernameSql, Integer.class, user.getName(), id);

        if (usernameCount > 0) {
            return "Username già in uso da un altro utente!";
        }

        // Controllo se un altro utente ha già la stessa email
        String checkEmailSql = "SELECT COUNT(*) FROM users WHERE email = ? AND id <> ?";
        int emailCount = jdbcTemplate.queryForObject(checkEmailSql, Integer.class, user.getEmail(), id);

        if (emailCount > 0) {
            return "Email già in uso da un altro utente!";
        }

        // Se username ed email sono validi, aggiorno l'utente
        String sql = "UPDATE users SET username = ?, email = ?, password_hash = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, user.getName(), user.getEmail(), user.getPassword(), id);

        return rowsAffected > 0 ? "Profilo aggiornato con successo!" : "Errore durante l'aggiornamento del profilo.";
    }


    // Metodo per eliminare un utente
    public int deleteUser(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public double getBudgetByUserId(int id) {
        String sql = "SELECT weekly_budget FROM users WHERE id = ?";
        try {
            // Restituisce il valore di weekly_budget
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, Double.class);
        } catch (EmptyResultDataAccessException e) {
            // Se l'utente non esiste o non ha un budget settimanale, ritorna 0.0
            return 0.0;
        }
    }
}
