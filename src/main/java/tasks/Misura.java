package tasks;


import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.concurrent.TimeoutException;

/**
 * Describe a Task with its properties.
 */
public class Misura {

    // the unique id of the office
    private int id;

    private String tipo;

    private String misurazione;

    private Timestamp data;

    private int sensore_id;



    /**
     * Waiting room main constructor.
     *
     * @param id represents the reservation unique identifier
     * @param tipo the reservation description
     */
    public Misura(int id, String tipo, String misurazione, Timestamp data, int sensore_id) {
        this.id = id;
        this.tipo = tipo;
        this.misurazione = misurazione;
        this.data = data;
        this.sensore_id = sensore_id;
    }


    /**
     * Overloaded constructor. It create a reservation without a given id.
     *
     * @param tipo the task content
     */
    public Misura(String tipo, String misurazione, Timestamp data, int sensore_id) {
        this.id = 0;
        this.tipo = tipo;
        this.misurazione = misurazione;
        this.data = data;
        this.sensore_id = sensore_id;
    }


    /*** Getters **/

    public int getId() {
        return id;
    }

    public String getTipo() {
        return tipo;
    }

    public String getMisurazione() {
        return misurazione;
    }

    public Timestamp getdata() {
        return data;
    }

    public int getsensore_id(){ return sensore_id; }


}