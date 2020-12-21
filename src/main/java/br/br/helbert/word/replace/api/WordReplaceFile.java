package br.br.helbert.word.replace.api;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.*;

public class WordReplaceFile implements Closeable {

    private final File template;
    private final FileInputStream is;
    private final XWPFDocument wdoc;
    private FileOutputStream output;

    public WordReplaceFile(final File template) {
        this.template = template;
        this.is = this.getFileInputStream(template);
        this.wdoc =  this.getXWPFDocument(this.is);
    }

    public XWPFDocument getWdoc() {
        return wdoc;
    }

    public void writeTemplate(final File file) {
        this.output = this.getFileOutputStream(file);
        this.writeFile();
    }



    private void writeFile() {
        try {
            this.wdoc.write(output);
            this.output.flush();
        } catch (IOException e) {
            final String msg = "It was not possible write file.";
            throw new RuntimeException(msg, e);
        }
    }

    private FileOutputStream getFileOutputStream(final File file) {
        FileOutputStream output = null;
        try {
            output = new FileOutputStream(file);
            return output;
        } catch (FileNotFoundException e) {
            final String msg = "It was not possible to create file  "+file.getAbsolutePath()+".";
            throw new RuntimeException(msg, e);
        }
    }

    private FileInputStream getFileInputStream(final File file) {
        FileInputStream is = null;
        try {
            is = new FileInputStream(file);
            return is;
        } catch (FileNotFoundException e) {
            final String msg = "File not found" + file.getAbsolutePath() + ".";
            throw new RuntimeException(msg, e);
        }
    }

    private XWPFDocument getXWPFDocument(final FileInputStream is) {
        XWPFDocument wdoc = null;
        try {
            wdoc = new XWPFDocument(is);
            return wdoc;
        } catch (IOException e) {
            final String msg = "It was not possible to instantiate the class XWPFDocument";
            throw new RuntimeException(msg, e);
        }
    }

    @Override
    public void close() throws IOException {
        if (this.is != null) { this.is.close(); }
        if (this.output != null) { this.output.close(); }

    }
}
