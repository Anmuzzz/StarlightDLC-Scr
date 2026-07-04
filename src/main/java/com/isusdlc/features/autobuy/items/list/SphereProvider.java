package com.isusdlc.features.autobuy.items.list;

import com.isusdlc.features.autobuy.items.AutoBuyableItem;
import com.isusdlc.features.autobuy.items.customitem.CustomItem;
import com.isusdlc.features.autobuy.items.defaultsetpricec.Defaultpricec;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;

public class SphereProvider {
   public static List<AutoBuyableItem> getSpheres() {
      ArrayList var0 = new ArrayList();
      long var1 = -6798872239657110598L;
      long var3 = -4833892245449253829L;
      UUID var5 = new UUID(var1, var3);
      NbtCompound var6 = createSphereWithAttributes(
         var5.toString(),
         "ewogICJ0aW1lc3RhbXAiIDogMTc1MDI3ODY0MTkwMCwKICAicHJvZmlsZUlkIiA6ICIxNzRjZmRiNGEzY2I0M2I1YmZjZGU0MjRjM2JiMmM2ZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJtYXJhZWwxOCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9lN2E3YWU3Y2RjZjYxNmU4YjdhNDIyMWE2MjFiMjQzNTc1M2M2MGVkNmEyNThlYTA2MGRhZTMwMDJmZmU5ZTI4IiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=",
         new SphereProvider.AttributeData("minecraft:generic.max_health", -4.0, 0, "offhand"),
         new SphereProvider.AttributeData("minecraft:generic.armor", 2.0, 0, "offhand"),
         new SphereProvider.AttributeData("minecraft:generic.attack_damage", 3.0, 0, "offhand"),
         new SphereProvider.AttributeData("minecraft:generic.movement_speed", 0.07, 1, "offhand"),
         new SphereProvider.AttributeData("minecraft:generic.attack_speed", 0.13, 1, "offhand")
      );
      var0.add(new CustomItem("[★] Сфера Хаоса", var6, Items.PLAYER_HEAD, Defaultpricec.getPrice("Сфера Хаоса"), null, null));
      long var7 = -9026076527302004012L;
      long var9 = -6423087529222398544L;
      UUID var11 = new UUID(var7, var9);
      NbtCompound var12 = createSphereWithAttributes(
         var11.toString(),
         "ewogICJ0aW1lc3RhbXAiIDogMTc1MDI3ODYwODUyOCwKICAicHJvZmlsZUlkIiA6ICJkMTQ4NjFiM2UwZmM0Njk5OTFlMTcyNTllMzdiZjZhZCIsCiAgInByb2ZpbGVOYW1lIiA6ICJyYXhpdG9jbCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS83NzFhOWE0OThiNGZhNWVjNDkzNjJmOWJjODhlZGE0ZjUyYjA0ZGU0OWQ3NWFhM2NhMzMyYTFmZWExYWEwZTU3IiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=",
         new SphereProvider.AttributeData("minecraft:generic.attack_damage", 2.0, 0, "offhand"),
         new SphereProvider.AttributeData("minecraft:generic.attack_speed", 0.15, 1, "offhand")
      );
      var0.add(new CustomItem("[★] Сфера Сатира", var12, Items.PLAYER_HEAD, Defaultpricec.getPrice("Сфера Сатира"), null, null));
      long var13 = -8146046410937254878L;
      long var15 = -5669630775673977578L;
      UUID var17 = new UUID(var13, var15);
      NbtCompound var18 = createSphereWithAttributes(
         var17.toString(),
         "ewogICJ0aW1lc3RhbXAiIDogMTc1MDM0MzgzNDkzMCwKICAicHJvZmlsZUlkIiA6ICI1MzUzNWIxN2M0ZDY0NWQ0YWUwY2U2ZjM4Zjk0NTFjYSIsCiAgInByb2ZpbGVOYW1lIiA6ICJVYml2aXMiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTQxMWFjMTczODFiOWZjZTliYWIzYzcyYWZkYjdmMTk4NTcwZGFmNDczMmJkODExZDMxYzIyN2Q4MGZhMzliMSIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9",
         new SphereProvider.AttributeData("minecraft:generic.armor", 1.0, 0, "offhand"),
         new SphereProvider.AttributeData("minecraft:generic.max_health", 4.0, 0, "offhand"),
         new SphereProvider.AttributeData("minecraft:generic.movement_speed", 0.1, 1, "offhand"),
         new SphereProvider.AttributeData("minecraft:generic.attack_speed", 0.1, 1, "offhand")
      );
      var0.add(new CustomItem("[★] Сфера Бестии", var18, Items.PLAYER_HEAD, Defaultpricec.getPrice("Сфера Бестии"), null, null));
      long var19 = 9126222445474821379L;
      long var21 = -6490033164945922889L;
      UUID var23 = new UUID(var19, var21);
      NbtCompound var24 = createSphereWithAttributes(
         var23.toString(),
         "ewogICJ0aW1lc3RhbXAiIDogMTc1MDM0Mzc3NDI1NSwKICAicHJvZmlsZUlkIiA6ICJhYWMxYjA2OWNkMjE0NWE2ODNlNzQxNzE4MDcxMGU4MiIsCiAgInByb2ZpbGVOYW1lIiA6ICJqdXNhbXUiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzE2YWRjNmJhZmNiNTdmZDcwN2RlZTdkZDZhNzM2ZmUxMjY3MTFkNTNhMWZkNmNlNzg5ZGE0MWIzYmUxM2YyYSIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9",
         new SphereProvider.AttributeData("minecraft:generic.attack_damage", 6.0, 0, "offhand"),
         new SphereProvider.AttributeData("minecraft:generic.armor", -2.0, 0, "offhand"),
         new SphereProvider.AttributeData("minecraft:generic.max_health", -2.0, 0, "offhand")
      );
      var0.add(new CustomItem("[★] Сфера Ареса", var24, Items.PLAYER_HEAD, Defaultpricec.getPrice("Сфера Ареса"), null, null));
      long var25 = -5621245920202180285L;
      long var27 = -5613754434531457129L;
      UUID var29 = new UUID(var25, var27);
      NbtCompound var30 = createSphereWithAttributes(
         var29.toString(),
         "ewogICJ0aW1lc3RhbXAiIDogMTc1MDI3ODUzMjE4MywKICAicHJvZmlsZUlkIiA6ICI1OGZmZWI5NTMxNGQ0ODcwYTQwYjVjYjQyZDRlYTU5OCIsCiAgInByb2ZpbGVOYW1lIiA6ICJTa2luREJuZXQiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2UzYzExOGQ2OTZkOTEwZTU0ZGUwMmNhNGQ4MDc1NDNmOWIxOGMwMDhjOTgzOGQyZmY2OTM3NzYyMmZiMWQzMiIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9",
         new SphereProvider.AttributeData("minecraft:generic.max_health", 4.0, 0, "offhand"),
         new SphereProvider.AttributeData("minecraft:generic.armor", 2.0, 0, "offhand")
      );
      var0.add(new CustomItem("[★] Сфера Гидры", var30, Items.PLAYER_HEAD, Defaultpricec.getPrice("Сфера Гидры"), null, null));
      long var31 = 7981920535521997300L;
      long var33 = -5781467750772262283L;
      UUID var35 = new UUID(var31, var33);
      NbtCompound var36 = createSphereWithAttributes(
         var35.toString(),
         "ewogICJ0aW1lc3RhbXAiIDogMTc1MDI3ODU4MjQ5MSwKICAicHJvZmlsZUlkIiA6ICJhZWNkODIxZTQyYzE0ZDJlOThmNTA1OTg1MWI5OWMzNyIsCiAgInByb2ZpbGVOYW1lIiA6ICJSb2RyaVgyMDc1IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2M2ODAzZTZkNTY2N2EyZDYxMDYyOGJjM2IzMmY4NjNjZGE0OTVjNDY1NjE2ZGU2NTVjYjMyOTkzM2I2MWFmNzciLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==",
         new SphereProvider.AttributeData("minecraft:generic.attack_damage", 2.0, 0, "offhand"),
         new SphereProvider.AttributeData("minecraft:generic.max_health", 2.0, 0, "offhand")
      );
      var0.add(new CustomItem("[★] Сфера Икара", var36, Items.PLAYER_HEAD, Defaultpricec.getPrice("Сфера Икара"), null, null));
      long var37 = -526715816248135208L;
      long var39 = -8741392382276611177L;
      UUID var41 = new UUID(var37, var39);
      NbtCompound var42 = createSphereWithAttributes(
         var41.toString(),
         "ewogICJ0aW1lc3RhbXAiIDogMTc1MDM0Mzg2MTE4NywKICAicHJvZmlsZUlkIiA6ICJlZGUyYzdhMGFjNjM0MTNiYjA5ZDNmMGJlZTllYzhlYyIsCiAgInByb2ZpbGVOYW1lIiA6ICJ0aGVEZXZKYWRlIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzZlNGUyZjEwNDdmM2VjNmU5ZTQ1OTE4NDczOWUzM2I3YzFmYzYzYWQ4MjAyYmRhYjlmMDI0NTA4YWRkMjNlNWIiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==",
         new SphereProvider.AttributeData("minecraft:generic.luck", 1.0, 0, "offhand"),
         new SphereProvider.AttributeData("minecraft:generic.max_health", 2.0, 0, "offhand")
      );
      var0.add(new CustomItem("[★] Сфера Эрида", var42, Items.PLAYER_HEAD, Defaultpricec.getPrice("Сфера Эрида"), null, null));
      long var43 = -5398019805405426922L;
      long var45 = -8833212323713517718L;
      UUID var47 = new UUID(var43, var45);
      NbtCompound var48 = createSphereWithAttributes(
         var47.toString(),
         "ewogICJ0aW1lc3RhbXAiIDogMTc1MDM0Mzg2MTE4NywKICAicHJvZmlsZUlkIiA6ICJlZGUyYzdhMGFjNjM0MTNiYjA5ZDNmMGJlZTllYzhlYyIsCiAgInByb2ZpbGVOYW1lIiA6ICJTcGhlcmVBdGhlbmEiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTNmOWVlZGEzYmEyM2ZlMTQyM2M0MDM2ZTdkZDBhNzQ0NjFkZmY5NmJhZGM1YjJmMmI5ZmFhN2NjMTZmMzgyZiIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9",
         new SphereProvider.AttributeData("minecraft:generic.attack_speed", 0.15, 1, "offhand"),
         new SphereProvider.AttributeData("minecraft:generic.movement_speed", 0.15, 1, "offhand"),
         new SphereProvider.AttributeData("minecraft:generic.attack_damage", 3.0, 0, "offhand"),
         new SphereProvider.AttributeData("minecraft:generic.max_health", -2.0, 0, "offhand")
      );
      var0.add(new CustomItem("[★] Сфера Афины", var48, Items.PLAYER_HEAD, Defaultpricec.getPrice("Сфера Афины"), null, null));
      return var0;
   }

