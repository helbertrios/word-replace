package br.br.helbert.word.replace.api;

class WordReplaceCharacterLoop {

    static final String OPEN_SCRIPT_LOOP = "[";
    static final String CLOSE_SCRIPT_LOOP = "]";
    static final String BEGIN_LOOP_KEY = "foreach";
    static final String END_LOOP_KEY = "/foreach";
    static final String LOOP_SEPARATOR = "in";

    static final String LABEL_LOOP = ":";
    static final String LOOP_SIZE = ".$size";
    static final String LOOP_INDEX = ".$index";
    static final String LOOP_POS = ".$pos";


    private static final Object mutex = new Object();
    private static volatile WordReplaceCharacterLoop instance;

    private WordReplaceCharacterLoop() {
    }

    static WordReplaceCharacterLoop getInstance() {
        WordReplaceCharacterLoop result = instance;
        if (result == null) {
            synchronized (mutex) {
                result = instance;
                if (result == null)
                    instance = result = new WordReplaceCharacterLoop();
            }
        }
        return result;
    }

    boolean isLoopClose(final String scriptIN) {
        if (scriptIN == null || scriptIN.length() == 0) { return false; }
        if (scriptIN.indexOf(END_LOOP_KEY) < 0) {
            return false;
        }

        return true;
    }

    boolean isLoopOpen(final String scriptIN) {
        if (scriptIN == null || scriptIN.length() == 0) { return false; }
        if (scriptIN.indexOf(BEGIN_LOOP_KEY) < 0 && !this.isLoopClose(scriptIN)) {
            return false;
        }
        final String content = WRCP.SCRIPT.getContent(scriptIN);
        String scriptLessKey = content.replace(BEGIN_LOOP_KEY, WRCP.WORD_STRING_EMPTY);
        String[] itens = scriptLessKey.split(LOOP_SEPARATOR);
        if (itens.length < 2) {
            return false;
        }
        return true;
    }

    String getLabel(final String scriptIN) {
        final String label = this.getValue(scriptIN,1);
        return label;
    }

    String getSource(final String scriptIN) {
        final String source = this.getValue(scriptIN,0);
        return source;
    }

    private String[] getItens(final String scriptIN) {
        final String content = WRCP.SCRIPT.getContent(scriptIN);
        final String scriptLessKey = content.replace(BEGIN_LOOP_KEY, WRCP.WORD_STRING_EMPTY);
        final String[] itens = scriptLessKey.split(LOOP_SEPARATOR);
        return itens;
    }

    private String getValue(final String scriptIN, final int i) {
        final String[] itens = this.getItens(scriptIN);
        final String value = itens[i].replaceAll("\\s+", WRCP.WORD_STRING_EMPTY);
        return value;
    }
}
