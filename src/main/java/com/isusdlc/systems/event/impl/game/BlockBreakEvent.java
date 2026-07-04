package com.isusdlc.systems.event.impl.game;

import lombok.Generated;
import com.isusdlc.systems.event.EventCancellable;
import net.minecraft.util.math.BlockPos;

public class BlockBreakEvent extends EventCancellable {
   private final BlockPos blockPos;

   public BlockBreakEvent(BlockPos blockPos) {
      this.blockPos = blockPos;
   }

   @Generated
   public BlockPos getBlockPos() {
      return this.blockPos;
   }
}
