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
        final String sql = "SELECT id, descrizione, stato, manuale, sensore_id FROM attuatori";

        List<Attuatore> actuators = new LinkedList<>();

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);

            ResultSet rs = st.executeQuery();

            while (rs.next()) {

                Attuatore t = new Attuatore(rs.getInt("id"), rs.getString("descrizione"), rs.getInt("stato"), rs.getInt("manuale"), rs.getInt("sensore_id"));
                actuators.add(t);
            }

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return actuators;
    }

    public Attuatore getActuatorsOfSensor(int sensorId)
    {
        final String sql = "SELECT id, descrizione, stato, manuale FROM attuatori WHERE sensore_id = ?";
        Attuatore actuator = null;
        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, sensorId);

            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                actuator = new Attuatore(rs.getInt("id"), rs.getString("descrizione"), rs.getInt("stato"), rs.getInt("manuale"), sensorId);
            }

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return actuator;
    }

    /**
     * Get a single task from the DB
     * @param id of the task to retrieve
     * @return the Actuator, or null if not found
     */
    public Attuatore getActuator(int id)
    {
        Attuatore actuator = null;
        final String sql = "SELECT id, descrizione, stato, manuale, sensore_id FROM attuatori WHERE id = ?";

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, id);

            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                actuator = new Attuatore(id, rs.getString("descrizione"), rs.getInt("stato"), rs.getInt("manuale"), rs.getInt("sensore_id"));
            }

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return actuator;
    }

    /**
     * Add a new task into the DB
     * @param newActuator the Actuator to be added
     */
    public void addActuator(Attuatore newActuator) {
        final String sql = "INSERT INTO attuatori (descrizione, stato, manuale, sensore_id) VALUES (?,?,?,?)";

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, newActuator.getDescription());
            st.setInt(2, newActuator.getState());
            st.setInt(3, newActuator.getManual());
            st.setInt(4, newActuator.getSensorId());


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