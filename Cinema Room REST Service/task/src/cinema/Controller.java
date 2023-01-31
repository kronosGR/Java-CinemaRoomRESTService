package cinema;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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


}
