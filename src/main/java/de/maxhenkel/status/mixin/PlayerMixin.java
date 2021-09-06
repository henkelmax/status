package de.maxhenkel.status.mixin;

import com.mojang.datafixers.util.Either;
import de.maxhenkel.status.events.PlayerEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(at = @At("HEAD"), method = "startSleepInBed")
    public void startSleepInBed(BlockPos pos, CallbackInfoReturnable<Either<Player.BedSleepingProblem, Unit>> info) {
        LivingEntity thisPlayer = this;
        if (thisPlayer instanceof ServerPlayer player) {
            PlayerEvents.PLAYER_SLEEP.invoker().accept(player);
        }
    }

}
