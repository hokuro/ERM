package basashi.erm.entity;

import java.io.File;
import java.util.Map;

import basashi.erm.core.Mod_ERM;
import basashi.erm.core.log.ModLog;
import basashi.erm.event.EventHook;
import basashi.erm.resource.ERMResourceManager;
import basashi.erm.resource.parts.ERMDressParts;
import basashi.erm.util.Util;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityLittleGirl extends EntityERMBase{
	// 登録名
	public static final String NAME = "LittleGirl";
	// ドレステクスチャファイル名
	public static final String textureDressName = "LittleGirl.png";
	public static final String textureDressText = "LittleGirl.tex";
	// アーマーテクスチャファイル名
	public static final String textureArmorName = "LittleGirlArmor.png";
	public static final String textureArmorText = "LittleGirlAromor.tex";

	public EntityLittleGirl(World worldIn) {
		super(worldIn);
		// テクスチャアップデートを無効にしておく
		this.updateDress = false;
		this.updateArmor = false;
		// TODO 自動生成されたコンストラクター・スタブ
		this.tasks.addTask(1,  new EntityAIWander(this,1.0));
		this.tasks.addTask(2,  new EntityAILookIdle(this));
	}

	// エンティティの初期化
	@Override
	public void entityInit(){
		super.entityInit();

		// テクスチャの確認
		File texd = new File(EventHook.textureDir,textureDressName);
		File texdtxt = new File(EventHook.textureDir,textureDressText);

		File texa = new File(EventHook.textureDir,textureArmorName);
		File texatxt = new File(EventHook.textureDir,textureArmorText);
		if (ERMResourceManager.instance().initEntityTexture(this, texd, texdtxt, texa, texatxt)){
			// テクスチャを更新するように指示を出す
			this.updateDress = true;
		}else{
			// デフォルトテクスチャを使用するように指定
			this.updateDress = false;
			this.setTextureDress("");
		}
	}

	@Override
	public EntityAgeable createChild(EntityAgeable ageable) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public void onEntityUpdate(){
		super.onEntityUpdate();
	}

	// 死亡時
	@Override
    protected void onDeathUpdate(){
		// インベントリの中身をすべて捨てて

		// 体力を全開にして
		this.setHealth(20.0f);
		// リスポーン地点にテレポート
		EntityPlayer pl = (EntityPlayer)this.getOwner();
		if ( pl != null){
			BlockPos pos = pl.worldObj.getSpawnPoint();
			this.setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(), 0.0f, 0.0f);
		}
	}

    public float getEyeHeight()
    {
        float f = 1.74F;

        if (this.isChild())
        {
            f = (float)((double)f - 0.81D);
        }

        return f;
    }


    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        super.writeEntityToNBT(tagCompound);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound tagCompund)
    {
        super.readEntityFromNBT(tagCompund);
        Mod_ERM.setEntityInstance(this);
    }

    public void setPosition(double x, double y, double z){
    	super.setPosition(x, y, z);
		ModLog.log().debug(this.posX+","+this.posY+","+this.posZ);
    }


	// 登録名を取り出す
	@Override
	public String REGISTER_NAME() {
		// TODO 自動生成されたメソッド・スタブ
		return NAME;
	}

	// 主人を設定する
	@Override
	public void initOwner(EntityPlayer player) {
		// TODO 自動生成されたメソッド・スタブ
		// 懐き済み
        this.setTamed(true);
        this.setHealth(20.0F);
        this.setOwnerId(player.getUniqueID());
        this.playTameEffect(true);
	}














	// ドレス用のテクスチャ銘を一斉設定
	@Override
	public void setTextureDress(Map<ERMDressParts,String> texture){
		super.setTextureDress(texture);
		// 必須パーツが空欄の場合デフォルトパーツを設定する
		this.dataWatcher.set(TEX_BODY,
				Util.StringisEmptyOrNull(texture.get(ERMDressParts.BODY))?"littlegirl_body_cheek01.png":texture.get(ERMDressParts.BODY));
		this.dataWatcher.set(TEX_HEAD,
				Util.StringisEmptyOrNull(texture.get(ERMDressParts.HEAD))?"littlegirl_default0_head.png":texture.get(ERMDressParts.HEAD));
		this.dataWatcher.set(TEX_DRESS,
				Util.StringisEmptyOrNull(texture.get(ERMDressParts.DRESS))?"littlegirl_head_defalut01.png":texture.get(ERMDressParts.DRESS));
		this.dataWatcher.set(TEX_EYE,
				Util.StringisEmptyOrNull(texture.get(ERMDressParts.EYE))?"littlegirl_eye_default01.png":texture.get(ERMDressParts.EYE));
	}
}
