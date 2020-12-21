package br.br.helbert.word.replace.api;

import com.fasterxml.jackson.databind.node.*;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;

class WordReplaceValueMask {

    private final static TreeMap<Integer, String> map = new TreeMap<Integer, String>();

    private static final String WORD_TEXT_UPPERCASE = "upper";
    private static final String WORD_TEXT_LOWERCASE = "lower";
    private static final String WORD_TEXT_CAMEL = "camel";

    private static final String WORD_TEXT_ROMAN = "roman";
    private static final String WORD_TEXT_ALPHABET = "alphabet";

    private static final String WORD_TEXT_CURRENCY = "currency";
    private static final String WORD_TEXT_CURRENCY_LESS = "currencyLess";

    static {

        map.put(1000, "M");
        map.put(900, "CM");
        map.put(500, "D");
        map.put(400, "CD");
        map.put(100, "C");
        map.put(90, "XC");
        map.put(50, "L");
        map.put(40, "XL");
        map.put(10, "X");
        map.put(9, "IX");
        map.put(5, "V");
        map.put(4, "IV");
        map.put(1, "I");

    }


    private final Locale locale;
    private final Object valueOrigin;
    private final List<String> formats;
    private final String valueFormated;

    WordReplaceValueMask(final Locale locale, final Object valueOrigin, final String formatScriptContent) {
        this.locale = locale;
        this.valueOrigin = valueOrigin;
        this.formats = WRCP.SCRIPT_FORMAT.getFormats(formatScriptContent);
        this.valueFormated = this.extractValueFormat();
    }

    String getValueFormated() {
        return valueFormated;
    }

    private String extractValueFormat() {
        if (this.valueOrigin == null) {
            final String o = WRCP.WORD_TEXT_EMPTY;
            return this.getValueFormat(o);
        } else if (this.valueOrigin instanceof TextNode) {
            final TextNode valueNode = (TextNode)this. valueOrigin;
            return this.getValueFormat(valueNode.textValue());
        } else if (this.valueOrigin instanceof IntNode) {
            final IntNode valueNode = (IntNode) this.valueOrigin;
            return this.getValueFormat(valueNode.intValue());
        }  else if (this.valueOrigin instanceof LongNode) {
            final LongNode valueNode = (LongNode) this.valueOrigin;
            return this.getValueFormat(valueNode.longValue());
        } else if (this.valueOrigin instanceof FloatNode) {
            final FloatNode valueNode = (FloatNode) this.valueOrigin;
            return this.getValueFormat(valueNode.floatValue());
        } else if (this.valueOrigin instanceof DoubleNode) {
            final DoubleNode valueNode = (DoubleNode) this.valueOrigin;
            return this.getValueFormat(valueNode.doubleValue());
        } else if (this.valueOrigin instanceof BooleanNode) {
            final BooleanNode valueNode = (BooleanNode) this.valueOrigin;
            return this.getValueFormat(valueNode.booleanValue());
        } else if (this.valueOrigin instanceof DecimalNode) {
            DecimalNode valueNode = (DecimalNode) this.valueOrigin ;
            return this.getValueFormat(valueNode.decimalValue());
        }

        return "#ERROR#";
    }

    private String getValueFormat(final Boolean value) {
        String valueText = WRCP.WORD_TEXT_EMPTY;
        if (value != null) {
            for (int i = 0; i < this.formats.size(); i++ ) {
                final String f = this.formats.get(i);
                if (i == 0) {
                    valueText = formatBoolean(f, value);
                } else {
                    valueText = formatString(f, valueText);
                }
                if ( formats.size() == 0) { valueText = value.toString(); }
            }
            if ( formats.size() == 0) { valueText = value.toString(); }
        }
        return valueText;
    }

