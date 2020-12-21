package br.br.helbert.word.replace.api;

interface WRCP {

    String WORD_TEXT_EMPTY = "";
    String WORD_STRING_EMPTY = "";
    String SCRIPT_OBJECT = ".";
    Character CHARACTER_LITERAL_STRING_SIMPLE_OPEN = 8216;
    Character CHARACTER_LITERAL_STRING_SIMPLE_CLOSE = 8217;
    Character CHARACTER_LITERAL_STRING_DOUBLE_OPEN = 8220;
    Character CHARACTER_LITERAL_STRING_DOUBLE_CLOSE = 8221;

    Character CHARACTER_LITERAL_STRING_DEFAULT_DOUBLE = '\'';
    Character CHARACTER_LITERAL_STRING_DEFAULT_SIMPLE = '\"';


    Character SPACE = ' ';
    WordReplaceCharacterScript SCRIPT = WordReplaceCharacterScript.getInstance();
    WordReplaceCharacterFormat SCRIPT_FORMAT = WordReplaceCharacterFormat.getInstance();
    WordReplaceCharacterLoop SCRIPT_LOOP = WordReplaceCharacterLoop.getInstance();
}
