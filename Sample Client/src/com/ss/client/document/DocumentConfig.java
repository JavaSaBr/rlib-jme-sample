package com.ss.client.document;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import rlib.data.AbstractStreamDocument;
import rlib.util.VarTable;

import java.io.InputStream;

/**
 * The configuration parser.
 * 
 * @author JavaSaBr
 */
public final class DocumentConfig extends AbstractStreamDocument<VarTable> {

	private static final String NODE_LIST = "list";
    private static final String NODE_SET = "set";

    private static final String ATTR_NAME = "name";
    private static final String ATTR_VALUE = "value";

    public DocumentConfig(InputStream stream) {
		super(stream);
	}

	@Override
	protected VarTable create() {
		return VarTable.newInstance();
	}

	@Override
	protected void parse(@NotNull final Document document) {
		for(Node child = document.getFirstChild(); child != null; child = child.getNextSibling()) {
			if(NODE_LIST.equals(child.getNodeName())) {
				result.parse(child, NODE_SET, ATTR_NAME, ATTR_VALUE);
			}
		}
	}
}
