package tasksDao;

import spark.QueryParamsMap;
import tasks.Locale;
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
public class GestioneLocale {

    public List<Locale> getAllLocals(QueryParamsMap queryParamsMap) {
        final String sql = "SELECT id, descrizione, tipo FROM locali";

        List<Locale> locals = new LinkedList<>();

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);

            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                Locale t = new Locale(rs.getInt("id"), rs.getString("descrizione"),rs.getString("tipo"));
                locals.add(t);
            }

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return where(queryParamsMap, locals);
    }
    /**
     * Get all offices from the DB
     * @return a list of office, or an empty list if no offices are available
     * @param queryParamsMap
     */
    public List<Locale> getAllOffices(QueryParamsMap queryParamsMap) {
        final String sql = "SELECT id, descrizione, tipo FROM locali WHERE tipo = 'ufficio'";

        List<Locale> offices = new LinkedList<>();

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);

            ResultSet rs = st.executeQuery();

            while (rs.next()) {

                Locale t = new Locale(rs.getInt("id"), rs.getString("descrizione"),rs.getString("tipo"));
                offices.add(t);
            }

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return where(queryParamsMap, offices);
    }

    private List<Locale> where(QueryParamsMap queryParamsMap, List<Locale> locals){
        if(queryParamsMap.hasKey("description") && queryParamsMap.get("description").value().length() != 0){
            for(int i = 0; i<locals.size(); i++){
                if(!locals.get(i).getDescription().toLowerCase().contains(queryParamsMap.get("description").value().toLowerCase())) {
                    locals.remove(i);
                    i--;
                }
            }
        }
        if(queryParamsMap.hasKey("type") && queryParamsMap.get("type").value().length() != 0){
            for(int i = 0; i<locals.size(); i++){
                if(!locals.get(i).getDescription().toLowerCase().contains(queryParamsMap.get("type").value().toLowerCase())) {
                    locals.remove(i);
                    i--;
                }
            }
        }
        return locals;
    }
    /**
     * Get a single task from the DB
     * @param id of the task to retrieve
     * @return the office, or null if not found
     */
    public Locale getOffice(int id)
    {
        Locale office = null;
        final String sql = "SELECT descrizione, tipo FROM locali WHERE id = ?";

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, id);

            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                office = new Locale(id, rs.getString("descrizione"),rs.getString("tipo"));
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
    public void addOffice(Locale newOffice) {
        final String sql = "INSERT INTO locali(descrizione,tipo) VALUES (?,?)";

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, newOffice.getDescription());
            st.setString(2, newOffice.getType());

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
