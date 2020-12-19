package br.br.helbert.word.replace.db;

import br.br.helbert.word.replace.dto.Actor;
import br.br.helbert.word.replace.dto.Fone;
import br.br.helbert.word.replace.dto.Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DB {

    private static final Object mutex = new Object();
    private static volatile DB instance;
    private Model model;
    private List<Actor> actors;


    private DB() {
        this.loadActors();
        this.loadModel();
    }

    public static DB getInstance() {
        DB result = instance;
        if (result == null) {
            synchronized (mutex) {
                result = instance;
                if (result == null)
                    instance = result = new DB();
            }
        }
        return result;
    }

    public void loadModel() {
        this.model = new Model(1, "My first model", new Date(), 5.13, this.actors);
    }

    public void loadActors() {

        this.actors = new ArrayList<>();

        Actor actor = new Actor("Fagundes", "Antonio");
        actor.getNovels().add("Rei do gado.");
        actor.getNovels().add("Renascer.");
        actor.getNovels().add("A viagem.");
        actor.getFones().add(new Fone("21", "11", "99991111"));
        actor.getFones().add(new Fone("21", "11", "99991112"));
        actor.getFones().add(new Fone("21", "11", "99991112"));
        //actors.add(actor);

        actor = new Actor("Tarcísio", "Meira");
        actor.getNovels().add("Imãos Coragem.");
        actor.getNovels().add("Pátria minha.");
        actor.getNovels().add("Saramandaia.");
        actor.getFones().add(new Fone("22", "12", "99991114"));
        actor.getFones().add(new Fone("22", "12", "99991115"));
        actor.getFones().add(new Fone("22", "12", "99991116"));
        //actors.add(actor);

        actor = new Actor("Glória", "Menezes");
        actor.getNovels().add("Sangue e areia.");
        actor.getNovels().add("Brega e chique.");
        actor.getNovels().add("Torre de babel.");
        actor.getFones().add(new Fone("23", "13", "99991114"));
        actor.getFones().add(new Fone("23", "13", "99991115"));
        actor.getFones().add(new Fone("23", "13", "99991116"));
        //actors.add(actor);

        actor = new Actor("Regina", "duarte");
        actor.getNovels().add("Malu mulher.");
        actor.getNovels().add("Roque santeiro.");
        actor.getNovels().add("Rainha da sucata.");
        actor.getFones().add(new Fone("24", "14", "99991117"));
        actor.getFones().add(new Fone("24", "14", "99991118"));
        actor.getFones().add(new Fone("24", "14", "99991119"));
        //actors.add(actor);

    }


    public List<Actor> getActors() {
        return actors;
    }

    public Model getModel() {
        return model;
    }
}
