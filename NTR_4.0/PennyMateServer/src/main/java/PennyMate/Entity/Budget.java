package PennyMate.Entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Budget {
    private int id;
    private int userId;
    private BigDecimal Week;
    private BigDecimal Month;
    private BigDecimal Year;
    

    public Budget(int id, BigDecimal week, BigDecimal month, BigDecimal year) {
		this.id = id;
		Week = week;
		Month = month;
		Year = year;
	}

	// Getters e Setters
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

	public BigDecimal getWeek() {
		return Week;
	}

	public void setWeek(BigDecimal week) {
		Week = week;
	}

	public BigDecimal getMonth() {
		return Month;
	}

	public void setMonth(BigDecimal month) {
		Month = month;
	}

	public BigDecimal getYear() {
		return Year;
	}

	public void setYear(BigDecimal year) {
		Year = year;
	}

    
}
