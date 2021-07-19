package RESTService;

import com.google.gson.Gson;
import tasks.Prenotazione;
import taskModelsJSGrid.Slot;
import tasksDao.GestionePrenotazione;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
               String userIdraw = JWTfun.getUserId();
               String userId = userIdraw.substring(1,userIdraw.length()-1);
                System.out.println("id: " + userId); //<- test di prova per vedere cosa stampa
                if(usertype.contains("admin")) {
                    // get all tasks from the DB
                    List<Prenotazione> allReservations = ReservationDao.getAllReservations();
                    // prepare the JSON-related structure to return
                    Map<String, List<Prenotazione>> finalJson = new HashMap<>();
                    finalJson.put("reservations", allReservations);

                return allReservations; }else if(usertype.contains("user"))
                {
                    List<Prenotazione> allReservations = ReservationDao.getAllReservationsUser(userId);
                    // prepare the JSON-related structure to return
                    Map<String, List<Prenotazione>> finalJson = new HashMap<>();
                    finalJson.put("Reservations", allReservations);
                    return allReservations;
                } else return halt(401);
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
                    int ufficio_id = Integer.parseInt(String.valueOf(addRequest.get("ufficioId")));
                    String utente_id = String.valueOf(addRequest.get("utenteId"));

                    // add the task into the DB
                    prenotazione = new Prenotazione(descrizione, data, oraInizio, oraFine, clienti, ufficio_id,utente_id);
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
                {
                    if(request.params(":id")!=null) {
                    // add the task into the DB
                    ReservationDao.deleteReservation(Integer.parseInt(String.valueOf(request.params(":id"))));
                    response.status(201);

                    }
                    else {
                        halt(403);
                    }
                    return "";
                }
            });

            // get all slots available
            get(baseURL + "/slots", (request, response) -> {
                // set a proper response code and type
                response.type("application/json");
                response.status(200);

                List<Prenotazione> allReservations = ReservationDao.getAllReservations();

                int startHour = 8;
                int finalHour = 20;
                // Calcolo slot di tempo
                LocalDate startDate = LocalDate.now();
                LocalDate finalDate = startDate.plusWeeks(2);
                List<Integer> startTimes = getStartTimes(allReservations);
                List<Integer> finalTimes = getFinalTimes(allReservations);

                List<Slot> slots = new LinkedList<>();
                LocalDate date = startDate;
                while(!date.equals(finalDate)){
                    int nowHour = LocalDateTime.now().getHour() + 1;

                    if (nowHour < startHour) nowHour = 8;
                    else if (nowHour > finalHour) nowHour = 8;

                    if(date == startDate) date = date.plusDays(1);

                    for (int hour=nowHour; hour<finalHour; hour++){
                        if(!contains(hour, startTimes, finalTimes)) // Lo slot è libero
                            slots.add(new Slot(hour + "-" + Integer.toString(hour+1), date.toString(), true));
                    }
                    date = date.plusDays(1);
                }

                return slots;
            }, gson::toJson);

            post(baseURL + "/slots", "application/json", (request, response) -> {
                // get the body of the HTTP request
                Map addRequest = gson.fromJson(request.body(), Map.class);
                Slot slot = null;
                // check whether everything is in place
                if(addRequest!=null && addRequest.containsKey("orario") && addRequest.containsKey("data") && addRequest.containsKey("libero")) {
                    String orario = String.valueOf(addRequest.get("orario"));
                    String data = String.valueOf(addRequest.get("data"));
                    boolean libero = Boolean.valueOf(String.valueOf(addRequest.get("libero")));

                    // add the task into the DB
                    slot = new Slot(orario, data, libero);


                    // if success, prepare a suitable HTTP response code
                    response.status(201);
                }
                else {
                    halt(403);
                }

                return slot;
            }, gson::toJson);
        }

        public static List<Integer> getStartTimes(List<Prenotazione> allReservations){
            List<Integer> startTimes = new LinkedList<>();
            for(Prenotazione prenotazione : allReservations){
                startTimes.add(prenotazione.getOraInizio());
            }
            return startTimes;
        }

        public static List<Integer> getFinalTimes(List<Prenotazione> allReservations){
            List<Integer> finalTimes = new LinkedList<>();
            for(Prenotazione prenotazione : allReservations){
                finalTimes.add(prenotazione.getOraFine());
            }
            return finalTimes;
        }

        public static boolean contains(int oraInizio, List<Integer> startTimes, List<Integer> finalTimes){
            for(int i=0; i<startTimes.size(); i++){
                if(startTimes.get(i) <= oraInizio && finalTimes.get(i) > oraInizio)
                    return true;
            }
            return false;
        }
    }





