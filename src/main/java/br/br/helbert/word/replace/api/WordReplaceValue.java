package br.br.helbert.word.replace.api;

import br.gov.caixa.bsb.siatd.ejb.utils.tools.MiscUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

class WordReplaceValue {


    static final String WORD_TEXT_UPPERCASE = "CAPS";
    static final String WORD_TEXT_LOWERCASE = "caps";
    static final String WORD_TEXT_CAMEL = "Caps";
    private static final String dateDefaultFormat = "dd/MM/yyyy HH:mm:ss";
    private static final String numberDefaultFormat = "##,##,00.0";
    private static final String booleanDefaultFormat = "Sim/Não";
    private final List<WordReplaceItem> wrs;
    private final String script;
    private String format;
    private String scriptFormat;
    private final String keyValue;
    private final Object value;
    private final String valueFormat;

    WordReplaceValue(Map<String, Object> values, String script) {
        this.script = script;
        this.format = null;
        this.scriptFormat = null;


        this.scriptFormat = WRCP.SCRIPT_FORMAT.getScriptFormat(script);
        this.format = WRCP.SCRIPT_FORMAT.getContent(this.scriptFormat);
        this.keyValue = WRCP.SCRIPT.getScriptKey(script, this.scriptFormat);
        this.value = values.get(this.keyValue);
        this.wrs = new ArrayList<>();
        this.valueFormat = this.extractFormatValue();
    }

    void replace() {
        for (WordReplaceItem wri : this.wrs) {
            wri.replace(this.valueFormat);
        }
    }

    void addReplaceItem(XWPFDocument wdoc, XWPFParagraph p, XWPFRun r) {
        this.wrs.add(new WordReplaceItem(wdoc, p, r));
    }

    private String extractFormatValue() {

        if (this.value == null) {
            String o = WRCP.WORD_TEXT_EMPTY;
            return this.getValueFormat(this.format, o);
        } else if (this.value instanceof Boolean) {
            Boolean b = (Boolean) value;
            return this.getValueFormat(this.format, b);
        } else if (this.value instanceof Integer) {
            Integer i = (Integer) value;
            return this.getValueFormat(this.format, i);
        } else if (this.value instanceof Long) {
            Long l = (Long) value;
            return this.getValueFormat(this.format, l);
        } else if (this.value instanceof Float) {
            Double f = (Double) value;
            return this.getValueFormat(this.format, f);
        } else if (this.value instanceof Double) {
            Double d = (Double) value;
            return this.getValueFormat(this.format, d);
        } else if (value instanceof Date) {
            Date dt = (Date) value;
            return this.getValueFormat(this.format, dt);
        } else if (value instanceof String) {
            String s = (String) value;
            return this.getValueFormat(this.format, s);
        }
        return "###" + script + "###";
    }

    private String getValueFormat(String format, final String value) {
        String valueText;
        if (value == null) {
            valueText = WRCP.WORD_TEXT_EMPTY;
        } else {
            String[] formats = getFormats(format);
            valueText = formatString(formats[0], value);
            valueText = formatCase(formats[1], valueText);
        }
        return valueText;
    }

    private String getValueFormat(String format, final Long value) {
        String valueText;
        if (value == null) {
            valueText = WRCP.WORD_TEXT_EMPTY;
        } else {
            String[] formats = getFormats(format);
            valueText = formatNumber(formats[0], value);
            valueText = formatCase(formats[1], valueText);
        }
        return valueText;
    }

    private String getValueFormat(String format, final Integer value) {
        String valueText;
        if (value == null) {
            valueText = WRCP.WORD_TEXT_EMPTY;
        } else {
            String[] formats = getFormats(format);
            valueText = formatNumber(formats[0], value);
            valueText = formatCase(formats[1], valueText);
        }
        return valueText;
    }

    private String getValueFormat(String format, final Float value) {
        format = (format == null ? numberDefaultFormat : format);
        String valueText;
        if (value == null) {
            valueText = WRCP.WORD_TEXT_EMPTY;
        } else {
            String[] formats = getFormats(format);
            final DecimalFormat df = new DecimalFormat(formats[0]);
            valueText = df.format(value);
            valueText = formatCase(formats[1], valueText);
        }
        return valueText;
    }

    private String getValueFormat(String format, final Double value) {
        format = (format == null ? numberDefaultFormat : format);
        String valueText;
        if (value == null) {
            valueText = WRCP.WORD_TEXT_EMPTY;
        } else {
            String[] formats = getFormats(format);
            final DecimalFormat df = new DecimalFormat(formats[0]);
            valueText = df.format(value);
            valueText = formatCase(formats[1], valueText);
        }
        return valueText;
    }


    private String getValueFormat(String format, final Date value) {
        format = (format == null ? dateDefaultFormat : format);
        String valueText;
        if (value == null) {
            valueText = WRCP.WORD_TEXT_EMPTY;
        } else {
            String[] formats = getFormats(format);
            final SimpleDateFormat sdf = new SimpleDateFormat(formats[0], MiscUtils.localeBR);
            valueText = sdf.format(value);
            valueText = formatCase(formats[1], valueText);
        }
        return valueText;
    }

    private String getValueFormat(String format, final Boolean value) {
        format = (format == null ? booleanDefaultFormat : format);
        String valueText;
        if (value == null) {
            valueText = WRCP.WORD_TEXT_EMPTY;
        } else {
            String[] formats = getFormats(format);
            final String[] list = formats[0].split("/");
            valueText = (value.booleanValue() ? list[0] : list[1]);
            valueText = formatCase(formats[1], valueText);
        }
        return valueText;
    }


    private String[] getFormats(String format) {
        String[] formats = new String[2];
        if (format != null) {
            String[] formatsIn = format.split(",");
            formats[0] = formatsIn.length >= 1 ? formatsIn[0] : null;
            formats[1] = formatsIn.length >= 2 ? formatsIn[1] : null;
        }
        return formats;
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

    private String formatString(String format, String value) {
        if (value == null) {
            return null;
        }
        if ("".equals(value)) {
            return value;
        }
        if (format == null || "".equals(format)) {
            return value;
        }
        String newValue = "";
        int pos = 0;
        int i = 0;
        while (value.length() > i) {

            if (pos > format.length()) {
                newValue += value.charAt(i);
            } else {
                if (format.charAt(pos) == '#') {
                    newValue += value.charAt(i);
                } else {
                    newValue += value.charAt(pos);
                    pos++;
                    continue;
                }
            }

            pos++;
            i++;
        }
        return newValue;
    }


    private String formatNumber(String format, Number value) {
        if (value == null) {
            return null;
        }
        if (format == null || "".equals(format)) {
            return value.toString();
        }
        final DecimalFormat df = new DecimalFormat(format);
        return df.format(value);
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


    String getKeyValue() {
        return keyValue;
    }
}
