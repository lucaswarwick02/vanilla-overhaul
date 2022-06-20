package lucaswarwick02.mixin;

import lucaswarwick02.VanillaOverhaulMod;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mixin(ItemStack.class)
public class UnbreakableMixin {
    @Inject(method = "damage(ILnet/minecraft/util/math/random/Random;Lnet/minecraft/server/network/ServerPlayerEntity;)Z", at = @At(value = "HEAD"))
    private void makeUnbreakable(int amount, Random random, ServerPlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        // Get the reference to this item as an ItemStack (we reference this a lot, so its worth to store)
        ItemStack item = (ItemStack) (Object) this;

        Map<Enchantment, Integer> enchantmentMap = EnchantmentHelper.get(item);
        Enchantment[] enchantsToRemove = {Enchantments.UNBREAKING, Enchantments.MENDING};

        Iterator<Map.Entry<Enchantment, Integer>> iterator = enchantmentMap.entrySet().iterator();
        while (iterator.hasNext()) {
            // Get the entry at this iteration
            Map.Entry<Enchantment, Integer> entry = iterator.next();
            if (Arrays.asList(enchantsToRemove).contains(entry.getKey())) {
                iterator.remove();
            }
        }

        // Remove previous damage, and make Unbreakable
        item.setDamage(0);
        item.getOrCreateNbt().putBoolean("Unbreakable", true);
        EnchantmentHelper.set(enchantmentMap, item);
    }
}
