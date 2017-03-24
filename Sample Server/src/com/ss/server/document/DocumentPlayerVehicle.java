package com.ss.server.document;

import com.ss.server.template.PlayerVehicleTemplate;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;
import rlib.util.VarTable;
import rlib.util.array.Array;
import rlib.util.array.ArrayFactory;

import java.nio.file.Path;

/**
 * The parser of player vehicle templates.
 *
 * @author JavaSaBr
 */
public class DocumentPlayerVehicle extends DocumentVehicle<PlayerVehicleTemplate> {

    public DocumentPlayerVehicle(@NotNull final Path path) {
        super(path);
    }

    @NotNull
    @Override
    protected Array<PlayerVehicleTemplate> create() {
        return ArrayFactory.newArray(PlayerVehicleTemplate.class);
    }

    @NotNull
    @Override
    protected PlayerVehicleTemplate createTemplate(@NotNull final Element element, @NotNull final VarTable vars) {
        return new PlayerVehicleTemplate(vars, element);
    }
}
