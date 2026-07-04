package com.isusdlc.systems.modules.modules.player;
import com.isusdlc.systems.event.EventListener;

import com.isusdlc.systems.event.EventListener;
import com.isusdlc.systems.event.impl.player.ClientPlayerTickEvent;
import com.isusdlc.systems.modules.api.ModuleCategory;
import com.isusdlc.systems.modules.api.ModuleInfo;
import com.isusdlc.systems.modules.impl.BaseModule;
import com.isusdlc.systems.setting.settings.BooleanSetting;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;

@ModuleInfo(name = "NoDelay", category = ModuleCategory.PLAYER, desc = "Убирает задержку предметам")
public class NoDelay extends BaseModule {

    private final BooleanSetting jump = new BooleanSetting(this, "Прыжок").enabled(true);
    private final BooleanSetting xp = new BooleanSetting(this, "Пузырёк опыта").enabled(true);
    private final BooleanSetting crystal = new BooleanSetting(this, "Кристаллы").enabled(true);
    private final BooleanSetting place = new BooleanSetting(this, "ПКМ").enabled(false);

    private final EventListener<ClientPlayerTickEvent> listener = event -> {
        if (check(mc.player.getMainHandStack().getItem())) {
            mc.player.getItemCooldownManager().remove(Registries.ITEM.getId(mc.player.getMainHandStack().getItem()));
        }
    };

    private boolean check(Item item) {
        return (item instanceof BlockItem && place.isEnabled())
            || (item == Items.END_CRYSTAL && crystal.isEnabled())
            || (item == Items.EXPERIENCE_BOTTLE && xp.isEnabled());
    }
}
