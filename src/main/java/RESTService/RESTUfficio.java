package RESTService;

import com.google.gson.Gson;
import tasks.Ufficio;
import tasksDao.GestioneUfficio;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;
import static spark.Spark.halt;

public class RESTUfficio {

    public static void REST(Gson gson, String baseURL){

        GestioneUfficio officeDao = new GestioneUfficio();

        // get all the tasks
        get(baseURL + "/offices", (request, response) -> {
            // set a proper response code and type
            response.type("application/json");
            response.status(200);

            // get all tasks from the DB
            List<Ufficio> allOffices = officeDao.getAllOffices();
            // prepare the JSON-related structure to return
            Map<String, List<Ufficio>> finalJson = new HashMap<>();
            finalJson.put("offices", allOffices);

            return allOffices;
        }, gson::toJson);


        // get a single task
        get(baseURL + "/offices/:id", "application/json", (request, response) -> {
            // get the id from the URL
            Ufficio office = officeDao.getOffice(Integer.valueOf(request.params(":id")));

            // no task? 404!
            if(office==null)
                halt(404);

            // prepare the JSON-related structure to return
            // and the suitable HTTP response code and type
            Map<String, Ufficio> finalJson = new HashMap<>();
            finalJson.put("office", office);
            response.status(200);
            response.type("application/json");

            return finalJson;
        }, gson::toJson);

        // add a new task
        post(baseURL + "/offices", "application/json", (request, response) -> {
            {
                // get the body of the HTTP request
                Map addRequest = gson.fromJson(request.body(), Map.class);
                Ufficio ufficio = null;
                // check whether everything is in place
                if (addRequest != null && addRequest.containsKey("descrizione")) {
                    String descrizione = String.valueOf(addRequest.get("descrizione"));
                    String tipo = "ufficio";

                    // add the task into the DB
                    ufficio = new Ufficio(descrizione, tipo);
                    officeDao.addOffice(ufficio);

                    // if success, prepare a suitable HTTP response code
                    response.status(201);
                } else {
                    halt(403);
                }

                return ufficio;
            }
        }, gson::toJson);


        delete(baseURL + "/offices/:id", "application/json", (request, response) -> {
            {
                if(request.params(":id")!=null) {
                    // add the task into the DB
                officeDao.deleteOffice(Integer.parseInt(String.valueOf(request.params(":id"))));
                    response.status(201);

                }
                else {
                    halt(403);
                }
                return "";
            }});
    }

}
