package br.br.helbert.word.replace.api;

import java.util.ArrayList;
import java.util.List;

class WordReplaceCharacterFormat {

    static final Character OPEN_FORMAT = '(';
    static final Character CLOSE_FORMAT = ')';
    static final String PREFIX_FORMAT_NUMBER = "N:";
    static final Character SEPARATOR_FORMAT = ',';

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

    List<String> getFormats(final String formatContent) {

        List<String> formats = new ArrayList<>();
        if (formatContent == null || formatContent.length() == 0) { return formats; }


        StringBuilder content = new StringBuilder();
        Character characterSpecial = null;
        for (int i = 0; i < formatContent.length(); i++) {
            Character c = formatContent.charAt(i);
            characterSpecial = this.getCharacterEspecial(c, characterSpecial);
            if (characterSpecial == null && content.length() > 0) {
                formats.add(content.toString());
                content.setLength(0);
            } else if (characterSpecial != null && (!this.isCharacterSpecial(c))) {
                content.append(c);
            }
        }

        if (content.length() > 0) {
            formats.add(content.toString());
        }
        return formats;
    }

    Character getCharacterEspecial(final Character c, final Character characterSpecial) {

        Character result = characterSpecial;
        if (WRCP.CHARACTER_LITERAL_STRING_DEFAULT_SIMPLE.equals(c) && characterSpecial == null) {
            result = WRCP.CHARACTER_LITERAL_STRING_DEFAULT_SIMPLE;
        } else if (WRCP.CHARACTER_LITERAL_STRING_DEFAULT_SIMPLE.equals(c) &&  WRCP.CHARACTER_LITERAL_STRING_DEFAULT_SIMPLE.equals(characterSpecial)) {
            result = null;
        } else if (WRCP.CHARACTER_LITERAL_STRING_DEFAULT_DOUBLE.equals(c) && characterSpecial == null) {
            result = WRCP.CHARACTER_LITERAL_STRING_DEFAULT_DOUBLE;
        } else if (WRCP.CHARACTER_LITERAL_STRING_DEFAULT_DOUBLE.equals(c) &&  WRCP.CHARACTER_LITERAL_STRING_DEFAULT_DOUBLE.equals(characterSpecial)) {
            result = null;
        } else if (WRCP.CHARACTER_LITERAL_STRING_SIMPLE_OPEN.equals(c) && characterSpecial == null) {
            result = WRCP.CHARACTER_LITERAL_STRING_SIMPLE_CLOSE;
        } else if (WRCP.CHARACTER_LITERAL_STRING_SIMPLE_CLOSE.equals(c) &&  WRCP.CHARACTER_LITERAL_STRING_SIMPLE_CLOSE.equals(characterSpecial)) {
            result = null;
        } else  if (WRCP.CHARACTER_LITERAL_STRING_DOUBLE_OPEN.equals(c) && characterSpecial == null) {
            result = WRCP.CHARACTER_LITERAL_STRING_DOUBLE_CLOSE;
        } else if (WRCP.CHARACTER_LITERAL_STRING_DOUBLE_CLOSE.equals(c) &&  WRCP.CHARACTER_LITERAL_STRING_DOUBLE_CLOSE.equals(characterSpecial)) {
            result = null;
        }
        return result;
    }


    boolean isCharacterSpecial(final Character c) {

        return     WRCP.CHARACTER_LITERAL_STRING_DEFAULT_SIMPLE.equals(c)
                || WRCP.CHARACTER_LITERAL_STRING_DEFAULT_DOUBLE.equals(c)
                || WRCP.CHARACTER_LITERAL_STRING_SIMPLE_OPEN.equals(c)
                || WRCP.CHARACTER_LITERAL_STRING_SIMPLE_CLOSE.equals(c)
                || WRCP.CHARACTER_LITERAL_STRING_DOUBLE_OPEN.equals(c)
                || WRCP.CHARACTER_LITERAL_STRING_DOUBLE_CLOSE.equals(c);

    }


}
