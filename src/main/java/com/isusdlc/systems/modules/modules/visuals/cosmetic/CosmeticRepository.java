package com.isusdlc.systems.modules.modules.visuals.cosmetic;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import static com.isusdlc.systems.modules.modules.visuals.cosmetic.IisusClientInfo.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public final class CosmeticRepository {
    private static final CosmeticRepository INSTANCE = new CosmeticRepository();

    private final Map<String, CosmeticModel> catalog = new LinkedHashMap<>();

    public static CosmeticRepository getInstance() {
        return INSTANCE;
    }

    public void reload() {
        catalog.clear();
        discoverBuiltIn();
        discoverUserFolder(Path.of(CONFIG_PATH_COSMETICS));
    }

    public int importFiguraAvatars() {
        int before = catalog.size();
        discoverFiguraAvatars();
        return catalog.size() - before;
    }

    public List<CosmeticModel> all() {
        return new ArrayList<>(catalog.values());
    }

    public List<CosmeticModel> byType(CosmeticType type) {
        return catalog.values().stream().filter(m -> m.getType() == type).toList();
    }

    public CosmeticModel get(String id) {
        return catalog.get(id);
    }

    private void register(CosmeticModel model) {
        if (model != null && !catalog.containsKey(model.getId())) {
            catalog.put(model.getId(), model);
        }
    }

    private static final List<String> BUNDLED_MODELS = List.of(
            "model/miku", "model/teto", "model/goose",
            "model/howlpendragon", "model/repo",
            "model/kaltist", "model/starhorm",
            "accessory/simplewings",
            "pet/allay", "pet/mothli", "pet/birb"
    );

    private void discoverBuiltIn() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc != null && mc.getResourceManager() != null) {
            try {
                mc.getResourceManager().findResources("cosmetic", id -> id.getPath().endsWith("/avatar.json"))
                        .forEach((id, resource) -> {
                            String path = id.getPath().toLowerCase();
                            if (!path.startsWith("cosmetic/") || !path.endsWith("/avatar.json")) return;
                            String resourcePath = path.substring("cosmetic/".length(), path.length() - "/avatar.json".length());
                            CosmeticType forced = typeFromResourcePath(resourcePath);
                            register(CosmeticModel.loadBuiltin(resourcePath, forced));
                        });
            } catch (Exception ignored) {
            }
        }

        for (String folder : BUNDLED_MODELS) {
            String id = CosmeticModel.builtinCatalogId(folder);
            if (!catalog.containsKey(id)) {
                CosmeticType forced = typeFromResourcePath(folder);
                register(CosmeticModel.loadBuiltin(folder, forced));
            }
        }

        registerKaltistPet();
    }

    private void registerKaltistPet() {
        String petId = "builtin:pet_kaltist";
        if (catalog.containsKey(petId)) return;
        CosmeticModel kaltist = catalog.get(CosmeticModel.builtinCatalogId("model/kaltist"));
        if (kaltist == null) {
            kaltist = CosmeticModel.loadBuiltin("model/kaltist", CosmeticType.MODEL);
        }
        if (kaltist == null) return;
        CosmeticModel pet = CosmeticModel.derivePet(kaltist, "monst3r", "pet/kaltist", "Kaltist Pet");
        if (pet != null) {
            register(pet);
        }
    }

    private static CosmeticType typeFromResourcePath(String resourcePath) {
        if (resourcePath == null) return null;
        int slash = resourcePath.indexOf('/');
        if (slash <= 0) return null;
        return CosmeticType.fromJson(resourcePath.substring(0, slash));
    }

    private void discoverUserFolder(Path root) {
        if (!Files.isDirectory(root)) {
            try {
                Files.createDirectories(root);
                for (CosmeticType type : CosmeticType.values()) {
                    Files.createDirectories(root.resolve(type.getId().toLowerCase()));
                }
            } catch (Exception ignored) {
            }
            return;
        }

        for (CosmeticType type : CosmeticType.values()) {
            Path typeDir = root.resolve(type.getId().toLowerCase());
            scanDirectory(typeDir, type, "user");
        }
        scanDirectory(root, null, "user");
    }

    private void discoverFiguraAvatars() {
        Path figura = Path.of(GAME_PATH, "figura", "avatars");
        if (!Files.isDirectory(figura)) return;

        try (Stream<Path> stream = Files.list(figura)) {
            stream.filter(Files::isDirectory).forEach(dir -> {
                String key = "figura:" + dir.getFileName().toString().toLowerCase();
                if (catalog.containsKey(key)) return;
                CosmeticModel model = CosmeticModel.loadExternal(dir.toFile(), "figura");
                if (model != null) {
                    model.setSource("Figura");
                    register(model);
                }
            });
        } catch (Exception ignored) {
        }
    }

    private void scanDirectory(Path dir, CosmeticType forcedType, String sourceTag) {
        if (!Files.isDirectory(dir)) return;
        try (Stream<Path> stream = Files.list(dir)) {
            stream.filter(Files::isDirectory).forEach(sub -> {
                File folder = sub.toFile();
                if (new File(folder, "avatar.json").exists() || hasBbmodel(folder)) {
                    CosmeticModel model = CosmeticModel.loadExternal(folder, sourceTag);
                    if (model != null) {
                        if (forcedType != null && model.getType() == CosmeticType.MODEL) {
                            model = model.withType(forcedType);
                        }
                        register(model);
                    }
                }
            });
        } catch (Exception ignored) {
        }
    }

    private static boolean hasBbmodel(File folder) {
        return new File(folder, "model.bbmodel").exists()
                || new File(folder, folder.getName() + ".bbmodel").exists();
    }

    public static void openCosmeticsFolder() {
        try {
            File dir = new File(CONFIG_PATH_COSMETICS);
            if (!dir.exists()) dir.mkdirs();
            String cmd = "explorer \"" + dir.getAbsolutePath() + "\"";
            Runtime.getRuntime().exec(cmd);
        } catch (Exception ignored) {
        }
    }
}
