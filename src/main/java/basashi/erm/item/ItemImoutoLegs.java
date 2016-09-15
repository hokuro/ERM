package basashi.erm.item;

import com.google.common.collect.Multimap;

import basashi.erm.core.ModCommon;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;

public class ItemImoutoLegs extends ItemAxe {
	public static final String NAME = "imoutolegs";
	public ItemImoutoLegs() {
		super(ToolMaterial.DIAMOND);

        this.efficiencyOnProperMaterial = 4.0F;
        this.toolMaterial = ERMItem.materialImout;
        this.setMaxDamage(ERMItem.materialImout.getMaxUses());
        this.efficiencyOnProperMaterial = ERMItem.materialImout.getEfficiencyOnProperMaterial();
        this.damageVsEntity = ERMItem.materialImout.getDamageVsEntity();

		if (!ModCommon.norelease){
			// タブに登録しない
			this.setCreativeTab(null);
		}else{
			this.setCreativeTab(CreativeTabs.tabMaterials);
		}
        // 修理は禁止
        this.setNoRepair();
	}


    /**
     * Current implementations of this method in child classes do not use the entry argument beside ev. They just raise
     * the damage on the stack.
     */
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
    	// モブに攻撃しても耐久減少は1にする
        stack.damageItem(1, attacker);
        return true;
    }

    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot)
    {
        Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);
        return multimap;
    }
}
