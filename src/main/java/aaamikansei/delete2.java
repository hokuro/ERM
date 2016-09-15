//package aaamikansei;
//
//package net.blacklab.lmr.entity;
//
//import static net.blacklab.lmr.util.Statics.*;
//
//import java.lang.reflect.Method;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//import java.util.UUID;
//import java.util.concurrent.CopyOnWriteArrayList;
//
//import net.blacklab.lib.minecraft.item.ItemUtil;
//import net.blacklab.lib.vevent.VEventBus;
//import net.blacklab.lmr.LittleMaidReengaged;
//import net.blacklab.lmr.achievements.AchievementsLMRE;
//import net.blacklab.lmr.api.event.EventLMRE;
//import net.blacklab.lmr.api.item.IItemSpecialSugar;
//import net.blacklab.lmr.client.entity.EntityLittleMaidAvatarSP;
//import net.blacklab.lmr.client.sound.SoundLoader;
//import net.blacklab.lmr.client.sound.SoundRegistry;
//import net.blacklab.lmr.entity.ai.EntityAILMAttackArrow;
//import net.blacklab.lmr.entity.ai.EntityAILMAttackOnCollide;
//import net.blacklab.lmr.entity.ai.EntityAILMAvoidPlayer;
//import net.blacklab.lmr.entity.ai.EntityAILMBeg;
//import net.blacklab.lmr.entity.ai.EntityAILMBegMove;
//import net.blacklab.lmr.entity.ai.EntityAILMCollectItem;
//import net.blacklab.lmr.entity.ai.EntityAILMFindBlock;
//import net.blacklab.lmr.entity.ai.EntityAILMFleeRain;
//import net.blacklab.lmr.entity.ai.EntityAILMFollowOwner;
//import net.blacklab.lmr.entity.ai.EntityAILMJumpToMaster;
//import net.blacklab.lmr.entity.ai.EntityAILMOpenDoor;
//import net.blacklab.lmr.entity.ai.EntityAILMRestrictOpenDoor;
//import net.blacklab.lmr.entity.ai.EntityAILMRestrictRain;
//import net.blacklab.lmr.entity.ai.EntityAILMSwimming;
//import net.blacklab.lmr.entity.ai.EntityAILMTracerMove;
//import net.blacklab.lmr.entity.ai.EntityAILMWait;
//import net.blacklab.lmr.entity.ai.EntityAILMWander;
//import net.blacklab.lmr.entity.ai.EntityAILMWatchClosest;
//import net.blacklab.lmr.entity.experience.ExperienceHandler;
//import net.blacklab.lmr.entity.experience.ExperienceUtil;
//import net.blacklab.lmr.entity.maidmodel.EquippedStabilizer;
//import net.blacklab.lmr.entity.maidmodel.IModelCaps;
//import net.blacklab.lmr.entity.maidmodel.IModelEntity;
//import net.blacklab.lmr.entity.maidmodel.ModelConfigCompound;
//import net.blacklab.lmr.entity.maidmodel.TextureBox;
//import net.blacklab.lmr.entity.maidmodel.TextureBoxBase;
//import net.blacklab.lmr.entity.mode.EntityModeBase;
//import net.blacklab.lmr.entity.mode.EntityMode_Playing;
//import net.blacklab.lmr.entity.pathnavigate.PathNavigatorLittleMaid;
//import net.blacklab.lmr.inventory.InventoryLittleMaid;
//import net.blacklab.lmr.item.ItemTriggerRegisterKey;
//import net.blacklab.lmr.network.EnumPacketMode;
//import net.blacklab.lmr.network.GuiHandler;
//import net.blacklab.lmr.network.LMRNetwork;
//import net.blacklab.lmr.util.Counter;
//import net.blacklab.lmr.util.EntityCaps;
//import net.blacklab.lmr.util.EnumSound;
//import net.blacklab.lmr.util.IFF;
//import net.blacklab.lmr.util.SwingStatus;
//import net.blacklab.lmr.util.TriggerSelect;
//import net.blacklab.lmr.util.helper.CommonHelper;
//import net.blacklab.lmr.util.helper.ItemHelper;
//import net.blacklab.lmr.util.helper.NetworkHelper;
//import net.blacklab.lmr.util.helper.OwnableEntityHelper;
//import net.blacklab.lmr.util.manager.EntityModeManager;
//import net.blacklab.lmr.util.manager.ModelManager;
//import net.minecraft.block.state.IBlockState;
//import net.minecraft.client.Minecraft;
//import net.minecraft.entity.Entity;
//import net.minecraft.entity.EntityAgeable;
//import net.minecraft.entity.EntityCreature;
//import net.minecraft.entity.EntityList;
//import net.minecraft.entity.EntityLivingBase;
//import net.minecraft.entity.SharedMonsterAttributes;
//import net.minecraft.entity.ai.EntityAIBase;
//import net.minecraft.entity.ai.EntityAILeapAtTarget;
//import net.minecraft.entity.ai.EntityAILookIdle;
//import net.minecraft.entity.ai.EntityAIPanic;
//import net.minecraft.entity.ai.EntityAITasks;
//import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
//import net.minecraft.entity.ai.EntityAITempt;
//import net.minecraft.entity.ai.attributes.AttributeModifier;
//import net.minecraft.entity.ai.attributes.IAttributeInstance;
//import net.minecraft.entity.item.EntityItem;
//import net.minecraft.entity.monster.EntityCreeper;
//import net.minecraft.entity.monster.EntityMob;
//import net.minecraft.entity.passive.EntityChicken;
//import net.minecraft.entity.passive.EntityHorse;
//import net.minecraft.entity.passive.EntitySquid;
//import net.minecraft.entity.passive.EntityTameable;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.entity.player.EntityPlayerMP;
//import net.minecraft.entity.projectile.EntityArrow;
//import net.minecraft.entity.projectile.EntityArrow.PickupStatus;
//import net.minecraft.entity.projectile.EntitySnowball;
//import net.minecraft.init.Items;
//import net.minecraft.inventory.EntityEquipmentSlot;
//import net.minecraft.item.Item;
//import net.minecraft.item.ItemAxe;
//import net.minecraft.item.ItemPotion;
//import net.minecraft.item.ItemStack;
//import net.minecraft.nbt.NBTTagCompound;
//import net.minecraft.nbt.NBTTagList;
//import net.minecraft.network.datasync.DataParameter;
//import net.minecraft.network.datasync.DataSerializers;
//import net.minecraft.network.datasync.EntityDataManager;
//import net.minecraft.network.play.server.SPacketEntityEffect;
//import net.minecraft.network.play.server.SPacketEntityEquipment;
//import net.minecraft.network.play.server.SPacketRemoveEntityEffect;
//import net.minecraft.pathfinding.PathEntity;
//import net.minecraft.pathfinding.PathNavigate;
//import net.minecraft.pathfinding.PathNavigateGround;
//import net.minecraft.pathfinding.PathNodeType;
//import net.minecraft.pathfinding.WalkNodeProcessor;
//import net.minecraft.potion.Potion;
//import net.minecraft.potion.PotionEffect;
//import net.minecraft.potion.PotionUtils;
//import net.minecraft.profiler.Profiler;
//import net.minecraft.tileentity.TileEntity;
//import net.minecraft.util.DamageSource;
//import net.minecraft.util.EnumHand;
//import net.minecraft.util.EnumParticleTypes;
//import net.minecraft.util.ResourceLocation;
//import net.minecraft.util.SoundEvent;
//import net.minecraft.util.math.AxisAlignedBB;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.MathHelper;
//import net.minecraft.util.math.Vec3d;
//import net.minecraft.util.text.Style;
//import net.minecraft.util.text.TextComponentString;
//import net.minecraft.util.text.TextFormatting;
//import net.minecraft.util.text.translation.I18n;
//import net.minecraft.world.EnumDifficulty;
//import net.minecraft.world.World;
//import net.minecraft.world.WorldServer;
//import net.minecraft.world.biome.BiomeGenBase;
//import net.minecraft.world.biome.BiomeGenBase.TempCategory;
//import net.minecraftforge.fml.relauncher.Side;
//import net.minecraftforge.fml.relauncher.SideOnly;
//
//public class EntityLittleMaid extends EntityTameable implements IModelEntity {
//
//		/** EntityPlayer と EntityTameable で17番がかぶっているため、EntityPlayer側を28へ移動。 */
//	protected static final DataParameter<Float> dataWatch_AbsorptionAmount	= EntityDataManager.createKey(EntityLittleMaidAvatarMP.class, DataSerializers.FLOAT);
//
////	protected long maidContractLimit;		// 契約失効日
//	protected int maidContractLimit;		// 契約期間
//	public long maidAnniversary;			// 契約日UIDとして使用
//	private int maidDominantArm;			// 利き腕、1Byte
//	/** テクスチャ関連のデータを管理 **/
//	public ModelConfigCompound textureData;
//	public Map<String, EquippedStabilizer> maidStabilizer = new HashMap<String, EquippedStabilizer>();
//
//	public float getLastDamage(){
//		return lastDamage;
//	}
//
//	public int fencerDefDetonateTick = 0;
//
////	public int jumpTicks;
//
//	public InventoryLittleMaid maidInventory;
//	public EntityPlayer maidAvatar;
//	public EntityCaps maidCaps;	// Client側のみ
//
//	public List<EntityModeBase> maidEntityModeList;
//	public Map<Integer, EntityAITasks[]> maidModeList;
//	public Map<String, Integer> maidModeIndexList;
//	public int maidMode;		// 2Byte
//	public boolean maidTracer;
//	public boolean maidFreedom;
//	public boolean maidWait;
//	public int homeWorld;
//	protected int maidTiles[][] = new int[9][3];
//	public int maidTile[] = new int[3];
//	public TileEntity maidTileEntity;
//
//	// 動的な状態
//	protected EntityPlayer mstatMasterEntity;	// 主
//	protected double mstatMasterDistanceSq;		// 主との距離、計算軽量化用
//	protected Entity mstatgotcha;				// ワイヤード用
//	protected boolean mstatBloodsuck;
//	protected boolean mstatClockMaid;
//	// マスク判定
//	protected int mstatMaskSelect;
//	// 追加の頭部装備
//	protected boolean mstatCamouflage;
//	protected boolean mstatPlanter;
////	protected boolean isMaidChaseWait;
//	protected int mstatWaitCount;
//	protected int mstatTime;
//	protected Counter maidOverDriveTime;
//	protected boolean mstatFirstLook;
//	protected boolean mstatLookSuger;
//	protected Counter workingCount;
//	protected int mstatPlayingRole;
//	protected int mstatWorkingInt;
//	protected String mstatModeName;
//	protected boolean mstatOpenInventory;
//
//	protected int ticksSinceLastDamage = 0;
//
//	// 腕振り
//	public SwingStatus mstatSwingStatus[];
//	public boolean mstatAimeBow;
//	// 首周り
//	private boolean looksWithInterest;
//	private boolean looksWithInterestAXIS;
//	private float rotateAngleHead;			// Angle
//	private float prevRotateAngleHead;		// prevAngle
//
//	public float defaultWidth;
//	public float defaultHeight;
//
//	/**
//	 * 個体ごとに値をバラつかせるのに使う。
//	 */
//	public float entityIdFactor;
//
//	public boolean weaponFullAuto;	// 装備がフルオート武器かどうか
//	public boolean weaponReload;	// 装備がリロードを欲しているかどうか
//	public boolean maidCamouflage;
//
//	// 音声
////	protected LMM_EnumSound maidAttackSound;
//	private EnumSound maidDamegeSound;
//	protected int maidSoundInterval;
//	protected float maidSoundRate;
//
//	// クライアント専用音声再生フラグ
//	private CopyOnWriteArrayList<EnumSound> playingSound = new CopyOnWriteArrayList<EnumSound>();
//
//	// 実験用
//	private int firstload = 100;
//	public String statusMessage = "";
//
//	// AI
//	public EntityAITempt aiTempt;
//	public EntityAILMBeg aiBeg;
//	public EntityAILMBegMove aiBegMove;
//	public EntityAILMOpenDoor aiOpenDoor;
//	public EntityAILMRestrictOpenDoor aiCloseDoor;
//	public EntityAILMAvoidPlayer aiAvoidPlayer;
//	public EntityAILMFollowOwner aiFollow;
//	public EntityAILMAttackOnCollide aiAttack;
//	public EntityAILMAttackArrow aiShooting;
//	public EntityAILMCollectItem aiCollectItem;
//	public EntityAILMRestrictRain aiRestrictRain;
//	public EntityAILMFleeRain aiFreeRain;
//	public EntityAILMWander aiWander;
//	public EntityAILMJumpToMaster aiJumpTo;
//	public EntityAILMFindBlock aiFindBlock;
//	public EntityAILMTracerMove aiTracer;
//	public EntityAILMSwimming aiSwiming;
//	public EntityAIPanic aiPanic;
//
//	public EntityAILMWatchClosest aiWatchClosest;
//	// ActiveModeClass
//	private EntityModeBase maidActiveModeClass;
//	public Profiler aiProfiler;
//
//	//モデル
//	protected String textureNameMain;
//	protected String textureNameArmor;
//
//	public int playingTick = 0;
//
//	public boolean isWildSaved = false;
//
//	// サーバ用テクスチャ処理移行フラグ
//	private boolean isMadeTextureNameFlag = false;
//
//	protected int maidArmorVisible = 15;
//
//	private static final int[] ZBOUND_BLOCKOFFS = new int[]{  1,  1,  0, -1, -1, -1,  0,  1};
//	private static final int[] XBOUND_BLOCKOFFS = new int[]{  0, -1, -1, -1,  0,  1,  1,  1};
//	public int DEBUGCOUNT = 0;
//	private boolean isInsideOpaque = false;
//	protected Counter registerTick;
//	protected String registerMode;
//
//	// NX5 レベル関連
//	protected float maidExperience = 0;				// 経験値
//	protected ExperienceHandler experienceHandler;	// 経験値アクション制御
//	private int gainExpBoost = 1;					// 取得経験値倍率
//
//	public EntityLittleMaid(World par1World) {
//		if (par1World != null ) {
//			if(par1World.isRemote)
//			{
//				maidAvatar = new EntityLittleMaidAvatarSP(par1World, this);
//			}
//			else
//			{
//				try{
//					maidAvatar = new EntityLittleMaidAvatarMP(par1World, this);
//				}catch(Throwable throwable){
//					throwable.printStackTrace();
//					maidAvatar = null;
//					setDead();
//					return;
//				}
//			}
//		}
//
//		setExperienceHandler(new ExperienceHandler(this));
//
//	}
//
//	public IEntityLittleMaidAvatar getAvatarIF()
//	{
//		return (IEntityLittleMaidAvatar)maidAvatar;
//	}
//
//	public void onSpawnWithEgg() {
//		// テクスチャーをランダムで選択
//		String ls;
//		if (LittleMaidReengaged.cfg_isFixedWildMaid) {
//			ls = "default_Orign";
//		} else {
//			ls = ModelManager.instance.getRandomTextureString(rand);
//		}
//		textureData.setTextureInitServer(ls);
//		LittleMaidReengaged.Debug("init-ID:%d, %s:%d", getEntityId(), textureData.textureBox[0].textureName, textureData.getColor());
////		setTexturePackIndex(textureData.getColor(), textureData.textureIndex);
//		setTextureNameMain(textureData.textureBox[0].textureName);
//		setTextureNameArmor(textureData.textureBox[1].textureName);
////		recallRenderParamTextureName(textureModelNameForClient, textureArmorNameForClient);
//		if(!isContract()) {
//			setMaidMode("Wild");
//			onSpawnWild();
//		}
//	}
//
//	protected void onSpawnWild() {
//		// 野生メイドの色設定処理
//		int nsize = 0;
//		int avaliableColor[] = new int[16];
//		TextureBoxBase box = getModelConfigCompound().textureBox[0];
//		for (int i=0; i<16; i++) {
//			if ((box.wildColor & 1<<i) > 0) {
//				avaliableColor[nsize++] = i;
//			}
//		}
//		setColor(avaliableColor[rand.nextInt(nsize)]);
//	}
//
//
//	public void initModeList() {
//		// AI
//		aiBeg = new EntityAILMBeg(this, 8F);
//		aiBegMove = new EntityAILMBegMove(this, 1.0F);
//		aiOpenDoor = new EntityAILMOpenDoor(this, true);
//		aiCloseDoor = new EntityAILMRestrictOpenDoor(this);
//		aiAvoidPlayer = new EntityAILMAvoidPlayer(this, 1.0F, 3);
//		aiFollow = new EntityAILMFollowOwner(this, 1.0F, 36D, 25D, 81D);
//		aiAttack = new EntityAILMAttackOnCollide(this, 1.0F, true);
//		aiShooting = new EntityAILMAttackArrow(this);
//		aiCollectItem = new EntityAILMCollectItem(this, 1.0F);
//		aiRestrictRain = new EntityAILMRestrictRain(this);
//		aiFreeRain = new EntityAILMFleeRain(this, 1.0F);
//		aiWander = new EntityAILMWander(this, 1.0F);
//		aiJumpTo = new EntityAILMJumpToMaster(this);
//		aiFindBlock = new EntityAILMFindBlock(this);
//		aiSwiming = new EntityAILMSwimming(this);
//		aiPanic = new EntityAIPanic(this, 2.0F);
//		aiTracer = new EntityAILMTracerMove(this);
//		aiSit = new EntityAILMWait(this);
//
//		aiWatchClosest = new EntityAILMWatchClosest(this, EntityLivingBase.class, 10F);
//
//		// TODO:これいらなくね？
//		aiProfiler = worldObj != null && worldObj.theProfiler != null ? worldObj.theProfiler : null;
//
//		// 動作モード用のTasksListを初期化
//		EntityAITasks ltasks[] = new EntityAITasks[2];
//		ltasks[0] = new EntityAITasks(aiProfiler);
//		ltasks[1] = new EntityAITasks(aiProfiler);
//
//		// default
//		ltasks[0].addTask(1, aiSwiming);
//		ltasks[0].addTask(2, aiSit);
//		ltasks[0].addTask(3, aiJumpTo);
//		ltasks[0].addTask(4, aiFindBlock);
//		ltasks[0].addTask(5, aiAttack);
//		ltasks[0].addTask(6, aiShooting);
//		//ltasks[0].addTask(8, aiPanic);
//		ltasks[0].addTask(10, aiBeg);
//		ltasks[0].addTask(11, aiBegMove);
//		ltasks[0].addTask(20, aiAvoidPlayer);
//		ltasks[0].addTask(21, aiFreeRain);
//		ltasks[0].addTask(22, aiCollectItem);
//		// 移動用AI
//		ltasks[0].addTask(30, aiTracer);
//		ltasks[0].addTask(31, aiFollow);
//		ltasks[0].addTask(32, aiWander);
//		ltasks[0].addTask(33, new EntityAILeapAtTarget(this, 0.3F));
//		// Mutexの影響しない特殊行動
//		ltasks[0].addTask(40, aiCloseDoor);
//		ltasks[0].addTask(41, aiOpenDoor);
//		ltasks[0].addTask(42, aiRestrictRain);
//		// 首の動き単独
//		ltasks[0].addTask(51, aiWatchClosest);
//		ltasks[0].addTask(52, new EntityAILookIdle(this));
//
//		// 追加分
//		for (EntityModeBase ieml : maidEntityModeList) {
//			ieml.addEntityMode(ltasks[0], ltasks[1]);
//		}
//	}
//
//	@Override
//	protected PathNavigate getNewNavigator(World worldIn) {
//		return new PathNavigatorLittleMaid(this, worldIn);
//	}
//
//	public void addMaidMode(EntityAITasks[] peaiTasks, String pmodeName, int pmodeIndex) {
//		maidModeList.put(pmodeIndex, peaiTasks);
//		maidModeIndexList.put(pmodeName, pmodeIndex);
//	}
//
//
//	public int getMaidModeInt() {
//		return maidMode;
//	}
//
//	public String getMaidModeString() {
//		if (!isContract()) {
//			return getMaidModeString(maidMode);
//		} else if (!isRemainsContract()) {
//			return "Strike";
//		} else if (isMaidWait()) {
//			return "Wait";
//		} else if (isPlaying()) {
//			return "Playing";
//		} else {
//			String ls = getMaidModeString(maidMode);
////			if (maidOverDriveTime.isEnable()) {
////				ls = "D-" + ls;
////			} else
//			if (isTracer()&&isFreedom()) {
//				ls = "T-" + ls;
//			} else if (isFreedom()) {
//				ls = "F-" + ls;
//			}
//			return ls;
//		}
//	}
//
//	public String getMaidModeString(int pindex) {
//		// モード名称の獲得
//		String ls = "";
//		for (Entry<String, Integer> le : maidModeIndexList.entrySet()) {
//			if (le.getValue() == pindex) {
//				ls = le.getKey();
//				break;
//			}
//		}
//		return ls;
//	}
//
//	public EntityModeBase getMaidActiveModeClass() {
//		return maidActiveModeClass;
//	}
//
//	public void setMaidActiveModeClass(EntityModeBase maidActiveModeClass) {
//		this.maidActiveModeClass = maidActiveModeClass;
//	}
//
//	public boolean setMaidMode(String pname) {
//		return setMaidMode(pname, false);
//	}
//
//	public boolean setMaidMode(String pname, boolean pplaying) {
//		if (!maidModeIndexList.containsKey(pname)) {
//			return false;
//		}
//		return setMaidMode(maidModeIndexList.get(pname), pplaying);
//	}
//
//	public boolean setMaidMode(int pindex) {
//		return setMaidMode(pindex, false);
//	}
//
///*
//	public boolean isInWater() {
//		IBlockState state = worldObj.getBlockState(getPosition());
//		return inWater ? true : state.getBlock().getMaterial(state) == Material.water;
//	}
//*/
//
//	public int[][] getMaidTiles() {
//		return maidTiles;
//	}
//
//	public void setMaidArmorVisible(int i){
//		if(i<0) i=0;
//		if(i>15)i=15;
//		maidArmorVisible = i;
//	}
//
//	public void setMaidArmorVisible(boolean a, boolean b, boolean c, boolean d){
//		setMaidArmorVisible((a?1:0)<<3 | (b?1:0)<<2 | (c?1:0)<<1 | (d?1:0));
//	}
//
//	public boolean isArmorVisible(int index){
//		if(index<0||index>3) return true;
//		return ((maidArmorVisible>>(3-index)) & 0x1) != 0;
//	}
//
//	public void syncMaidArmorVisible() {
//		syncNet(EnumPacketMode.SYNC_ARMORFLAG, new byte[]{(byte) maidArmorVisible});
//	}
//
//	/**
//	 * Client用
//	 * 経験値ブースト値の取得
//	 */
//	public void requestExpBoost() {
//		syncNet(EnumPacketMode.SERVER_REQUEST_BOOST, new byte[]{});
//	}
//
//	/**
//	 * 経験値ブーストの同期
//	 */
//	public void syncExpBoost() {
//		byte b[] = new byte[] {
//				0, 0, 0, 0
//		};
//		NetworkHelper.setIntToPacket(b, 0, getExpBooster());
//		syncNet(EnumPacketMode.SYNC_EXPBOOST, b);
//	}
//
//	public void syncModelNames() {
//		byte main[] = new byte[getModelNameMain().length()+1];
//		main[0] = 0;
//		NetworkHelper.setStrToPacket(main, 1, getModelNameMain());
//		syncNet(EnumPacketMode.SYNC_MODEL, main);
//
//		byte armor[] = new byte[getModelNameArmor().length()+1];
//		armor[0] = 1;
//		NetworkHelper.setStrToPacket(armor, 1, getModelNameArmor());
//		syncNet(EnumPacketMode.SYNC_MODEL, armor);
//	}
//
//	public void syncNet(EnumPacketMode pMode, byte[] contents) {
//		if(worldObj.isRemote){
//			LMRNetwork.sendToServerWithEntityID(pMode, this, contents);
//		}else{
//			LMRNetwork.sendToAllClientWithEntityID(pMode, this, contents);
//		}
//	}
//
//	public boolean setMaidMode(int pindex, boolean pplaying) {
//		// モードに応じてAIを切り替える
//		velocityChanged = true;
//		if (!maidModeList.containsKey(pindex)) return false;
//		if (maidMode == pindex) return true;
//
//		if (pplaying) {
//
//		} else {
//			mstatWorkingInt = pindex;
//		}
//		mstatModeName = getMaidModeString(pindex);
//		maidMode = pindex;
//		dataWatcher.set(EntityLittleMaid.dataWatch_Mode, maidMode);
//		EntityAITasks[] ltasks = maidModeList.get(pindex);
//
//		// AIを根底から書き換える
//		if (ltasks.length > 0 && ltasks[0] != null) {
//			setMaidModeAITasks(ltasks[0], tasks);
//		} else {
//			setMaidModeAITasks(null, tasks);
//		}
//		if (ltasks.length > 1 && ltasks[1] != null) {
//			setMaidModeAITasks(ltasks[1], targetTasks);
//		} else {
//			setMaidModeAITasks(null, targetTasks);
//		}
//
//		// モード切替に応じた処理系を確保
//		maidAvatar.stopActiveHand();
//		setSitting(false);
//		setSneaking(false);
//		setActiveModeClass(null);
//		aiJumpTo.setEnable(true);
////		aiFollow.setEnable(true);
//		aiAttack.setEnable(true);
//		aiShooting.setEnable(false);
//		aiAvoidPlayer.setEnable(true);
////		aiWander.setEnable(maidFreedom);
//		setBloodsuck(false);
//		clearTilePosAll();
//		for (int li = 0; li < maidEntityModeList.size(); li++) {
//			EntityModeBase iem = maidEntityModeList.get(li);
//			if (iem.setMode(maidMode)) {
//				setActiveModeClass(iem);
//				aiFollow.setMinDist(iem.getRangeToMaster(0));
//				aiFollow.setMaxDist(iem.getRangeToMaster(1));
//				break;
//			}
//		}
//		getNextEquipItem();
//
//		return true;
//	}
//
//	protected void setMaidModeAITasks(EntityAITasks pTasksSRC, EntityAITasks pTasksDEST) {
//		// 既存のAIを削除して置き換える。
//		// 動作をクリア
//		List<EntityAIBase> originAIs = new ArrayList<EntityAIBase>();
//		for (Iterator<EntityAITaskEntry> iterator = pTasksDEST.taskEntries.iterator(); iterator.hasNext(); ) {
//			EntityAITaskEntry lEntry = iterator.next();
//			originAIs.add(lEntry.action);
//		}
//		for (EntityAIBase pAiBase: originAIs) {
//			pTasksDEST.removeTask(pAiBase);
//		}
//
//		// 動作追加
//		for (Iterator<EntityAITaskEntry> iterator = pTasksSRC.taskEntries.iterator(); iterator.hasNext(); ) {
//			pTasksDEST.taskEntries.add(iterator.next());
//		}
//	}
//	public static ArrayList<EntityAITaskEntry> getEntityAITasks_taskEntries(EntityAITasks task)
//	{
//		return (ArrayList<EntityAITaskEntry>) task.taskEntries;
//	}
//	public static ArrayList<EntityAITaskEntry> getEntityAITasks_executingTaskEntries(EntityAITasks task)
//	{
//		return (ArrayList<EntityAITaskEntry>) task.taskEntries;
//	}
//
//	/**
//	 * 適用されているモードクラス
//	 */
//	public EntityModeBase getActiveModeClass() {
//		return getMaidActiveModeClass();
//	}
//
//	public void setActiveModeClass(EntityModeBase pEntityMode) {
//		setMaidActiveModeClass(pEntityMode);
//	}
//
//	public boolean isActiveModeClass() {
//		return getMaidActiveModeClass() != null;
//	}
//
//	public Counter getWorkingCount() {
//		return workingCount;
//	}
//
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
///*
//	@Override
//	protected SoundEvent getLivingSound() {
//		// 普段の声
//		//LMM_LittleMaidMobNX.Debug("DEBUG INFO=tick %d", livingSoundTick);
//		//livingSoundTick--;
//		return null;//"dummy.living";
//	}
//*/
//
//	public EnumSound getMaidDamegeSound() {
//		return maidDamegeSound;
//	}
//
//	public void setMaidDamegeSound(EnumSound maidDamegeSound) {
//		this.maidDamegeSound = maidDamegeSound;
//	}
//
//	/**
//	 * 文字列指定による音声再生
//	 */
//	public void playSound(String pname) {
//		// TODO SoundEventに関しては，FMLで登録方法を提供してけれみたいなissueがあった気がするのでしばらく保留．
//		playSound(pname, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
//	}
//
//	/**
//	 * 文字列指定による音声再生
//	 */
//	public void playSound(String pName, float pitch) {
//		LittleMaidReengaged.Debug("REQUESTED PLAYING SOUND: %s", pName);
////		if(!worldObj.isRemote) {
//			SoundEvent sEvent = SoundEvent.soundEventRegistry.getObject(new ResourceLocation(pName));
//			if (sEvent != null) {
//				if (worldObj.isRemote)
//					LittleMaidReengaged.Debug("PLAYING SOUND EVENT-%s", sEvent.getSoundName().toString());
//				playSound(sEvent, 1, pitch);
//			} else if (worldObj.isRemote) {
//				// ClientOnlyなダメ元
//				sEvent = new SoundEvent(new ResourceLocation(pName));
//				LittleMaidReengaged.Debug("PLAYING SOUND EVENT-%s", sEvent.getSoundName().toString());
//				try {
//					playSound(sEvent, 1, pitch);
//				} catch (Exception exception) {}
//			}
////		}
//	}
//
//	/**
//	 * ネットワーク対応音声再生
//	 */
//	public void playSound(EnumSound enumsound, boolean force) {
//		if (worldObj.isRemote && enumsound!=EnumSound.Null && maidSoundInterval <= 0) {
//			if (!force) {
//				if(Math.random() > LittleMaidReengaged.cfg_voiceRate) {
//					return;
//				}
//			}
//			playingSound.add(enumsound);
//			maidSoundInterval = 10;
////			float lpitch = LMM_LittleMaidMobNX.cfg_VoiceDistortion ? (rand.nextFloat() * 0.2F) + 0.95F : 1.0F;
////			LMM_LittleMaidMobNX.proxy.playLittleMaidSound(worldObj, posX, posY, posZ, s, getSoundVolume(), lpitch, false);
//		}
//	}
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
//	@Override
//	public void onKillEntity(EntityLivingBase par1EntityLiving) {
//		super.onKillEntity(par1EntityLiving);
//		if (isBloodsuck()) {
//			playLittleMaidSound(EnumSound.laughter, false);
//		} else {
//			setAttackTarget(null);
//		}
//	}
//
//	@Override
//	protected boolean canDespawn() {
//		// デスポーン判定
//		return isTamed()||hasCustomName() ? false : LittleMaidReengaged.cfg_canDespawn;
//	}
//
//
//
//	@Override
//	public AxisAlignedBB getEntityBoundingBox() {
//		return super.getEntityBoundingBox();
//	}
//
//	@Override
//	public void setDead() {
//		if (mstatgotcha != null&&maidAvatar != null) {
//			// 首紐をドロップ
//			EntityItem entityitem = new EntityItem(worldObj, mstatgotcha.posX, mstatgotcha.posY, mstatgotcha.posZ, new ItemStack(Items.string));
//			worldObj.spawnEntityInWorld(entityitem);
//			mstatgotcha = null;
//		}
//		super.setDead();
//	}
//
//	/**
//	 * 読み込み領域内のメイドさんの数
//	 */
//	public int getMaidCount() {
//		int lj = 0;
//		for (int li = 0; li < worldObj.loadedEntityList.size(); li++) {
//			if (worldObj.loadedEntityList.get(li) instanceof EntityLittleMaid) {
//				lj++;
//			}
//		}
//		return lj;
//	}
//
//	@Override
//	public EntityAgeable createChild(EntityAgeable var1) {
//		// お子さんの設定
//		return null;
//	}
//
//	@Override
//	public void handleStatusUpdate(byte par1) {
//		// worldObj.setEntityState(this, (byte))で指定されたアクションを実行
//		switch (par1) {
//		case 10:
//			// 不機嫌
//			showParticleFX(EnumParticleTypes.SMOKE_NORMAL, 0.02D, 0.02D, 0.02D);
//			break;
//		case 11:
//			// ゴキゲン
//			double a = getContractLimitDays() / 7D;
//			double d6 = a * 0.3D;
//			double d7 = a;
//			double d8 = a * 0.3D;
//
//			worldObj.spawnParticle(EnumParticleTypes.NOTE, posX, posY + height + 0.1D, posZ, d6, d7, d8);
//			break;
//		case 12:
//			// 自由行動
//			showParticleFX(EnumParticleTypes.REDSTONE, 0.5D, 0.5D, 0.5D, 1.0D, 1.0D, 1.0D);
//			break;
//		case 13:
//			// 不自由行動
//			showParticleFX(EnumParticleTypes.SMOKE_NORMAL, 0.02D, 0.02D, 0.02D);
//			break;
//		case 14:
//			// トレーサー
//			showParticleFX(EnumParticleTypes.EXPLOSION_NORMAL, 0.3D, 0.3D, 0.3D, 0.0D, 0.0D, 0.0D);
//			break;
//		case 17:
//			// トリガー登録
//			showParticleFX(EnumParticleTypes.FIREWORKS_SPARK, 0.05D, 0.05D, 0.05D);
//			break;
//		default:
//			super.handleStatusUpdate(par1);
//		}
//	}
//
//	// ポーション効果のエフェクト
//	public void setAbsorptionAmount(float par1) {
//		// AbsorptionAmount
//		if (par1 < 0.0F) {
//			par1 = 0.0F;
//		}
//
//		dataWatcher.set(EntityLittleMaid.dataWatch_Absoption, Float.valueOf(par1));
//	}
//	public float getAbsorptionAmount() {
//		return dataWatcher.get(EntityLittleMaid.dataWatch_Absoption);
//	}
//
//
//	public int colorMultiplier(float pLight, float pPartialTicks) {
//		// 発光処理用
//		int lbase = 0, i = 0, j = 0, k = 0, x = 0, y = 0;
//		if (maidOverDriveTime.isDelay()) {
//			j = 0x00df0000;
//			if (maidOverDriveTime.isEnable()) {
//				x = 128;
//			}else{
//				x = (int) (128 - maidOverDriveTime.getValue() * (128f / LittleMaidReengaged.cfg_maidOverdriveDelay));
//			}
//		}
//		if (registerTick.isDelay()) {
//			k = 0x0000df00;
//			if (registerTick.isEnable()) {
//				y = 128;
//			}else{
//				y = (int) (128 - registerTick.getValue() * (128f / 20));
//			}
//		}
//		i = x==0 ? (y>=128 ? y : 0) : (y==0 ? x : Math.min(x, y));
//		lbase = i << 24 | j | k;
//
//		if (isActiveModeClass()) {
//			lbase = lbase | getActiveModeClass().colorMultiplier(pLight, pPartialTicks);
//		}
//
//		return lbase;
//	}
//
//
//	// AI関連
//	protected boolean isAIEnabled() {
//		// 新AI対応
//		return true;
//	}
//
//
//
//
//
//
//	public boolean getLooksWithInterest() {
//		looksWithInterest = (dataWatcher.get(EntityLittleMaid.dataWatch_Flags) & dataWatch_Flags_looksWithInterest) > 0;
//		looksWithInterestAXIS = (dataWatcher.get(EntityLittleMaid.dataWatch_Flags) & dataWatch_Flags_looksWithInterestAXIS) > 0;
//
//		return looksWithInterest && !isHeadMount();
//	}
//
//	public float getInterestedAngle(float f) {
//		if (maidInventory.armorInventory[3] != null) {
//			return 0f;
//		}
//		return (prevRotateAngleHead + (rotateAngleHead - prevRotateAngleHead) * f) * ((looksWithInterestAXIS ? 0.08F : -0.08F) * (float)Math.PI);
//	}
//
//
//
//	public void updateAITasks()
//	{
//		super.updateAITasks();
//		tasks.onUpdateTasks();
////		if(++playingTick==2){
//			for (EntityModeBase ieml : maidEntityModeList) {
//				ieml.updateAITick(getMaidModeInt());
//			}
////			playingTick = 0;
////		}
//	}
//
//
//
//
//
//
//	public Counter getMaidOverDriveTime() {
//		return maidOverDriveTime;
//	}
//
//	private void superLivingUpdate() {
//		if(onGround) isJumping = false;
//
//		if (newPosRotationIncrements > 0)
//		{
//			double d0 = posX + (interpTargetX - posX) / newPosRotationIncrements;
//			double d1 = posY + (interpTargetY - posY) / newPosRotationIncrements;
//			double d2 = posZ + (interpTargetZ - posZ) / newPosRotationIncrements;
//			double d3 = MathHelper.wrapAngleTo180_double(interpTargetYaw - rotationYaw);
//			rotationYaw = (float)(rotationYaw + d3 / newPosRotationIncrements);
//			rotationPitch = (float)(rotationPitch + (newPosX - rotationPitch) / newPosRotationIncrements);
//			--newPosRotationIncrements;
//			setPosition(d0, d1, d2);
//			setRotation(rotationYaw, rotationPitch);
//		}
//		else if (!isServerWorld())
//		{
//			motionX *= 0.98D;
//			motionY *= 0.98D;
//			motionZ *= 0.98D;
//		}
//
//		if (Math.abs(motionX) < 0.005D)
//		{
//			motionX = 0.0D;
//		}
//
//		if (Math.abs(motionY) < 0.005D)
//		{
//			motionY = 0.0D;
//		}
//
//		if (Math.abs(motionZ) < 0.005D)
//		{
//			motionZ = 0.0D;
//		}
//
//		worldObj.theProfiler.startSection("ai");
//
//		if (isMovementBlocked())
//		{
//			isJumping = false;
//			moveStrafing = 0.0F;
//			moveForward = 0.0F;
//			randomYawVelocity = 0.0F;
//		}
//		else if (isServerWorld())
//		{
//			worldObj.theProfiler.startSection("newAi");
//			try{
//				superUpdateEntityActionState();
//			}catch(Exception e){
//				e.printStackTrace();
//			}
//			worldObj.theProfiler.endSection();
//		}
//
//		worldObj.theProfiler.endSection();
//		worldObj.theProfiler.startSection("jump");
//
//		if (isInWater())
//		{
//			updateAITick();
//		}
//		else if (isInLava())
//		{
//			motionY += 0.0333333339D;
//		}
//
//		worldObj.theProfiler.endSection();
//
//		worldObj.theProfiler.startSection("travel");
//		moveStrafing *= 0.98F;
//		moveForward *= 0.98F;
//		randomYawVelocity *= 0.9F;
//		moveEntityWithHeading(moveStrafing, moveForward);
//		worldObj.theProfiler.endSection();
//
//		worldObj.theProfiler.startSection("push");
//
//		if (!worldObj.isRemote)
//		{
//			collideWithNearbyEntities();
//		}
//
//		worldObj.theProfiler.endSection();
//	}
//
//	private void superUpdateEntityActionState() {
//			worldObj.theProfiler.startSection("checkDespawn");
//			despawnEntity();
//			worldObj.theProfiler.endSection();
//			worldObj.theProfiler.startSection("sensing");
//			getEntitySenses().clearSensingCache();
//			worldObj.theProfiler.endSection();
//			worldObj.theProfiler.startSection("targetSelector");
//			targetTasks.onUpdateTasks();
//			worldObj.theProfiler.endSection();
//			worldObj.theProfiler.startSection("goalSelector");
//			tasks.onUpdateTasks();
//			worldObj.theProfiler.endSection();
//			worldObj.theProfiler.startSection("navigation");
//			navigator.onUpdateNavigation();
//			worldObj.theProfiler.endSection();
//			worldObj.theProfiler.startSection("mob tick");
//			updateAITasks();
//			worldObj.theProfiler.endSection();
//			worldObj.theProfiler.startSection("controls");
//			worldObj.theProfiler.startSection("move");
//			moveHelper.onUpdateMoveHelper();
//			worldObj.theProfiler.endStartSection("look");
//			getLookHelper().onUpdateLook();
//	//		worldObj.theProfiler.endStartSection("jump");
//	//		jumpHelper.doJump();
//	//		worldObj.theProfiler.endSection();
//			worldObj.theProfiler.endSection();
//		}
//
//	@Override
//	public void setLocationAndAngles(double x, double y, double z, float yaw, float pitch){
//		super.setLocationAndAngles(x, y, z, yaw, pitch);
//	}
//
//	@Override
//	public void onUpdate() {
//
//
//
//
//		// 独自処理用毎時処理
//		for (EntityModeBase leb : maidEntityModeList) {
//			leb.onUpdate(maidMode);
//		}
//
//		super.onUpdate();
//
//
//		if (!worldObj.isRemote) {
//			// サーバー側処理
//			// アイテム使用状態の更新
//			dataWatcher.set(EntityLittleMaid.dataWatch_ItemUse, litemuse);
//			// インベントリの更新
////			if (!mstatOpenInventory) {
//				// TODO メインインベントリに対する処理は既に行っているので，そこまで再チェックする必要がない・・・たぶん．
//				for (int li = InventoryLittleMaid.maxInventorySize; li < maidInventory.getSizeInventory(); li++) {
//					// インベントリの中身が変わった
//					if (maidInventory.isChanged(li)) {
//						ItemStack st = maidInventory.getStackInSlot(li);
//						for(EntityEquipmentSlot lSlot: EntityEquipmentSlot.values()) {
//							if (lSlot.getSlotType() == EntityEquipmentSlot.Type.ARMOR &&
//							lSlot.getIndex() == li-InventoryLittleMaid.maxInventorySize)
//								((WorldServer)worldObj).getEntityTracker().func_151248_b(this, new SPacketEntityEquipment(getEntityId(), lSlot, st));
//							maidInventory.resetChanged(li);
//						}
//						LittleMaidReengaged.Debug(String.format("ID:%d-%s - Slot(%d-%d,%d) Update.", getEntityId(), worldObj.isRemote ? "Client" : "Server", li, mstatSwingStatus[0].index, mstatSwingStatus[1].index));
//					}
////				}
//			}
//
//			// 弓構え
//			mstatAimeBow &= !getSwingStatusDominant().canAttack();
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
//
//			// 斧装備時は攻撃力が上がる
//			IAttributeInstance latt = getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
//			// 属性を解除
//			latt.removeModifier(attAxeAmp);
//			ItemStack lis = getCurrentEquippedItem();
//			if (lis != null && lis.getItem() instanceof ItemAxe) {
//				// 属性を設定
//				latt.applyModifier(attAxeAmp);
//			}
//
//			// Auto-fix transparent maid
//			if (!isContract() && firstload > 0) {
//				if(((1 << getColor()) & (textureData.textureBox[0].wildColor)) == 0) {
//					int r = textureData.getWildColor();
//					if (r < 0) {
//						onSpawnWithEgg();
//					} else {
//						setColor(r);
//					}
//				}
//				firstload = 0;
//			}
//		}
//
//		// 紐で拉致
//		if(mstatgotcha != null) {
//			double d = mstatgotcha.getDistanceSqToEntity(this);
//			if (getAttackTarget() == null) {
//				// インコムごっこ用
//				if (d > 4D) {
////					setPathToEntity(null);
//					getNavigator().clearPathEntity();
//					getLookHelper().setLookPositionWithEntity(mstatgotcha, 15F, 15F);
//				}
//				if (d > 12.25D) {
////					setPathToEntity(worldObj.getPathEntityToEntity(mstatgotcha, this, 16F, true, false, false, true));
//					getNavigator().tryMoveToXYZ(mstatgotcha.posX, mstatgotcha.posY, mstatgotcha.posZ, 1.0F);
//					getLookHelper().setLookPositionWithEntity(mstatgotcha, 15F, 15F);
//				}
//			}
//			if (d > 25D) {
//				double d1 = mstatgotcha.posX - posX;
//				double d3 = mstatgotcha.posZ - posZ;
//				double d5 = 0.125D / (Math.sqrt(d1 * d1 + d3 * d3) + 0.0625D);
//				d1 *= d5;
//				d3 *= d5;
//				motionX += d1;
//				motionZ += d3;
//			}
//			if (d > 42.25D) {
//				double d2 = mstatgotcha.posX - posX;
//				double d4 = mstatgotcha.posZ - posZ;
//				double d6 = 0.0625D / (Math.sqrt(d2 * d2 + d4 * d4) + 0.0625D);
//				d2 *= d6;
//				d4 *= d6;
//				mstatgotcha.motionX -= d2;
//				mstatgotcha.motionZ -= d4;
//			}
//			if (d > 64D) {
//				setGotcha(0);
//				mstatgotcha = null;
//				playSound("random.drr");
//			}
//			if(rand.nextInt(16) == 0) {
//				List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().expand(8D, 8D, 8D));
//				for (int k = 0; k < list.size(); k++) {
//					Entity entity = (Entity)list.get(k);
//					if (!(entity instanceof EntityMob)) {
//						continue;
//					}
//					EntityMob entitymob = (EntityMob)entity;
//					if (entitymob.getAttackTarget() == mstatgotcha) {
//						//1.8検討
//						entitymob.setAttackTarget(this);
//						entitymob.getNavigator().setPath(getNavigator().getPath(), entitymob.moveForward);
//					}
//				}
//			}
//		}
//
//	}
//
//	@Override
//	public void onDeath(DamageSource par1DamageSource) {
//		if (worldObj.isRemote || getExperienceHandler().onDeath(par1DamageSource)) {
//			return;
//		}
//
//		super.onDeath(par1DamageSource);
//
//		// 死因を表示
////		if (!worldObj.isRemote) {
//			if (LittleMaidReengaged.cfg_DeathMessage && getMaidMasterEntity() != null) {
//				TextComponentString text = new TextComponentString(sprintfDeadCause("your %s killed, by %s", par1DamageSource));
//				getMaidMasterEntity().addChatMessage(text);
//			}
////		}
//	}
//
//	public String sprintfDeadCause(String format, DamageSource source) {
//		String ls = source.getDamageType();
//		Entity lentity = source.getSourceOfDamage();
//		if (lentity != null) {
//			if (lentity instanceof EntityPlayer) {
//				ls += ":" + lentity.getName();
//			} else {
//				String lt = EntityList.getEntityString(lentity);
//				if (lt != null) {
//					ls += ":" + lt;
//				}
//			}
//		}
//
//		// 不具合対応
//		// getFormattedText → getUnformattedTextForChat
//		// getFormattedText はクライアント専用（描画用）。
//		// http://forum.minecraftuser.jp/viewtopic.php?f=13&t=23347&p=212078#p211805
//		String lt = getDisplayName().getUnformattedTextForChat();
//
//		return String.format(format, lt, ls);
//	}
//
//	/**
//	 * Client専用．
//	 */
//	private boolean manualDeath = false;
//
//	@Override
//	protected void onDeathUpdate() {
//		if (!worldObj.isRemote) {
//			if (getExperienceHandler().onDeathUpdate()) {
//				return;
//			}
//		} else {
//			if (!manualDeath) {
//				showParticleFX(EnumParticleTypes.SUSPENDED_DEPTH);
//				return;
//			}
//		}
//		super.onDeathUpdate();
//	}
//
//	@SideOnly(Side.CLIENT)
//	public void manualOnDeath() {
//		onDeath(new DamageSource("lmmnx_timeover"));
//		manualDeath = true;
//	}
//
//	// ポーションエフェクト
//	@Override
//	protected void onNewPotionEffect(PotionEffect par1PotionEffect) {
//		super.onNewPotionEffect(par1PotionEffect);
//		if (mstatMasterEntity instanceof EntityPlayerMP) {
//			((EntityPlayerMP)mstatMasterEntity).playerNetServerHandler.sendPacket(new SPacketEntityEffect(getEntityId(), par1PotionEffect));
//		}
//	}
//
//	@Override
//	protected void onChangedPotionEffect(PotionEffect par1PotionEffect, boolean par2) {
//		super.onChangedPotionEffect(par1PotionEffect, par2);
//		// TODO:必要かどうかのチェック
////		if (mstatMasterEntity instanceof EntityPlayerMP) {
////			((EntityPlayerMP)mstatMasterEntity).playerNetServerHandler.sendPacketToPlayer(new Packet41EntityEffect(getEntityId(), par1PotionEffect));
////		}
//	}
//
//	@Override
//	protected void onFinishedPotionEffect(PotionEffect par1PotionEffect) {
//		super.onFinishedPotionEffect(par1PotionEffect);
//		if (mstatMasterEntity instanceof EntityPlayerMP) {
//			((EntityPlayerMP)mstatMasterEntity).playerNetServerHandler.sendPacket(new SPacketRemoveEntityEffect(getEntityId(), par1PotionEffect.getPotion()));
//		}
//	}
//
//
//
//	/**
//	 *  インベントリが変更されました。
//	 */
//	public void onInventoryChanged() {
//		checkClockMaid();
//		checkHeadMount();
//		getNextEquipItem();
////		setArmorTextureValue();
//	}
//
//	/**
//	 * インベントリにある次の装備品を選択
//	 */
//	public boolean getNextEquipItem() {
//		if (worldObj.isRemote) {
////			return false;
//		}
//
//		int li;
//		if (isActiveModeClass()) {
//			li = getActiveModeClass().getNextEquipItem(maidMode);
//		} else {
//			li = -1;
//		}
//		setEquipItem(getDominantArm(), li);
//		return li > -1;
//	}
//
//	public void setEquipItem(int pArm, int pIndex) {
////		if (pArm == getDominantArm()) {
//			maidInventory.currentItem = pIndex;
////		}
//		int li = mstatSwingStatus[pArm].index;
//		if (li != pIndex) {
//			if (li > -1) {
//				maidInventory.setChanged(li);
//			}
//			if (pIndex > -1) {
//				maidInventory.setChanged(pIndex);
//			}
//			mstatSwingStatus[pArm].setSlotIndex(pIndex);
//		}
//	}
//	public void setEquipItem(int pIndex) {
//		setEquipItem(getDominantArm(), pIndex);
//	}
//
//
//	/**
//	 * 対応型射撃武器のリロード判定
//	 */
//	public void getWeaponStatus() {
//		// 飛び道具用の特殊処理
//		ItemStack is = maidInventory.getCurrentItem();
//		if (is == null) return;
//
//		try {
//			Method me = is.getItem().getClass().getMethod("isWeaponReload", ItemStack.class, EntityPlayer.class);
//			weaponReload = (Boolean)me.invoke(is.getItem(), is, maidAvatar);
//		}
//		catch (NoSuchMethodException e) {
//		}
//		catch (Exception e) {
//		}
//
//		try {
//			Method me = is.getItem().getClass().getMethod("isWeaponFullAuto", ItemStack.class);
//			weaponFullAuto = (Boolean)me.invoke(is.getItem(), is);
//		}
//		catch (NoSuchMethodException e) {
//		}
//		catch (Exception e) {
//		}
//	}
//
//	// 保持アイテム関連
//
//	/**
//	 * 現在の装備品
//	 */
//	public ItemStack getCurrentEquippedItem() {
//		return maidInventory.getCurrentItem();
//	}
//
//	@Override
//	public ItemStack getHeldItem(EnumHand hand) {
//		if (hand == EnumHand.MAIN_HAND) {
//			return maidInventory.getCurrentItem();
//		}
//		return null;
//	}
//
//	@Override
//	public Iterable<ItemStack> getArmorInventoryList() {
//		return Arrays.asList(maidInventory.armorInventory);
//	}
//
//	@Override
//	public Iterable<ItemStack> getHeldEquipment() {
//		return Arrays.asList(new ItemStack[]{getCurrentEquippedItem(), null});
//	}
//
//	@Override
//	public ItemStack getItemStackFromSlot(EntityEquipmentSlot slotIn) {
//		if (slotIn == EntityEquipmentSlot.MAINHAND) {
//			return getHeldItem(EnumHand.MAIN_HAND);
//		} else if (slotIn.getSlotType() == EntityEquipmentSlot.Type.ARMOR) {
//			return maidInventory.armorInventory[slotIn.getIndex()];
//		} else {
//			return null;
//		}
//	}
//
//	@Override
//	public void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack) {
//		if (slotIn.func_188452_c() == 0) {
//			maidInventory.setInventoryCurrentSlotContents(stack);
//		} else if (slotIn.func_188452_c() < 5) {
//			maidInventory.setInventorySlotContents(slotIn.getIndex() + InventoryLittleMaid.maxInventorySize, stack);
//			setTextureNames();
//		} else {
//			// TODO What was this used for?
///*
//			par1 -= 5;
//			// 持ち物のアップデート
//			// 独自拡張:普通にスロット番号の通り、上位８ビットは装備スロット
//			// par1はShortで渡されるのでそのように。
//			int lslotindex = par1 & 0x7f;
//			int lequip = (par1 >>> 8) & 0xff;
//			maidInventory.setInventorySlotContents(lslotindex, stack);
//			maidInventory.resetChanged(lslotindex);	// これは意味ないけどな。
//			maidInventory.inventoryChanged = true;
////			if (par1 >= maidInventory.mainInventory.length) {
////				LMM_Client.setArmorTextureValue(this);
////			}
//
//			for (SwingStatus lss: mstatSwingStatus) {
//				if (lslotindex == lss.index) {
//					lss.index = -1;
//				}
//			}
//			if (lequip != 0xff) {
//				setEquipItem(lequip, lslotindex);
////				mstatSwingStatus[lequip].index = lslotindex;
//			}
//			if (lslotindex >= InventoryLittleMaid.maxInventorySize) {
//				setTextureNames();
//			}
//			String s = stack == null ? null : stack.getDisplayName();
//			LittleMaidReengaged.Debug(String.format("ID:%d Slot(%2d:%d):%s", getEntityId(), lslotindex, lequip, s == null ? "NoItem" : s));
//*/
//		}
//	}
//
//	@Override
//	public boolean isMovementBlocked() {
//		return super.isMovementBlocked();
//	}
//
//	public double getDistanceSqToMaster() {
//		return mstatMasterDistanceSq;
//	}
//
//	protected void checkClockMaid() {
//		// 時計を持っているか？
//		mstatClockMaid = maidInventory.getInventorySlotContainItem(Items.clock) > -1;
//	}
//	/**
//	 * 時計を持っているか?
//	 */
//	public boolean isClockMaid() {
//		return mstatClockMaid;
//	}
//
//	/**
//	 * メットを被ってるか
//	 */
//	public boolean isMaskedMaid() {
//		return mstatMaskSelect > -1;
//	}
//
//	protected void checkHeadMount() {
//		// 追加の頭部装備の判定
//		// TODO Render Head.
///*
//		ItemStack lis = maidInventory.getHeadMount();
//		mstatPlanter = false;
//		mstatCamouflage = false;
//		if (lis != null) {
//			if (lis.getItem() instanceof ItemBlock) {
//				Block lblock = Block.getBlockFromItem(lis.getItem());
////				mstatPlanter =	(lblock instanceof BlockFlower	  && lblock.getRenderType() ==  1) ||
//				mstatPlanter =	(lblock.getRenderType() ==  1) ||
//								(lblock instanceof BlockDoublePlant && lblock.getRenderType() == 40);
//				mstatCamouflage = (lblock instanceof BlockLeaves) || (lblock instanceof BlockPumpkin) || (lblock instanceof BlockStainedGlass);
//			} else if (lis.getItem() instanceof ItemSkull) {
//				mstatCamouflage = true;
//			}
//		}
//*/
//	}
//	/**
//	 * カモフラージュ！
//	 */
//	public boolean isCamouflage() {
//		return mstatCamouflage;
//	}
//	/**
//	 * 鉢植え状態
//	 */
//	public boolean isPlanter() {
//		return mstatPlanter;
//	}
//
//	/**
//	 * ポーション等による腕振りモーションの速度補正
//	 */
//	public int getSwingSpeedModifier() {
//		if (isPotionActive(Potion.getPotionFromResourceLocation("haste"))) {
//			return 6 - (1 + getActivePotionEffect(Potion.getPotionFromResourceLocation("haste")).getAmplifier()) * 1;
//		}
//
//		if (isPotionActive(Potion.getPotionFromResourceLocation("mining_fatigue"))) {
//			return 6 + (1 + getActivePotionEffect(Potion.getPotionFromResourceLocation("mining_fatigue")).getAmplifier()) * 2;
//		}
//		return 6;
//	}
//
//	/**
//	 * 手持ちアイテムの破壊
//	 */
//	public void destroyCurrentEquippedItem() {
//		maidInventory.setInventoryCurrentSlotContents(null);
//	}
//
//	/**
//	 * メイドインベントリを開く
//	 * @param pEntityPlayer
//	 */
//	public void displayGUIMaidInventory(EntityPlayer pEntityPlayer) {
//		if (!worldObj.isRemote) {
//			GuiHandler.maidServer = this;
//			pEntityPlayer.openGui(LittleMaidReengaged.instance, GuiHandler.GUI_ID_INVVENTORY, worldObj,
//					(int)posX, (int)posY, (int)posZ);
//		}
//		else
//		{
//			GuiHandler.maidClient = this;
//		}
//	}
//
//	@Override
//	public boolean processInteract(EntityPlayer par1EntityPlayer, EnumHand par2Hand, ItemStack par3ItemStack) {
//		LittleMaidReengaged.Debug(worldObj.isRemote, "LMM_EntityLittleMaid.interact:"+par1EntityPlayer.getGameProfile().getName());
//		float lhealth = getHealth();
//
//		// プラグインでの処理を先に行う
//		for (int li = 0; li < maidEntityModeList.size(); li++) {
//			if (maidEntityModeList.get(li).preInteract(par1EntityPlayer, par3ItemStack)) {
//				return true;
//			}
//		}
//		// しゃがみ時は処理無効
//		if (par1EntityPlayer.isSneaking()) {
//			return false;
//		}
//		// ナデリ判定
//		if (lhealth > 0F && par1EntityPlayer.getControllingPassenger() != null && !(par1EntityPlayer.getControllingPassenger() instanceof EntityLittleMaid)) {
//			// 載せ替え
//			par1EntityPlayer.getControllingPassenger().startRiding(this);
//			return true;
//		}
//
//		if (mstatgotcha == null && par1EntityPlayer.fishEntity == null) {
//			if(par3ItemStack != null && par3ItemStack.getItem() == Items.lead) {
//				// 紐で繋ぐ
//				setGotcha(par1EntityPlayer.getEntityId());
//				mstatgotcha = par1EntityPlayer;
//				CommonHelper.decPlayerInventory(par1EntityPlayer, -1, 1);
//				playSound("entity.item.pickup");
//				return true;
//			}
//
//			if (isContract()) {
//				// Issue #35: 契約失効時の処理を優先化
//				if(!isRemainsContract() && par3ItemStack != null){
//					// ストライキ
//					if (par3ItemStack.getItem() == Items.sugar) {
//						// 受取拒否
//						worldObj.setEntityState(this, (byte)10);
//						return true;
//					} else if (par3ItemStack.getItem() == Items.cake) {
//						// 再契約
//						CommonHelper.decPlayerInventory(par1EntityPlayer, -1, 1);
//						maidContractLimit = (24000 * 7);
//						setFreedom(false);
//						setTracer(false);
//						setMaidWait(false);
//						setMaidMode("Escorter");
//						if(!isMaidContractOwner(par1EntityPlayer)){
//							// あんなご主人なんか捨てて、僕のもとへおいで(洗脳)
//							OwnableEntityHelper.setOwner(this, CommonHelper.getPlayerUUID(par1EntityPlayer));
//							playLittleMaidSound(EnumSound.getCake, true);
//							worldObj.setEntityState(this, (byte)7);
//							maidContractLimit = (24000 * 7);
//							maidAnniversary = worldObj.getTotalWorldTime();
//						}else{
//							// ごめんねメイドちゃん
//							worldObj.setEntityState(this, (byte)11);
//							playLittleMaidSound(EnumSound.Recontract, true);
//
//						}
//						return true;
//					}
//
//				}
//				// 契約状態
//				if (/*lhealth > 0F && */isMaidContractOwner(par1EntityPlayer)) {
//					if (par3ItemStack != null) {
//						// 追加分の処理
//						// プラグインでの処理を先に行う
//						for (int li = 0; li < maidEntityModeList.size(); li++) {
//							if (maidEntityModeList.get(li).interact(par1EntityPlayer, par3ItemStack)) {
//								return true;
//							}
//						}
//						if (isRemainsContract()) {
//							// 通常
//							if (ItemHelper.isSugar(par3ItemStack.getItem())) {
//								// モード切替
//								System.out.println("AAAAAAAAAAAAAAAAAA");
//								boolean cmode = true;
//								if(par3ItemStack.getItem() instanceof IItemSpecialSugar){
//									cmode = ((IItemSpecialSugar)par3ItemStack.getItem()).onSugarInteract(worldObj, par1EntityPlayer, par3ItemStack, this);
//								}
//								CommonHelper.decPlayerInventory(par1EntityPlayer, -1, 1);
//								eatSugar(false, true, false);
//								if(!cmode) return true;
//								worldObj.setEntityState(this, (byte)11);
//
//								// TODO 口開くよ…
//								LittleMaidReengaged.Debug("give suger." + worldObj.isRemote);
//								if (!worldObj.isRemote) {
//									setFreedom(isFreedom());
//									if (isMaidWait()) {
//										// 動作モードの切替
//										boolean lflag = false;
//										setActiveModeClass(null);
//										for (int li = 0; li < maidEntityModeList.size() && !lflag; li++) {
//											lflag = maidEntityModeList.get(li).changeMode(par1EntityPlayer);
//											if (lflag) {
//												setActiveModeClass(maidEntityModeList.get(li));
//											}
//										}
//										if (!lflag) {
//											setMaidMode("Escorter");
//											setEquipItem(-1);
////											maidInventory.currentItem = -1;
//										}
//										setMaidWait(false);
//										getNextEquipItem();
//									} else {
//										// 待機
//										setMaidWait(true);
//									}
//								}
//								return true;
//							}
//							else if (par3ItemStack.getItem()==LittleMaidReengaged.registerKey &&
//									!par1EntityPlayer.worldObj.isRemote) {
//								// トリガーセット
//								if (registerTick.isEnable()) {
//									registerTick.setEnable(false);
//									par1EntityPlayer.addChatComponentMessage(new TextComponentString(I18n.translateToLocal("littleMaidMob.chat.text.cancelregistration"))
//											.setChatStyle(new Style().setColor(TextFormatting.DARK_RED)));
//									return true;
//								}
//
//								NBTTagCompound tagCompound = par3ItemStack.getTagCompound();
//								if (tagCompound == null) return false;
//
//								String modeString = tagCompound.getString(ItemTriggerRegisterKey.RK_MODE_TAG);
//								if (modeString.isEmpty()) return false;
//
//								registerMode = modeString;
//								registerTick.setValue(200);
//
//								int count = tagCompound.getInteger(ItemTriggerRegisterKey.RK_COUNT);
//								if(++count >= ItemTriggerRegisterKey.RK_MAX_COUNT) {
//									par1EntityPlayer.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, null);
//								}
//								tagCompound.setInteger(ItemTriggerRegisterKey.RK_COUNT, count);
//
//								par1EntityPlayer.addChatComponentMessage(new TextComponentString(I18n.translateToLocal("littleMaidMob.chat.text.readyregistration") + registerMode));
//								if(count >= ItemTriggerRegisterKey.RK_MAX_COUNT-10){
//									if(count<ItemTriggerRegisterKey.RK_MAX_COUNT){
//										par1EntityPlayer.addChatComponentMessage(new TextComponentString(I18n.translateToLocal("littleMaidMob.chat.text.warningcount") +
//												(ItemTriggerRegisterKey.RK_MAX_COUNT-count)).setChatStyle(new Style().setColor(TextFormatting.YELLOW)));
//									} else {
//										par1EntityPlayer.addChatComponentMessage(new TextComponentString(I18n.translateToLocal("littleMaidMob.chat.text.endcount"))
//												.setChatStyle(new Style().setColor(TextFormatting.DARK_RED)));
//									}
//								}
//
//								return true;
//							} else if (registerTick.isEnable() && !par1EntityPlayer.worldObj.isRemote) {
//								List list = TriggerSelect.getuserTriggerList(CommonHelper.getPlayerUUID(par1EntityPlayer), registerMode);
//								Item item = par3ItemStack.getItem();
//								if (item != null) {
//									boolean flag = false;
//									while(list.remove(item)) flag = true;
//									if (!flag) {
//										list.add(item);
//										par1EntityPlayer.addChatComponentMessage(new TextComponentString(I18n.translateToLocal("littleMaidMob.chat.text.addtrigger") + " " + registerMode + "/+" + Item.itemRegistry.getNameForObject(item).toString()));
//									} else {
//										par1EntityPlayer.addChatComponentMessage(new TextComponentString(I18n.translateToLocal("littleMaidMob.chat.text.removetrigger") + " " + registerMode + "/-" + Item.itemRegistry.getNameForObject(item).toString()));
//									}
//								}
//								IFF.saveIFF(CommonHelper.getPlayerUUID(par1EntityPlayer));
//								registerTick.setEnable(false);
//								return true;
//							}
//							else if (par3ItemStack.getItem() == Items.dye) {
//								// カラーメイド
//								if (!worldObj.isRemote) {
//									setColor(15 - par3ItemStack.getItemDamage());
//								}
//								CommonHelper.decPlayerInventory(par1EntityPlayer, -1, 1);
//								return true;
//							}
//							else if (par3ItemStack.getItem() == Items.feather) {
//								// 自由行動
////								MMM_Helper.decPlayerInventory(par1EntityPlayer, -1, 1);
//								if(!worldObj.isRemote){
//									setFreedom(!isFreedom());
//									worldObj.setEntityState(this, isFreedom() ? (byte)12 : (byte)13);
//								}
//								return true;
//							}
//							else if (par3ItemStack.getItem() == Items.saddle) {
//								// 肩車
//								if (getRidingEntity() == par1EntityPlayer) {
//									dismountRidingEntity();
//								} else {
//									startRiding(par1EntityPlayer, true);
//								}
//								clearTilePosAll();
//								getNavigator().clearPathEntity();
//								return true;
//							}
//							else if (par3ItemStack.getItem() == Items.gunpowder) {
//								// test TNT-D
//								maidOverDriveTime.setValue(par3ItemStack.stackSize * 10);
//								playSound("mob.zombie.infect");
//								if (par3ItemStack.stackSize == 64) {
//									getMaidMasterEntity().addStat(AchievementsLMRE.ac_Boost);
//								}
//								CommonHelper.decPlayerInventory(par1EntityPlayer, -1, par3ItemStack.stackSize);
//								return true;
//							}
//							else if (par3ItemStack.getItem() == Items.book) {
//								// IFFのオープン
////								MMM_Helper.decPlayerInventory(par1EntityPlayer, -1, 1);
////								if (worldObj.isRemote) {
//									par1EntityPlayer.openGui(LittleMaidReengaged.instance,
//											GuiHandler.GUI_ID_IFF,
//											worldObj,
//											(int)posX,
//											(int)posY,
//											(int)posZ);
////								}
//								return true;
//							}/*
//							else if ((itemstack1.getItem() == Items.glass_bottle) && (experienceValue >= 5)) {
//								// Expボトル
//								MMM_Helper.decPlayerInventory(par1EntityPlayer, -1, 1);
//								if (!worldObj.isRemote) {
//									entityDropItem(new ItemStack(Items.experience_bottle), 0.5F);
//									experienceValue -= 5;
//									if (maidAvatar != null) {
//										maidAvatar.experienceTotal -= 5;
//									}
//								}
//								return true;
//							}*/
//							else if (par3ItemStack.getItem() instanceof ItemPotion) {
//								// ポーション
//								if(!worldObj.isRemote) {
//									List list = PotionUtils.getEffectsFromStack(par3ItemStack);
//									if (list != null) {
//										PotionEffect potioneffect;
//										for (Iterator iterator = list.iterator(); iterator.hasNext(); addPotionEffect(new PotionEffect(potioneffect))) {
//											potioneffect = (PotionEffect)iterator.next();
//										}
//									}
//								}
//								CommonHelper.decPlayerInventory(par1EntityPlayer, -1, 1);
//								return true;
//							}
//							else if (isFreedom() && par3ItemStack.getItem() == Items.redstone) {
//								// Tracer
//								CommonHelper.decPlayerInventory(par1EntityPlayer, -1, 1);
//								setMaidWait(false);
//								setTracer(!isTracer());
//								if (isTracer()) {
//									worldObj.setEntityState(this, (byte)14);
//								} else {
//									worldObj.setEntityState(this, (byte)12);
//								}
//								return true;
//							}else if(par3ItemStack.getItem() == Items.stick){
//								if(getDominantArm()==0){
//									setDominantArm(1);
//								}else{
//									setDominantArm(0);
//								}
//								return true;
//							}
//						}
//					}
//					// メイドインベントリ
//					OwnableEntityHelper.setOwner(this, CommonHelper.getPlayerUUID(par1EntityPlayer));
//					getNavigator().clearPathEntity();
//					isJumping = false;
//					if(!worldObj.isRemote){
//						syncMaidArmorVisible();
//						syncExpBoost();
//					}
//					displayGUIMaidInventory(par1EntityPlayer);
//					return true;
//				}
//			} else {
//				// 未契約
//				if (par3ItemStack != null) {
//					if (par3ItemStack.getItem() == Items.cake) {
//						// 契約
//						CommonHelper.decPlayerInventory(par1EntityPlayer, -1, 1);
//
//						deathTime = 0;
//						if (!worldObj.isRemote) {
//							if (AchievementsLMRE.ac_Contract != null) {
//								par1EntityPlayer.addStat(AchievementsLMRE.ac_Contract);
//							}
//							setContract(true);
//							OwnableEntityHelper.setOwner(this, CommonHelper.getPlayerUUID(par1EntityPlayer));
//							setHealth(20);
//							setMaidMode("Escorter");
//							setMaidWait(false);
//							setFreedom(false);
//							setPlayingRole(0);
//							playLittleMaidSound(EnumSound.getCake, true);
////							playLittleMaidSound(LMM_EnumSound.getCake, true);
////							playTameEffect(true);
//							worldObj.setEntityState(this, (byte)7);
//							// 契約記念日と、初期契約期間
//							maidContractLimit = (24000 * 7);
//							maidAnniversary = worldObj.getTotalWorldTime();
//							// テクスチャのアップデート:いらん？
////							LMM_Net.sendToAllEClient(this, new byte[] {LMM_Net.LMN_Client_UpdateTexture, 0, 0, 0, 0});
//
//						}
//						return true;
//					}
////					worldObj.setEntityState(this, (byte)6);
//				}
//			}
//		} else if (/*lhealth > 0F && */mstatgotcha != null) {
//			if (!worldObj.isRemote) {
//				EntityItem entityitem = new EntityItem(worldObj, mstatgotcha.posX, mstatgotcha.posY, mstatgotcha.posZ, new ItemStack(Items.string));
//				worldObj.spawnEntityInWorld(entityitem);
//				setGotcha(0);
//				mstatgotcha = null;
//			}
//			return true;
//		}
//
//		return false;
//	}
//
//	// メイドの契約設定
//	@Override
//	public boolean isTamed() {
//		return isContract();
//	}
//	public boolean isContract() {
////		return worldObj.isRemote ? maidContract : super.isTamed();
//		return super.isTamed();
//	}
//	public boolean isContractEX() {
//		return isContract() && isRemainsContract();
//	}
//
//	@Override
//	public void setTamed(boolean par1) {
//		setContract(par1);
//	}
//	@Override
//	public void setContract(boolean flag) {
//		super.setTamed(flag);
//		textureData.setContract(flag);
//	}
//
//	/**
//	 * 契約期間の残りがあるかを確認
//	 */
//	protected void updateRemainsContract() {
//		boolean lflag = false;
//		if (maidContractLimit > 0) {
//			maidContractLimit--;
//			lflag = true;
//		}
//		if (getMaidFlags(dataWatch_Flags_remainsContract) != lflag) {
//			setMaidFlags(lflag, dataWatch_Flags_remainsContract);
//		}
//	}
//	/**
//	 * ストライキに入っていないか判定
//	 * @return
//	 */
//	public boolean isRemainsContract() {
//		return getMaidFlags(dataWatch_Flags_remainsContract);
//	}
//
//	public float getContractLimitDays() {
//		return maidContractLimit > 0 ? (maidContractLimit / 24000F) : -1F;
//	}
//
//	public boolean updateMaidContract() {
//		// 同一性のチェック
//		boolean lf = isContract();
//		if (textureData.isContract() != lf) {
//			textureData.setContract(lf);
//			return true;
//		}
//		return false;
//	}
//
//	@Override
//	public EntityLivingBase getOwner() {
//		return getMaidMasterEntity();
//	}
//
//	public UUID getMaidMasterUUID() {
//		return OwnableEntityHelper.getOwner(this);
//	}
//
//	public EntityPlayer getMaidMasterEntity() {
//		// 主を獲得
//		if (isContract()) {
//			EntityPlayer entityplayer = mstatMasterEntity;
//			if (mstatMasterEntity == null || mstatMasterEntity.isDead) {
//				UUID lname;
//				// サーバー側ならちゃんとオーナ判定する
//
//				// Minecraftクラスのプレイヤーを取得していたが、サーバには存在しないためプロキシをかませる。サーバならNULL固定
//				EntityPlayer clientPlayer = LittleMaidReengaged.proxy.getClientPlayer();
//
//				if (!LittleMaidReengaged.proxy.isSinglePlayer()
//						|| LittleMaidReengaged.cfg_checkOwnerName
//						|| clientPlayer == null) {
//					lname = getMaidMasterUUID();
//				} else {
//					lname = CommonHelper.getPlayerUUID(clientPlayer);
//				}
//				entityplayer = worldObj.getPlayerEntityByUUID(lname);
//				// とりあえず主の名前を入れてみる
//				// TODO:再設定は不可になったので経過観察
////				maidAvatar.username = lname;
//
//				if (entityplayer != null && maidAvatar != null) {
//					maidAvatar.capabilities.isCreativeMode = entityplayer.capabilities.isCreativeMode;
//				}
//
//			}
//			return entityplayer;
//		}
//		return null;
//	}
//
//	public boolean isMaidContractOwner(UUID pname) {
//		return pname.equals(CommonHelper.getPlayerUUID(mstatMasterEntity));
//	}
//
//	public boolean isMaidContractOwner(EntityPlayer pentity) {
//		return pentity == getMaidMasterEntity();
//
////		return pentity == mstatMasterEntity;
//	}
//
//	// メイドの待機設定
//	public boolean isMaidWait() {
//		return maidWait;
//	}
//
//	public boolean isMaidWaitEx() {
//		return isMaidWait() | (mstatWaitCount > 0) | isOpenInventory();
//	}
//
//	public void setMaidWait(boolean pflag) {
//		// 待機常態の設定、 isMaidWait系でtrueを返すならAIが勝手に移動を停止させる。
//		maidWait = pflag;
//		setMaidFlags(pflag, dataWatch_Flags_Wait);
//		aiSit.setSitting(pflag);
//		//maidWait = pflag;
//		isJumping = false;
//		setAttackTarget(null);
//		setRevengeTarget(null);
//		//setPathToEntity(null);
//		getNavigator().clearPathEntity();
//		setPlayingRole(0);
//		if(pflag){
//			//setMaidModeAITasks(null,null);
//			setWorking(false);
//			getNavigator().clearPathEntity();
//			clearTilePosAll();
//			/*
//			setHomePosAndDistance(
//					new BlockPos(MathHelper.floor_double(lastTickPosX),
//					MathHelper.floor_double(lastTickPosY),
//					MathHelper.floor_double(lastTickPosZ)), 0);
//					*/
//		}else{
//			setMaidMode(maidMode,false);
//		}
//		velocityChanged = true;
//	}
//
//	public void setMaidWaitCount(int count) {
//		mstatWaitCount = count;
//	}
//
//
//	// インベントリの表示関係
//	// まさぐれるのは一人だけ
//	public void setOpenInventory(boolean flag) {
//		mstatOpenInventory = flag;
//	}
//
//	public boolean isOpenInventory() {
//		return mstatOpenInventory;
//	}
//
//	/**
//	 * GUIを開いた時にサーバー側で呼ばれる。
//	 */
//	public void onGuiOpened() {
//		setOpenInventory(true);
//	}
//
//	/**
//	 * GUIを閉めた時にサーバー側で呼ばれる。
//	 */
//	public void onGuiClosed() {
//		setOpenInventory(false);
//		int li = maidMode & 0x0080;
//		setMaidWaitCount((li == 0) ? 50 : 0);
//	}
//
//	// 腕振り
//	public void setSwing(int attacktime, EnumSound enumsound, boolean force) {
//		setSwing(attacktime, enumsound, getDominantArm(), force);
//	}
//
//	public void setSwing(int pattacktime, EnumSound enumsound, int pArm, boolean force) {
//		mstatSwingStatus[pArm].attackTime = pattacktime;
////		maidAttackSound = enumsound;
////		soundInterval = 0;// いるか？
//		if (!weaponFullAuto) {
//			setSwinging(pArm, enumsound, force);
//		}
//		if (!worldObj.isRemote) {
//			byte[] lba = new byte[] {
//				(byte)pArm,
//				0, 0, 0, 0,
//				0, 0, 0, 0
//			};
//			NetworkHelper.setIntToPacket(lba, 1, enumsound.index);
//			NetworkHelper.setIntToPacket(lba, 5, force?1:0);
//			syncNet(EnumPacketMode.CLIENT_SWINGARM, lba);
//		}
//	}
//
//	public void setSwinging(EnumSound pSound, boolean force) {
//		setSwinging(getDominantArm(), pSound, force);
//	}
//	public void setSwinging(int pArm, EnumSound pSound, boolean force) {
//		if(!pSound.equals(EnumSound.Null)) playLittleMaidSound(pSound, force);
//		if (mstatSwingStatus[pArm].setSwinging()) {
//			maidAvatar.swingProgressInt = -1;
////			maidAvatar.swingProgressInt = -1;
//			maidAvatar.isSwingInProgress = true;
//		}
//	}
//
//	public boolean getSwinging() {
//		return getSwinging(getDominantArm());
//	}
//	public boolean getSwinging(int pArm) {
//		return mstatSwingStatus[pArm].isSwingInProgress;
//	}
//
//	/**
//	 * 利き腕のリロードタイム
//	 */
//	public SwingStatus getSwingStatusDominant() {
//		return mstatSwingStatus[getDominantArm()];
//	}
//
//	public SwingStatus getSwingStatus(int pindex) {
//		return mstatSwingStatus[pindex];
//	}
//
//
//	public int getDominantArm() {
//		return maidDominantArm;
//	}
//
//	// 今宵のメイドは血に飢えておる
//	public void setBloodsuck(boolean pFlag) {
//		mstatBloodsuck = pFlag;
//		setMaidFlags(pFlag, dataWatch_Flags_Bloodsuck);
//	}
//
//	public boolean isBloodsuck() {
//		return mstatBloodsuck;
//	}
//
//
//	// 砂糖関連
//	public void setLookSuger(boolean pFlag) {
//		mstatLookSuger = pFlag;
//		setMaidFlags(pFlag, dataWatch_Flags_LooksSugar);
//	}
//
//	public boolean isLookSuger() {
//		return mstatLookSuger;
//	}
//
//	public static enum EnumConsumeSugar{
//		/**通常回復**/HEAL,
//		/**契約更新**/RECONTRACT,
//		/**その他（つまみ食いとか）**/OTHER
//	};
//
//	/** インベントリ内の砂糖を食べる。左上から消費する。
//	 * @param mode EnumConsumeSugar型の定数
//	 */
//	protected void consumeSugar(EnumConsumeSugar mode){
//		ItemStack[] stacklist = maidInventory.mainInventory;
//		ItemStack stack = null;
//		Item item = null;
//		int index = -1;
//		for(int i=0;i<stacklist.length;i++){
//			ItemStack ts = stacklist[i];
//			if(ts==null)continue;
//			Item ti = ts.getItem();
//			if(ItemHelper.isSugar(ti)){
//				stack = ts;
//				item = ti;
//				index = i;
//				break;
//			}
//		}
//		if(item==null||stack==null||index==-1) return;
//		if(item==Items.sugar){
//			eatSugar(true, true, mode==EnumConsumeSugar.RECONTRACT);
//		}else if(item instanceof IItemSpecialSugar){
//			//モノグサ実装。良い子の皆さんはちゃんとif使うように…
//			eatSugar(((IItemSpecialSugar)item).onSugarEaten(this, mode, stack), true, mode==EnumConsumeSugar.RECONTRACT);
//		}
//		if (mode == EnumConsumeSugar.RECONTRACT) {
//			addMaidExperience(3.5f);
//		}
//		maidInventory.decrStackSize(index, Math.min(1, mode == EnumConsumeSugar.OTHER ? 1 : getExpBooster()));
//	}
//
//	/** 主に砂糖を食べる仕草やその後のこと。consumeSugar()から呼ばれる．
//	 * interact()等メイドインベントリ外の砂糖を食べさせるときはこっちを直接呼ぶべし．
//	 * ペロッ・・・これは・・・砂糖ッ！！
//	 * @param heal デフォルトの1回復をするか？
//	 * @param motion 腕を振るか？
//	 * @param recontract 契約延長効果アリ？
//	 */
//	public void eatSugar(boolean heal, boolean motion, boolean recontract) {
//		if (motion) {
//			setSwing(2, (getMaxHealth() - getHealth() <= 1F) ?  EnumSound.eatSugar_MaxPower : EnumSound.eatSugar, false);
//		}
//		int h = hurtResistantTime;
//		if(heal) {
//			heal(1);
//		}
//		hurtResistantTime = h;
//		playSound("entity.item.pickup");
//		LittleMaidReengaged.Debug(("eat Sugar." + worldObj.isRemote));
//
//		if (recontract) {
//			// 契約期間の延長
//			maidContractLimit += 24000;
//			if (maidContractLimit > 168000) {
//				maidContractLimit = 168000;	// 24000 * 7
//			}
//
//			if (worldObj.getTotalWorldTime() - maidAnniversary > 24000 * 365) {
//				EntityPlayer player;
//				if ((player = getMaidMasterEntity()) != null)
//					player.addStat(AchievementsLMRE.ac_MyFavorite);
//			}
//		}
//
//		// 暫定処理
//		if (maidAvatar != null) {
//			maidAvatar.getFoodStats().addStats(20, 20F);
//		}
//	}
//
//
//	// お仕事チュ
//	/**
//	 * 仕事中かどうかの設定
//	 */
//	public void setWorking(boolean pFlag) {
//		workingCount.setEnable(pFlag);
//	}
//
//	/**
//	 * 仕事中かどうかを返す
//	 */
//	public boolean isWorking() {
//		return workingCount.isEnable();
//	}
//
//	/**
//	 * 仕事が終了しても余韻を含めて返す
//	 */
//	public boolean isWorkingDelay() {
//		return workingCount.isDelay();
//	}
//
//	/**
//	 * トレーサーモードの設定
//	 */
//	public void setTracer(boolean pFlag) {
//		maidTracer = pFlag;
//		aiTracer.setEnable(pFlag);
//		setMaidFlags(pFlag, dataWatch_Flags_Tracer);
////		if (maidTracer) {
////			setFreedom(true);
////		}
//		aiTracer.setEnable(pFlag);
//	}
//
//	/**
//	 * トレーサーモードであるか？
//	 */
//	public boolean isTracer() {
//		return getMaidFlags(dataWatch_Flags_Tracer);
//	}
//
//
//	// お遊びモード
//	public void setPlayingRole(int pValue) {
//
//		if (mstatPlayingRole != pValue) {
//			mstatPlayingRole = pValue;
//			if (pValue == 0) {
//				setAttackTarget(null);
//				setMaidMode(mstatWorkingInt , true);
//			} else {
//				setMaidMode(0x00ff, true);
//			}
//		}
//	}
//
//	public int getPlayingRole() {
//		return mstatPlayingRole;
//	}
//
//	public boolean isPlaying() {
//		return mstatPlayingRole != 0;
//	}
//
//
//	// 自由行動
//	public void setFreedom(boolean pFlag) {
//		// AI関連のリセットもここで。
//		maidFreedom = pFlag;
//		if (!worldObj.isRemote) {
//			aiRestrictRain.setEnable(pFlag);
//			aiFreeRain.setEnable(pFlag);
//			aiWander.setEnable(pFlag);
////			aiJumpTo.setEnable(!pFlag);
//			aiAvoidPlayer.setEnable(!pFlag);
//			aiFollow.setEnable(!pFlag);
//			aiTracer.setEnable(isTracer()&&pFlag);
////			setAIMoveSpeed(pFlag ? moveSpeed_Nomal : moveSpeed_Max);
////			setMoveForward(0.0F);
//			setPlayingRole(0);
//			if (maidFreedom && isContract()) {
//				setTracer(isTracer());
//				setHomePosAndDistance(getPosition(), (int) getMaximumHomeDistance());
//			} else {
//				detachHome();
//			}
//			setMaidFlags(maidFreedom, dataWatch_Flags_Freedom);
//		} else {
//			syncNet(EnumPacketMode.SERVER_CHAMGE_FREEDOM, new byte[]{(byte) (pFlag?1:0)});
//		}
//	}
//
//	@Override
//	public float getMaximumHomeDistance() {
//		return 20f;
//	}
//
//	public boolean isFreedom() {
//		return maidFreedom;
//	}
//
//	public void onWarp() {
//		getActiveModeClass().onWarp();
//	}
//
//	public boolean isHeadMount(){
//		return ItemUtil.isHelm(maidInventory.armorInventory[3]);
//	}
//
//	/**
//	 *  レベルを取得
//	 * @return
//	 */
//	public int getMaidLevel() {
//		return ExperienceUtil.getLevelFromExp(maidExperience);
//	}
//
//	/**
//	 *  現在経験値を取得
//	 * @return
//	 */
//	public float getMaidExperience() {
//		if (maidExperience<=0) {
//			return 0;
//		}
//		return maidExperience;
//	}
//
//	/**
//	 * 経験値を追加
//	 * @param value
//	 */
//	public void addMaidExperience(float value) {
//		if (!isContractEX() || worldObj.isRemote) {
//			return;
//		}
//
//		int currentLevel = getMaidLevel();
//		if (maidExperience > 0) {
//			value *= getExpBooster();
//		}
//		maidExperience += value;
//
//		// レベルが下がってしまう場合はそれ以上引かない
//		if (maidExperience < ExperienceUtil.getRequiredExpToLevel(currentLevel)) {
//			maidExperience = ExperienceUtil.getRequiredExpToLevel(currentLevel);
//		}
//
//		// 最大レベル
//		if (maidExperience > ExperienceUtil.getRequiredExpToLevel(ExperienceUtil.EXP_FUNCTION_MAX)) {
//			maidExperience = ExperienceUtil.getRequiredExpToLevel(ExperienceUtil.EXP_FUNCTION_MAX);
//		}
//
//		dataWatcher.set(EntityLittleMaid.dataWatch_MaidExpValue, maidExperience);
//		boolean flag = false;
//		for (;maidExperience >= ExperienceUtil.getRequiredExpToLevel(currentLevel+1); currentLevel++) {
//			// 一度に複数レベルアップした場合にその分だけ呼ぶ
//			if (!flag) {
//				playSound("random.levelup");
//				flag = true;
//			}
//			getExperienceHandler().onLevelUp(currentLevel+1);
//			VEventBus.instance.post(new EventLMRE.MaidLevelUpEvent(this, getMaidLevel()));
//		}
//	}
//
//	/**
//	 * 取得経験値による操作を定義するExperienceHandlerを取得
//	 * @return
//	 */
//	public ExperienceHandler getExperienceHandler() {
//		return experienceHandler;
//	}
//
//	public void setExperienceHandler(ExperienceHandler handler) {
//		if (handler == null) {
//			throw new NullPointerException("ExperienceHandler cannot be null!");
//		}
//		experienceHandler = handler;
//	}
//
//	/**
//	 * 経験値ブーストを取得
//	 */
//	public int getExpBooster() {
//		return gainExpBoost;
//	}
//
//	/**
//	 * 経験値ブーストを設定
//	 * @param v ブースト値(0以上)
//	 */
//	public void setExpBooster(int v) {
//		if (v < 1) {
//			v = 1;
//		}
//		if (v > ExperienceUtil.getBoosterLimit(getMaidLevel())) {
//			v = ExperienceUtil.getBoosterLimit(getMaidLevel());
//		}
//		gainExpBoost = v;
//	}
//
//	/**
//	 * サーバーへテクスチャパックのインデックスを送る。
//	 * クライアント側の処理
//	 */
//	public boolean sendTextureToServer() {
//		// 16bitあればテクスチャパックの数にたりんべ
////		MMM_TextureManager.instance.postSetTexturePack(this, textureData.getColor(), textureData.getTextureBox());
//		return true;
//	}
//
//	private boolean checkedTextureUpdate = false;
//
//	/**
//	 * テクスチャパックの更新を確認
//	 * @return
//	 */
//	public boolean updateTexturePack() {
//		/*
//		boolean lflag = false;
//		int ltexture = dataWatcher.getWatchableObjectInt(dataWatch_Texture);
//		int larmor = (ltexture >>> 16) & 0xffff;
//		ltexture &= 0xffff;
//		if (textureData.textureIndex[0] != ltexture) {
//			textureData.textureIndex[0] = ltexture;
//			lflag = true;
//		}
//		if (textureData.textureIndex[1] != larmor) {
//			textureData.textureIndex[1] = larmor;
//			lflag = true;
//		}
//		if (lflag) {
//			MMM_TextureManager.instance.postGetTexturePack(this, textureData.getTextureIndex());
//		}
//		return lflag;
//		*/
//		// TODO 移行準備:テクスチャ設定
//		if(!checkedTextureUpdate){
//			checkedTextureUpdate = true;
//		}
//		return false;
//	}
//
//	@Override
//	public int getColor() {
////		return textureData.getColor();
//		return dataWatcher.get(EntityLittleMaid.dataWatch_Color);
//	}
//
//	@Override
//	public void setColor(int index) {
//		textureData.setColor(index);
//		dataWatcher.set(EntityLittleMaid.dataWatch_Color, index);
//	}
//
//	public boolean updateMaidColor() {
//		// 同一性のチェック
//		int lc = getColor();
//		if (textureData.getColor() != lc) {
//			textureData.setColor(lc);
//			return true;
//		}
//		return false;
//	}
//
//	/**
//	 * 紐の持ち主
//	 */
//	public void updateGotcha() {
//		int lid = dataWatcher.get(EntityLittleMaid.dataWatch_Gotcha);
//		if (lid == 0) {
//			mstatgotcha = null;
//			return;
//		}
//		if (mstatgotcha != null && mstatgotcha.getEntityId() == lid) {
//			return;
//		}
//		for (int li = 0; li < worldObj.loadedEntityList.size(); li++) {
//			if (((Entity)worldObj.loadedEntityList.get(li)).getEntityId() == lid) {
//				mstatgotcha = (Entity)worldObj.loadedEntityList.get(li);
//				break;
//			}
//		}
//	}
//
//	public void setGotcha(int pEntityID) {
//		dataWatcher.set(EntityLittleMaid.dataWatch_Gotcha, Integer.valueOf(pEntityID));
//	}
//	public void setGotcha(Entity pEntity) {
//		setGotcha(pEntity == null ? 0 : pEntity.getEntityId());
//	}
//
//
//
//	/**
//	 * フラグ群に値をセット。
//	 * @param pCheck： 対象値。
//	 * @param pFlags： 対象フラグ。
//	 */
//	public void setMaidFlags(boolean pFlag, int pFlagvalue) {
//		int li = dataWatcher.get(EntityLittleMaid.dataWatch_Flags);
//		li = pFlag ? (li | pFlagvalue) : (li & ~pFlagvalue);
//		dataWatcher.set(EntityLittleMaid.dataWatch_Flags, Integer.valueOf(li));
//	}
//
//	/**
//	 * 指定されたフラグを獲得
//	 */
//	public boolean getMaidFlags(int pFlagvalue) {
//		return (dataWatcher.get(EntityLittleMaid.dataWatch_Flags) & pFlagvalue) > 0;
//	}
//
//	/**
//	 *  利き腕の設定
//	 */
//	public void setDominantArm(int pindex) {
//		if (mstatSwingStatus.length <= pindex) return;
//		if (maidDominantArm == pindex) return;
//		for (SwingStatus lss : mstatSwingStatus) {
//			lss.index = lss.lastIndex = -1;
//		}
//		maidDominantArm = pindex;
//		dataWatcher.set(EntityLittleMaid.dataWatch_DominamtArm, maidDominantArm);
//		LittleMaidReengaged.Debug("Change Dominant.");
//	}
//
//	@Override
//	public void setHomePosAndDistance(BlockPos par1, int par4) {
//		homeWorld = dimension;
//		super.setHomePosAndDistance(par1, par4);
//	}
//
//	@Override
//	public void setTexturePackName(TextureBox[] pTextureBox) {
//		// Client
//		textureData.setTexturePackName(pTextureBox);
//		setTextureNames();
//		LittleMaidReengaged.Debug("ID:%d, TextureModel:%s", getEntityId(), textureData.getTextureName(0));
//		// モデルの初期化
//		((TextureBox)textureData.textureBox[0]).models[0].setCapsValue(IModelCaps.caps_changeModel, maidCaps);
//		// スタビの付け替え
////		for (Entry<String, MMM_EquippedStabilizer> le : pEntity.maidStabilizer.entrySet()) {
////			if (le.getValue() != null) {
////				le.getValue().updateEquippedPoint(pEntity.textureModel0);
////			}
////		}
//	}
//
//	/**
//	 * Client用
//	 */
//	public void setTextureNames() {
//		textureData.setTextureNames();
//		if (worldObj.isRemote) {
//			textureNameMain = textureData.getTextureName(0);
//			textureNameArmor = textureData.getTextureName(1);
//		}
//	}
//
//	public void setNextTexturePackege(int pTargetTexture) {
//		textureData.setNextTexturePackege(pTargetTexture);
//	}
//
//	public void setPrevTexturePackege(int pTargetTexture) {
//		textureData.setPrevTexturePackege(pTargetTexture);
//	}
//
//
//	// textureEntity
//	@Override
//	public void setTextureBox(TextureBoxBase[] pTextureBox) {
//		textureData.setTextureBox(pTextureBox);
//	}
//
//	public String getModelNameMain() {
//		return textureNameMain;
//	}
//
//	public String getModelNameArmor() {
//		return textureNameArmor;
//	}
//
//	public void setTextureNameMain(String modelNameMain) {
//		this.textureNameMain = modelNameMain;
//		refreshModels();
//	}
//
//	public void setTextureNameArmor(String modelNameArmor) {
//		this.textureNameArmor = modelNameArmor;
//		refreshModels();
//	}
//
//	protected void refreshModels() {
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
//	}
//
//	private TextureBoxBase modelBoxAutoSelect(String pName) {
//		return worldObj.isRemote ? ModelManager.instance.getTextureBox(pName) : ModelManager.instance.getTextureBoxServer(pName);
//	}
//
//	@Override
//	public TextureBoxBase[] getTextureBox() {
//		return textureData.getTextureBox();
//	}
//
//	@Override
//	public void setTextures(int pIndex, ResourceLocation[] pNames) {
//		textureData.setTextures(pIndex, pNames);
//	}
//
//	@Override
//	public ResourceLocation[] getTextures(int pIndex) {
//		ResourceLocation[] r = textureData.getTextures(pIndex);
//		return r;
//	}
//
//	@Override
//	public ModelConfigCompound getModelConfigCompound() {
//		return textureData;
//	}
//
//	// Tile関係
//
//	/**
//	 * 使っているTileかどうか判定して返す。
//	 */
//	public boolean isUsingTile(TileEntity pTile) {
//		if (isActiveModeClass()) {
//			return getActiveModeClass().isUsingTile(pTile);
//		}
//		for (int li = 0; li < maidTiles.length; li++) {
//			if (maidTiles[li] != null &&
//					pTile.getPos().getX() == maidTiles[li][0] &&
//					pTile.getPos().getY() == maidTiles[li][1] &&
//					pTile.getPos().getZ() == maidTiles[li][2]) {
//				return true;
//			}
//		}
//		return false;
//	}
//
//	public boolean isEqualTile() {
//		return worldObj.getTileEntity(new BlockPos(maidTile[0], maidTile[1], maidTile[2])) == maidTileEntity;
//	}
//
//	public boolean isTilePos() {
//		return maidTileEntity != null;
//	}
//	public boolean isTilePos(int pIndex) {
//		if (pIndex < maidTiles.length) {
//			return maidTiles[pIndex] != null;
//		}
//		return false;
//	}
//
//	/**
//	 * ローカル変数にTileの位置を入れる。
//	 */
//	public boolean getTilePos(int pIndex) {
//		if (pIndex < maidTiles.length && maidTiles[pIndex] != null) {
//			maidTile[0] = maidTiles[pIndex][0];
//			maidTile[1] = maidTiles[pIndex][1];
//			maidTile[2] = maidTiles[pIndex][2];
//			return true;
//		}
//		return false;
//	}
//
//	public void setTilePos(int pX, int pY, int pZ) {
//		maidTile[0] = pX;
//		maidTile[1] = pY;
//		maidTile[2] = pZ;
//	}
//	public void setTilePos(TileEntity pEntity) {
//		maidTile[0] = pEntity.getPos().getX();
//		maidTile[1] = pEntity.getPos().getY();
//		maidTile[2] = pEntity.getPos().getZ();
//		maidTileEntity = pEntity;
//	}
//	public void setTilePos(int pIndex) {
//		if (pIndex < maidTiles.length) {
//			if (maidTiles[pIndex] == null) {
//				maidTiles[pIndex] = new int[3];
//			}
//			maidTiles[pIndex][0] = maidTile[0];
//			maidTiles[pIndex][1] = maidTile[1];
//			maidTiles[pIndex][2] = maidTile[2];
//		}
//	}
//	public void setTilePos(int pIndex, int pX, int pY, int pZ) {
//		if (pIndex < maidTiles.length) {
//			if (maidTiles[pIndex] == null) {
//				maidTiles[pIndex] = new int[3];
//			}
//			maidTiles[pIndex][0] = pX;
//			maidTiles[pIndex][1] = pY;
//			maidTiles[pIndex][2] = pZ;
//		}
//	}
//
//	public TileEntity getTileEntity() {
//		return maidTileEntity = worldObj.getTileEntity(new BlockPos(maidTile[0], maidTile[1], maidTile[2]));
//	}
//	public TileEntity getTileEntity(int pIndex) {
//		if (pIndex < maidTiles.length && maidTiles[pIndex] != null) {
//			TileEntity ltile = worldObj.getTileEntity(new BlockPos(
//					maidTiles[pIndex][0], maidTiles[pIndex][1], maidTiles[pIndex][2]));
//			if (ltile == null) {
//				clearTilePos(pIndex);
//			}
//			return ltile;
//		}
//		return null;
//	}
//
//	public void clearTilePos() {
//		maidTileEntity = null;
//	}
//	public void clearTilePos(int pIndex) {
//		if (pIndex < maidTiles.length) {
//			maidTiles[pIndex] = null;
//		}
//	}
//	public void clearTilePosAll() {
//		for (int li = 0; li < maidTiles.length; li++) {
//			maidTiles[li] = null;
//		}
//	}
//
//	public double getDistanceTilePos() {
//		return getDistance(
//				maidTile[0] + 0.5D,
//				maidTile[1] + 0.5D,
//				maidTile[2] + 0.5D);
//	}
//	public double getDistanceTilePosSq() {
//		return getDistanceSq(
//				maidTile[0] + 0.5D,
//				maidTile[1] + 0.5D,
//				maidTile[2] + 0.5D);
//	}
//
//	public double getDistanceTilePos(int pIndex) {
//		if (maidTiles.length > pIndex && maidTiles[pIndex] != null) {
//			return getDistance(
//					maidTiles[pIndex][0] + 0.5D,
//					maidTiles[pIndex][1] + 0.5D,
//					maidTiles[pIndex][2] + 0.5D);
//		}
//		return -1D;
//	}
//	public double getDistanceTilePosSq(int pIndex) {
//		if (maidTiles.length > pIndex && maidTiles[pIndex] != null) {
//			return getDistanceSq(
//					maidTiles[pIndex][0] + 0.5D,
//					maidTiles[pIndex][1] + 0.5D,
//					maidTiles[pIndex][2] + 0.5D);
//		}
//		return -1D;
//	}
//	public double getDistanceTilePos(TileEntity pTile) {
//		if (pTile != null) {
//			return getDistance(
//					pTile.getPos().getX() + 0.5D,
//					pTile.getPos().getY() + 0.5D,
//					pTile.getPos().getZ() + 0.5D);
//		}
//		return -1D;
//	}
//	public double getDistanceTilePosSq(TileEntity pTile) {
//		if (pTile != null) {
//			return getDistanceSq(
//					pTile.getPos().getX() + 0.5D,
//					pTile.getPos().getY() + 0.5D,
//					pTile.getPos().getZ() + 0.5D);
//		}
//		return -1D;
//	}
//
//	public void looksTilePos() {
//		getLookHelper().setLookPosition(
//				maidTile[0] + 0.5D, maidTile[1] + 0.5D, maidTile[2] + 0.5D,
//				10F, getVerticalFaceSpeed());
//	}
//	public void looksTilePos(int pIndex) {
//		if (maidTiles.length > pIndex && maidTiles[pIndex] != null) {
//			getLookHelper().setLookPosition(
//					maidTiles[pIndex][0] + 0.5D,
//					maidTiles[pIndex][1] + 0.5D,
//					maidTiles[pIndex][2] + 0.5D,
//					10F, getVerticalFaceSpeed());
//		}
//	}
//
//	public boolean isUsingItem() {
//		return dataWatcher.get(EntityLittleMaid.dataWatch_ItemUse) > 0;
//	}
//
//	public boolean isUsingItem(int pIndex) {
//		return (dataWatcher.get(EntityLittleMaid.dataWatch_ItemUse) & (1 << pIndex)) > 0;
//	}
//
//	public void setExperienceValue(int val) {
////		experienceValue = val;
//	}
//
//	public void setFlag(int par1, boolean par2) {
//		super.setFlag(par1, par2);
//	}
//
//	//1.8検討
//	/*
//	public void updateWanderPath()
//	{
//		super.updateWanderPath();
//	}
//	*/
//
//	public void setSize2(float par1, float par2)
//	{
//		super.setSize(par1, par2);
//	}
//
//}
//
