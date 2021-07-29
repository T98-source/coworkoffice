package tasksDao;

import tasks.Sensore;
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
public class GestioneSensore {

    /**
     * Get all Sensors from the DB
     * @return a list of Sensor, or an empty list if no Sensors are available
     */
    public List<Sensore> getAllSensors() {
        final String sql = "SELECT id, descrizione, locale_id FROM sensori";

        List<Sensore> sensors = new LinkedList<>();

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);

            ResultSet rs = st.executeQuery();

            while (rs.next()) {

                Sensore sensor = new Sensore(rs.getInt("id"), rs.getString("descrizione"),rs.getInt("locale_id"));
                sensors.add(sensor);
            }

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sensors;
    }

    /**
     * Get a single task from the DB
     * @param id of the task to retrieve
     * @return the Sensor, or null if not found
     */
    public Sensore getSensor(int id)
    {
        Sensore sensor = null;
        final String sql = "SELECT descrizione, locale_id FROM sensori WHERE id = ?";

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, id);

            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                sensor = new Sensore(id, rs.getString("descrizione"),rs.getInt("locale_id"));
            }

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sensor;
    }

    public Sensore getSensorOfLocal(int localId)
    {
        Sensore sensor = null;
        final String sql = "SELECT id, descrizione, locale_id FROM sensori WHERE locale_id = ?";

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, localId);

            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                sensor = new Sensore(rs.getInt("id"), rs.getString("descrizione"), localId);
            }

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sensor;
    }

    /**
     * Add a new task into the DB
     * @param newSensor the Sensor to be added
     */
    public void addSensor(Sensore newSensor) {
        final String sql = "INSERT INTO sensori(descrizione, locale_id) VALUES (?,?)";

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, newSensor.getDescription());
            st.setInt(2, newSensor.getLocalId());

            st.executeUpdate();

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteSensor(int id) {
        final String sql = "DELETE FROM sensori WHERE id= ?";

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
