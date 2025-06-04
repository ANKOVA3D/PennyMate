package PennyMate.Entity;

import java.sql.Timestamp;

public class DateResponseDTO {
    private Timestamp wDate;
    private Timestamp mDate;
    private Timestamp yDate;

    public DateResponseDTO(Timestamp wDate, Timestamp mDate, Timestamp yDate) {
        this.wDate = wDate;
        this.mDate = mDate;
        this.yDate = yDate;
    }

    public Timestamp getwDate() {
        return wDate;
    }

    public Timestamp getmDate() {
        return mDate;
    }

    public Timestamp getyDate() {
        return yDate;
    }
}
