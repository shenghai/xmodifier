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

    public Cons() {
    }

    public void setLeft(L left) {
        this.left = left;
    }

    public void setRight(R right) {
        this.right = right;
    }

    public L getLeft() {
        return left;
    }

    public R getRight() {
        return right;
    }

    @Override
    public String toString() {
        return "Cons{" +
                "left=" + left +
                ", right=" + right +
                '}';
    }
}
