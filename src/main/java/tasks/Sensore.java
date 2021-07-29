package tasks;

/**
 * Describe a Task with its properties.
 */
public class Sensore {

    // the unique id of the sensor
    private int id;

    private String description;

    private int localId;


    /**
     * sensor main constructor.
     *
     * @param id represents the sensor unique identifier
     * @param description the sensor description
     */
    public Sensore(int id, String description, int localId) {
        this.id = id;
        this.description = description;
        this.localId = localId;
    }


    /**
     * Overloaded constructor. It create an sensor without a given id.
     *
     * @param description the task content
     */
    public Sensore(String description, int localId) {
        this.id = 0;
        this.description = description;
        this.localId = localId;
    }


    /*** Getters **/

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
    public int getLocalId() {
        return localId;
    }

}
