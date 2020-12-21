package br.br.helbert.word.replace.api;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

class WordReplaceValue {



    private final String script;
    private final Locale locale;

    private final String scriptFormat;
    private final String format;
    private final String keyValue;
    private final Object value;
    private final WordReplaceValueMask wrvm;
    private final String valueFormated;
    private final List<WordReplaceItem> wrs;


    WordReplaceValue(Map<String, Object> values, String script, Locale locale) {
        this.script = script;
        this.locale = locale;

        this.scriptFormat = WRCP.SCRIPT_FORMAT.getScriptFormat(script);
        this.format = WRCP.SCRIPT_FORMAT.getContent(this.scriptFormat);
        this.keyValue = WRCP.SCRIPT.getScriptKey(script, this.scriptFormat);
        this.value = values.get(this.keyValue);

        this.wrvm = new WordReplaceValueMask(this.locale, this.value, this.format);
        this.valueFormated = wrvm.getValueFormated();
        this.wrs = new ArrayList<>();
    }

    void replace() {
        for (WordReplaceItem wri : this.wrs) {
            wri.replace(this.valueFormated);
        }
    }

    void addReplaceItem(XWPFDocument wdoc, XWPFParagraph p, XWPFRun r) {
        this.wrs.add(new WordReplaceItem(wdoc, p, r));
    }

    String getKeyValue() {
        return keyValue;
    }

    public String getValueFormated() {
        return valueFormated;
    }
}
