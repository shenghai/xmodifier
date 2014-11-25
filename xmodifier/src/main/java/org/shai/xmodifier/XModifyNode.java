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
    private List<String> elements = new ArrayList<String>();
    private List<String> elementXPaths = new ArrayList<String>();
    private int index = 0;


    public XModifyNode(String expression) {
        String[] split = StringUtils.splitTwoWithKey1Key2ByLast(expression, "=>", "=");
        xPath = split[0];
        value = split[1];
        elements = Arrays.asList(xPath.split("/+"));
        index = 0;
        String temp = xPath;
        for (String element : elements) {
            int i = temp.indexOf(element);
            String substring = temp.substring(0, i + element.length());
            if (elementXPaths.size() > 0) {
                elementXPaths.add(elementXPaths.get(elementXPaths.size() - 1) + substring);
            }else{
                elementXPaths.add(substring);
            }
            temp = xPath.substring(i + element.length());
        }
    }

    public XModifyNode(String xPath, String value) {
        this(xPath + "=" + value);
    }

    public String getCurNode() {
        return elements.get(index);
    }

    public String getPreNode() {
        if (index - 1 >= 0) {
            return elements.get(index - 1);
        }
        return null;
    }

    public boolean moveNext() {
        index++;
        return index < elements.size();
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
        return elementXPaths.get(index);
    }

//    public String getXPath() {
//        return xPath;
//    }
//
//    public String getValue() {
//        return value;
//    }
//
//    public String getXPathHeader() {
//        return xPathHeader;
//    }
//
//    public String getXPathRest() {
//        return restPath;
//    }
//
//    public boolean isAttributeModifier() {
//        return xPathHeader.startsWith("@");
//    }
//
//    public void getHeaderXPath() {
//
//    }
//
//    public void moveToNextElement() {
//
//    }
}
