package RESTService;

import com.google.gson.Gson;
import tasks.Misura;
import tasksDao.GestioneMisura;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;
import static spark.Spark.halt;

    public class RESTMisura {


        public static void REST(Gson gson, String baseURL){

            GestioneMisura MeasureDao = new GestioneMisura();


            // get all the tasks
            get(baseURL + "/measures", (request, response) -> {
                // set a proper response code and type
                response.type("application/json");
                response.status(200);

                // get all tasks from the DB
                List<Misura> allMeasures = MeasureDao.getAllMeasures();
                // prepare the JSON-related structure to return
                Map<String, List<Misura>> finalJson = new HashMap<>();
                finalJson.put("measures", allMeasures);

                return allMeasures;
            }, gson::toJson);


            // get a single task
            get(baseURL + "/measures/:id", "application/json", (request, response) -> {
                // get the id from the URL
                Misura Measure = MeasureDao.getMeasure(Integer.valueOf(request.params(":id")));

                // no task? 404!
                if(Measure==null)
                    halt(404);

                // prepare the JSON-related structure to return
                // and the suitable HTTP response code and type
                Map<String, Misura> finalJson = new HashMap<>();
                finalJson.put("Measure", Measure);
                response.status(200);
                response.type("application/json");

                return finalJson;
            }, gson::toJson);

            // add a new task
            post(baseURL + "/measures", "application/json", (request, response) -> {
                // get the body of the HTTP request
                Map addRequest = gson.fromJson(request.body(), Map.class);
                Misura misura = null;
                // check whether everything is in place
                if(addRequest!=null && addRequest.containsKey("tipo") && addRequest.containsKey("misurazione") && addRequest.containsKey("data") && addRequest.containsKey("sensore_id")) {
                    String tipo = String.valueOf(addRequest.get("tipo"));
                    String misurazione = String.valueOf(addRequest.get("misurazione"));
                    Timestamp data = Timestamp.valueOf(String.valueOf(addRequest.get("data")));
                    int sensore_id = Integer.parseInt(String.valueOf(addRequest.get("sensore_id")));

                    // add the task into the DB
                    misura = new Misura(tipo, misurazione, data,sensore_id);
                    MeasureDao.addMeasure(misura);

                    // if success, prepare a suitable HTTP response code
                    response.status(201);
                }
                else {
                    halt(403);
                }

                return misura;
            }, gson::toJson);
        }

    }

