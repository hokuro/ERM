package basashi.erm.container;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;

import basashi.erm.core.ModCommon;
import basashi.erm.core.log.ModLog;
import basashi.erm.entity.EntityERMBase;
import basashi.erm.util.Values;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ReportedException;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class InventoryERMBase implements IInventory {
	// モブ
    protected EntityERMBase entity;
	// メインインベントリ 2 * 9
    public final ItemStack[] mainInventory;
    // 防具インベントリ 4
    public final ItemStack[] armorInventory = new ItemStack[4];
    // 作業用インベントリ 1
    public final ItemStack[] workInventory = new ItemStack[1];
	// 全てのインベントリ
    protected final ItemStack[][] allInventories;

    // インベントリが変更されているか
    public boolean inventoryChanged;
	public Object setInventory;

	// カレントアイテムインデックス
	public int currentMainItem;
	// 作業アイテムインデックス
	public int currentWorkItem;

    public InventoryERMBase(EntityERMBase entity, int mainInvSize){
    	this.entity = entity;
    	mainInventory=new ItemStack[mainInvSize];
    	allInventories = new ItemStack[][]{mainInventory, armorInventory, workInventory};
    }

    /**
     * すべてのインベントリを取り出す
     * @return
     */
    protected ItemStack[][] allInventory(){
    	return this.allInventories;
    }

    /**
     * インベントリの中身をすべてまき散らす
     */
	public void dropAllItems(){
		// インベントリの中身を全部放出する
        for (ItemStack[] aitemstack1 : this.allInventories)
        {
            for (ItemStack stack : aitemstack1){
            	if (stack != null){
            		entity.entityDropItem(stack, 0F);
            	}
            }

        }
        // インベントリクリア
        this.clear();
	}

	/****************************************************************************************************/
	/** メインインベントリ操作                                                                         **/
	/****************************************************************************************************/
	/**
	 * インベントリの最大サイズ
	 * @return
	 */
	public int maxInventorySize(){
		return this.mainInventory.length;
	}

	public int maxEquipmentSize(){
		return this.armorInventory.length;
	}

	public int maxWorkToolSize(){
		return this.workInventory.length;
	}

	/**
	 * 現在所持しているアイテムを取り出す
	 * @return
	 */
	public ItemStack getCurrentItem(){
		if(ModCommon.isDebug){ModLog.log().debug("-D Log Inventory currentMainItem : " + currentMainItem + "/" + mainInventory.length);}
		if(currentMainItem >= 0 && currentMainItem < mainInventory.length){
			return  mainInventory[currentMainItem];
		}
		currentMainItem = 0;
		return mainInventory[currentMainItem];
	}

	/**
	 * 指定したインデックスのアイテムとカレントのアイテムを入れ替える
	 * @param index
	 */
	 public void swapCurrentMainItem(int index){
		if(ModCommon.isDebug){ModLog.log().debug("-D Log Inventory currentMainItem : " + currentMainItem + "<=>" + index);}
		if (mainInventory[currentMainItem] != null && isBrakeItem(mainInventory[currentMainItem])){
			if(ModCommon.isDebug){ModLog.log().debug("-D Log Inventory BrokenItem Change");}
			mainInventory[currentMainItem] = null;
		}
		 ItemStack itemstack = this.mainInventory[this.currentMainItem];
		 this.mainInventory[this.currentMainItem] = this.mainInventory[index];
		 this.mainInventory[index] = itemstack;
	 }


	/**
	 * メインインベントリ内に指定したアイテムがある場合、
	 * 現在手持ちのアイテムと入れ替える
	 * @param item
	 * @return
	 */
	public boolean swapCurrentMainItem(ItemStack item){
		if(ModCommon.isDebug){ModLog.log().debug("-D Log Inventory currentMainItem : " + currentMainItem + "/" + mainInventory.length);}
		if(ModCommon.isDebug){ModLog.log().debug("-D Log Inventory searchItem : " + item!=null?item.getDisplayName():"NULL");}
		if(currentMainItem >= 0 && currentMainItem < mainInventory.length){
			try{
				if (mainInventory[currentMainItem] != null && isBrakeItem(mainInventory[currentMainItem])){
					if(ModCommon.isDebug){ModLog.log().debug("-D Log Inventory BrokenItem Change");}
					mainInventory[currentMainItem] = null;
				}

				// 同じアイテムを持っているか検索
				int index = searchNextItemIndex(item,this.currentMainItem);
				if ( index >= 0){
					// アイテムを入れ替える
					ItemStack swap = mainInventory[currentMainItem];
					mainInventory[currentMainItem] = mainInventory[index];
					mainInventory[index] = swap;
					return true;
				}
			}catch(Exception ex){

			}
		}
		return false;
	}

	 /**
	  * アイテムのスロット番号を検索
	  * @param stack
	  * @return
	  */
	 @SideOnly(Side.CLIENT)
	public int getSlotFor(ItemStack itemstack){
		return searchNextItemIndex(itemstack,-1);
	}

	/**
	 * カレントスロットを変更する
	 * @param idx
	 * @return
	 */
	public boolean setCurrentSlot(int idx){
		if(ModCommon.isDebug){ModLog.log().debug("-D Log Inventory currentMainItem : " + idx + "/" + mainInventory.length);}
		if (idx >= 0 && idx < mainInventory.length){
			this.currentMainItem = idx;
			return true;
		}
		return false;
	}


	 /**
	  * 現在手持ちのアイテムを設定する
	  * @param object
	  */
		public void CurrentSlotContents(Object object) {
			if(ModCommon.isDebug){ModLog.log().debug("-D Log Inventory currentIndex : " + currentMainItem + "/" + mainInventory.length);}
			if(ModCommon.isDebug){ModLog.log().debug("-D Log Inventory currentItem  : " + mainInventory[currentMainItem]!=null?mainInventory[currentMainItem].getDisplayName():"NULL");}
			if(ModCommon.isDebug){ModLog.log().debug("-D Log Inventory nextItem     : " + object!=null?((ItemStack)object).getDisplayName():"NULL");}
			this.mainInventory[this.currentMainItem] = (ItemStack)object;
		}

		/**
		 * 手持ちのアイテムを作業用アイテムに持ち替える
		 * @param workItem
		 */
		public void setNextWorkItem(ItemStack[] workItem) {
			for(ItemStack item: workItem){
				if (this.stackEqualExact(mainInventory[currentMainItem], item)){
					// すでに作業用アイテムを手持ちにしている
					return;
				}
			}

			for(ItemStack item: workItem){
				if (this.swapCurrentMainItem(item)){
					// 一番最初に見つかったアイテムに持ち替えた
					return;
				}
			}
		}

	/****************************************************************************************************/
	/** 作業インベントリ操作                                                                           **/
	/****************************************************************************************************/
	/**
	 * 作業用のアイテムを取り出す
	 * @return
	 */
	public ItemStack getWorkdTool(){
		return this.workInventory[currentWorkItem];
	}

	/**
	 * 作業用ツールを持っているか確認
	 * @param item
	 * @return
	 */
	public boolean hasWorkTool(ItemStack item){
		return this.stackEqualExact(item, this.workInventory[currentWorkItem]);
	}

	/**
	 * 作業用のアイテムを設定する
	 * @param item
	 */
	public void setWorkTool(ItemStack item){
		if(ModCommon.isDebug){ModLog.log().debug("-D Log Inventory workItem     : " + workInventory[currentWorkItem]!=null?workInventory[currentWorkItem].getDisplayName():"NULL");}
		if(ModCommon.isDebug){ModLog.log().debug("-D Log Inventory nextItem     : " + item!=null?item.getDisplayName():"NULL");}
		this.workInventory[currentWorkItem] = item;
	}

	/**
	 * 作業用アイテムを持ち替える
	 */
	public void setNextWorkTool(ItemStack item){
		if (workInventory[currentWorkItem] != null && isBrakeItem(workInventory[currentWorkItem])){
			if(ModCommon.isDebug){ModLog.log().debug("-D Log Inventory BrokenTool Change");}
			workInventory[currentWorkItem] = null;
		}

		int idx = searchNextItemIndex(item,-1);
		if (idx >= 0){
			this.workInventory[currentWorkItem] = this.mainInventory[idx];
			this.mainInventory[idx]=null;
		}
	}

	/**
	 * 指定したアイテムが作業用インベントリに設定されているか確認する
	 * @param itemStack
	 * @return
	 */
	public boolean hasWorkItem(ItemStack itemStack){
		return stackEqualExact(itemStack, this.workInventory[this.currentWorkItem]);
	}


	/****************************************************************************************************/
	/** インベントリ検索                                                                               **/
	/****************************************************************************************************/
	/**
	 * 現在所持しているアイテムと同じアイテムがある場合、そちらに持ち替える
	 */
	private int searchNextItemIndex(ItemStack item, int sameIndex){
		int idx = -1;
		for (int i = 0; i < mainInventory.length; i++ ){
			if (i != sameIndex &&                                // カレントアイテムの比較は飛ばす
					stackEqualExact(item,mainInventory[currentMainItem])){  // 探しているアイテムと一致するか
				idx = i;
				break;
			}
		}
		if(ModCommon.isDebug){ModLog.log().debug("-D Log Inventory search same item. Index : " + idx);}
		return idx;
	}

	/**
	 * ダメージを受けて壊れているか、スタックサイズが0であるか
	 * @param stack
	 * @return
	 */
	private boolean isBrakeItem(ItemStack stack){
		if (stack.stackSize <= 0 || (stack.isItemDamaged() && stack.getItemDamage() <= 0)){
			return true;
		}
		return false;
	}

	/**
	 * 指定したエフェクトを持つアイテムを検索する
	 * @param effect
	 * @return
	 */
	public List<Integer> SerchItemHaveEffect(Potion effect){
		if(ModCommon.isDebug){ModLog.log().debug("-D Log Inventory search effect : " + effect.getName());}
		List<Integer> idxList= new ArrayList<Integer>();
		for (int i = 0; i < this.mainInventory.length; i++){
			if (this.mainInventory[i] == null){continue;}
			try{
				// ポーションの効果リストを取り出す
				List<PotionEffect> effects = PotionUtils.getEffectsFromStack(this.mainInventory[i]);
				for (Iterator<PotionEffect> it = effects.iterator(); it.hasNext();){
					if (it.next().getPotion() == effect){
						// 一致するエフェクトがあれば追加
						idxList.add(i);
						if(ModCommon.isDebug){ModLog.log().debug("-D Log Inventory effect item : " + this.mainInventory[i].getDisplayName());}
					}
				}
			}catch(Exception ex){
				continue;
			}
		}
		return idxList;
	}

	/**
	 * 指定した効果を持つポーションを検索する
	 * @param effect
	 * @param kind
	 * @return
	 */
	public int SearchPotion(Potion effect, Values.KIND_SEARCHIPOTION kind){
		if(ModCommon.isDebug){ModLog.log().debug("-D Log Inventory search effect : " + effect.getName());}
		if(ModCommon.isDebug){ModLog.log().debug("-D Log Inventory search kind : " + kind.toString());}
		int idx = -1;
		for (int i = 0; i < this.mainInventory.length && idx == -1; i++){
			if (this.mainInventory[i] == null){continue;}
			Item itm = this.mainInventory[i].getItem();
			// ポーションの種類を確認
			if (	((kind==Values.KIND_SEARCHIPOTION.ALL || kind==Values.KIND_SEARCHIPOTION.NORMAL) && itm == Items.potionitem) ||
					((kind==Values.KIND_SEARCHIPOTION.ALL || kind==Values.KIND_SEARCHIPOTION.SPLASH) && itm == Items.splash_potion) ||
					((kind==Values.KIND_SEARCHIPOTION.ALL || kind==Values.KIND_SEARCHIPOTION.LINGERING) && itm == Items.lingering_potion)){
				// ポーションの効果リストを取り出す
				List<PotionEffect> effects = PotionUtils.getEffectsFromStack(this.mainInventory[i]);
				for (Iterator<PotionEffect> it = effects.iterator(); it.hasNext();){
					if (it.next().getPotion() == effect){
						// 一致するエフェクトがあればブレーク
						idx = i;
						break;
					}
				}
			}
		}
		return idx;
	}

	/**
	 * 何も設定されていないスロットを検索する
	 * @return
	 */
	public  int getFirstEmptyStack(){
		int idx = -1;
		for(int i = 0; i < this.mainInventory.length; i++){
			if (mainInventory[i] != null){
				idx = i;
				break;
			}
		}
		return idx;
	}

	/**
	 * アニメーション
	 */
	public void decrementAnimations(){
		for (int li = 0; li < mainInventory.length; ++li) {
			if (this.mainInventory[li] != null) {
				try {
					this.mainInventory[li].updateAnimation(this.entity.worldObj,
							entity, li, this.currentMainItem == li);
				} catch (ClassCastException e) {
					this.mainInventory[li].updateAnimation(this.entity.worldObj,
							entity.mobAvatar, li, this.currentMainItem == li);
				}
			}
		}
	}

	/****************************************************************************************************/
	/** セーブ&ロード                                                                                  **/
	/****************************************************************************************************/
	/**
	 * NBT書き込み
	 * @param tagCompound
	 * @return
	 */
	public NBTTagList writeToNBT(NBTTagList tagListIn){
        for (int i = 0; i < this.mainInventory.length; ++i)
        {
            if (this.mainInventory[i] != null)
            {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte)i);
                this.mainInventory[i].writeToNBT(nbttagcompound);
                tagListIn.appendTag(nbttagcompound);
            }
        }

        for (int j = 0; j < this.armorInventory.length; ++j)
        {
            if (this.armorInventory[j] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)(j + 100));
                this.armorInventory[j].writeToNBT(nbttagcompound1);
                tagListIn.appendTag(nbttagcompound1);
            }
        }

        for (int k = 0; k < this.workInventory.length; ++k)
        {
            if (this.workInventory[k] != null)
            {
                NBTTagCompound nbttagcompound2 = new NBTTagCompound();
                nbttagcompound2.setByte("Slot", (byte)(k + 150));
                this.workInventory[k].writeToNBT(nbttagcompound2);
                tagListIn.appendTag(nbttagcompound2);
            }
        }

        return tagListIn;
	}

	/**
	 * NBT読み込み
	 * @param tagCompound
	 * @return
	 */
	public void readFromNBT(NBTTagList tagList){
        Arrays.fill(this.mainInventory, (Object)null);
        Arrays.fill(this.armorInventory, (Object)null);
        Arrays.fill(this.workInventory, (Object)null);

        for (int i = 0; i < tagList.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
            int j = nbttagcompound.getByte("Slot") & 255;
            ItemStack itemstack = ItemStack.loadItemStackFromNBT(nbttagcompound);

            if (itemstack != null)
            {
                if (j >= 0 && j < this.mainInventory.length)
                {
                    this.mainInventory[j] = itemstack;
                }
                else if (j >= 100 && j < this.armorInventory.length + 100)
                {
                    this.armorInventory[j - 100] = itemstack;
                }
                else if (j >= 150 && j < this.workInventory.length + 150)
                {
                    this.workInventory[j - 150] = itemstack;
                }
            }
        }
	}

	/****************************************************************************************************/
	/** IIvnentory                                                                                     **/
	/***************************************************************************************************/
	/**
	 * 指定スロットのアイテムを取り出す
	 * @param index
	 * @return
	 */
	@Override
	public ItemStack getStackInSlot(int index) {
        ItemStack[] aitemstack = null;
        if(ModCommon.isDebug){ModLog.log().debug("-D Log Inventory get item slot index : " + index);}
        for (ItemStack[] aitemstack1 : allInventory())
        {
            if (index < aitemstack1.length)
            {
                aitemstack = aitemstack1;
                break;
            }

            index -= aitemstack1.length;
        }

        return aitemstack == null ? null : aitemstack[index];
	}

	/**
	 * 指定スロットのスタック数を減らす
	 * @param index
	 * @param count
	 * @return
	 */

	@Override
	public ItemStack decrStackSize(int index, int count) {
        ItemStack[] aitemstack = null;
        if(ModCommon.isDebug){ModLog.log().debug("-D Log Inventory stack change  : " + index + " count : " + count);}
        for (ItemStack[] aitemstack1 : allInventory())
        {
            if (index < aitemstack1.length)
            {
                aitemstack = aitemstack1;
                break;
            }

            index -= aitemstack1.length;
        }

        return aitemstack != null && aitemstack[index] != null ? ItemStackHelper.func_188382_a(aitemstack, index, count) : null;

	}

	/**
	 * 指定スロットのアイテムを削除する
	 * @param index
	 * @return
	 */
	@Override
	public ItemStack removeStackFromSlot(int index) {
        ItemStack[] aitemstack = null;
        if(ModCommon.isDebug){ModLog.log().debug("-D Log Inventory stack change  : " + index);}

        for (ItemStack[] aitemstack1 : allInventory())
        {
            if (index < aitemstack1.length)
            {
                aitemstack = aitemstack1;
                break;
            }

            index -= aitemstack1.length;
        }

        if (aitemstack != null && aitemstack[index] != null)
        {
            ItemStack itemstack = aitemstack[index];
            aitemstack[index] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
	}

	/**
	 * 指定したスロットにアイテムを設定する
	 * @param index
	 * @param stack
	 */
	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
        ItemStack[] aitemstack = null;
        if(ModCommon.isDebug){ModLog.log().debug("-D Log Inventory stack change  : " + index);}
        if(ModCommon.isDebug){ModLog.log().debug("-D Log Inventory stack Item    : " + stack.getDisplayName());}

        for (ItemStack[] aitemstack1 : allInventory())
        {
            if (index < aitemstack1.length)
            {
                aitemstack = aitemstack1;
                break;
            }

            index -= aitemstack1.length;
        }

        if (aitemstack != null)
        {
            aitemstack[index] = stack;
        }
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return this.entity.isDead ? false : entity.getDistanceSqToEntity(this.entity) <= 64.0D;
	}

	/**
	 * インベントリをクリアする
	 */
	@Override
	public void clear() {
        for (ItemStack[] aitemstack : allInventory())
        {
            for (int i = 0; i < aitemstack.length; ++i)
            {
                aitemstack[i] = null;
            }
        }
	}

	/**
	 * インベントリ名
	 * @return
	 */
	@Override
	public String getName() {
		return "Inventory " + entity.EntityName();
	}

	/**
	 * カスタム名を持っているか
	 * @return
	 */
	@Override
	public boolean hasCustomName() {
		return true;
	}

	/**
	 * 表示名を取り出す
	 * @return
	 */
	@Override
	public ITextComponent getDisplayName() {
		return new TextComponentString(getName());
	}

	/**
	 * 最大スタック数を取り出す
	 * @return
	 */
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	/**
	 * インベントリが変更されているか
	 */
	@Override
	public void markDirty() {
		this.inventoryChanged = true;
	}

	/**
	 * スロットヴァリデータ
	 * @param index
	 * @param stack
	 * @return
	 */
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		if (stack != null && index >= mainInventory.length && index < mainInventory.length + armorInventory.length) {
			int armorSlotIndex = index - mainInventory.length;
			for (EntityEquipmentSlot slot: EntityEquipmentSlot.values()) {
				if (slot.getSlotType()==EntityEquipmentSlot.Type.ARMOR && slot.getIndex() == armorSlotIndex) {
					if (stack.getItem().isValidArmor(stack, slot, entity)) {
						return true;
					}
				}
			}
		} else if (index >= 0 && index < getSizeInventory()) {
			return true;
		}
		return false;
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {

	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public int getSizeInventory() {
		return this.mainInventory.length + this.armorInventory.length + this.workInventory.length;
	}

	@Override
	public void openInventory(EntityPlayer player) {
		entity.onGuiOpened();
	}

	@Override
	public void closeInventory(EntityPlayer player) {
		entity.onGuiClosed();
	}


	/****************************************************************************************************/
	/** InventoryPlayerからコピペ                                                                      **/
	/***************************************************************************************************/

	/**
	 * 同じアイテムか確認
	 * @param stack1
	 * @param stack2
	 * @return
	 */
    private boolean stackEqualExact(ItemStack stack1, ItemStack stack2)
    {
    	if (stack1 != null && stack2 != null){return false;}
        return stack1.getItem() == stack2.getItem() && (!stack1.getHasSubtypes() || stack1.getMetadata() == stack2.getMetadata()) && ItemStack.areItemStackTagsEqual(stack1, stack2);
    }

	/**
	 * 一致するアイテムをインベントリから削除
	 * @param itemIn
	 * @param metadataIn
	 * @param removeCount
	 * @param itemNBT
	 * @return
	 */
	public int clearMatchingItems(Item itemIn, int metadataIn, int removeCount, NBTTagCompound itemNBT)
	{
		int i = 0;

		for (int j = 0; j < this.getSizeInventory(); ++j)
		{
			ItemStack itemstack = this.getStackInSlot(j);

			if (itemstack != null && (itemIn == null || itemstack.getItem() == itemIn) && (metadataIn <= -1 || itemstack.getMetadata() == metadataIn) && (itemNBT == null || NBTUtil.areNBTEquals(itemNBT, itemstack.getTagCompound(), true)))
			{
				int k = removeCount <= 0 ? itemstack.stackSize : Math.min(removeCount - i, itemstack.stackSize);
				i += k;

				if (removeCount != 0)
				{
					itemstack.stackSize -= k;

					if (itemstack.stackSize == 0)
					{
						this.setInventorySlotContents(j, (ItemStack)null);
					}

					if (removeCount > 0 && i >= removeCount)
					{
						return i;
					}
				}
			}
		}
		return i;
	}


	private int storePartialItemStack(ItemStack itemStackIn)
	{
		Item item = itemStackIn.getItem();
		int i = itemStackIn.stackSize;
		int j = this.storeItemStack(itemStackIn);

		if (j == -1)
		{
			j = this.getFirstEmptyStack();
		}

		if (j == -1)
		{
			return i;
		}
		else
		{
			ItemStack itemstack = this.getStackInSlot(j);

			if (itemstack == null)
			{
				itemstack = itemStackIn.copy();
				itemstack.stackSize = 0;
				this.setInventorySlotContents(j, itemstack);
			}

			int k = i;

			if (i > itemstack.getMaxStackSize() - itemstack.stackSize)
			{
				k = itemstack.getMaxStackSize() - itemstack.stackSize;
			}

			if (k > this.getInventoryStackLimit() - itemstack.stackSize)
			{
				k = this.getInventoryStackLimit() - itemstack.stackSize;
			}

			if (k == 0)
			{
				return i;
			}
			else
			{
				i = i - k;
				itemstack.stackSize += k;
				itemstack.animationsToGo = 5;
				return i;
			}
		}
	}

	private int storeItemStack(ItemStack itemStackIn)
	{
		if (stackEqualExact(this.mainInventory[this.currentMainItem], itemStackIn))
		{
			return this.currentMainItem;
		}
		else
		{
			for (int i = 0; i < this.mainInventory.length; ++i)
			{
				if (stackEqualExact(this.mainInventory[i], itemStackIn))
				{
					return i;
				}
			}

			return -1;
		}
	}

	/**
	 * インベントリにアイテムを追加する
	 * @param itemStackIn
	 * @return
	 */
	public boolean addItemStackToInventory(final ItemStack itemStackIn)
	{
		if (itemStackIn != null && itemStackIn.stackSize != 0 && itemStackIn.getItem() != null)
		{
			try
			{
				if (itemStackIn.isItemDamaged())
				{
					int j = this.getFirstEmptyStack();

					if (j >= 0)
					{
						this.mainInventory[j] = ItemStack.copyItemStack(itemStackIn);
						this.mainInventory[j].animationsToGo = 5;
						itemStackIn.stackSize = 0;
						return true;
					}
					else
					{
						return false;
					}
				}
				else
				{
					int i;

					while (true)
					{
						i = itemStackIn.stackSize;
						itemStackIn.stackSize = this.storePartialItemStack(itemStackIn);

						if (itemStackIn.stackSize <= 0 || itemStackIn.stackSize >= i)
						{
							break;
						}
					}
					return itemStackIn.stackSize < i;

				}
			}
			catch (Throwable throwable)
			{
				CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Adding item to inventory");
				CrashReportCategory crashreportcategory = crashreport.makeCategory("Item being added");
				crashreportcategory.addCrashSection("Item ID", Integer.valueOf(Item.getIdFromItem(itemStackIn.getItem())));
				crashreportcategory.addCrashSection("Item data", Integer.valueOf(itemStackIn.getMetadata()));
				crashreportcategory.addCrashSectionCallable("Item name", new Callable<String>()
				{
					public String call() throws Exception
					{
						return itemStackIn.getDisplayName();
					}
				});
				throw new ReportedException(crashreport);
			}
		}
		else
		{
			return false;
		}
	}

	/**
	 * アイテムを削除する
	 * @param p_184437_1_
	 */
	public void deleteStack(ItemStack p_184437_1_)
	{
		for (ItemStack[] aitemstack : this.allInventories)
		{
			for (int i = 0; i < aitemstack.length; ++i)
			{
				if (aitemstack[i] == p_184437_1_)
				{
					aitemstack[i] = null;
					break;
				}
			}
		}
	}

	/**
	 * アーマースロットの内容を取り出す
	 * @param slotIn
	 * @return
	 */
    @SideOnly(Side.CLIENT)
	public ItemStack armorItemInSlot(int slotIn)
	{
		return this.armorInventory[slotIn];
	}

    /**
     * 防具のダメージ計算
     * @param damage
     */
	public void damageArmor(float damage)
	{
		damage = damage / 4.0F;

		if (damage < 1.0F)
		{
			damage = 1.0F;
		}

		for (int i = 0; i < this.armorInventory.length; ++i)
		{
			if (this.armorInventory[i] != null && this.armorInventory[i].getItem() instanceof ItemArmor)
			{
				this.armorInventory[i].damageItem((int)damage, this.entity.mobAvatar);

				if (this.armorInventory[i].stackSize == 0)
				{
					this.armorInventory[i] = null;
				}
			}
		}
	}

	/**
	 * インベントリをコピーする
	 * @param playerInventory
	 */
	public void copyInventory(InventoryPlayer playerInventory)
	{
		for (int i = 0; i < this.getSizeInventory(); ++i)
		{
			this.setInventorySlotContents(i, playerInventory.getStackInSlot(i));
		}

		this.currentMainItem = playerInventory.currentItem;
	}


}
