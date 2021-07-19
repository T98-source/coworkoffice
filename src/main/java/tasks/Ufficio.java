package tasks;

/**
 * Describe a Task with its properties.
 */
public class Ufficio {

    // the unique id of the office
    private int id;

    private String descrizione;

    private String tipo;


    /**
     * Office main constructor.
     *
     * @param id represents the office unique identifier
     * @param descrizione the office description
     */
    public Ufficio(int id, String descrizione, String tipo) {
        this.id = id;
        this.descrizione = descrizione;
        this.tipo = tipo;
    }


    /**
     * Overloaded constructor. It create an office without a given id.
     *
     * @param description the task content
     */
    public Ufficio(String description, String tipo) {
        this.id = 0;
        this.descrizione = description;
        this.tipo = tipo;
    }


    /*** Getters **/

    public int getId() {
        return id;
    }

    public String getDescrizione() {
        return descrizione;
    }
    public String getTipo() {
        return tipo;
    }

}
