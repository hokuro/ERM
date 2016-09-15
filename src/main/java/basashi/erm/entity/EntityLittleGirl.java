package basashi.erm.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import basashi.erm.config.ConfigValue;
import basashi.erm.container.InventoryERMBase;
import basashi.erm.entity.setting.EntityVoiceSetting.CallMeSetting;
import basashi.erm.item.ItemCandy;
import basashi.erm.model.ModelERMBase;
import basashi.erm.model.ModelLittleGirl;
import basashi.erm.resource.object.ResourceTag;
import basashi.erm.resource.textures.EnumTextureParts;
import basashi.erm.resource.textures.SettingCustomTexture.SettingData;
import basashi.erm.resource.tone.ToneTag;
import basashi.erm.util.Util;
import basashi.erm.util.Values;
import basashi.erm.util.Values.KIND_STATUS;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class EntityLittleGirl extends EntityERMBase{
	// 登録名
	public static final String NAME = "LittleGirl";
	// 手が届く距離
	private final static double contactDistance = 3.0D;
	// 横幅
	private final static float width = 0.6F;
	// 身長
	private final static float height = 1.2F;
	// メインモデル
	private final ModelLittleGirl mainmodel;
	// 利き腕
	private final int dominantArm;
	// インベントリ
	private InventoryERMBase inventory;
	// つぶやきに関する設定
	private CallMeSetting clmSetting;
	// テクスチャ設定
	private Map<EnumTextureParts,SettingData> texSetting;
	// トーン設定
	private Map<ResourceTag, List<String>> tone;
	// AI設定
	List<EntityAIBase> aiTasks;

	/**
	 * コンストラクタ
	 * @param worldIn
	 */
	public EntityLittleGirl(World worldIn) {
		super(worldIn);
		// メインモデル
		mainmodel = new ModelLittleGirl();
		// 利き手
		dominantArm = 0;

	}

	@Override
	public void entityInit(){
		initializeResource();
		super.entityInit();
	}

    private boolean getCanSpawnHere(EntityERMBase entity,double x, double y, double z)
    {
        int i = MathHelper.floor_double(x);
        int j = MathHelper.floor_double(y);
        int k = MathHelper.floor_double(z);
        BlockPos blockpos = new BlockPos(i, j, k);
        IBlockState state = entity.worldObj.getBlockState(blockpos.down());
        return state.getBlock() != Blocks.air &&
        		state.getBlock() != Blocks.leaves && state.getBlock() != Blocks.flowing_lava &&
        		state.getBlock() != Blocks.water &&state.getBlock() != Blocks.flowing_water;
    }

	/*******************************************************************************************************/
	/** 攻撃                                                                                              **/
	/*******************************************************************************************************/
	@Override
	public boolean attackEntityFrom(DamageSource source, float damage) {
		float dmg = damage;
		// ダメージを与えた相手を取り出す
		Entity target = source.getEntity();

		if(source.getDamageType().equalsIgnoreCase("thrown"))
		{
			if(target!=null && this.mobAvatar!=null && target.getEntityId()==this.mobAvatar.getEntityId())
			{
				dmg = 0;
			}
		}

		// ゲーム難易度によるダメージ・クールタイム補正
		this.getTickCounter().clearCounter(Values.KIND_COUNTER.COUNTER_LASTDAMAGE,15);

		if (dmg == 0) {
			// ノーダメージ
			dmg = 0;
		}

		return super.attackEntityFrom(source, damage);
	}

	@Override
	public boolean attackEntityAsMob(Entity entity) {
		return false;
	}

	@Override
	public boolean canAttackWithItem() {
		return true;
	}

	@Override
	protected void damageEntity(DamageSource source, float damageOrg) {
		super.damageEntity(source, damageOrg);
	}

	@Override
	public void onDeathUpdate(){
		// 体力を全開にして
		this.setHealth(20.0f);
		// リスポーン地点にテレポート
		EntityPlayer pl = (EntityPlayer)this.getOwner();
		if ( pl != null){
			BlockPos pos = pl.worldObj.getSpawnPoint();
			this.setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(), 0.0f, 0.0f);
		}
		// お手伝いをクリア
		this.settingAI().setAI("", null);
		// 待機状態に
		this.settingAI().setWaite(true);
	}

	/*******************************************************************************************************/
	/** アップデート                                                                                      **/
	/*******************************************************************************************************/
	@Override
	protected void updateAITick() {
		super.updateAITick();
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
	}

	@Override
	public void onEntityUpdate(){
		super.onEntityUpdate();
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();

//		ItemStack potion;
//		// 自分がダメージを受けていれば自分を回復
//		float lhealth = this.getHealth();
//		if (lhealth > 0) {
//			if (!this.worldObj.isRemote) {
//				if (this.getSwingStatusDominant().canAttack()) {
//					// 回復ポーションを持っているか
//					int idx = 0;
//					if ((idx = inventory.SearchPotion(MobEffects.heal,Values.KIND_SEARCHIPOTION.ALL)) != -1l){
//						// ポーションを使用アイテムに
//						inventory.swapCurrentMainItem(idx);
//						// 自分自身にポーションを使用
//						this.usePotionTotarget(this);
//						// アイテムを元に戻す
//						inventory.swapCurrentMainItem(idx);
//					}
//				}
//			}
//		}
//
//		// お手伝い中ではなく、御主人がダメージを受けていれば御主人を回復
//		if (!this.settingAI().doWork()){
//			EntityLivingBase owner = this.getOwner();
//			if (owner.getHealth() < owner.getMaxHealth()){
//				// 御主人と自分の位置関係を把握
//				// TODO: 視線が通らない場合回復できないようにした方がいいのかな
//				BlockPos mpos = this.getPosition();
//				BlockPos opos = owner.getPosition();
//				double x = mpos.getX() - opos.getX();
//				double y = mpos.getY() - opos.getY();
//				double z = mpos.getZ() - opos.getZ();
//				double dist = Math.sqrt(x*x + y*y + z*z);
//				if (contactDistance >= dist){
//					// 御主人が手の届く範囲にいる
//					// 回復ポーションを持っているか
//					int idx = 0;
//					if ((idx = inventory.SearchPotion(MobEffects.heal,Values.KIND_SEARCHIPOTION.ALL)) >= 0){
//						// ポーションを使用アイテムに
//						inventory.swapCurrentMainItem(idx);
//						// 御主人にポーションを使用
//						this.usePotionTotarget(owner);
//						// アイテムを元に戻す
//						inventory.swapCurrentMainItem(idx);
//					}
//				}
//			}
//		}
	}

	/*******************************************************************************************************/
	/** サイズ                                                                                           **/
	/*******************************************************************************************************/
	@Override
	public double getMountedYOffset() {
		// TODO: Changed from 'riddenByEntity'. Is it correct?
		if (this.getControllingPassenger() instanceof EntityChicken) {
			return height + 0.03D;
		}
		if (this.getControllingPassenger() instanceof EntitySquid) {
			return height - 0.2D;
		}
		return (double)this.height * 0.75D + 0.35D;
	}

	@Override
	public double getYOffset() {
		double yOffset = -0.30D;
		return yOffset;
	}

	@Override
	public float getEyeHeight() {
		 float f = 0.9F;
		 return f;
	}


	/*******************************************************************************************************/
	/** IERMMob                                                                                           **/
	/*******************************************************************************************************/
	@Override
	public float getBaseStamina()       { return 80.0F; }
	@Override
	public float getBaseIntarest()      { return 120.0F; }
	@Override
	public int getBaseRebelious()       { return 90; }
	@Override
	public float getWeightLove()        { return 1.5F; }
	@Override
	public float getWeightIntarest()    { return 2.0F; }
	@Override
	public int getSpdRefleshStamina()   { return 20; }
	@Override
	public int getSpdTiredStamina()     { return 10; }
	@Override
	public int getMemory()              { return 5; }
	@Override
	public float getWidth()             { return width; }
	@Override
	public float getHeight()            { return height; }
	@Override
	public String EntityName()          { return "LittleGirl"; }
	@Override
	public String MobDefaultName()      { return I18n.translateToLocal("entity.erm.LittleGirl.name"); }
	@Override
	public double getConbatSpeed()      { return 0.2; }
	@Override
	public double getSneakingSpeed()    { return 0.7; }
	@Override
	public int getInventorySize()       { return 18;  }
	@Override
	public float getWorkFood()          { return 3;   }
	@Override
	public float getFoodWieght()        { return  (statusFood.getStatus() <= 3.0F)?(2.0F + 3.0F - statusFood.getStatus()):1.0F;}

	@Override
	public Values.KIND_CANDYTAST getCandyFavoritte(String candyName) {
		Values.KIND_CANDYTAST ret = Values.KIND_CANDYTAST.GOOD;
		switch(candyName){
		case ItemCandy.CANDYNAME_NORMAL:
			ret = Values.KIND_CANDYTAST.GOOD;
			break;
		case ItemCandy.CANDYNAME_APPLE:
			ret = Values.KIND_CANDYTAST.LOVE;
			break;
		case ItemCandy.CANDYNAME_WMELLON:
			ret = Values.KIND_CANDYTAST.LOVE;
			break;
		case ItemCandy.CANDYNAME_CHOCO:
			ret = Values.KIND_CANDYTAST.YUMMY;
			break;
		case ItemCandy.CANDYNAME_PUMPKIN:
			ret = Values.KIND_CANDYTAST.BAD;
			break;
		case ItemCandy.CANDYNAME_SQUID:
			ret = Values.KIND_CANDYTAST.GOOD;
			break;
		case ItemCandy.CANDYNAME_CALLOT:
			ret = Values.KIND_CANDYTAST.BAD;
			break;
		case ItemCandy.CANDYNAME_CACTUS:
			ret = Values.KIND_CANDYTAST.BAD;
			break;
		default:
			ret = Values.KIND_CANDYTAST.GOOD;
		}
		return ret;
	}

	@Override
	public String getDressTextureFile()  { return "LittleGirlD.png"; }
	@Override
	public String getDressSettingFile()  { return "LittelGirlD.tex"; }
	@Override
	public String getAromorTextureFile() { return "LittleGirlA.png"; }
	@Override
	public String getSoundFile()         { return "LittleGirl.snd"; }


	@Override
	public CallMeSetting getVoiceSetting() {
		return clmSetting;
	}

	@Override
	public List<EntityAIBase> getDefaultAI(EntityERMBase entity) {
		if(aiTasks == null){
			aiTasks = new ArrayList<EntityAIBase>();
			aiTasks.add(new EntityAIWander(entity,1.0));
			aiTasks.add(new EntityAILookIdle(entity));
		}
		return aiTasks;
	}

	@Override
	public Map<EnumTextureParts, SettingData> getDefaultTexture() {
		return texSetting;
	}

	@Override
	public Map<ResourceTag, List<String>> getDefaultTone() {
		return tone;
	}

	@Override
	public void applyEntityAttributes(EntityERMBase entity) {
		// 対象移動可能範囲
		entity.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(20.0D);
		// 基本移動速度
		entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23000000417232513D);
		// 標準攻撃力１
		entity.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
		// 攻撃速度
		entity.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_SPEED).setBaseValue(5D);
		// Wrap EntityPlayer
		entity.getAttributeMap().registerAttribute(SharedMonsterAttributes.LUCK);
	}

	// キャンディ回数回復
	protected final static int[][] candyHeal = { {30,1}, {80,2}, {100,3} };
	@Override
	public int healCandyCounter() {
		int healRange = Util.random(100);
		int healValue = 0;
		for (int i = 0; i < candyHeal.length; i++){
			if (healRange < candyHeal[i][0]){
				healValue = candyHeal[i][1];
				break;
			}
		}
		return healValue;
	}

	// 会話回数回復
	protected final static int[][] talkHeal = { {20,1}, {50,2}, {70,3}, {90,4}, {100,5} };
	@Override
	public int healTalkCounter() {
		int healRange = Util.random(100);
		int healValue = 0;
		for (int i = 0; i < talkHeal.length; i++){
			if (healRange < talkHeal[i][0]){
				healValue = talkHeal[i][1];
				break;
			}
		}
		return healValue;
	}


	@Override
	public int getDominantArm() {
		return this.dominantArm;
	}

	@Override
	public boolean getIFF(Entity target) {
		// みんな友達
		return false;
	}

	@Override
	public boolean pushOutOfBlocks(EntityERMBase entity, double x, double y, double z) {
		// EntityPlayerSPのを引っ張ってきた
		int var7 = MathHelper.floor_double(x);
		int var8 = MathHelper.floor_double(y);
		int var9 = MathHelper.floor_double(z);
		double var10 = x - var7;
		double var12 = z - var9;

		boolean lflag = false;
		for (int li = 0; li < height; li++) {
			lflag |= isBlockTranslucent(entity, var7, var8 + li, var9);
		}
		if (lflag) {
			boolean var14 = !isBlockTranslucent(entity, var7 - 1, var8, var9) && !isBlockTranslucent(entity, var7 - 1, var8 + 1, var9);
			boolean var15 = !isBlockTranslucent(entity, var7 + 1, var8, var9) && !isBlockTranslucent(entity, var7 + 1, var8 + 1, var9);
			boolean var16 = !isBlockTranslucent(entity, var7, var8, var9 - 1) && !isBlockTranslucent(entity, var7, var8 + 1, var9 - 1);
			boolean var17 = !isBlockTranslucent(entity, var7, var8, var9 + 1) && !isBlockTranslucent(entity, var7, var8 + 1, var9 + 1);
			byte var18 = -1;
			double var19 = 9999.0D;

			if (var14 && var10 < var19) {
				var19 = var10;
				var18 = 0;
			}

			if (var15 && 1.0D - var10 < var19) {
				var19 = 1.0D - var10;
				var18 = 1;
			}

			if (var16 && var12 < var19) {
				var19 = var12;
				var18 = 4;
			}

			if (var17 && 1.0D - var12 < var19) {
				var19 = 1.0D - var12;
				var18 = 5;
			}

			float var21 = 0.1F;

			if (var18 == 0) {
				entity.motionX = (-var21);
			}

			if (var18 == 1) {
				entity.motionX = var21;
			}

			if (var18 == 4) {
				entity.motionZ = (-var21);
			}

			if (var18 == 5) {
				entity.motionZ = var21;
			}

			return !(var14 | var15 | var16 | var17);
		}

		return false;
	}

	/**
	 * 埋葬対策コピー
	 */
	private boolean isBlockTranslucent(EntityERMBase entity, int x, int y, int z) {
		IBlockState iState = entity.worldObj.getBlockState(new BlockPos(x, y, z));
		return iState.getBlock().isNormalCube(iState);
	}

	/**
	 * ダメージに応じて信頼度減少
	 * @param source
	 */
	@Override
	public void reduceLoveCauseDamage(EntityERMBase entity,DamageSource source, float damage){
		float value = 0.0F;
		if (source == DamageSource.inFire ||
				source == DamageSource.onFire){
			// 火災ダメージ(3.0F)
			value = 3.0F;
		}else if (source == DamageSource.fall){
			// 落下ダメージ(ダメージの半分)
			value = damage/2;
		}else if(source == DamageSource.inWall){
			// 窒息ダメージ(3.0F)
			value = 3.0F;
		}else if(source == DamageSource.starve){
			// プレイヤーによる攻撃 (現在値の1/10)
			value = entity.getStatus(KIND_STATUS.STATUS_LOVE).getStatus()/10;
		}else{
			// その他ダメージ
			value = 1.5F;
		}
		entity.getStatus(KIND_STATUS.STATUS_LOVE).addStatus(value*-1);
	}

	@Override
	public ModelERMBase getMainModel() {
		return mainmodel;
	}


	/**********************************************************************************************/
	/** read/write                                                                               **/
	/**********************************************************************************************/
	@Override
	public void writeEntityToNBT(NBTTagCompound tagCompound) {
		super.writeEntityToNBT(tagCompound);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound tagCompound) {
		super.readEntityFromNBT(tagCompound);
	}

	/**********************************************************************************************/
	/** リソース初期化                                                                           **/
	/**********************************************************************************************/
	/**
	 * リソース初期化
	 */
	private void initializeResource(){
			inventory = null;
			// つぶやきに関する設定
		clmSetting = new CallMeSetting(
				ConfigValue._littlegirl.TalkFreq,
				ConfigValue._littlegirl.TalkSpan,
				ConfigValue._littlegirl.CallMe,
				ConfigValue._littlegirl.CallPlayer);
		// デフォルトテクスチャ設定
		texSetting = new HashMap<EnumTextureParts,SettingData>();
		texSetting.put(EnumTextureParts.HEAD, new SettingData("littlegirl_head_defalut01.png",48,48,0,0));
		texSetting.put(EnumTextureParts.BODY, new SettingData("littlegirl_body_cheek01.png",48,48,0,0));
		texSetting.put(EnumTextureParts.EYE, new SettingData("littlegirl_eye_default01.png",8,8,8,8));
		texSetting.put(EnumTextureParts.DRESS, new SettingData("littlegirl_dress_default01.png",48,48,0,0));
		texSetting.put(EnumTextureParts.OPT1, new SettingData("littlegirl_option_default01.json",48,48,48,0));
		// デフォルトトーン設定
		tone = new HashMap<ResourceTag, List<String>>();
		tone.put(ToneTag.voice_callposition,new ArrayList<String>(){
			{add(I18n.translateToLocal("chat.littlegirl.tone_callposition.tone01"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_callposition.tone02"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_callposition.tone03"));}});
		tone.put(ToneTag.voice_candy_good,new ArrayList<String>(){
			{add(I18n.translateToLocal("chat.littlegirl.tone_candy.tone01"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_candy.tone02"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_candy.tone03"));}});
		tone.put(ToneTag.voice_candy_yummy,new ArrayList<String>(){
			{add(I18n.translateToLocal("chat.littlegirl.tone_candy_like.tone01"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_candy_like.tone02"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_candy_like.tone03"));}});
		tone.put(ToneTag.voice_candy_love,new ArrayList<String>(){
			{add(I18n.translateToLocal("chat.littlegirl.tone_candy_love.tone01"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_candy_love.tone02"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_candy_love.tone03"));}});
		tone.put(ToneTag.voice_candy_bad,new ArrayList<String>(){
			{add(I18n.translateToLocal("chat.littlegirl.tone_candy_unpalatable.tone01"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_candy_unpalatable.tone02"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_candy_unpalatable.tone03"));}});
		tone.put(ToneTag.voice_damage,new ArrayList<String>(){
			{add(I18n.translateToLocal("chat.littlegirl.tone_damage.tone01"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_damage.tone02"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_damage.tone03"));}});
		tone.put(ToneTag.voice_damage_fall,new ArrayList<String>(){
			{add(I18n.translateToLocal("chat.littlegirl.tone_damage_fall.tone01"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_damage_fall.tone02"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_damage_fall.tone03"));}});
		tone.put(ToneTag.voice_damage_fire,new ArrayList<String>(){
			{add(I18n.translateToLocal("chat.littlegirl.tone_damage_fire.tone01"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_damage_fire.tone02"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_damage_fire.tone03"));}});
		tone.put(ToneTag.voice_damage_cactus,new ArrayList<String>(){
			{add(I18n.translateToLocal("chat.littlegirl.tone_damage_cactus.tone01"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_damage_cactus.tone02"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_damage_cactus.tone03"));}});
		tone.put(ToneTag.voice_damage_lightning,new ArrayList<String>(){
			{add(I18n.translateToLocal("chat.littlegirl.tone_damage_lightning.tone01"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_damage_lightning.tone02"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_damage_lightning.tone03"));}});
		tone.put(ToneTag.voice_damage_owner,new ArrayList<String>(){
			{add(I18n.translateToLocal("chat.littlegirl.tone_damage_owner.tone01"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_damage_owner.tone02"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_damage_owner.tone03"));}});
		tone.put(ToneTag.voice_damage_suffocation,new ArrayList<String>(){
			{add(I18n.translateToLocal("chat.littlegirl.tone_damage_suffocation.tone01"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_damage_suffocation.tone02"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_damage_suffocation.tone03"));}});
		tone.put(ToneTag.voice_dead,new ArrayList<String>(){
			{add(I18n.translateToLocal("chat.littlegirl.tone_dead.tone01"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_dead.tone02"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_dead.tone03"));}});
		tone.put(ToneTag.voice_dead_owner,new ArrayList<String>(){
			{add(I18n.translateToLocal("chat.littlegirl.tone_dead_owner.tone01"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_dead_owner.tone02"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_dead_owner.tone03"));}});
		tone.put(ToneTag.voice_detect_monster,new ArrayList<String>(){
			{add(I18n.translateToLocal("chat.littlegirl.tone_detect_monster.tone01"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_detect_monster.tone02"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_detect_monster.tone03"));}});
		tone.put(ToneTag.voice_living,new ArrayList<String>(){
			{add(I18n.translateToLocal("chat.littlegirl.tone_living.tone01"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_living.tone02"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_living.tone03"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_living.tone04"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_living.tone05"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_living.tone06"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_living.tone07"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_living.tone08"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_living.tone09"));}});
		tone.put(ToneTag.voice_living_hungry,new ArrayList<String>(){
			{add(I18n.translateToLocal("chat.littlegirl.tone_hunger.tone01"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_hunger.tone02"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_hunger.tone03"));}});
		tone.put(ToneTag.voice_living_morning,new ArrayList<String>(){
			{add(I18n.translateToLocal("chat.littlegirl.tone_living_morning.tone01"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_living_morning.tone02"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_living_morning.tone03"));}});
		tone.put(ToneTag.voice_living_night,new ArrayList<String>(){
			{add(I18n.translateToLocal("chat.littlegirl.tone_living_night.tone01"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_living_night.tone02"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_living_night.tone03"));}});
		tone.put(ToneTag.voice_living_waite,new ArrayList<String>(){
			{add(I18n.translateToLocal("chat.littlegirl.tone_living_waite.tone01"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_living_waite.tone02"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_living_waite.tone03"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_living_waite.tone04"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_living_waite.tone05"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_living_waite.tone06"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_living_waite.tone07"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_living_waite.tone08"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_living_waite.tone09"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_living_waite.tone10"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_living_waite.tone11"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_living_waite.tone12"));}});
		tone.put(ToneTag.voice_attack,new ArrayList<String>(){
			{add(I18n.translateToLocal("chat.littlegirl.tone_attack.tone01"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_attack.tone02"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_attack.tone03"));}});
		tone.put(ToneTag.voice_win,new ArrayList<String>(){
			{add(I18n.translateToLocal("chat.littlegirl.tone_win.tone01"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_win.tone02"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_win.tone03"));}});
		tone.put(ToneTag.voice_work_start,new ArrayList<String>(){
			{add(I18n.translateToLocal("chat.littlegirl.tone_work_start.tone01"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_work_start.tone02"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_work_start.tone03"));}});
		tone.put(ToneTag.voice_work_stop,new ArrayList<String>(){
			{add(I18n.translateToLocal("chat.littlegirl.tone_work_stop.tone01"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_work_stop.tone02"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_work_stop.tone03"));}});
		tone.put(ToneTag.voice_work_stop_hungry,new ArrayList<String>(){
			{add(I18n.translateToLocal("chat.littlegirl.tone_work_hungerstopr.tone01"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_work_hungerstopr.tone02"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_work_hungerstopr.tone03"));}});
		tone.put(ToneTag.voice_work_stop_item,new ArrayList<String>(){
			{add(I18n.translateToLocal("chat.littlegirl.tone_work_stop_item.tone01"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_work_stop_item.tone02"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_work_stop_item.tone03"));}});
		tone.put(ToneTag.voice_work_stop_stamina,new ArrayList<String>(){
			{add(I18n.translateToLocal("chat.littlegirl.tone_work_stop_stamina.tone01"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_work_stop_stamina.tone02"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_work_stop_stamina.tone03"));}});
		tone.put(ToneTag.voice_work_stop_tool,new ArrayList<String>(){
			{add(I18n.translateToLocal("chat.littlegirl.tone_work_stop_tool.tone01"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_work_stop_tool.tone02"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_work_stop_tool.tone03"));}});
		tone.put(ToneTag.voice_work_item_missing,new ArrayList<String>(){
			{add(I18n.translateToLocal("chat.littlegirl.tone_work_item_missing.tone01"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_work_item_missing.tone02"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_work_item_missing.tone03"));}});
		tone.put(ToneTag.voice_work_tool_missing,new ArrayList<String>(){
			{add(I18n.translateToLocal("chat.littlegirl.tone_work_tool_missing.tone01"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_work_tool_missing.tone02"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_work_tool_missing.tone03"));}});
		tone.put(ToneTag.voice_work_turndown_hungry,new ArrayList<String>(){
			{add(I18n.translateToLocal("chat.littlegirl.tone_work_turndown_hungry.tone01"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_work_turndown_hungry.tone02"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_work_turndown_hungry.tone03"));}});
		tone.put(ToneTag.voice_work_turndown_intarest,new ArrayList<String>(){
			{add(I18n.translateToLocal("chat.littlegirl.tone_work_turndown.tone01"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_work_turndown.tone02"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_work_turndown.tone03"));}});
		tone.put(ToneTag.voice_work_turndown_stamina,new ArrayList<String>(){
			{add(I18n.translateToLocal("chat.littlegirl.tone_turndown_stamina.tone01"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_turndown_stamina.tone02"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_turndown_stamina.tone03"));}});
		tone.put(ToneTag.voice_food_eat,new ArrayList<String>(){
			{add(I18n.translateToLocal("chat.littlegirl.tone_eat.tone01"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_eat.tone02"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_eat.tone03"));}});
		tone.put(ToneTag.voice_food_satiety,new ArrayList<String>(){
			{add(I18n.translateToLocal("chat.littlegirl.tone_food_satiety.tone01"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_food_satiety.tone02"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_food_satiety.tone03"));}});
		tone.put(ToneTag.voice_talk_nocnt,new ArrayList<String>(){
			{add(I18n.translateToLocal("chat.littlegirl.tone_canttalk.tone01"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_canttalk.tone02"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_canttalk.tone03"));}});
		tone.put(ToneTag.voice_talk,new ArrayList<String>(){
			{add(I18n.translateToLocal("chat.littlegirl.tone_talk.tone01"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_talk.tone02"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_talk.tone03"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_talk.tone04"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_talk.tone05"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_talk.tone06"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_talk.tone07"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_talk.tone08"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_talk.tone09"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_talk.tone10"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_talk.tone11"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_talk.tone12"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_talk.tone13"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_talk.tone14"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_talk.tone15"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_talk.tone16"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_talk.tone17"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_talk.tone18"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_talk.tone19"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_talk.tone20"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_talk.tone21"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_talk.tone22"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_talk.tone23"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_talk.tone24"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_talk.tone25"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_talk.tone26"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_talk.tone27"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_talk.tone28"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_talk.tone29"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_talk.tone30"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_talk.tone31"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_talk.tone32"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_talk.tone33"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_talk.tone34"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_talk.tone35"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_talk.tone36"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_talk.tone37"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_talk.tone38"));}
			{add(I18n.translateToLocal("chat.littlegirl.tone_talk.tone39"));}});
		// デフォルトAI
		aiTasks = null;
	}


	@Override
	public String getRegisterName() {
		return NAME;
	}


}
