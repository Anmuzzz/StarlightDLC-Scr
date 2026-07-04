package com.isusdlc.display.screens.autobuy.history;

import com.isusdlc.features.autobuy.items.AutoBuyableItem;
import com.isusdlc.framework.msdf.Fonts;
import com.isusdlc.framework.msdf.MsdfFont;
import com.isusdlc.framework.msdf.MsdfRenderer;
import com.isusdlc.framework.objects.BorderRadius;
import com.isusdlc.systems.modules.modules.visuals.Interface;
import com.isusdlc.utility.animation.base.Animation;
import com.isusdlc.utility.animation.base.Easing;
import com.isusdlc.utility.colors.ColorRGBA;
import com.isusdlc.utility.colors.Colors;
import com.isusdlc.utility.interfaces.IMinecraft;
import com.isusdlc.utility.math.MathUtility;
import com.isusdlc.utility.render.DrawUtility;
import com.isusdlc.utility.render.ScissorUtility;
import java.util.List;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.MathHelper;
import org.joml.Matrix4f;

public class HistoryRenderer implements IMinecraft {
   private static HistoryRenderer instance;
   private final HistoryManager historyManager = HistoryManager.getInstance();
   private static final MsdfFont ESSENCE_FONT = Fonts.REGULAR;
   private static final MsdfFont SEMIBOLD_FONT = Fonts.SEMIBOLD;
   private static final MsdfFont MEDIUM_FONT = Fonts.MEDIUM;
   public final Animation animation = new Animation(200, 0.0F, new Easing.BackIn(1.5F));
   private float x;
   private float y;
   private float lastX;
   private float lastY;
   private final float width = 228.0F;
   private static final float ITEM_HEIGHT = 26.0F;
   private static final float ITEM_SPACING = 3.0F;
   private static final float PADDING = 4.0F;
   private static final int VISIBLE_ITEMS = 7;
   private static final float HEADER_HEIGHT = 32.0F;
   private static final float BOTTOM_PADDING = 5.0F;
   private static final float LIST_HEIGHT = 200.0F;
   private final float height = 237.0F;
   private float scroll = 0.0F;
   private float smoothedScroll = 0.0F;
   private float targetScroll = 0.0F;
   private static final float SCROLLBAR_WIDTH = 2.0F;
   private static final ColorRGBA SELLER_COLOR = new ColorRGBA(157.0F, 157.0F, 160.0F, 255.0F);
   private static final ColorRGBA SCROLLBAR_BG_COLOR = new ColorRGBA(27.0F, 27.0F, 30.0F, 255.0F);

   private HistoryRenderer() {
   }

   public static HistoryRenderer getInstance() {
      if (instance == null) {
         instance = new HistoryRenderer();
      }

      return instance;
   }

   public boolean shouldRender() {
      if (mc.currentScreen == null) {
         if (this.animation.getTargetValue() != 0.0F) {
            this.animation.update(0.0F);
         }

         return false;
      } else {
         boolean var2 = false;
         if (mc.currentScreen instanceof GenericContainerScreen var3) {
            String var5 = var3.getTitle().getString();
            var2 = var5.contains("Аукцион") || var5.contains("Аукционы") || var5.contains("Поиск") || var5.contains("Хранилище");
         }

         if (var2) {
            if (this.animation.getTargetValue() != 1.0F) {
               this.animation.update(1.0F);
            }
         } else {
            if (this.animation.getTargetValue() != 0.0F) {
               this.animation.update(0.0F);
            }
         }

         return var2;
      }
   }

   public void render(DrawContext var1, int var2, int var3, float var4) {
      boolean var5 = this.shouldRender();
      float var6 = this.animation.getValue();
      if (var5 || !(var6 <= 0.0F)) {
         this.renderWithAnimation(var1, var2, var3, var4, var6);
      }
   }

