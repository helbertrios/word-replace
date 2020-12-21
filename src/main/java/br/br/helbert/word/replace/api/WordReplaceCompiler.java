package br.br.helbert.word.replace.api;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.util.Locale;
import java.util.Map;

class WordReplaceCompiler {

    final XWPFDocument wdoc;
    final Map<String, Object> values;
    final Locale locale;
    final WordReplaceCompilerLoop wrcl;
    final WordReplaceCompilerIf wrci;

    WordReplaceCompiler(XWPFDocument wdoc, Map<String, Object> values, Locale locale) {
        this.wdoc = wdoc;
        this.values = values;
        this.locale = locale;
        this.wrcl = new WordReplaceCompilerLoop(wdoc, values);
        this.wrci = new WordReplaceCompilerIf(wdoc, values, locale);
    }

    void compile() {
       this.wrcl.compile();
       this.wrci.compile();
    }
}
