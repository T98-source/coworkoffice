package tasks;

/**
 * Describe a Task with its properties.
 */
public class Locale {

    // the unique id of the office
    private int id;

    private String description;

    private String type;

    private int num_posti;


    /**
     * Office main constructor.
     *
     * @param id represents the office unique identifier
     * @param description the office description
     */
    public Locale(int id, String description, String type, int num_posti) {
        this.id = id;
        this.description = description;
        this.type = type;
        this.num_posti = num_posti;
    }


    /**
     * Overloaded constructor. It create an office without a given id.
     *
     * @param description the task content
     */
    public Locale(String description, String type,  int num_posti) {
        this.id = 0;
        this.description = description;
        this.type = type;
        this.num_posti = num_posti;
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
    public int getNumPosti() {
        return num_posti;
    }

}
