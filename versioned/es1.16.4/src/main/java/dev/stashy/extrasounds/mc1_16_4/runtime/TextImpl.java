package dev.stashy.extrasounds.mc1_16_4.runtime;

import dev.stashy.extrasounds.logics.runtime.VersionedText;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class TextImpl extends VersionedText {
    @Override
    public Text empty() {
        return Text.of("");
    }

    @Override
    public Text getDoneText() {
        return ScreenTexts.DONE;
    }

    @Override
    public Text getCancelText() {
        return ScreenTexts.CANCEL;
    }

    @Override
    public Text translatable(String key, Object... args) {
        return new TranslatableText(key, args);
    }
}
