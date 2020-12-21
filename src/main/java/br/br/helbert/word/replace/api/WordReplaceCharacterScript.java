package br.br.helbert.word.replace.api;

class WordReplaceCharacterScript {

    static final String OPEN_SCRIPT = "{{";
    static final String CLOSE_SCRIPT = "}}";
    private static final Object mutex = new Object();
    private static volatile WordReplaceCharacterScript instance;

    private WordReplaceCharacterScript() {
    }

    static WordReplaceCharacterScript getInstance() {
        WordReplaceCharacterScript result = instance;
        if (result == null) {
            synchronized (mutex) {
                result = instance;
                if (result == null)
                    instance = result = new WordReplaceCharacterScript();
            }
        }
        return result;
    }

    boolean isScript(final String scriptIN) {
        final String script = scriptIN.trim();
        final int total = OPEN_SCRIPT.length() + CLOSE_SCRIPT.length() + 1;
        if (script == null || script.length() < total) {
            return false;
        }

        int i = script.length() - CLOSE_SCRIPT.length();
        String tupleBegin = script.substring(0, OPEN_SCRIPT.length());
        String tupleEnd = script.substring(i);

        return OPEN_SCRIPT.equals(tupleBegin) && CLOSE_SCRIPT.equals(tupleEnd);
    }

    String getScriptKey(final String script, final String scriptFormat) {
        if (script == null) {
            return null;
        }
        final String scriptWithScriptFormat = scriptFormat == null ? script : script.replace(scriptFormat, WRCP.WORD_STRING_EMPTY);
        final String content = this.getContent(scriptWithScriptFormat).trim();
        return content;
    }

    String getScriptFormatter(final String scriptIN) {
        if (scriptIN == null || scriptIN.length() == 0) { return null; }
        if (WRCP.SCRIPT_LOOP.isLoopOpen(scriptIN)) {
            return scriptIN;
        }

        StringBuilder content = new StringBuilder();
        Character characterSpecial = null;
        for (int i = 0; i < scriptIN.length(); i++) {
            Character c = scriptIN.charAt(i);
            characterSpecial = WRCP.SCRIPT_FORMAT.getCharacterEspecial(c, characterSpecial);
            if ((!WRCP.SPACE.equals(c)) || characterSpecial != null) {
                content.append(c);
            }
        }
        return content.toString();
    }


    public String getContent(final String script) {
        final String aux = script.replace(OPEN_SCRIPT, WRCP.WORD_STRING_EMPTY);
        final String content = aux.replace(CLOSE_SCRIPT, WRCP.WORD_STRING_EMPTY);
        return content;
    }

    public String getScript(final String text, final WordReplaceIndex wri) {
        String content = WRTU.substring(text, wri.getIndexBegin(), wri.getIndexEnd());
        return content;
    }


    public WordReplaceIndex getIndex(final String text, final int fromIndex) {
        final int indexBegin = WRTU.getIndexBegin(text, fromIndex, OPEN_SCRIPT);
        final int indexEnd = WRTU.getIndexEnd(text, fromIndex, CLOSE_SCRIPT);
        if (indexBegin == 0 || indexEnd == 0) {
            return null;
        }
        WordReplaceIndex wri = new WordReplaceIndex(indexBegin, indexEnd);
        return wri;
    }


}
