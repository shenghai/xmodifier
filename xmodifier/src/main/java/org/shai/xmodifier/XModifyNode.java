package org.shai.xmodifier;

import org.shai.xmodifier.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Shenghai on 14-11-24.
 */
public class XModifyNode {
    private String xPath;
    private String value;
    private String[] elements;
    private String[] elementXPaths;
    private int index = 0;

    public XModifyNode(String xPath, String value) {
        this.xPath = xPath;
        this.value = value;
        analyseElements(xPath);
    }

    private void analyseElements(String xPath) {
        elements = StringUtils.splitBySeparator(xPath, new String[]{"/", "//"},
                new char[][]{{'\'', '\''}, {'[', ']'}, {'(', ')'}}, false);
        index = 0;
        elementXPaths = StringUtils.conArrays(
                StringUtils.splitBySeparator(xPath, new String[]{"/", "//"},
                        new char[][]{{'\'', '\''}, {'[', ']'}, {'(', ')'}}, true));
    }

    public String getCurNode() {
        return elements[index];
    }

    public String getPreNode() {
        if (index - 1 >= 0) {
            return elements[index - 1];
        }
        return null;
    }

    public boolean moveNext() {
        index++;
        return index < elements.length;
    }

    public String getValue() {
        return value;
    }

    public boolean isAttributeModifier() {
        return getCurNode().startsWith("@");
    }

    public boolean isRootNode() {
        return index == 0 && StringUtils.isEmpty(getCurNode());
    }

    public String getXPath() {
        return xPath;
    }

    public String getCurNodeXPath() {
        return elementXPaths[index];
    }

    @Override
    public String toString() {
        return "XModifyNode{" +
                "xPath='" + xPath + '\'' +
                ", value='" + value + '\'' +
                ", index=" + index +
                '}';
    }

    protected XModifyNode duplicate(){
        XModifyNode node = new XModifyNode(xPath, value);
        node.index = this.index;
        return node;
    }
}
