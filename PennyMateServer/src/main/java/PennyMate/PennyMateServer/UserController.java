package PennyMate.PennyMateServer;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import PennyMate.Entity.User;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private Repos userService;

    // API per ottenere tutti gli utenti
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    
    @PostMapping("/login")
    public String login(@RequestBody User user) {
    	String RealPW = userService.findByUsername(user.getName()).getPassword();     
        if (RealPW.equals(user.getPassword())) {
            return "Login effettuato con successo!";
        } else {
            return "Credenziali non valide";
        }
    }

    
    // API per ottenere un singolo utente tramite ID
    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        return userService.getUserById(id);
    }

    // API per aggiungere un nuovo utente
    @PostMapping("/register")
    public int addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    // API per aggiornare un utente esistente
    @PutMapping("/{id}")
    public int updateUser(@PathVariable int id, @RequestBody User user) {
        user.setId(id);
        return userService.updateUser(user);
    }

    // API per eliminare un utente
    @DeleteMapping("/{id}")
    public int deleteUser(@PathVariable int id) {
        return userService.deleteUser(id);
    }
}
