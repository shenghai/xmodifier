package org.shai.xmodifier.util;

/**
 * Created by Shenghai on 14-11-21.
 */
public class ArrayUtils {
    public static boolean isNotEmpty(String[] array) {
        return (array != null && array.length != 0);
    }

    public static boolean isNotEmpty(char[] array) {
        return (array != null && array.length != 0);
    }

    public static boolean isEmpty(boolean[] array) {
        if (array == null || array.length == 0) {
            return true;
        }
        return false;
    }

    public static boolean isEmpty(String[] array) {
        if (array == null || array.length == 0) {
            return true;
        }
        return false;
    }

    public static boolean isEmpty(char[] array) {
        if (array == null || array.length == 0) {
            return true;
        }
        return false;
    }
}
