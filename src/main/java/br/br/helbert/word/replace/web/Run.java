package br.br.helbert.word.replace.web;

import br.br.helbert.word.replace.api.WordReplace;
import br.br.helbert.word.replace.db.DB;
import br.br.helbert.word.replace.dto.Model;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

@WebServlet("/run")
public class Run extends HttpServlet {

    private static final Locale localeBR = new Locale("pt", "BR");
    private static final DB db = DB.getInstance();

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        final PrintWriter writer = response.getWriter();
        final ObjectMapper mapper = new ObjectMapper();

        final Model model = db.getModel();
        final String json = mapper.writeValueAsString(model);
        writer.println("JSON: " + json);
        writer.println("");

        final File template = new File("D:\\dev\\Projetos\\wordreplace\\fontes\\fonte1\\template\\template.docx");

        try {
            WordReplace wr = new WordReplace(localeBR, template, json);
            wr.formatterTemplate();
            wr.compileTemplate();
            wr.replaceTemplate();
            writer.println("successful.");
        } catch (Exception e) {
            writer.println("Erro: "+e.getMessage());
            e.printStackTrace();
        }
    }


}