    private String getValueFormat(final Integer value) {
        String valueText = WRCP.WORD_TEXT_EMPTY;
        if (value != null) {
            for (int i = 0; i < this.formats.size(); i++ ) {
                final String f = this.formats.get(i);
                if (i == 0) {
                    valueText = formatInteger(f, value);
                } else {
                    valueText = formatString(f, valueText);
                }
            }
            if ( formats.size() == 0) { valueText = value.toString(); }
        }
        return valueText;
    }

    private String getValueFormat(final Long value) {
        String valueText = WRCP.WORD_TEXT_EMPTY;
        if (value != null) {
            for (int i = 0; i < this.formats.size(); i++ ) {
                final String f = this.formats.get(i);
                if (i == 0) {
                    valueText = formatLong(f, value);
                } else {
                    valueText = formatString(f, valueText);
                }
            }
            if ( formats.size() == 0) { valueText = value.toString(); }
        }
        return valueText;
    }

    private String getValueFormat(final Number value) {
        String valueText = WRCP.WORD_TEXT_EMPTY;
        if (value != null) {
            for (int i = 0; i < this.formats.size(); i++ ) {
                final String f = formats.get(i);
                if (i == 0) {
                    valueText = this.formatNumber(f, value);
                } else {
                    valueText = formatString(f, valueText);
                }
            }
            if ( formats.size() == 0) { valueText = value.toString(); }
        }
        return valueText;
    }

    private String getValueFormat(final String value) {
        String valueText = WRCP.WORD_TEXT_EMPTY;
        if (value != null) {
            for (int i = 0; i < this.formats.size(); i++ ) {
                final String f = this.formats.get(i);
                valueText = formatString(f, value);
            }
            if ( formats.size() == 0) { valueText = value.toString(); }
        }
        return valueText;
    }







    private String formatBoolean(String format, Boolean value) {
        final String[] list = format.split("/");
        String valueTrue = list.length > 0 ?  list[0] : value.toString();
        String valueFalse = list.length > 1 ?  list[1] : value.toString();
        String valueText = value.booleanValue() ? valueTrue : valueFalse;
        return valueText;
    }


    private String formatInteger(String format, Integer value) {
        if (value == null ) { return WRCP.WORD_TEXT_EMPTY; }
        if (format == null || WRCP.WORD_STRING_EMPTY.equals(format)) {
            return value.toString();
        }

        if (this.isNumberEspecialRepresentation(format)) {
            return this.formatNumberEspecialRepresentation(format, value);
        }

        return formatNumber(format, value);
    }

    private String formatLong(String format, Long value) {
        if (value == null ) { return WRCP.WORD_TEXT_EMPTY; }
        if (format == null || WRCP.WORD_STRING_EMPTY.equals(format)) {
            return value.toString();
        }

        if (format.length() > 2) {
            String prefix = format.substring(0,2);
            if (WRCP.SCRIPT_FORMAT.equals(prefix)) {
                return formatNumber(format, value);
            }
        }

        return formatDate(format, value);
    }

    private String formatNumber(String format, Number value) {
        if (value == null ) { return WRCP.WORD_TEXT_EMPTY; }
        if (format == null || WRCP.WORD_STRING_EMPTY.equals(format)) {
            return value.toString();
        }
        if (this.isCurrency(format) && (value instanceof Double || value instanceof Float || value instanceof Float)) {
            return this.formatCurrency(format, value);
        }
        DecimalFormat nf = new DecimalFormat(format);
        return nf.format(value);
    }

    private String formatDate(String format, Long value) {
        if (value == null ) { return WRCP.WORD_TEXT_EMPTY; }
        if (format == null || WRCP.WORD_STRING_EMPTY.equals(format)) {
            return value.toString();
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format, this.locale);
        return sdf.format(value);
    }

    private String formatString(String format, String value) {
        if (value == null || WRCP.WORD_STRING_EMPTY.equals(value)) { return WRCP.WORD_TEXT_EMPTY;}
        if (format == null || WRCP.WORD_STRING_EMPTY.equals(format)) {
            return value;
        }

        if (isCaseFormat(format)) {
            return  formatCase(format, value);
        }
        return formatStringEspecial(format, value);
    }




