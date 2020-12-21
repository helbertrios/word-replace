package br.br.helbert.word.replace.api;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class WordReplaceFormatter extends WordReplacePoi {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(WordReplaceFormatter.class);

    private final XWPFDocument wdoc;
    private final String text;
    private final String regua;


    private final Map<Integer, Character> characterReplace;
    private final Map<Integer, Character> characterBegin;
    private final Map<Integer, Character> characterEnd;


    WordReplaceFormatter(XWPFDocument wdoc) {
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

    void log() {

        for (XWPFParagraph p : wdoc.getParagraphs()) {
            for (XWPFRun r : p.getRuns()) {
                String text = r.getText(0);
                if (text != null && (!"".equals(text))) {
                    logger.info("-->" + text + "<--");
                    int i = 0;
                }
            }
        }

        try (FileOutputStream oS = new FileOutputStream(new File("D:\\dev\\tmp\\arquivos\\word-replace\\log-formatter.txt"))) {
            final StringBuilder log = new StringBuilder();
            log.append("------------------------------------------------------------------------------------\n");
            for (int i = 0; i < this.wdoc.getParagraphs().size(); i++) {
                final XWPFParagraph p = wdoc.getParagraphs().get(i);
                logger.info("paragraph " + i + ": " + p.getText());
                log.append("paragraph " + i + ": " + p.getText() + "\n");
            }
            log.append("------------------------------------------------------------------------------------\n");
            oS.write(log.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    void formatter() {

        boolean inScript = false;
        String script = "";
        int indexText = 1;


        int endParagraphs = this.wdoc.getParagraphs().size()-1;
        int posBeforeForClone = endParagraphs;
        for (int i = 0; i <= endParagraphs; i++) {
            final XWPFParagraph sourceParagraph = this.wdoc.getParagraphs().get(i);
            final XWPFParagraph beforeParagraph = this.wdoc.getParagraphs().get(posBeforeForClone++);
            final XWPFParagraph cloneParagraph = this.cloneParagraph(sourceParagraph, beforeParagraph);

            String textThisRun = "";
            for (int j = 0; j < sourceParagraph.getRuns().size(); j++) {
                final XWPFRun sourceRun = sourceParagraph.getRuns().get(j);
                final String textRun = sourceRun.getText(0);
                if (textRun == null) {
                    this.cloneRun(cloneParagraph, sourceRun);
                } else {
                    for (int k = 0; k < textRun.length(); k++) {
                        final Character c = textRun.charAt(k);
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
                            this.cloneRun(cloneParagraph, sourceRun, script);
                            script = "";
                        } else if (finishText) {
                            this.cloneRun(cloneParagraph, sourceRun, textThisRun);
                            textThisRun = "";
                        }

                        indexText++;
                    } //  for (int k = 0; k < textRun.length(); k++) {

                    boolean inScriptNext = this.isScript(indexText);
                    boolean finishScript = inScript && (!inScriptNext);
                    boolean finishText = (!inScript);

                    if (finishScript) {
                        this.cloneRun(cloneParagraph, sourceRun, script);
                        script = "";
                    } else if (finishText) {
                        this.cloneRun(cloneParagraph, sourceRun, textThisRun);
                        textThisRun = "";
                    }

                } //   if (textRun == null) {
            } //  for (int j = 0; j < sourceParagraph.getRuns().size(); j++) {
        } // for (int i = 0; i < this.wdoc.getParagraphs().size(); i++) {

        for (int i = endParagraphs; i >= 0; i--) {
            final XWPFParagraph p = this.wdoc.getParagraphs().get(i);
            this.removeParagraph(p, i);
        }

        this.removeSpaceScript();

    }


    private void removeSpaceScript() {

        for (int i = 0; i < this.wdoc.getParagraphs().size(); i++) {
            final XWPFParagraph p = wdoc.getParagraphs().get(i);
            for (int j = 0; j < p.getRuns().size(); j++) {
                final XWPFRun r = p.getRuns().get(j);
                final String text = r.getText(0);
                 if (WRCP.SCRIPT.isScript(text)) {
                    final String script = WRCP.SCRIPT.getScriptFormatter(text);
                    r.setText(script, 0);
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
