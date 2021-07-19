package RESTService;

import com.google.gson.Gson;
import tasks.Sensore;
import tasksDao.GestioneSensore;

import java.sql.Date;
import java.sql.Time;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;
import static spark.Spark.halt;

public class RESTSensore {


    public static void REST(Gson gson, String baseURL){

        GestioneSensore SensorDao = new GestioneSensore();

        // get all the tasks
        get(baseURL + "/sensors", (request, response) -> {
            // set a proper response code and type
            response.type("application/json");
            response.status(200);

            // get all tasks from the DB
            List<Sensore> allSensors = SensorDao.getAllSensors();
            // prepare the JSON-related structure to return
            Map<String, List<Sensore>> finalJson = new HashMap<>();
            finalJson.put("sensors", allSensors);

            return allSensors;
        }, gson::toJson);


        // get a single task
        get(baseURL + "/sensors/:id", "application/json", (request, response) -> {
            // get the id from the URL
            Sensore Sensor = SensorDao.getSensor(Integer.valueOf(request.params(":id")));

            // no task? 404!
            if(Sensor==null)
                halt(404);

            // prepare the JSON-related structure to return
            // and the suitable HTTP response code and type
            Map<String, Sensore> finalJson = new HashMap<>();
            finalJson.put("Sensor", Sensor);
            response.status(200);
            response.type("application/json");

            return finalJson;
        }, gson::toJson);

        // add a new task
        post(baseURL + "/sensors", "application/json", (request, response) -> {
            // get the body of the HTTP request
            Map addRequest = gson.fromJson(request.body(), Map.class);
            Sensore sensore = null;
            // check whether everything is in place
            if(addRequest!=null && addRequest.containsKey("descrizione") && addRequest.containsKey("localeId")) {

                String descrizione = String.valueOf(addRequest.get("descrizione"));
                int locale_id = Integer.parseInt(String.valueOf(addRequest.get("localeId")));

                // add the task into the DB
                sensore = new Sensore(descrizione, locale_id);
                SensorDao.addSensor(sensore);

                // if success, prepare a suitable HTTP response code
                response.status(201);
            }
            else {
                halt(403);
            }

            return sensore;
        }, gson::toJson);


        delete(baseURL + "/sensors/:id", "application/json", (request, response) -> {
            {

                if(request.params(":id")!=null) {

                    // add the task into the DB
                    SensorDao.deleteSensor(Integer.parseInt(String.valueOf(request.params(":id"))));
                    response.status(201);

                }
                else {
                    halt(403);
                }

                return "";
            }});
    }

}


