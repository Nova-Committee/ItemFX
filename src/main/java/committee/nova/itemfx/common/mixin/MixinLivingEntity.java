package committee.nova.itemfx.common.mixin;

import committee.nova.itemfx.common.util.TagReference;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {
    @Inject(method = "addEatEffect", at = @At("HEAD"))
    public void onAddEatEffect(ItemStack stack, Level level, LivingEntity entity, CallbackInfo callbackInfo) {
        if (stack.getItem().isEdible()) {
            final int effect = stack.getOrCreateTag().getInt(TagReference.EFFECT_ID);
            final Random random = entity.getRandom();
            switch (effect) {
                case 2, 3 -> {
                    entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 400 * effect, 0, false, false, true, null));
                    entity.addEffect(new MobEffectInstance(MobEffects.HUNGER, 100 * effect, effect - 2, false, false, true, null));
                }
                case 1 -> {
                    if (random.nextFloat() > 0.5F)
                        entity.addEffect(new MobEffectInstance(MobEffects.LUCK, 300, 0, false, false, true, null));
                    if (random.nextFloat() > 0.9F)
                        entity.addEffect(new MobEffectInstance(MobEffects.HEALTH_BOOST, 100, 0, false, false, true, null));
                }
            }
        }
    }
}
