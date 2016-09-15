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
		this.field_70714_bg.func_75776_a(1,  new EntityAIWander(this,1.0));
		this.field_70714_bg.func_75776_a(2,  new EntityAILookIdle(this));
	}

	// エンティティの初期化
	@Override
	public void func_70088_a(){
		super.func_70088_a();

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
	public EntityAgeable func_90011_a(EntityAgeable ageable) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public void func_70030_z(){
		super.func_70030_z();
	}

	// 死亡時
	@Override
    protected void func_70609_aI(){
		// インベントリの中身をすべて捨てて

		// 体力を全開にして
		this.func_70606_j(20.0f);
		// リスポーン地点にテレポート
		EntityPlayer pl = (EntityPlayer)this.func_70902_q();
		if ( pl != null){
			BlockPos pos = pl.field_70170_p.func_175694_M();
			this.func_70012_b(pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p(), 0.0f, 0.0f);
		}
	}

    public float func_70047_e()
    {
        float f = 1.74F;

        if (this.func_70631_g_())
        {
            f = (float)((double)f - 0.81D);
        }

        return f;
    }


    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void func_70014_b(NBTTagCompound tagCompound)
    {
        super.func_70014_b(tagCompound);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void func_70037_a(NBTTagCompound tagCompund)
    {
        super.func_70037_a(tagCompund);
        Mod_ERM.setEntityInstance(this);
    }

    public void func_70107_b(double x, double y, double z){
    	super.func_70107_b(x, y, z);
		ModLog.log().debug(this.field_70165_t+","+this.field_70163_u+","+this.field_70161_v);
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
        this.func_70903_f(true);
        this.func_70606_j(20.0F);
        this.func_184754_b(player.func_110124_au());
        this.func_70908_e(true);
	}














	// ドレス用のテクスチャ銘を一斉設定
	@Override
	public void setTextureDress(Map<ERMDressParts,String> texture){
		super.setTextureDress(texture);
		// 必須パーツが空欄の場合デフォルトパーツを設定する
		this.field_70180_af.func_187227_b(TEX_BODY,
				Util.StringisEmptyOrNull(texture.get(ERMDressParts.BODY))?"littlegirl_body_cheek01.png":texture.get(ERMDressParts.BODY));
		this.field_70180_af.func_187227_b(TEX_HEAD,
				Util.StringisEmptyOrNull(texture.get(ERMDressParts.HEAD))?"littlegirl_default0_head.png":texture.get(ERMDressParts.HEAD));
		this.field_70180_af.func_187227_b(TEX_DRESS,
				Util.StringisEmptyOrNull(texture.get(ERMDressParts.DRESS))?"littlegirl_head_defalut01.png":texture.get(ERMDressParts.DRESS));
		this.field_70180_af.func_187227_b(TEX_EYE,
				Util.StringisEmptyOrNull(texture.get(ERMDressParts.EYE))?"littlegirl_eye_default01.png":texture.get(ERMDressParts.EYE));
	}
}
