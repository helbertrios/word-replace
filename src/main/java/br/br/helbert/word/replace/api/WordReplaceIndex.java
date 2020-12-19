package br.br.helbert.word.replace.api;

class WordReplaceIndex {

    private int indexBegin;
    private int indexEnd;
    private String code;
    private String format;

    WordReplaceIndex(int indexBegin, int indexEnd) {
        this.indexBegin = indexBegin;
        this.indexEnd = indexEnd;
        this.code = code;
    }

    WordReplaceIndex(int index, String text) {
        this.indexBegin = index + 1;
        this.indexEnd = index + text.length();
        this.code = null;
    }

    int getIndexBegin() {
        return indexBegin;
    }

    void setIndexBegin(int indexBegin) {
        this.indexBegin = indexBegin;
    }

    int getIndexEnd() {
        return indexEnd;
    }

    void setIndexEnd(int indexEnd) {
        this.indexEnd = indexEnd;
    }

    String getCode() {
        return code;
    }

    void setCode(String code) {
        this.code = code;
    }

    String getFormat() {
        return format;
    }

    void setFormat(String format) {
        this.format = format;
    }

}
