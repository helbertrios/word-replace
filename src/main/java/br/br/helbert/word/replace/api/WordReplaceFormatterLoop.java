package br.br.helbert.word.replace.api;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;


class WordReplaceFormatterLoop extends WordReplacePoi {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(WordReplaceFormatterLoop.class);

    private final XWPFDocument wdoc;
    private final Map<String, Object> values;

    private final StringBuilder log;

    WordReplaceFormatterLoop(final XWPFDocument wdoc, final Map<String, Object> values) {
        super(wdoc);
        this.wdoc = wdoc;
        this.values = values;
        this.log = new StringBuilder();
    }

    void formatter() {
        try {
            this.logDoc();
            this.findLoops();
        } finally {
            this.logWrite();
        }
    }

    private void logWrite() {
        try (FileOutputStream oS = new FileOutputStream(new File("D:\\dev\\tmp\\arquivos\\modelo-documento\\log.txt"))) {
            oS.write(this.log.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void findLoops() {
        int lookFor = 0;
        WordReplaceLoopNode wrln = null;

        do {
            wrln = this.findLoopNode(lookFor);
            if (wrln != null) {
                lookFor = wrln.getBeginParagraph();
                if (wrln.isInternalLoop()) {
                    this.insertRuns(wrln);
                } else {
                    final int indexLoopEnd = this.insertParagraphs(wrln);
                    this.removeParagraphs(wrln, indexLoopEnd);
                }
            }
            this.logDoc();
        } while (wrln != null);

        XWPFWordExtractor ext = new XWPFWordExtractor(wdoc);
        logger.info("clone doc: " + ext.getText());
        logger.info("-------------------------------------------------");

    }

    private void insertRuns(final WordReplaceLoopNode wrln) {

        final XWPFParagraph source = this.wdoc.getParagraphs().get(wrln.getBeginParagraph());
        final int posSource = wrln.getBeginParagraph();
        final XWPFParagraph clone = this.cloneParagraph(source, source);


        int beginRun = 0;
        int endRun = wrln.getBeginRun();
        for (int j = 0; j < endRun; j++) {
            final XWPFRun r = source.getRuns().get(j);
            this.cloneRun(clone, r);
        }

        final Integer loopSize = values.get(wrln.getKeySize()) == null ? 0 : (Integer) values.get(wrln.getKeySize());
        beginRun = wrln.getBeginRun() + 1;
        endRun = wrln.getEndRun() - 1;
        for (int loopIndex = 0; loopIndex < loopSize; loopIndex++) {
            for (int j = beginRun; j <= endRun; j++) {
                final XWPFRun r = source.getRuns().get(j);
                final String text = r.getText(0);
                if (text.contains(wrln.getLoopLabel())) {
                    String textReplaced = text.replace(wrln.getLoopLabel(), wrln.getTextForReplace(loopIndex));
                    this.cloneRun(clone, r, textReplaced);
                } else {
                    this.cloneRun(clone, r);
                }
            }
        }

        beginRun = wrln.getEndRun() + 1;
        endRun = source.getRuns().size();
        for (int j = beginRun; j < endRun; j++) {
            if (j > source.getRuns().size()) {
                break;
            }
            final XWPFRun r = source.getRuns().get(j);
            this.cloneRun(clone, r);
        }


        this.removeParagraph(source, posSource);
    }

    private void removeParagraphs(final WordReplaceLoopNode wrln, final int indexLoopEnd) {

        for (int i = wrln.getEndParagraph(); i >= wrln.getBeginParagraph(); i--) {

            final XWPFParagraph p = wdoc.getParagraphs().get(i);
            final boolean isFirst = i == wrln.getBeginParagraph();
            final boolean isLast = i == wrln.getEndParagraph();
            boolean doRemove = false;
            if (isFirst) {
                boolean isNotContainsTextBefore = !this.containsTextBefore(p, wrln.getBeginRun());
                if (isNotContainsTextBefore) {
                    doRemove = true;
                }
            } else if (isLast) {
                boolean isNotContainsTextAfter = !this.containsTextAfter(p, wrln.getEndRun());
                if (isNotContainsTextAfter) {
                    doRemove = true;
                }
            } else {
                doRemove = true;
            }


            int beginRun = isFirst ? wrln.getBeginRun() : 0;
            final int endRun = isLast ? wrln.getEndRun() : p.getRuns().size() - 1;
            this.removeRuns(p, beginRun, endRun);

            if (isLast && (!doRemove)) {
                final XWPFParagraph before = wdoc.getParagraphs().get(indexLoopEnd);
                final XWPFParagraph clone = this.cloneParagraph(p, before);
                this.cloneRuns(p, clone);
                this.wdoc.removeBodyElement(i);
            }

            if (doRemove) {
                this.wdoc.removeBodyElement(i);
            }
        }
    }

    private int insertParagraphs(final WordReplaceLoopNode wrln) {

        final Integer loopSize = values.get(wrln.getKeySize()) == null ? 0 : (Integer) values.get(wrln.getKeySize());

        int posParagraphBefore = wrln.getEndParagraph();
        for (int loopIndex = 0; loopIndex < loopSize; loopIndex++) {
            for (int i = wrln.getBeginParagraph(); i <= wrln.getEndParagraph(); i++) {
                final XWPFParagraph p = wdoc.getParagraphs().get(i);
                int beginRun = 0;
                int endRun = p.getRuns().size() - 1;
                final boolean isFirst = i == wrln.getBeginParagraph();
                final boolean isLast = i == wrln.getEndParagraph();

                if (isFirst) {
                    beginRun = wrln.getBeginRun() + 1;
                    boolean isNotContainsTextAfter = !this.containsTextAfter(p, wrln.getBeginRun());
                    if (isNotContainsTextAfter) {
                        continue;
                    }
                } else if (isLast) {
                    endRun = wrln.getEndRun() - 1;
                    boolean isNotContainsTextAfter = !this.containsTextBefore(p, wrln.getEndRun());
                    if (isNotContainsTextAfter) {
                        continue;
                    }
                }

                posParagraphBefore = this.cloneParagraph(wrln, loopIndex, posParagraphBefore, i, beginRun, endRun);
            }
        }
        return posParagraphBefore;
    }


    private int cloneParagraph(final WordReplaceLoopNode wrln, int loopIndex, int posParagraphBefore, int posParagraphSource, int beginRun, int endRun) {

        final XWPFParagraph source = this.wdoc.getParagraphs().get(posParagraphSource);

        final XWPFParagraph before = wdoc.getParagraphs().get(posParagraphBefore);
        final XWPFParagraph clone = this.cloneParagraph(source, before);

        for (int j = beginRun; j <= endRun; j++) {
            if (j > source.getRuns().size()) {
                break;
            }
            final XWPFRun r = source.getRuns().get(j);
            final String text = r.getText(0);
            if (text.contains(wrln.getLoopLabel())) {
                String textReplaced = text.replace(wrln.getLoopLabel(), wrln.getTextForReplace(loopIndex));
                this.cloneRun(clone, r, textReplaced);
            } else {
                this.cloneRun(clone, r);
            }
        }

        return ++posParagraphBefore;
    }


    private WordReplaceLoopNode findLoopNode(final int lookFor) {

        String currentScriptLoop = null;
        int indexBeginLoopParagraph = 0;
        int indexBeginLoopRun = 0;
        int indexEndLoopParagraph = 0;
        int indexEndLoopRun = 0;

        for (int i = lookFor; i < this.wdoc.getParagraphs().size(); i++) {
            final XWPFParagraph p = wdoc.getParagraphs().get(i);
            for (int j = 0; j < p.getRuns().size(); j++) {
                final XWPFRun r = p.getRuns().get(j);
                final String text = r.getText(0);
                if (WRCP.SCRIPT.isScript(text)) {
                    final String script = WRCP.SCRIPT.getScriptFormatter(text); // WordReplaceType.SCRIPT.get().getAllContentWithoutSpace(text);
                    if (WRCP.SCRIPT_LOOP.isLoop(script)) {
                        if (currentScriptLoop == null) {
                            currentScriptLoop = script;
                            indexBeginLoopParagraph = i;
                            indexBeginLoopRun = j;
                        } else {
                            if (currentScriptLoop.equals(script)) {
                                indexEndLoopParagraph = i;
                                indexEndLoopRun = j;
                                String loopLabel = WRCP.SCRIPT_LOOP.getContent(script);// WordReplaceType.SCRIPT.get().getCharacterPairLoopContent(script);
                                WordReplaceLoopNode wrln = new WordReplaceLoopNode(
                                        indexBeginLoopParagraph,
                                        indexEndLoopParagraph,
                                        indexBeginLoopRun,
                                        indexEndLoopRun,
                                        loopLabel
                                );
                                return wrln;
                            }
                        }
                    }
                }
            }
        }

        return null;
    }

    private void logDoc() {
        this.log.append("------------------------------------------------------------------------------------\n");
        for (int i = 0; i < this.wdoc.getParagraphs().size(); i++) {
            final XWPFParagraph p = wdoc.getParagraphs().get(i);
            logger.info("paragraph " + i + ": " + p.getText());
            this.log.append("paragraph " + i + ": " + p.getText() + "\n");
        }
        this.log.append("------------------------------------------------------------------------------------\n");
    }


}
