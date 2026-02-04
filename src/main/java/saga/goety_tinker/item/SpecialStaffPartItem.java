package saga.goety_tinker.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;
import slimeknights.tconstruct.library.tools.part.ToolPartItem;

import javax.annotation.Nullable;
import java.util.List;

public class SpecialStaffPartItem extends ToolPartItem {

    public SpecialStaffPartItem(Properties properties, MaterialStatsId id) {
        super(properties, id);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, worldIn, tooltip, flag);
    }
}
