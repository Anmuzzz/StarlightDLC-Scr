package com.isusdlc.systems.modules.modules.other;

import com.isusdlc.systems.event.EventListener;
import com.isusdlc.systems.event.impl.player.ClientPlayerTickEvent;
import com.isusdlc.systems.modules.api.ModuleCategory;
import com.isusdlc.systems.modules.api.ModuleInfo;
import com.isusdlc.systems.modules.impl.BaseModule;
import com.isusdlc.utility.game.MessageUtility;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

@ModuleInfo(name = "AutoAppleFarm", category = ModuleCategory.OTHER, desc = "Автоматическая ферма яблок")
public class AutoAppleFarm extends BaseModule {

    private enum State {
        PLACING_SAPLING,
        GROWING_TREE,
        BREAKING_LEAVES,
        BREAKING_LOG,
        WAITING
    }

    private State currentState = State.WAITING;
    private BlockPos treePos = null;
    private boolean isBreaking = false;
    private BlockPos currentBreakingPos = null;

    private final EventListener<ClientPlayerTickEvent> onTick = event -> {
        if (mc.world == null || mc.player == null) return;

        if (!checkInventory()) {
            disable();
            return;
        }

        autoSelectTool();

        switch (currentState) {
            case PLACING_SAPLING:
                placeSapling();
                break;
            case GROWING_TREE:
                growTree();
                break;
            case BREAKING_LEAVES:
                breakLeaves();
                break;
            case BREAKING_LOG:
                breakLog();
                break;
            case WAITING:
                break;
        }
    };

    @Override
    public void onEnable() {
        currentState = State.PLACING_SAPLING;
        treePos = null;
        isBreaking = false;
        currentBreakingPos = null;
        MessageUtility.info(Text.of("AutoAppleFarm включён"));
    }

    @Override
    public void onDisable() {
        if (isBreaking) {
            mc.options.attackKey.setPressed(false);
            isBreaking = false;
            currentBreakingPos = null;
        }
        currentState = State.WAITING;
        treePos = null;
        MessageUtility.info(Text.of("AutoAppleFarm выключен"));
    }

    private void autoSelectTool() {
        if (mc.crosshairTarget instanceof BlockHitResult hitResult) {
            BlockPos targetPos = hitResult.getBlockPos();
            if (targetPos != null && mc.world != null) {
                var targetBlock = mc.world.getBlockState(targetPos).getBlock();

                if (targetBlock == Blocks.OAK_LOG || targetBlock == Blocks.BIRCH_LOG ||
                        targetBlock == Blocks.SPRUCE_LOG || targetBlock == Blocks.JUNGLE_LOG ||
                        targetBlock == Blocks.ACACIA_LOG || targetBlock == Blocks.DARK_OAK_LOG ||
                        targetBlock == Blocks.CHERRY_LOG || targetBlock == Blocks.MANGROVE_LOG) {
                    int axeSlot = findToolSlot("axe");
                    if (axeSlot != -1) {
                        mc.player.getInventory().selectedSlot = axeSlot;
                    }
                } else if (targetBlock == Blocks.OAK_LEAVES || targetBlock == Blocks.BIRCH_LEAVES ||
                        targetBlock == Blocks.SPRUCE_LEAVES || targetBlock == Blocks.JUNGLE_LEAVES ||
                        targetBlock == Blocks.ACACIA_LEAVES || targetBlock == Blocks.DARK_OAK_LEAVES ||
                        targetBlock == Blocks.CHERRY_LEAVES || targetBlock == Blocks.MANGROVE_LEAVES) {
                    int hoeSlot = findToolSlot("hoe");
                    if (hoeSlot != -1) {
                        mc.player.getInventory().selectedSlot = hoeSlot;
                    }
                }
            }
        }
    }

    private boolean checkInventory() {
        boolean hasSapling = false;
        boolean hasBoneMeal = false;
        boolean hasAxe = false;
        boolean hasHoe = false;

        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (!stack.isEmpty()) {
                if (stack.getItem() == Items.OAK_SAPLING) hasSapling = true;
                if (stack.getItem() == Items.BONE_MEAL) hasBoneMeal = true;

                String name = stack.getItem().toString().toLowerCase();
                if (name.contains("axe") && !name.contains("pick")) hasAxe = true;
                if (name.contains("hoe")) hasHoe = true;
            }
        }

