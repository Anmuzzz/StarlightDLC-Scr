package com.isusdlc.mixin.minecraft.client.gui.screen;

import com.isusdlc.elegant;
import com.isusdlc.framework.base.CustomDrawContext;
import com.isusdlc.systems.event.impl.render.ScreenRenderEvent;
import com.isusdlc.systems.event.impl.window.ContainerClickEvent;
import com.isusdlc.systems.event.impl.window.ContainerReleaseEvent;
import com.isusdlc.systems.modules.modules.player.InvUtils;
import com.isusdlc.utility.interfaces.IMinecraft;
import com.isusdlc.utility.time.Timer;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({HandledScreen.class})
public abstract class HandledScreenMixin implements IMinecraft {
   @Unique
   private final Timer timer = new Timer();

   @Shadow
   protected abstract boolean isPointOverSlot(Slot var1, double var2, double var4);

   @Shadow
   protected abstract void onMouseClick(Slot var1, int var2, int var3, SlotActionType var4);

   @Inject(
      method = {"render(Lnet/minecraft/client/gui/DrawContext;IIF)V"},
      at = {@At("TAIL")}
   )
   private void onRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
      CustomDrawContext customDrawContext = CustomDrawContext.of(context);
      elegant.getInstance().getEventManager().triggerEvent(new ScreenRenderEvent(customDrawContext, delta));

      for (Slot slot : mc.player.currentScreenHandler.slots) {
         InvUtils invUtils = elegant.getInstance().getModuleManager().getModule(InvUtils.class);
         if (this.isPointOverSlot(slot, mouseX, mouseY)
            && slot.isEnabled()
            && invUtils.isEnabled()
            && invUtils.getScroller().isSelected()
            && this.timer.finished((long)invUtils.getScrollDelay().getCurrentValue())
            && InputUtil.isKeyPressed(mc.getWindow().getHandle(), 340)
            && GLFW.glfwGetMouseButton(mc.getWindow().getHandle(), 0) == 1) {
            this.onMouseClick(slot, slot.id, 0, SlotActionType.QUICK_MOVE);
            this.timer.reset();
         }
      }
   }

   @Inject(
      method = {"mouseClicked(DDI)Z"},
      at = {@At("HEAD")}
   )
   private void onMouseClick(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
      elegant.getInstance().getEventManager().triggerEvent(new ContainerClickEvent((float)mouseX, (float)mouseY, button));
   }

   @Inject(
      method = {"mouseReleased(DDI)Z"},
      at = {@At("HEAD")}
   )
   public void mouseReleased(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
      elegant.getInstance().getEventManager().triggerEvent(new ContainerReleaseEvent((float)mouseX, (float)mouseY, button));
   }
}
