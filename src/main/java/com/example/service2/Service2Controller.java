package com.example.service2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.example.service2.generalticket.GeneralTicket;

@CrossOrigin
@RestController
public class Service2Controller {

    volatile int restCount;
    @Autowired
    TicketService ticketService;

    @Autowired
    GeneralTicketService generalTicketService;

    @GetMapping("")
    public String getDefaultString() {
        return "default";
    }

    @GetMapping("/tickets")
    public List<Ticket> getAllTickets() {
        return ticketService.getAllTickets();
    }

    @GetMapping("/tickets/delete/{title}")
    public void deleteTicketbyNumber(@PathVariable("title") String title) {
        ticketService.removeTicketByNumber(title);
    }

    @GetMapping("/tickets/create/{number}")
    public Ticket createTicketById(@PathVariable("number") int number) {
        Ticket ticket = new Ticket(number, "ticket" + number);
        return ticket;
    }

    @GetMapping("/tickets/clear")
    public void cleartickets() {
        ticketService.deleteAll();
    }

    @GetMapping("/results")
    public List<GeneralTicket> getGeneralTickets() {
        List<GeneralTicket> generalTickets = generalTicketService.getAllTickets();
        return generalTickets;
    }

    @GetMapping("/delete")
    public String deleteAllTickets() {
        generalTicketService.deleteAll();
        return "deleted";
    }

    @PostMapping("/generalticket/create")
    public ResponseEntity<String> createGeneralTicket(@RequestBody Ticket ticket) {
        restCount++;
        generalTicketService.applyTicket(ticket, "rest", restCount);
        System.out.println("received ticket by http =>" + ticket + "rest count = " + restCount);
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }
}
