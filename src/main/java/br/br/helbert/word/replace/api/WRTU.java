package br.br.helbert.word.replace.api;


class WRTU {

    static String substring(final String texto, final int indexBegin, final int indexEnd) {
        if (isLimitText(texto, indexBegin) || isLimitText(texto, indexEnd)) {
            return null;
        }
        final int indexBeginCorrect = indexBegin - 1;
        return texto.substring(indexBeginCorrect, indexEnd);
    }

    static boolean isLimitText(final String texto, final int index) {
        int minIndex = 1;
        int maxIndex = texto.length();
        return !(index >= minIndex && index <= maxIndex);
    }

    static boolean between(final int index, WordReplaceIndex indexs) {
        return (index >= indexs.getIndexBegin() && index <= indexs.getIndexEnd());
    }

    static int getIndexBegin(final String text, final int fromIndex, final String textSearch) {
        if (text == null || WRCP.WORD_STRING_EMPTY.equals(text)) {
            return 0;
        }
        final int index = text.indexOf(textSearch, fromIndex);
        if (index == -1) {
            return 0;
        }
        final int result = index + 1;
        return result;
    }

    static int getIndexEnd(final String text, final int fromIndex, final String textSearch) {
        if (text == null || WRCP.WORD_STRING_EMPTY.equals(text)) {
            return 0;
        }
        final int index = text.indexOf(textSearch, fromIndex);
        if (index == -1) {
            return 0;
        }
        final int result = index + textSearch.length();
        return result;
    }

}

