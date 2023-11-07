package com.example.service2;

public class TicketDTO {
    private int number;
    private String title;

    public TicketDTO(int number, String title) {
        this.number = number;
        this.title = title;
    }

    public TicketDTO() {
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "TicketDTO{" +
                "number=" + number +
                ", title='" + title + '\'' +
                '}';
    }
}
