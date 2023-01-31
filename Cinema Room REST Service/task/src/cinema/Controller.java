package cinema;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class Controller {

    private Cinema cinema;

    public Controller() {
        this.cinema = Cinema.getAllSeats(9, 9);
    }

    @GetMapping("/seats")
    public Cinema getSeats() {
        return cinema;
    }

    @PostMapping("/purchase")
    public ResponseEntity<?> purchase(@RequestBody Seat seat) {
        if (seat.getColumn() > cinema.getTotal_columns()
                || seat.getRow() > cinema.getTotal_rows()
                || seat.getRow() < 1
                || seat.getColumn() < 1) {
            return new ResponseEntity<>(Map.of("error", "The number of a row or a column is out of bounds!"), HttpStatus.BAD_REQUEST);
        }
        for (int seatI = 0; seatI < cinema.getAvailable_seats().size(); seatI++) {
            Seat tmp = cinema.getAvailable_seats().get(seatI);
            if (tmp.equals(seat)) {
                Order order = new Order(UUID.randomUUID(), tmp);
                cinema.getAvailable_seats().remove(seatI);
                cinema.getOrders().add(order);
                return new ResponseEntity<>(order, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(Map.of("error", "The ticket has been already purchased!"), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/return")
    public ResponseEntity<?> getOrder(@RequestBody Token token){
        List<Order> orders = cinema.getOrders();
        for (Order order: orders){
            if (order.getToken().equals(token.getToken())){
                orders.remove(order);
                cinema.getAvailable_seats().add(order.getTicket());
                return  new ResponseEntity<>(Map.of("returned_ticket", order.getTicket()), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(Map.of("error", "Wrong token!"), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/stats")
    public ResponseEntity<?> getStats(@RequestParam(required = false) String password){
        if (password != null && password.equals("super_secret")) {
            Map<String, Integer> statistic = new HashMap<>();
            int currentIncome = 0;
            for (Order order : cinema.getOrders()) {
                currentIncome += order.getTicket().getPrice();
            }
            int numberOfAvailableSeats = cinema.getAvailable_seats().size();
            int numberOfPurchasedTickets = cinema.getOrders().size();
            statistic.put("current_income", currentIncome);
            statistic.put("number_of_available_seats", numberOfAvailableSeats);
            statistic.put("number_of_purchased_tickets", numberOfPurchasedTickets);
            return new ResponseEntity<>(statistic, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(Map.of("error", "The password is wrong!"), HttpStatus.valueOf(401));
        }
    }


}
