package dev.stashy.extrasounds.mc1_19.runtime;

import dev.stashy.extrasounds.logics.runtime.VersionedText;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

public class TextImpl extends VersionedText {
    @Override
    public Text empty() {
        return Text.empty();
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
        return Text.translatable(key, args);
    }
}
