package basashi.erm.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import basashi.erm.config.EntityConst;
import basashi.erm.config.EntityConst.EntityBaseValue;
import basashi.erm.container.InventoryERMBase;
import basashi.erm.core.Mod_ERM;
import basashi.erm.gui.TextureBoxBase;
import basashi.erm.item.ItemCandy;
import basashi.erm.resource.parts.ERMDressParts;
import basashi.erm.util.Util;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

public abstract class EntityERMBase extends EntityTameable implements IEntityERMBase{
	// 懐き度
	protected static final DataParameter<Integer> LOVE = EntityDataManager.<Integer>func_187226_a(EntityERMBase.class, DataSerializers.field_187192_b);
	// スタミナ
	protected static final DataParameter<Float> STAMINA = EntityDataManager.<Float>func_187226_a(EntityERMBase.class, DataSerializers.field_187193_c);
	// 興味
	protected static final DataParameter<Float> INTEREST = EntityDataManager.<Float>func_187226_a(EntityERMBase.class, DataSerializers.field_187193_c);
	// 満腹度
	protected static final DataParameter<Float> FOOD = EntityDataManager.<Float>func_187226_a(EntityERMBase.class, DataSerializers.field_187193_c);
	// キャンディ残数
	protected static final DataParameter<Integer> CANDY = EntityDataManager.<Integer>func_187226_a(EntityERMBase.class,DataSerializers.field_187192_b);
	// 会話残数
	protected static final DataParameter<Integer> TALK = EntityDataManager.<Integer>func_187226_a(EntityERMBase.class,DataSerializers.field_187192_b);
	// テクスチャ銘
	// 髪、体色、目、衣装、オプション1～5
	protected static final DataParameter<String> TEX_HEAD = EntityDataManager.<String>func_187226_a(EntityERMBase.class, DataSerializers.field_187194_d);
	protected static final DataParameter<String> TEX_BODY = EntityDataManager.<String>func_187226_a(EntityERMBase.class, DataSerializers.field_187194_d);
	protected static final DataParameter<String> TEX_EYE = EntityDataManager.<String>func_187226_a(EntityERMBase.class, DataSerializers.field_187194_d);
	protected static final DataParameter<String> TEX_DRESS = EntityDataManager.<String>func_187226_a(EntityERMBase.class, DataSerializers.field_187194_d);
	protected static final DataParameter<String> TEX_OPT1 = EntityDataManager.<String>func_187226_a(EntityERMBase.class, DataSerializers.field_187194_d);
	protected static final DataParameter<String> TEX_OPT2 = EntityDataManager.<String>func_187226_a(EntityERMBase.class, DataSerializers.field_187194_d);
	protected static final DataParameter<String> TEX_OPT3 = EntityDataManager.<String>func_187226_a(EntityERMBase.class, DataSerializers.field_187194_d);
	protected static final DataParameter<String> TEX_OPT4 = EntityDataManager.<String>func_187226_a(EntityERMBase.class, DataSerializers.field_187194_d);
	protected static final DataParameter<String> TEX_OPT5 = EntityDataManager.<String>func_187226_a(EntityERMBase.class, DataSerializers.field_187194_d);


	// 懐き度最大値
	public static final int LOVE_MAX = 200;
	// キャンディ最大数
	public static final int CANDY_MAX = 5;
	// 会話最大数
	public static final int TALK_MAX = 5;
	// キャンディ回数回復
	protected int[][] candyHeal = {
			{30,1},
			{80,2},
			{100,3}
	};
	// 会話回数回復
	protected int[][] talkHeal = {
			{20,1},
			{50,2},
			{70,3},
			{90,4},
			{100,5}
	};


	/**
	 * 懐き度：モブがどれだけ好意を持っているか。懐き度が高いほど、反抗される確率が下がる。
	 * スタミナ:お手伝いをするのに必要な体力。お手伝い中に減っていき0未満になると協定的にお手伝いが中断される
	 *       1秒ごとに最大値の1/10 ずつ回復
	 * 興味    :お手伝いに対する興味。同じお手伝いを連続でお願いするか、継続して1日以上同じお手伝いを続けていると減少する。
	 *       興味がベース値の1/4以下になると興味の1/10の確率でお手伝いを拒む
	 * 飽きっぽさ：興味の減少分母。ベース興味/飽きっぽさ分お手伝いに対する興味が減少する
	 * わがまま度:お手伝いを拒む確率。わがまま度/懐き度の確率でお手伝いを拒む。拒んだお手伝いはゲーム内時間24時間選択できない
	 *
	 */

