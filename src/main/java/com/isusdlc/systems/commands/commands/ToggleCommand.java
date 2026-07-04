package com.isusdlc.systems.commands.commands;

import java.util.List;
import com.isusdlc.elegant;
import com.isusdlc.systems.commands.Command;
import com.isusdlc.systems.commands.CommandBuilder;
import com.isusdlc.systems.commands.ParameterBuilder;
import com.isusdlc.systems.localization.Localizator;
import com.isusdlc.systems.modules.Module;
import com.isusdlc.utility.game.MessageUtility;
import net.minecraft.text.Text;
import ru.kotopushka.compiler.sdk.annotations.Compile;

public class ToggleCommand {
   @Compile
   public Command command() {
      List<String> moduleNames = elegant.getInstance()
         .getModuleManager()
         .getModules()
         .stream()
         .filter(module -> !module.isHidden())
         .map(module -> module.getName().replace(" ", ""))
         .toList();
      return CommandBuilder.begin("toggle")
         .aliases("t")
         .desc("commands.toggle.description")
         .param("module", (ParameterBuilder<Module> p) -> p.validator(ParameterBuilder.MODULE).suggests(moduleNames))
         .handler(
            context -> {
               Module module = (Module)context.arguments().getFirst();
               module.toggle();
               MessageUtility.info(
                  Text.of(Localizator.translate("commands.toggle." + (module.isEnabled() ? "enabled" : "disabled"), module.getName()))
               );
            }
         )
         .build();
   }
}
