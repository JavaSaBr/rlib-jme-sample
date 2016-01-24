package org.sample.client.document;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import rlib.data.AbstractStreamDocument;
import rlib.util.VarTable;

import java.io.InputStream;

/**
 * Реализация парсинга конфигурации из файла.
 * 
 * @author Ronn
 */
public final class DocumentConfig extends AbstractStreamDocument<VarTable> {

	public static final String NODE_LIST = "list";
    public static final String NODE_SET = "set";

    public static final String ATTR_NAME = "name";
    public static final String ATTR_VALUE = "value";

    public DocumentConfig(InputStream stream) {
		super(stream);
	}

	@Override
	protected VarTable create() {
		return VarTable.newInstance();
	}

	@Override
	protected void parse(final Document document) {
		for(Node child = document.getFirstChild(); child != null; child = child.getNextSibling()) {
			if(NODE_LIST.equals(child.getNodeName())) {
				result.parse(child, NODE_SET, ATTR_NAME, ATTR_VALUE);
			}
		}
	}
}
