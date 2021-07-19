package tasks;

/**
 * Describe a Task with its properties.
 */
public class Sensore {

    // the unique id of the sensor
    private int id;

    private String descrizione;

    private int locale_id;


    /**
     * sensor main constructor.
     *
     * @param id represents the sensor unique identifier
     * @param descrizione the sensor description
     */
    public Sensore(int id, String descrizione, int locale_id) {
        this.id = id;
        this.descrizione = descrizione;
        this.locale_id = locale_id;
    }


    /**
     * Overloaded constructor. It create an sensor without a given id.
     *
     * @param description the task content
     */
    public Sensore(String description, int locale_id) {
        this.id = 0;
        this.descrizione = description;
        this.locale_id = locale_id;
    }


    /*** Getters **/

    public int getId() {
        return id;
    }

    public String getDescrizione() {
        return descrizione;
    }
    public int getlocale_id() {
        return locale_id;
    }

}
