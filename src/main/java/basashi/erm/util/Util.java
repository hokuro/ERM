package basashi.erm.util;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import basashi.erm.core.Mod_ERM;
import basashi.erm.core.log.ModLog;
import basashi.erm.entity.EntityERMBase;
import basashi.erm.entity.EntityLittleGirl;
import basashi.erm.util.Values.KIND_STATUS;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.world.World;

public final class Util {


	private static Random rand = null;
	private static String crlf = null;
	private static int previousRand = 0;

	/**
	 * 対象の文字列がnullまたは空文字か判定する
	 * @param value
	 * @return
	 */
	public static boolean StringisEmptyOrNull(String value){
		if (value == null || value.isEmpty()){
			return true;
		}
		return false;
	}

	/**
	 * 0～n-1の範囲の乱数を取り出す
	 * @param n
	 * @return
	 */
	public static int random(int n) {
		if ( rand == null){
			rand = new Random();
			rand.setSeed(System.currentTimeMillis() + Runtime.getRuntime().freeMemory());
		}

	    long adjusted_max = (((long)Integer.MAX_VALUE) + 1) - (((long)Integer.MAX_VALUE) + 1) % n;
	    int r;
	    do {
	       r = Math.abs(rand.nextInt());
	    } while (r >= adjusted_max);
	    previousRand = (int)(((double)r / adjusted_max) * n);
	    return previousRand;
	}

	/**
	 * 取り出した乱数の前回の値を取得する
	 * @return
	 */
	public static int randomPrevious(){
		return previousRand;
	}

	/**
	 * 使用環境の改行コードを取得する
	 * @return
	 */
	public static String ReturnCode(){
		if(crlf == null){
			crlf = System.getProperty("line.separator");
		}
		return crlf;
	}

	/**
	 * 指定した小数点以下を切り捨てたfloat 値を返す
	 * @param value
	 * @param scale
	 * @return
	 */
	public static float RounScale(float value, int scale){
		return new BigDecimal(value).setScale(scale,BigDecimal.ROUND_DOWN).floatValue();
	}

	public static double RounScale(double value, int scale){
		return new BigDecimal(value).setScale(scale,BigDecimal.ROUND_DOWN).doubleValue();
	}

	public static int RoundScaleToInt(float value, int scale){
		return (int)RounScale(value,scale);
	}
	public static int RoundScaleToInt(double value, int scale){
		return (int)RounScale(value,scale);
	}

	/**
	 * 指定したアイテムに設定されているポーション効果を
	 * モブに設定する
	 * @param entity
	 * @param item
	 * @param world
	 */
	public static void addPotionEffectItemStack(EntityERMBase entity, ItemStack item, World world){
		if (!world.isRemote){
			List<PotionEffect> effects = PotionUtils.getEffectsFromStack(item);
			for(Iterator it = effects.iterator(); it.hasNext();){
				entity.addPotionEffect((PotionEffect)it.next());
			}
		}
	}

	public static long getWorldTime(long worldTime){
		long allminit = worldTime * 1440/23999;
		long hour = allminit/60;
		long minute = allminit%60;
		return hour*100+minute;
	}


	/**
	 * プレーヤのインベントリからアイテムを減らす
	 */
	public static ItemStack decPlayerInventory(EntityPlayer par1EntityPlayer, int par2Index, int par3DecCount) {
		if (par1EntityPlayer == null) {
			return null;
		}

		if (par2Index == -1) {
			par2Index = par1EntityPlayer.inventory.currentItem;
		}
		ItemStack itemstack1 = par1EntityPlayer.inventory.getStackInSlot(par2Index);
		if (itemstack1 == null) {
			return null;
		}

		if (!par1EntityPlayer.capabilities.isCreativeMode) {
			// クリエイティブだと減らない
			itemstack1.stackSize -= par3DecCount;
		}

		if (itemstack1.getItem() instanceof ItemPotion) {
			if (!par1EntityPlayer.capabilities.isCreativeMode) {
				// クリエイティブだと減らない
				if(itemstack1.stackSize <= 0) {
					par1EntityPlayer.inventory.setInventorySlotContents(par1EntityPlayer.inventory.currentItem, new ItemStack(Items.glass_bottle, par3DecCount));
					return null;
				}
				par1EntityPlayer.inventory.addItemStackToInventory(new ItemStack(Items.glass_bottle, par3DecCount));
			}
		} else {
			if (itemstack1.stackSize <= 0) {
				par1EntityPlayer.inventory.setInventorySlotContents(par2Index, null);
				return null;
			}
		}

		return itemstack1;
	}




	/**********************************************************************************************/
	/** デバッグ                                                                                 **/
	/**********************************************************************************************/
	public static void ShowERMStatus(int entity){
		switch(entity){
		case 0:
			ModLog.log().debug(LogERMStatus(Mod_ERM.getEntity(EntityLittleGirl.class)));
			break;
		}
	}

	public static String LogERMStatus(EntityERMBase entity){
		StringBuilder deb = new StringBuilder();
		deb.append(Util.ReturnCode()).append("==== Debug Status").append(Util.ReturnCode());
    	deb.append("==============================================================").append(Util.ReturnCode());
    	deb.append("== Status                                                   ==").append(Util.ReturnCode());
    	deb.append("Status HP Value      :").append(Float.toString(entity.getHealth())).append(Util.ReturnCode());
		deb.append("Status Love Value    :").append(Float.toString(entity.getStatus(KIND_STATUS.STATUS_LOVE).getStatus())).append(Util.ReturnCode());
		deb.append("Status Food Value    :").append(Float.toString(entity.getStatus(KIND_STATUS.STATUS_FOOD).getStatus())).append(Util.ReturnCode());
		deb.append("Status Stamina Value :").append(Float.toString(entity.getStatus(KIND_STATUS.STATUS_STAMINA).getStatus())).append(Util.ReturnCode());
		deb.append("Status Candy Value   :").append(Float.toString(entity.getStatus(KIND_STATUS.STATUS_CANDY).getStatus())).append(Util.ReturnCode());
		deb.append("Status Talk Value    :").append(Float.toString(entity.getStatus(KIND_STATUS.STATUS_TALK).getStatus())).append(Util.ReturnCode());
		deb.append("Status Exp Value     :").append(Float.toString(entity.getStatus(KIND_STATUS.STATUS_EXP).getStatus())).append(Util.ReturnCode());
    	deb.append("==============================================================").append(Util.ReturnCode());
		deb.append(entity.settingVoice().debugLog());
    	deb.append(entity.getTickCounter().debugCounter());
		deb.append(entity.settingAI().debugLog());
		deb.append(entity.CandyMemory().debugLog());
		return deb.toString();
	}

}
