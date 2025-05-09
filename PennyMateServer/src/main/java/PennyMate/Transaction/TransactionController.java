package PennyMate.Transaction;
import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import PennyMate.Entity.Transaction;
import PennyMate.Entity.User;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/transaction")
@CrossOrigin(origins = "http://localhost", allowCredentials = "true")
public class TransactionController {

    @Autowired
    private TransactionRepos transactionRepo;

    //Aggiungi nuova transazione
    @PostMapping("/add")
    public ResponseEntity<?> addTransaction(@RequestBody Transaction t, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Non autenticato");
        }

        if (t.getType() == false && t.getAmount().compareTo(BigDecimal.ZERO) > 0) {
            // se è una uscita, rendi l’importo negativo
            t.setAmount(t.getAmount().negate());
        }

        t.setUserId(user.getId());
        transactionRepo.insert(t);
        return ResponseEntity.ok("Transazione salvata");
    }


    //Ottieni tutte le transazioni dell'utente
    @GetMapping("/all")
    public ResponseEntity<?> getTransactions(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Non autenticato");
        }
        return ResponseEntity.ok(transactionRepo.findByUserId(user.getId()));
    }

    //Modifica transazione
    @PutMapping("/update")
    public ResponseEntity<?> updateTransaction(HttpServletRequest request, @RequestBody Transaction t) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Non autenticato");
        }
        t.setUserId(user.getId()); // opzionale: verifica che sia suo
        transactionRepo.update(t);
        return ResponseEntity.ok("Transazione aggiornata");
    }

    //Elimina transazione
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTransaction(HttpServletRequest request, @PathVariable int id) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Non autenticato");
        }
        transactionRepo.delete(id);
        return ResponseEntity.ok("Transazione eliminata");
    }

    //Calcola totale generale (entrate + uscite)
    @GetMapping("/total")
    public ResponseEntity<?> getTotalAll(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Non autenticato");
        }
        BigDecimal total = transactionRepo.totalAll(user.getId());
        return ResponseEntity.ok(Map.of("total", total != null ? total : BigDecimal.ZERO));
    }

    
    //Calcola totale entrate/spese (type: true=entrate, false=spese)
    @GetMapping("/total/{type}")
    public ResponseEntity<?> getTotal(HttpServletRequest request, @PathVariable boolean type) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Non autenticato");
        }
        BigDecimal total = transactionRepo.totalByType(user.getId(), type);
        return ResponseEntity.ok(Map.of("total", total != null ? total : BigDecimal.ZERO));
    }
}
