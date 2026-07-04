package com.isusdlc.ui.mainmenu;

import com.isusdlc.elegant;
import com.isusdlc.framework.base.CustomScreen;
import com.isusdlc.framework.base.UIContext;
import com.isusdlc.framework.msdf.Font;
import com.isusdlc.framework.msdf.Fonts;
import com.isusdlc.framework.objects.BorderRadius;
import com.isusdlc.framework.objects.MouseButton;
import com.isusdlc.ui.components.textfield.TextField;
import com.isusdlc.utility.colors.ColorRGBA;
import com.isusdlc.utility.colors.Colors;
import com.isusdlc.utility.gui.GuiUtility;
import com.isusdlc.utility.interfaces.IMinecraft;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;

import java.util.List;

public class FriendsScreen extends CustomScreen implements IMinecraft {
    private final Screen parent;
    private final TextField addField;
    private String message;
    private long messageTime;

    public FriendsScreen(Screen parent) {
        this.parent = parent;
        Font fieldFont = Fonts.MEDIUM.getFont(8);
        this.addField = new TextField(fieldFont);
        this.addField.setPreview("Введите ник");
        this.addField.setTextColor(Colors.getTextColor());
    }

    @Override
    public void render(UIContext context) {
        context.drawRect(0, 0, this.width, this.height, ColorRGBA.BLACK.withAlpha(140));

        float panelWidth = 300;
        float panelHeight = 280;
        float x = (this.width - panelWidth) / 2;
        float y = (this.height - panelHeight) / 2;

        context.drawShadow(x - 5, y - 5, panelWidth + 10, panelHeight + 10, 18, BorderRadius.all(10), ColorRGBA.BLACK.withAlpha(80));
        context.drawSquircle(x, y, panelWidth, panelHeight, 10, BorderRadius.all(10), Colors.getBackgroundColor().withAlpha(230));

        Font titleFont = Fonts.MEDIUM.getFont(10);
        Font buttonFont = Fonts.MEDIUM.getFont(7.5F);
        Font nameFont = Fonts.MEDIUM.getFont(8);

        context.drawCenteredText(titleFont, "Список друзей", this.width / 2, y + 14, Colors.getTextColor());

        float addY = y + 34;
        float fieldHeight = 16;
        float fieldWidth = panelWidth - 28;
        this.addField.setX(x + 14);
        this.addField.setY(addY);
        this.addField.setWidth(fieldWidth - 50);
        this.addField.setHeight(fieldHeight);

        ColorRGBA inputBg = Colors.getBackgroundColor().withAlpha(210);
        context.drawRoundedRect(this.addField.getX(), this.addField.getY(), this.addField.getWidth(), this.addField.getHeight(), BorderRadius.all(6), inputBg);
        if (this.addField.isFocused()) {
            context.drawRoundedBorder(this.addField.getX() - 1, this.addField.getY() - 1, this.addField.getWidth() + 2, this.addField.getHeight() + 2, 0.6F, BorderRadius.all(7), Colors.getAccentColor().withAlpha(130));
        }
        this.addField.render(context);

        float addBtnX = this.addField.getX() + this.addField.getWidth() + 4;
        float addBtnW = 42;
        drawButton(context, addBtnX, addY, addBtnW, fieldHeight, "Доб.", buttonFont);

        float listY = addY + fieldHeight + 8;
        float listHeight = panelHeight - listY + y - 36;

        List<String> friends = elegant.getInstance().getFriendManager().listFriends();
        if (friends.isEmpty()) {
            context.drawText(nameFont, "Список друзей пуст", x + 16, listY + 6, Colors.getTextColor().withAlpha(150));
        } else {
            float itemY = listY + 4;
            for (int i = 0; i < friends.size(); i++) {
                String friend = friends.get(i);
                float itemH = 14;
                if (itemY + itemH > y + panelHeight - 20) break;

                boolean hovered = isHovered(x + 14, itemY, fieldWidth, itemH, context);
                ColorRGBA itemBg = new ColorRGBA(255, 255, 255, hovered ? 10 : 5);
                context.drawRoundedRect(x + 14, itemY, fieldWidth, itemH, BorderRadius.all(4), itemBg);
                context.drawText(nameFont, friend, x + 20, itemY + 3, Colors.getTextColor());

                float delX = x + 14 + fieldWidth - 30;
                boolean delHovered = isHovered(delX, itemY, 26, itemH, context);
                context.drawRoundedRect(delX, itemY, 26, itemH, BorderRadius.all(4), new ColorRGBA(255, 60, 60, delHovered ? 60 : 30));
                context.drawCenteredText(buttonFont, "×", delX + 13, itemY + 3, Colors.getTextColor());

                itemY += itemH + 3;
            }
        }

        float backWidth = 60;
        float backHeight = 16;
        float backX = x + panelWidth - backWidth - 14;
        float backY = y + panelHeight - backHeight - 10;
        drawButton(context, backX, backY, backWidth, backHeight, "Назад", buttonFont);

        if (this.message != null && System.currentTimeMillis() - this.messageTime < 3000) {
            context.drawCenteredText(buttonFont, this.message, this.width / 2, backY - 12, Colors.getAccentColor());
        }
    }

