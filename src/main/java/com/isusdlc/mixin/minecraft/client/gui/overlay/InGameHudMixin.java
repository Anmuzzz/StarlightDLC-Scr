package com.isusdlc.mixin.minecraft.client.gui.overlay;

import com.isusdlc.elegant;
import com.isusdlc.framework.base.CustomDrawContext;
import com.isusdlc.systems.event.impl.render.HudRenderEvent;
import com.isusdlc.systems.event.impl.render.PostHudRenderEvent;
import com.isusdlc.systems.event.impl.render.PreHudRenderEvent;
import com.isusdlc.systems.modules.modules.visuals.Removals;
import com.isusdlc.ui.hud.impl.ArmorHud;
import com.isusdlc.ui.hud.impl.HotbarHud;
import com.isusdlc.utility.game.server.ServerUtility;
import com.isusdlc.utility.interfaces.IMinecraft;
import com.isusdlc.utility.render.DrawUtility;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin({InGameHud.class})
public class InGameHudMixin implements IMinecraft {
   @Shadow
   private int heldItemTooltipFade;

   @Inject(
      method = {"renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/scoreboard/ScoreboardObjective;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void renderScoreboardSidebarHook(DrawContext context, ScoreboardObjective objective, CallbackInfo ci) {
      if (objective.getDisplayName().getString().contains("Анархия") && (ServerUtility.isFT() || ServerUtility.isST())) {
         try {
            ServerUtility.ftAn = Integer.parseInt(objective.getDisplayName().getString().split("-")[1].trim());
         } catch (Exception var5) {
         }
      }

      Removals removals = elegant.getInstance().getModuleManager().getModule(Removals.class);
      if (removals.isEnabled() && removals.getScoreboard().isSelected()) {
         ci.cancel();
      }
   }

   @Inject(
      method = {"renderPortalOverlay(Lnet/minecraft/client/gui/DrawContext;F)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void renderPortalOverlayHook(DrawContext context, float nauseaStrength, CallbackInfo ci) {
      Removals removals = elegant.getInstance().getModuleManager().getModule(Removals.class);
      if (removals.isEnabled() && removals.getPortal().isSelected()) {
         ci.cancel();
      }
   }

   @ModifyArgs(
      method = {"renderMiscOverlays(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/hud/InGameHud;renderOverlay(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/util/Identifier;F)V",
         ordinal = 0
      )
   )
   private void onRenderPumpkinOverlay(Args args) {
      Removals removals = elegant.getInstance().getModuleManager().getModule(Removals.class);
      if (removals.isEnabled() && removals.getPumpkin().isSelected()) {
         args.set(2, 0.0F);
      }
   }

   @Inject(
      method = {"render(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V"},
      at = {@At("HEAD")}
   )
   public void triggerPreHudRenderEvent(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
      CustomDrawContext customDrawContext = CustomDrawContext.of(context);
      elegant.getInstance().getEventManager().triggerEvent(new PreHudRenderEvent(customDrawContext, tickCounter.getTickDelta(false)));
   }

   @Inject(
      method = {"render(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V"},
      at = {@At("RETURN")}
   )
   public void triggerPostHudRenderEvent(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
      CustomDrawContext customDrawContext = CustomDrawContext.of(context);
      elegant.getInstance().getEventManager().triggerEvent(new PostHudRenderEvent(customDrawContext, tickCounter.getTickDelta(false)));
   }

   @Inject(
      method = {"renderMainHud(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V"},
      at = {@At("HEAD")}
   )
   private void beforeRenderMainHud(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
      ArmorHud armorHud = elegant.getInstance().getHud().getElementByName("hud.armor");
      if (armorHud != null && armorHud.isShowing() && armorHud.show()) {
         this.heldItemTooltipFade = 60;
      }
   }

   @Inject(
      method = {"tick()V"},
      at = {@At("TAIL")}
   )
   private void afterTick(CallbackInfo ci) {
      ArmorHud armorHud = elegant.getInstance().getHud().getElementByName("hud.armor");
      if (armorHud != null && armorHud.isShowing() && armorHud.show() && this.heldItemTooltipFade < 60) {
         this.heldItemTooltipFade = 60;
      }
   }

   @Inject(
      method = {"renderMainHud(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V"},
      at = {@At("TAIL")}
   )
   private void triggerHudRenderEvent(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
      CustomDrawContext customDrawContext = CustomDrawContext.of(context);
      DrawUtility.blurProgram.draw();
      elegant.getInstance().getEventManager().triggerEvent(new HudRenderEvent(customDrawContext, tickCounter.getTickDelta(false)));
   }

   @Inject(
      method = {"renderHotbar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V"},
      at = {@At("HEAD")}
   )
   private void onRenderHotbar(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
   }
}
