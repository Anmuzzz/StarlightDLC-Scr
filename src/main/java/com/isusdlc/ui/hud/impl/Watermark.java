package com.isusdlc.ui.hud.impl;

import com.isusdlc.framework.base.UIContext;
import com.isusdlc.framework.msdf.Fonts;
import com.isusdlc.framework.objects.BorderRadius;
import com.isusdlc.systems.modules.modules.visuals.Interface;
import com.isusdlc.systems.setting.settings.BooleanSetting;
import com.isusdlc.ui.hud.HudElement;
import com.isusdlc.utility.colors.ColorRGBA;
import com.isusdlc.utility.colors.Colors;
import net.minecraft.client.network.PlayerListEntry;

public class Watermark extends HudElement {
   private final BooleanSetting showRole = new BooleanSetting(this, "hud.watermark.show_role").enabled(true);
   private final BooleanSetting showPing = new BooleanSetting(this, "hud.watermark.show_ping").enabled(true);
   private final BooleanSetting showFps = new BooleanSetting(this, "hud.watermark.show_fps").enabled(true);

   public Watermark() {
      super("hud.watermark", "icons/hud/watermark.png");
   }

   @Override
   public void update(UIContext context) {
      this.height = 20.0F;

      float padding = 6.0F;
      float pillGap = 4.0F;
      float totalWidth = padding;

      String clientName = "StarlightDLC";
      float nameFontSize = 7.5F;
      float pillHorizontalPadding = 8.0F;
      float nameWidth = Fonts.MEDIUM.getFont(nameFontSize).width(clientName);
      totalWidth += pillHorizontalPadding * 2.0F + nameWidth;

      if (this.showFps.isEnabled()) {
         String fpsText = mc.getCurrentFps() + " FPS";
         float statFontSize = 7.0F;
         float fpsWidth = Fonts.REGULAR.getFont(statFontSize).width(fpsText);
         totalWidth += pillGap + pillHorizontalPadding * 2.0F + fpsWidth;
      }

      if (this.showPing.isEnabled()) {
         String pingText = this.getPing() + " MS";
         float statFontSize = 7.0F;
         float pingWidth = Fonts.REGULAR.getFont(statFontSize).width(pingText);
         totalWidth += pillGap + pillHorizontalPadding * 2.0F + pingWidth;
      }

      this.width = totalWidth + padding;
      super.update(context);
   }

   @Override
   protected void renderComponent(UIContext context) {
      this.height = 20.0F;

      float padding = 6.0F;
      float pillGap = 4.0F;
      float pillRadius = 6.0F;
      float pillHorizontalPadding = 8.0F;
      float centerY = this.y + this.height / 2.0F;
      float x = this.x + padding;

      context.drawRoundedRect(
         this.x,
         this.y,
         this.width,
         this.height,
         BorderRadius.all(7.0F),
         Colors.getBackgroundColor().withAlpha(160.0F)
      );

      String clientName = "StarlightDLC";
      float nameFontSize = 7.5F;
      float nameWidth = Fonts.MEDIUM.getFont(nameFontSize).width(clientName);
      float namePillWidth = pillHorizontalPadding * 2.0F + nameWidth;

      context.drawRoundedRect(
         x,
         this.y + 2.0F,
         namePillWidth,
         this.height - 4.0F,
         BorderRadius.all(pillRadius),
         Colors.getBackgroundColor().withAlpha(210.0F)
      );

      context.drawText(
         Fonts.MEDIUM.getFont(nameFontSize),
         clientName,
         x + pillHorizontalPadding,
         centerY - Fonts.MEDIUM.getFont(nameFontSize).height() / 2.0F,
         Colors.getTextColor()
      );

      x += namePillWidth + pillGap;

      if (this.showFps.isEnabled()) {
         int fps = mc.getCurrentFps();
         String fpsText = fps + " FPS";
         float statFontSize = 7.0F;
         float fpsWidth = Fonts.REGULAR.getFont(statFontSize).width(fpsText);
         float pillWidth = pillHorizontalPadding * 2.0F + fpsWidth;

         context.drawRoundedRect(
            x,
            this.y + 2.0F,
            pillWidth,
            this.height - 4.0F,
            BorderRadius.all(pillRadius),
            Colors.getBackgroundColor().withAlpha(190.0F)
         );

         context.drawText(
            Fonts.REGULAR.getFont(statFontSize),
            fpsText,
            x + pillHorizontalPadding,
            centerY - Fonts.REGULAR.getFont(statFontSize).height() / 2.0F,
            Colors.getTextColor()
         );

         x += pillWidth + pillGap;
      }

      if (this.showPing.isEnabled()) {
         int ping = this.getPing();
         String pingText = ping + " MS";
         float statFontSize = 7.0F;
         float pingWidth = Fonts.REGULAR.getFont(statFontSize).width(pingText);
         float pillWidth = pillHorizontalPadding * 2.0F + pingWidth;

         context.drawRoundedRect(
            x,
            this.y + 2.0F,
            pillWidth,
            this.height - 4.0F,
            BorderRadius.all(pillRadius),
            Colors.getBackgroundColor().withAlpha(190.0F)
         );

         context.drawText(
            Fonts.REGULAR.getFont(statFontSize),
            pingText,
            x + pillHorizontalPadding,
            centerY - Fonts.REGULAR.getFont(statFontSize).height() / 2.0F,
            this.getPingColor(ping)
         );
      }
   }

   private ColorRGBA getPingColor(int ping) {
      if (ping <= 45) return new ColorRGBA(80f, 255f, 80f, 255f);
      if (ping <= 90) return new ColorRGBA(255f, 220f, 60f, 255f);
      return new ColorRGBA(255f, 80f, 80f, 255f);
   }

   private int getPing() {
      if (mc.player != null && mc.getNetworkHandler() != null) {
         PlayerListEntry entry = mc.getNetworkHandler().getPlayerListEntry(mc.player.getUuid());
         if (entry != null) return entry.getLatency();
      }
      return 0;
   }

   @Override
   public boolean show() {
      return Interface.showWatermark();
   }
}