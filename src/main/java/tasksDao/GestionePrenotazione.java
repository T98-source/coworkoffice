package tasksDao;

import tasks.Prenotazione;
import utils.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * The DAO for the {@code Task} class.
 */
public class GestionePrenotazione {

    /**
     * Get all reservations from the DB
     * @return a list of Reservation, or an empty list if no reservations are available
     */
    public List<Prenotazione> getAllReservations() {
        final String sql = "SELECT id, descrizione, data , ora_inizio, ora_fine, clienti, ufficio_id, utente_id FROM prenotazioni";

        List<Prenotazione> reservations = new LinkedList<>();

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);

            ResultSet rs = st.executeQuery();

            while (rs.next()) {

                Prenotazione t = new Prenotazione(rs.getInt("id"), rs.getString("descrizione"), rs.getDate("data"), rs.getTime("ora_inizio"), rs.getTime("ora_fine"), rs.getInt("clienti"),rs.getInt("ufficio_id"),rs.getInt("utente_id"));
                reservations.add(t);
            }

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    public List<Prenotazione> getAllReservationsUser(String utente_id) {
        final String sql = "SELECT id, descrizione, data , ora_inizio, ora_fine, clienti, ufficio_id, utente_id FROM prenotazioni WHERE utente_id=?";

        List<Prenotazione> reservations = new LinkedList<>();

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, utente_id);

            ResultSet rs = st.executeQuery();

            while (rs.next()) {

                Prenotazione t = new Prenotazione(rs.getInt("id"), rs.getString("descrizione"), rs.getDate("data"), rs.getTime("ora_inizio"), rs.getTime("ora_fine"), rs.getInt("clienti"),rs.getInt("ufficio_id"),rs.getInt("utente_id"));
                reservations.add(t);
            }

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }



    /**
     * Get a single task from the DB
     * @param id of the task to retrieve
     * @return the Reservation, or null if not found
     */
    public Prenotazione getReservation(int id)
    {
        Prenotazione Reservation = null;
        final String sql = "SELECT id, descrizione, data, ora_inizio, ora_fine, clienti, ufficio_id, utente_id FROM prenotazioni WHERE id = ?";

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, id);

            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                Reservation = new Prenotazione(id, rs.getString("descrizione"), rs.getDate("data"), rs.getTime("ora_inizio"), rs.getTime("ora_fine"), rs.getInt("clienti"),rs.getInt("ufficio_id"),rs.getInt("utente_id"));
            }

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Reservation;
    }

    /**
     * Add a new task into the DB
     * @param newReservation the Reservation to be added
     */
    public void addReservation(Prenotazione newReservation) {
        final String sql = "INSERT INTO prenotazioni (descrizione, data, ora_inizio, ora_fine, clienti, ufficio_id, utente_id) VALUES (?,?,?,?,?,?,?)";

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, newReservation.getDescrizione());
            st.setDate(2, newReservation.getData());
            st.setTime(3, newReservation.getora_inizio());
            st.setTime(4, newReservation.getora_fine());
            st.setInt(5, newReservation.getClienti());
            st.setInt(6, newReservation.getufficio_id());
            st.setInt(7, newReservation.getutente_id());

            st.executeUpdate();

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteReservation(int id) {
        final String sql = "DELETE FROM prenotazioni WHERE id= ?";

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, id);

            st.executeUpdate();

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
