package basashi.erm.item;

import java.util.List;

import basashi.erm.core.ModCommon;
import basashi.erm.core.log.ModLog;
import basashi.erm.entity.EntityERMBase;
import basashi.erm.util.Values;
import basashi.erm.util.Values.KIND_STATUS;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * キャンディ
 * @author as
 *
 */
public class ItemCandy extends ItemFood{
	public static final String NAME = "candy";
	public static final String CANDYNAME_NORMAL  = "normal";
	public static final String CANDYNAME_APPLE   = "apple";
	public static final String CANDYNAME_WMELLON = "watermellon";
	public static final String CANDYNAME_CHOCO   = "choco";
	public static final String CANDYNAME_PUMPKIN = "pumpkin";
	public static final String CANDYNAME_SQUID   = "squid";
	public static final String CANDYNAME_CALLOT  = "callot";
	public static final String CANDYNAME_CACTUS  = "cactus";

	/**
	 * キャンディリスト
	 */
	public static final Candy[] candys = new Candy[]{
			new Candy(CANDYNAME_NORMAL,  1),
			new Candy(CANDYNAME_APPLE,   4),
			new Candy(CANDYNAME_WMELLON, 4),
			new Candy(CANDYNAME_CHOCO,   4),
			new Candy(CANDYNAME_PUMPKIN, 4),
			new Candy(CANDYNAME_SQUID,   4),
			new Candy(CANDYNAME_CALLOT,  4),
			new Candy(CANDYNAME_CACTUS,  4)
	};

    /** represents the potion effect that will occurr upon eating this food. Set by setPotionEffect */
    private PotionEffect potionId;
    /** probably of the set potion effect occurring */
    private float potionEffectProbability;

	public ItemCandy(int amount, float saturation, boolean isWolfFood) {
		super(amount, saturation, false);
		this.setHasSubtypes(true);
		this.setCreativeTab(CreativeTabs.tabFood);
	}

	/**
	 * キャンディによる好意の上昇値を取得する
	 * @param favo 好み
	 * @param stack アイテムスタック
	 * @return 上昇値
	 */
	public float upLove(Values.KIND_CANDYTAST favo, ItemStack stack){
		try{
			return candys[stack.getItemDamage()].love(favo);
		}catch(Exception ex){
			if (ModCommon.isDebug) ModLog.log().debug("D LOG Out of Range Candy");
			return 0;
		}
	}

	/**
	 * キャンディの名前を取得
	 * @return
	 */
	public String getCandyName(ItemStack stack) {
		try{
			return candys[stack.getItemDamage()].CandyName();
		}catch(Exception ex){
			if (ModCommon.isDebug) ModLog.log().debug("D LOG unkown candy");
			return CANDYNAME_NORMAL;
		}
	}


    @Override
    public String getItemStackDisplayName(ItemStack par1ItemStack)
    {
    	return I18n.translateToLocal("item.candy." + candys[par1ItemStack.getItemDamage()].CandyName() + ".name");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems)
    {
        for(int i = 0; i < candys.length; ++i) {
			ItemStack itemstack = new ItemStack(itemIn, 1,i);
            subItems.add(itemstack);
        }
    }

    public void eatCandy(EntityERMBase entity, ItemStack stack){
    	// 空腹回復
    	entity.getStatus(KIND_STATUS.STATUS_FOOD).addStatus(this.getHealAmount(stack));
        if (!Minecraft.getMinecraft().theWorld.isRemote && this.potionId != null
        		&& Minecraft.getMinecraft().theWorld.rand.nextFloat() < this.potionEffectProbability)
        {
            entity.addPotionEffect(new PotionEffect(this.potionId));
        }
    }


    @Override
    public ItemFood setPotionEffect(PotionEffect p_185070_1_, float p_185070_2_)
    {
        this.potionId = p_185070_1_;
        this.potionEffectProbability = p_185070_2_;
        return super.setPotionEffect(p_185070_1_, p_185070_2_);
    }

	/**
	 * キャンディ情報
	 * @author as
	 *
	 */
	public static class Candy{
		private String taste;
		private float value;

		/**
		 * コンストラクタ
		 * @param taste 味
		 * @param value 回復値
		 */
		public Candy(String taste, int value){
			this.taste = taste;
			this.value = value;
		}

		/**
		 * キャンディによる好意の上昇値を取得する
		 * @param favo 好み
		 * @return 上昇値
		 */
		public float love(Values.KIND_CANDYTAST favo){
			float m = 1.0F;
			switch(favo){
			case LOVE:
				m = 1.5F;
				break;
			case YUMMY:
				m = 1.2F;
				break;
			case GOOD:
				m = 1.0F;
				break;
			case BAD:
				m= 0.8F;
				break;
			}
			return this.value * m;
		}

		public String CandyName(){
			return this.taste;
		}

		public float NoWeightValue(){
			return value;
		}
	}
}
