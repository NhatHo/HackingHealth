package hackinghealth.com.whatsapnea;

/**
 * Created by paull on 2017-04-30.
 */

public class PersistentUser {
    private int id;
    private String name;
    private int bdate;
    private int bmonth;
    private int byear;
    private String history;
    private String allergies;
    private int start;
    private int end;
    private int duration;
    private int occurance;

    public PersistentUser (
            int id,
            String name,
            int bdate,
            int bmonth,
            int byear,
            String history,
            String allergies,
            int start,
            int end,
            int duration,
            int occurance) {
        this.id = id;
        this.name = name;
        this.bdate = bdate;
        this.bmonth = bmonth;
        this.byear = byear;
        this.history = history;
        this.allergies = allergies;
        this.start = start;
        this.end = end;
        this.duration = duration;
        this.occurance = occurance;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getBdate() {
        return bdate;
    }

    public int getBmonth() {
        return bmonth;
    }

    public int getByear() {
        return byear;
    }

    public String getHistory() {
        return history;
    }

    public String getAllergies() {
        return allergies;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public int getDuration() {
        return duration;
    }

    public int getOccurance() {
        return occurance;
    }
}
