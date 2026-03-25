package saga.goety_tinker.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;
import slimeknights.tconstruct.library.tools.part.MaterialItem;
import slimeknights.tconstruct.library.tools.part.ToolPartItem;

import javax.annotation.Nullable;
import java.util.List;

public class SpecialStaffPartItem extends ToolPartItem {

    public SpecialStaffPartItem(Properties properties, MaterialStatsId id) {
        super(properties, id);
    }

    // TiCのパーツ名（素材名 + パーツ名）を正しく表示させるためのオーバーライド
    @Override
    public Component getName(ItemStack stack) {
        return MaterialItem.getName(this, stack);
    }

    // 素材情報をツールチップに表示し、TiCのシステムに素材を認識させる
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flag) {
        // MaterialItemの静的メソッドを呼ぶことで、素材IDの表示などを確実に行う
        MaterialItem.appendHoverText(this, stack, tooltip, flag);
        super.appendHoverText(stack, worldIn, tooltip, flag);
    }

    // アイテムがロードされた時にNBT（素材情報）が正しいか検証する
    @Override
    public void verifyTagAfterLoad(net.minecraft.nbt.CompoundTag nbt) {
        MaterialItem.verifyTag(nbt);
    }
}