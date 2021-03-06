package tasksDao;

import jwtToken.jwtlib.JWTfun;
import spark.QueryParamsMap;
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
import java.sql.Date;

/**
 * The DAO for the {@code Task} class.
 */
public class GestionePrenotazione {

    /**
     * Get all reservations from the DB
     * @return a list of Reservation, or an empty list if no reservations are available
     * @param queryParamsMap
     */
    public List<Prenotazione> getAllReservations(QueryParamsMap queryParamsMap) {
        final String sql = "SELECT id, data_p , ora_inizio, ora_fine, clienti, ufficio_id, utente_id FROM prenotazioni";

        List<Prenotazione> reservations = new LinkedList<>();

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);

            ResultSet rs = st.executeQuery();

            Prenotazione old = null;
            while(rs.next()) {
                if(old == null){
                    old = new Prenotazione(rs.getInt("id"), rs.getString("data_p"), rs.getInt("ora_inizio"), rs.getInt("ora_fine"), rs.getInt("clienti"), rs.getInt("ufficio_id"), rs.getString("utente_id"));
                }
                else if(old.getFinalHour() == rs.getInt("ora_fine") && old.getDate().equals(rs.getString("data_p")) && old.getOfficeId() == rs.getInt("ufficio_id") && old.getUserId() == rs.getString("utente_id"))
                    old = new Prenotazione(old.getId(), old.getDate(), old.getStartHour(), rs.getInt("ora_fine"), old.getClients() + rs.getInt("clienti"), old.getOfficeId(), old.getUserId());
                else {
                    reservations.add(old);
                    Prenotazione t = new Prenotazione(rs.getInt("id"), rs.getString("data_p"), rs.getInt("ora_inizio"), rs.getInt("ora_fine"), rs.getInt("clienti"), rs.getInt("ufficio_id"), rs.getString("utente_id"));
                    reservations.add(t);
                }
            }

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    public List<Prenotazione> getAllReservationsUser(String utenteId, QueryParamsMap queryParamsMap) {
        final String sql = "SELECT id, data_p, ora_inizio, ora_fine, clienti, ufficio_id, utente_id FROM prenotazioni WHERE utente_id = ?";

        List<Prenotazione> reservations = new LinkedList<>();

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, utenteId);

            ResultSet rs = st.executeQuery();

