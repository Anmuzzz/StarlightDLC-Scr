package com.isusdlc.systems.commands.commands;

import java.util.List;
import com.isusdlc.elegant;
import com.isusdlc.systems.commands.Command;
import com.isusdlc.systems.commands.CommandBuilder;
import com.isusdlc.systems.commands.ParameterBuilder;
import com.isusdlc.systems.commands.CommandContext;
import com.isusdlc.systems.commands.ValidationResult;
import com.isusdlc.systems.friends.FriendManager;
import com.isusdlc.systems.localization.Localizator;
import com.isusdlc.utility.game.MessageUtility;
import net.minecraft.text.Text;
import ru.kotopushka.compiler.sdk.annotations.Compile;

public class FriendCommand {
   @Compile
   public Command command() {
      return CommandBuilder.begin(
            "friend",
            b -> b.aliases("friends")
               .desc("commands.friends.description")
               .param("action", (ParameterBuilder<String> p) -> p.literal("add", "remove", "del", "delete", "clear", "list"))
               .param("id", (ParameterBuilder<String> p) -> p.optional().validator(ValidationResult::ok))
               .handler(this::handle)
         )
         .build();
   }

   @Compile
   private void handle(CommandContext ctx) {
      String action = (String)ctx.arguments().get(0);
      String id = (String)ctx.arguments().get(1);
      FriendManager fm = elegant.getInstance().getFriendManager();
      String var5 = action.toLowerCase();
      switch (var5) {
         case "add":
            fm.add(id);
            break;
         case "remove":
         case "del":
         case "delete":
            fm.remove(id);
            break;
         case "clear":
            fm.clear();
            break;
         case "list":
            this.printList();
      }
   }

   @Compile
   private void printList() {
      List<String> friends = elegant.getInstance().getFriendManager().listFriends();
      if (friends.isEmpty()) {
         MessageUtility.info(Text.of(Localizator.translate("commands.friends.empty")));
      } else {
         MessageUtility.info(Text.of(Localizator.translate("commands.friends.list")));

         for (int i = 0; i < friends.size(); i++) {
            MessageUtility.info(Text.of("[" + (i + 1) + "] " + friends.get(i)));
         }
      }
   }
}
