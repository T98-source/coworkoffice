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

        GestioneAttuatore actuatorDao = new GestioneAttuatore();

        // get all the tasks
        get(baseURL + "/actuators", (request, response) -> {
            // set a proper response code and type
            response.type("application/json");
            response.status(200);

            // get all tasks from the DB
            List<Attuatore> allactuators = actuatorDao.getAllActuators();
            // prepare the JSON-related structure to return

            return allactuators;
        }, gson::toJson);


        // get a single task
        get(baseURL + "/actuators/:id", "application/json", (request, response) -> {
            // get the id from the URL
            Attuatore actuator = actuatorDao.getActuator(Integer.valueOf(request.params(":id")));

            // no task? 404!
            if(actuator==null)
                halt(404);

            // prepare the JSON-related structure to return
            // and the suitable HTTP response code and type
            Map<String, Attuatore> finalJson = new HashMap<>();
            finalJson.put("Actuator", actuator);
            response.status(200);
            response.type("application/json");

            return finalJson;
        }, gson::toJson);

        delete(baseURL + "/actuators/:id", "application/json", (request, response) -> {
            {
                if(request.params(":id")!=null) {
                // add the task into the DB
                actuatorDao.deleteActuator(Integer.parseInt(String.valueOf(request.params(":id"))));
                response.status(201);
                }
                else {
                    halt(403);
                }
                return "";
            }});
    }

}


