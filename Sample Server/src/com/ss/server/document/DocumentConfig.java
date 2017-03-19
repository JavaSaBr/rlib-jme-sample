package com.ss.server.document;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import rlib.data.AbstractFileDocument;
import rlib.util.VarTable;

import java.nio.file.Path;

/**
 * Парсер конфига с xml файла.
 *
 * @author Ronn
 */
public final class DocumentConfig extends AbstractFileDocument<VarTable> {

    private static final String ROOT_NODE = "list";

    public DocumentConfig(final Path path) {
        super(path);
    }

    @Override
    protected VarTable create() {
        return VarTable.newInstance();
    }

    @Override
    protected void parse(final Document document) {
        for (Node node = document.getFirstChild(); node != null; node = node.getNextSibling()) {
            if (ROOT_NODE.equals(node.getNodeName())) {
                result.parse(node, "set", "name", "value");
            }
        }
    }
}
