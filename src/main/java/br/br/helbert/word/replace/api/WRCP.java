package br.br.helbert.word.replace.api;

interface WRCP {

    String WORD_TEXT_EMPTY = "";
    String WORD_STRING_EMPTY = "";
    WordReplaceCharacterScript SCRIPT  = WordReplaceCharacterScript.getInstance();
    WordReplaceCharacterFormat SCRIPT_FORMAT = WordReplaceCharacterFormat.getInstance();
    WordReplaceCharacterLoop SCRIPT_LOOP = WordReplaceCharacterLoop.getInstance();

}
