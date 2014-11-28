package org.shai.xmodifier;

import org.junit.Assert;
import org.junit.Test;
import org.shai.xmodifier.util.StringUtils;

import java.util.Arrays;

/**
 * Created by Shenghai on 14-11-28.
 */
public class StringUtilTest {
    @Test
    public void splitBySeparator() {
        String s = "ns:root//ns:element1/ns:element11";
        String[] strings = StringUtils.splitBySeparator(s, new String[]{"/", "//"}, new char[][]{{'\'', '\''}, {'[', ']'}, {'(', ')'}}, true);
        Assert.assertEquals("[ns:root, //ns:element1, /ns:element11]", Arrays.toString(strings));
    }

    @Test
    public void splitBySeparator2() {
        String s = "@attr=1";
        String[] strings = StringUtils.splitBySeparator(s, new String[]{"/", "//"}, new char[][]{{'\'', '\''}, {'[', ']'}, {'(', ')'}}, true);
        Assert.assertEquals("[@attr=1]", Arrays.toString(strings));

    }

    @Test
    public void removeMarks() {
        String s = "ns:root//ns:element1(:add)/ns:element11(:delete)";
        String s1 = StringUtils.removeMarks(s);
        System.out.println(s1);
    }
}
