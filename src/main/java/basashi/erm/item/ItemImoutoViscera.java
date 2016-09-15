package basashi.erm.item;

import com.google.common.collect.Multimap;

import basashi.erm.core.ModCommon;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemImoutoViscera extends ItemSword {
	public static final String NAME = "imoutoviscear";

	public ItemImoutoViscera() {
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

    /**
     * Called when a Block is destroyed using this Item. Return true to trigger the "Use Item" statistic.
     */
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState blockIn, BlockPos pos, EntityLivingBase entityLiving)
    {
    	// ブロック破壊時の耐久減少を1にする
        if ((double)blockIn.getBlockHardness(worldIn, pos) != 0.0D)
        {
            stack.damageItem(1, entityLiving);
        }

        return true;
    }

    /** 収穫できるブロックはなし */
    public boolean canHarvestBlock(IBlockState blockIn) {return false;}


    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot)
    {
        Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);

        if (equipmentSlot == EntityEquipmentSlot.MAINHAND)
        {
//            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getAttributeUnlocalizedName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", this.DAMAGE, 0));
//            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getAttributeUnlocalizedName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.4000000953674316D, 0));
        }

        return multimap;
    }
}
