package br.br.helbert.word.replace.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Actor implements Serializable {

    private String name;
    private String firstName;
    private List<String> novels;
    private List<Fone> fones;


    public Actor() {
        this.novels = new ArrayList<>();
        this.fones = new ArrayList<>();
    }


    public Actor(String name, String firstName) {
        this();
        this.name = name;
        this.firstName = firstName;

    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getFirstName() {
        return firstName;
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public List<String> getNovels() {
        return novels;
    }

    public void setNovels(List<String> novels) {
        this.novels = novels;
    }

    public List<Fone> getFones() {
        return fones;
    }

    public void setFones(List<Fone> fones) {
        this.fones = fones;
    }
}
