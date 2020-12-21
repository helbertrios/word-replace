package br.br.helbert.word.replace.api;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.util.Locale;
import java.util.Map;

class WordReplaceCompilerIf extends WordReplacePoi {

    private final XWPFDocument wdoc;
    private final Map<String, Object> values;
    final Locale locale;

    public WordReplaceCompilerIf(XWPFDocument wdoc, Map<String, Object> values, Locale locale) {
        super(wdoc);
        this.wdoc = wdoc;
        this.values = values;
        this.locale = locale;
    }

    void compile() {

    }
}
