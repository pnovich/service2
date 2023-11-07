package com.example.service2;

import com.example.service2.generalticket.GeneralTicket;
import com.example.service2.generalticket.GeneralTicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GeneralTicketService {

    @Autowired
    GeneralTicketRepository generalTicketRepository;

    public void applyTicket(Ticket ticket, String source, int count) {
        System.out.println("applying ticket =" + ticket + "source = " + source + "count = " + count);
        String name = ticket.getTitle();
        int number = ticket.getNumber();
        GeneralTicket generalTicket = getGeneralTicketByName(name);
        if (generalTicket == null) {
            generalTicket = createGeneralTicket(new GeneralTicket(number, name));
        } else {
            updateTicketsTotalNumberInGeneralTicket(generalTicket, number, source, count);
        }
    }

    @Retryable(maxAttempts = 15)
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void updateTicketsTotalNumberInGeneralTicket(GeneralTicket generalTicket,
                                                        int number,
                                                        String source,
                                                        int count) {
        int totalNumber = generalTicket.getTotalNumber();
        totalNumber = totalNumber + number;
        generalTicket.setTotalNumber(totalNumber);
        changeGeneralTicket(generalTicket);

    }

    public GeneralTicket createGeneralTicket(GeneralTicket generalTicket) {
        generalTicketRepository.save(generalTicket);
        return generalTicket;
    }

    public GeneralTicket changeGeneralTicket(GeneralTicket generalTicket) {
        generalTicketRepository.save(generalTicket);
        return generalTicket;
    }

    public GeneralTicket getGeneralTicketByName(String name) {
        GeneralTicket generalTicket = generalTicketRepository.findByName(name);
        return generalTicket;
    }

    public GeneralTicket getGeneralTicketById(int id) {
        return generalTicketRepository.findById(id).orElse(new GeneralTicket(1, 1, "1"));
    }

    public void deleteGeneralTicket(int id) {
        generalTicketRepository.deleteById(id);
    }

    public List<GeneralTicket> getAllTickets() {
        return generalTicketRepository.findAll();
    }

    public void deleteAll() {
        generalTicketRepository.deleteAll();
    }
}
