package committee.nova.itemfx.common.mixin;

import committee.nova.itemfx.common.util.TagReference;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.minecraft.world.item.enchantment.EnchantmentHelper.getItemEnchantmentLevel;

@Mixin(EnchantmentHelper.class)
public abstract class MixinEnchantmentHelper {
    @Inject(method = "hasBindingCurse", at = @At("RETURN"), cancellable = true)
    private static void onGetBindingCurse(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(getItemEnchantmentLevel(Enchantments.BINDING_CURSE, stack) > 0 || stack.getOrCreateTag().getInt(TagReference.EFFECT_ID) >= 2);
    }
}
