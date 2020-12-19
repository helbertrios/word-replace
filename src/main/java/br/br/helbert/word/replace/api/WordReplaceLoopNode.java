package br.br.helbert.word.replace.api;

class WordReplaceLoopNode {

    private final Integer beginParagraph;
    private final Integer endParagraph;
    private final Integer beginRun;
    private final Integer endRun;

    private final String loopLabel;

    WordReplaceLoopNode(Integer beginParagraph, Integer endParagraph, Integer beginRun, Integer endRun, String loopLabel) {
        this.beginParagraph = beginParagraph;
        this.endParagraph = endParagraph;
        this.beginRun = beginRun;
        this.endRun = endRun;
        this.loopLabel = loopLabel;
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

    String getLoopLabel() {
        return loopLabel;
    }

    String getTextForReplace(int pos) {
        final String nameWithoutLabel = this.getLoopLabeltWithouID();
        String tfr = nameWithoutLabel + WordReplaceCharacterLoop.OPEN_SCRIPT_LOOP + pos + WordReplaceCharacterLoop.CLOSE_SCRIPT_LOOP;
        return tfr;
    }

    String getKeySize() {
        final String nameWithoutLabel = this.getLoopLabeltWithouID();
        String keySize = nameWithoutLabel + WordReplaceCharacterLoop.LOOP_SIZE;
        return keySize;
    }

    String getLoopLabeltWithouID() {
        if (loopLabel.contains(WordReplaceCharacterLoop.LABEL_LOOP)) {
            String[] s = loopLabel.split(WordReplaceCharacterLoop.LABEL_LOOP);
            return s[1];
        }
        return loopLabel;
    }

    boolean isInternalLoop() {
        return beginParagraph == endParagraph;
    }
}
