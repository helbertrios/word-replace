package br.br.helbert.word.replace.api;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class WordReplace {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(WordReplace.class);


    private final Locale locale;
    private final File template;
    private final String json;
    private final File directory;


    private Map<String, Object> values;
    private File templateFormatted;
    private File templateCompiled;
    private File templateReplaced;



    public WordReplace(final Locale locale, final  File template, final String json)  {
        this.validFileTemplate(template);
        this.locale = locale;
        this.template = template;
        this.json = json;
        this.directory = template.getParentFile();
        final WordReplaceJson wrj = new WordReplaceJson(json);
        this.values = wrj.getValues();



    }

    public File formatterTemplate() throws IOException {

        try (WordReplaceFile wrf = new WordReplaceFile(this.template)) {
            WordReplaceFormatter wrfr = new WordReplaceFormatter(wrf.getWdoc());
            wrfr.formatter();
            File file = this.getFile("templateFormatted.docx");
            wrf.writeTemplate(file);
            this.templateFormatted = file;
            return file;
        }

    }

    public File compileTemplate() throws IOException {
        File file = null;
        if (this.templateFormatted != null) {
            try (WordReplaceFile wrf = new WordReplaceFile(this.templateFormatted)) {
                WordReplaceCompiler wrc = new WordReplaceCompiler(wrf.getWdoc(), this.values, this.locale);
                wrc.compile();
                file = this.getFile("templateCompiled.docx");
                wrf.writeTemplate(file);
                this.templateCompiled = file;

            }
        }
        return file;
    }

    public File replaceTemplate() throws IOException {
        File file = null;
        if (this.templateCompiled != null) {
            try (WordReplaceFile wrf = new WordReplaceFile(this.templateCompiled)) {
                this.replace(wrf.getWdoc());
                file = this.getFile("document.docx");
                wrf.writeTemplate(file);
                this.templateReplaced = file;

            }
        }
        return file;
    }

    private void replace(final XWPFDocument wdoc) {
        final  Map<String, WordReplaceValue> wrvs = getWordReplaceValue(wdoc);
        for (Map.Entry<String, WordReplaceValue> entry : wrvs.entrySet()) {
            entry.getValue().replace();
        }
    }

    private File getFile(final String name) {
        String absoluteName = this.directory + File.separator + name;
        File file = new File(absoluteName);
        return  file;
    }

    private Map<String, WordReplaceValue> getWordReplaceValue(final XWPFDocument wdoc) {
        Map<String, WordReplaceValue> wrvslocal = new HashMap<>();
        for (int i = 0; i < wdoc.getParagraphs().size(); i++) {
            final XWPFParagraph p = wdoc.getParagraphs().get(i);
            for (XWPFRun r : p.getRuns()) {
                final String text = r.getText(0);
                if (WRCP.SCRIPT.isScript(text)) {
                    final String key = text;
                    WordReplaceValue wrv = wrvslocal.get(key);
                    if (wrv == null) {
                        wrv = new WordReplaceValue(values, key, this.locale);
                        wrvslocal.put(key, wrv);
                    }
                    wrv.addReplaceItem(wdoc, p, r);
                }
            }
        }
        return wrvslocal;
    }

    private void validFileTemplate(final File template) {
        if (template == null) {
            throw new RuntimeException("The template is null.");
        }

        if (!template.exists()) {
            throw new RuntimeException("The template "+template.getAbsolutePath()+" does not exists.");
        }

        if (!template.isFile()) {
            throw new RuntimeException("The template "+template.getAbsolutePath()+" is not file.");
        }
    }

    public Locale getLocale() {
        return locale;
    }

    public File getDirectory() {
        return directory;
    }

    public File getTemplate() {
        return template;
    }

    public String getJson() {
        return json;
    }

    public File getTemplateFormatted() {
        return templateFormatted;
    }

    public File getTemplateCompiled() {
        return templateCompiled;
    }

    public File getTemplateReplaced() {
        return templateReplaced;
    }

}
