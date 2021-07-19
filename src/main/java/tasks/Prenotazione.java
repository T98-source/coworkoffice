package tasks;


import java.sql.Date;
import java.sql.Time;


/**
 * Describe a Task with its properties.
 */
public class Prenotazione {

    // the unique id of the office
    private int id;

    private String descrizione;

    private Date data;

    private Time ora_inizio;

    private Time ora_fine;

    private int clienti;

    private int ufficio_id;

    private String utente_id;







    /**
     * Waiting room main constructor.
     *
     * @param id represents the reservation unique identifier
     * @param descrizione the reservation description
     */
    public Prenotazione(int id, String descrizione, Date data, Time ora_inizio, Time ora_fine, int clienti, int ufficio_id, String utente_id) {
        this.id = id;
        this.descrizione = descrizione;
        this.data = data;
        this.ora_inizio = ora_inizio;
        this.ora_fine = ora_fine;
        this.clienti = clienti;
        this.ufficio_id = ufficio_id;
        this.utente_id=utente_id;
    }


    /**
     * Overloaded constructor. It create a reservation without a given id.
     *
     * @param description the task content
     */
    public Prenotazione(String description, Date data, Time ora_inizio, Time ora_fine, int clienti, int ufficio_id, String utente_id) {
        this.id = 0;
        this.descrizione = description;
        this.data = data;
        this.ora_inizio = ora_inizio;
        this.ora_fine = ora_fine;
        this.clienti = clienti;
        this.ufficio_id = ufficio_id;
        this.utente_id=utente_id;
    }


    /*** Getters **/

    public int getId() {
        return id;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public Date getData() {
        return data;
    }

    public Time getora_inizio() {
        return ora_inizio;
    }

    public Time getora_fine() {
        return ora_fine;
    }

    public int getClienti(){return clienti; }

    public int getufficio_id() {
        return ufficio_id;
    }

    public String getutente_id(){ return utente_id; }


}