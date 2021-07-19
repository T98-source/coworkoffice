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

        List<Sensore> Sensors = new LinkedList<>();

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);

            ResultSet rs = st.executeQuery();

            while (rs.next()) {

                Sensore t = new Sensore(rs.getInt("id"), rs.getString("descrizione"),rs.getInt("locale_id"));
                Sensors.add(t);
            }

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Sensors;
    }

    /**
     * Get a single task from the DB
     * @param id of the task to retrieve
     * @return the Sensor, or null if not found
     */
    public Sensore getSensor(int id)
    {
        Sensore Sensor = null;
        final String sql = "SELECT descrizione, locale_id FROM sensori WHERE id = ?";

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, id);

            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                Sensor = new Sensore(id, rs.getString("descrizione"),rs.getInt("locale_id"));
            }

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Sensor;
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
            st.setString(1, newSensor.getDescrizione());
            st.setInt(2, newSensor.getlocale_id());

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
