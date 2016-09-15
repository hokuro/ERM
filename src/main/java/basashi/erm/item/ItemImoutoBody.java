package basashi.erm.item;

import java.util.List;

import basashi.erm.core.ModCommon;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemImoutoBody extends ItemShield{
	public static final String NAME = "imoutobody";

	public ItemImoutoBody()
	{
		super();
		if (!ModCommon.norelease){
			// タブに登録しない
			this.setCreativeTab(null);
		}else{
			this.setCreativeTab(CreativeTabs.tabMaterials);
		}
		this.setMaxDamage(021);
		this.setHasSubtypes(false);
		this.setNoRepair();
	}

	public String getItemStackDisplayName(ItemStack stack)
	{
		return I18n.translateToLocal("item.imouto.body.name");
	}

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems)
    {
    	subItems.add(new ItemStack(itemIn));
    }

	/**
	* gets the CreativeTab this item is displayed on
	*/
	@SideOnly(Side.CLIENT)
	public CreativeTabs getCreativeTab()
	{
		return CreativeTabs.tabMaterials;
	}

	/**
	* How long it takes to use or consume an item
	*/
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 999999;
	}

	/**
	* Return whether this item is repairable in an anvil.
	*/
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
	{
		return false;
	}
}
