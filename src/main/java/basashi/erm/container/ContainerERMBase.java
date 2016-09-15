package basashi.erm.container;

import basashi.erm.entity.EntityERMBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerERMBase extends Container {

	protected final InventoryERMBase littleGirlInventory;
	protected final int numRows;
	protected final EntityERMBase owner;

	public ContainerERMBase(IInventory playerInventory, EntityERMBase entity){
		inventorySlots.clear();
		inventoryItemStacks.clear();

		InventoryERMBase inventory = entity.getInventory();
		numRows = inventory.maxInventorySize() / 9;
		owner = entity;
		littleGirlInventory = inventory;
		littleGirlInventory.openInventory(owner.mobAvatar);

		// モブメインインベントリ
		for ( int ly = 0; ly < numRows; ly++){
			for (int lx = 0; lx < 9; lx++){
				addSlotToContainer(new Slot(littleGirlInventory, lx+ly*9, 8+lx * 18, 76+ly * 18));
			}
		}


		// モブ装備インベントリ
		for (int j = 0; j < 4; j++) {
			final int armorIndex = j; // ヘルメットはないと思っていたのか！
			this.addSlotToContainer(new Slot(littleGirlInventory, littleGirlInventory.maxInventorySize()+armorIndex, 8 + 72*((3-j)/2), 8 + ((3-j)%2)*36) {
				/**
				 * Returns the maximum stack size for a given slot (usually the same as getInventoryStackLimit(), but 1
				 * in the case of armor slots)
				 */
				public int getSlotStackLimit()
				{
					return 1;
				}
				/**
				 * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
				 */
				public boolean isItemValid(ItemStack par1ItemStack)
				{
					if (par1ItemStack == null) return false;
					boolean flag = littleGirlInventory.isItemValidForSlot(littleGirlInventory.maxInventorySize()+armorIndex, par1ItemStack);
					return flag;
				}
				/**
				 * Returns the icon index on items.png that is used as background image of the slot.
				 */
				@SideOnly(Side.CLIENT)
				public String getSlotTexture()
				{
					return ItemArmor.EMPTY_SLOT_NAMES[armorIndex];
				}
			});
		}

		// モブ作業用インベントリ
		addSlotToContainer(new Slot(littleGirlInventory,
				littleGirlInventory.maxInventorySize()+ littleGirlInventory.maxEquipmentSize(), 117, 44));

		// プレイヤーのインベントリ
		int lyoffset = (numRows - 4) * 18 + 59;
		for (int ly = 0; ly < 3; ly++) {
			for (int lx = 0; lx < 9; lx++) {
				addSlotToContainer(new Slot(playerInventory, lx + ly * 9 + 9, 8 + lx * 18, 103 + ly * 18 + lyoffset));
			}
		}

		// プレイヤーの手持ちアイテム
		for (int lx = 0; lx < 9; lx++) {
			addSlotToContainer(new Slot(playerInventory, lx, 8 + lx * 18, 161 + lyoffset));
		}
	}


	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		// 開けるかどうかの判定
		EntityERMBase entitylittlemaid = littleGirlInventory.entity;
		if(entitylittlemaid.isDead) {
			return false;
		}
		return playerIn.getDistanceSqToEntity(entitylittlemaid) <= 64D;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int pIndex) {
		ItemStack litemstack = null;
		Slot slot = (Slot)inventorySlots.get(pIndex);
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();

			litemstack = itemstack1.copy();
			int lline = numRows * 9;
			if (pIndex < lline) {
				if (!this.mergeItemStack(itemstack1, lline, lline + 36, true)) {
					return null;
				}
			} else if (pIndex >= lline && pIndex < lline + 36) {
				if (!this.mergeItemStack(itemstack1, 0, lline, false)) {
					return null;
				}
			} else {
				if (!this.mergeItemStack(itemstack1, 0, lline + 36, false)) {
					return null;
				}
			}
			if (itemstack1.stackSize == 0) {
				slot.putStack(null);
			} else {
				slot.onSlotChanged();
			}
		}
		return litemstack;
	}

	@Override
	public void onContainerClosed(EntityPlayer par1EntityPlayer) {
		super.onContainerClosed(par1EntityPlayer);
		littleGirlInventory.closeInventory(owner.mobAvatar);
	}

	@Override
	public boolean canMergeSlot(ItemStack par1ItemStack, Slot par2Slot) {
		return true;
	}



}
