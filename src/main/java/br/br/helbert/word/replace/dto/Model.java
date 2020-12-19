package br.br.helbert.word.replace.dto;

import java.util.Date;
import java.util.List;

public class Model {

    private Integer id;
    private String name;
    private Date date;
    private Double price;
    private List<Actor> actors;

    public Model(Integer id, String name, Date date, Double price, List<Actor> actors) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.price = price;
        this.actors = actors;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public List<Actor> getActors() {
        return actors;
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }
}
