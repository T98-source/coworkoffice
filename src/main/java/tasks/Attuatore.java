package tasks;

/**
 * Describe a Task with its properties.
 */
public class Attuatore {

    // the unique id of the office
    private int id;

    private String descrizione;

    private int stato;

    private int locale_id;


    /**
     * Office main constructor.
     *
     * @param id represents the office unique identifier
     * @param descrizione the office description
     */
    public Attuatore(int id, String descrizione, int stato, int locale_id) {
        this.id = id;
        this.descrizione = descrizione;
        this.stato = stato;
        this.locale_id=locale_id;
    }


    /**
     * Overloaded constructor. It create an office without a given id.
     *
     * @param description the task content
     */
    public Attuatore(String description, int stato, int locale_id ) {
        this.id = 0;
        this.descrizione = description;
        this.stato = stato;
        this.locale_id=locale_id;
    }


    /*** Getters **/

    public int getId() {
        return id;
    }
    public String getDescrizione() {
        return descrizione;
    }
    public int getStato() {
        return stato;
    }
    public int getlocale_id() {
        return locale_id;
    }


}
