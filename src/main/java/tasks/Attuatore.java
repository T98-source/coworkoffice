package tasks;

/**
 * Describe a Task with its properties.
 */
public class Attuatore {

    // the unique id of the office
    private int id;

    private String description;

    private int state;

    private int manual;

    private int sensorId;


    /**
     * Office main constructor.
     *
     * @param id represents the office unique identifier
     */
    public Attuatore(int id, String description, int state, int manual, int sensorId) {
        this.id = id;
        this.description = description;
        this.state = state;
        this.manual = manual;
        this.sensorId = sensorId;
    }


    /**
     * Overloaded constructor. It create an office without a given id.
     *
     * @param description the task content
     */
    public Attuatore(String description, int state, int manual,  int sensorId ) {
        this.id = 0;
        this.description = description;
        this.state = state;
        this.manual = manual;
        this.sensorId = sensorId;
    }


    /*** Getters **/

    public int getId() {
        return id;
    }
    public String getDescription() {
        return description;
    }
    public int getState() {
        return state;
    }
    public int getManual() { return manual; }
    public int getSensorId() {
        return sensorId;
    }


}
