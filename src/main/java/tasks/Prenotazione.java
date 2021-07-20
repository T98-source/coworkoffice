package tasks;


import java.sql.Date;
import java.sql.Time;


/**
 * Describe a Task with its properties.
 */
public class Prenotazione {

    // the unique id of the office
    private int id;

    private Date data;

    private int oraInizio;

    private int oraFine;

    private int clienti;

    private int ufficioId;

    private String utenteId;







    /**
     * Waiting room main constructor.
     *
     * @param id represents the reservation unique identifier
     */
    public Prenotazione(int id, Date data, int oraInizio, int oraFine, int clienti, int ufficioId, String utenteId) {
        this.id = id;
        this.data = data;
        this.oraInizio = oraInizio;
        this.oraFine = oraFine;
        this.clienti = clienti;
        this.ufficioId = ufficioId;
        this.utenteId = utenteId;
    }


    /**
     * Overloaded constructor. It create a reservation without a given id.
     *
     */
    public Prenotazione(Date data, int oraInizio, int oraFine, int clienti, int ufficioId, String utenteId) {
        this.id = 0;
        this.data = data;
        this.oraInizio = oraInizio;
        this.oraFine = oraFine;
        this.clienti = clienti;
        this.ufficioId = ufficioId;
        this.utenteId = utenteId;
    }


    /*** Getters **/

    public int getId() {
        return id;
    }

    public Date getData() {
        return data;
    }

    public int getOraInizio() {
        return oraInizio;
    }

    public int getOraFine() {
        return oraFine;
    }

    public int getClienti(){
        return clienti;
    }

    public int getUfficioId() {
        return ufficioId;
    }

    public String getUtenteId(){
        return utenteId;
    }
}