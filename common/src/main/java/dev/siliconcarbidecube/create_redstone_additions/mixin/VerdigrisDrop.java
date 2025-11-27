package dev.siliconcarbidecube.create_redstone_additions.mixin;

import dev.architectury.platform.Platform;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import dev.siliconcarbidecube.create_redstone_additions.init.ModItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AxeItem.class)
public class VerdigrisDrop {
    @Inject(
            method = "useOn",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;levelEvent(Lnet/minecraft/world/entity/player/Player;ILnet/minecraft/core/BlockPos;I)V"
            )
    )

    private void inject_VerdigrisDrop(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);

        if (WeatheringCopper.getPrevious(state).isPresent() && !level.isClientSide) {
            if (!Platform.isModLoaded("copperative")) {
                int count = 3 + level.random.nextInt(4);
                ItemStack drop = new ItemStack(ModItems.VERDIGRIS.get(), count);
                Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), drop);
            }
        }
    }

}
