package org.shai.xmodifier;

import org.shai.xmodifier.exception.XModifyFailException;
import org.shai.xmodifier.util.ArrayUtils;
import org.shai.xmodifier.util.StringUtils;
import org.w3c.dom.*;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.*;

/**
 * Created by Shenghai on 14-11-24.
 */
public class XModifier {

    private final Document document;
    private Map<String, String> nsMap = new HashMap<String, String>();
    private List<XModifyNode> xModifyNodes = new ArrayList<XModifyNode>();
    private XPath xPathEvaluator;


    public XModifier(Document document) {
        this.document = document;
    }

    public void setNamespace(String prefix, String url) {
        nsMap.put(prefix, url);
    }

    public void addModify(String xPath, String value) {
        xModifyNodes.add(new XModifyNode(xPath, value));
    }

    public void addModify(String xPath) {
        xModifyNodes.add(new XModifyNode(xPath, null));
    }

    public void modify() {
        initXPath();
        for (XModifyNode xModifyNode : xModifyNodes) {
            try {
                create(document, xModifyNode);
            } catch (Exception e) {
                throw new XModifyFailException(xModifyNode.toString(), e);
            }
        }
    }

    private void create(Node parent, XModifyNode node) throws XPathExpressionException {
        Node newNode;
        if (node.isAttributeModifier()) {
            //attribute
            createAttributeByXPath(parent, node.getCurNode().substring(1), node.getValue());
        } else {
            //element
            if (node.isRootNode()) {
                //root node
                newNode = parent;
                boolean canMoveToNext = node.moveNext();
                if (!canMoveToNext) {
                    //last node
                    newNode.setTextContent(node.getValue());
                } else {
                    //next node
                    create(newNode, node);
                }
            } else if (node.getCurNode().equals("text()")) {
                parent.setTextContent(node.getValue());
            } else {
                //element
                findOrCreateElement(parent, node);
            }
        }

    }

    private void initXPath() {
        XPathFactory factory = XPathFactory.newInstance();
        XPath xPath = factory.newXPath();
        xPath.setNamespaceContext(new NamespaceContext() {
            @Override
            public String getNamespaceURI(String prefix) {
                return nsMap.get(prefix);
            }

            @Override
            public String getPrefix(String namespaceURI) {
                for (Map.Entry<String, String> entry : nsMap.entrySet()) {
                    if (entry.getValue().equals(namespaceURI)) {
                        return entry.getKey();
                    }
                }
                return null;
            }

            @Override
            public Iterator getPrefixes(String namespaceURI) {
                return nsMap.keySet().iterator();
            }
        });
        this.xPathEvaluator = xPath;
    }

    private void createAttributeByXPath(Node node, String current, String value) {
        ((Element) node).setAttribute(current, value);
    }

    private void findOrCreateElement(Node parent, XModifyNode node) throws XPathExpressionException {
        Map<String, Object> aResult = analyzeNodeExpression(node.getCurNode());
        String namespaceURI = (String) aResult.get("namespaceURI");
        String localName = (String) aResult.get("localName");
        String[] conditions = (String[]) aResult.get("conditions");
        String mark = (String) aResult.get("mark");

        if (mark != null && mark.equals("add")) {
            //create new element without double check
            Node newCreatedNode = createNewElement(parent, namespaceURI, localName, conditions);
            boolean canMoveToNext = node.moveNext();
            if (!canMoveToNext) {
                //last node
                newCreatedNode.setTextContent(node.getValue());
            } else {
                //next node
                create(newCreatedNode, node);
            }
            return;
        }

        NodeList existNodeList = (NodeList) xPathEvaluator.evaluate(node.getCurNodeXPath(), parent, XPathConstants.NODESET);
        if (existNodeList.getLength() > 0) {
            for (int i = 0; i < existNodeList.getLength(); i++) {
                XModifyNode newNode = node.duplicate();
                Node item = existNodeList.item(i);
                if (mark != null && mark.equals("delete")) {
                    parent.removeChild(item);
                    continue;
                }
                boolean canMoveToNext = newNode.moveNext();
                if (!canMoveToNext) {
                    //last node
                    item.setTextContent(node.getValue());
                } else {
                    //next node
                    create(item, newNode);
                }
            }
        } else {
            Node newCreatedNode = createNewElement(parent, namespaceURI, localName, conditions);
            Node checkExistNode = (Node) xPathEvaluator.evaluate(node.getCurNodeXPath(), parent, XPathConstants.NODE);
            if (!newCreatedNode.equals(checkExistNode)) {
                throw new RuntimeException("Error to create " + node.getCurNode());
            }
            boolean canMoveToNext = node.moveNext();
            if (!canMoveToNext) {
                //last node
                newCreatedNode.setTextContent(node.getValue());
            } else {
                //next node
                create(newCreatedNode, node);
            }
        }
    }


    private Element createNewElement(Node parent, String namespaceURI, String local, String[] conditions) throws XPathExpressionException {
        Element newElement = document.createElementNS(namespaceURI, local);
        if (ArrayUtils.isNotEmpty(conditions)) {
            for (String condition : conditions) {
                if (StringUtils.containsOnly(condition, "0123456789")) {
                    continue;
                }
                //TODO: support not( ) function, need to refactory
                if (condition.startsWith("not")) {
                    continue;
                }
                String[] strings = StringUtils.splitBySeparator(condition, '=');
                String xpath = strings[0];
                String value = StringUtils.unquote(strings[1]);
                create(newElement, new XModifyNode(xpath, value));
            }
        }
        parent.appendChild(newElement);
        return newElement;
    }

    private Map<String, Object> analyzeNodeExpression(String nodeExpression) {
        //nsPrefix:local[condition][condition]  for example ns:person[@name='john'][@age='16'][job]
        String temp = nodeExpression.trim();
        Map<String, Object> result = new HashMap<String, Object>();

        if (nodeExpression.contains("(:")) {
            String mark = StringUtils.substringBetween(nodeExpression, "(:", ")");
            result.put("mark", mark);
            temp = temp.replaceAll("\\(:.*?\\)", "");
        }

        //1. deal with namespace prefix   temp = nsPrefix:local[condition][condition]
        String[] split = StringUtils.splitBySeparator(temp, ':', new char[][]{{'\'', '\''}, {'[', ']'}, {'(', ')'}}, false);
        String nsPrefix = null;
        if (split[1] != null) {
            nsPrefix = StringUtils.trimToNull(split[0]);
            result.put("nsPrefix", nsPrefix);
            temp = split[1];
        }
        String namespaceURI = nsMap.get(nsPrefix);
        result.put("namespaceURI", namespaceURI);

        //2. deal with local name    temp = local[condition][condition]
        split = StringUtils.splitBySeparator(temp, '[', true);
        if (split[1] == null) {
            String localName = StringUtils.trimToNull(split[0]);
            result.put("localName", localName);
            return result;
        } else {
            String localName = StringUtils.trimToNull(split[0]);
            result.put("localName", localName);
            temp = split[1];
        }

        //3. deal with conditions    temp = [condition][condition]
        split = temp.substring(1).split("\\[");
        String[] conditions = new String[split.length];
        for (int i = 0; i < split.length; i++) {
            String s = split[i];
            conditions[i] = StringUtils.stripEnd(s, "]");
        }
        result.put("conditions", conditions);
        return result;
    }

}
