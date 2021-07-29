package RESTService;

import com.google.gson.Gson;

import tasks.Misura;
import tasksDao.GestioneMisura;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;
import static spark.Spark.halt;

    public class RESTMisura {


        public static void REST(Gson gson, String baseURL){

            GestioneMisura measureDao = new GestioneMisura();


            // get all the tasks
            get(baseURL + "/measures", (request, response) -> {
                // set a proper response code and type
                response.type("application/json");
                response.status(200);

                // get all tasks from the DB
                List<Misura> allMeasures = measureDao.getAllMeasures(request.queryMap());

                return allMeasures;
            }, gson::toJson);


            // get a single task
            get(baseURL + "/measures/:id", "application/json", (request, response) -> {
                // get the id from the URL
                Misura measure = measureDao.getMeasure(Integer.valueOf(request.params(":id")));

                // no task? 404!
                if(measure==null)
                    halt(404);

                // prepare the JSON-related structure to return
                // and the suitable HTTP response code and type
                Map<String, Misura> finalJson = new HashMap<>();
                finalJson.put("Measure", measure);
                response.status(200);
                response.type("application/json");

                return finalJson;
            }, gson::toJson);

            // add a new task
            post(baseURL + "/measures", "application/json", (request, response) -> {
                // get the body of the HTTP request
                Map addRequest = gson.fromJson(request.body(), Map.class);
                Misura measure = null;
                // check whether everything is in place
                if(addRequest!=null && addRequest.containsKey("tipo") && addRequest.containsKey("misurazione") && addRequest.containsKey("data") && addRequest.containsKey("sensore_id") && addRequest.containsKey("locale_id")) {
                    String type = String.valueOf(addRequest.get("tipo"));
                    String measurement = String.valueOf(addRequest.get("misurazione"));
                    String data = String.valueOf(addRequest.get("data"));
                    int sensorId = Integer.parseInt(String.valueOf(addRequest.get("sensore_id")));
                    int localId = Integer.parseInt(String.valueOf(addRequest.get("sensore_id")));

                    // add the task into the DB
                    measure = new Misura(type, measurement, data, sensorId, localId);
                    measureDao.addMeasure(measure);

                    // if success, prepare a suitable HTTP response code
                    response.status(201);
                }
                else {
                    halt(403);
                }

                return measure;
            }, gson::toJson);

            delete(baseURL + "/measures/:id", "application/json", (request, response) -> {

                if(request.params(":id")!=null) {
                    // add the task into the DB
                    measureDao.deleteMeasure(Integer.parseInt(String.valueOf(request.params(":id"))));
                    response.status(201);
                }
                else {
                    halt(403);
                }
                return "";
            }, gson::toJson);
        }

    }

