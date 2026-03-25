package saga.goety_tinker.mixin;

import com.Polarice3.Goety.common.items.ModItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import saga.goety_tinker.item.SpecialStaffItem;
import saga.goety_tinker.modifier.NecromancyModifier;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Inject(method = "is(Lnet/minecraft/world/item/Item;)Z", at = @At("HEAD"), cancellable = true)
    private void goety_tinker$fakeNamelessStaff(Item item, CallbackInfoReturnable<Boolean> cir) {
        ItemStack stack = (ItemStack) (Object) this;
        // Goetyの「名もなき杖」チェックを、TiC杖（レベル2）でパスさせる
        if (item == ModItems.NAMELESS_STAFF.get() && stack.getItem() instanceof SpecialStaffItem) {
            if (NecromancyModifier.getNecroLevel(stack) >= 2) {
                cir.setReturnValue(true);
            }
        }
    }
}