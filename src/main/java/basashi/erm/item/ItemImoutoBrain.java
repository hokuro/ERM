package basashi.erm.item;

import basashi.erm.core.ModCommon;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;

public class ItemImoutoBrain extends ItemPickaxe {
	public static final String NAME = "imoutobrain";

	public ItemImoutoBrain() {
		super(ERMItem.materialImout);
		if (!ModCommon.norelease){
			// タブに登録しない
			this.setCreativeTab(null);
		}else{
			this.setCreativeTab(CreativeTabs.tabMaterials);
		}
		// 修復不可
		this.setNoRepair();
	}

    public float getStrVsBlock(ItemStack stack, IBlockState state)
    {
        return 999.9F;
    }
}