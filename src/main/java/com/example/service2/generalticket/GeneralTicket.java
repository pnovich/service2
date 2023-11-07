package com.example.service2.generalticket;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;

@Entity
public class GeneralTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private int totalNumber;
    private String name;

    public GeneralTicket(int id, int totalNumber, String name) {
        this.id = id;
        this.totalNumber = totalNumber;
        this.name = name;
    }

    public GeneralTicket() {
    }

    public GeneralTicket(int totalNumber, String name) {
        this.totalNumber = totalNumber;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(int totalNumber) {
        this.totalNumber = totalNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "GeneralTicket{" +
                "id=" + id +
                ", totalNumber=" + totalNumber +
                ", name='" + name + '\'' +
                '}';
    }
}
