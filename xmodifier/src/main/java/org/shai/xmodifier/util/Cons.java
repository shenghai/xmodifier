package org.shai.xmodifier.util;

/**
 * Created by Shenghai on 14-11-28.
 */
public class Cons<L, R> {
    private L left;
    private R right;

    public Cons(L left, R right) {
        this.left = left;
        this.right = right;
    }

    public L getLeft() {
        return left;
    }

    public R getRight() {
        return right;
    }
}
