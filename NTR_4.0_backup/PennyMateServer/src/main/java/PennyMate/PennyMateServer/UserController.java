package PennyMate.PennyMateServer;

import java.util.HashMap;
import java.util.List;
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

import PennyMate.Entity.User;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost", allowCredentials = "true")
public class UserController {

    @Autowired
    private Repos userService;
    
    // API per ottenere tutti gli utenti
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody User user, HttpServletRequest request) {
        Map<String, String> response = new HashMap<>();
        
        // Verifica se l'utente esiste nel database
        User foundUser = userService.findByUsername(user.getName());

        if (foundUser == null || !foundUser.getPassword().equals(user.getPassword())) {
            response.put("message", "Credenziali non valide");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        // Salva l'utente nella sessione
        request.getSession().setAttribute("user", foundUser);

        response.put("message", "Login effettuato con successo");
        response.put("redirect", "/dashboard"); 
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
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Non autenticato");
        }
    }
    
    @PutMapping("/update")
    public ResponseEntity<?> updateUserProfile(HttpServletRequest request, @RequestBody Map<String, String> updates) {
        User sessionUser = (User) request.getSession().getAttribute("user");
        if (sessionUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Non autenticato");
        }

        // Ottieni l'utente completo dal database
        User user = userService.getUserById(sessionUser.getId());
        
        // Applica gli aggiornamenti
        if (updates.containsKey("username")) {
            user.setName(updates.get("username"));
        }
        if (updates.containsKey("email")) {
            user.setEmail(updates.get("email"));
        }
        if (updates.containsKey("password") && !updates.get("password").isEmpty()) {
            user.setPassword(updates.get("password"));
        }

        String result = userService.updateUser(user, user.getId());
        if (result.equals("Profilo aggiornato con successo!")) {
            // Aggiorna l'utente nella sessione
            request.getSession().setAttribute("user", user);
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
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
        try {
            userService.addUser(user);
            response.put("message", "Utente registrato con successo");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            response.put("message", "Errore durante la registrazione");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // API per aggiornare un utente esistente
    @PutMapping("/editprofile")
    public ResponseEntity<?> updateUser(HttpServletRequest request, @RequestBody User user) {
    	User user1 = (User) request.getSession().getAttribute("user");
        if (user1 == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Non autenticato");
        }
        String updatedRows = userService.updateUser(user, userService.findByUsername(user1.getName()).getId());
        
        if (updatedRows.equals("Profilo aggiornato con successo!")) {
            return new ResponseEntity<>(updatedRows, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(updatedRows, HttpStatus.NOT_FOUND);
        }
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
