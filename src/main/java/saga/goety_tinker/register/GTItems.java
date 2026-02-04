package saga.goety_tinker.register;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import saga.goety_tinker.Goety_tinker;
import saga.goety_tinker.item.SpecialStaffItem;
import saga.goety_tinker.item.SpecialStaffPartItem;
import slimeknights.mantle.registration.object.ItemObject;
import slimeknights.tconstruct.common.registration.ItemDeferredRegisterExtension;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.part.ToolPartItem;
import slimeknights.tconstruct.tools.stats.HeadMaterialStats;
import slimeknights.tconstruct.tools.stats.HandleMaterialStats;
import slimeknights.tconstruct.tools.stats.StatlessMaterialStats;

public class GTItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Goety_tinker.MODID);

    public static final ItemDeferredRegisterExtension TINKER_ITEMS = new ItemDeferredRegisterExtension(Goety_tinker.MODID);

    public static final ItemObject<ToolPartItem> STAFF_HANDLE = TINKER_ITEMS.register("staff_handle",
            () -> new SpecialStaffPartItem(new Item.Properties(), HandleMaterialStats.ID));

    public static final ItemObject<ToolPartItem> STAFF_HEAD = TINKER_ITEMS.register("staff_head",
            () -> new SpecialStaffPartItem(new Item.Properties(), HeadMaterialStats.ID));

    public static final ItemObject<ToolPartItem> STAFF_CORE = TINKER_ITEMS.register("staff_core",
            () -> new SpecialStaffPartItem(new Item.Properties(), StatlessMaterialStats.BINDING.getIdentifier()));
    public static final ItemObject<SpecialStaffItem> SPECIAL_STAFF = TINKER_ITEMS.register("special_staff",
            () -> new SpecialStaffItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE),
                    ToolDefinition.create(Goety_tinker.resource("special_staff"))));
}