            while(rs.next()) {
                Prenotazione t = new Prenotazione(rs.getInt("id"), rs.getString("data_p"), rs.getInt("ora_inizio"), rs.getInt("ora_fine"), rs.getInt("clienti"),rs.getInt("ufficio_id"),rs.getString("utente_id"));
                reservations.add(t);
            }

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return where(queryParamsMap, reservations);
    }

    private List<Prenotazione> where(QueryParamsMap queryParamsMap, List<Prenotazione> reservations){
        if(queryParamsMap.hasKey("date")){
            for(int i = 0; i<reservations.size(); i++){
                if(!reservations.get(i).getDate().toLowerCase().contains(queryParamsMap.get("date").value().toLowerCase())){
                    reservations.remove(i);
                    i--;
                }
            }
        }
        if(queryParamsMap.hasKey("startHour")){
            for(int i = 0; i<reservations.size(); i++){
                if(!String.valueOf(reservations.get(i).getStartHour()).contains(queryParamsMap.get("startHour").value())) {
                    reservations.remove(i);
                    i--;
                }
            }
        }
        if(queryParamsMap.hasKey("finalHour")){
            for(int i = 0; i<reservations.size(); i++){
                if(!String.valueOf(reservations.get(i).getFinalHour()).contains(queryParamsMap.get("finalHour").value())){
                    reservations.remove(i);
                    i--;
                }
            }
        }
        if(queryParamsMap.hasKey("officeId")){
            for(int i = 0; i<reservations.size(); i++){
                if(!String.valueOf(reservations.get(i).getOfficeId()).contains(queryParamsMap.get("officeId").value())){
                    reservations.remove(i);
                    i--;
                }
            }
        }

        return reservations;
    }

    public List<Prenotazione> getAllReservationsOffice(int ufficioId) {
        final String sql = "SELECT id, data_p, ora_inizio, ora_fine, clienti, ufficio_id, utente_id FROM prenotazioni WHERE ufficio_id = ?";

        List<Prenotazione> reservations = new LinkedList<>();

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, ufficioId);

            ResultSet rs = st.executeQuery();

            while(rs.next()) {
                String utenteId = rs.getString("utente_id");
                Prenotazione t = new Prenotazione(rs.getInt("id"), rs.getString("data_p"), rs.getInt("ora_inizio"), rs.getInt("ora_fine"), rs.getInt("clienti"), ufficioId, utenteId);
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
        final String sql = "SELECT id, data_p, ora_inizio, ora_fine, clienti, ufficio_id, utente_id FROM prenotazioni WHERE id = ?";

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, id);

            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                Reservation = new Prenotazione(id, rs.getString("data_p"), rs.getInt("ora_inizio"), rs.getInt("ora_fine"), rs.getInt("clienti"),rs.getInt("ufficio_id"),rs.getString("utente_id"));
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
        final String sql = "INSERT INTO prenotazioni (data_p, ora_inizio, ora_fine, clienti, ufficio_id, utente_id) VALUES (?,?,?,?,?,?)";

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, newReservation.getDate());
            st.setInt(2, newReservation.getStartHour());
            st.setInt(3, newReservation.getFinalHour());
            st.setInt(4, newReservation.getClients());
            st.setInt(5, newReservation.getOfficeId());
            st.setString(6, newReservation.getUserId());

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

    public void deleteReservation(String date, int startHour, int officeId, String userId)
    {
        final String sql = "DELETE FROM prenotazioni WHERE data_p = ? AND ora_inizio = ? AND ufficio_id = ? AND utente_id = ?";

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, date);
            st.setInt(2, startHour);
            st.setInt(3, officeId);
            st.setString(4, userId);

            st.executeUpdate();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Slot> getSlots(int officeId) {
        List<Prenotazione> allReservationsOffice = getAllReservationsOffice(officeId);

        int startHour = 8;
        int finalHour = 20;
        // Calcolo slot di tempo
        LocalDate startDate = LocalDate.now();
        LocalDate finalDate = startDate.plusWeeks(2);

        List<Slot> slots = new LinkedList<>();
        LocalDate date = startDate;

        String userIdRaw = JWTfun.getUserId();
        String userId = userIdRaw.substring(1, userIdRaw.length()-1);

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
                int state = contains(allReservationsOffice, hour, userId, date);
                if(state == 0) // Lo slot ?? libero
                    slots.add(new Slot(hour + ":00-" + Integer.toString(hour+1) + ":00", Date.valueOf(date).toString(), true));
                else if(state == 2) {
                    slots.add(new Slot(hour + ":00-" + Integer.toString(hour+1) + ":00", Date.valueOf(date).toString(), false));
                }
            }
            date = date.plusDays(1);
        }

        return slots;
    }

    /**
     * @return 0 -> non contiene la prenotazione, lo slot ?? libero
     * @return 1 -> contiene la prenotazione, lo slot ?? occupato da un altro utente
     * @return 2 -> contiene la prenotazione, lo slot ?? occupato dall'utente che ha fatto la richiesta
     */
    private static int contains(List<Prenotazione> allReservationsOffice, int startHour, String userId, LocalDate date){
        String dateString = Date.valueOf(date).toString();
        for(Prenotazione prenotazione : allReservationsOffice){
            if(prenotazione.getStartHour() == startHour && prenotazione.getDate().equals(dateString)) {
                if(prenotazione.getUserId().equals(userId))
                    return 2;
                else
                    return 1;
            }
        }
        return 0;
    }
}
