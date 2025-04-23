package PennyMate.Transaction;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class Transaction {
    private int id;
    private int userId;
    private BigDecimal amount;
    private boolean type;
    private String category;
    private String description;
    private Timestamp date;

    // Costruttore completo
    public Transaction(int id, int userId, BigDecimal amount, boolean type, String category, String description, Timestamp date) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.type = type;
        this.category = category;
        this.description = description;
        this.date = date;
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public boolean getType() {
		return type;
	}

	public void setType(boolean type) {
		this.type = type;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

   
}
