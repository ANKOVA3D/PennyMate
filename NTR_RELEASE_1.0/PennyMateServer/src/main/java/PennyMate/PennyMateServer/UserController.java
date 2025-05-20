package PennyMate.PennyMateServer;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import PennyMate.Entity.Budget;
import PennyMate.Entity.User;
import PennyMate.Transaction.BudgetR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private Repos userService;
    @Autowired
    private BudgetR BudgetService;
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    
    // API per ottenere tutti gli utenti
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody User user, HttpServletRequest request) {
        Map<String, String> response = new HashMap<>();
        
        User foundUser = userService.findByUsername(user.getName());

        if (foundUser == null || !passwordEncoder.matches(user.getPassword(), foundUser.getPassword())) {
            response.put("message", "Credenziali non valide");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        request.getSession().setAttribute("user", foundUser);
        System.out.println("User saved in session: " + user);
        
        response.put("message", "Login effettuato con successo");
        response.put("redirect", "/dashboard"); 
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletRequest request) {
        Map<String, String> response = new HashMap<>();
        // Invalida la sessione esistente
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        response.put("message", "Logout effettuato con successo");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        
        if (user != null) {
            // Ottieni l'utente completo dal database
            User fullUser = userService.getUserById(user.getId());
            return ResponseEntity.ok(Map.of(
                "username", fullUser.getName(),
                "email", fullUser.getEmail(),
                "id", fullUser.getId()
            ));
        } else {
        	System.out.println("User saved in session: " + user);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Non autenticato");
        }
    }
    
    @PutMapping("/update")
    public ResponseEntity<Map<String, String>> updateUserProfile(HttpServletRequest request, @RequestBody Map<String, String> updates) {
        User sessionUser = (User) request.getSession().getAttribute("user");
        if (sessionUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body(Map.of("message", "Non autenticato"));
        }

        User user = userService.getUserById(sessionUser.getId());

        if (updates.containsKey("username")) {
            user.setName(updates.get("username"));
        }
        if (updates.containsKey("email")) {
            user.setEmail(updates.get("email"));
        }
        if (updates.containsKey("password") && !updates.get("password").isEmpty()) {
            user.setPassword(passwordEncoder.encode(updates.get("password")));
        }

        String result = userService.updateUser(user, user.getId());

        Map<String, String> response = Map.of("message", result);

        if (result.equals("Profilo aggiornato con successo!")) {
            request.getSession().setAttribute("user", user);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }


    
    
    @GetMapping("/email")
    public ResponseEntity<?> getCurrentEmail(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user != null) {
            return ResponseEntity.ok(Map.of("email", user.getEmail()));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Non autenticato");
        }
    }



    // API per ottenere un singolo utente tramite ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    
    
    // API per aggiungere un nuovo utente
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> addUser(@RequestBody User user) {
    		Map<String, String> response = new HashMap<>();
    		
    		user.setPassword(passwordEncoder.encode(user.getPassword()));
    		
            String msg = userService.addUser(user);
            if(msg.equals("Registrazione avvenuta con successo!")) {   	
            BigDecimal Value = new BigDecimal("0.00");
            Budget b = new Budget(Value, Value, Value);
            BudgetService.insert(b, userService.findByUsername(user.getName()).getId());
            }
            response.put("message", msg);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    // API per eliminare un utente
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(HttpServletRequest request, @PathVariable User user) {
    	User user1 = (User) request.getSession().getAttribute("user");
        if (user1 == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Non autenticato");
        }
        	Map<String, String> response = new HashMap<>();
            int deletedRows = userService.deleteUser(user.getId());
            if (deletedRows > 0) {
                response.put("message", "Utente eliminato con successo");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("message", "Utente non trovato");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        
    }
}
