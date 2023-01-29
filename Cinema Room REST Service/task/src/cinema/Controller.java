package cinema;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class Controller {

    @GetMapping("/seats")
    public Cinema getSeats() {
        return getAllSeats();
    }

    public Cinema getAllSeats() {
        int totalRows = 9;
        int totalCols = 9;
        List<Seat> cinema = new ArrayList<>();
        for (int r = 1; r <= totalRows; r++) {
            for (int c = 1; c <= totalCols; c++) {
                cinema.add(new Seat(r, c));
            }
        }
        return new Cinema(totalRows, totalCols, cinema);
    }
}
