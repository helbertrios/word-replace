package br.br.helbert.word.replace.api;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordReplaceFormatter extends WordReplacePoi {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(WordReplaceFormatter.class);

    private final XWPFDocument wdoc;
    private final String text;
    private final String regua;


    private final Map<Integer, Character> characterReplace;
    private final Map<Integer, Character> characterBegin;
    private final Map<Integer, Character> characterEnd;


    public WordReplaceFormatter(XWPFDocument wdoc) {
        super(wdoc);

        final StringBuilder sb = new StringBuilder();
        for (XWPFParagraph p : wdoc.getParagraphs()) {
            for (XWPFRun r : p.getRuns()) {
                String text = r.getText(0);
                if (text != null) {
                    sb.append(text);
                }
            }
        }

        this.text = sb.toString();
        StringBuilder sbRegua = new StringBuilder();
        for (int i = 1; i <= text.length(); i++) {
            String nr = i + "";
            nr = nr.substring((nr.length() - 1));
            sbRegua.append(nr);
        }


        this.regua = sbRegua.toString();
        this.wdoc = wdoc;
        this.characterReplace = new HashMap<>();
        this.characterBegin = new HashMap<>();
        this.characterEnd = new HashMap<>();
        this.findScripts();
    }

    public void log() {
        for (XWPFParagraph p : wdoc.getParagraphs()) {
            for (XWPFRun r : p.getRuns()) {
                String text = r.getText(0);
                if (text != null && (!"".equals(text))) {
                    logger.info("-->" + text + "<--");
                    int i = 0;
                }
            }
        }
    }

    public void formatter() {

        logger.info("wdoc text: " + this.text);
        logger.info("wdoc regu: " + this.regua);


        boolean inScript = false;
        String script = "";
        int indexText = 1;
        for (XWPFParagraph p : this.wdoc.getParagraphs()) {
            int size = p.getRuns().size();
            int indexRun = size - 1;
            for (int z = 0; z < size; z++) {
                final XWPFRun r = p.getRuns().get(z);
                String textRun = r.getText(0);
                if (textRun == null) {
                    this.cloneRun(p, r);
                } else {
                    String textThisRun = "";
                    for (int i = 0; i < textRun.length(); i++) {
                        if (indexText >= 243 && indexText <= 243) {
                            logger.info("### ATENCAO ###");
                        }

                        final char c = textRun.charAt(i);

                        boolean inScriptPrevious = inScript;
                        inScript = this.isScript(indexText);

                        if (inScript) {
                            script += this.characterReplace.get(indexText);
                        } else {
                            textThisRun += c;
                        }

                        boolean finishScript = (inScriptPrevious && (!inScript)) || this.isScriptBetweenTogetter(indexText);
                        boolean finishText = (!inScriptPrevious) && inScript;

                        if (finishScript) {
                            this.cloneRun(p, r, script);
                            script = "";
                        } else if (finishText) {
                            this.cloneRun(p, r, textThisRun);
                            textThisRun = "";
                        }

                        indexText++;
                    } //  for (int i = 0; i < textRun.length(); i++) {

                    boolean inScriptNext = this.isScript(indexText);
                    boolean finishScript = inScript && (!inScriptNext);
                    boolean finishText = (!inScript);

                    if (finishScript) {
                        this.cloneRun(p, r, script);
                        script = "";
                    } else if (finishText) {
                        this.cloneRun(p, r, textThisRun);
                        textThisRun = "";
                    }
                } //  if (textRun == null)  {} else {}
            }   // for (int z = 0; z < size; z++) {
            for (int i = indexRun; i >= 0; i--) {
                p.removeRun(i);
            }
        } // for (XWPFParagraph p : this.wdoc.getParagraphs()) {

        this.removeSpaceScript();

    }

    private void removeSpaceScript() {

        for (int i = 0; i < this.wdoc.getParagraphs().size(); i++) {
            final XWPFParagraph p = wdoc.getParagraphs().get(i);
            for (int j = 0; j < p.getRuns().size(); j++) {
                final XWPFRun r = p.getRuns().get(j);
                final String text = r.getText(0);
                if (text == null || WRCP.WORD_STRING_EMPTY.equals(text)) {
                    p.removeRun(j);
                } else if (WRCP.SCRIPT.isScript(text)) {
                    final String script = WRCP.SCRIPT.getScriptFormatter(text);
                    r.setText(script);
                }
            }
        }
    }

    private boolean isScriptBetweenTogetter(int index) {
        return this.isScriptEnd(index) && this.isScriptBegin((index + 1));
    }

    private boolean isScript(int index) {
        final Character sc = this.characterReplace.get(index);
        return (sc != null);
    }

    private boolean isScriptBegin(int index) {
        final Character sc = this.characterBegin.get(index);
        return (sc != null);
    }

    private boolean isScriptEnd(int index) {
        final Character sc = this.characterEnd.get(index);
        return (sc != null);
    }

    private void findScripts() {
        final Map<String, List<WordReplaceIndex>> map = new HashMap<>();
        WordReplaceIndex wri = null;
        int indexFrom = 0;
        do {
            wri = WRCP.SCRIPT.getIndex(this.text, indexFrom);  //WordReplaceType.SCRIPT.get().getIndex(this.text, indexFrom);;
            this.addWordReplaceIndex(wri);

            indexFrom = wri == null ? indexFrom : wri.getIndexEnd();
        } while (wri != null);
    }

    private void addcharacterReplace(WordReplaceIndex wri, String key) {
        int wriIndex = wri.getIndexBegin();
        for (int i = 0; i < key.length(); i++) {
            this.characterReplace.put(wriIndex++, key.charAt(i));
        }
        this.characterBegin.put(wri.getIndexBegin(), key.charAt(0));
        this.characterEnd.put(wri.getIndexEnd(), key.charAt(key.length() - 1));
    }

    private void addWordReplaceIndex(final WordReplaceIndex wri) {
        if (wri != null) {
            final String key = WRCP.SCRIPT.getScript(this.text, wri); //WordReplaceType.SCRIPT.get().getAllContent(this.text, wri);
            logger.info("wri: #" + key + "#: { begin = " + wri.getIndexBegin() + ", end = " + wri.getIndexEnd() + " }");
            this.addcharacterReplace(wri, key);
        }
    }

}