	/******************************************************************************************************/
	/** ステータス関係                                                                                                                                                                                                                                       **/
	/******************************************************************************************************/
	/** 値固定ステータス **/
	// スタミナベース値
	protected final float BASE_STAMINA;
	// 興味ベース値
	protected final float BASE_INTAREST;
	// わがままベース値
	protected final float BASE_REBELLIOUS;
	// 懐きやすさ
	protected final float MAG_LOVED;
	// 嫌いやすさ
	protected final float MAG_UNLOVED;
	// 飽きっぽさ
	protected final float MAG_UNINTAREST;





	/****************************************************/
	/**  テクスチャ関係                                                                                              **/
	/****************************************************/
	// テクスチャのパス
	public String textureDress;
	// ドレスが更新されたか？
	public boolean updateDress;
	// 鎧テクスチャのパス
	public String textureArmor;
	// 鎧が更新されたか？
	public boolean updateArmor;


	/*****************************************************/
	/** テクスチャ以外のリソース                                                                                  **/
	/*****************************************************/
	// セリフの名称
	public String mobText;
	// 音声設定ファイルの名称
	public String mobSound;


	/*****************************************************/
	/** その他                                                                                                                    **/
	/*****************************************************/
	// tick
	protected int cntTick;
	// 仕事中かどうか
	protected boolean isWork;
	// 前回作業
	protected IERMAI lastTimeAI;
	// 拒んだお手伝い
	protected List<RegistWork> rebelliousworks;
	// インベントリ
	InventoryERMBase inventory;

	// コンストラクタ
	public EntityERMBase(World worldIn){
		super(worldIn);
		EntityBaseValue value = EntityConst.getEntityBaseValue(this);
		BASE_STAMINA = value.getbstamina();
		BASE_INTAREST = value.getbintarest();
		BASE_REBELLIOUS = value.getbrebellious();
		MAG_LOVED = value.getmlove();
		MAG_UNLOVED = value.getmunlove();
		MAG_UNINTAREST = value.getmunintarest();
	}

