package RESTService;

import com.google.gson.Gson;
import tasks.Locale;
import tasksDao.GestioneLocale;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;
import static spark.Spark.halt;

public class RESTLocale {

    public static void REST(Gson gson, String baseURL){

        GestioneLocale localDao = new GestioneLocale();

        // get all the tasks
        get(baseURL + "/locals", (request, response) -> {
            // set a proper response code and type
            response.type("application/json");
            response.status(200);

            // get all tasks from the DB
            List<Locale> allLocals = localDao.getAllLocals(request.queryMap());

            return allLocals;
        }, gson::toJson);

        // get all the tasks
        get(baseURL + "/offices", (request, response) -> {
            // set a proper response code and type
            response.type("application/json");
            response.status(200);

            // get all tasks from the DB
            List<Locale> allOffices = localDao.getAllOffices(request.queryMap());

            return allOffices;
        }, gson::toJson);


        // get a single task
        get(baseURL + "/offices/:id", "application/json", (request, response) -> {
            // get the id from the URL
            Locale office = localDao.getOffice(Integer.valueOf(request.params(":id")));

            // no task? 404!
            if(office==null)
                halt(404);

            // prepare the JSON-related structure to return
            // and the suitable HTTP response code and type
            Map<String, Locale> finalJson = new HashMap<>();
            finalJson.put("office", office);
            response.status(200);
            response.type("application/json");

            return finalJson;
        }, gson::toJson);

        // get the living room
        get(baseURL + "/livingroom/:tipo", "application/json", (request, response) -> {
            // get the id from the URL
            Locale livingroom = localDao.getLivingRoom(String.valueOf(request.params(":tipo")));

            // no task? 404!
            if(livingroom==null)
                halt(404);

            // prepare the JSON-related structure to return
            // and the suitable HTTP response code and type
            Map<String, Locale> finalJson = new HashMap<>();
            finalJson.put("livingroom", livingroom);
            response.status(200);
            response.type("application/json");

            return finalJson;
        }, gson::toJson);

        // add a new task
        post(baseURL + "/offices", "application/json", (request, response) -> {
            {
                // get the body of the HTTP request
                Map addRequest = gson.fromJson(request.body(), Map.class);
                Locale ufficio = null;
                // check whether everything is in place
                if (addRequest != null && addRequest.containsKey("descrizione")&& addRequest.containsKey("num_posti")) {
                    String descrizione = String.valueOf(addRequest.get("descrizione"));
                    String tipo = "ufficio";
                    int num_posti = Integer.parseInt(String.valueOf(addRequest.get("num_posti")));

                    // add the task into the DB
                    ufficio = new Locale(descrizione, tipo, num_posti);
                    localDao.addOffice(ufficio);

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
                    localDao.deleteOffice(Integer.parseInt(String.valueOf(request.params(":id"))));
                    response.status(201);
                }
                else {
                    halt(403);
                }
                return "";
            }});
    }

}
