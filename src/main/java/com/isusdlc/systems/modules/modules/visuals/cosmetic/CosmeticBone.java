package com.isusdlc.systems.modules.modules.visuals.cosmetic;

import lombok.Getter;
import lombok.Setter;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CosmeticBone {
    private final String name;
    private final String uuid;
    private final Vector3f pivot;
    private final Vector3f defaultRotation;
    private final List<CosmeticCube> cubes = new ArrayList<>();
    private final List<CosmeticBone> children = new ArrayList<>();

    @Setter private Vector3f animRotation = new Vector3f(0, 0, 0);
    @Setter private Vector3f animPosition = new Vector3f(0, 0, 0);

    public CosmeticBone(String name, String uuid, Vector3f pivot, Vector3f defaultRotation) {
        this.name = name;
        this.uuid = uuid;
        this.pivot = pivot;
        this.defaultRotation = defaultRotation;
    }

    public Matrix4f buildMatrix() {
        Matrix4f mat = new Matrix4f();

        mat.translate(pivot.x / 16f, pivot.y / 16f, pivot.z / 16f);

        if (defaultRotation.x != 0 || defaultRotation.y != 0 || defaultRotation.z != 0
                || animRotation.x != 0 || animRotation.y != 0 || animRotation.z != 0) {
            float rx = (float) Math.toRadians(defaultRotation.x + animRotation.x);
            float ry = (float) Math.toRadians(defaultRotation.y + animRotation.y);
            float rz = (float) Math.toRadians(defaultRotation.z + animRotation.z);
            mat.rotateZYX(rz, ry, rx);
        }

        mat.translate(-pivot.x / 16f, -pivot.y / 16f, -pivot.z / 16f);

        if (animPosition.x != 0 || animPosition.y != 0 || animPosition.z != 0) {
            mat.translate(animPosition.x / 16f, animPosition.y / 16f, animPosition.z / 16f);
        }

        return mat;
    }

    public void resetAnim() {
        animRotation = new Vector3f(0, 0, 0);
        animPosition = new Vector3f(0, 0, 0);
        for (CosmeticBone child : children) child.resetAnim();
    }

    public CosmeticBone deepClone() {
        CosmeticBone copy = new CosmeticBone(name, uuid, new Vector3f(pivot), new Vector3f(defaultRotation));
        copy.getCubes().addAll(cubes);
        for (CosmeticBone child : children) {
            copy.getChildren().add(child.deepClone());
        }
        return copy;
    }
}