	// Entity初期化
	@Override
	public void func_70088_a(){
		super.func_70088_a();
		cntTick = 0;
		isWork = false;

		this.field_70180_af.func_187214_a(LOVE, 20);
        this.field_70180_af.func_187214_a(STAMINA, MaxStamina());
        this.field_70180_af.func_187214_a(INTEREST, MaxIntarest());
        this.field_70180_af.func_187214_a(FOOD, 20.0F);
        this.field_70180_af.func_187214_a(TEX_HEAD, "default0");
        this.field_70180_af.func_187214_a(TEX_BODY, "default0");
        this.field_70180_af.func_187214_a(TEX_EYE, "default0");
        this.field_70180_af.func_187214_a(TEX_DRESS, "default0");
        this.field_70180_af.func_187214_a(TEX_OPT1, "");
        this.field_70180_af.func_187214_a(TEX_OPT2, "");
        this.field_70180_af.func_187214_a(TEX_OPT3, "");
        this.field_70180_af.func_187214_a(TEX_OPT4, "");
        this.field_70180_af.func_187214_a(TEX_OPT5, "");
        this.field_70180_af.func_187214_a(CANDY, CANDY_MAX);
        this.field_70180_af.func_187214_a(TALK, TALK_MAX);

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


	// スタミナの最大値を取得(スタミナベース + (スタミナベース*懐き度/100)
	public float MaxStamina(){
		return BASE_STAMINA + (BASE_STAMINA * this.getLove()/100);

	}

	// 興味の最大値を取得(興味ベース + (興味ベース*懐き度/100)
	public float MaxIntarest(){
		return BASE_INTAREST + (BASE_INTAREST * this.getLove()/100);
	}

	// スタミナ関連のチェック処理(回復、作業時のスタミナ減少)
	// お手伝い中の場合お手伝いを続行できるかどうかを返す
	protected boolean checkStamina(){
		// tickカウントアップ
		cntTick+=1;
		if (cntTick >= 20){
			// 前回回復から1秒以上経過で回復(最大値の1/10)
			addStamina(MaxStamina()/10);
			cntTick = 0;

			if (isWork){
				// 作業中なら作業に必要なスタミナを減らす
				addStamina(-1*Math.abs(lastTimeAI.needStamina()));
				if (this.getStamina() <= 0){
					// 作業続行不可
					return false;
				}
			}
		}
		// 作業続行可能
		return true;
	}

	protected boolean checkIntarest(IERMAI nextAI){
		if (nextAI.equals(lastTimeAI)){
			// 前回実行AIと同じ場合、ベース/の飽きっぽさ分興味を減らす
			addIntarest(-1*(BASE_INTAREST*this.MAG_UNINTAREST));
		}else{
			// 違う場合最大値/10興味を回復
			addIntarest(MaxIntarest()/10);
		}
		if ((this.getIntarest()/MaxIntarest())<4.0f){
			// 興味が最大値の1/4の場合

		}
		return true;
	}


	/*********************************************************************************/
	/** パラメータの取得                                                                                                                                                                           **/
	/*********************************************************************************/
	// 懐き度を設定する
	public void setLove(int value){
		int Love = ((value>LOVE_MAX)?LOVE_MAX:(value < 0)?0:value);
		this.field_70180_af.func_187227_b(LOVE, Love);
	}
	// 懐き度を加算する
	public void addLove(int addvalue){
		setLove(getLove() + addvalue);
	}
	// 懐き度を取得する
	public int getLove(){
		return field_70180_af.func_187225_a(LOVE);
	}
	// 割り算用の懐き度(0の場合1が帰る)
	protected int divideLove(){
		return getLove()==0?1:getLove();
	}

	// スタミナを設定する
	public void setStamina(float value){
		float stamina = value>MaxStamina()?MaxStamina():value<0?0:value;
		this.field_70180_af.func_187227_b(STAMINA, stamina);
	}
	// スタミナを加算する
	public void addStamina(float addvalue){
		setStamina(getStamina() +addvalue);
	}
	// スタミナを取得する
	public float getStamina(){
		return this.field_70180_af.func_187225_a(STAMINA);
	}

	// 興味を設定する
	public void setIntarest(float value){
		float intarest = value>MaxIntarest()?MaxIntarest():value<0?0:value;
		this.field_70180_af.func_187227_b(INTEREST, intarest);
	}
	// 興味を加算する
	public void addIntarest(float addvalue){
		setIntarest(getIntarest()+addvalue);
	}
	// 興味を取得する
	public float getIntarest(){
		return this.field_70180_af.func_187225_a(INTEREST);
	}

	// 満腹度を設定する
	public void setFood(float value){
		float food = (value<0?0:(value>=20.0f?20.0f:value));
	}
	// 満腹度を加算する
	public void addFood(float addValue){
		setFood(getFood() + addValue);
	}
	// 満腹度を取得
	public float getFood(){
		return this.field_70180_af.func_187225_a(FOOD);
	}


	// キャンディ使用残回数を取得
	public int getCandy(){
		return this.field_70180_af.func_187225_a(CANDY);
	}
	// キャンディ使用回数を設定する
	public void setCandy(int cnt){
		int candy = cnt<0?0:cnt>CANDY_MAX?CANDY_MAX:cnt;
		this.field_70180_af.func_187227_b(CANDY, cnt);
	}
	// キャンディ使用回数を回復する
	public void healCandy(){
		int healRange = Util.random(100);
		int healValue = 0;
		for (int i = 0; i < candyHeal.length; i++){
			if (healRange < candyHeal[i][0]){
				healValue = candyHeal[i][1];
				break;
			}
		}
		setCandy(getCandy() + healValue);
	}
	// キャンディを使用して懐き度を上げる
	public void useCandy(ItemCandy candy){
		int cnt = getCandy();
		if ( cnt != 0){
			// 上昇値を取得
			int value = (int)Math.floor(candy.healLove() * this.MAG_LOVED);
			// 懐き度を上昇させる
			addLove(value);
			// キャンディ使用回数を減らす
			setCandy(cnt-1);
		}
	}

	// 会話回数残回数を取得
	public int getTalk(){
		return this.field_70180_af.func_187225_a(TALK);
	}
	// 会話回数を設定する
	public void setTalk(int cnt){
		int candy = cnt<0?0:cnt>getTalkMax()?getTalkMax():cnt;
		this.field_70180_af.func_187227_b(TALK, cnt);
	}
	// 会話回数を回復する
	public void healTalk(){
		int healRange = Util.random(100);
		int healValue = 0;
		for (int i = 0; i < talkHeal.length; i++){
			if (healRange < talkHeal[i][0]){
				healValue = talkHeal[i][1];
				break;
			}
		}
		setTalk(getTalk() + healValue);
	}
	// 会話ができるか確認
	// true:セリフテキスト再生、false:固定テキスト再生
	public boolean canTalk(){
		int cnt = getTalk();
		if ( cnt == 0){return false;}

		// 懐き度を取得
		int love = getLove();
		if(love < 130){
			// 懐き度が130未満の場合、会話回数の減少と懐き度の上昇を行う
			// 上昇値を取得
			int value = (int)Math.floor(2 * this.MAG_LOVED);
			// 懐き度を上昇させる
			addLove(value);
			// 会話回数を減らす
			setTalk(cnt-1);
		}
		return true;
	}
	// 会話回数の最大数を取得
	public int getTalkMax(){
		return TALK_MAX;
	}

	// ドレス用のテクスチャ銘一覧を取得
	public Map<ERMDressParts,String> getTextureDresses(){
		Map<ERMDressParts,String> retMap = new HashMap<ERMDressParts,String>();
		retMap.put(ERMDressParts.BODY, this.field_70180_af.func_187225_a(TEX_BODY));
		retMap.put(ERMDressParts.HEAD, this.field_70180_af.func_187225_a(TEX_HEAD));
		retMap.put(ERMDressParts.DRESS, this.field_70180_af.func_187225_a(TEX_DRESS));
		retMap.put(ERMDressParts.EYE, this.field_70180_af.func_187225_a(TEX_EYE));
		retMap.put(ERMDressParts.OPT1, this.field_70180_af.func_187225_a(TEX_OPT1));
		retMap.put(ERMDressParts.OPT2, this.field_70180_af.func_187225_a(TEX_OPT2));
		retMap.put(ERMDressParts.OPT3, this.field_70180_af.func_187225_a(TEX_OPT3));
		retMap.put(ERMDressParts.OPT4, this.field_70180_af.func_187225_a(TEX_OPT4));
		retMap.put(ERMDressParts.OPT5, this.field_70180_af.func_187225_a(TEX_OPT5));
		return retMap;
	}

	// ドレス用のテクスチャ銘を一斉設定
	public void setTextureDress(Map<ERMDressParts,String> texture){
		this.field_70180_af.func_187227_b(TEX_BODY,texture.get(ERMDressParts.BODY));
		this.field_70180_af.func_187227_b(TEX_HEAD,texture.get(ERMDressParts.HEAD));
		this.field_70180_af.func_187227_b(TEX_DRESS,texture.get(ERMDressParts.DRESS));
		this.field_70180_af.func_187227_b(TEX_EYE,texture.get(ERMDressParts.EYE));
		this.field_70180_af.func_187227_b(TEX_OPT1,texture.get(ERMDressParts.OPT1));
		this.field_70180_af.func_187227_b(TEX_OPT2,texture.get(ERMDressParts.OPT2));
		this.field_70180_af.func_187227_b(TEX_OPT3,texture.get(ERMDressParts.OPT3));
		this.field_70180_af.func_187227_b(TEX_OPT4,texture.get(ERMDressParts.OPT4));
		this.field_70180_af.func_187227_b(TEX_OPT5,texture.get(ERMDressParts.OPT5));
	}

	// ドレス用のテクスチャをパーツ指定で取得
	public String getTextureDress(ERMDressParts parts){
		String ret = "";
		switch(parts){
		case BODY:
			ret = this.field_70180_af.func_187225_a(TEX_BODY);
		break;
		case HEAD:
			ret = this.field_70180_af.func_187225_a(TEX_HEAD);
		break;
		case DRESS:
			ret = this.field_70180_af.func_187225_a(TEX_DRESS);
		break;
		case EYE:
			ret = this.field_70180_af.func_187225_a(TEX_EYE);
		break;
		case OPT1:
			ret = this.field_70180_af.func_187225_a(TEX_OPT1);
		break;
		case OPT2:
			ret = this.field_70180_af.func_187225_a(TEX_OPT2);
		break;
		case OPT3:
			ret = this.field_70180_af.func_187225_a(TEX_OPT3);
		break;
		case OPT4:
			ret = this.field_70180_af.func_187225_a(TEX_OPT4);
			break;
		case OPT5:
			ret = this.field_70180_af.func_187225_a(TEX_OPT5);
			break;
		}
		return ret;
	}

	// ドレス用のテクスチャをパーツ指定で設定
	public void setTextureDress(ERMDressParts parts, String texture){
		switch(parts){
		case BODY:
			this.field_70180_af.func_187227_b(TEX_BODY,texture);
		break;
		case HEAD:
			this.field_70180_af.func_187227_b(TEX_HEAD,texture);
		break;
		case DRESS:
			this.field_70180_af.func_187227_b(TEX_DRESS,texture);
		break;
		case EYE:
			this.field_70180_af.func_187227_b(TEX_EYE,texture);
		break;
		case OPT1:
			this.field_70180_af.func_187227_b(TEX_OPT1,texture);
		break;
		case OPT2:
			this.field_70180_af.func_187227_b(TEX_OPT2,texture);
		break;
		case OPT3:
			this.field_70180_af.func_187227_b(TEX_OPT3,texture);
		break;
		case OPT4:
			this.field_70180_af.func_187227_b(TEX_OPT4,texture);
			break;
		case OPT5:
			this.field_70180_af.func_187227_b(TEX_OPT5,texture);
			break;
		}
	}


    //////////////////////// IEntityERMBase
	// モブの名称を取り出す
	@Override
	public abstract String REGISTER_NAME();

	// オーナー情報を初期化する
	@Override
	public abstract void initOwner(EntityPlayer player);


	// モブの防具用テクスチャのファイルパスを取得する
	@Override
	public void setTextureArmor(String modelNameArmor) {
		this.textureArmor = modelNameArmor;
		refreshModels();
	}
	// モブの防具用テクスチャのファイルパスを取得する
	@Override
	public String getTextureArmor(){
		return this.textureArmor;
	}

	// モブのテクスチャのファイルパスを設定する
	@Override
	public void setTextureDress(String modelNameMain) {
		this.textureDress = modelNameMain;
		refreshModels();
	}

	// モブのテクスチャのファイルパスを取得する
	@Override
	public String getTextureDress(){
		return this.textureDress;
	}

	// ドレスを更新するかどうか取得する
	public boolean isUpdateDress(){
		if (Util.StringisEmptyOrNull(this.textureDress)){return false;}
		return this.updateDress;
	}
	// ドレスを更新するかどうか設定する
	public void UpdateDress(boolean update){
		this.updateDress = update;
	}


	@Override
	public TextureBoxBase[] getTextureBox() {
		return null; //textureData.getTextureBox();
	}




	protected void refreshModels() {
//		String defName = ModelManager.instance.getRandomTextureString(rand);
//		TextureBoxBase mainModel  = modelBoxAutoSelect(textureNameMain);
//		if (mainModel == null) {
//			mainModel = modelBoxAutoSelect(defName);
//		}
//
//		TextureBoxBase armorModel = modelBoxAutoSelect(textureNameArmor);
//		if (armorModel == null) {
//			armorModel = modelBoxAutoSelect(defName);
//		}
//
//		setTextureBox(new TextureBoxBase[]{mainModel, armorModel});
//		setTextureNames();
	}
}