   private void renderWithAnimation(DrawContext var1, int var2, int var3, float var4, float var5) {
      MatrixStack var6 = var1.getMatrices();
      if (mc.currentScreen instanceof GenericContainerScreen var7) {
         short var10 = 176;
         int var9 = (var7.width - var10) / 2;
         this.x = Math.max(5.0F, var9 - 228.0F - 10.0F);
         this.y = (var7.height - 237.0F) / 2.0F;
         this.lastX = this.x;
         this.lastY = this.y;
      } else if (this.lastX == 0.0F && this.lastY == 0.0F) {
         this.x = (mc.getWindow().getScaledWidth() - 228.0F) / 2.0F;
         this.y = (mc.getWindow().getScaledHeight() - 237.0F) / 2.0F;
      } else {
         this.x = this.lastX;
         this.y = this.lastY;
      }

      var6.push();
      var6.translate(this.x + 114.0F, this.y + 118.5F, 0.0F);
      var6.scale(var5, var5, 1.0F);
      var6.translate(-(this.x + 114.0F), -(this.y + 118.5F), 0.0F);

      if (Interface.glassSelected()) {
         DrawUtility.drawBlur(var6, this.x, this.y, 228.0F, 237.0F, 11.25F, BorderRadius.all(7.0F), Colors.getBackgroundColor());
      }

      DrawUtility.drawRoundedRect(var6, this.x, this.y, 228.0F, 237.0F, BorderRadius.all(7.0F), Colors.getBackgroundColor());
      this.renderHeader(var1, var6);
      this.renderPurchaseList(var1, var6, var2, var3);
      this.renderScrollbar(var6);

      var6.pop();
   }

   private void renderHeader(DrawContext var1, MatrixStack var2) {
      float var3 = 218.0F;
      float var4 = this.x + 5.0F;
      float var5 = this.y + 5.0F;
      DrawUtility.drawRoundedRect(var2, var4, var5, var3, 22.0F, BorderRadius.all(3.0F),
         Colors.getAccentColor(), Colors.getAccentColor(), Colors.getAccentColor(), Colors.getAccentColor());
      Matrix4f var6 = var2.peek().getPositionMatrix();
      if (ESSENCE_FONT != null && SEMIBOLD_FONT != null) {
         float var9 = 8.0F;
         float var10 = var4 + 10.0F;
         float var11 = var5 + 7.1F;
         MsdfRenderer.renderText(ESSENCE_FONT, "B", var9, Colors.getTextColor().getRGB(), var6, var10, var11, 0.0F);
         float var12 = ESSENCE_FONT.getWidth("B", var9);
         float var13 = var10 + var12 + 5.0F;
         float var14 = 8.0F;
         float var15 = var5 + 6.5F;
         MsdfRenderer.renderText(SEMIBOLD_FONT, "History", var14, Colors.getTextColor().getRGB(), var6, var13, var15, 0.0F);
      }
   }

   private void renderPurchaseList(DrawContext var1, MatrixStack var2, int var3, int var4) {
      List<PurchaseRecord> var5 = this.historyManager.getHistory();
      float var6 = this.x + 4.0F;
      float var7 = this.y + 32.0F;
      float var8 = var5.size() * 29.0F - 3.0F;
      float var9 = Math.max(0.0F, var8 - 200.0F);
      this.targetScroll = MathHelper.clamp(this.targetScroll, -var9, 0.0F);
      this.scroll = MathHelper.clamp(this.scroll, -var9, 0.0F);
      this.smoothedScroll = MathUtility.interpolate(this.smoothedScroll, this.targetScroll, 0.15F);
      ScissorUtility.push(var2, var6, var7, 220.0F, 200.0F);
      float var11 = var7 + this.smoothedScroll;

      for (PurchaseRecord var13 : var5) {
         if (var11 + 26.0F >= var7 - 26.0F && var11 <= var7 + 200.0F + 26.0F) {
            this.renderPurchaseItem(var1, var2, var13, var6, var11);
         }

         var11 += 29.0F;
      }

      ScissorUtility.pop();
      if (var5.isEmpty()) {
         String var20 = "Пусто :(";
         Matrix4f var21 = var2.peek().getPositionMatrix();
         if (MEDIUM_FONT == null) {
            return;
         }

         float var15 = MEDIUM_FONT.getWidth(var20, 9.0F);
         float var16 = var6 + (220.0F - var15) / 2.0F;
         float var17 = var7 + 100.0F;
         float var18 = MEDIUM_FONT.getMetrics().baselineHeight();
         float var19 = var17 - var18 * 9.0F;
         MsdfRenderer.renderText(MEDIUM_FONT, var20, 9.0F, SELLER_COLOR.getRGB(), var21, var16, var19 - 4.0F, 0.0F);
      }
   }

