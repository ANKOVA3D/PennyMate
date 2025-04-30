package PennyMate.Entity;

public class User {

    private int id;
    private String username;
    private String email;
    private String password;

    public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	// Costruttore
    public User(int id, String name, String email, String password) {
        this.id = id;
        this.username = name;
        this.email = email;
        this.password = password;
    }

    // Getter e Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return username;
    }

    public void setName(String name) {
        this.username = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