    private boolean isCaseFormat(String format) {
        return WORD_TEXT_UPPERCASE.equals(format) || WORD_TEXT_LOWERCASE.equals(format) || WORD_TEXT_CAMEL.equals(format);
    }


    private String formatCase(String format, String value) {
        if (WORD_TEXT_UPPERCASE.equals(format)) {
            return value.toUpperCase();
        } else if (WORD_TEXT_LOWERCASE.equals(format)) {
            return value.toLowerCase();
        } else if (WORD_TEXT_CAMEL.equals(format)) {
            return capitalize(value);
        }
        return value;
    }

    private boolean isNumberEspecialRepresentation(String format) {
        return WORD_TEXT_ALPHABET.equals(format) || WORD_TEXT_ROMAN.equals(format);
    }

    private String formatNumberEspecialRepresentation(String format, Integer value) {
        if (WORD_TEXT_ALPHABET.equals(format)) {
            return this.toAlphabetic(value);
        } else if (WORD_TEXT_ROMAN.equals(format)) {
            return this.toRoman(value);
        }
        return value.toString();
    }

    private boolean isCurrency(String format) {
        return WORD_TEXT_CURRENCY.equals(format) || WORD_TEXT_CURRENCY_LESS.equals(format);
    }

    private String formatCurrency(String format, Number value) {
        if (WORD_TEXT_CURRENCY.equals(format)) {
            NumberFormat nf = DecimalFormat.getCurrencyInstance(this.locale);
            return nf.format(value);
        } else if (WORD_TEXT_CURRENCY_LESS.equals(format)) {
            NumberFormat nf = DecimalFormat.getCurrencyInstance(this.locale);
            DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) nf).getDecimalFormatSymbols();
            decimalFormatSymbols.setCurrencySymbol(WRCP.WORD_STRING_EMPTY);
            ((DecimalFormat) nf).setDecimalFormatSymbols(decimalFormatSymbols);
            return nf.format(value);
        }
        return value.toString();
    }

    private String formatStringEspecial(String format, String value) {
        String newValue = WRCP.WORD_STRING_EMPTY;
        int pos = 0;
        int i = 0;
        while ( i < value.length()) {
            if (pos < format.length()) {
                if (format.charAt(pos) == '#') {
                    newValue += value.charAt(i++);
                    pos++;
                } else {
                    newValue += format.charAt(pos++);
                }
            } else {
                newValue += value.charAt(i++);
            }
        }
        return newValue;
    }




    private String capitalize(String name) {
        if (name != null && name.length() != 0) {
            char[] chars = name.toCharArray();
            chars[0] = Character.toUpperCase(chars[0]);
            return new String(chars);
        } else {
            return name;
        }
    }


    private  String toAlphabetic(Integer i) {
        if (i == null || i == 0) { return WRCP.WORD_TEXT_EMPTY; }
        if (i < 0) { return ("-"+toAlphabeticBase(Math.abs(i))).toLowerCase(); }
        return toAlphabeticBase(i).toLowerCase();
    }

    private String toRoman(Integer i) {
        if (i == null || i == 0) { return WRCP.WORD_TEXT_EMPTY; }
        if (i < 0) { return ("-"+toRomanBase(Math.abs(i))); }
        return toRomanBase((i));
    }

    private String toRomanBase(int number) {
        int l =  map.floorKey(number);
        if ( number == l ) {
            return map.get(number);
        }
        return map.get(l) + toRomanBase(number-l);
    }

    private String toAlphabeticBase(int i) {
        int quot = (i/26);
        int rem = (i%26);
        if( quot == 0 ) {
            char letter = (char)((int)'@' + rem);
            return ""+letter;
        } else {
            char letter = (char)((int)'A' + rem);
            return toAlphabeticBase(quot) + letter;
        }
    }

}
