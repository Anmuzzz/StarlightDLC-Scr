package com.isusdlc.systems.modules.modules.visuals.cosmetic;

import com.google.gson.JsonObject;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;
import static com.isusdlc.systems.modules.modules.visuals.cosmetic.IisusClientInfo.NAME;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Locale;

final class CosmeticTextureSlot {
    private final int index;
    private final Identifier id;
    private int width;
    private int height;
    private final String fileName;

    private boolean loaded;
    private JsonObject textureJson;

    CosmeticTextureSlot(int index, Identifier id, int width, int height, String fileName) {
        this.index = index;
        this.id = id;
        this.width = Math.max(width, 1);
        this.height = Math.max(height, 1);
        this.fileName = fileName;
    }

    int getIndex() {
        return index;
    }

    Identifier getId() {
        return id;
    }

    int getWidth() {
        return width;
    }

    int getHeight() {
        return height;
    }

    boolean isLoaded() {
        return loaded;
    }

    void markLoaded() {
        loaded = true;
    }

    static CosmeticTextureSlot fromJson(int index, String modelKey, JsonObject texture) {
        int w = texture.has("width") ? texture.get("width").getAsInt() : 64;
        int h = texture.has("height") ? texture.get("height").getAsInt() : 64;
        String name = texture.has("name") ? texture.get("name").getAsString() : "tex" + index;
        Identifier id = Identifier.of(
                NAME.toLowerCase(),
                "dynamic/cosmetic/" + modelKey + "/tex/" + index
        );
        CosmeticTextureSlot slot = new CosmeticTextureSlot(index, id, w, h, name);
        slot.textureJson = texture;
        return slot;
    }

    void reset() {
        loaded = false;
    }

    void load(MinecraftClient mc, File externalDir, String resourcePath) {
        if (loaded || mc == null || mc.getTextureManager() == null) return;

        try {
            if (externalDir != null) {
                if (tryFile(new File(externalDir, fileName))) return;
                if (tryFile(new File(externalDir, "outfits/" + fileName))) return;
                if (tryFile(new File(externalDir, "texture.png"))) return;
            }

            if (textureJson != null && textureJson.has("source")) {
                String source = textureJson.get("source").getAsString();
                if (source.startsWith("data:image") && source.contains(",")) {
                    byte[] bytes = Base64.getDecoder().decode(source.substring(source.indexOf(',') + 1));
                    try (InputStream is = new ByteArrayInputStream(bytes)) {
                        register(mc, is);
                    }
                }
            }

            if (!loaded && mc.getResourceManager() != null) {
                String base = "cosmetic/" + resourcePath + "/";
                String fileLower = fileName.toLowerCase(Locale.ROOT);
                String[] paths = {
                        base + fileLower,
                        base + fileName,
                        base + "outfits/" + fileLower,
                        base + "texture.png"
                };
                for (String path : paths) {
                    if (!isValidResourcePath(path)) continue;
                    Identifier rid = Identifier.of(NAME.toLowerCase(), path);
                    try (InputStream is = mc.getResourceManager().open(rid)) {
                        register(mc, is);
                        return;
                    } catch (Exception ignored) {
                    }
                }
            }
        } catch (Exception ignored) {
        }
    }

    private boolean tryFile(File file) throws IOException {
        if (!file.exists()) return false;
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc == null) return false;
        try (InputStream is = new FileInputStream(file)) {
            register(mc, is);
            return true;
        }
    }

    private static boolean isValidResourcePath(String path) {
        for (int i = 0; i < path.length(); i++) {
            char c = path.charAt(i);
            if (c >= 'A' && c <= 'Z') {
                return false;
            }
        }
        return true;
    }

    private void register(MinecraftClient mc, InputStream is) throws IOException {
        NativeImage image = NativeImage.read(is);
        width = Math.max(image.getWidth(), 1);
        height = Math.max(image.getHeight(), 1);
        NativeImageBackedTexture texture = new NativeImageBackedTexture(image);
        mc.getTextureManager().registerTexture(id, texture);
        loaded = true;
    }
}
