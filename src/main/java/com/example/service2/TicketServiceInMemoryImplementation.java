package com.example.service2;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class TicketServiceInMemoryImplementation implements TicketService {

    List<Ticket> tickets = new ArrayList<>();

    @PostConstruct
    public void initState() {
        tickets.addAll(Arrays.asList(new Ticket(101, "ticket101"),
                new Ticket(202, "ticket202")));
    }

    public List<Ticket> getAllTickets() {
        return tickets;
    }

    public Ticket addTicket(Ticket ticket) {
        tickets.add(ticket);
        return ticket;
    }

    public void removeTicketByNumber(String title) {
        for (Ticket ticket : tickets) {
            if (ticket.getTitle().equals(title)) {
                tickets.remove(ticket);
            }
        }

    }

    public void deleteAll() {
        tickets.clear();
    }
}
