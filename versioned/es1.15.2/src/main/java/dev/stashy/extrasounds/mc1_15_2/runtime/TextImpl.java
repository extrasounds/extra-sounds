package dev.stashy.extrasounds.mc1_15_2.runtime;

import dev.stashy.extrasounds.logics.runtime.VersionedText;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class TextImpl extends VersionedText {
    @Override
    public Text empty() {
        return new LiteralText("");
    }

    @Override
    public Text getDoneText() {
        return new TranslatableText("gui.done");
    }

    @Override
    public Text getCancelText() {
        return new TranslatableText("gui.cancel");
    }

    @Override
    public Text translatable(String key, Object... args) {
        return new TranslatableText(key, args);
    }
}
