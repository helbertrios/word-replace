package br.br.helbert.word.replace.api;

class WordReplaceCharacterFormat {

    static final Character OPEN_FORMAT = '<';
    static final Character CLOSE_FORMAT = '>';

    static final String OPEN_FORMAT_STRING = OPEN_FORMAT.toString();
    static final String CLOSE_FORMAT_STRING = CLOSE_FORMAT.toString();
    private static final Object mutex = new Object();
    private static volatile WordReplaceCharacterFormat instance;


    private WordReplaceCharacterFormat() {
    }

    static WordReplaceCharacterFormat getInstance() {
        WordReplaceCharacterFormat result = instance;
        if (result == null) {
            synchronized (mutex) {
                result = instance;
                if (result == null)
                    instance = result = new WordReplaceCharacterFormat();
            }
        }
        return result;
    }

    public String getContentByScriptFormat(final String script) {
        if (script == null || WRCP.WORD_STRING_EMPTY.equals(script)) {
            return null;
        }
        String content = script.replace(OPEN_FORMAT_STRING, WRCP.WORD_STRING_EMPTY);
        content = content.replace(CLOSE_FORMAT_STRING, WRCP.WORD_STRING_EMPTY);
        return content;
    }

    public String getContent(final String text) {
        final WordReplaceIndex wri = this.getIndex(text);
        if (wri == null) {
            return null;
        }
        final String script = this.getScriptFormat(text, wri);
        if (script == null || WRCP.WORD_STRING_EMPTY.equals(script)) {
            return null;
        }
        String content = script.replace(OPEN_FORMAT_STRING, WRCP.WORD_STRING_EMPTY);
        content = content.replace(CLOSE_FORMAT_STRING, WRCP.WORD_STRING_EMPTY);
        return content;
    }

    public String getScriptFormat(final String text) {
        final WordReplaceIndex wri = this.getIndex(text);
        if (wri == null) {
            return null;
        }
        return this.getScriptFormat(text, wri);
    }


    private String getScriptFormat(final String text, final WordReplaceIndex wri) {
        String content = WRTU.substring(text, wri.getIndexBegin(), wri.getIndexEnd());
        return content;
    }


    public WordReplaceIndex getIndex(final String text) {
        final int indexBegin = WRTU.getIndexBegin(text, 0, OPEN_FORMAT_STRING);
        final int indexEnd = WRTU.getIndexEnd(text, 0, CLOSE_FORMAT_STRING);
        if (indexBegin == 0 || indexEnd == 0) {
            return null;
        }
        WordReplaceIndex wri = new WordReplaceIndex(indexBegin, indexEnd);
        return wri;
    }


}
