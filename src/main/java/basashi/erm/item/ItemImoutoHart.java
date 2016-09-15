package basashi.erm.item;

import basashi.erm.core.ModCommon;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemImoutoHart extends ItemFood {
	public static final String NAME = "imoutohart";

	public ItemImoutoHart() {
    	super(40,40,false);
		if (!ModCommon.norelease){
			// タブに登録しない
			this.setCreativeTab(null);
		}else{
			this.setCreativeTab(CreativeTabs.tabMaterials);
		}
	}

	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player)
	{
		player.addPotionEffect(new PotionEffect(MobEffects.confusion,14400,999));
		player.addPotionEffect(new PotionEffect(MobEffects.absorption,0,999));
		player.addPotionEffect(new PotionEffect(MobEffects.heal,0,999));
	}

}
