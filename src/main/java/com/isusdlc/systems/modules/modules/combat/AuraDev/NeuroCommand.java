package com.isusdlc.systems.modules.modules.combat.AuraDev;

import com.isusdlc.elegant;
import com.isusdlc.systems.commands.Command;
import com.isusdlc.systems.commands.CommandBuilder;
import com.isusdlc.systems.commands.CommandContext;
import com.isusdlc.systems.commands.ParameterBuilder;
import com.isusdlc.systems.modules.modules.combat.KillAura;
import com.isusdlc.utility.game.MessageUtility;
import com.isusdlc.utility.interfaces.IMinecraft;
import net.minecraft.text.Text;
import ru.kotopushka.compiler.sdk.annotations.Compile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class NeuroCommand implements IMinecraft {

    @Compile
    public Command command() {
        return CommandBuilder.begin("neuro")
                .aliases("нейро", "aura-neuro", "neurosystem")
                .desc("Управление нейро-системой ауры")
                .param("action", p -> p.literal("record", "stop", "play", "clear", "status"))
                .param("name", p -> p.optional()
                        .suggests(getPatternNames()))
                .handler(this::handle)
                .build();
    }

    @Compile
    private void handle(CommandContext ctx) {
        KillAura aura = elegant.getInstance().getModuleManager().getModule(KillAura.class);
        if (aura == null) {
            MessageUtility.error(Text.of("§c[Neuro] §fМодуль Kill Aura не найден!"));
            return;
        }

        NeuroAuraSystem neuro = aura.neuroSystem;

        String action = (String) ctx.arguments().getFirst();
        String name = ctx.arguments().size() > 1 ? (String) ctx.arguments().get(1) : null;

        switch (action.toLowerCase()) {
            case "record":
                handleRecord(neuro, aura);
                break;
            case "stop":
                handleStop(neuro, aura, name);
                break;
            case "play":
                handlePlay(neuro, aura, name);
                break;
            case "clear":
                handleClear(neuro);
                break;
            case "status":
                handleStatus(neuro);
                break;
        }
    }

    private void handleRecord(NeuroAuraSystem neuro, KillAura aura) {
        neuro.startRecording();
        neuro.setUsingNeuro(false);

        MessageUtility.info(Text.of("§a[Neuro] §fЗапись начата"));
    }

    private void handleStop(NeuroAuraSystem neuro, KillAura aura, String name) {
        if (neuro.getPatternCount() == 0) {
            MessageUtility.error(Text.of("§c[Neuro] §fНет записанных паттернов"));
            return;
        }

        String saveName = (name != null && !name.isEmpty()) ? name : "neuro_" + System.currentTimeMillis();
        neuro.savePatterns(saveName);
        neuro.stopRecording();

        MessageUtility.info(Text.of("§a[Neuro] §fЗапись остановлена и сохранена как §e" + saveName));
    }

    private void handlePlay(NeuroAuraSystem neuro, KillAura aura, String name) {
        if (name == null || name.isEmpty()) {
            MessageUtility.error(Text.of("§c[Neuro] §fУкажи имя паттерна!"));
            return;
        }

        if (!aura.isEnabled()) {
            MessageUtility.error(Text.of("§c[Neuro] §fВключи Kill Aura для использования паттернов!"));
            return;
        }

        neuro.loadPatterns(name);
        neuro.setRecording(false);
        neuro.setUsingNeuro(true);

        MessageUtility.info(Text.of("§a[Neuro] §fЗагружен паттерн §e" + name + " §f(§e" + neuro.getPatternCount() + "§f паттернов)"));
        MessageUtility.info(Text.of("§7Не забудь выбрать режим поворотов 'Neuro' в настройках Kill Aura"));
    }

    private void handleClear(NeuroAuraSystem neuro) {
        neuro.clearPatterns();
        MessageUtility.info(Text.of("§e[Neuro] §fВсе паттерны очищены"));
    }

    private void handleStatus(NeuroAuraSystem neuro) {
        String status = neuro.getStatusString();
        status = status.replace("§8[§bNeuro§8] §f", "");
        MessageUtility.info(Text.of("§8[§bNeuro§8] §f" + status));

        if (neuro.getPatternCount() > 0) {
            MessageUtility.info(Text.of("§7Новых в сессии: §f" + neuro.getRecordedThisSession()));
        }
    }

    private List<String> getPatternNames() {
        List<String> patterns = new ArrayList<>();
        File dir = new File("neuro_patterns");
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles((d, name) -> name.endsWith(".neuro"));
            if (files != null) {
                for (File file : files) {
                    String name = file.getName();
                    patterns.add(name.substring(0, name.length() - 6));
                }
            }
        }
        return patterns;
    }
}
