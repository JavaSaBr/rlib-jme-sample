package com.ss.server.document;

import com.ss.server.template.NpcVehicleTemplate;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;
import rlib.util.VarTable;
import rlib.util.array.Array;
import rlib.util.array.ArrayFactory;

import java.nio.file.Path;

/**
 * The parser of NPC vehicle templates.
 *
 * @author JavaSaBr
 */
public class DocumentNpcVehicle extends DocumentVehicle<NpcVehicleTemplate> {

    public DocumentNpcVehicle(@NotNull final Path path) {
        super(path);
    }

    @NotNull
    @Override
    protected Array<NpcVehicleTemplate> create() {
        return ArrayFactory.newArray(NpcVehicleTemplate.class);
    }

    @NotNull
    @Override
    protected NpcVehicleTemplate createTemplate(@NotNull final Element element, @NotNull final VarTable vars) {
        return new NpcVehicleTemplate(vars, element);
    }
}
