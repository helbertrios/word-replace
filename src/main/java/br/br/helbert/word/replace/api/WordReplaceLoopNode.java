package br.br.helbert.word.replace.api;

class WordReplaceLoopNode {

    private final Integer beginParagraph;
    private final Integer endParagraph;
    private final Integer beginRun;
    private final Integer endRun;
    private final String label;
    private final String source;

    WordReplaceLoopNode(final Integer beginParagraph, final Integer endParagraph, final Integer beginRun, final Integer endRun, final String label, final String source) {
        this.beginParagraph = beginParagraph;
        this.endParagraph = endParagraph;
        this.beginRun = beginRun;
        this.endRun = endRun;
        this.label = label;
        this.source = source;
    }

    Integer getBeginParagraph() {
        return beginParagraph;
    }

    Integer getEndParagraph() {
        return endParagraph;
    }

    Integer getBeginRun() {
        return beginRun;
    }

    Integer getEndRun() {
        return endRun;
    }

    String getLabel() {
        return label;
    }

    public String getSource() {
        return source;
    }

    String getScriptReplace(int pos) {
        String tfr = this.source + WordReplaceCharacterLoop.OPEN_SCRIPT_LOOP + pos + WordReplaceCharacterLoop.CLOSE_SCRIPT_LOOP;
        return tfr;
    }

    String getKeySize() {
            String keySize = this.source + WordReplaceCharacterLoop.LOOP_SIZE;
            return keySize;
    }


    boolean isInternalLoop() {
        return beginParagraph == endParagraph;
    }
}
