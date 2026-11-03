package com.possessedgrin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

/**
 * The possessed skin: a sickly, pale wash over the whole body, crossed by dark veins and cracks, like the makeup in
 * the film. It's made on the fly from each mob's own texture (only where that texture has skin), so it fits every
 * mob, including modded ones and resource packs, and is drawn see-through on top of the normal skin.
 */
public final class PossessedSkins {
    private static final int WASH_ALPHA = 0x46;
    private static final int[] WASH = {214, 224, 196};          // pale, faintly green
    private static final int[] VEIN = {92, 18, 46};             // dark purple-red
    private static final int[] VEIN_CORE = {54, 6, 18};

    /** Render thread only. Empty when a texture can't be read. */
    private static final Map<Identifier, Optional<Identifier>> SKINS = new HashMap<>();

    private PossessedSkins() {
    }

    @Nullable
    public static Identifier skinFor(Identifier baseTexture) {
        return SKINS.computeIfAbsent(baseTexture, PossessedSkins::create).orElse(null);
    }

    private static Optional<Identifier> create(Identifier baseTexture) {
        MinecraftClient client = MinecraftClient.getInstance();
        Optional<Resource> resource = client.getResourceManager().getResource(baseTexture);
        if (resource.isEmpty()) {
            return Optional.empty();
        }
        try (InputStream in = resource.get().getInputStream(); NativeImage source = NativeImage.read(in)) {
            int width = source.getWidth();
            int height = source.getHeight();
            NativeImage skin = new NativeImage(width, height, true);

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    skin.setColor(x, y, hasSkin(source, x, y) ? abgr(WASH_ALPHA, WASH) : 0);
                }
            }

            // Veins and cracks: short wandering lines, the same pattern every time for a given mob
            Random random = new Random(baseTexture.toString().hashCode());
            int veins = Math.max(8, width * height / 110);
            for (int i = 0; i < veins; i++) {
                int x = random.nextInt(width);
                int y = random.nextInt(height);
                int dx = random.nextInt(3) - 1;
                int dy = dx == 0 ? (random.nextBoolean() ? 1 : -1) : random.nextInt(3) - 1;
                int length = 3 + random.nextInt(7);
                for (int step = 0; step < length && x >= 0 && y >= 0 && x < width && y < height; step++) {
                    if (hasSkin(source, x, y)) {
                        skin.setColor(x, y, step == 0 ? abgr(210, VEIN_CORE) : abgr(170, VEIN));
                    }
                    if (random.nextInt(3) == 0) {
                        dx = Math.max(-1, Math.min(1, dx + random.nextInt(3) - 1));
                        if (dx == 0 && dy == 0) {
                            dy = 1;
                        }
                    }
                    x += dx;
                    y += dy;
                }
            }

            Identifier id = client.getTextureManager().registerDynamicTexture("possessedgrin_skin", new NativeImageBackedTexture(skin));
            return Optional.of(id);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private static boolean hasSkin(NativeImage image, int x, int y) {
        return ((image.getColor(x, y) >>> 24) & 0xFF) >= 16;
    }

    /** NativeImage stores colors as ABGR. */
    private static int abgr(int alpha, int[] rgb) {
        return (alpha << 24) | (rgb[2] << 16) | (rgb[1] << 8) | rgb[0];
    }
}
