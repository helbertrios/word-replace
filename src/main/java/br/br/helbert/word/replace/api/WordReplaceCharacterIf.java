package br.br.helbert.word.replace.api;

class WordReplaceCharacterIf {

    static final String BEGIN_IF = "if";
    static final String END_IF = "/if";
    static final String IF_CLOSE = "/";

    static final String LOOP_SEPARATOR = "in";


    static final String LABEL_LOOP = ":";
    static final String LOOP_SIZE = ".$size";
    static final String LOOP_INDEX = ".$index";
    static final String LOOP_POS = ".$pos";


    private static final Object mutex = new Object();
    private static volatile WordReplaceCharacterIf instance;

    private WordReplaceCharacterIf() {
    }

    static WordReplaceCharacterIf getInstance() {
        WordReplaceCharacterIf result = instance;
        if (result == null) {
            synchronized (mutex) {
                result = instance;
                if (result == null)
                    instance = result = new WordReplaceCharacterIf();
            }
        }
        return result;
    }


}
