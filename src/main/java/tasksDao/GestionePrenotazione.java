package tasksDao;

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
        final String sql = "SELECT id, data, ora_inizio, ora_fine, clienti, ufficio_id, utente_id FROM prenotazioni WHERE utente_id=?";

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
        final String sql = "SELECT id, data, ora_inizio, ora_fine, clienti, ufficio_id, utente_id FROM prenotazioni WHERE ufficio_id=?";

        List<Prenotazione> reservations = new LinkedList<>();

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, ufficioId);

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

    public void deleteReservation(int oraInizio, int ufficioId)
    {
        Prenotazione Reservation = null;
        final String sql = "DELETE FROM prenotazioni WHERE ora_inizio = ? AND ufficio_id = ?";

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, oraInizio);
            st.setInt(2, ufficioId);

            st.executeQuery();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Slot> getSlots(List<Prenotazione> allReservations) {
        int startHour = 8;
        int finalHour = 20;
        // Calcolo slot di tempo
        LocalDate startDate = LocalDate.now();
        LocalDate finalDate = startDate.plusWeeks(2);
        List<Integer> startTimes = getStartTimes(allReservations);
        List<Integer> finalTimes = getFinalTimes(allReservations);

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
                if(!contains(hour, startTimes, finalTimes)) // Lo slot è libero
                    slots.add(new Slot(hour + "-" + Integer.toString(hour+1), date.toString()));
            }
            date = date.plusDays(1);
        }

        return slots;
    }

    public static List<Integer> getStartTimes(List<Prenotazione> allReservations){
        List<Integer> startTimes = new LinkedList<>();
        for(Prenotazione prenotazione : allReservations){
            startTimes.add(prenotazione.getOraInizio());
        }
        return startTimes;
    }

    public static List<Integer> getFinalTimes(List<Prenotazione> allReservations){
        List<Integer> finalTimes = new LinkedList<>();
        for(Prenotazione prenotazione : allReservations){
            finalTimes.add(prenotazione.getOraFine());
        }
        return finalTimes;
    }

    public static boolean contains(int oraInizio, List<Integer> startTimes, List<Integer> finalTimes){
        for(int i=0; i<startTimes.size(); i++){
            if(startTimes.get(i) <= oraInizio && finalTimes.get(i) > oraInizio)
                return true;
        }
        return false;
    }
}
