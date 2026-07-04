package com.isusdlc.systems.commands.commands;

import java.util.Map.Entry;
import com.isusdlc.elegant;
import com.isusdlc.framework.msdf.Fonts;
import com.isusdlc.framework.objects.BorderRadius;
import com.isusdlc.systems.commands.Command;
import com.isusdlc.systems.commands.CommandBuilder;
import com.isusdlc.systems.commands.ParameterBuilder;
import com.isusdlc.systems.commands.CommandContext;
import com.isusdlc.systems.commands.ValidationResult;
import com.isusdlc.systems.event.EventListener;
import com.isusdlc.systems.event.impl.render.PreHudRenderEvent;
import com.isusdlc.systems.waypoints.WayPointsManager;
import com.isusdlc.utility.colors.ColorRGBA;
import com.isusdlc.utility.game.MessageUtility;
import com.isusdlc.utility.interfaces.IMinecraft;
import com.isusdlc.utility.interfaces.IScaledResolution;
import com.isusdlc.utility.render.Utils;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.util.math.MatrixStack;

public class WaypointsCommand implements IMinecraft, IScaledResolution {
   private final EventListener<PreHudRenderEvent> onHudRenderEvent = event -> {
      MatrixStack matrices = event.getContext().getMatrices();
      this.renderWaypoints(event, matrices);
   };

   public WaypointsCommand() {
      elegant.getInstance().getEventManager().subscribe(this);
   }

   public Command command() {
      return CommandBuilder.begin("waypoint")
         .aliases("way")
         .desc("Метки")
         .param("action", (ParameterBuilder<String> p) -> p.literal("add", "del", "clear"))
         .param("name", (ParameterBuilder<String> p) -> p.optional().validator(ValidationResult::ok))
         .param("x", (ParameterBuilder<String> p) -> p.optional().validator(this::verifyCoordinate))
         .param("y", (ParameterBuilder<String> p) -> p.optional().validator(this::verifyCoordinate))
         .param("z", (ParameterBuilder<String> p) -> p.optional().validator(this::verifyCoordinate))
         .handler(this::handle)
         .build();
   }

   private ValidationResult verifyCoordinate(String input) {
      try {
         Integer.parseInt(input);
         return ValidationResult.ok(input);
      } catch (NumberFormatException var3) {
         return ValidationResult.error("Не правильное число");
      }
   }

   private void handle(CommandContext ctx) {
      String action = (String)ctx.arguments().get(0);
      String name = (String)ctx.arguments().get(1);
      String x = (String)ctx.arguments().get(2);
      String y = (String)ctx.arguments().get(3);
      String z = (String)ctx.arguments().get(4);
      WayPointsManager wayPointsManager = elegant.getInstance().getWayPointsManager();
      String var8 = action.toLowerCase();
      switch (var8) {
         case "add":
            if (name == null || x == null || y == null || z == null) {
               MessageUtility.error(Text.of("Укажите название и координаты (.way add \"Название\" x y z)"));
               return;
            }

            try {
               wayPointsManager.add(name, Integer.parseInt(x), Integer.parseInt(y), Integer.parseInt(z));
            } catch (NumberFormatException var12) {
               MessageUtility.error(Text.of("Координаты должны быть числами"));
            }
            break;
         case "del":
            if (name == null) {
               MessageUtility.error(Text.of("Укажите название (.way del \"Название\")"));
               return;
            }

            wayPointsManager.del(name);
            break;
         case "clear":
            wayPointsManager.clear();
      }
   }

   private void renderWaypoints(PreHudRenderEvent event, MatrixStack matrices) {
      for (Entry<String, Vec3d> entry : elegant.getInstance().getWayPointsManager().getEntries()) {
         String name = entry.getKey();
         Vec3d pos = entry.getValue();
         Vec3d renderPos = pos.add(0.0, 0.5, 0.0);
         Vec2f screenPos = Utils.worldToScreen(renderPos);
         if (screenPos != null) {
            float distance = (float)mc.player.getPos().distanceTo(pos);
            float scale = MathHelper.clamp(1.0F - distance / 20.0F, 0.5F, 1.0F);
            matrices.push();
            matrices.translate(screenPos.x, screenPos.y, 0.0F);
            matrices.scale(scale, scale, 1.0F);
            String text = name + " (" + String.format("%.1f", distance) + "m)";
            int width = (int)Fonts.MEDIUM.getFont(11.0F).width(text);
            int x = -width / 2;
            event.getContext()
               .drawRoundedRect(
                  (float)(x - 3),
                  2.0F,
                  (float)(width + 8),
                  Fonts.MEDIUM.getFont(11.0F).height() + 6.0F,
                  BorderRadius.all(3.0F),
                  new ColorRGBA(0.0F, 0.0F, 0.0F, 100.0F)
               );
            event.getContext().drawText(Fonts.MEDIUM.getFont(11.0F), text, x, 5.0F, ColorRGBA.WHITE);
            matrices.pop();
         }
      }
   }
}
