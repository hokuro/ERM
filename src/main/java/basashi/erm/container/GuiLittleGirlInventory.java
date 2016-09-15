package basashi.erm.container;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import basashi.erm.core.ModCommon;
import basashi.erm.entity.EntityERMBase;
import basashi.erm.util.Values.KIND_STATUS;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.translation.I18n;

public class GuiLittleGirlInventory extends GuiContainer {

	protected static final ResourceLocation guiTex =
			new ResourceLocation(ModCommon.MOD_ID, "textures/gui/container/littlegirlinventory.png");

	private Random rand;
	private IInventory playerInventory;
	private IInventory mobInventory;
	private int ySizebk;
	private int updateCounter;
	public EntityERMBase entity;
	public boolean isChangeTexture;
	private int topTicks = 0;

	// ボタン
	public GuiButton SelectAI;
	public GuiButton SelectVoice;
	public GuiButton SelectTexture;
	public GuiButton TalkEntity;

	public GuiLittleGirlInventory(EntityPlayer player, EntityERMBase entity) {
		super(new ContainerERMBase(player.inventory,entity));

		rand = new Random();
		playerInventory = player.inventory;
		mobInventory = entity.getInventory();
		allowUserInput = false;
		updateCounter = 0;
		ySizebk = ySize;
		ySize = 207;
		isChangeTexture = true;
		this.entity = entity;

		topTicks = entity.ticksExisted;
	}

	@Override
	public void initGui(){
		super.initGui();
		if (!entity.getActivePotionEffects().isEmpty()){
			guiLeft = 160 +(width-xSize -200)/2;
		}
		// TODO 自動生成されたメソッド・スタブ
		SelectAI = new GuiButton(100,136,44,16,7,"AI");
		TalkEntity = new GuiButton(101,155,44,16,7,"Talk");
		SelectVoice = new GuiButton(102,136,54,16,7,"Vo");
		SelectTexture = new GuiButton(103,155,54,16,7,"Tex");
		buttonList.add(SelectAI);
		buttonList.add(TalkEntity);
		buttonList.add(SelectVoice);
		buttonList.add(SelectTexture);
	}


	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		String mInventoryString = I18n.translateToLocal(mobInventory.getName());
		mc.fontRendererObj.drawString(mInventoryString, 168 - mc.fontRendererObj.getStringWidth(mInventoryString), 64, 0x404040);
		mc.fontRendererObj.drawString(I18n.translateToLocal(
				playerInventory.getName()), 8, 114, 0x404040);

//		if (entity.getMaidModeInt() == EntityMode_Basic.mmode_Escorter && !entitylittlemaid.isTracer()) {
//			RenderInfoPart.setEnabled(2, false);
//		} else {
//			RenderInfoPart.setEnabled(2, true);
//		}
		if (RenderInfoPart.getRenderingPart() == 2) {
			mc.fontRendererObj.drawString(I18n.translateToLocal(entity.settingAI().AIName()), 7, 64, 0x404040);
		}

		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		// 背景
		ResourceLocation lrl = guiTex;

		mc.getTextureManager().bindTexture(lrl);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int lj = guiLeft;
		int lk = guiTop;
		drawTexturedModalRect(lj, lk, 0, 0, xSize, ySize);

		// PotionEffect
		displayDebuffEffects();

		// LP/AP
		drawHeathArmor(0, 0);

