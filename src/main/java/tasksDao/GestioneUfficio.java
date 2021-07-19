package tasksDao;

import tasks.Ufficio;
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
public class GestioneUfficio {

    /**
     * Get all offices from the DB
     * @return a list of office, or an empty list if no offices are available
     */
    public List<Ufficio> getAllOffices() {
        final String sql = "SELECT id, descrizione, tipo FROM locali";

        List<Ufficio> offices = new LinkedList<>();

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);

            ResultSet rs = st.executeQuery();

            while (rs.next()) {

                Ufficio t = new Ufficio(rs.getInt("id"), rs.getString("descrizione"),rs.getString("tipo"));
                offices.add(t);
            }

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return offices;
    }

    /**
     * Get a single task from the DB
     * @param id of the task to retrieve
     * @return the office, or null if not found
     */
    public Ufficio getOffice(int id)
    {
        Ufficio office = null;
        final String sql = "SELECT descrizione, tipo FROM locali WHERE id = ?";

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, id);

            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                office = new Ufficio(id, rs.getString("descrizione"),rs.getString("tipo"));
            }

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return office;
    }

    /**
     * Add a new task into the DB
     * @param newOffice the office to be added
     */
    public void addOffice(Ufficio newOffice) {
        final String sql = "INSERT INTO locali(descrizione,tipo) VALUES (?,?)";

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, newOffice.getDescrizione());
            st.setString(2, newOffice.getTipo());

            st.executeUpdate();

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteOffice(int id) {
        final String sql = "DELETE FROM locali WHERE id= ?";

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