   private static NbtCompound createSphereWithAttributes(String var0, String var1, SphereProvider.AttributeData... var2) {
      NbtCompound var3 = new NbtCompound();
      var3.putInt("HideFlags", 1);
      NbtCompound var4 = new NbtCompound();
      var4.putUuid("Id", UUID.fromString(var0));
      NbtCompound var5 = new NbtCompound();
      NbtList var6 = new NbtList();
      NbtCompound var7 = new NbtCompound();
      var7.putString("Value", var1);
      var6.add(var7);
      var5.put("textures", var6);
      var4.put("Properties", var5);
      var3.put("SkullOwner", var4);
      NbtList var8 = new NbtList();

      for (SphereProvider.AttributeData var12 : var2) {
         NbtCompound var13 = new NbtCompound();
         var13.putString("AttributeName", var12.attributeName);
         var13.putDouble("Amount", var12.amount);
         var13.putInt("Operation", var12.operation);
         var13.putString("Slot", var12.slot);
         var13.putString("Name", UUID.randomUUID().toString());
         var13.putIntArray(
            "UUID",
            new int[]{
               (int)(Math.random() * 2.147483647E9),
               (int)(Math.random() * 2.147483647E9),
               (int)(Math.random() * 2.147483647E9),
               (int)(Math.random() * 2.147483647E9)
            }
         );
         var8.add(var13);
      }

      var3.put("AttributeModifiers", var8);
      return var3;
   }

   private static class AttributeData {
      final String attributeName;
      final double amount;
      final int operation;
      final String slot;

      AttributeData(String var1, double var2, int var4, String var5) {
         this.attributeName = var1;
         this.amount = var2;
         this.operation = var4;
         this.slot = var5;
      }
   }
}
