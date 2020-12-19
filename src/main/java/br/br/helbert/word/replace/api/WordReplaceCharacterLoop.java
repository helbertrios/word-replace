package br.br.helbert.word.replace.api;

class WordReplaceCharacterLoop {

    static final String OPEN_SCRIPT_LOOP = "[";
    static final String CLOSE_SCRIPT_LOOP = "]";
    static final String LABEL_LOOP = ":";
    static final String LOOP_SIZE = ".size";
    static final String LOOP_INDEX = ".index";
    static final String LOOP_POS = ".pos";

    private static volatile WordReplaceCharacterLoop instance;
    private static Object mutex = new Object();

    private WordReplaceCharacterLoop() { }

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


    boolean isLoop(final String scriptIN) {
        final String script = scriptIN.trim();
        final int total = WRCP.SCRIPT.OPEN_SCRIPT.length() + WRCP.SCRIPT.CLOSE_SCRIPT.length() + 2;
        if (script == null || script.length() < total) { return false; }
        int i = WRCP.SCRIPT.OPEN_SCRIPT.length() + 1;
        int j = script.length() - (WRCP.SCRIPT.CLOSE_SCRIPT.length() + 1);
        String tupleBegin = script.substring(0, i);
        String tupleEnd = script.substring(j);

        String tupleBeginLoop = WRCP.SCRIPT.OPEN_SCRIPT + OPEN_SCRIPT_LOOP;
        String tupleEndLoop = CLOSE_SCRIPT_LOOP + WRCP.SCRIPT.CLOSE_SCRIPT;

        return tupleBeginLoop.equals(tupleBegin) && tupleEndLoop.equals(tupleEnd);
    }

    String getContent(final String scriptIN) {
        String script = scriptIN.trim();
        int total = WRCP.SCRIPT.OPEN_SCRIPT.length() + WRCP.SCRIPT.CLOSE_SCRIPT.length() + 2;
        if (script == null || script.length() < total) {
            return null;
        }
        final int i = WRCP.SCRIPT.OPEN_SCRIPT.length() + 1;
        final int j = script.length() - (WRCP.SCRIPT.CLOSE_SCRIPT.length() + 1);
        final String tupleBegin = script.substring(0, i);
        final String tupleEnd = script.substring(j);

        final String tupleBeginLoop = WRCP.SCRIPT.OPEN_SCRIPT + OPEN_SCRIPT_LOOP;
        final String tupleEndLoop = CLOSE_SCRIPT_LOOP + WRCP.SCRIPT.CLOSE_SCRIPT;
        script = script.replace(tupleBeginLoop, WRCP.WORD_STRING_EMPTY);
        script = script.replace(tupleEndLoop, WRCP.WORD_STRING_EMPTY);
        return script;
    }

}
