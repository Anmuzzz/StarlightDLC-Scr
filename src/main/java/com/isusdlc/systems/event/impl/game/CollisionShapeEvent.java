package com.isusdlc.systems.event.impl.game;

import lombok.Generated;
import com.isusdlc.systems.event.EventCancellable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.block.BlockState;

public class CollisionShapeEvent extends EventCancellable {
   private final BlockState state;
   private final BlockPos pos;
   private VoxelShape shape;

   @Generated
   public CollisionShapeEvent(BlockState state, BlockPos pos, VoxelShape shape) {
      this.state = state;
      this.pos = pos;
      this.shape = shape;
   }

   @Generated
   public BlockState getState() {
      return this.state;
   }

   @Generated
   public BlockPos getPos() {
      return this.pos;
   }

   @Generated
   public VoxelShape getShape() {
      return this.shape;
   }

   @Generated
   public void setShape(VoxelShape shape) {
      this.shape = shape;
   }
}
