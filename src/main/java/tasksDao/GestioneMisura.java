package tasksDao;

import spark.QueryParamsMap;
import tasks.Misura;
import tasks.Prenotazione;
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
public class GestioneMisura {

    /**
     * Get all measures from the DB
     * @return a list of Measure, or an empty list if no measures are available
     * @param queryParamsMap
     */
    public List<Misura> getAllMeasures(QueryParamsMap queryParamsMap) {
        final String sql = "SELECT id, tipo, misurazione, data, sensore_id, locale_id FROM misure";

        List<Misura> measures = new LinkedList<>();

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);

            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                String tipo = rs.getString("tipo");
                String measurement = rs.getString("misurazione");
                if(tipo.equals("temperatura")) {
                    tipo = "Temperatura.";
                    measurement += "°C";
                }
                else{
                    measurement += "%";
                    if(tipo.equals("umidita"))
                        tipo = "Umidità.";
                    else if(tipo.equals("luminosita"))
                        tipo = "Luminosità..";
                    else if(tipo.equals("gas"))
                        tipo = "Gas.";
                }
                Misura t = new Misura(rs.getInt("id"), tipo, measurement, rs.getString("data"), rs.getInt("sensore_id"), rs.getInt("locale_id"));
                measures.add(t);
            }

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return where(queryParamsMap, measures);
    }

    private List<Misura> where(QueryParamsMap queryParamsMap, List<Misura> measures){
        if(queryParamsMap.hasKey("type")){
            for(int i = 0; i<measures.size(); i++){
                if(!measures.get(i).getType().toLowerCase().contains(queryParamsMap.get("type").value().toLowerCase())){
                    measures.remove(i);
                    i--;
                }
            }
        }
        if(queryParamsMap.hasKey("measurement")){
            for(int i = 0; i<measures.size(); i++){
                if(!String.valueOf(measures.get(i).getMeasurement()).contains(queryParamsMap.get("measurement").value())) {
                    measures.remove(i);
                    i--;
                }
            }
        }
        if(queryParamsMap.hasKey("dateTime")){
            for(int i = 0; i<measures.size(); i++){
                if(!String.valueOf(measures.get(i).getDateTime()).contains(queryParamsMap.get("dateTime").value())){
                    measures.remove(i);
                    i--;
                }
            }
        }
        if(queryParamsMap.hasKey("localId")){
            for(int i = 0; i<measures.size(); i++){
                if(!String.valueOf(measures.get(i).getLocalId()).contains(queryParamsMap.get("localId").value())){
                    measures.remove(i);
                    i--;
                }
            }
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
                Measure = new Misura(id, rs.getString("tipo"), rs.getString("misurazione"), rs.getString("data"), rs.getInt("sensore_id"), rs.getInt("locale_id"));
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
        final String sql = "INSERT INTO misure(tipo, misurazione, data, sensore_id, locale_id) VALUES (?,?,?,?,?)";

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, newMeasure.getType());
            st.setString(2, newMeasure.getMeasurement());
            st.setString(3, newMeasure.getDateTime());
            st.setInt(4, newMeasure.getSensorId());
            st.setInt(5, newMeasure.getLocalId());

            st.executeUpdate();

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteMeasure(int id) {
        final String sql = "DELETE FROM misure WHERE id = ?";

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
