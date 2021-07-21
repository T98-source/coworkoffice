package tasks;

/**
 * Describe a Task with its properties.
 */
public class Ufficio {

    // the unique id of the office
    private int id;

    private String description;

    private String type;


    /**
     * Office main constructor.
     *
     * @param id represents the office unique identifier
     * @param description the office description
     */
    public Ufficio(int id, String description, String type) {
        this.id = id;
        this.description = description;
        this.type = type;
    }


    /**
     * Overloaded constructor. It create an office without a given id.
     *
     * @param description the task content
     */
    public Ufficio(String description, String type) {
        this.id = 0;
        this.description = description;
        this.type = type;
    }


    /*** Getters **/

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
    public String getType() {
        return type;
    }

}
