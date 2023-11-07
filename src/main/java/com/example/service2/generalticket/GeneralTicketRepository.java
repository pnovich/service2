package com.example.service2.generalticket;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface GeneralTicketRepository extends JpaRepository<GeneralTicket, Integer> {
    public GeneralTicket findByName(String name);
}
