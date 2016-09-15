package aaamikansei;
import java.util.Collection;
import java.util.UUID;

import com.mojang.authlib.GameProfile;

import basashi.erm.container.InventoryERMBase;
import basashi.erm.entity.EntityERMBase;
import basashi.erm.entity.IEntityAvatar;
import basashi.erm.util.Values.KIND_STATUS;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;


public class EntityERMAvater extends EntityPlayer implements IEntityAvatar{

	public EntityERMBase avatar;
	public InventoryERMBase inventory;
	/** いらん？ **/
	public boolean isItemTrigger;
	/** いらん？ **/
	public boolean isItemReload;
	/** いらん？ **/
	private boolean isItemPreReload;
	private double appendX;
	private double appendY;
	private double appendZ;



	public EntityERMAvater(World par1World, EntityERMBase entity) {
		super(par1World, new GameProfile(UUID.randomUUID(), entity.EntityName()));

		// 初期設定
		avatar = entity;
		dataWatcher = avatar.getDataManager();

		inventory = avatar.getInventory();
	}

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
	}

	@Override
	public void onUpdate()
	{
		EntityPlayer lep = (EntityPlayer)avatar.getOwner();
		setEntityId(avatar.getEntityId());

		if (lep != null) {
			capabilities.isCreativeMode = lep.capabilities.isCreativeMode;
		}

		if (xpCooldown > 0) {
			xpCooldown--;
		}
		avatar.getStatus(KIND_STATUS.STATUS_EXP).setStatus(experienceTotal);
	}

	@Override
	public void playSound(SoundEvent soundIn, float volume, float pitch)
	{
		avatar.playSound(soundIn, volume, pitch);
	}

	@Override
	public int getScore() {
		return 0;
	}

	@Override
	public void setScore(int scoreIn)
	{

	}

	@Override
	public void addScore(int scoreIn)
	{
	}

	@Override
	protected SoundEvent getHurtSound()
	{
		return null;
	}

	@Override
	protected SoundEvent getDeathSound()
	{
		return null;
	}

	@Override
	public void attackTargetEntityWithCurrentItem(Entity targetEntity)
	{
		super.attackTargetEntityWithCurrentItem(targetEntity);
	}

	@Override
	public void onCriticalHit(Entity entityHit)
	{
		if (worldObj.isRemote) {
			// TODO: ?????
			//LMM_Client.onCriticalHit(this, entityHit);
		} else {
		}
	}

	@Override
	public void onEnchantmentCritical(Entity entityHit)
	{
		if (worldObj.isRemote) {
			// TODO:????
			//LittleMaidReengaged.proxy.onEnchantmentCritical(this, par1Entity);
		} else {
		}
	}

	@Override
	public void onKillEntity(EntityLivingBase entityLivingIn)
	{
		avatar.onKillEntity(entityLivingIn);
	}

	@Override
	public ItemStack getItemStackFromSlot(EntityEquipmentSlot slotIn)
	{
		return avatar.getItemStackFromSlot(slotIn);
	}

	@Override
	public boolean isSpectator(){
		return false;
	}

	@Override
	public boolean isCreative(){
		return false;
	}

	@Override
	public float getEyeHeight()
	{
		return avatar.getEyeHeight();
	}

	@Override
	public void setAbsorptionAmount(float amount)
	{
		avatar.setAbsorptionAmount(amount);
	}

	@Override
	public float getAbsorptionAmount()
	{
		return avatar.getAbsorptionAmount();
	}




	@Override
	public boolean canCommandSenderUseCommand(int var1, String var2) {
		return false;
	}

	@Override
	public void addStat(StatBase par1StatBase, int par2) {}

	@Override
	public void onItemPickup(Entity entity, int i) {
		// アイテム回収のエフェクト
		if (worldObj.isRemote) {
			// Client
			//TODO: アイテムピックアップ
			//LittleMaidReengaged.proxy.onItemPickup(this, entity, i);
		} else {
			super.onItemPickup(entity, i);
		}
	}

	protected Entity getEntityServer() {
		return worldObj.isRemote ? null : this;
	}

	// Item使用関連

	public int getItemInUseDuration(int pIndex) {
		return avatar.getSwingStatus(pIndex).getItemInUseDuration();
	}

	public ItemStack getItemInUse(int pIndex) {
		// TODO:
		//return avatar.getSwingStatus(pIndex).getItemInUse();
		return null;
	}

	public ItemStack getItemInUse() {
		return getItemInUse(avatar.Dw_DominantArm());
	}

	public int getItemInUseCount(int pIndex) {
		return avatar.getSwingStatus(pIndex).getItemInUseCount();
	}

	@Override
	public int getItemInUseCount() {
		return getItemInUseCount(avatar.Dw_DominantArm());
	}

	public boolean isUsingItem(int pIndex) {
		return avatar.getSwingStatus(pIndex).isUsingItem();
	}

	public boolean isUsingItem() {
		return isUsingItem(avatar.Dw_DominantArm());
	}
	public boolean isUsingItemLittleMaid() {
		return isUsingItem() | isItemTrigger;
	}

	public void clearItemInUse(int pIndex) {
		isItemTrigger = false;
		isItemReload = isItemPreReload = false;
		avatar.getSwingStatus(pIndex).clearItemInUse(getEntityServer());
	}

	public void clearItemInUse() {
		isItemTrigger = false;
		isItemReload = isItemPreReload = false;
		clearItemInUse(avatar.Dw_DominantArm());
	}

	public void stopUsingItem(int pIndex) {
		isItemTrigger = false;
		isItemReload = isItemPreReload = false;
		avatar.getSwingStatus(pIndex).stopUsingItem(getEntityServer());
	}

	@Override
	public void stopActiveHand() {
		isItemTrigger = false;
		isItemReload = isItemPreReload = false;
		stopUsingItem(avatar.Dw_DominantArm());
	}

	public void setItemInUse(int pIndex, ItemStack itemstack, int i) {
		isItemTrigger = true;
		isItemReload = isItemPreReload;
		avatar.getSwingStatus(pIndex).setItemInUse(itemstack, i, getEntityServer());
	}

	public void setItemInUse(ItemStack itemstack, int i) {
		isItemTrigger = true;
		isItemReload = isItemPreReload;
		setItemInUse(avatar.Dw_DominantArm(), itemstack, i);
	}

	@Override
	public void setAir(int par1) {
		avatar.setAir(par1);
	}

	@Override
	public int getAir() {
		return avatar.getAir();
	}

	@Override
	public void setFire(int par1) {
		avatar.setFire(par1);
	}

	@Override
	public boolean isBurning() {
		return avatar.isBurning();
	}

	@Override
	protected void setFlag(int par1, boolean par2) {
		avatar.setFlag(par1, par2);
	}

	@Override
	public void addChatMessage(ITextComponent var1) {
		// チャットメッセージは使わない。
	}

	// 不要？

	protected void setHideCape(int par1, boolean par2) {}

	protected boolean getHideCape(int par1) {
		return false;
	}

	/**
	 * 属性値リストを取得
	 */
	public AbstractAttributeMap getAttributeMap() {
		return avatar == null ? super.getAttributeMap() : avatar.getAttributeMap();
	}

	@Override
	public void addPotionEffect(PotionEffect par1PotionEffect) {
		avatar.addPotionEffect(par1PotionEffect);
	}

	@Override
	public PotionEffect getActivePotionEffect(Potion par1Potion) {
		return avatar.getActivePotionEffect(par1Potion);
	}

	@Override
	public Collection getActivePotionEffects() {
		return avatar.getActivePotionEffects();
	}

	@Override
	public void clearActivePotions() {
		avatar.clearActivePotions();
	}

	public void getValue() {
		// EntityLittleMaidから値をコピー
		setPosition(avatar.posX, avatar.posY, avatar.posZ);
		prevPosX = avatar.prevPosX;
		prevPosY = avatar.prevPosY;
		prevPosZ = avatar.prevPosZ;
		rotationPitch = avatar.rotationPitch;
		rotationYaw = avatar.rotationYaw;
		prevRotationPitch = avatar.prevRotationPitch;
		prevRotationYaw = avatar.prevRotationYaw;
		renderYawOffset = avatar.renderYawOffset;
		prevRenderYawOffset = avatar.prevRenderYawOffset;
		rotationYawHead = avatar.rotationYawHead;
	}

	public void getValueVector(double atx, double aty, double atz, double atl) {
		double l = MathHelper.sqrt_double(atl);
		appendX = atx / l;
		appendY = aty / l;
		appendZ = atz / l;
		posX = avatar.posX + appendX;
		posY = avatar.posY + appendY;
		posZ = avatar.posZ + appendZ;
		prevPosX = avatar.prevPosX + appendX;
		prevPosY = avatar.prevPosY + appendY;
		prevPosZ = avatar.prevPosZ + appendZ;
		rotationPitch		= avatar.rotationPitch;
		prevRotationPitch	= avatar.prevRotationPitch;
		rotationYaw			= avatar.rotationYaw;
		prevRotationYaw		= avatar.prevRotationYaw;
		renderYawOffset		= avatar.renderYawOffset;
		prevRenderYawOffset	= avatar.prevRenderYawOffset;
		rotationYawHead		= avatar.rotationYawHead;
		prevRotationYawHead	= avatar.prevRotationYawHead;
		motionX = avatar.motionX;
		motionY = avatar.motionY;
		motionZ = avatar.motionZ;
		isSwingInProgress = avatar.getSwinging();
	}

	/**
	 * 射撃管制用、rotationを頭に合わせる
	 */
	public void getValueVectorFire(double atx, double aty, double atz, double atl) {
		double l = MathHelper.sqrt_double(atl);
		appendX = atx / l;
		appendY = aty / l;
		appendZ = atz / l;
		posX = avatar.posX + appendX;
		posY = avatar.posY + appendY;
		posZ = avatar.posZ + appendZ;
		prevPosX = avatar.prevPosX + appendX;
		prevPosY = avatar.prevPosY + appendY;
		prevPosZ = avatar.prevPosZ + appendZ;
		rotationPitch		= updateDirection(avatar.rotationPitch);
		prevRotationPitch	= updateDirection(avatar.prevRotationPitch);
		rotationYaw			= updateDirection(avatar.rotationYawHead);
		prevRotationYaw		= updateDirection(avatar.prevRotationYawHead);
		renderYawOffset		= updateDirection(avatar.renderYawOffset);
		prevRenderYawOffset	= updateDirection(avatar.prevRenderYawOffset);
		rotationYawHead		= updateDirection(avatar.rotationYawHead);
		prevRotationYawHead	= updateDirection(avatar.prevRotationYawHead);
		motionX = avatar.motionX;
		motionY = avatar.motionY;
		motionZ = avatar.motionZ;
		isSwingInProgress = avatar.getSwinging();
	}

	protected float updateDirection(float pValue) {
		pValue %= 360F;
		if (pValue < 0) pValue += 360F;
		return pValue;
	}

	public void setValue() {
		avatar.setPosition(posX, posY, posZ);
		avatar.prevPosX = prevPosX;
		avatar.prevPosY = prevPosY;
		avatar.prevPosZ = prevPosZ;
		avatar.rotationPitch = rotationPitch;
		avatar.rotationYaw = rotationYaw;
		avatar.prevRotationPitch = prevRotationPitch;
		avatar.prevRotationYaw = prevRotationYaw;
		//avatar.getYOffset();
		avatar.renderYawOffset = renderYawOffset;
		avatar.prevRenderYawOffset = prevRenderYawOffset;
		//avatar.getSwingStatusDominant().attackTime = avatar.attackTime = attackTime;
	}

	public void setValueRotation() {
		// EntityLittleMiadへ値をコピー
		avatar.rotationPitch = rotationPitch;
		avatar.rotationYaw = rotationYaw;
		avatar.prevRotationPitch = prevRotationPitch;
		avatar.prevRotationYaw = prevRotationYaw;
		avatar.renderYawOffset = renderYawOffset;
		avatar.prevRenderYawOffset = prevRenderYawOffset;
		avatar.motionX = motionX;
		avatar.motionY = motionY;
		avatar.motionZ = motionZ;
		//TODO: サウンド
		//if (isSwingInProgress) avatar.setSwinging(EnumSound.Null, false);

	}

	public void setValueVector() {
		avatar.posX = posX - appendX;
		avatar.posY = posY - appendY;
		avatar.posZ = posZ - appendZ;
		avatar.prevPosX = prevPosX - appendX;
		avatar.prevPosY = prevPosY - appendY;
		avatar.prevPosZ = prevPosZ - appendZ;
		avatar.rotationPitch	 = rotationPitch;
		avatar.prevRotationPitch = prevRotationPitch;
		avatar.motionX = motionX;
		avatar.motionY = motionY;
		avatar.motionZ = motionZ;
		// TODO: サウンド
		//if (isSwingInProgress) avatar.setSwinging(EnumSound.Null, false);
	}


	/******************************************************************************************/
	/** インタフェース                                                                                                                                                                                                          **/
	/******************************************************************************************/
	public void W_damageArmor(float par1){
		super.damageArmor(par1);
	}

	public float applyArmorCalculations(DamageSource par1DamageSource, float par2)
	{
		return super.applyArmorCalculations(par1DamageSource, par2);
	}

	public float applyPotionDamageCalculations(DamageSource par1DamageSource, float par2)
	{
		return super.applyPotionDamageCalculations(par1DamageSource, par2);
	}

	public void W_damageEntity(DamageSource par1DamageSource, float par2)
	{
		super.damageEntity(par1DamageSource, par2);
	}

	@Override
	public float W_applyArmorCalculations(DamageSource par1DamageSource, float par2) {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public float W_applyPotionDamageCalculations(DamageSource par1DamageSource, float par2) {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public boolean getIsItemTrigger() {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	public boolean getIsItemReload() {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	public EntityERMBase getEntity() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}
}
