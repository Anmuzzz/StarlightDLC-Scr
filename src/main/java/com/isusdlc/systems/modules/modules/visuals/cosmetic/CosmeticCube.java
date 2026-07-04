package com.isusdlc.systems.modules.modules.visuals.cosmetic;

import lombok.Getter;
import org.joml.Vector3f;

@Getter
public class CosmeticCube {
    private final Vector3f from;
    private final Vector3f to;
    private final Vector3f rotation;
    private final Vector3f origin;
    private final float inflate;
    private final CubeFace[] faces;
    private final int textureWidth;
    private final int textureHeight;

    public CosmeticCube(Vector3f from, Vector3f to, Vector3f rotation, Vector3f origin,
                        float inflate, CubeFace[] faces, int textureWidth, int textureHeight) {
        this.from = from;
        this.to = to;
        this.rotation = rotation;
        this.origin = origin;
        this.inflate = inflate;
        this.faces = faces;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
    }

    @Getter
    public static class CubeFace {
        private final float u1, v1, u2, v2;
        private final int textureIndex;

        public CubeFace(float u1, float v1, float u2, float v2, int textureIndex) {
            this.u1 = u1;
            this.v1 = v1;
            this.u2 = u2;
            this.v2 = v2;
            this.textureIndex = textureIndex;
        }
    }
}