		// Mob
		net.minecraft.client.gui.inventory.GuiInventory.drawEntityOnScreen(
				lj + 51, lk + 57, 30,
				(float)(lj + 51) - i, (float)(lk + 57 - 50) - j, entity);


//		// EXPゲージ
//		GlStateManager.colorMask(true, true, true, false);
//		GlStateManager.disableLighting();
//		GlStateManager.disableDepth();
//		int level = entitylittlemaid.getMaidLevel();
//		if (level >= ExperienceUtil.EXP_FUNCTION_MAX) {
//			level--;
//		}
//		float currentxp = entitylittlemaid.getMaidExperience() - ExperienceUtil.getRequiredExpToLevel(level);
//		float nextxp = ExperienceUtil.getRequiredExpToLevel(level+1) - ExperienceUtil.getRequiredExpToLevel(level);
//		drawGradientRect(guiLeft+97, guiTop+7, guiLeft+168, guiTop+18, 0x80202020, 0x80202020);
//		drawGradientRect(guiLeft+98, guiTop+8, guiLeft+98+(int)(69*currentxp/nextxp), guiTop+17, 0xf0008000, 0xf000f000);
//
//		//経験値ブースト
//		drawGradientRect(guiLeft+112, guiTop-16, guiLeft+xSize-16, guiTop, 0x80202020, 0x80202020);
//		drawCenteredString(fontRendererObj, String.format("x%d", booster), guiLeft+112+(xSize-128)/2, guiTop-12, 0xffffff);
//
//		// LV数値
////		GlStateManager.pushMatrix();
//		String lvString = String.format("Lv. %d", entitylittlemaid.getMaidLevel());
//		mc.fontRendererObj.drawString(lvString, guiLeft + 108, guiTop + 13 - mc.fontRendererObj.FONT_HEIGHT/2, 0xff303030);
////		mc.fontRendererObj.drawString(lvString, guiLeft + 109, guiTop + 13 - mc.fontRendererObj.FONT_HEIGHT/2, 0xfff0f0f0);
////		GlStateManager.popMatrix();
//
//		// HP stack
//		// テキスト表示
//		if (RenderInfoPart.getRenderingPart() == 0) {
//			float lhealth = entitylittlemaid.getHealth();
//			if (lhealth > 20) {
//				mc.fontRendererObj.drawString(String.format("x%d", MathHelper.floor_float((lhealth-1) / 20)), guiLeft + 95, guiTop + 64, 0x404040);
//			}
//		}
//
//		GlStateManager.enableLighting();
//		GlStateManager.enableDepth();
//		GlStateManager.colorMask(true, true, true, true);

	}



	@Override
	public void drawScreen(int i, int j, float f) {
		if ((entity.ticksExisted - topTicks) % 30 == 0) {
			if (!RenderInfoPart.islocked())RenderInfoPart.shiftPart();
			RenderInfoPart.lock();
		} else {
			RenderInfoPart.unlock();
		}
		super.drawScreen(i, j, f);
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		updateCounter++;
	}

	@Override
	protected void mouseClicked(int i, int j, int k) {
		try {
			super.mouseClicked(i, j, k);
		} catch (IOException e) {
		}
	}

	@Override
	protected void actionPerformed(GuiButton par1GuiButton) {
		int booster = entitylittlemaid.getExpBooster();
		switch (par1GuiButton.id) {
		case 100:
			// SelectAI
		break;
		case 101:
			// Talk
			break;
		case 102:
			// SelectVoice
			break;
		case 103:
			// SelectTexture
			break;
		}
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		 entity.onGuiClosed();
		 // TODO: テクスチャ
//		if (isChangeTexture) {
//			entity.sendTextureToServer();
//		}
	}



	protected void drawHeathArmor(int par1, int par2) {
		int ldrawx;
		int ldrawy;

		this.rand.setSeed(updateCounter * 312871);

		Minecraft.getMinecraft().getTextureManager().bindTexture(icons);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		// HP
		// データ取り出し
		int orgnHealth = MathHelper.ceiling_float_int(entity.getHealth());
		int orgnLasthealth = orgnHealth + MathHelper.ceiling_float_int(entity.getLastDamage());
		float origAbsorption = entity.getAbsorptionAmount();
		float absorption = origAbsorption;
		float maxHealth = (float) entity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getAttributeValue();
		int maxHealthRows = MathHelper.ceiling_float_int((maxHealth + origAbsorption) / 20.0F);
		int healthRows = MathHelper.ceiling_float_int(orgnHealth / 20f);
		int isregene = -1;
		if (entity.isPotionActive(Potion.getPotionById(10))) {
			isregene = updateCounter % MathHelper.ceiling_float_int(maxHealth + 5.0F);
		}
		boolean isResist = entity.hurtResistantTime / 3 % 2 == 1;
		if (entity.hurtResistantTime < 10) {
			isResist = false;
		}

		// 描画
		for (int li = maxHealthRows > healthRows ? 9 : MathHelper.ceiling_float_int((maxHealth + origAbsorption - 2)/2f) % 10; li >= 0; --li) {
			int icon = 16;
			if (entity.isPotionActive(Potion.getPotionById(19))) {
				icon += 36;
			} else if (entity.isPotionActive(Potion.getPotionById(20))) {
				icon += 72;
			}

			ldrawx = guiLeft + li % 10 * 8 + 100;
			ldrawy = guiTop + 8;

			if (orgnHealth <= 4) {
				ldrawy += this.rand.nextInt(2);
			}
			if (li == isregene) {
				ldrawy -= 2;
			}

			this.drawTexturedModalRect(ldrawx, ldrawy, isResist ? 25 : 16, 0, 9, 9);

			int lhealth = orgnHealth % 20;
			if (lhealth == 0 && orgnHealth > 0) lhealth = 20;
			int llasthealth = orgnLasthealth % 20;
			if (llasthealth == 0 && orgnLasthealth > 0) llasthealth = 20;

			if (isResist) {
				if (li * 2 + 1 < llasthealth) {
					this.drawTexturedModalRect(ldrawx, ldrawy, icon + 54, 0, 9, 9);
				}
				if (li * 2 + 1 == llasthealth) {
					this.drawTexturedModalRect(ldrawx, ldrawy, icon + 63, 0, 9, 9);
				}
			}

			if (absorption > 0.0F) {
				if (absorption == origAbsorption && origAbsorption % 2.0F == 1.0F) {
					this.drawTexturedModalRect(ldrawx, ldrawy, icon + 153, 0, 9, 9);
				} else {
					this.drawTexturedModalRect(ldrawx, ldrawy, icon + 144, 0, 9, 9);
				}

				absorption -= 2.0F;
			} else {
				if (li * 2 + 1 < lhealth) {
					this.drawTexturedModalRect(ldrawx, ldrawy, icon + 36, 0, 9, 9);
				}
				if (li * 2 + 1 == lhealth) {
					this.drawTexturedModalRect(ldrawx, ldrawy, icon + 45, 0, 9, 9);
				}
			}
		}

		// FOOD
		// データ取り出し
		int orgnfood = MathHelper.ceiling_float_int(entity.getStatus(KIND_STATUS.STATUS_FOOD).getStatus());
		int foodRows = MathHelper.ceiling_float_int(orgnfood / 20f);
		int maxFood = MathHelper.ceiling_float_int(entity.getStatus(KIND_STATUS.STATUS_FOOD).getMax());
		int maxFoodRows = MathHelper.ceiling_float_int(maxHealth / 20.0F);
		// 描画
		for (int li = maxFoodRows > foodRows ? 9 : MathHelper.ceiling_float_int((maxFood - 2)/2f) % 10; li >= 0; --li) {
			int icon = 16;
			if (entity.isPotionActive(Potion.getPotionById(17))) {
				icon += 36;
			}

			ldrawx = guiLeft + li % 10 * 8 + 100;
			ldrawy = guiTop + 20;

			if (orgnHealth <= 4) {
				ldrawy += this.rand.nextInt(2);
			}
			if (li == isregene) {
				ldrawy -= 2;
			}

			this.drawTexturedModalRect(ldrawx, ldrawy, isResist ? 25 : 16, 0, 9, 9);

			int lhealth = orgnHealth % 20;
			if (lhealth == 0 && orgnHealth > 0) lhealth = 20;
			int llasthealth = orgnLasthealth % 20;
			if (llasthealth == 0 && orgnLasthealth > 0) llasthealth = 20;

			if (isResist) {
				if (li * 2 + 1 < llasthealth) {
					this.drawTexturedModalRect(ldrawx, ldrawy, icon + 54, 0, 9, 9);
				}
				if (li * 2 + 1 == llasthealth) {
					this.drawTexturedModalRect(ldrawx, ldrawy, icon + 63, 0, 9, 9);
				}
			}

			if (absorption > 0.0F) {
				if (absorption == origAbsorption && origAbsorption % 2.0F == 1.0F) {
					this.drawTexturedModalRect(ldrawx, ldrawy, icon + 153, 0, 9, 9);
				} else {
					this.drawTexturedModalRect(ldrawx, ldrawy, icon + 144, 0, 9, 9);
				}

				absorption -= 2.0F;
			} else {
				if (li * 2 + 1 < lhealth) {
					this.drawTexturedModalRect(ldrawx, ldrawy, icon + 36, 0, 9, 9);
				}
				if (li * 2 + 1 == lhealth) {
					this.drawTexturedModalRect(ldrawx, ldrawy, icon + 45, 0, 9, 9);
				}
			}
		}

//		int orgnLasthealth = orgnHealth + MathHelper.ceiling_float_int(entity.getLastDamage());
//		float origAbsorption = entity.getAbsorptionAmount();
//		float absorption = origAbsorption;
//		float maxHealth = (float) entity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getAttributeValue();
//		int maxHealthRows = MathHelper.ceiling_float_int((maxHealth + origAbsorption) / 20.0F);
//		int healthRows = MathHelper.ceiling_float_int(orgnHealth / 20f);
//		int isregene = -1;
//		if (entity.isPotionActive(Potion.getPotionById(10))) {
//			isregene = updateCounter % MathHelper.ceiling_float_int(maxHealth + 5.0F);
//		}
//		boolean isResist = entity.hurtResistantTime / 3 % 2 == 1;
//		if (entity.hurtResistantTime < 10) {
//			isResist = false;
//		}
//		int love = MathHelper.ceiling_float_int(entity.getStatus(KIND_STATUS.STATUS_LOVE).getStatus());
//











		// 防具
		int larmor = entity.getTotalArmorValue();
		ldrawy = guiTop + 64;
		for (int li = 0; li < 10; ++li) {
				ldrawx = guiLeft + li * 8 + 7;

				if (li * 2 + 1 < larmor) {
					this.drawTexturedModalRect(ldrawx, ldrawy, 34, 9, 9, 9);
				}
				if (li * 2 + 1 == larmor) {
					this.drawTexturedModalRect(ldrawx, ldrawy, 25, 9, 9, 9);
				}
				if (li * 2 + 1 > larmor) {
					this.drawTexturedModalRect(ldrawx, ldrawy, 16, 9, 9, 9);
				}
		}





		// Air
		ldrawy = guiTop + 46;
		if (entitylittlemaid.isInsideOfMaterial(Material.water)) {
			int var23 = entitylittlemaid.getAir();
			int var35 = MathHelper.ceiling_double_int((var23 - 2) * 10.0D / 300.0D);
			int var25 = MathHelper.ceiling_double_int(var23 * 10.0D / 300.0D) - var35;

			for (int var26 = 0; var26 < var35 + var25; ++var26) {
				ldrawx = guiLeft + var26 * 8 + 86;
				if (var26 < var35) {
					this.drawTexturedModalRect(ldrawx, ldrawy, 16, 18, 9, 9);
				} else {
					this.drawTexturedModalRect(ldrawx, ldrawy, 25, 18, 9, 9);
				}
			}
		}

	}
	private void displayDebuffEffects() {
		// ポーションエフェクトの表示
		int lx = guiLeft - 124;
		int ly = guiTop;
		Collection collection = entity.getActivePotionEffects();
		if (collection.isEmpty()) {
			return;
		}
		int lh = 33;
		if (collection.size() > 5) {
			lh = 132 / (collection.size() - 1);
		}
		for (Iterator iterator = entity.getActivePotionEffects().iterator(); iterator.hasNext();) {
			PotionEffect potioneffect = (PotionEffect) iterator.next();
			Potion potion = potioneffect.getPotion();
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/inventory.png"));
			drawTexturedModalRect(lx, ly, 0, ySizebk, 140, 32);

			if (potion.hasStatusIcon()) {
				int i1 = potion.getStatusIconIndex();
				drawTexturedModalRect(lx + 6, ly + 7, 0 + (i1 % 8) * 18,
						ySizebk + 32 + (i1 / 8) * 18, 18, 18);
			}
			String ls = I18n.translateToLocal(potion.getName());
			if (potioneffect.getAmplifier() > 0) {
				ls = (new StringBuilder()).append(ls).append(" ")
						.append(I18n.translateToLocal((new StringBuilder())
								.append("potion.potency.")
								.append(potioneffect.getAmplifier())
								.toString())).toString();
			}
			mc.fontRendererObj.drawString(ls, lx + 10 + 18, ly + 6, 0xffffff);
			// TODO ここもよく分からん
			String s1 = Potion.getPotionDurationString(potioneffect, 1);
			mc.fontRendererObj.drawString(s1, lx + 10 + 18, ly + 6 + 10, 0x7f7f7f);
			ly += lh;
		}
	}
	private static class RenderInfoPart{
		private static boolean shiftLock;

		private static boolean renderInfo[] = new boolean[]{
				true, false, true, false
		};

		private static int renderingPart = 0;

		public static int getEnabledCounts(){
			int count = 0;
			for (boolean s: renderInfo){
				if(s) ++count;
			}
			return count;
		}

		public static void setEnabled(int index, boolean flag){
			renderInfo[index] = flag;
			renderInfo[0] = true;
			if(renderingPart == index && !flag){
				shiftPart();
			}
		}

		public static boolean isEnabled(int index){
			try{
				return renderInfo[index];
			}catch(IndexOutOfBoundsException ex){
				return false;
			}
		}

		public static void shiftPart(){
			while(!isEnabled(++renderingPart)){
				if(renderingPart >= renderInfo.length){
					renderingPart = -1;
				}
			}
		}

		public static int getRenderingPart(){
			return renderingPart;
		}

		public static void lock(){
			shiftLock = true;
		}

		public static void unlock(){
			shiftLock = false;
		}

		public static boolean islocked(){
			return shiftLock;
		}
	}

}
