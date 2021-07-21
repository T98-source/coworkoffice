package RESTService;

import com.google.gson.Gson;
import tasks.Prenotazione;
import taskModelsJSGrid.Slot;
import tasksDao.GestionePrenotazione;

import java.sql.Date;
import java.util.*;

import static spark.Spark.*;
import static spark.Spark.halt;

import jwtToken.jwtlib.*;

public class RESTPrenotazione {


        public static void REST(Gson gson, String baseURL){
            GestionePrenotazione ReservationDao = new GestionePrenotazione();

            // get all the tasks
            get(baseURL + "/reservations", (request, response) -> {
                // set a proper response code and type
                response.type("application/json");
                response.status(200);

                String usertype = JWTfun.getUserType();
                String userIdRaw = JWTfun.getUserId();
                String userId = userIdRaw.substring(1,userIdRaw.length()-1);

                List<Prenotazione> allReservations = new LinkedList<>();

                if(usertype.contains("admin"))
                    allReservations = ReservationDao.getAllReservations();
                else if(usertype.contains("user"))
                    allReservations = ReservationDao.getAllReservationsUser(userId);
                else
                    return halt(401);

                Map<String, List<Prenotazione>> finalJson = new HashMap<>();
                finalJson.put("Reservations", allReservations);
                return allReservations;

            }, gson::toJson);


            // get a single task
            get(baseURL + "/reservations/:id", "application/json", (request, response) -> {
                // get the id from the URL
                Prenotazione Reservation = ReservationDao.getReservation(Integer.valueOf(request.params(":id")));

                // no task? 404!
                if(Reservation==null)
                    halt(404);

                // prepare the JSON-related structure to return
                // and the suitable HTTP response code and type
                Map<String, Prenotazione> finalJson = new HashMap<>();
                finalJson.put("reservation", Reservation);
                response.status(200);
                response.type("application/json");

                return finalJson;
            }, gson::toJson);

            // add a new task
            post(baseURL + "/reservations", "application/json", (request, response) -> {
                // get the body of the HTTP request
                Map addRequest = gson.fromJson(request.body(), Map.class);
                Prenotazione prenotazione = null;
                // check whether everything is in place
                if(addRequest!=null && addRequest.containsKey("descrizione") && addRequest.containsKey("data") && addRequest.containsKey("oraInizio") && addRequest.containsKey("oraFine") && addRequest.containsKey("clienti") && addRequest.containsKey("ufficioId") && addRequest.containsKey("utenteId")) {
                    String descrizione = String.valueOf(addRequest.get("descrizione"));
                    Date data = Date.valueOf(String.valueOf(addRequest.get("data")));
                    int oraInizio = Integer.valueOf(String.valueOf(addRequest.get("oraInizio")));
                    int oraFine = Integer.valueOf(String.valueOf(addRequest.get("oraFine")));
                    int clienti = Integer.parseInt(String.valueOf(addRequest.get("clienti")));
                    int ufficioId = Integer.parseInt(String.valueOf(addRequest.get("ufficioId")));
                    String utenteId = String.valueOf(addRequest.get("utenteId"));

                    // add the task into the DB
                    prenotazione = new Prenotazione(data, oraInizio, oraFine, clienti, ufficioId,utenteId);
                    ReservationDao.addReservation(prenotazione);

                    // if success, prepare a suitable HTTP response code
                    response.status(201);
                }
                else {
                    halt(403);
                }

                return prenotazione;
            }, gson::toJson);


            delete(baseURL + "/reservations/:id", "application/json", (request, response) -> {
                if(request.params(":id")!=null) {
                    // add the task into the DB
                    ReservationDao.deleteReservation(Integer.valueOf(request.params(":id")));
                    response.status(201);
                }
                else {
                    halt(403);
                }
                return "";
            });

            // get all slots available
            get(baseURL + "/slots/:officeId", (request, response) -> {
                // set a proper response code and type
                response.type("application/json");
                response.status(200);

                List<Slot> slots = ReservationDao.getSlots(Integer.valueOf(request.params(":officeId")));

                return slots;
            }, gson::toJson);

            post(baseURL + "/slots/:officeId/:clients", "application/json", (request, response) -> {
                // get the body of the HTTP request
                Map addRequest = gson.fromJson(request.body(), Map.class);

                // check whether everything is in place
                if(addRequest!=null && addRequest.containsKey("schedule") && addRequest.containsKey("date")) {
                    String schedule = String.valueOf(addRequest.get("schedule"));
                    Date date = Date.valueOf(addRequest.get("date").toString());

                    int i = schedule.indexOf(':');
                    int startHour = Integer.valueOf(schedule.substring(0, i));
                    int finalHour = Integer.valueOf(schedule.substring(i+1, schedule.length()));
                    String userIdRaw = JWTfun.getUserId();
                    String userId = userIdRaw.substring(1, userIdRaw.length()-1);
                    int clients = Integer.valueOf(request.params(":clients"));
                    int officeId = Integer.valueOf(request.params(":officeId"));

                    Prenotazione reservation = new Prenotazione(date, startHour, finalHour, clients, officeId, userId);
                    ReservationDao.addReservation(reservation);

                    // if success, prepare a suitable HTTP response code
                    response.status(201);
                }
                else {
                    halt(403);
                }

                return "";
            }, gson::toJson);

            delete(baseURL + "/slots/:officeId", "application/json", (request, response) -> {
                // get the body of the HTTP request
                Map deleteRequest = gson.fromJson(request.body(), Map.class);

                if(deleteRequest!=null && deleteRequest.containsKey("schedule") && deleteRequest.containsKey("date")) {
                    String schedule = String.valueOf(deleteRequest.get("schedule"));
                    String date = String.valueOf(deleteRequest.get("date"));

                    int officeId = Integer.valueOf(request.params(":officeId"));
                    int i = schedule.indexOf(':');
                    int startHour = Integer.valueOf(schedule.substring(0, i));
                    String userIdRaw = JWTfun.getUserId();
                    String userId = userIdRaw.substring(1, userIdRaw.length()-1);
                    ReservationDao.deleteReservation(startHour, officeId, userId);
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





