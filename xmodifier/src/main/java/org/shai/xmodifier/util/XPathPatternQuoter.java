package org.shai.xmodifier.util;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Shenghai.Geng
 */
public class XPathPatternQuoter {
    private Map<Character, Character> quoterMap = new HashMap<Character, Character>();
    private Map<Character, Boolean> statusMap = new HashMap<Character, Boolean>();

    /**
     * Check specified character is in quotes collection
     *
     * @param c Character use to check
     */
    public void check(char c) {
        for (Map.Entry<Character, Character> entry : quoterMap.entrySet()) {
            Character start = entry.getKey();
            Character end = entry.getValue();
            if (c == start) {
                Boolean status = statusMap.get(start);
                if (status == null) {
                    status = false;
                }
                statusMap.put(start, !status);
                break;
            }
            if (c == end) {
                Boolean status = statusMap.get(start);
                if (status == null) {
                    status = false;
                }
                statusMap.put(start, !status);
                break;
            }
        }
    }

    /**
     * Check quotes status.
     *
     * @return true if the character is used.
     */
    public boolean isQuoting() {
        for (Map.Entry<Character, Boolean> entry : statusMap.entrySet()) {
            if (entry.getValue()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Add quotes
     *
     * @param start
     * @param end
     */
    public void addQuoter(char start, char end) {
        quoterMap.put(start, end);
    }
}
