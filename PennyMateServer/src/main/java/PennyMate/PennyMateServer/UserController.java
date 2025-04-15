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
@CrossOrigin(origins = "http://localhost")
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
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user != null) {
            return ResponseEntity.ok(Map.of("username", user.getName())); // Ritorna il nome utente
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Non autenticato"); // Se l'utente non è loggato
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
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateUser(@PathVariable int id, @RequestBody User user) {
        Map<String, String> response = new HashMap<>();
        user.setId(id);
        int updatedRows = userService.updateUser(user);
        if (updatedRows > 0) {
            response.put("message", "Utente aggiornato con successo");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("message", "Utente non trovato");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    // API per eliminare un utente
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable int id) {
        Map<String, String> response = new HashMap<>();
        int deletedRows = userService.deleteUser(id);
        if (deletedRows > 0) {
            response.put("message", "Utente eliminato con successo");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("message", "Utente non trovato");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
}
