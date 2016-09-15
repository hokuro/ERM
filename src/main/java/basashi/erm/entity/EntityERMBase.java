package basashi.erm.entity;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import aaamikansei.EntityERMAvater;
import basashi.erm.container.InventoryERMBase;
import basashi.erm.core.ModCommon;
import basashi.erm.core.Mod_ERM;
import basashi.erm.core.log.ModLog;
import basashi.erm.entity.logic.IntarestCandyList;
import basashi.erm.entity.logic.PathNavigatorERMBase;
import basashi.erm.entity.logic.SwingStatus;
import basashi.erm.entity.setting.EntityAISetting;
import basashi.erm.entity.setting.EntitySoundSetting;
import basashi.erm.entity.setting.EntityTextureSetting;
import basashi.erm.entity.setting.EntityToneSetting;
import basashi.erm.entity.setting.EntityVoiceSetting;
import basashi.erm.entity.status.EntityERMCounter;
import basashi.erm.entity.status.EntityERMStatus;
import basashi.erm.entity.status.EntityERMStatusCounter;
import basashi.erm.entity.status.EntityERMStatusExp;
import basashi.erm.entity.status.EntityERMStatusFood;
import basashi.erm.entity.status.EntityERMStatusLove;
import basashi.erm.entity.status.EntityERMStatusStamina;
import basashi.erm.gui.GuiHandler;
import basashi.erm.item.ERMItem;
import basashi.erm.item.ItemCandy;
import basashi.erm.message.MessageCandy;
import basashi.erm.message.MessageEat;
import basashi.erm.resource.ERMResourceManager;
import basashi.erm.resource.object.VoiceTag;
import basashi.erm.resource.textures.SettingCustomTexture;
import basashi.erm.util.Util;
import basashi.erm.util.Values;
import basashi.erm.util.Values.KIND_STATUS;
import basashi.erm.util.Values.KIND_WORK_CANCEL;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraft.network.play.server.SPacketRemoveEntityEffect;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class EntityERMBase extends EntityTameable implements IEntityERMMob {
	/**************************************************************/
	/** DataWatcher                                                                                                                                     **/
	/**************************************************************/
	// Absoption効果をクライアント側へ転送するのに使う
	protected static final DataParameter<Float> DW_ABSOPTION = EntityDataManager.createKey(EntityERMBase.class, DataSerializers.FLOAT);
	public void Dw_Absoption(float absoption){
		this.dataWatcher.set(DW_ABSOPTION, absoption);
	}
	public float Dw_Absoption(){
		return this.dataWatcher.get(DW_ABSOPTION);
	}

	// 利き腕(Byte)
	protected static final DataParameter<Integer> DW_DOMINANTARM = EntityDataManager.createKey(EntityERMBase.class, DataSerializers.VARINT);
	public void Dw_DominantArm(int value){
		this.dataWatcher.set(DW_DOMINANTARM, value);
	}
	public int Dw_DominantArm(){
		return this.dataWatcher.get(DW_DOMINANTARM);
	}

	// 紐の持ち主のEntityID。
	protected static final DataParameter<Integer> DW_LEADER = EntityDataManager.createKey(EntityERMBase.class, DataSerializers.VARINT);
	public void Dw_Leader(int value){
		this.dataWatcher.set(DW_LEADER, value);
	}
	public int Dw_Leader(){
		return this.dataWatcher.get(DW_LEADER);
	}

	// アイテムの使用判定、腕毎(Integer)
	protected static final DataParameter<Integer> DW_ITEMUSE = EntityDataManager.createKey(EntityERMBase.class, DataSerializers.VARINT);
	public void Dw_ItemUse(int value){
		this.dataWatcher.set(DW_ITEMUSE, value);
	}
	public int Dw_ItemUse(){
		return this.dataWatcher.get(DW_ITEMUSE);
	}
	public boolean isUsingItem() {
		return Dw_ItemUse() > 0;
	}

	// 保持経験値
	protected static final DataParameter<Integer> DW_EXP = EntityDataManager.createKey(EntityERMBase.class, DataSerializers.VARINT);
	public void Dw_Exp(int value){
		this.dataWatcher.set(DW_EXP, value);
	}
	public int Dw_Exp(){
		return this.dataWatcher.get(DW_EXP);
	}

	// 弓構え
	protected static final DataParameter<Boolean> DW_AIMEBOW =  EntityDataManager.createKey(EntityERMBase.class, DataSerializers.BOOLEAN);
	public void Dw_AimeBow(boolean value){
		this.dataWatcher.set(DW_AIMEBOW, value);
	}
	public boolean Dw_AimeBow(){
		return this.dataWatcher.get(DW_AIMEBOW);
	}

	//
	protected static final DataParameter<Boolean> DW_LOOKCANDY =  EntityDataManager.createKey(EntityERMBase.class, DataSerializers.BOOLEAN);
	public void Dw_LookCandy(boolean value){
		this.dataWatcher.set(DW_LOOKCANDY, value);
	}
	public boolean Dw_LookCandy(){
		return this.dataWatcher.get(DW_LOOKCANDY);
	}

	//
	protected static final DataParameter<Boolean> DW_LOOKINTAREST =  EntityDataManager.createKey(EntityERMBase.class, DataSerializers.BOOLEAN);
	public void Dw_LookIntarest(boolean value){
		this.dataWatcher.set(DW_LOOKINTAREST, value);
	}
	public boolean Dw_LookIntarest(){
		return this.dataWatcher.get(DW_LOOKINTAREST);
	}

	//
	protected static final DataParameter<Boolean> DW_LOOKINTARESTAXIS =  EntityDataManager.createKey(EntityERMBase.class, DataSerializers.BOOLEAN);
	public void Dw_LookIntarestAXIS(boolean value){
		this.dataWatcher.set(DW_LOOKINTARESTAXIS, value);
	}
	public boolean Dw_LookIntarestAXIS(){
		return this.dataWatcher.get(DW_LOOKINTARESTAXIS);
	}

	/**
	 * datawatherの登録を行う
	 */
	public void registerDataWatcher(){
		this.dataWatcher.register(DW_ABSOPTION,0F);
		this.dataWatcher.register(DW_DOMINANTARM,0);
		this.dataWatcher.register(DW_LEADER,0);
		this.dataWatcher.register(DW_ITEMUSE,0);
		this.dataWatcher.register(DW_EXP,0);
		this.dataWatcher.register(DW_AIMEBOW,false);
		this.dataWatcher.register(DW_LOOKCANDY,false);
		this.dataWatcher.register(DW_LOOKINTAREST,false);
		this.dataWatcher.register(DW_LOOKINTARESTAXIS,false);
	}

	/**************************************************************/
	/** ステータス                                                                                                                                     **/
	/**************************************************************/
	// カウンタ
	protected EntityERMCounter cntTicks;
	// 信頼ステータス
	protected EntityERMStatus statusLove;
	// スタミナステータス
	protected EntityERMStatus statusStamina;
	// 空腹ステータス
	protected EntityERMStatus statusFood;
	// キャンディカウンタ
	protected EntityERMStatus statusCandy;
	// 会話カウンタ
	protected EntityERMStatus statusTalk;
	// 経験値
	protected EntityERMStatus statusExp;
	// ステータスリスト
	protected EntityERMStatus[] statusArray = new EntityERMStatus[]{statusLove, statusStamina, statusFood, statusCandy, statusTalk, statusExp};


	// 定数はStaticsへ移動
	protected static final UUID ermUUID = UUID.fromString("e2361272-644a-3028-8416-8536667f0efb");
	protected static final UUID ermUUIDSneak = UUID.fromString("5649cf91-29bb-3a0c-8c31-b170a1045560");
	protected AttributeModifier attCombatSpeed;
	protected AttributeModifier attSneakingSpeed;

	/****************************************************/
	/**  リソース                                                                                                            **/
	/****************************************************/
	// AI設定
	protected EntityAISetting settingAI;
	// テクスチャ設定
	protected EntityTextureSetting settingTexture;
	// ボイス設定
	protected EntityVoiceSetting settingVoice;
	// インベントリ
	protected InventoryERMBase inventory;

	/*****************************************************/
	/** 動作                                                                                                                     **/
	/*****************************************************/
	// アバター
	public EntityERMAvater mobAvatar;
	// 腕振り
	public SwingStatus mstatSwingStatus[];

	/*****************************************************/
	/** フラグ                                                                                                                    **/
	/*****************************************************/
	// 弓
	public boolean statAimeBow;
	// キャンディを見ているか
	protected boolean statLookCandy;
	// インベントリを開いているか
	protected boolean statOpenInventory;

	/*****************************************************/
	/** 首回り                                                                                                                    **/
	/*****************************************************/
	protected boolean looksWithInterest;
	protected boolean looksWithInterestAXIS;
	protected float rotateAngleHead;			// Angle
	protected float prevRotateAngleHead;		// prevAngle


	// ディメンジョン
	public int homeWorld;

	// 使用タイル
	protected int mobTiles[][] = new int[9][3];
	// 使用タイル
	public int mobTile[] = new int[3];
	// 使用タイル
	public TileEntity mobTileEntity;

	// 動的な状態
	protected double statMasterDistanceSq;		// 主との距離、計算軽量化用
	protected Entity statgotcha;				// ワイヤード用

	/**************************************************************************/
	/** 初期化処理                                                                                                                                                               **/
	/**************************************************************************/
	public EntityERMBase(World worldIn){
		super(worldIn);
		if (worldIn != null) {
			// アバターを作る
			mobAvatar = new EntityERMAvater(worldIn, this);
		}

		// 腕振り
		mstatSwingStatus = new SwingStatus[] { new SwingStatus(), new SwingStatus()};
		Dw_DominantArm(this.getDominantArm());

		// 属性
		attCombatSpeed = (new AttributeModifier(ermUUID, "Combat speed boost", this.getConbatSpeed(), 0)).setSaved(false);
		attSneakingSpeed = (new AttributeModifier(ermUUIDSneak, "Sneking speed ampd", this.getSneakingSpeed(), 2)).setSaved(false);

		// インベントリ初期化
		inventory = new InventoryERMBase(this,this.getInventorySize());
		// インベントリ
		statOpenInventory = false;

		// カウンタ
		cntTicks = new EntityERMCounter();
		// Helth
		this.setHealth(20.0F);
		// ステータス設定
		initStatus();

		// 移動用フィジカル設定
		// ドアを開けられるか
		((PathNavigateGround)navigator).setEnterDoors(true);
		// 泳げるか
		((PathNavigateGround)navigator).setCanSwim(true);
	}

	// Entity初期化
	@Override
	public void entityInit(){
		super.entityInit();

		registerDataWatcher();

		// リソース設定
		// AI
		settingAI = new EntityAISetting(this,
				this.getMemory(),
				this.getBaseIntarest(),
				this.getWeightIntarest());

		// 音声
		EntitySoundSetting settingSound = new EntitySoundSetting(this, this.getSoundFile());
		EntityToneSetting settingTone = new EntityToneSetting(this,"", this.getDefaultTone());
		settingVoice = new EntityVoiceSetting(this,settingSound,settingTone, this.getVoiceSetting());
		// テクスチャ
		settingTexture = new EntityTextureSetting(this,
				this.getDressTextureFile(),
				this.getAromorTextureFile(),
				this.getDressSettingFile(),
				new SettingCustomTexture(this.getDressSettingFile(),this.getDefaultTexture()));

		// サイズ設定
		setSize(this.getWidth(), this.getHeight());
		// カスタムモデル作成
		if (ERMResourceManager.instance().StartOn()){
			if (!Mod_ERM.isAliveEntity(this.getClass())){
				Mod_ERM.setEntity(this);
				settingTexture.customTexture(this);
			}
		}
	}

	/**
	 * ステータスバリュー初期化
	 */
	private void initStatus(){
		// 信頼
		statusLove = new EntityERMStatusLove(IEntityERMMob.LOVE_MAX, 1.0F, 10.0F);
		// スタミナ
		statusStamina = new EntityERMStatusStamina(this.MaxStamina(), 0.0F, this.MaxStamina());
		// 空腹
		statusFood = new EntityERMStatusFood(IEntityERMMob.FOOD_MAX, 0.0F,IEntityERMMob.FOOD_MAX);
		// キャンディ
		statusCandy = new EntityERMStatusCounter(IEntityERMMob.CANDY_MAX, 0.0F, IEntityERMMob.CANDY_MAX,Values.KIND_COUNTER.COUNTER_LOVEREDUCE_CANDY);
		// トーク
	    statusTalk= new EntityERMStatusCounter(IEntityERMMob.TALK_MAX, 0.0F, IEntityERMMob.TALK_MAX,Values.KIND_COUNTER.COUNTER_LOVEREDUCE_TALK);
	    // 経験値
	    statusExp = new EntityERMStatusExp(Float.MAX_VALUE, 0.0F, 0.0F);
		// キャンディ記憶の作成
		memoryCandy = new IntarestCandyList(
				this.getMemory(),
				this.getBaseIntarest(),
				this.getWeightIntarest());
	}

	/*************************************************************************************************/
	/** リソースの取り出し                                                                                                                                                                                                                   **/
	/*************************************************************************************************/
	/**
	 * AI情報を取り出す
	 * @return
	 */
	public EntityAISetting settingAI(){
		return settingAI;
	}

	/**
	 * 音声情報を取り出す
	 * @return
	 */
	public EntityVoiceSetting settingVoice(){
		return settingVoice;
	}

	/**
	 * テクスチャ情報を取り出す
	 * @return
	 */
	public EntityTextureSetting settingTexture(){
		return settingTexture;
	}

	/**
	 * カウンタ情報を取り出す
	 * @return
	 */
	public EntityERMCounter getTickCounter() {
		return cntTicks;
	}

	/*************************************************************************************************/
	/** ステータス                                                                                                                                                                                                                                  **/
	/*************************************************************************************************/
	/**
	 *  スタミナの最大値を取得(スタミナベース + (スタミナベース*懐き度/100)
	 * @return 最大スタミナ
	 */
	public float MaxStamina(){
		float stamina = this.getBaseStamina();
		return  stamina + (stamina * Util.RounScale(statusLove.getStatus()/100.0F, 1));
	}

	/**
	 *  興味の最大値を取得(興味ベース + (興味ベース*懐き度/100)
	 * @return
	 */
	public float MaxIntarest(){
		float intarest = this.getBaseIntarest();
		return intarest + (intarest * Util.RounScale(statusLove.getStatus()/100.0F, 1));
	}

	/**
	 * わがまま度を取得(お仕事キャンセルの確率)
	 * @return
	 */
	public int Rebelious(){
		int rebellious = this.getBaseRebelious();
		return rebellious - (int)(rebellious *  Util.RounScale(statusLove.getStatus()/100.0F, 1) / 3);
	}

	/**
	 * 指定したステータスを取り出す
	 * @param kind
	 * @return
	 */
	public EntityERMStatus getStatus(KIND_STATUS kind){
		switch(kind){
		case STATUS_LOVE:
			return this.statusLove;
		case STATUS_STAMINA:
			return this.statusStamina;
		case STATUS_FOOD:
			return this.statusFood;
		case STATUS_CANDY:
			return this.statusCandy;
		case STATUS_TALK:
			return this.statusTalk;
		case STATUS_EXP:
			return this.statusExp;
		}
		return null;
	}

	/***********************************************************************************/
	/** エンティティロジック                                                                                                                                            **/
	/***********************************************************************************/
	/**
	 * lastDamageの取り出し
	 * @return
	 */
	public float getLastDamage() {
		return this.lastDamage;
	}
	/**
	 * アバターの取り出し
	 * @return
	 */
	public IEntityAvatar getAvatarIF()
	{
		return (IEntityAvatar)mobAvatar;
	}

	/**
	 * 体力回復が必要か?
	 * @return
	 */
	public boolean shudHeal() {
		return (this.getHealth() != this.getMaxHealth());
	}

	/**
	 *  お手伝い続行可能かどうか判定する
	 * @return
	 */
	protected KIND_WORK_CANCEL checkDoWork(){
		KIND_WORK_CANCEL ret = KIND_WORK_CANCEL.CANCEL_NO;
		// 空腹確認
		if (statusFood.getStatus() <= 0){
			// 作業続行不可
			ret = KIND_WORK_CANCEL.CANCEL_FOOD;
			if(ModCommon.isDebug)ModLog.log().debug("D LOG cancel work food");
		}else if (statusStamina.getStatus() <= 0){
			// 作業続行不可
			ret = KIND_WORK_CANCEL.CANCEL_STAMINA;
			if(ModCommon.isDebug)ModLog.log().debug("D LOG cancel work stamina");
		}else{
			ret = settingAI.doWorkCheck();
			if(ModCommon.isDebug)ModLog.log().debug("D LOG cancel work :" + ret.toString());
		}
		// 作業続行可能
		return ret;
	}

	/**
	 * 各種フラグのアップデート
	 */
	public void updateMaidFlagsClient() {
		statAimeBow = Dw_AimeBow();
		statLookCandy = Dw_LookCandy();
		looksWithInterest = Dw_LookIntarest();
		looksWithInterestAXIS = Dw_LookIntarestAXIS();
	}

	/**
	 * 現在のAIをクリアする
	 */
	public void clearAI() {
		this.velocityChanged = true;
		this.tasks.taskEntries.clear();
	}

	/**
	 * 紐の持ち主
	 */
	public void updateGotcha() {
		int lid = Dw_Leader();
		if (lid == 0) {
			statgotcha = null;
			return;
		}
		if (statgotcha != null && statgotcha.getEntityId() == lid) {
			return;
		}
		for (int li = 0; li < worldObj.loadedEntityList.size(); li++) {
			if (((Entity)worldObj.loadedEntityList.get(li)).getEntityId() == lid) {
				statgotcha = (Entity)worldObj.loadedEntityList.get(li);
				break;
			}
		}
	}

	public void setGotcha(int pEntityID) {
		this.Dw_Leader(Integer.valueOf(pEntityID));
	}

	public void setGotcha(Entity pEntity) {
		setGotcha(pEntity == null ? 0 : pEntity.getEntityId());
	}

	/**
	 * インベントリを開く
	 * @param pEntityPlayer
	 */
	public void displayGUIMaidInventory(EntityPlayer pEntityPlayer) {
		if (!worldObj.isRemote) {
			GuiHandler.mobServer = this;
			pEntityPlayer.openGui(Mod_ERM.instance, GuiHandler.GUI_ID_INVVENTORY, worldObj,
					(int)posX, (int)posY, (int)posZ);
		}
		else
		{
			GuiHandler.mobClient = this;
		}
	}

	public boolean isWait() {
		return settingAI.isWaite() | statOpenInventory;
	}

	@Override
	public boolean canAttackClass(Class par1Class) {
		// IFFの設定、クラス毎の判定しかできないので使わない。
		return true;
	}

	@Override
	protected int getExperiencePoints(EntityPlayer par1EntityPlayer) {
		return this.Dw_Exp();
	}

	@Override
	public boolean isTamed() {
		return true;
	}

	@Override
	public float getMaximumHomeDistance() {
		return 20f;
	}

	@Override
	public void setHomePosAndDistance(BlockPos par1, int par4) {
		homeWorld = dimension;
		super.setHomePosAndDistance(par1, par4);
	}

	@Override
	public boolean processInteract(EntityPlayer player, EnumHand hand, ItemStack itemStack) {
		if (player.isSneaking()){
			// スニークなで
			if (!worldObj.isRemote){
				//アクティブ<=>ウェイト切り替え
				settingAI.setWaite(!settingAI.isWaite());
			}
			return true;
		}else{
			if(itemStack != null){
				// アイテムを持ってる
				Item item = itemStack.getItem();
				if (item == ERMItem.candy){
					// キャンディ!!
					if(ModCommon.isDebug){ModLog.log().debug("-D Log Interact Candy!!");}
					if (!worldObj.isRemote){
						// キャンディ使用
						useCandy(itemStack);
						// 食事
						((ItemCandy)item).eatCandy(this, itemStack);
						// えっふぇくと
						worldObj.setEntityState(this,(byte)10);
						Mod_ERM.INSTANCE.sendToServer(new MessageCandy(this,this.statusCandy.getStatus(),statusFood.getStatus(),statusLove.getStatus()));
					}
					// キャンディを食べて喜ぶよ
					this.settingVoice.eatCandy(this.getCandyFavoritte(((ItemCandy)item).getCandyName(itemStack)),itemStack);
					// プレイヤーのアイテムを減らす
					Util.decPlayerInventory(player,-1,1);
					if(ModCommon.isDebug){ModLog.log().debug(Util.LogERMStatus(this));}
					if(ModCommon.isDebug){Util.ShowERMStatus(0);}
					return true;
				}else if(item instanceof ItemFood  || item == Items.cake){
					// 食事!
					if (!statusFood.isMax()){
						if (!worldObj.isRemote){
							// いただきます
							int value = 12;
							if (item instanceof ItemFood){
								value = ((ItemFood) item).getHealAmount(itemStack);
							}
							statusFood.addStatus(value);
							// ポーション効果がある場合は効果を付ける
							Util.addPotionEffectItemStack(this,itemStack,this.worldObj);
							// えっふぇくと
							worldObj.setEntityState(this,(byte)11);
							Mod_ERM.INSTANCE.sendToServer(new MessageEat(this,statusFood.getStatus()));
						}
						// ごちそうさまでした
						settingVoice.eatFood(itemStack, VoiceTag.voice_food_eat);
						// プレイヤーのアイテムを減らす
						Util.decPlayerInventory(player,-1,1);
					}else{
						// おなか減ってない
						settingVoice.eatFood(itemStack, VoiceTag.voice_food_satiety);
					}
					return true;
				}else if(item == Items.lead){
					// リード?
					if(ModCommon.isDebug){ModLog.log().debug("-D Log Interact Lead!!");}
					// 紐で繋ぐ
					setGotcha(player.getEntityId());
					statgotcha = player;
					Util.decPlayerInventory(player, -1, 1);
					return true;
				}else if(item == Items.potionitem || item == Items.lingering_potion || item == Items.splash_potion){
					// ポーション効果がある場合は効果を付ける
					Util.addPotionEffectItemStack(this,itemStack,this.worldObj);
					// プレイヤーのアイテムを減らす
					Util.decPlayerInventory(player,-1,1);
					return true;
				}
			}

			getNavigator().clearPathEntity();
			isJumping = false;


//			// メイドインベントリ
//			OwnableEntityHelper.setOwner(this, CommonHelper.getPlayerUUID(par1EntityPlayer));
//			getNavigator().clearPathEntity();
//			isJumping = false;
//			if(!worldObj.isRemote){
//				syncMaidArmorVisible();
//				syncExpBoost();
//			}
//			displayGUIMaidInventory(par1EntityPlayer);
			// 持ち物を見せるよ
			if(ModCommon.isDebug){ModLog.log().debug("-D Log Interact inventory!!");}
			return true;
		}
	}

	/***********************************************************************************/
	/** エフェクト                                                                                                                                            **/
	/***********************************************************************************/
	/**
	 * エフェクト表示
	 * @param s
	 */
	public void showParticleFX(EnumParticleTypes particle) {
		showParticleFX(particle, 1D, 1D, 1D);
	}

	public void showParticleFX(EnumParticleTypes particle, double xpos, double ypos, double zpos) {
		showParticleFX(particle, xpos, ypos, zpos, 0D, 0D, 0D);
	}

	public void showParticleFX(EnumParticleTypes particle,
			double xpos, double ypos, double zpos,
			double xoft, double yoft, double zoft ) {
		for (int i = 0; i < 7; i++) {
			double d6 = rand.nextGaussian() * xpos + xoft;
			double d7 = rand.nextGaussian() * ypos + yoft;
			double d8 = rand.nextGaussian() * zpos + zoft;
			worldObj.spawnParticle(particle,
					(posX + rand.nextFloat() * width * 2.0F) - width,
					posY + 0.5D + rand.nextFloat() * height,
					(posZ + rand.nextFloat() * width * 2.0F) - width, d6, d7, d8);
		}
	}

	/**
	 * ポーション効果のエフェクト
	 */
	@Override
	public void setAbsorptionAmount(float amount) {
		// AbsorptionAmount
		if (amount < 0.0F) {
			amount = 0.0F;
		}
		Dw_Absoption(amount);
	}

	@Override
	public void handleStatusUpdate(byte par1) {
		// worldObj.setEntityState(this, (byte))で指定されたアクションを実行
		switch (par1) {
		case 10:
			// キャンディエフェクト
			IntarestCandyList.AIIntarest candy = memoryCandy.getRecentCandy();
			double oftx,ofty,oftz;
			double weight = 0.3D;
			try{
				switch(this.getCandyFavoritte(candy.candyName)){
				case LOVE:
					ofty = 1.0D * weight;
					oftx = oftz = 1.0D * weight;
					break;
				case YUMMY:
					ofty = 0.6D * weight;
					oftx = oftz = 0.6D * weight;
					break;
				case GOOD:
					ofty = 0.4D * weight;
					oftx = oftz = 0.4D * weight;
					break;
				case BAD:
					ofty = 0.2D * weight;
					oftx = oftz = 0.2D * weight;
					break;
					default:
						oftx = ofty = oftz = 0;
						break;
				}
			}catch(Exception ex){
				oftx = ofty = oftz = 0;
			}
			showParticleFX(EnumParticleTypes.NOTE, posX, posY + height + 0.1D, posZ, oftx, ofty, oftz);
			break;
		case 11:
			// 食事エフェクト
			showParticleFX(EnumParticleTypes.HEART, 0.02D, 0.02D, 0.02D);
			break;
		case 12:
			// 状態変化
			showParticleFX(EnumParticleTypes.FIREWORKS_SPARK, 0.05D, 0.05D, 0.05D);
			break;
		default:
			super.handleStatusUpdate(par1);
		}
	}

	/*********************************************************************************/
	/** キャンディ                                                                                                                                                                                         **/
	/*********************************************************************************/
	// 前回キャンディ
	protected IntarestCandyList memoryCandy;

	public IntarestCandyList CandyMemory(){
		return memoryCandy;
	}

	/**
	 * キャンディの味にどれだけ飽きているか補正値を取得
	 * @param candyName
	 * @return
	 */
	protected float checkCandyIntarest(String candyName, Values.KIND_CANDYTAST favo){
		return this.memoryCandy.candyIntarest(candyName,favo);
	}

	/**
	 * キャンディ使用時の懐き度回復処理
	 * @param candy キャンディ
	 */
	public void useCandy(ItemStack candy){
		if ( statusCandy.getStatus() > 0){
			// 上昇値を取得
			ItemCandy icandy = (ItemCandy)candy.getItem();
			Values.KIND_CANDYTAST fav = this.getCandyFavoritte(((ItemCandy)candy.getItem()).getCandyName(candy));
			float value = icandy.upLove(fav, candy) * 	                               // キャンディ上昇基本値
					      this.getWeightLove() *                                        // 対象の懐きやすさ
					      this.checkCandyIntarest(icandy.getCandyName(candy),fav);     // キャンディへの飽きによる補正値を取得
			// 懐き度を上昇させる
			statusLove.addStatus(value);
			// キャンディ使用回数を減らす
			statusCandy.addStatus(-1.0F);
		}
	}

	/*********************************************************************************/
	/** 会話                                                                                                                                                                                                 **/
	/*********************************************************************************/
	/**
	 * 会話が可能か判定
	 * @return true:セリフテキスト再生、false:固定テキスト再生
	 */
	public boolean canTalk(){
		if ( statusTalk.getStatus() > 0){return false;}

		// 懐き度を取得
		float love = statusLove.getStatus();
		if(love < 130.0F){
			// 懐き度が130未満の場合、会話回数の減少と懐き度の上昇を行う
			// 上昇値を取得
			float value = 2 * this.getWeightLove();
			// 懐き度を上昇させる
			statusLove.addStatus(value);
			// 会話回数を減らす
			statusTalk.addStatus(-1.0F);
		}
		return true;
	}

	/*********************************************************************************/
	/** 死亡                                                                                                                                                                                                **/
	/*********************************************************************************/
	@Override
    public void onDeath(DamageSource cause)
    {
        super.onDeath(cause);
        // アイテムドロップ
        this.dropLoot(true, 0, cause);
		// TODO: 死因を表示
    }

	/**
	 * モブ死亡
	 */
	@Override
    protected void onDeathUpdate(){
		super.onDeathUpdate();
		if (!worldObj.isRemote) {
			// TODO: 紐につながれている
			// 何かに乗っている場合はおろす
			if (this.getRidingEntity() != null){
				this.dismountRidingEntity();
			}
		}
	}

	@Override
	public void setDead() {
		// 何もしないように
		if (statgotcha != null&&mobAvatar != null) {
			// 首紐をドロップ
			EntityItem entityitem = new EntityItem(worldObj, statgotcha.posX, statgotcha.posY, statgotcha.posZ, new ItemStack(Items.string));
			worldObj.spawnEntityInWorld(entityitem);
			statgotcha = null;
		}
	}

	@Override
	protected void dropFewItems(boolean par1, int par2) {
		// インベントリの中身をばらまく
		getInventory().dropAllItems();
	}

	@Override
	protected Item getDropItem() {
		// アイテムドロップなし
		return null;
	}

	/***********************************************************************************/
	/** 初期化                                                                                                                                                                                                  **/
	/***********************************************************************************/
	/**
	 * 主人を設定する
	 */
	public void initOwner(EntityPlayer player) {
		// 懐き済み
        this.setTamed(true);
        this.setHealth(20.0F);
        this.setOwnerId(player.getUniqueID());
        this.playTameEffect(true);
	}

	@Override
	public void setFlag(int par1, boolean par2) {
		super.setFlag(par1, par2);
	}

	/**
	 * 属性の設定
	 */
	@Override
	protected void applyEntityAttributes() {
		// 初期パラメーター
		super.applyEntityAttributes();
		// モブごとの処理
		this.applyEntityAttributes(this);
	}

	protected String tagName = "";
	public String EntityName() {
		// TODO 自動生成されたメソッド・スタブ
		return Util.StringisEmptyOrNull(tagName)?this.EntityName():tagName;
	}
	public void onGuiClosed() {
		// TODO 自動生成されたメソッド・スタブ

	}
	public void onGuiOpened() {
		// TODO 自動生成されたメソッド・スタブ

	}

	/***********************************************************************************/
	/** 位置                                                                                                                                                                                                     **/
	/***********************************************************************************/
	protected PathEntity prevPathEntity = null;

	public PathEntity getPrevPathEntity() {
		return prevPathEntity;
	}

	@Override
	public void setLocationAndAngles(double x, double y, double z, float yaw, float pitch){
		super.setLocationAndAngles(x, y, z, yaw, pitch);
	}

	@Override
	public void applyEntityCollision(Entity par1Entity) {
		// 閉所接触回避用
		super.applyEntityCollision(par1Entity);
	}

	@Override
	public void updateRidden() {
		super.updateRidden();
	}

	/**
	 * 埋葬対策コピー
	 */
	@Override
	protected boolean pushOutOfBlocks(double x, double y, double z) {
		// モブごとに対策
		return this.pushOutOfBlocks(this, x, y, z);
	}
	/**
	 * 埋葬対策コピー
	 */
	private boolean isBlockTranslucent(int par1, int par2, int par3) {
		IBlockState iState = worldObj.getBlockState(new BlockPos(par1, par2, par3));
		return iState.getBlock().isNormalCube(iState);
	}

	/***********************************************************************************/
	/** 首回り                                                                                                                                                                                                  **/
	/***********************************************************************************/
	/**
	 * 首回り
	 * @param f
	 */
	public void setLooksWithInterest(boolean f) {
		if (looksWithInterest != f) {
			looksWithInterest = f;
			// TODO:
			boolean lIntarest = Dw_LookIntarest();
			boolean lIntarestAXIS = Dw_LookIntarestAXIS();
			lIntarest = looksWithInterest ? lIntarest : !lIntarest;
			lIntarestAXIS = looksWithInterestAXIS ? lIntarestAXIS : !lIntarestAXIS;
			Dw_LookIntarest(lIntarest);
			Dw_LookIntarestAXIS(lIntarestAXIS);
		}
	}
	public boolean getLooksWithInterest() {
		// TODO:
		looksWithInterest = Dw_LookIntarest();
		looksWithInterestAXIS = Dw_LookIntarestAXIS();

		return looksWithInterest;
	}
	public float getInterestedAngle(float f) {
		return (prevRotateAngleHead + (rotateAngleHead - prevRotateAngleHead) * f) * ((looksWithInterestAXIS ? 0.08F : -0.08F) * (float)Math.PI);
	}

	/***********************************************************************************/
	/** 腕                                                                                                                                                                                                           **/
	/***********************************************************************************/
	/**
	 * 利き腕のリロードタイム
	 */
	public SwingStatus getSwingStatusDominant() {
		return mstatSwingStatus[this.getDominantArm()];
	}
	public SwingStatus getSwingStatus(int pindex) {
		return mstatSwingStatus[pindex];
	}

	public boolean getSwinging() {
		return getSwinging(Dw_DominantArm());
	}

	public boolean getSwinging(int pArm) {
		return mstatSwingStatus[pArm].isSwingInProgress;
	}

	/**
	 * ポーション等による腕振りモーションの速度補正
	 */
	public int getSwingSpeedModifier() {
		// TODO:
		if (isPotionActive(Potion.getPotionFromResourceLocation("haste"))) {
			return 6 - (1 + getActivePotionEffect(Potion.getPotionFromResourceLocation("haste")).getAmplifier()) * 1;
		}

		if (isPotionActive(Potion.getPotionFromResourceLocation("mining_fatigue"))) {
			return 6 + (1 + getActivePotionEffect(Potion.getPotionFromResourceLocation("mining_fatigue")).getAmplifier()) * 2;
		}
		return 6;
	}

	/**
	 * 利き腕を設定
	 * @param pindex
	 */
	private void setDominantArm(int pindex) {
		if(ModCommon.isDebug){ModLog.log().debug("-D Log ");}
		if (mstatSwingStatus.length <= pindex) return;
		for (SwingStatus lss : mstatSwingStatus) {
			lss.index = lss.lastIndex = -1;
		}
		Dw_DominantArm(this.getDominantArm());
		if(ModCommon.isDebug){ModLog.log().debug("-D Log DominantArm (" + pindex +")");}
	}

	@Override
	public float getSwingProgress(float par1) {
		for (SwingStatus lswing : mstatSwingStatus) {
			lswing.getSwingProgress(par1);
		}
		return getSwingStatusDominant().onGround;
	}

	/***********************************************************************************/
	/** インベントリ                                                                                                                                                                                         **/
	/***********************************************************************************/
	/**
	 * 現在の装備品
	 */
	public ItemStack getCurrentEquippedItem() {
		return getInventory().getCurrentItem();
	}

	/**
	 * インベントリを取り出す
	 * @return
	 */
	public InventoryERMBase getInventory(){
		return inventory;
	}

	/**
	 *  インベントリが変更されました。
	 */
	public void onInventoryChanged() {
		// TODO:
		//getNextEquipItem();
	}

	/**
	 * インベントリにある次の装備品を選択
	 */
	public void getNextEquipItem() {
		if (worldObj.isRemote) {
			return;
		}

		if (settingAI.doWork()){
			// 作業用アイテムがワークインベントリにセットされていない場合、セットする
			ItemStack workTool = settingAI.getNextTool();
			if (workTool != null && !inventory.hasWorkTool(workTool)){
				inventory.setNextWorkTool(workTool);
			}
			ItemStack[] workItem = settingAI.getNextItem();
			if (workItem!= null){
				// 作業用インベントリを更新
				inventory.setNextWorkItem(workItem);
			}
		}
	}

	/**
	 * 装備アイテムを更新する
	 * @param pArm
	 * @param pIndex
	 */
	public void setEquipItem(int pArm, int pIndex) {
		getInventory().currentMainItem = pIndex;
		int li = mstatSwingStatus[pArm].index;
		if (li != pIndex) {
			if (li > -1) {
				getInventory().setCurrentSlot(li);
			}
			if (pIndex > -1) {
				getInventory().setCurrentSlot(pIndex);
			}
			mstatSwingStatus[pArm].setSlotIndex(pIndex);
		}
	}
	public void setEquipItem(int pIndex) {
		setEquipItem(this.getDominantArm(), pIndex);
	}


	@Override
	public ItemStack getHeldItem(EnumHand hand) {
		if (hand == EnumHand.MAIN_HAND){
			return this.inventory.getWorkdTool();
		}else{
			return this.inventory.getCurrentItem();
		}
	}

	@Override
	public Iterable<ItemStack> getArmorInventoryList() {
		return Arrays.asList(getInventory().armorInventory);
	}

	@Override
	public Iterable<ItemStack> getHeldEquipment() {
		return Arrays.asList(new ItemStack[]{getCurrentEquippedItem(), null});
	}

	@Override
	public ItemStack getItemStackFromSlot(EntityEquipmentSlot slotIn) {
		if (slotIn == EntityEquipmentSlot.MAINHAND) {
			return getHeldItem(EnumHand.MAIN_HAND);
		} else if (slotIn.getSlotType() == EntityEquipmentSlot.Type.ARMOR) {
			return inventory.armorInventory[slotIn.getIndex()];
		} else {
			return null;
		}
	}

	@Override
	public void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack) {
		if (slotIn.func_188452_c() == 0) {
			getInventory().setInventorySlotContents(slotIn.getIndex(), stack);
		} else if (slotIn.func_188452_c() < 5) {
			getInventory().setInventorySlotContents(slotIn.getIndex() + getInventory().maxInventorySize(), stack);
		} else {
			// TODO What was this used for?
		}
	}

	/***********************************************************************************/
	/** 戦闘系                                                                                                                                                                                                **/
	/***********************************************************************************/
	/**
	 * ダメージコントロール
	 */
	public boolean isBlocking() {
		return getSwingStatusDominant().isBlocking();
	}

	@Override
	public void onKillEntity(EntityLivingBase entity) {
		// ころしたよー
		settingVoice.killEntity(entity);
		setAttackTarget(null);
		if (settingAI.doWork()){
			// お手伝い中はお手伝い用の処理を優先
			//settingAI.onKillEntity(entity);
		}else{
			// aiごとの処理
			//settingAI.onKillEntity(entity);
		}

	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float damage) {
		if (worldObj.isRemote) {
			return false;
		}

		// AI ごとの処理
		float dmg = damage;
		settingAI.Damage(source);

		// ノーダメージ
		if (dmg == 0){return false;}

		Entity target = source.getEntity();
		if(super.attackEntityFrom(source, dmg)) {
			//契約者の名前チェックはマルチ用
			if (target != null) {
				if (this.getIFF(target)) {
					return true;
				}
			} else if (getInventory().getCurrentItem() == null) {
				return true;
			}
			return true;
		}
		return false;
	}

	/**
	 * 自分が攻撃するときの設定
	 */
	@Override
	public boolean attackEntityAsMob(Entity entity) {
		if (settingAI.doWork()){
			// AIごとの処理
			return true;//settingAI.attackEntityAsMob(entity);
		}else{
			// モブごとの処理
			return super.attackEntityAsMob(entity);
		}
	}

	@Override
	protected void damageEntity(DamageSource source, float damageOrg) {
		super.damageEntity(source, damageOrg);

		if (damageOrg < getHealth()){
			// 生きているならボイス再生
			settingVoice.damageVoice(source,source.getSourceOfDamage());
		}

		// 被ダメ
		float llasthealth = getHealth();
		if (damageOrg > 0) {
			getAvatarIF().W_damageEntity(source, damageOrg);
			// ダメージに応じて信頼度減少
			this.reduceLoveCauseDamage(this,source,damageOrg);
			// ダメージに応じて行動変更
			settingAI.Damage(source);
		}
	}

	@Override
	protected void damageArmor(float pDamage) {
		getInventory().damageArmor(pDamage);
		getAvatarIF().W_damageArmor(pDamage);
	}

	@Override
	public int getTotalArmorValue() {
		return mobAvatar.getTotalArmorValue();
	}

	@Override
	protected float applyArmorCalculations(DamageSource par1DamageSource, float par2) {
		return getAvatarIF().W_applyArmorCalculations(par1DamageSource, par2);
	}

	@Override
	protected float applyPotionDamageCalculations(DamageSource par1DamageSource, float par2) {
		return getAvatarIF().W_applyPotionDamageCalculations(par1DamageSource, par2);
	}

	/***********************************************************************************/
	/** アイテム使用                                                                  **/
	/***********************************************************************************/
	/**
	 * 対象にポーションを使う。
	 */
	public void usePotionTotarget(EntityLivingBase entityliving) {
		ItemStack itemstack = getInventory().getCurrentItem();
		if (itemstack != null && itemstack.getItem() instanceof ItemPotion) {
			// ポーション効果の発動
			itemstack.stackSize--;
			List list = PotionUtils.getEffectsFromStack(itemstack);
			if (list != null) {
				PotionEffect potioneffect;
				for (Iterator iterator = list.iterator(); iterator.hasNext(); entityliving.addPotionEffect(new PotionEffect(potioneffect))) {
					potioneffect = (PotionEffect)iterator.next();
				}
			}
			if(itemstack.stackSize <= 0) {
				getInventory().CurrentSlotContents(null);
			}
			getInventory().addItemStackToInventory(new ItemStack(Items.glass_bottle));
		}
	}

	/**
	 * ポーション効果
	 */
	@Override
	public float getAbsorptionAmount() {
		return Dw_Absoption();
	}

	/**
	 * ポーションエフェクト
	 */
	@Override
	protected void onNewPotionEffect(PotionEffect par1PotionEffect) {
		super.onNewPotionEffect(par1PotionEffect);
		if (this.getOwner() instanceof EntityPlayerMP) {
			((EntityPlayerMP)this.getOwner()).playerNetServerHandler.sendPacket(new SPacketEntityEffect(getEntityId(), par1PotionEffect));
		}
	}

	@Override
	protected void onChangedPotionEffect(PotionEffect par1PotionEffect, boolean par2) {
		super.onChangedPotionEffect(par1PotionEffect, par2);
		// TODO:必要かどうかのチェック
	}

	@Override
	protected void onFinishedPotionEffect(PotionEffect par1PotionEffect) {
		super.onFinishedPotionEffect(par1PotionEffect);
		if (this.getOwner()instanceof EntityPlayerMP) {
			((EntityPlayerMP)this.getOwner()).playerNetServerHandler.sendPacket(new SPacketRemoveEntityEffect(getEntityId(), par1PotionEffect.getPotion()));
		}
	}

	/**
	 * 弓構えを更新
	 */
	public void updateAimebow() {
		boolean lflag = (mobAvatar != null && getAvatarIF().isUsingItemLittleMaid()) || statAimeBow;
		Dw_AimeBow(lflag);
	}

	/***********************************************************************************/
	/** onUpdate                                                                      **/
	/***********************************************************************************/
	/**
	 * バージョンアップで水浮き専用に
	 */
	@Override
	protected void updateAITick() {
		super.updateAITick();
		//		// TODO 自動生成されたメソッド・スタブ
//		if(getNavigator().getPath()!=null)
//			prevPathEntity = getNavigator().getPath();
//		super.updateAITick();
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		//		int litemuse = 0;
//
//		// 主の確認など
//		EntityLivingBase mstatMasterEntity = getOwner();
//		if (mstatMasterEntity != null) {
//			statMasterDistanceSq = getDistanceSqToEntity(mstatMasterEntity);
//		}
//
//		// リアルタイム変動値をアップデート
//		if (worldObj.isRemote) {
//			// クライアント側
//			setDominantArm(Dw_DominantArm());
//			updateMaidFlagsClient();
//			updateGotcha();
//			// 腕の挙動関連
//			litemuse = Dw_ItemUse();
//			for (int li = 0; li < mstatSwingStatus.length; li++) {
//				ItemStack lis = mstatSwingStatus[li].getItemStack(this);
//				if ((litemuse & (1 << li)) > 0 && lis != null) {
//					mstatSwingStatus[li].setItemInUse(lis, lis.getMaxItemUseDuration(), this);
//				} else {
//					mstatSwingStatus[li].stopUsingItem(this);
//				}
//			}
//		} else {
//			boolean lf;
//			// サーバー側
//			// 移動速度の変更
//			IAttributeInstance latt = getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
//			// 属性を解除
//			latt.removeModifier(attCombatSpeed);
//
//			// スニーキング判定
//			latt.removeModifier(attSneakingSpeed);
//			if ((onGround && isSneaking()) || isUsingItem()) {
//				latt.applyModifier(attSneakingSpeed);
//			}
//		}
//
//		super.onUpdate();
//
//		// SwingUpdate
//		SwingStatus lmss1 = getSwingStatusDominant();
//		prevSwingProgress = mobAvatar.prevSwingProgress = lmss1.prevSwingProgress;
//		swingProgress = mobAvatar.swingProgress = lmss1.swingProgress;
//		swingProgressInt = mobAvatar.swingProgressInt = lmss1.swingProgressInt;
//		isSwingInProgress = mobAvatar.isSwingInProgress = lmss1.isSwingInProgress;
//
//		// Aveterの毎時処理
//		if (mobAvatar != null) {
//			getAvatarIF().getValue();
//			mobAvatar.onUpdate();
//		}
//
//		// くびかしげ
//		prevRotateAngleHead = rotateAngleHead;
//		if (getLooksWithInterest()) {
//			rotateAngleHead = rotateAngleHead + (1.0F - rotateAngleHead) * 0.4F;
//		} else {
//			rotateAngleHead = rotateAngleHead + (0.0F - rotateAngleHead) * 0.4F;
//		}
//
//		// 腕の挙動に関する処理
//		litemuse = 0;
//		for (int li = 0; li < mstatSwingStatus.length; li++) {
//			mstatSwingStatus[li].onUpdate(this);
//			if (mstatSwingStatus[li].isUsingItem()) {
//				litemuse |= (1 << li);
//			}
//		}
//
//		// 標準変数に対する数値の代入
//		SwingStatus lmss = getSwingStatusDominant();
//		prevSwingProgress = mobAvatar.prevSwingProgress = lmss.prevSwingProgress;
//		swingProgress = mobAvatar.swingProgress = lmss.swingProgress;
//		swingProgressInt = mobAvatar.swingProgressInt = lmss.swingProgressInt;
//		isSwingInProgress = mobAvatar.isSwingInProgress = lmss.isSwingInProgress;
//
//		// 持ち物の確認
//		if (getInventory().inventoryChanged) {
//			onInventoryChanged();
//			getInventory().inventoryChanged = false;
//		}
//
//		if (!worldObj.isRemote) {
//			// サーバー側処理
//			// アイテム使用状態の更新
//			Dw_ItemUse(litemuse);
//
//			// 弓構え
//			statAimeBow &= !getSwingStatusDominant().canAttack();
//			// 構えの更新
//			updateAimebow();
//
//			// 自分より大きなものは乗っけない（イカ除く）
//			if (getControllingPassenger() != null && !(getControllingPassenger() instanceof EntitySquid)) {
//				if (height * width < getControllingPassenger().height * getControllingPassenger().width) {
//					if (getControllingPassenger() instanceof EntityLivingBase) {
//						attackEntityFrom(DamageSource.causeMobDamage((EntityLivingBase)getControllingPassenger()), 0);
//					}
//					getControllingPassenger().dismountRidingEntity();
//					return;
//				}
//			}
//		}
//
//		// 紐で拉致
//		if(statgotcha != null) {
//			double d = statgotcha.getDistanceSqToEntity(this);
//			if (getAttackTarget() == null) {
//				// インコムごっこ用
//				if (d > 4D) {
//					getNavigator().clearPathEntity();
//					getLookHelper().setLookPositionWithEntity(statgotcha, 15F, 15F);
//				}
//				if (d > 12.25D) {
//					getNavigator().tryMoveToXYZ(statgotcha.posX, statgotcha.posY, statgotcha.posZ, 1.0F);
//					getLookHelper().setLookPositionWithEntity(statgotcha, 15F, 15F);
//				}
//			}
//			if (d > 25D) {
//				double d1 = statgotcha.posX - posX;
//				double d3 = statgotcha.posZ - posZ;
//				double d5 = 0.125D / (Math.sqrt(d1 * d1 + d3 * d3) + 0.0625D);
//				d1 *= d5;
//				d3 *= d5;
//				motionX += d1;
//				motionZ += d3;
//			}
//			if (d > 42.25D) {
//				double d2 = statgotcha.posX - posX;
//				double d4 = statgotcha.posZ - posZ;
//				double d6 = 0.0625D / (Math.sqrt(d2 * d2 + d4 * d4) + 0.0625D);
//				d2 *= d6;
//				d4 *= d6;
//				statgotcha.motionX -= d2;
//				statgotcha.motionZ -= d4;
//			}
//			if (d > 64D) {
//				Dw_Leader(0);
//				statgotcha = null;
//				// TODO: 紐音
//				//playSound("random.drr");
//			}
//			if(rand.nextInt(16) == 0) {
//				List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().expand(8D, 8D, 8D));
//				for (int k = 0; k < list.size(); k++) {
//					Entity entity = (Entity)list.get(k);
//					if (!(entity instanceof EntityMob)) {
//						continue;
//					}
//					EntityMob entitymob = (EntityMob)entity;
//					if (entitymob.getAttackTarget() == statgotcha) {
//						entitymob.setAttackTarget(this);
//						entitymob.getNavigator().setPath(getNavigator().getPath(), entitymob.moveForward);
//					}
//				}
//			}
//		}
//		// モブごとの処理
//		mob.onUpdate(this);
//
//		// TODO: AIごとの処理
	}

	@Override
	public void onEntityUpdate(){
		super.onEntityUpdate();
//		//音声再生
//		if(worldObj.isRemote){
//			// 再生
//			settingVoice.playSound();
//			//worldObj.playSound(posX, posY, posZ, new SoundEvent(new ResourceLocation(LittleMaidReengaged.DOMAIN+":"+sname)), getSoundCategory(), getSoundVolume(), lpitch, false);
//		}
//		// モブごとの処理
//		mob.onEntityUpdate(this);
//
//		// TODO: AIごとの処理
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
//
//		// カウントアップ
//		cntTicks.updateTicks();
//
//		// 各ステータスのアップデート処理
//		for(EntityERMStatus status : statusArray){
//			status.onUpdate(this, cntTicks);
//		}
//
//		try {
//			super.onLivingUpdate();
//		} catch (NullPointerException exception) {
//			exception.printStackTrace();
//		}
//
//		// 水中関連
//		((PathNavigateGround)navigator).setCanSwim(true);
//
//		if(!worldObj.isRemote) getInventory().decrementAnimations();
//
//
//		// 埋葬対策
//		boolean grave = true;
//		grave &= pushOutOfBlocks(posX - (double)width * 0.34999999999999998D, getCollisionBoundingBox().minX, posZ + (double)width * 0.34999999999999998D);
//		grave &= pushOutOfBlocks(posX - (double)width * 0.34999999999999998D, getCollisionBoundingBox().minY, posZ - (double)width * 0.34999999999999998D);
//		grave &= pushOutOfBlocks(posX + (double)width * 0.34999999999999998D, getCollisionBoundingBox().minY, posZ - (double)width * 0.34999999999999998D);
//		grave &= pushOutOfBlocks(posX + (double)width * 0.34999999999999998D, getCollisionBoundingBox().minY, posZ + (double)width * 0.34999999999999998D);
//		if (grave && onGround) {
//			jump();
//		}
//
//		// 近接監視の追加はここ
//		// アイテムの回収
//		if (!worldObj.isRemote) {
//			List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().expand(1.0D, 0.0D, 1.0D));
//			if (list != null) {
//				for (int i = 0; i < list.size(); i++) {
//					Entity entity = (Entity)list.get(i);
//					if (!entity.isDead) {
//						if (entity instanceof EntityArrow) {
//							// 特殊回収
//							((EntityArrow)entity).canBePickedUp = PickupStatus.ALLOWED;
//						}
//						entity.onCollideWithPlayer(mobAvatar);
//					}
//				}
//			}
//		}
	}

	/*********************************************************************************/
	/** override                                                                    **/
	/*********************************************************************************/
	@Override
	protected boolean canDespawn() {
		// 出たら消えない
		return false;
	}

	@Override
	public boolean isBreedingItem(ItemStack itemStack) {
		// キャンディ
		return itemStack == null?false:itemStack.getItem() == ERMItem.candy;
	}

	@Override
	public EntityAgeable createChild(EntityAgeable var1) {
		// お子さんはいません
		return null;
	}

	@Override
	protected PathNavigate getNewNavigator(World worldIn) {
		return new PathNavigatorERMBase(this, worldIn);
	}

	/*********************************************************************************/
	/** 再生                                                                    **/
	/*********************************************************************************/
