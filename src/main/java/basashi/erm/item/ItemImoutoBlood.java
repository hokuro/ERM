package basashi.erm.item;

import java.util.ArrayList;
import java.util.List;

import basashi.erm.core.ModCommon;
import basashi.erm.util.Util;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatList;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemImoutoBlood extends ItemPotion {
	public static final String NAME = "imoutoblood";
	// ポーションエフェクト
	private static final Potion[] potionlist = new Potion[]{
		    MobEffects.moveSpeed,
		    MobEffects.moveSlowdown,
		    MobEffects.digSpeed,
		    MobEffects.digSlowdown,
		    MobEffects.damageBoost,
		    MobEffects.heal,
		    MobEffects.harm,
		    MobEffects.jump,
		    MobEffects.confusion,
		    MobEffects.regeneration,
		    MobEffects.resistance,
		    MobEffects.fireResistance,
		    MobEffects.waterBreathing,
		    MobEffects.invisibility,
		    MobEffects.blindness,
		    MobEffects.nightVision,
		    MobEffects.hunger,
		    MobEffects.weakness,
		    MobEffects.poison,
		    MobEffects.wither,
		    MobEffects.healthBoost,
		    MobEffects.absorption,
		    MobEffects.saturation,
		    MobEffects.glowing,
		    MobEffects.levitation,
		    MobEffects.luck,
		    MobEffects.unluck
	};
	// エフェクトの数の確率
	private static final int[] range = new int[]{
			300, 600, 800, 900 ,950
	};


    public ItemImoutoBlood()
    {
		if (!ModCommon.norelease){
			// タブに登録しない
			this.setCreativeTab(null);
		}else{
			this.setCreativeTab(CreativeTabs.tabMaterials);
		}
        this.setMaxStackSize(64);
        this.setHasSubtypes(false);
    }

    /**
     * gets the CreativeTab this item is displayed on
     */
    @SideOnly(Side.CLIENT)
    public CreativeTabs getCreativeTab()
    {
        return super.getCreativeTab();
    }

    /**
     * Called when the player finishes using this Item (E.g. finishes eating.). Not called when the player stops using
     * the Item before the action is complete.
     */
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
    {
        EntityPlayer entityplayer = entityLiving instanceof EntityPlayer ? (EntityPlayer)entityLiving : null;

        if (entityplayer == null || !entityplayer.capabilities.isCreativeMode)
        {
            --stack.stackSize;
        }

        if (!worldIn.isRemote)
        {
        	// 発生するエンチャントの数を決める
        	int rnd = Util.random(950);
        	int num = 0;
        	for (num = 0; num < range.length; num++ ){
        		if ( range[num] >= rnd){
        			num++;
        			break;
        		}
        	}
        	// 発生するエンチャントを決める
        	List<Potion> plist = new ArrayList<Potion>();
        	while(num != 0){
        		 int idx = Util.random(potionlist.length);
        		 if (!plist.contains(potionlist[idx])){
        			 plist.add(potionlist[idx]);
        			 num--;
        		 }
        	}

        	// エンチャント発生
        	for (Potion p : plist ){
        		entityLiving.addPotionEffect(new PotionEffect(p, 420, 999));
        	}
        }

        if (entityplayer != null)
        {
            entityplayer.addStat(StatList.func_188057_b(this));
        }

        if (entityplayer == null || !entityplayer.capabilities.isCreativeMode)
        {
            if (stack.stackSize <= 0)
            {
                return new ItemStack(Items.glass_bottle);
            }

            if (entityplayer != null)
            {
                entityplayer.inventory.addItemStackToInventory(new ItemStack(Items.glass_bottle));
            }
        }

        return stack;
    }

    public String getItemStackDisplayName(ItemStack stack)
    {
        return I18n.translateToLocal("itme.imoutoblood.name");
    }

    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack)
    {
        return true;
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems)
    {
    	subItems.add(new ItemStack(itemIn));
    }

}
