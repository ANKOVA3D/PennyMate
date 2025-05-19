package PennyMate.Transaction;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import PennyMate.Entity.Budget;
import PennyMate.Entity.BudgetSummary;
import PennyMate.Entity.DateResponseDTO;
import PennyMate.Entity.User;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "http://localhost", allowCredentials = "true")
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
    
    
    
    @PutMapping("/update")
    public ResponseEntity<String> updateBudget(HttpServletRequest request, @RequestBody Budget budget) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Non autenticato");
        }
        budgetRepository.updateBudget(budget, user.getId());
        return ResponseEntity.ok("Budget aggiornato con successo!");
    }
    
    //Date x Budget
    
    @GetMapping("/summary")
    public ResponseEntity<?> getBudgetSummary(HttpServletRequest request) {
    	User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Non autenticato");
        }
        BudgetSummary summary = budgetRepository.calculateSumForUser(user.getId());
        System.out.println(summary.getMonthlySum());
        System.out.println(summary.getWeeklySum());
        System.out.println(summary.getYearlySum());
        return ResponseEntity.ok(summary);
    }
    
    @GetMapping("/dates")
    public ResponseEntity<?> getBudgetDates(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Non autenticato");
        }

        DateResponseDTO dates = budgetRepository.getBudgetDates(user.getId());
        return ResponseEntity.ok(dates);
    }

    
    @PutMapping("/updateWDate")
    public ResponseEntity<String> updateBudgetW(HttpServletRequest request, @RequestBody Timestamp date) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Non autenticato");
        }
        budgetRepository.updateWeekDate(date, user.getId());
        return ResponseEntity.ok("Budget aggiornato con successo!");
    }
    
    @PutMapping("/updateMDate")
    public ResponseEntity<String> updateBudgetM(HttpServletRequest request, @RequestBody Timestamp date) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Non autenticato");
        }
        budgetRepository.updateMonthDate(date, user.getId());
        return ResponseEntity.ok("Budget aggiornato con successo!");
    }
    
    @PutMapping("/updateYDate")
    public ResponseEntity<String> updateBudgetY(HttpServletRequest request, @RequestBody Timestamp date) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Non autenticato");
        }
        budgetRepository.updateYearDate(date, user.getId());
        return ResponseEntity.ok("Budget aggiornato con successo!");
    }

    
}
