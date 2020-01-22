package me.swirtzly.regeneration.client.gui.parts;

import net.minecraftforge.fml.client.config.GuiButtonExt;

import java.io.File;

/**
 * Created by Swirtzly
 * on 11/01/2020 @ 11:04
 */
public class BlankButton extends GuiButtonExt {

    private File fileLocation;

    public BlankButton(int id, int xPos, int yPos, String displayString) {
        super(id, xPos, yPos, displayString);
    }

    public BlankButton(int id, int xPos, int yPos, int width, int height, String displayString) {
        super(id, xPos, yPos, width, height, displayString);
    }

    public File getFile() {
        return fileLocation;
    }

    public void setFile(File skin) {
        this.fileLocation = skin;
    }
}