        if (!hasSapling) {
            MessageUtility.warn(Text.of("Отсутствует саженец дуба"));
            return false;
        }
        if (!hasBoneMeal) {
            MessageUtility.warn(Text.of("Отсутствует костная мука"));
            return false;
        }
        if (!hasAxe) {
            MessageUtility.warn(Text.of("Отсутствует топор"));
            return false;
        }
        if (!hasHoe) {
            MessageUtility.warn(Text.of("Отсутствует мотыга"));
            return false;
        }
        return true;
    }

    private void placeSapling() {
        if (isBreaking) {
            stopBreaking();
        }

        BlockPos groundPos = findGroundPos();
        if (groundPos == null) return;

        BlockPos airPos = groundPos.up();
        if (mc.world.getBlockState(airPos).getBlock() == Blocks.OAK_LOG ||
                mc.world.getBlockState(airPos).getBlock() == Blocks.OAK_LEAVES) {
            currentState = State.BREAKING_LEAVES;
            return;
        }

        if (mc.world.getBlockState(airPos).isAir()) {
            int saplingSlot = findItemSlot(Items.OAK_SAPLING);
            if (saplingSlot != -1) {
                mc.player.getInventory().selectedSlot = saplingSlot;

                BlockHitResult hit = new BlockHitResult(
                        Vec3d.ofCenter(groundPos), Direction.UP, groundPos, false
                );
                mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, hit);
                mc.player.swingHand(Hand.MAIN_HAND);

                treePos = airPos;
                currentState = State.GROWING_TREE;
                MessageUtility.info(Text.of("[+] Саженец поставлен!"));
            }
        } else if (mc.world.getBlockState(airPos).getBlock() == Blocks.OAK_SAPLING) {
            treePos = airPos;
            currentState = State.GROWING_TREE;
        }
    }

    private void growTree() {
        if (treePos == null) {
            currentState = State.PLACING_SAPLING;
            return;
        }

        if (mc.world.getBlockState(treePos).getBlock() == Blocks.OAK_SAPLING) {
            int boneMealSlot = findItemSlot(Items.BONE_MEAL);
            if (boneMealSlot != -1) {
                mc.player.getInventory().selectedSlot = boneMealSlot;

                BlockHitResult hit = new BlockHitResult(
                        Vec3d.ofCenter(treePos), Direction.UP, treePos, true
                );
                mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, hit);
                mc.player.swingHand(Hand.MAIN_HAND);
            }
        } else {
            currentState = State.BREAKING_LEAVES;
            MessageUtility.info(Text.of("[+] Дерево выросло! Начинаем ломать..."));
        }
    }

    private void breakLeaves() {
        BlockPos leafPos = findNearestBlock(Blocks.OAK_LEAVES);
        if (leafPos != null) {
            if (currentBreakingPos != leafPos) {
                if (isBreaking) stopBreaking();
                mc.options.attackKey.setPressed(true);
                currentBreakingPos = leafPos;
                isBreaking = true;
            }
        } else {
            if (isBreaking) stopBreaking();
            currentState = State.BREAKING_LOG;
        }
    }

    private void breakLog() {
        BlockPos logPos = findNearestBlock(Blocks.OAK_LOG);
        if (logPos != null) {
            if (currentBreakingPos != logPos) {
                if (isBreaking) stopBreaking();
                mc.options.attackKey.setPressed(true);
                currentBreakingPos = logPos;
                isBreaking = true;
            }
        } else {
            if (isBreaking) stopBreaking();
            currentState = State.PLACING_SAPLING;
            treePos = null;
            MessageUtility.info(Text.of("[+] Дерево срублено! Ставим новое..."));
        }
    }

    private void stopBreaking() {
        mc.options.attackKey.setPressed(false);
        isBreaking = false;
        currentBreakingPos = null;
    }

    private BlockPos findGroundPos() {
        var facing = mc.player.getHorizontalFacing();
        var frontPos = mc.player.getBlockPos().offset(facing);

        for (int yOffset = 0; yOffset >= -2; yOffset--) {
            BlockPos checkPos = frontPos.down(Math.abs(yOffset));
            var block = mc.world.getBlockState(checkPos).getBlock();
            if (block == Blocks.GRASS_BLOCK || block == Blocks.DIRT ||
                    block == Blocks.COARSE_DIRT || block == Blocks.ROOTED_DIRT) {
                return checkPos;
            }
        }
        return null;
    }

    private BlockPos findNearestBlock(net.minecraft.block.Block block) {
        BlockPos nearest = null;
        double nearestDist = 8.0;

        int range = 7;
        for (int x = -range; x <= range; x++) {
            for (int y = -range; y <= range; y++) {
                for (int z = -range; z <= range; z++) {
                    BlockPos pos = BlockPos.ofFloored(
                            mc.player.getX() + x,
                            mc.player.getY() + y,
                            mc.player.getZ() + z
                    );
                    if (mc.world.getBlockState(pos).getBlock() == block) {
                        double dist = mc.player.getEyePos().distanceTo(Vec3d.ofCenter(pos));
                        if (dist < nearestDist && dist <= 5.0) {
                            nearestDist = dist;
                            nearest = pos;
                        }
                    }
                }
            }
        }
        return nearest;
    }

    private int findItemSlot(net.minecraft.item.Item item) {
        for (int i = 0; i < 9; i++) {
            if (mc.player.getInventory().getStack(i).getItem() == item) {
                return i;
            }
        }
        return -1;
    }

    private int findToolSlot(String tool) {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (!stack.isEmpty()) {
                String name = stack.getItem().toString().toLowerCase();
                if (tool.equals("axe") && name.contains("axe") && !name.contains("pick")) {
                    return i;
                }
                if (tool.equals("hoe") && name.contains("hoe")) {
                    return i;
                }
            }
        }
        return -1;
    }
}
