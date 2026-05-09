package xyz.bonfiremc.windy;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class ConfigScreen extends Screen {
    private final Screen parent;
    private WindyConfig workingCopy;
    private EditBox minimumWindHeightBox;
    private EditBox spawnRateMultiplierBox;

    private ConfigScreen(Screen parent) {
        super(Component.translatable("windy.config.title"));
        this.parent = parent;
    }

    public static Screen create(Screen parent) {
        return new ConfigScreen(parent);
    }

    @Override
    protected void init() {
        this.workingCopy = copyOf(WindyConfig.get());
        int centerX = this.width / 2;
        int y = this.height / 4;
        int fieldWidth = 80;
        int buttonWidth = 220;

        this.addRenderableWidget(CycleButton.onOffBuilder(this.workingCopy.spawnWind)
                .create(centerX - buttonWidth / 2, y, buttonWidth, 20, Component.translatable("windy.config.spawnWind"),
                        (button, value) -> this.workingCopy.spawnWind = value));

        y += 28;
        this.addRenderableWidget(CycleButton.onOffBuilder(this.workingCopy.windMustSeeSky)
                .create(centerX - buttonWidth / 2, y, buttonWidth, 20, Component.translatable("windy.config.windMustSeeSky"),
                        (button, value) -> this.workingCopy.windMustSeeSky = value));

        y += 30;
        this.minimumWindHeightBox = new EditBox(this.font, centerX - fieldWidth / 2, y, fieldWidth, 20, Component.translatable("windy.config.minimumWindHeight"));
        this.minimumWindHeightBox.setValue(Integer.toString(this.workingCopy.minimumWindHeight));
        this.addRenderableWidget(this.minimumWindHeightBox);

        y += 30;
        this.spawnRateMultiplierBox = new EditBox(this.font, centerX - fieldWidth / 2, y, fieldWidth, 20, Component.translatable("windy.config.spawnRateMultiplier"));
        this.spawnRateMultiplierBox.setValue(Double.toString(this.workingCopy.spawnRateMultiplier));
        this.addRenderableWidget(this.spawnRateMultiplierBox);

        y += 40;
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, btn -> saveAndClose())
                .bounds(centerX - 102, y, 100, 20)
                .build());
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_CANCEL, btn -> onClose())
                .bounds(centerX + 2, y, 100, 20)
                .build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFF);
        guiGraphics.drawCenteredString(this.font, Component.translatable("windy.config.spawnWind.desc"), this.width / 2, this.height / 4 - 12, 0xA0A0A0);
        guiGraphics.drawCenteredString(this.font, Component.translatable("windy.config.minimumWindHeight"), this.width / 2, this.height / 4 + 60, 0xFFFFFF);
        guiGraphics.drawCenteredString(this.font, Component.translatable("windy.config.spawnRateMultiplier"), this.width / 2, this.height / 4 + 90, 0xFFFFFF);
        guiGraphics.drawCenteredString(this.font, Component.literal("0.0 - 100.0 (1.0 = default)").withStyle(ChatFormatting.DARK_GRAY), this.width / 2, this.height / 4 + 112, 0x808080);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    private void saveAndClose() {
        this.workingCopy.minimumWindHeight = parseInt(this.minimumWindHeightBox.getValue(), this.workingCopy.minimumWindHeight);
        this.workingCopy.spawnRateMultiplier = parseDouble(this.spawnRateMultiplierBox.getValue(), this.workingCopy.spawnRateMultiplier);
        WindyConfig.get().spawnWind = this.workingCopy.spawnWind;
        WindyConfig.get().windMustSeeSky = this.workingCopy.windMustSeeSky;
        WindyConfig.get().minimumWindHeight = this.workingCopy.minimumWindHeight;
        WindyConfig.get().spawnRateMultiplier = this.workingCopy.spawnRateMultiplier;
        WindyConfig.save();
        this.minecraft.setScreen(this.parent);
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parent);
    }

    private static WindyConfig copyOf(WindyConfig original) {
        WindyConfig copy = new WindyConfig();
        copy.spawnWind = original.spawnWind;
        copy.minimumWindHeight = original.minimumWindHeight;
        copy.windMustSeeSky = original.windMustSeeSky;
        copy.spawnRateMultiplier = original.spawnRateMultiplier;
        return copy;
    }

    private static int parseInt(String value, int fallback) {
        try {
            int parsed = Integer.parseInt(value.trim());
            return Math.max(-64, Math.min(320, parsed));
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    private static double parseDouble(String value, double fallback) {
        try {
            double parsed = Double.parseDouble(value.trim());
            return Math.max(0.0D, Math.min(100.0D, parsed));
        } catch (NumberFormatException e) {
            return fallback;
        }
    }
}
