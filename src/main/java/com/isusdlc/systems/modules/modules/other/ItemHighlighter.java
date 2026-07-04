package com.isusdlc.systems.modules.modules.other;

import com.isusdlc.framework.base.CustomDrawContext;
import com.isusdlc.framework.objects.BorderRadius;
import com.isusdlc.mixin.accessors.HandledScreenAccessor;
import com.isusdlc.systems.event.EventListener;
import com.isusdlc.systems.event.impl.render.PostHudRenderEvent;
import com.isusdlc.systems.event.impl.render.ScreenRenderEvent;
import com.isusdlc.systems.modules.api.ModuleCategory;
import com.isusdlc.systems.modules.api.ModuleInfo;
import com.isusdlc.systems.modules.impl.BaseModule;
import com.isusdlc.systems.setting.settings.ColorSetting;
import com.isusdlc.systems.setting.settings.SelectSetting;
import com.isusdlc.utility.colors.ColorRGBA;
import com.isusdlc.utility.colors.Colors;
import com.isusdlc.utility.game.PotionUtility;
import com.isusdlc.utility.interfaces.IScaledResolution;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.screen.slot.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

@ModuleInfo(
   name = "Item Highlighter",
   category = ModuleCategory.OTHER,
   desc = "modules.descriptions.item_highlighter"
)
@Environment(EnvType.CLIENT)
public class ItemHighlighter extends BaseModule {
   private static final float HIGHLIGHT_ALPHA = 0.55F;
   private static final int HOTBAR_SLOT_SIZE = 20;
   private static final int HOTBAR_TOTAL_WIDTH = 182;
   private static final int HOTBAR_TOP_OFFSET = 22;
   private final SelectSetting highlightItems = new SelectSetting(this, "modules.settings.item_highlighter.items").min(0);
   private final SelectSetting.Value hlTotem = new SelectSetting.Value(this.highlightItems, "Тотем").select();
   private final SelectSetting.Value hlEnchantedApple = new SelectSetting.Value(this.highlightItems, "Зачарованное яблоко").select();
   private final SelectSetting.Value hlGoldenApple = new SelectSetting.Value(this.highlightItems, "Золотое яблоко").select();
   private final SelectSetting.Value hlGoldenCarrot = new SelectSetting.Value(this.highlightItems, "Золотая морковь").select();
   private final SelectSetting.Value hlHealingPotion = new SelectSetting.Value(this.highlightItems, "Исцел").select();
   private final SelectSetting.Value hlEnderEye = new SelectSetting.Value(this.highlightItems, "Дезориентация").select();
   private final SelectSetting.Value hlNetheriteScrap = new SelectSetting.Value(this.highlightItems, "Трапка").select();
   private final SelectSetting.Value hlFireCharge = new SelectSetting.Value(this.highlightItems, "Огненный смерч").select();
   private final SelectSetting.Value hlNetherStar = new SelectSetting.Value(this.highlightItems, "Стан").select();
   private final SelectSetting.Value hlDriedKelp = new SelectSetting.Value(this.highlightItems, "Пласт").select();
   private final SelectSetting.Value hlPhantomMembrane = new SelectSetting.Value(this.highlightItems, "Божья аура").select();
   private final SelectSetting.Value hlSugar = new SelectSetting.Value(this.highlightItems, "Явная пыль").select();
   private final SelectSetting.Value hlSnowball = new SelectSetting.Value(this.highlightItems, "Ком снега").select();
   private final SelectSetting.Value hlPoppedChorusFruit = new SelectSetting.Value(this.highlightItems, "Трапка (HW)").select();
   private final SelectSetting.Value hlPrismarineShard = new SelectSetting.Value(this.highlightItems, "Взрывная трапка").select();
   private final SelectSetting.Value hlFireworkStar = new SelectSetting.Value(this.highlightItems, "Прощальный гул").select();
   private final SelectSetting.Value hlMagentaShulkerBox = new SelectSetting.Value(this.highlightItems, "Рюкзак").select();
   private final SelectSetting.Value hlCrossbow = new SelectSetting.Value(this.highlightItems, "Арбалет").select();
   private final SelectSetting.Value hlPlayerHead = new SelectSetting.Value(this.highlightItems, "Сфера").select();
   private final ColorSetting colorTotem = new ColorSetting(this, "Тотем", () -> !this.hlTotem.isSelected()).color(Colors.ACCENT);
   private final ColorSetting colorEnchantedApple = new ColorSetting(this, "Зачарованное яблоко", () -> !this.hlEnchantedApple.isSelected())
      .color(Colors.ACCENT);
   private final ColorSetting colorGoldenApple = new ColorSetting(this, "Золотое яблоко", () -> !this.hlGoldenApple.isSelected()).color(Colors.ACCENT);
   private final ColorSetting colorGoldenCarrot = new ColorSetting(this, "Золотая морковь", () -> !this.hlGoldenCarrot.isSelected()).color(Colors.ACCENT);
   private final ColorSetting colorHealingPotion = new ColorSetting(this, "Исцел", () -> !this.hlHealingPotion.isSelected()).color(Colors.ACCENT);
   private final ColorSetting colorEnderEye = new ColorSetting(this, "Дезориентация", () -> !this.hlEnderEye.isSelected()).color(Colors.ACCENT);
   private final ColorSetting colorNetheriteScrap = new ColorSetting(this, "Трапка", () -> !this.hlNetheriteScrap.isSelected()).color(Colors.ACCENT);
   private final ColorSetting colorFireCharge = new ColorSetting(this, "Огненный смерч", () -> !this.hlFireCharge.isSelected()).color(Colors.ACCENT);
   private final ColorSetting colorNetherStar = new ColorSetting(this, "Стан", () -> !this.hlNetherStar.isSelected()).color(Colors.ACCENT);
   private final ColorSetting colorDriedKelp = new ColorSetting(this, "Пласт", () -> !this.hlDriedKelp.isSelected()).color(Colors.ACCENT);
   private final ColorSetting colorPhantomMembrane = new ColorSetting(this, "Божья аура", () -> !this.hlPhantomMembrane.isSelected()).color(Colors.ACCENT);
   private final ColorSetting colorSugar = new ColorSetting(this, "Явная пыль", () -> !this.hlSugar.isSelected()).color(Colors.ACCENT);
   private final ColorSetting colorSnowball = new ColorSetting(this, "Ком снега", () -> !this.hlSnowball.isSelected()).color(Colors.ACCENT);
   private final ColorSetting colorPoppedChorusFruit = new ColorSetting(this, "Трапка (HW)", () -> !this.hlPoppedChorusFruit.isSelected()).color(Colors.ACCENT);
   private final ColorSetting colorPrismarineShard = new ColorSetting(this, "Взрывная трапка", () -> !this.hlPrismarineShard.isSelected()).color(Colors.ACCENT);
   private final ColorSetting colorFireworkStar = new ColorSetting(this, "Прощальный гул", () -> !this.hlFireworkStar.isSelected()).color(Colors.ACCENT);
   private final ColorSetting colorMagentaShulkerBox = new ColorSetting(this, "Рюкзак", () -> !this.hlMagentaShulkerBox.isSelected()).color(Colors.ACCENT);
   private final ColorSetting colorCrossbow = new ColorSetting(this, "Арбалет", () -> !this.hlCrossbow.isSelected()).color(Colors.ACCENT);
   private final ColorSetting colorPlayerHead = new ColorSetting(this, "Сфера", () -> !this.hlPlayerHead.isSelected()).color(Colors.ACCENT);
   private final EventListener<ScreenRenderEvent> onScreenRender = event -> {
      if (mc.player != null && mc.currentScreen != null && mc.currentScreen instanceof HandledScreenAccessor screen) {
         int screenX = screen.getX();
         int screenY = screen.getY();

         for (Slot slot : mc.player.currentScreenHandler.slots) {
            if (slot.hasStack()) {
               ColorRGBA c = this.getColorForStack(slot.getStack());
               if (c != null) {
                  float x = screenX + slot.x;
                  float y = screenY + slot.y;
                  event.getContext().drawRect(x, y, 16.0F, 16.0F, c);
                  ColorRGBA borderColor = new ColorRGBA(
                     Math.min(255.0F, c.getRed() * 1.3F), Math.min(255.0F, c.getGreen() * 1.3F), Math.min(255.0F, c.getBlue() * 1.3F), c.getAlpha()
                  );
                  event.getContext().drawRoundedBorder(x, y, 16.0F, 16.0F, 0.5F, BorderRadius.all(0.0F), borderColor);
               }
            }
         }
      }
   };
   private final EventListener<PostHudRenderEvent> onHudRender = event -> {
      if (mc.player != null) {
         CustomDrawContext ctx = event.getContext();
         int w = (int)IScaledResolution.sr.getScaledWidth();
         int h = (int)IScaledResolution.sr.getScaledHeight();
         int left = (w - 182) / 2;
         int top = h - 22;

         for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (!stack.isEmpty()) {
               ColorRGBA c = this.getColorForStack(stack);
               if (c != null) {
                  float x = left + 3 + i * 20;
                  float y = top + 3;
                  ctx.drawRect(x, y, 16.0F, 16.0F, c);
                  ColorRGBA borderColor = new ColorRGBA(
                     Math.min(255.0F, c.getRed() * 1.3F), Math.min(255.0F, c.getGreen() * 1.3F), Math.min(255.0F, c.getBlue() * 1.3F), c.getAlpha()
                  );
                  ctx.drawRoundedBorder(x, y, 16.0F, 16.0F, 0.5F, BorderRadius.all(0.0F), borderColor);
               }
            }
         }
      }
   };

   private ColorRGBA getColorForStack(ItemStack stack) {
      if (stack != null && !stack.isEmpty()) {
         Item item = stack.getItem();
         ColorRGBA base;
         if (item == Items.TOTEM_OF_UNDYING && this.hlTotem.isSelected()) {
            base = this.colorTotem.getColor();
         } else if (item == Items.ENCHANTED_GOLDEN_APPLE && this.hlEnchantedApple.isSelected()) {
            base = this.colorEnchantedApple.getColor();
         } else if (item == Items.GOLDEN_APPLE && this.hlGoldenApple.isSelected()) {
            base = this.colorGoldenApple.getColor();
         } else if (item == Items.GOLDEN_CARROT && this.hlGoldenCarrot.isSelected()) {
            base = this.colorGoldenCarrot.getColor();
         } else if (this.hlHealingPotion.isSelected() && PotionUtility.hasEffect(stack, StatusEffects.INSTANT_HEALTH)) {
            base = this.colorHealingPotion.getColor();
         } else if (item == Items.ENDER_EYE && this.hlEnderEye.isSelected()) {
            base = this.colorEnderEye.getColor();
         } else if (item == Items.NETHERITE_SCRAP && this.hlNetheriteScrap.isSelected()) {
            base = this.colorNetheriteScrap.getColor();
         } else if (item == Items.FIRE_CHARGE && this.hlFireCharge.isSelected()) {
            base = this.colorFireCharge.getColor();
         } else if (item == Items.NETHER_STAR && this.hlNetherStar.isSelected()) {
            base = this.colorNetherStar.getColor();
         } else if (item == Items.DRIED_KELP && this.hlDriedKelp.isSelected()) {
            base = this.colorDriedKelp.getColor();
         } else if (item == Items.PHANTOM_MEMBRANE && this.hlPhantomMembrane.isSelected()) {
            base = this.colorPhantomMembrane.getColor();
         } else if (item == Items.SUGAR && this.hlSugar.isSelected()) {
            base = this.colorSugar.getColor();
         } else if (item == Items.SNOWBALL && this.hlSnowball.isSelected()) {
            base = this.colorSnowball.getColor();
         } else if (item == Items.POPPED_CHORUS_FRUIT && this.hlPoppedChorusFruit.isSelected()) {
            base = this.colorPoppedChorusFruit.getColor();
         } else if (item == Items.PRISMARINE_SHARD && this.hlPrismarineShard.isSelected()) {
            base = this.colorPrismarineShard.getColor();
         } else if (item == Items.FIREWORK_STAR && this.hlFireworkStar.isSelected()) {
            base = this.colorFireworkStar.getColor();
         } else if (item == Items.MAGENTA_SHULKER_BOX && this.hlMagentaShulkerBox.isSelected()) {
            base = this.colorMagentaShulkerBox.getColor();
         } else if (item == Items.CROSSBOW && this.hlCrossbow.isSelected()) {
            base = this.colorCrossbow.getColor();
         } else {
            if (item != Items.PLAYER_HEAD || !this.hlPlayerHead.isSelected()) {
               return null;
            }

            base = this.colorPlayerHead.getColor();
         }

         return base.withAlpha(140.25F);
      } else {
         return null;
      }
   }
}
