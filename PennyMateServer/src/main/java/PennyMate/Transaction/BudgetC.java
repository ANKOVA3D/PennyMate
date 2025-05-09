package PennyMate.Transaction;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import PennyMate.Entity.Budget;
import PennyMate.Entity.User;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

@RestController
@RequestMapping("/budget")
@CrossOrigin(origins = "http://localhost", allowCredentials = "true")
public class BudgetC {

    @Autowired
    private BudgetR budgetR;

    // Aggiungi nuovo budget
    @PostMapping("/add")
    public ResponseEntity<?> addBudget(@RequestBody Budget budget, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Non autenticato");
        }

        budget.setUserId(user.getId());
        budgetR.insert(budget);
        return ResponseEntity.ok("Budget salvato");
    }

    // Elimina un budget per ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteBudget(@PathVariable int id, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Non autenticato");
        }

        budgetR.delete(id);
        return ResponseEntity.ok("Budget eliminato");
    }

    // Ottieni tutti i budget dell'utente
    @GetMapping("/all")
    public ResponseEntity<?> getBudgets(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Non autenticato");
        }

        List<Budget> budgets = budgetR.findAllByUserId(user.getId());
        return ResponseEntity.ok(budgets);
    }

}
