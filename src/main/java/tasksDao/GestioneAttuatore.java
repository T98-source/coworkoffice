package tasksDao;

import tasks.Attuatore;
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
public class GestioneAttuatore {

    /**
     * Get all Actuators from the DB
     * @return a list of Actuator, or an empty list if no Actuators are available
     */
    public List<Attuatore> getAllActuators() {
        final String sql = "SELECT id, descrizione, stato, locale_id FROM attuatori";

        List<Attuatore> Actuators = new LinkedList<>();

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);

            ResultSet rs = st.executeQuery();

            while (rs.next()) {

                Attuatore t = new Attuatore(rs.getInt("id"), rs.getString("descrizione"), rs.getInt("stato"),  rs.getInt("locale_id"));
                Actuators.add(t);
            }

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Actuators;
    }

    /**
     * Get a single task from the DB
     * @param id of the task to retrieve
     * @return the Actuator, or null if not found
     */
    public Attuatore getActuator(int id)
    {
        Attuatore Actuator = null;
        final String sql = "SELECT id, descrizione stato, locale_id FROM attuatori WHERE id = ?";

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, id);

            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                Actuator = new Attuatore(id, rs.getString("descrizione"), rs.getInt("stato"),  rs.getInt("locale_id"));
            }

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Actuator;
    }

    /**
     * Add a new task into the DB
     * @param newActuator the Actuator to be added
     */
    public void addActuator(Attuatore newActuator) {
        final String sql = "INSERT INTO attuatori (descrizione, stato, locale_id) VALUES (?,?,?)";

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, newActuator.getDescrizione());
            st.setInt(2, newActuator.getStato());
            st.setInt(3, newActuator.getlocale_id());


            st.executeUpdate();

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteActuator(int id) {
        final String sql = "DELETE FROM attuatori WHERE id= ?";

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