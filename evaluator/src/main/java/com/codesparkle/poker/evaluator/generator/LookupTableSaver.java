package com.codesparkle.poker.evaluator.generator;

import java.io.*;
import java.nio.file.*;

public class LookupTableSaver {

    private CardMultiSet head;

    public LookupTableSaver(CardMultiSet graph) {
        head = graph;
    }

    public void saveGraphAs(String path) throws IOException {
        Path target = Paths.get(path);
        target.getParent().toFile().mkdirs();
        PrintWriter out = new PrintWriter(Files.newBufferedWriter(target));
        CardMultiSet c = head;
        while (c != null) {
            StringBuilder b = new StringBuilder();
            b.append(c.getId());
            for (int i = 0; i < 13; i++) {
                CardMultiSet cms = c.getLink(i);
                b.append("\t");
                if (cms == null)
                    b.append("0");
                else
                    b.append(cms.getId());
            }
            b.append("\t").append(c.getECFlush());
            b.append("\t").append(c.getECNormal());
            b.append("\t").append(c.toString());
            b.append("\t").append(c.getMaxvalnormal());
            b.append("\t").append(c.getMaxvalflush());
            b.append("\t").append(c.getMinvalnormal());
            b.append("\t").append(c.getMinvalflush());
            c = c.getNext();
            out.println(b);
        }
        out.close();
    }

}
