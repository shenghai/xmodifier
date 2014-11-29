package org.shai.xmodifier.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by Shenghai on 2014/11/29.
 */
public class StringQuoter {

    private List<Cons<String, String>> quoterList = new ArrayList<Cons<String, String>>();
    private Stack<Cons<String, String>> stack = new Stack<Cons<String, String>>();

    public int check(String str) {
        if (isQuoting()) {
            Cons<String, String> peek = stack.peek();
            if (str.startsWith(peek.getRight())) {
                Cons<String, String> pop = stack.pop();
                return pop.getRight().length();
            }
        }
        if (quoterList != null) {
            for (Cons<String, String> quoter : quoterList) {
                if (str.startsWith(quoter.getLeft())) {
                    stack.push(quoter);
                    return quoter.getLeft().length();
                }
            }
        }
        return 0;
    }

    public boolean isQuoting() {
        return !stack.empty();
    }

    public void addQuoter(Cons<String, String> quoter) {
        quoterList.add(quoter);
    }

    public void addAllQuoters(List<Cons<String, String>> quoterList) {
        if (quoterList == null) {
            return;
        }
        this.quoterList.addAll(quoterList);
    }
}
