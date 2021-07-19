package RESTService;

import com.google.gson.Gson;
import tasks.Attuatore;
import tasksDao.GestioneAttuatore;

import java.sql.Date;
import java.sql.Time;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;
import static spark.Spark.halt;

public class RESTAttuatore {


    public static void REST(Gson gson, String baseURL){

        GestioneAttuatore ActuatorDao = new GestioneAttuatore();

        // get all the tasks
        get(baseURL + "/actuators", (request, response) -> {
            // set a proper response code and type
            response.type("application/json");
            response.status(200);

            // get all tasks from the DB
            List<Attuatore> allactuators = ActuatorDao.getAllActuators();
            // prepare the JSON-related structure to return
            Map<String, List<Attuatore>> finalJson = new HashMap<>();
            finalJson.put("actuators", allactuators);

            return allactuators;
        }, gson::toJson);


        // get a single task
        get(baseURL + "/actuators/:id", "application/json", (request, response) -> {
            // get the id from the URL
            Attuatore Actuator = ActuatorDao.getActuator(Integer.valueOf(request.params(":id")));

            // no task? 404!
            if(Actuator==null)
                halt(404);

            // prepare the JSON-related structure to return
            // and the suitable HTTP response code and type
            Map<String, Attuatore> finalJson = new HashMap<>();
            finalJson.put("Actuator", Actuator);
            response.status(200);
            response.type("application/json");

            return finalJson;
        }, gson::toJson);

        // add a new task
        post(baseURL + "/actuators", "application/json", (request, response) -> {
            // get the body of the HTTP request
            Map addRequest = gson.fromJson(request.body(), Map.class);
            Attuatore attuatore = null;
            // check whether everything is in place
            if(addRequest!=null && addRequest.containsKey("descrizione") && addRequest.containsKey("stato") && addRequest.containsKey("localeId")) {
                String descrizione = String.valueOf(addRequest.get("descrizione"));
                int stato = Integer.parseInt(String.valueOf(addRequest.get("stato")));
                int locale_id = Integer.parseInt(String.valueOf(addRequest.get("localeId")));

                // add the task into the DB
                attuatore = new Attuatore(descrizione, stato, locale_id);
                ActuatorDao.addActuator(attuatore);

                // if success, prepare a suitable HTTP response code
                response.status(201);
            }
            else {
                halt(403);
            }

            return attuatore;
        }, gson::toJson);


        delete(baseURL + "/actuators/:id", "application/json", (request, response) -> {
            {
                if(request.params(":id")!=null) {
                // add the task into the DB
                ActuatorDao.deleteActuator(Integer.parseInt(String.valueOf(request.params(":id"))));
                response.status(201);
                }
                else {
                    halt(403);
                }
                return "";
            }});
    }

}


