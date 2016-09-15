//package aaamikansei;
//
//package net.minecraft.src;
//
//import static net.minecraft.src.LMM_Statics.*;
//
//import java.lang.reflect.Method;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//import java.util.UUID;
//
//public class LMM_EntityLittleMaid extends EntityTameable implements MMM_ITextureEntity {
//
//	// 定数はStaticsへ移動
////	protected static final UUID maidUUID = UUID.nameUUIDFromBytes("net.minecraft.src.littleMaidMob".getBytes());
//	protected static final UUID maidUUID = UUID.fromString("e2361272-644a-3028-8416-8536667f0efb");
////	protected static final UUID maidUUIDSneak = UUID.nameUUIDFromBytes("net.minecraft.src.littleMaidMob.sneak".getBytes());
//	protected static final UUID maidUUIDSneak = UUID.fromString("5649cf91-29bb-3a0c-8c31-b170a1045560");
//	protected static AttributeModifier attCombatSpeed = (new AttributeModifier(maidUUID, "Combat speed boost", 0.07D, 0)).func_111168_a(false);
//	protected static AttributeModifier attAxeAmp = (new AttributeModifier(maidUUID, "Axe Attack boost", 0.5D, 1)).func_111168_a(false);
//	protected static AttributeModifier attSneakingSpeed = (new AttributeModifier(maidUUIDSneak, "Sneking speed ampd", -0.4D, 2)).func_111168_a(false);
//
//
//	// 変数減らしたいなぁ
////    protected long maidContractLimit;		// 契約失効日
//	protected int maidContractLimit;		// 契約期間
//	protected long maidAnniversary;			// 契約日UIDとして使用
//	protected int maidDominantArm;			// 利き腕、1Byte
//	/** テクスチャ関連のデータを管理 **/
//	public MMM_TextureData textureData;
//	public Map<String, MMM_EquippedStabilizer> maidStabilizer = new HashMap<String, MMM_EquippedStabilizer>();
//
//
//	public LMM_InventoryLittleMaid maidInventory;
//	public LMM_EntityLittleMaidAvatar maidAvatar;
//	public LMM_EntityCaps maidCaps;	// Client側のみ
//
//	public List<LMM_EntityModeBase> maidEntityModeList;
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
//	protected MMM_Counter maidOverDriveTime;
//	protected boolean mstatFirstLook;
//	protected boolean mstatLookSuger;
//	protected MMM_Counter mstatWorkingCount;
//	protected int mstatPlayingRole;
//	protected int mstatWorkingInt;
//	protected String mstatModeName;
//	protected boolean mstatOpenInventory;
//	// 腕振り
//	public LMM_SwingStatus mstatSwingStatus[];
//	public boolean mstatAimeBow;
//	// 首周り
//	private boolean looksWithInterest;
//	private boolean looksWithInterestAXIS;
//	private float rotateAngleHead;			// Angle
//	private float prevRotateAngleHead;		// prevAngle
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
//
//	// 音声
////	protected LMM_EnumSound maidAttackSound;
//	protected LMM_EnumSound maidDamegeSound;
//	protected int maidSoundInterval;
//	protected float maidSoundRate;
//
//	// 実験用
//	private int firstload = 1;
//	public String statusMessage = "";
//
//
//	// AI
//	public EntityAITempt aiTempt;
//	public LMM_EntityAIBeg aiBeg;
//	public LMM_EntityAIBegMove aiBegMove;
//	public EntityAIOpenDoor aiOpenDoor;
//	public EntityAIRestrictOpenDoor aiCloseDoor;
//	public LMM_EntityAIAvoidPlayer aiAvoidPlayer;
//	public LMM_EntityAIFollowOwner aiFollow;
//	public LMM_EntityAIAttackOnCollide aiAttack;
//	public LMM_EntityAIAttackArrow aiShooting;
//	public LMM_EntityAICollectItem aiCollectItem;
//	public LMM_EntityAIRestrictRain aiRestrictRain;
//	public LMM_EntityAIFleeRain aiFreeRain;
//	public LMM_EntityAIWander aiWander;
//	public LMM_EntityAIJumpToMaster aiJumpTo;
//	public LMM_EntityAIFindBlock aiFindBlock;
//	public LMM_EntityAITracerMove aiTracer;
//	public EntityAISwimming aiSwiming;
//	public EntityAIPanic aiPanic;
//	// ActiveModeClass
//	protected LMM_EntityModeBase maidActiveModeClass;
//	public Profiler aiProfiler;
//
//
//
//	@Override
//	public void onDeath(DamageSource par1DamageSource) {
//		super.onDeath(par1DamageSource);
//
//		// 死因を表示
//		if (!worldObj.isRemote) {
//			// マスター判定失敗するかも？
//			if (mod_LMM_littleMaidMob.cfg_DeathMessage && mstatMasterEntity != null) {
//				String ls = par1DamageSource.getDamageType();
//				Entity lentity = par1DamageSource.getEntity();
//				if (lentity != null) {
//					if (par1DamageSource.getEntity() instanceof EntityPlayer) {
//						ls += ":" + ((EntityPlayer)lentity).username;
//					} else {
//						String lt = EntityList.getEntityString(lentity);
//						if (lt != null) {
//							ls += ":" + lt;
//						}
//					}
//				}
//				String lt = getTranslatedEntityName();
//				mstatMasterEntity.addChatMessage(String.format("your %s killed by %s", lt, ls));
//			}
//		}
//	}
//
//	// ポーションエフェクト
//	@Override
//	protected void onNewPotionEffect(PotionEffect par1PotionEffect) {
//		super.onNewPotionEffect(par1PotionEffect);
//		if (mstatMasterEntity instanceof EntityPlayerMP) {
//			((EntityPlayerMP)mstatMasterEntity).playerNetServerHandler.sendPacketToPlayer(new Packet41EntityEffect(this.entityId, par1PotionEffect));
//		}
//	}
//
//	@Override
//	protected void onChangedPotionEffect(PotionEffect par1PotionEffect, boolean par2) {
//		super.onChangedPotionEffect(par1PotionEffect, par2);
//		// TODO:必要かどうかのチェック
////		if (mstatMasterEntity instanceof EntityPlayerMP) {
////			((EntityPlayerMP)mstatMasterEntity).playerNetServerHandler.sendPacketToPlayer(new Packet41EntityEffect(this.entityId, par1PotionEffect));
////		}
//	}
//
//	@Override
//	protected void onFinishedPotionEffect(PotionEffect par1PotionEffect) {
//		super.onFinishedPotionEffect(par1PotionEffect);
//		if (mstatMasterEntity instanceof EntityPlayerMP) {
//			((EntityPlayerMP)mstatMasterEntity).playerNetServerHandler.sendPacketToPlayer(new Packet42RemoveEntityEffect(this.entityId, par1PotionEffect));
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
//		checkMaskedMaid();
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
//			// クライアント側は処理しない
//			return false;
//		}
//
//		int li;
//		if (isActiveModeClass()) {
//			li = getActiveModeClass().getNextEquipItem(maidMode);
//		} else {
//			li = -1;
//		}
//		setEquipItem(maidDominantArm, li);
//		return li > -1;
//	}
//
//	public void setEquipItem(int pArm, int pIndex) {
//		if (pArm == maidDominantArm) {
//			maidInventory.currentItem = pIndex;
//		}
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
//		setEquipItem(maidDominantArm, pIndex);
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
//	@Override
//	public ItemStack getHeldItem() {
//		return maidInventory.getCurrentItem();
//	}
//
//	@Override
//	public ItemStack getCurrentItemOrArmor(int par1) {
//		if (par1 == 0) {
//			return getHeldItem();
//		} else if (par1 < 5) {
//			return maidInventory.armorItemInSlot(par1 - 1);
//		} else {
//			return maidInventory.getStackInSlot(par1 - 5);
//		}
//	}
//
//	@Override
//	public ItemStack func_130225_q(int par1) {
//		return maidInventory.armorItemInSlot(par1);
//	}
//
//	@Override
//	public void setCurrentItemOrArmor(int par1, ItemStack par2ItemStack) {
//		par1 &= 0x0000ffff;
//		if (par1 == 0) {
//			maidInventory.setInventoryCurrentSlotContents(par2ItemStack);
//		} else if (par1 > 0 && par1 < 4) {
//			maidInventory.armorInventory[par1 - 1] = par2ItemStack;
//			setTextureNames();
//		} else if (par1 == 4) {
////			maidInventory.mainInventory[mstatMaskSelect] = mstatMaskSelect > -1 ? par2ItemStack : null;
//			if (mstatMaskSelect > -1) {
//				maidInventory.mainInventory[mstatMaskSelect] = par2ItemStack;
//			}
//			setTextureNames();
//		} else {
//			par1 -= 5;
//			// 持ち物のアップデート
//			// 独自拡張:普通にスロット番号の通り、上位８ビットは装備スロット
//			// par1はShortで渡されるのでそのように。
//			int lslotindex = par1 & 0x7f;
//			int lequip = (par1 >>> 8) & 0xff;
//			maidInventory.setInventorySlotContents(lslotindex, par2ItemStack);
//			maidInventory.resetChanged(lslotindex);	// これは意味ないけどな。
//			maidInventory.inventoryChanged = true;
////			if (par1 >= maidInventory.mainInventory.length) {
////				LMM_Client.setArmorTextureValue(this);
////			}
//
//			for (LMM_SwingStatus lss: mstatSwingStatus) {
//				if (lslotindex == lss.index) {
//					lss.index = -1;
//				}
//			}
//			if (lequip != 0xff) {
//				setEquipItem(lequip, lslotindex);
////				mstatSwingStatus[lequip].index = lslotindex;
//			}
//			if (lslotindex >= maidInventory.maxInventorySize) {
//				setTextureNames();
//			}
//			String s = par2ItemStack == null ? null : par2ItemStack.getItemName();
//			mod_LMM_littleMaidMob.Debug(String.format("ID:%d Slot(%2d:%d):%s", entityId, lslotindex, lequip, s == null ? "NoItem" : s));
//		}
//	}
//
//	@Override
//	public ItemStack[] getLastActiveItems() {
//		return maidInventory.armorInventory;
//	}
//
//	protected void checkClockMaid() {
//		// 時計を持っているか？
//		mstatClockMaid = maidInventory.getInventorySlotContainItem(Item.pocketSundial.itemID) > -1;
//	}
//	/**
//	 * 時計を持っているか?
//	 */
//	public boolean isClockMaid() {
//		return mstatClockMaid;
//	}
//
//	protected void checkMaskedMaid() {
//		// インベントリにヘルムがあるか？
//		for (int i = maidInventory.mainInventory.length - 1; i >= 0; i--) {
//			ItemStack is = maidInventory.getStackInSlot(i);
//			if (is != null && is.getItem() instanceof ItemArmor && ((ItemArmor)is.getItem()).armorType == 0) {
//				// ヘルムを持ってる
//				mstatMaskSelect = i;
//				maidInventory.armorInventory[3] = is;
//				if (worldObj.isRemote) {
//					setTextureNames();
//				}
//				return;
//			}
//		}
//
//		mstatMaskSelect = -1;
//		maidInventory.armorInventory[3] = null;
//		return;
//	}
//	/**
//	 * メットを被ってるか
//	 */
//	public boolean isMaskedMaid() {
//		return mstatMaskSelect > -1;
//	}
//
//	protected void checkHeadMount() {
//		// 追加の頭部装備の判定
//		ItemStack lis = maidInventory.getHeadMount();
//		mstatPlanter = false;
//		mstatCamouflage = false;
//		if (lis != null) {
//			if (lis.getItem() instanceof ItemBlock) {
//				Block lblock = Block.blocksList[lis.getItem().itemID];
//				mstatPlanter = (lblock instanceof BlockFlower) && lblock.getRenderType() == 1;
//				mstatCamouflage = (lblock instanceof BlockLeaves) || (lblock instanceof BlockPumpkin);
//			} else if (lis.getItem() instanceof ItemSkull) {
//				mstatCamouflage = true;
//			}
//		}
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
//		if (isPotionActive(Potion.digSpeed)) {
//			return 6 - (1 + getActivePotionEffect(Potion.digSpeed).getAmplifier()) * 1;
//		}
//
//		if (isPotionActive(Potion.digSlowdown)) {
//			return 6 + (1 + getActivePotionEffect(Potion.digSlowdown).getAmplifier()) * 2;
//		} else {
//			return 6;
//		}
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
//			// server
//			Container lcontainer = new LMM_ContainerInventory(pEntityPlayer.inventory, this);
//			ModLoader.serverOpenWindow((EntityPlayerMP)pEntityPlayer, lcontainer, mod_LMM_littleMaidMob.containerID, entityId, 0, 0);
//		}
//	}
//
//	@Override
//	public boolean interact(EntityPlayer par1EntityPlayer) {
//		float lhealth = func_110143_aJ();
//		ItemStack itemstack1 = par1EntityPlayer.getCurrentEquippedItem();
//
//		// プラグインでの処理を先に行う
//		for (int li = 0; li < maidEntityModeList.size(); li++) {
//			if (maidEntityModeList.get(li).preInteract(par1EntityPlayer, itemstack1)) {
//				return true;
//			}
//		}
//		// しゃがみ時は処理無効
//		if (par1EntityPlayer.isSneaking()) {
//			return false;
//		}
//		// ナデリ判定
//		if (lhealth > 0F && par1EntityPlayer.riddenByEntity != null && !(par1EntityPlayer.riddenByEntity instanceof LMM_EntityLittleMaid)) {
//			// 載せ替え
//			par1EntityPlayer.riddenByEntity.mountEntity(this);
//			return true;
//		}
//
//
//
//		if (mstatgotcha == null && par1EntityPlayer.fishEntity == null) {
//			if(itemstack1 != null && itemstack1.itemID == Item.silk.itemID) {
//				// 紐で繋ぐ
//				setGotcha(par1EntityPlayer.entityId);
//				mstatgotcha = par1EntityPlayer;
//				MMM_Helper.decPlayerInventory(par1EntityPlayer, -1, 1);
//				playSound("random.pop");
//				return true;
//			}
//
//			if (isContract()) {
//				// 契約状態
//				if (lhealth > 0F && isMaidContractOwner(par1EntityPlayer)) {
//					if (itemstack1 != null) {
//						// 追加分の処理
//						setPathToEntity(null);
//						// プラグインでの処理を先に行う
//						for (int li = 0; li < maidEntityModeList.size(); li++) {
//							if (maidEntityModeList.get(li).interact(par1EntityPlayer, itemstack1)) {
//								return true;
//							}
//						}
//						if (isRemainsContract()) {
//							// 通常
//							if (itemstack1.itemID == Item.sugar.itemID) {
//								// モード切替
//								MMM_Helper.decPlayerInventory(par1EntityPlayer, -1, 1);
//								eatSugar(false, true);
//								worldObj.setEntityState(this, (byte)11);
//
//								mod_LMM_littleMaidMob.Debug("give suger." + worldObj.isRemote);
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
////	    									maidInventory.currentItem = -1;
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
//							else if (itemstack1.itemID == Item.dyePowder.itemID) {
//								// カラーメイド
//								if (!worldObj.isRemote) {
//									setColor(15 - itemstack1.getItemDamage());
//								}
//								MMM_Helper.decPlayerInventory(par1EntityPlayer, -1, 1);
//								return true;
//							}
//							else if (itemstack1.itemID == Item.feather.itemID) {
//								// 自由行動
//								MMM_Helper.decPlayerInventory(par1EntityPlayer, -1, 1);
//								setFreedom(!isFreedom());
//								worldObj.setEntityState(this, isFreedom() ? (byte)12 : (byte)13);
//								return true;
//							}
//							else if (itemstack1.itemID == Item.saddle.itemID) {
//								// 肩車
//								if (!worldObj.isRemote) {
//									if (ridingEntity == par1EntityPlayer) {
//										this.mountEntity(null);
//									} else {
//										this.mountEntity(par1EntityPlayer);
//									}
//									return true;
//								}
//							}
//							else if (itemstack1.itemID == Item.gunpowder.itemID) {
//								// test TNT-D
////								playSound(LMM_EnumSound.eatGunpowder, false);
//								maidOverDriveTime.setValue(itemstack1.stackSize * 10);
//								MMM_Helper.decPlayerInventory(par1EntityPlayer, -1, itemstack1.stackSize);
//								return true;
//							}
//							else if (itemstack1.itemID == Item.book.itemID) {
//								// IFFのオープン
//								MMM_Helper.decPlayerInventory(par1EntityPlayer, -1, 1);
////	    		            	ModLoader.openGUI(par1EntityPlayer, new LMM_GuiIFF(worldObj, this));
//								if (worldObj.isRemote) {
//									LMM_Client.OpenIFF(this, par1EntityPlayer);
//								}
//								return true;
//							}
//							else if ((itemstack1.itemID == Item.glassBottle.itemID) && (experienceValue >= 5)) {
//								// Expボトル
//								MMM_Helper.decPlayerInventory(par1EntityPlayer, -1, 1);
//								if (!worldObj.isRemote) {
//									entityDropItem(new ItemStack(Item.expBottle), 0.5F);
//									experienceValue -= 5;
//									if (maidAvatar != null) {
//										maidAvatar.experienceTotal -= 5;
//									}
//								}
//								return true;
//							}
//							else if (itemstack1.getItem() instanceof ItemPotion) {
//								// ポーション
//								if(!worldObj.isRemote) {
//									List list = ((ItemPotion)itemstack1.getItem()).getEffects(itemstack1);
//									if (list != null) {
//										PotionEffect potioneffect;
//										for (Iterator iterator = list.iterator(); iterator.hasNext(); addPotionEffect(new PotionEffect(potioneffect))) {
//											potioneffect = (PotionEffect)iterator.next();
//										}
//									}
//								}
//								MMM_Helper.decPlayerInventory(par1EntityPlayer, -1, 1);
//								return true;
//							}
//							else if (isFreedom() && itemstack1.itemID == Item.redstone.itemID) {
//								// Tracer
//								MMM_Helper.decPlayerInventory(par1EntityPlayer, -1, 1);
//								setPathToEntity(null);
//								setMaidWait(false);
//								setTracer(!isTracer());
//								if (isTracer()) {
//									worldObj.setEntityState(this, (byte)14);
//								} else {
//									worldObj.setEntityState(this, (byte)12);
//								}
//
//								return true;
//							}
//						} else {
//							// ストライキ
//							if (itemstack1.itemID == Item.sugar.itemID) {
//								// 受取拒否
//								worldObj.setEntityState(this, (byte)10);
//								return true;
//							} else if (itemstack1.itemID == Item.cake.itemID) {
//								// 再契約
//								MMM_Helper.decPlayerInventory(par1EntityPlayer, -1, 1);
//								maidContractLimit = (24000 * 7);
//								setFreedom(false);
//								setTracer(false);
//								setMaidWait(false);
//								setMaidMode("Escorter");
//								worldObj.setEntityState(this, (byte)11);
//								playSound(LMM_EnumSound.Recontract, true);
//								return true;
//							}
//						}
//					}
//					// メイドインベントリ
//					setOwner(par1EntityPlayer.username);
//					getNavigator().clearPathEntity();
//					isJumping = false;
//					displayGUIMaidInventory(par1EntityPlayer);
////    		        	ModLoader.openGUI(par1EntityPlayer, new LMM_GuiInventory(this, par1EntityPlayer.inventory, maidInventory));
////    				serchedChest.clear();
//					return true;
//				}
//			} else {
//				// 未契約
//				if (itemstack1 != null) {
//					if (itemstack1.itemID == Item.cake.itemID) {
//						// 契約
//						MMM_Helper.decPlayerInventory(par1EntityPlayer, -1, 1);
//
//						deathTime = 0;
//						if (!worldObj.isRemote) {
//							if (mod_LMM_littleMaidMob.ac_Contract != null) {
//								par1EntityPlayer.triggerAchievement(mod_LMM_littleMaidMob.ac_Contract);
//							}
//							setContract(true);
//							setOwner(par1EntityPlayer.username);
//							setEntityHealth(20);
//							setMaidMode("Escorter");
//							setMaidWait(false);
//							setFreedom(false);
//							playSound(LMM_EnumSound.getCake, true);
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
//					} else {
////    	                worldObj.setEntityState(this, (byte)6);
//					}
//				}
//			}
//		} else if (lhealth > 0F && mstatgotcha != null) {
//			if (!worldObj.isRemote) {
//				EntityItem entityitem = new EntityItem(worldObj, mstatgotcha.posX, mstatgotcha.posY, mstatgotcha.posZ, new ItemStack(Item.silk));
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
//		if (flag) {
////        	maidMode = mmode_Escorter;
//		} else {
//		}
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
//		return maidContractLimit > 0 ? ((float)maidContractLimit / 24000F) : -1F;
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
//	public Entity getOwner() {
//		return getMaidMasterEntity();
//	}
//	public String getMaidMaster() {
//		return getOwnerName();
//	}
//
//	public EntityPlayer getMaidMasterEntity() {
//		// 主を獲得
//		if (isContract()) {
//			EntityPlayer entityplayer = mstatMasterEntity;
//			if (mstatMasterEntity == null || mstatMasterEntity.isDead) {
//				String lname;
//				// サーバー側ならちゃんとオーナ判定する
//				if (!MMM_Helper.isClient
//						|| mod_LMM_littleMaidMob.cfg_checkOwnerName
//						|| MMM_Helper.mc.thePlayer == null) {
//					lname = getMaidMaster();
//				} else {
//					lname = MMM_Helper.mc.thePlayer.username;
//				}
//				entityplayer = worldObj.getPlayerEntityByName(lname);
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
//		} else {
//			return null;
//		}
//	}
//
//	public boolean isMaidContractOwner(String pname) {
//		return pname.equalsIgnoreCase(mstatMasterEntity.username);
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
//
//		aiSit.setSitting(pflag);
//		maidWait = pflag;
//		isJumping = false;
//		setAttackTarget(null);
//		setRevengeTarget(null);
//		setPathToEntity(null);
//		getNavigator().clearPathEntity();
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
//	public void setSwing(int attacktime, LMM_EnumSound enumsound) {
//		setSwing(attacktime, enumsound, maidDominantArm);
//	}
//	public void setSwing(int pattacktime, LMM_EnumSound enumsound, int pArm) {
//		mstatSwingStatus[pArm].attackTime = pattacktime;
////		maidAttackSound = enumsound;
////        soundInterval = 0;// いるか？
//		if (!weaponFullAuto) {
//			setSwinging(pArm, enumsound);
//		}
//		if (!worldObj.isRemote) {
//			byte[] lba = new byte[] {
//				LMM_Statics.LMN_Client_SwingArm,
//				0, 0, 0, 0,
//				(byte)pArm,
//				0, 0, 0, 0
//			};
//			MMM_Helper.setInt(lba, 6, enumsound.index);
//			LMM_Net.sendToAllEClient(this, lba);
//		}
//	}
//
//	public void setSwinging(LMM_EnumSound pSound) {
//		setSwinging(maidDominantArm, pSound);
//	}
//	public void setSwinging(int pArm, LMM_EnumSound pSound) {
//		if (mstatSwingStatus[pArm].setSwinging()) {
//			playLittleMaidSound(pSound, true);
//			maidAvatar.field_110158_av = -1;
////			maidAvatar.swingProgressInt = -1;
//			maidAvatar.isSwingInProgress = true;
//		}
//	}
//
//	public boolean getSwinging() {
//		return getSwinging(maidDominantArm);
//	}
//	public boolean getSwinging(int pArm) {
//		return mstatSwingStatus[pArm].isSwingInProgress;
//	}
//
//	/**
//	 * 利き腕のリロードタイム
//	 */
//	public LMM_SwingStatus getSwingStatusDominant() {
//		return mstatSwingStatus[maidDominantArm];
//	}
//
//	public LMM_SwingStatus getSwingStatus(int pindex) {
//		return mstatSwingStatus[pindex];
//	}
//
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
//	/**
//	 * ペロッ・・・これは・・・砂糖ッ！！
//	 * motion : 腕を振るか？
//	 * recontract : 契約延長効果アリ？
//	 */
//	public void eatSugar(boolean motion, boolean recontract) {
//		if (motion) {
//			setSwing(2, (func_110138_aP() - func_110143_aJ() <= 1F) ?  LMM_EnumSound.eatSugar_MaxPower : LMM_EnumSound.eatSugar);
//		}
//		int h = hurtResistantTime;
//		heal(1);
//		hurtResistantTime = h;
//		playSound("random.pop");
//		mod_LMM_littleMaidMob.Debug(("eat Suger." + worldObj.isRemote));
//
//		if (recontract) {
//			// 契約期間の延長
//			maidContractLimit += 24000;
//			if (maidContractLimit > 168000) {
//				maidContractLimit = 168000;	// 24000 * 7
//			}
//		}
//
//		// 暫定処理
//		if (maidAvatar != null) {
//			maidAvatar.foodStats.addStats(20, 20F);
//		}
//	}
//
//
//	// お仕事チュ
//	/**
//	 * 仕事中かどうかの設定
//	 */
//	public void setWorking(boolean pFlag) {
//		mstatWorkingCount.setEnable(pFlag);
//	}
//
//	/**
//	 * 仕事中かどうかを返す
//	 */
//	public boolean isWorking() {
//		return mstatWorkingCount.isEnable();
//	}
//
//	/**
//	 * 仕事が終了しても余韻を含めて返す
//	 */
//	public boolean isWorkingDelay() {
//		return mstatWorkingCount.isDelay();
//	}
//
//	/**
//	 * トレーサーモードの設定
//	 */
//	public void setTracer(boolean pFlag) {
//		maidTracer = pFlag;
//		setMaidFlags(pFlag, dataWatch_Flags_Tracer);
//		if (maidTracer) {
//			setFreedom(true);
//		}
//		aiTracer.setEnable(pFlag);
//	}
//
//	/**
//	 * トレーサーモードであるか？
//	 */
//	public boolean isTracer() {
//		return maidTracer;
//	}
//
//
//	// お遊びモード
//	public void setPlayingRole(int pValue) {
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
//		aiRestrictRain.setEnable(pFlag);
//		aiFreeRain.setEnable(pFlag);
//		aiWander.setEnable(pFlag);
////		aiJumpTo.setEnable(!pFlag);
//		aiAvoidPlayer.setEnable(!pFlag);
//		aiFollow.setEnable(!pFlag);
//		aiTracer.setEnable(false);
////		setAIMoveSpeed(pFlag ? moveSpeed_Nomal : moveSpeed_Max);
////		setMoveForward(0.0F);
//
//		if (maidFreedom && isContract()) {
//			func_110171_b(
////			setHomeArea(
//					MathHelper.floor_double(posX),
//					MathHelper.floor_double(posY),
//					MathHelper.floor_double(posZ), 16);
//		} else {
//			func_110177_bN();
////			detachHome();
//			setPlayingRole(0);
//		}
//
//		setMaidFlags(maidFreedom, dataWatch_Flags_Freedom);
//	}
//
//	public boolean isFreedom() {
//		return maidFreedom;
//	}
//
//
//	/**
//	 * サーバーへテクスチャパックのインデックスを送る。
//	 * クライアント側の処理
//	 */
//	protected boolean sendTextureToServer() {
//		// 16bitあればテクスチャパックの数にたりんべ
//		MMM_TextureManager.instance.postSetTexturePack(this, textureData.getColor(), textureData.getTextureBox());
//		return true;
//	}
//
//
//	public boolean updateTexturePack() {
//		// テクスチャパックが更新されていないかをチェック
//		// クライアント側の
//		boolean lflag = false;
//		MMM_TextureBoxServer lbox;
//
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
//	}
//
//	@Override
//	public int getColor() {
////		return textureData.getColor();
//		return dataWatcher.getWatchableObjectByte(dataWatch_Color);
//	}
//
//	@Override
//	public void setColor(int index) {
//		textureData.setColor(index);
//		dataWatcher.updateObject(dataWatch_Color, (byte)index);
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
//		int lid = dataWatcher.getWatchableObjectInt(dataWatch_Gotcha);
//		if (lid == 0) {
//			mstatgotcha = null;
//			return;
//		}
//		if (mstatgotcha != null && mstatgotcha.entityId == lid) {
//			return;
//		}
//		for (int li = 0; li < worldObj.loadedEntityList.size(); li++) {
//			if (((Entity)worldObj.loadedEntityList.get(li)).entityId == lid) {
//				mstatgotcha = (Entity)worldObj.loadedEntityList.get(li);
//				break;
//			}
//		}
//	}
//
//	public void setGotcha(int pEntityID) {
//		dataWatcher.updateObject(dataWatch_Gotcha, Integer.valueOf(pEntityID));
//	}
//	public void setGotcha(Entity pEntity) {
//		setGotcha(pEntity == null ? 0 : pEntity.entityId);
//	}
//
//
//	/**
//	 * 弓構えを更新
//	 */
//	public void updateAimebow() {
//		boolean lflag = (maidAvatar != null && maidAvatar.isUsingItemLittleMaid()) || mstatAimeBow;
//		setMaidFlags(lflag, dataWatch_Flags_Aimebow);
//	}
//
//	public boolean isAimebow() {
//		return (dataWatcher.getWatchableObjectInt(dataWatch_Flags) & dataWatch_Flags_Aimebow) > 0;
//	}
//
//
//	/**
//	 * 各種フラグのアップデート
//	 */
//	public void updateMaidFlagsClient() {
//		int li = dataWatcher.getWatchableObjectInt(dataWatch_Flags);
//		maidFreedom = (li & dataWatch_Flags_Freedom) > 0;
//		maidTracer = (li & dataWatch_Flags_Tracer) > 0;
//		maidWait = (li & dataWatch_Flags_Wait) > 0;
//		mstatAimeBow = (li & dataWatch_Flags_Aimebow) > 0;
//		mstatLookSuger = (li & dataWatch_Flags_LooksSugar) > 0;
//		mstatBloodsuck = (li & dataWatch_Flags_Bloodsuck) > 0;
//		looksWithInterest = (li & dataWatch_Flags_looksWithInterest) > 0;
//		looksWithInterestAXIS = (li & dataWatch_Flags_looksWithInterestAXIS) > 0;
//		maidOverDriveTime.updateClient((li & dataWatch_Flags_OverDrive) > 0);
//		mstatWorkingCount.updateClient((li & dataWatch_Flags_Working) > 0);
//	}
//
//	/**
//	 * フラグ群に値をセット。
//	 * @param pCheck： 対象値。
//	 * @param pFlags： 対象フラグ。
//	 */
//	public void setMaidFlags(boolean pFlag, int pFlagvalue) {
//		int li = dataWatcher.getWatchableObjectInt(dataWatch_Flags);
//		li = pFlag ? (li | pFlagvalue) : (li & ~pFlagvalue);
//		dataWatcher.updateObject(dataWatch_Flags, Integer.valueOf(li));
//	}
//
//	/**
//	 * 指定されたフラグを獲得
//	 */
//	public boolean getMaidFlags(int pFlagvalue) {
//		return (dataWatcher.getWatchableObjectInt(dataWatch_Flags) & pFlagvalue) > 0;
//	}
//
//	@Override
//	public void func_110171_b(int par1, int par2, int par3, int par4) {
//		homeWorld = dimension;
//		super.func_110171_b(par1, par2, par3, par4);
////		super.setHomeArea(par1, par2, par3, par4);
//	}
//
//	@Override
//	public void setTexturePackIndex(int pColor, int[] pIndex) {
//		// Server
//		textureData.setTexturePackIndex(pColor, pIndex);
//		dataWatcher.updateObject(dataWatch_Texture, ((textureData.textureIndex[0] & 0xffff) | (textureData.textureIndex[1] & 0xffff) << 16));
//		mod_LMM_littleMaidMob.Debug("changeSize-ID:%d: %f, %f, %b", entityId, width, height, worldObj.isRemote);
//		setColor(pColor);
//		setTextureNames();
//	}
//
//	@Override
//	public void setTexturePackName(MMM_TextureBox[] pTextureBox) {
//		// Client
//		textureData.setTexturePackName(pTextureBox);
//		setTextureNames();
//		mod_LMM_littleMaidMob.Debug("ID:%d, TextureModel:%s", entityId, textureData.getTextureName(0));
//		// モデルの初期化
//		((MMM_TextureBox)textureData.textureBox[0]).models[0].setCapsValue(MMM_IModelCaps.caps_changeModel, maidCaps);
//		// スタビの付け替え
////		for (Entry<String, MMM_EquippedStabilizer> le : pEntity.maidStabilizer.entrySet()) {
////			if (le.getValue() != null) {
////				le.getValue().updateEquippedPoint(pEntity.textureModel0);
////			}
////		}
//		maidSoundRate = LMM_SoundManager.getSoundRate(textureData.getTextureName(0), getColor());
//
//	}
//
//	/**
//	 * Client用
//	 */
//	public void setTextureNames() {
//		if (!textureData.setTextureNames()) {
//			// TODO:setDefaultTexture
////			if (worldObj.isRemote) {
//				setNextTexturePackege(0);
////			}
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
//
//	@Override
//	public void setTextureBox(MMM_TextureBoxBase[] pTextureBox) {
//		textureData.setTextureBox(pTextureBox);
//	}
//
//	@Override
//	public MMM_TextureBoxBase[] getTextureBox() {
//		return textureData.getTextureBox();
//	}
//
//	@Override
//	public void setTextureIndex(int[] pTextureIndex) {
//		textureData.setTextureIndex(pTextureIndex);
//	}
//
//	@Override
//	public int[] getTextureIndex() {
//		return textureData.getTextureIndex();
//	}
//
//	@Override
//	public void setTextures(int pIndex, ResourceLocation[] pNames) {
//		textureData.setTextures(pIndex, pNames);
//	}
//
//	@Override
//	public ResourceLocation[] getTextures(int pIndex) {
//		return textureData.getTextures(pIndex);
//	}
//
//	@Override
//	public MMM_TextureData getTextureData() {
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
//					pTile.xCoord == maidTiles[li][0] &&
//					pTile.yCoord == maidTiles[li][1] &&
//					pTile.zCoord == maidTiles[li][2]) {
//				return true;
//			}
//		}
//		return false;
//	}
//
//	public boolean isEqualTile() {
//		return worldObj.getBlockTileEntity(maidTile[0], maidTile[1], maidTile[2]) == maidTileEntity;
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
//		maidTile[0] = pEntity.xCoord;
//		maidTile[1] = pEntity.yCoord;
//		maidTile[2] = pEntity.zCoord;
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
//		return maidTileEntity = worldObj.getBlockTileEntity(maidTile[0], maidTile[1], maidTile[2]);
//	}
//	public TileEntity getTileEntity(int pIndex) {
//		if (pIndex < maidTiles.length && maidTiles[pIndex] != null) {
//			TileEntity ltile = worldObj.getBlockTileEntity(
//					maidTiles[pIndex][0], maidTiles[pIndex][1], maidTiles[pIndex][2]);
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
//				(double)maidTile[0] + 0.5D,
//				(double)maidTile[1] + 0.5D,
//				(double)maidTile[2] + 0.5D);
//	}
//	public double getDistanceTilePosSq() {
//		return getDistanceSq(
//				(double)maidTile[0] + 0.5D,
//				(double)maidTile[1] + 0.5D,
//				(double)maidTile[2] + 0.5D);
//	}
//
//	public double getDistanceTilePos(int pIndex) {
//		if (maidTiles.length > pIndex && maidTiles[pIndex] != null) {
//			return getDistance(
//					(double)maidTiles[pIndex][0] + 0.5D,
//					(double)maidTiles[pIndex][1] + 0.5D,
//					(double)maidTiles[pIndex][2] + 0.5D);
//		}
//		return -1D;
//	}
//	public double getDistanceTilePosSq(int pIndex) {
//		if (maidTiles.length > pIndex && maidTiles[pIndex] != null) {
//			return getDistanceSq(
//					(double)maidTiles[pIndex][0] + 0.5D,
//					(double)maidTiles[pIndex][1] + 0.5D,
//					(double)maidTiles[pIndex][2] + 0.5D);
//		}
//		return -1D;
//	}
//	public double getDistanceTilePos(TileEntity pTile) {
//		if (pTile != null) {
//			return getDistance(
//					(double)pTile.xCoord + 0.5D,
//					(double)pTile.yCoord + 0.5D,
//					(double)pTile.zCoord + 0.5D);
//		}
//		return -1D;
//	}
//	public double getDistanceTilePosSq(TileEntity pTile) {
//		if (pTile != null) {
//			return getDistanceSq(
//					(double)pTile.xCoord + 0.5D,
//					(double)pTile.yCoord + 0.5D,
//					(double)pTile.zCoord + 0.5D);
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
//		return dataWatcher.getWatchableObjectInt(dataWatch_ItemUse) > 0;
//	}
//
//	public boolean isUsingItem(int pIndex) {
//		return (dataWatcher.getWatchableObjectInt(dataWatch_ItemUse) & (1 << pIndex)) > 0;
//	}
//
//}
