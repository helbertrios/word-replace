package br.br.helbert.word.replace.web;

import br.br.helbert.word.replace.api.WordReplaceJson;
import br.br.helbert.word.replace.db.DB;
import br.br.helbert.word.replace.dto.Model;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet("/run")
public class Run extends HttpServlet {

    public static final DB db = DB.getInstance();

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        PrintWriter writer = response.getWriter();
        ObjectMapper mapper = new ObjectMapper();

        Model model = db.getModel();
        String json = mapper.writeValueAsString(model);
        writer.println("JSON: " + json);
        writer.println("");

        WordReplaceJson wrj = new WordReplaceJson(json);
        Map<String, Object> values = wrj.getValues();
        wrj.write(writer);
    }

}
