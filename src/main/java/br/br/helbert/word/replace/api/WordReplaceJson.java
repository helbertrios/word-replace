package br.br.helbert.word.replace.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.IntNode;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class WordReplaceJson {

    private final String json;
    private final Map<String, Object> values;

    public WordReplaceJson(String json) {
        this.json = json;
        this.values = new HashMap<>();
    }

    public Map<String, Object> getValues() {
        final ObjectMapper mapper = new ObjectMapper();
        try {
            final JsonNode node = mapper.readTree(this.json);
            this.processNode(node, new StringBuilder());
            return this.values;
        } catch (IOException ioe) {
            throw new RuntimeException("Inválid json format.");
        }
    }

    public void write(PrintWriter writer) {
        writer.println("Print keys and values: ");
        for (Map.Entry<String, Object> entry : this.values.entrySet()) {
            final String value = entry.getValue() == null ? WRCP.WORD_STRING_EMPTY : entry.getValue().toString();
            writer.println("key: " + entry.getKey() + ", value: " + value);
        }
    }

    public void processNode(final JsonNode node, final StringBuilder key) {
        if (node.isArray()) {
            this.processNodeArray(node, key);
        } else if (node.isContainerNode()) {
            Iterator<Map.Entry<String, JsonNode>> it = node.fields();
            while (it.hasNext()) {
                Map.Entry<String, JsonNode> field = it.next();
                final StringBuilder keyObject = new StringBuilder(key.toString());
                final String keyAux = WRCP.WORD_STRING_EMPTY.equals(keyObject.toString()) ? field.getKey() : WRCP.SCRIPT_OBJECT + field.getKey();
                keyObject.append(keyAux);
                processNode(field.getValue(), keyObject);
            }
        } else {
            this.values.put(key.toString(), node);
        }
    }

    public void processNodeArray(final JsonNode node, final StringBuilder key) {
        final StringBuilder keyArray = new StringBuilder(key.toString());
        keyArray.append(WRCP.SCRIPT_LOOP.LOOP_SIZE);
        int i = 0;
        for (final JsonNode objInArray : node) {
            final StringBuilder keyElement = new StringBuilder(key.toString());
            keyElement.append(WRCP.SCRIPT_LOOP.OPEN_SCRIPT_LOOP + i + WRCP.SCRIPT_LOOP.CLOSE_SCRIPT_LOOP);
            final String KeyArrayElement =  keyElement.toString();
            this.processNode(objInArray, keyElement);
            final String keyIndex = KeyArrayElement + WRCP.SCRIPT_LOOP.LOOP_INDEX;
            final IntNode index = new IntNode(i);
            this.values.put(keyIndex, index);
            i++;
            final String keyPos = KeyArrayElement + WRCP.SCRIPT_LOOP.LOOP_POS;
            final IntNode pos = new IntNode(i);
            this.values.put(keyPos, pos);
        }
        IntNode size = new IntNode(i);
        this.values.put(keyArray.toString(), size);

    }


}
