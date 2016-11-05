package nl.mikero.spiner.core.transformer.latex.model;

import nl.mikero.spiner.core.transformer.latex.model.command.AbstractCommand;
import nl.mikero.spiner.core.transformer.latex.model.command.UsePackageCommand;
import nl.mikero.spiner.core.transformer.latex.model.command.WhitelineCommand;

public class GamebookLatexDocument extends LatexDocument {
    public GamebookLatexDocument() {
        addCommand(new AbstractCommand("documentclass").options().add("b5paper").and().parameters().add("article").done());
        addCommand(new UsePackageCommand().options().add("utf8").and().parameters().add("inputenc").done());
        addCommand(new UsePackageCommand().parameters().add("wallpaper").done());
        addCommand(new UsePackageCommand().parameters().add("gamebook").done());
        addCommand(new WhitelineCommand());
    }
}