   private void renderPurchaseItem(DrawContext var1, MatrixStack var2, PurchaseRecord var3, float var4, float var5) {
      float var6 = 214.0F;
      DrawUtility.drawRoundedRect(var2, var4, var5, var6, 26.0F, BorderRadius.all(3.0F), Colors.getAdditionalColor());
      float var7 = 14.0F;
      float var8 = var4 + 8.0F;
      float var9 = var5 + (26.0F - var7) / 2.0F;
      var2.push();
      var2.translate(var8, var9 - 0.5, 0.0);
      float var10 = var7 / 16.0F;
      var2.scale(var10, var10, 1.0F);
      ItemStack var11 = var3.getItem();
      if (var11 == null || var11.isEmpty()) {
         var11 = new ItemStack(Items.PAPER);
      }

      var1.drawItem(var11, 0, 0);
      var2.pop();
      float var12 = var8 + var7 + 3.3333333F;
      float var13 = var5 + 7.4999995F;
      var1.drawText(mc.textRenderer, var3.getDisplayName(), (int)var12, (int)var13, Colors.getTextColor().getRGB(), false);
      Matrix4f var14 = var2.peek().getPositionMatrix();
      float var15 = var13 + 9.166666F;
      float var16 = 6.0F;
      if (MEDIUM_FONT != null) {
         float var17 = MEDIUM_FONT.getMetrics().baselineHeight();
         float var18 = var15 - var17 * var16 + 3.0F;
         MsdfRenderer.renderText(MEDIUM_FONT, var3.getSellerName(), var16, SELLER_COLOR.getRGB(), var14, var12, var18, 0.0F);
      }
      String var19 = var3.getFormattedTime();
      float var20 = 6.0F;
      float var21 = MEDIUM_FONT != null ? MEDIUM_FONT.getWidth(var19, var20) : 0.0F;
      float var22 = var4 + var6 - 8.0F - var21;
      String var23 = var3.getFormattedPrice();
      float var24 = 7.0F;
      if (SEMIBOLD_FONT != null) {
         float var25 = SEMIBOLD_FONT.getWidth(var23, var24);
         float var26 = var22 + var21 - var25;
         float var27 = SEMIBOLD_FONT.getMetrics().baselineHeight();
         float var28 = var13 - var27 * var24 + 3.0F;
         MsdfRenderer.renderText(SEMIBOLD_FONT, var23, var24, Colors.getAccentColor().getRGB(), var14, var26, var28, 0.0F);
      }
      if (MEDIUM_FONT != null) {
         float var29 = MEDIUM_FONT.getMetrics().baselineHeight();
         float var30 = var15 - var29 * var20 + 3.0F;
         MsdfRenderer.renderText(MEDIUM_FONT, var19, var20, SELLER_COLOR.getRGB(), var14, var22, var30, 0.0F);
      }
   }

   private void renderScrollbar(MatrixStack var1) {
      List var2 = this.historyManager.getHistory();
      if (!var2.isEmpty()) {
         float var3 = this.x + 228.0F - 4.0F - 2.0F;
         float var4 = this.y + 32.0F;
         float var5 = 200.0F;
         DrawUtility.drawRoundedRect(var1, var3, var4, 2.0F, var5, BorderRadius.all(1.0F), SCROLLBAR_BG_COLOR);
         float var6 = var2.size() * 29.0F - 3.0F;
         if (var6 > 200.0F) {
            float var7 = var6 - 200.0F;
            float var8 = Math.abs(this.smoothedScroll) / var7;
            float var9 = Math.max(15.0F, 200.0F / var6 * var5);
            float var10 = var4 + var8 * (var5 - var9);
            DrawUtility.drawRoundedRect(var1, var3, var10, 2.0F, var9, BorderRadius.all(1.0F), Colors.getAccentColor());
         }
      }
   }

   public boolean mouseScrolled(double var1, double var3, double var5) {
      if (!this.shouldRender()) {
         return false;
      } else if (var1 >= (double)this.x && var1 <= (double)(this.x + 228.0F) && var3 >= (double)this.y && var3 <= (double)(this.y + 237.0F)) {
         float var7 = 29.0F;
         this.targetScroll = (float)(this.targetScroll + var5 * var7);
         return true;
      } else {
         return false;
      }
   }
}
