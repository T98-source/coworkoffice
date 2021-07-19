package tasks;


import java.sql.Date;
import java.sql.Time;


/**
 * Describe a Task with its properties.
 */
public class Utente {

    // the unique id of the office
    private String id;

    private String nome;

    private String cognome;

    private String email;

    private String password;



    /**
     * Waiting room main constructor.
     *
     * @param id represents the reservation unique identifier
     * @param nome the user name
     */
    public Utente(String id, String nome, String cognome, String email, String password) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
    }


    /**
     * Overloaded constructor. It create a reservation without a given id.
     *
     * @param nome the task content
     */
    public Utente(String nome, String cognome, String email, String password) {
        this.id = "0";
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
    }


    /*** Getters **/

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }


}