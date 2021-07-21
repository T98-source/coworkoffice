package taskModelsJSGrid;

import java.sql.Date;

public class Slot {

    private String schedule;
    private Date date;
    private boolean free;

    public Slot(String schedule, Date date, boolean free){
        this.schedule = schedule;
        this.date = date;
        this.free = free;
    }
}
