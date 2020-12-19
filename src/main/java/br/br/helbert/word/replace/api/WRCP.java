package br.br.helbert.word.replace.api;

import java.util.Locale;

interface WRCP {

    String WORD_TEXT_EMPTY = "";
    String WORD_STRING_EMPTY = "";
    String SCRIPT_OBJECT = ".";
    WordReplaceCharacterScript SCRIPT = WordReplaceCharacterScript.getInstance();
    WordReplaceCharacterFormat SCRIPT_FORMAT = WordReplaceCharacterFormat.getInstance();
    WordReplaceCharacterLoop SCRIPT_LOOP = WordReplaceCharacterLoop.getInstance();
    Locale localeBR = new Locale("pt", "BR");
}
