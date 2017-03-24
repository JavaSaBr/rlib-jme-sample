package com.ss.server.document;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import rlib.data.AbstractFileDocument;
import rlib.util.VarTable;

import java.nio.file.Path;

/**
 * The configuration parser.
 *
 * @author JavaSaBr
 */
public final class DocumentConfig extends AbstractFileDocument<VarTable> {

    private static final String ROOT_NODE = "list";

    public DocumentConfig(@NotNull final Path path) {
        super(path);
    }

    @Override
    protected VarTable create() {
        return VarTable.newInstance();
    }

    @Override
    protected void parse(@NotNull final Document document) {
        for (Node node = document.getFirstChild(); node != null; node = node.getNextSibling()) {
            if (ROOT_NODE.equals(node.getNodeName())) {
                result.parse(node, "set", "name", "value");
            }
        }
    }
}
