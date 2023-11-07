package com.example.service2;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class TicketsKafkaListener {

    @Autowired
    ModelMapper modelMapper;
    @Autowired
    TicketServiceInMemoryImplementation ticketService;

    @Autowired
    GeneralTicketService generalTicketService;

    volatile int kafkaCount = 0;

    @KafkaListener(topics = "all_tickets2",
            groupId = "allTicketsGroup_2",
            properties = {"spring.json.value.default.type=com.example.service2.TicketDTO"},
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listenTicketsGroup(
            @Payload Ticket ticket
    ) {
        kafkaCount++;
        System.out.println("received ticket from kafka => " + ticket + "kafka count = " + kafkaCount);
        ticketService.addTicket(ticket);
        generalTicketService.applyTicket(ticket, "kafka", kafkaCount);
    }
}