    private void drawButton(UIContext context, float x, float y, float w, float h, String text, Font font) {
        boolean hovered = isHovered(x, y, w, h, context);
        ColorRGBA bg = Colors.getBackgroundColor().mix(ColorRGBA.WHITE, hovered ? 0.15F : 0).withAlpha(200);
        context.drawRoundedRect(x, y, w, h, BorderRadius.all(6), bg);
        context.drawCenteredText(font, text, x + w / 2, y + GuiUtility.getMiddleOfBox(font.height(), h), Colors.getTextColor());
    }

    private boolean isHovered(float x, float y, float w, float h, UIContext context) {
        double mx = context.getMouseX();
        double my = context.getMouseY();
        return mx >= x && mx <= x + w && my >= y && my <= y + h;
    }

    @Override
    public void onMouseClicked(double mouseX, double mouseY, MouseButton button) {
        this.addField.onMouseClicked(mouseX, mouseY, button);

        if (button == MouseButton.LEFT) {
            float panelWidth = 300;
            float panelHeight = 280;
            float px = (this.width - panelWidth) / 2;
            float py = (this.height - panelHeight) / 2;

            float addY = py + 34;
            float fieldHeight = 16;
            float fieldWidth = panelWidth - 28;
            float addBtnX = px + 14 + (fieldWidth - 50) + 4;

            float backWidth = 60;
            float backHeight = 16;
            float backX = px + panelWidth - backWidth - 14;
            float backY = py + panelHeight - backHeight - 10;

            if (mouseX >= backX && mouseX <= backX + backWidth && mouseY >= backY && mouseY <= backY + backHeight) {
                if (TextField.LAST_FIELD != null) TextField.LAST_FIELD.setFocused(false);
                mc.setScreen(this.parent);
                return;
            }

            if (mouseX >= addBtnX && mouseX <= addBtnX + 42 && mouseY >= addY && mouseY <= addY + fieldHeight) {
                String name = this.addField.getBuiltText().trim();
                if (!name.isEmpty()) {
                    elegant.getInstance().getFriendManager().add(name);
                    this.addField.clear();
                    this.addField.setFocused(false);
                    this.message = "Друг добавлен!";
                    this.messageTime = System.currentTimeMillis();
                }
                return;
            }

            List<String> friends = elegant.getInstance().getFriendManager().listFriends();
            float listY = addY + fieldHeight + 8;
            float itemY = listY + 4;
            for (int i = 0; i < friends.size(); i++) {
                float itemH = 14;
                if (itemY + itemH > py + panelHeight - 20) break;
                float delX = px + 14 + fieldWidth - 10;
                if (mouseX >= delX - 30 && mouseX <= delX + 26 && mouseY >= itemY && mouseY <= itemY + itemH) {
                    elegant.getInstance().getFriendManager().remove(friends.get(i));
                    this.message = "Друг удалён!";
                    this.messageTime = System.currentTimeMillis();
                    return;
                }
                itemY += itemH + 3;
            }
        }
        super.onMouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void onMouseReleased(double mouseX, double mouseY, MouseButton button) {
        this.addField.onMouseReleased(mouseX, mouseY, button);
        super.onMouseReleased(mouseX, mouseY, button);
    }

    public void onKeyPressed(int keyCode, int scanCode, int modifiers) {
        this.addField.onKeyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        this.addField.charTyped(chr, modifiers);
        return super.charTyped(chr, modifiers);
    }

    public boolean shouldPause() {
        return false;
    }

    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
    }
}
