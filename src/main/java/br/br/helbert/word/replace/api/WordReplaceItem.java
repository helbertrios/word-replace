package br.br.helbert.word.replace.api;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

class WordReplaceItem {


    private final XWPFDocument wdoc;
    private final XWPFParagraph p;
    private final XWPFRun r;
    private String textReplaced;

    WordReplaceItem(XWPFDocument wdoc, XWPFParagraph p, XWPFRun r) {
        this.wdoc = wdoc;
        this.p = p;
        this.r = r;
    }

    void replace(String textReplaced) {
        this.textReplaced = textReplaced;
        this.r.setText(textReplaced, 0);
    }

}
