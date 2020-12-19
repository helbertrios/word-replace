package br.br.helbert.word.replace.api;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordReplace {


    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(WordReplace.class);
    private final Map<String, WordReplaceValue> wrvs;

    public WordReplace(final XWPFDocument wdoc, final Map<String, Object> values) {
        this.wrvs = new HashMap<>();
        //this.formatLoopIntern(wdoc, values);

        WordReplaceFormatterLoop wrfl = new WordReplaceFormatterLoop(wdoc, values);
        wrfl.formatter();


        for (int i = 0; i < wdoc.getParagraphs().size(); i++) {
            final XWPFParagraph p = wdoc.getParagraphs().get(i);
            for (XWPFRun r : p.getRuns()) {
                final String text = r.getText(0);
                if (WRCP.SCRIPT.isScript(text)) {
                    final String key = text;
                    WordReplaceValue wrv = wrvs.get(key);
                    if (wrv == null) {
                        wrv = new WordReplaceValue(values, key);
                        this.wrvs.put(key, wrv);
                    }
                    wrv.addReplaceItem(wdoc, p, r);
                }
            }
        }
    }

    public static void INITlIST(String key, Map<String, Object> values, List l, int index) {
        if (index == 0) {
            values.put((key + WRCP.SCRIPT_LOOP.LOOP_SIZE), l.size());
        }
        values.put((key + INDEX(index)), index);
        values.put((key + POS(index)), (index + 1));
    }

    public static String INDEX(int i) {
        return WRCP.SCRIPT_LOOP.OPEN_SCRIPT_LOOP + i + WRCP.SCRIPT_LOOP.CLOSE_SCRIPT_LOOP + WRCP.SCRIPT_LOOP.LOOP_INDEX;
    }

    public static String POS(int i) {
        return WRCP.SCRIPT_LOOP.OPEN_SCRIPT_LOOP + i +WRCP.SCRIPT_LOOP.CLOSE_SCRIPT_LOOP + WRCP.SCRIPT_LOOP.LOOP_POS;
    }

    public void replace() {
        for (Map.Entry<String, WordReplaceValue> entry : this.wrvs.entrySet()) {
            entry.getValue().replace();
        }
    }


}
