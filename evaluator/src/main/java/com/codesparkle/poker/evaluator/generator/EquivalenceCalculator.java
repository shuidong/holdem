package com.codesparkle.poker.evaluator.generator;


import com.codesparkle.poker.evaluator.Hand;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EquivalenceCalculator {
    private Map<String, Hand> eqcl;

    public EquivalenceCalculator(InputStream stream) throws IOException {
        Pattern p = Pattern
                .compile("^\\s*(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\w\\s\\w\\s\\w\\s\\w\\s\\w)\\s+(\\w+)\\s+(.*)");
        BufferedReader r = new BufferedReader(new InputStreamReader(stream));
        String line;
        int linec = 0;
        eqcl = new HashMap<>();
        while ((line = r.readLine()) != null) {
            linec++;
            Matcher m = p.matcher(line);
            if (!m.matches()) {
                throw new IOException("Cannot parse line " + linec + ": " + line);
            }
            int id = Integer.parseInt(m.group(1));
            String cards = m.group(6);
            String group = m.group(7);
            String type = m.group(8);
            Hand ec = new Hand(id, group, cards, type);
            if (!group.equals("F") && !group.equals("SF")) {
                eqcl.put("N " + cards, ec);
            } else {
                eqcl.put("F " + cards, ec);
            }
        }
        r.close();
    }

    /**
     * Get the equivalence class. Some equivalence classes do not exist, like pair cards for flush hands. In this case,
     * -1 is returned.
     * 
     * @param topcards
     *            the string of topcards
     * @param flush
     *            true if this is to be a flush hand
     * @return the id of the equivalence class (starting with 1), or -1 if it does not exist
     */
    public int getHandWeakness(String topcards, boolean flush) {
        Hand c = eqcl.get((flush ? "F " : "N ") + topcards);
        if (c == null) {
            assert (flush);
            return -1;
        }
        return c.weakness;
    }

}
