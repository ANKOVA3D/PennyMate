package PennyMate.Transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import PennyMate.Entity.Budget;
import PennyMate.Entity.User;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/budget")
public class BudgetC {

    @Autowired
    private BudgetR budgetRepository;

    @PostMapping("/add")
    public ResponseEntity<String> addBudget(HttpServletRequest request, @RequestBody Budget budget) {
    	User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Non autenticato");
        }
        budgetRepository.insert(budget, user.getId());
        return ResponseEntity.ok("Budget inserito con successo!");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteBudget(HttpServletRequest request) {
    	User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Non autenticato");
        }
        budgetRepository.deleteBudget(user.getId());
        return ResponseEntity.ok("Budget eliminato.");
    }

    @GetMapping("/user")
    public ResponseEntity<?> getBudgetByUser(HttpServletRequest request) {
    	User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Non autenticato");
        }
        Budget budget = budgetRepository.getBudget(user.getId());
        return ResponseEntity.ok(budget);
    }
}
