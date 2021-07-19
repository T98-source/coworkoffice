package RESTService;

import com.google.gson.Gson;
import tasks.Utente;
import tasksDao.GestioneUtente;

import java.sql.Date;
import java.sql.Time;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;
import static spark.Spark.halt;

public class RESTUtente {


    public static void REST(Gson gson, String baseURL){

        GestioneUtente UserDao = new GestioneUtente();

        // get all the tasks
        get(baseURL + "/users", (request, response) -> {
            // set a proper response code and type
            response.type("application/json");
            response.status(200);

            // get all tasks from the DB
            List<Utente> allUsers = UserDao.getAllUsers();
            // prepare the JSON-related structure to return
            Map<String, List<Utente>> finalJson = new HashMap<>();
            finalJson.put("users", allUsers);

            return allUsers;
        }, gson::toJson);


        // get a single task
        get(baseURL + "/users/:id", "application/json", (request, response) -> {
            // get the id from the URL
            Utente User = UserDao.getUser(String.valueOf(request.params(":id")));

            // no task? 404!
            if(User==null)
                halt(404);

            // prepare the JSON-related structure to return
            // and the suitable HTTP response code and type
            Map<String, Utente> finalJson = new HashMap<>();
            finalJson.put("User", User);
            response.status(200);
            response.type("application/json");

            return finalJson;
        }, gson::toJson);

        // add a new task
        post(baseURL + "/users", "application/json", (request, response) -> {
            // get the body of the HTTP request
            Map addRequest = gson.fromJson(request.body(), Map.class);
            
            // check whether everything is in place
            if(addRequest!=null && addRequest.containsKey("nome") && addRequest.containsKey("cognome") && addRequest.containsKey("email") && addRequest.containsKey("password")) {
                String nome = String.valueOf(addRequest.get("nome"));
                String cognome = String.valueOf(addRequest.get("cognome"));
                String email = String.valueOf(addRequest.get("email"));
                String password = String.valueOf(addRequest.get("password"));


                // add the task into the DB
                UserDao.addUser(new Utente(nome, cognome, email, password));

                // if success, prepare a suitable HTTP response code
                response.status(201);
            }
            else {
                halt(403);
            }

            return "";
        }, gson::toJson);
    }

}



