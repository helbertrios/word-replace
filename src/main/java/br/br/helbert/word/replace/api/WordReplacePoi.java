package br.br.helbert.word.replace.api;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.xmlbeans.XmlCursor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;

class WordReplacePoi {

    private final XWPFDocument wdoc;

    WordReplacePoi(final XWPFDocument wdoc) {
        this.wdoc = wdoc;
    }


    XWPFParagraph cloneParagraph(final XWPFParagraph source, final XWPFParagraph before) {
        final XmlCursor cur = before.getCTP().newCursor();
        cur.toNextSibling();
        final XWPFParagraph clone = wdoc.insertNewParagraph(cur);
        CTPPr pPr = clone.getCTP().isSetPPr() ? clone.getCTP().getPPr() : clone.getCTP().addNewPPr();
        pPr.set(source.getCTP().getPPr());
        return clone;
    }


    void cloneRuns(final XWPFParagraph p, final XWPFParagraph np) {
        for (int j = 0; j < p.getRuns().size(); j++) {
            final XWPFRun r = p.getRuns().get(j);
            this.cloneRun(np, r);
        }
    }

    XWPFRun cloneRun(final XWPFParagraph p, final XWPFRun source) {
        XWPFRun clone = p.createRun();
        CTRPr rPr = clone.getCTR().isSetRPr() ? clone.getCTR().getRPr() : clone.getCTR().addNewRPr();
        rPr.set(source.getCTR().getRPr());
        clone.setText(source.getText(0));
        return clone;
    }

    XWPFRun cloneRun(final XWPFParagraph p, final XWPFRun source, String text) {
        XWPFRun clone = p.createRun();
        CTRPr rPr = clone.getCTR().isSetRPr() ? clone.getCTR().getRPr() : clone.getCTR().addNewRPr();
        rPr.set(source.getCTR().getRPr());
        clone.setText(text);
        return clone;
    }


    boolean containsTextBefore(final XWPFParagraph p, final int posRun) {
        int begin = posRun - 1;
        for (int j = begin; j >= 0; j--) {
            final XWPFRun r = p.getRuns().get(j);
            final String text = r.getText(0);
            if (text != null && !("".equals(text))) {
                return true;
            }
        }
        return false;
    }

    boolean containsTextAfter(final XWPFParagraph p, final int posRun) {
        int begin = posRun + 1;
        for (int j = begin; j < p.getRuns().size(); j++) {
            final XWPFRun r = p.getRuns().get(j);
            final String text = r.getText(0);
            if (text != null && !("".equals(text))) {
                return true;
            }
        }
        return false;
    }

    XWPFParagraph cloneParagraph(XWPFParagraph source) {
        XWPFParagraph clone = this.wdoc.createParagraph();
        CTPPr pPr = clone.getCTP().isSetPPr() ? clone.getCTP().getPPr() : clone.getCTP().addNewPPr();
        pPr.set(source.getCTP().getPPr());
        return clone;
    }

    void removeRuns(final XWPFParagraph p, final int beginRun, final int endRun) {
        for (int j = endRun; j >= beginRun; j--) {
            p.removeRun(j);
        }
    }

    void removeAllRuns(final XWPFParagraph p) {
        final int beginRun = 0;
        final int endRun = p.getRuns().size()-1;
        for (int j = endRun; j >= beginRun; j--) {
            p.removeRun(j);
        }
    }

    void removeParagraph(final XWPFParagraph p, final int pos) {
        this.removeAllRuns(p);
        this.wdoc.removeBodyElement(pos);
    }

}
