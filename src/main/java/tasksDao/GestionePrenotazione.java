package tasksDao;

import jwtToken.jwtlib.JWTfun;
import taskModelsJSGrid.Slot;
import tasks.Prenotazione;
import utils.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
        final String sql = "SELECT id, data , ora_inizio, ora_fine, clienti, ufficio_id, utente_id FROM prenotazioni";

        List<Prenotazione> reservations = new LinkedList<>();

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);

            ResultSet rs = st.executeQuery();

            while(rs.next()) {
                Prenotazione t = new Prenotazione(rs.getInt("id"), rs.getDate("data"), rs.getInt("ora_inizio"), rs.getInt("ora_fine"), rs.getInt("clienti"),rs.getInt("ufficio_id"),rs.getString("utente_id"));
                reservations.add(t);
            }

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    public List<Prenotazione> getAllReservationsUser(String utenteId) {
        final String sql = "SELECT id, data, ora_inizio, ora_fine, clienti, ufficio_id, utente_id FROM prenotazioni WHERE utente_id = ?";

        List<Prenotazione> reservations = new LinkedList<>();

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, utenteId);

            ResultSet rs = st.executeQuery();

            while(rs.next()) {
                Prenotazione t = new Prenotazione(rs.getInt("id"), rs.getDate("data"), rs.getInt("ora_inizio"), rs.getInt("ora_fine"), rs.getInt("clienti"),rs.getInt("ufficio_id"),rs.getString("utente_id"));
                reservations.add(t);
            }

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }


    public List<Prenotazione> getAllReservationsOffice(int ufficioId) {
        final String sql = "SELECT id, data, ora_inizio, ora_fine, clienti, ufficio_id, utente_id FROM prenotazioni WHERE ufficio_id = ?";

        List<Prenotazione> reservations = new LinkedList<>();

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, ufficioId);

            ResultSet rs = st.executeQuery();

            while(rs.next()) {
                String utenteId = rs.getString("utente_id");
                Prenotazione t = new Prenotazione(rs.getInt("id"), rs.getDate("data"), rs.getInt("ora_inizio"), rs.getInt("ora_fine"), rs.getInt("clienti"), ufficioId, utenteId);
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
        final String sql = "SELECT id, data, ora_inizio, ora_fine, clienti, ufficio_id, utente_id FROM prenotazioni WHERE id = ?";

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, id);

            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                Reservation = new Prenotazione(id, rs.getDate("data"), rs.getInt("ora_inizio"), rs.getInt("ora_fine"), rs.getInt("clienti"),rs.getInt("ufficio_id"),rs.getString("utente_id"));
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
        final String sql = "INSERT INTO prenotazioni (data, ora_inizio, ora_fine, clienti, ufficio_id, utente_id) VALUES (?,?,?,?,?,?)";

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            st.setDate(1, newReservation.getData());
            st.setInt(2, newReservation.getOraInizio());
            st.setInt(3, newReservation.getOraFine());
            st.setInt(4, newReservation.getClienti());
            st.setInt(5, newReservation.getUfficioId());
            st.setString(6, newReservation.getUtenteId());

            st.executeUpdate();

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteReservation(int id) {
        final String sql = "DELETE FROM prenotazioni WHERE id = ?";

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

    public void deleteReservation(int oraInizio, int ufficioId, String utenteId)
    {
        Prenotazione Reservation = null;
        final String sql = "DELETE FROM prenotazioni WHERE ora_inizio = ? AND ufficio_id = ? AND utente_id = ?";

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, oraInizio);
            st.setInt(2, ufficioId);
            st.setString(3, utenteId);

            st.executeUpdate();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Slot> getSlots(int ufficioId) {
        List<Prenotazione> allReservationsOffice = getAllReservationsOffice(ufficioId);

        int startHour = 8;
        int finalHour = 20;
        // Calcolo slot di tempo
        LocalDate startDate = LocalDate.now();
        LocalDate finalDate = startDate.plusWeeks(2);

        List<Slot> slots = new LinkedList<>();
        LocalDate date = startDate;
        while(!date.equals(finalDate)){
            if(date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
                date = date.plusDays(1);
                continue;
            }

            int nowHour = LocalDateTime.now().getHour() + 1;

            if(date == startDate) {
                if (nowHour < startHour) nowHour = 8;
                else if (nowHour > finalHour){
                    nowHour = 8;
                    date = date.plusDays(1);
                }
            } else nowHour = 8;

            for (int hour=nowHour; hour<finalHour; hour++){
                int state = contains(allReservationsOffice, hour);
                if(state == 0) // Lo slot è libero
                    slots.add(new Slot(hour + "-" + Integer.toString(hour+1), date.toString(), true));
                else if(state == 2)
                    slots.add(new Slot(hour + "-" + Integer.toString(hour+1), date.toString(), false));
            }
            date = date.plusDays(1);
        }

        return slots;
    }

    /**
     * @return 0 -> non contiene la prenotazione, lo slot è libero
     * @return 1 -> contiene la prenotazione, lo slot è occupato da un altro utente
     * @return 2 -> contiene la prenotazione, lo slot è occupato dall'utente che ha fatto la richiesta
     */
    private static int contains(List<Prenotazione> allReservationsOffice, int oraInizio){
        for(Prenotazione prenotazione : allReservationsOffice){
            if(prenotazione.getOraInizio() == oraInizio) {
                String utenteId = JWTfun.getUserId();
                String utenteIdFinal = utenteId.substring(1, utenteId.length()-1);
                if(prenotazione.getUtenteId().equals(utenteIdFinal))
                    return 2;
                else
                    return 1;
            }
        }
        return 0;
    }
}