//	// 効果音の設定
//	@Override
//	protected SoundEvent getHurtSound() {
//		if(getHealth()>0f) playLittleMaidSound(getMaidDamegeSound(), true);
//		return null;
//	}
//
//	@Override
//	protected SoundEvent getDeathSound() {
//		playLittleMaidSound(EnumSound.death, true);
//		return null;
//	}
//
//	@Override
//	public void playLivingSound() {
//		if (!worldObj.isRemote) return;
//		// 普段の声
//		//LMM_LittleMaidMobNX.Debug("DEBUG INFO=tick %d", livingSoundTick);
//		//livingSoundTick--;
//		if(getAttackTarget()!=null/* || Math.random() > 0.3*/) return;
//		EnumSound so = EnumSound.Null;
//		if (getHealth() < 10)
//			so = EnumSound.living_whine;
//		else /*if (rand.nextFloat() < maidSoundRate) */{
//			if (mstatTime > 23500 || mstatTime < 1500) {
//				so = EnumSound.living_morning;
//			} else if (mstatTime < 12500) {
//				if (isContract()) {
//					BiomeGenBase biomegenbase = worldObj.getBiomeGenForCoords(getPosition());
//					TempCategory ltemp = biomegenbase.getTempCategory();
//					if (ltemp == TempCategory.COLD) {
//						so = EnumSound.living_cold;
//					} else if (ltemp == TempCategory.WARM) {
//						so = EnumSound.living_hot;
//					} else {
//						so = EnumSound.living_daytime;
//					}
//					if (worldObj.isRaining()) {
//						if (biomegenbase.getEnableSnow()) {
//							so = EnumSound.living_snow;
//						} else {
//							so = EnumSound.living_rain;
//						}
//					}
//				} else {
//					so = EnumSound.living_daytime;
//				}
//			} else {
//				so = EnumSound.living_night;
//			}
//		}
//
//		//if(livingSoundTick<=0){
//			LittleMaidReengaged.Debug("id:%d LivingSound:%s", getEntityId(), worldObj == null ? "null" : worldObj.isRemote ? "Client" : "Server");
////			if(!worldObj.isRemote)
////				playLittleMaidSound(so, false);
////			else
//		// LivingSoundの再生調整はonEntityUpdateで行う
//		playSound(so, true);
//		//	livingSoundTick = 1;
//		//}
//	}
//
//
//	/**
//	 * 音声再生用。
//	 * 通常の再生ではネットワーク越しになるのでその対策。
//	 */
//	public void playLittleMaidSound(EnumSound enumsound, boolean force) {
//		// 音声の再生
//		if (enumsound == EnumSound.Null) return;
////		if (!force && rand.nextFloat() > LittleMaidReengaged.cfg_voiceRate) return;
//		if (!worldObj.isRemote) {
//			// Server
////			if((LMM_LittleMaidMobNX.cfg_ignoreForceSound || !force) && new Random().nextInt(LMM_LittleMaidMobNX.cfg_soundPlayChance)!=0) return;
//			LittleMaidReengaged.Debug("id:%d-%s, seps:%04x-%s", getEntityId(), "Server",  enumsound.index, enumsound.name());
//			byte[] lbuf = new byte[] {
//					0, 0, 0, 0,
//					0
//			};
//			NetworkHelper.setIntToPacket(lbuf, 0, enumsound.index);
//			lbuf[4] = (byte) (force ? 1 : 0);
//			syncNet(EnumPacketMode.CLIENT_PLAY_SOUND, lbuf);
//		}
//	}

	/***********************************************************************************/
	/** NBT処理                                                                                                                                                                                               **/
	/***********************************************************************************/
    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        super.writeEntityToNBT(tagCompound);
        // インベントリ
        tagCompound.setTag("INVENTORY", getInventory().writeToNBT(new NBTTagList()));// インベントリ

        // ステータス
        statusLove.writeNBT(tagCompound,"LOVE");                                 // 信頼
        statusStamina.writeNBT(tagCompound,"STAMINA");                           // スタミナ
        statusFood.writeNBT(tagCompound,"FOOD");                                 // 空腹
        statusCandy.writeNBT(tagCompound,"CANDY");                               // キャンディ残り
        statusTalk.writeNBT(tagCompound,"TALK");                                 // 会話数
        statusExp.writeNBT(tagCompound, "EXP");                                  // 経験値
        cntTicks.writeNBT(tagCompound, "COUNTER");                               // カウンタ


        // リソース
        settingAI.writeNBT(tagCompound);                                         // AI設定
        settingVoice.writeNBT(tagCompound);                                      // 音声設定
        settingTexture.writeNBT(tagCompound);                                    // テクスチャ設定
        memoryCandy.writeNBT(tagCompound, "MEMORY_CANDY");                       // キャンディ

		// HomePosition
		tagCompound.setInteger("HOMEX", getPosition().getX());
		tagCompound.setInteger("HOMEY", getPosition().getY());
		tagCompound.setInteger("HOMEZ", getPosition().getZ());
		tagCompound.setInteger("HOMEWORLD", homeWorld);

        // 利き腕
		tagCompound.setInteger("DOMINANTARM", Dw_DominantArm());

		// 何かに乗っているか
		boolean isRide = isRiding();
		tagCompound.setBoolean(ModCommon.MOD_ID + ":RIDING", isRide);
		if (isRide) {
			tagCompound.setIntArray(ModCommon.MOD_ID + ":LASTPOSITION", new int[]{(int) posX, (int) posY, (int) posZ});
		}

		// Tiles
		NBTTagCompound lnbt = new NBTTagCompound();
		tagCompound.setTag("TILES", lnbt);
		for (int li = 0; li < mobTiles.length; li++) {
			if (mobTiles[li] != null) {
				lnbt.setIntArray(String.valueOf(li), mobTiles[li]);
			}
		}

    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound tagCompound)
    {
        super.readEntityFromNBT(tagCompound);
        String work;

        // インベントリ
        getInventory().readFromNBT(tagCompound.getTagList("INVENTORY", 10));

        // ステータス
        statusLove.readNBT(tagCompound,"LOVE");                                // 信頼
        statusStamina.readNBT(tagCompound,"STAMINA");                          // スタミナ
        statusFood.readNBT(tagCompound,"FOOD");                                // 空腹
        statusCandy.readNBT(tagCompound,"CANDY");                              // キャンディ残り
        statusTalk.readNBT(tagCompound,"TALK");                                // 会話数
        statusExp.readNBT(tagCompound, "EXP");                                 // 経験値
        cntTicks.readNBT(tagCompound, "COUNTER");                              // カウンタ


        // リソース
        settingAI.readNBT(tagCompound);                                        // AI設定
        settingVoice.readNBT(tagCompound);                                     // 音声設定
        settingTexture.readNBT(tagCompound);                                   // テクスチャ設定

        memoryCandy.readNBT(tagCompound, "MEMORY_CANDY");                      // キャンディ

        // 更新の必要があるリソースを更新
        if (settingVoice.getSound().isUpdate()){
        	settingVoice.getSound().loadSound(this);
        }
        if (settingVoice.getTone().isUpdate()){
        	settingVoice.getTone().updateTone(this);
        }
        if(settingTexture.doUpdateSetting()){
        	settingTexture.customTexture(this);
        }


		Dw_DominantArm(tagCompound.getInteger("DOMINANTARM"));
		if (mstatSwingStatus.length <= Dw_DominantArm()) {
			Dw_DominantArm(0);
		}

		// HomePosition
		int lhx = tagCompound.getInteger("HOMEX");
		int lhy = tagCompound.getInteger("HOMEY");
		int lhz = tagCompound.getInteger("HOMEZ");
		setHomePosAndDistance(new BlockPos(lhx, lhy, lhz),(int)getMaximumHomeDistance());
		homeWorld = tagCompound.getInteger("HOEMWORLD");

		// Tiles
		NBTTagCompound lnbt = tagCompound.getCompoundTag("TILES");
		for (int li = 0; li < mobTiles.length; li++) {
			int ltile[] = lnbt.getIntArray(String.valueOf(li));
			mobTiles[li] = ltile.length > 0 ? ltile : null;
		}


		// 肩車
		boolean isRide = tagCompound.getBoolean(ModCommon.MOD_ID + ":RIDING");
		if (isRide) {
			int[] lastPosition = tagCompound.getIntArray(ModCommon.MOD_ID + ":LASTPOSITION");
			setPosition(lastPosition[0], lastPosition[1], lastPosition[2]);
		}

		// TODO: インベントリ変更
		onInventoryChanged();

		if(!worldObj.isRemote){
			Mod_ERM.INSTANCE.sendToServer(new MessageEat(this,statusFood.getStatus()));
			Mod_ERM.INSTANCE.sendToServer(new MessageCandy(this,statusCandy.getStatus(),statusFood.getStatus(),statusLove.getStatus()));
		}
    }


    /**
     * 登録名を取得する
     * @return 登録名
     */
    public abstract String getRegisterName();

}
