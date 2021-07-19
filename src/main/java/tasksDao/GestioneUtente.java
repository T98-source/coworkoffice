package tasksDao;

import tasks.Utente;
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
public class GestioneUtente {

    /**
     * Get all Users from the DB
     * @return a list of User, or an empty list if no Users are available
     */
    public List<Utente> getAllUsers() {
        final String sql = "SELECT id, nome, cognome, email, password FROM utenti";

        List<Utente> Users = new LinkedList<>();

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);

            ResultSet rs = st.executeQuery();

            while (rs.next()) {

                Utente t = new Utente(rs.getString("id"), rs.getString("nome"),rs.getString("cognome"),rs.getString("email"),rs.getString("password"));
                Users.add(t);
            }

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Users;
    }

    /**
     * Get a single task from the DB
     * @param id of the task to retrieve
     * @return the User, or null if not found
     */
    public Utente getUser(String id)
    {
        Utente User = null;
        final String sql = "SELECT id, nome, cognome, email, password FROM utenti WHERE id = ?";

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, id);

            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                User = new Utente(id, rs.getString("nome"),rs.getString("cognome"),rs.getString("email"),rs.getString("password"));
            }

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return User;
    }

    /**
     * Add a new task into the DB
     * @param newUser the User to be added
     */
    public void addUser(Utente newUser) {
        final String sql = "INSERT INTO utenti(nome,cognome,email,password) VALUES (?,?,?,?)";

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, newUser.getNome());
            st.setString(2, newUser.getCognome());
            st.setString(3, newUser.getEmail());
            st.setString(4, newUser.getPassword());

            st.executeUpdate();

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(String descrizione) {
        final String sql = "DELETE FROM utenti WHERE descrizione= ?";

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, descrizione);

            st.executeUpdate();

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}