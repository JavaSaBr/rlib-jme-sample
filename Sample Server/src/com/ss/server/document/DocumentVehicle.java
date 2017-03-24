package com.ss.server.document;

import com.ss.server.template.VehicleTemplate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Element;
import rlib.data.AbstractFileDocument;
import rlib.util.VarTable;
import rlib.util.array.Array;

import java.nio.file.Path;

/**
 * The parser of vehicle templates.
 *
 * @author JavaSaBr
 */
public abstract class DocumentVehicle<T extends VehicleTemplate> extends AbstractFileDocument<Array<T>> {

    private static final String ROOT_NODE = "vehicle";

    public DocumentVehicle(@NotNull final Path path) {
        super(path);
    }

    @Override
    protected void handle(@Nullable final Element parent, @NotNull final Element element) {
        super.handle(parent, element);

        if (ROOT_NODE.equals(element.getTagName())) {

            final VarTable vars = VarTable.newInstance();
            vars.parse(element);

            result.add(createTemplate(element, vars));
        }
    }

    @NotNull
    protected abstract T createTemplate(@NotNull final Element element, @NotNull final VarTable vars);
}
