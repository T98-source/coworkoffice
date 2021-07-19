package tasksDao;

import tasks.Misura;
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
public class GestioneMisura {

    /**
     * Get all measures from the DB
     * @return a list of Measure, or an empty list if no measures are available
     */
    public List<Misura> getAllMeasures() {
        final String sql = "SELECT id, tipo, misurazione, data, sensore_id FROM misure";

        List<Misura> measures = new LinkedList<>();

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);

            ResultSet rs = st.executeQuery();

            while (rs.next()) {

                Misura t = new Misura(rs.getInt("id"), rs.getString("tipo"), rs.getString("misurazione"), rs.getTimestamp("data"), rs.getInt("sensore_id"));
                measures.add(t);
            }

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return measures;
    }

    /**
     * Get a single task from the DB
     * @param id of the task to retrieve
     * @return the Measure, or null if not found
     */
    public Misura getMeasure(int id)
    {
        Misura Measure = null;
        final String sql = "SELECT id, tipo, misurazione, data, sensore_id FROM misure WHERE id = ?";

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, id);

            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                Measure = new Misura(id, rs.getString("tipo"),rs.getString("misurazione"), rs.getTimestamp("data"), rs.getInt("sensore_id"));
            }

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Measure;
    }

    /**
     * Add a new task into the DB
     * @param newMeasure the Measure to be added
     */
    public void addMeasure(Misura newMeasure) {
        final String sql = "INSERT INTO misure(tipo, misurazione, data, sensore_id) VALUES (?,?,?,?)";

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, newMeasure.getTipo());
            st.setString(2, newMeasure.getMisurazione());
            st.setTimestamp(3, newMeasure.getdata());
            st.setInt(4, newMeasure.getsensore_id());

            st.executeUpdate();

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
