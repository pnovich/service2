package com.example.service2;

import java.util.List;

public interface TicketService {
    public List<Ticket> getAllTickets();

    public Ticket addTicket(Ticket ticket);

    public void removeTicketByNumber(String title);

    public void deleteAll();
}
