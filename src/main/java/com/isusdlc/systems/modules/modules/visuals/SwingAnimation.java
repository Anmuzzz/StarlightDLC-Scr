package com.isusdlc.systems.modules.modules.visuals;

import com.isusdlc.elegant;
import com.isusdlc.systems.event.EventListener;
import com.isusdlc.systems.event.impl.render.HandRenderEvent;
import com.isusdlc.systems.modules.api.ModuleCategory;
import com.isusdlc.systems.modules.api.ModuleInfo;
import com.isusdlc.systems.modules.constructions.swinganim.SwingAnimScreen;
import com.isusdlc.systems.modules.constructions.swinganim.SwingTransformations;
import com.isusdlc.systems.modules.impl.BaseModule;
import com.isusdlc.systems.setting.settings.ButtonSetting;
import net.minecraft.util.Arm;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.consume.UseAction;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Quaternionf;

@ModuleInfo(
   name = "Swing Animation",
   category = ModuleCategory.VISUALS,
   desc = "Изменяет анимации рук при взмахе"
)
public class SwingAnimation extends BaseModule {
   private final ButtonSetting button = new ButtonSetting(this, "swing.open_menu").action(() -> mc.setScreen(new SwingAnimScreen()));
   private final EventListener<HandRenderEvent> onHandRender = event -> {
      if (this.isEnabled()) {
         if (event.getArm() == Arm.RIGHT) {
            ItemStack itemStack = event.getItemStack();
            if (!this.shouldApplyAnimation(itemStack)) {
               return;
            }

            MatrixStack matrices = event.getMatrices();
            float swingProgress = event.getSwingProgress();
            SwingTransformations trans = elegant.getInstance().getSwingManager().transformations(swingProgress);
            matrices.translate(trans.getAnchorX(), trans.getAnchorY(), trans.getAnchorZ());
            matrices.translate(trans.getMoveX(), trans.getMoveY(), trans.getMoveZ());
            matrices.multiply(
               new Quaternionf()
                  .rotationXYZ((float)Math.toRadians(trans.getRotateX()), (float)Math.toRadians(trans.getRotateY()), (float)Math.toRadians(trans.getRotateZ()))
            );
            matrices.translate(-trans.getAnchorX(), -trans.getAnchorY(), -trans.getAnchorZ());
            event.cancel();
         }
      }
   };

   public boolean shouldApplyAnimation(ItemStack itemStack) {
      Item item = itemStack.getItem();
      return item != Items.AIR
         && item != Items.FILLED_MAP
         && item != Items.CROSSBOW
         && item != Items.BOW
         && item != Items.TRIDENT
         && item.getUseAction(itemStack) != UseAction.DRINK
         && item.getUseAction(itemStack) != UseAction.EAT;
   }
}
