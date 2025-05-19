package PennyMate.Transaction;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryRepos {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<String> findAll() {
        String sql = "SELECT nome FROM category";
        return jdbcTemplate.queryForList(sql, String.class);
    }
}
