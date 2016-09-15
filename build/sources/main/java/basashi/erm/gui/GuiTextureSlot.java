package basashi.erm.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiSlot;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class GuiTextureSlot extends GuiSlot {

	public GuiTextureSelect parent;
	public int selected;
	public List<TextureBox> texDress;
	public List<TextureBox> texArmor;
	public boolean mode;
	public int texsel[] = new int[2];

	private ItemStack armors[] = new ItemStack[]{
			new ItemStack(Items.leather_boots),
			new ItemStack(Items.leather_leggings),
			new ItemStack(Items.leather_chestplate),
			new ItemStack(Items.leather_helmet)
	};
	protected boolean isContract;
	protected static TextureBox blankBox;

//	public EntityLittleMaideForTexSelect entity;

	public GuiTextureSlot(GuiTextureSelect parent){
		super(parent.mc, parent.width, parent.height, 16, parent.height - 64, 36);
		this.parent = parent;
//		entity = new EntityLittleMaidForTexSelect(parent.mc.theWorld);
//		bloankBox = new TextureBox();
//		bloankBox.models = new ModelERMBase[]{null, null, null};

		texsel[0] = 0;
		texsel[1] = 0;
		texDress = new ArrayList<TextureBox>();
		texArmor = new ArrayList<TextureBox>();

//		entity.setContract(isContract);

		TextureBoxBase ltbox[] = parent.target.getTextureBox();
//		for (int i = 0; i < ERMResourceManager.instance.getTextureCount(); i++){
//			TextureBox box = ERMResourceManager.getTextureList().get(i);
//			texDress.add(box);
//			if (box.hasArmor()){
//				texArmor.add(box);
//			}
//			if (box == ltbox[0]){
//				texsel[0] = texDress.size()-1;
//			}
//			if (box == ltbox[1]){
//				texsel[1] = texArmor.size()-1;
//			}
//		}
		setMode(false);
	}

	@Override
	protected int getSize(){
		return mode ? texArmor.size() : texDress.size();
	}



	@Override
	protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY){
		if (mode){
			selected = slotIndex;
			texsel[1] = slotIndex;
		}else{
			selected = slotIndex;
			texsel[0] = slotIndex;
		}
	}

	@Override
	protected void drawBackground(){
	}

	@Override
	protected void drawSlot(int entryID, int var2, int var3, int var4, int mouseXIn, int mouseYIn){
//		GlStateManager.pushMatrix();
//
//		TextureBox box;
//		if (mode){
//			box = texArmor.get(entryID);
////			entity.textureData.textureBox[0] = blankBox;
////			entity.textureData.textureBox[1] = box;
//		}else{
//			box = texDress.get(entryID);
////			entity.textureData.textureBox[0] = lbox;
////			entity.textureData.textureBox[1] = blankBox;
//		}
//
//		GlStateManager.enableBlent();
//		parent.drawString(parent.mc.fontRendereerObj, box.textureName,
//		var2 + 207 - mc.fontRendererObj.getStringWidth(box.textureName), var3 + 25, -1);
//		GlStateManager.translate(var2 + 8, var3 + 25, 50);
//		GlStateManager.Scale(12f,-12f,12f);
//		entity.rendereYawOffset=30F;
//		entity.rotationYawHead=15F;
//		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
//		entity.modeArmor = mode;
//
//		if (mode){
//			GlStateManager.Translagef(1f,0.25f,0f);
//			entity.setTextureName("default");
//			Minecraft.getMinecraft().getRenderManager().doRenderEntity(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
//			RendererHelper.setLightmapTextureCoords(61680);
//			for ( int i = 0; i < ERMResourceManager.armorFilenamePrefix.length; li++) {
//				GlStateManager.translate(1F,0F,0F);
//				if (box.armors.containsKey(ERMResourceManger.armorFilenamePrefix[i])){
//					entity.setTextureNames(ERMResourceManager.armorFilenamePrefix[i]);
//					Minecraft.getMinecraft().getRenderManager().doRenderEntity(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
//					RendererHelper.setLightmapTextureCoords(61680);
//				}
//			}
//		}else{
//			GlStateManager.Translage(1F,0,0);
//			entity.setTextureNames();
//			Minecraft.getMinecraft().getRenderManager().doRenderEntity(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
//			RendererHelper.setLightmapTextureCoords(61680);
//		}
//		RenderHelper.disableStandardItemLighting();
//		GlStateManager.disableRescaleNormal();
//		OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
//		GlStateManager.disableTexture2D();
//		OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
//		GlStateManager.popMatrix()

	}


	public static TextureBox getBlankBox(){
		return blankBox;
	}

	public TextureBox getSelectedBox(){
		return getSelectedBox(selected);
	}

	public TextureBox getSelectedBox(int index){
		return mode ? texArmor.get(index) : texDress.get(index);
	}

	public TextureBox getSelectedBox(boolean mode){
		return mode ? texArmor.get(texsel[1]):texDress.get(texsel[0]);
	}

	public void setMode(boolean flag){
//		scrollBy(slotHeight * - getSize());
//		entity.modeArmor = flag;
//		if (flag){
//			selected texsel[1];
//			mode = true;
//			entity.setItemStackToSlot(EntityEquipmentSlot.FEET,  armors[0]);
//			entity.setItemStackToSlot(EntityEquipmentSlot.LEGS,  armors[1]);
//			entity.setItemStackToSlot(EntityEquipmentSlot.CHEST, armors[2]);
//			entity.setItemStackToSlot(EntityEquipmentSlot.HEAD,  armors[3]);
//		}else{
//			selected = texsel[0];
//			mode = false;
//			entity.setItemStackToSlot(EntityEquipmentSlot.FEET,  null);
//			entity.setItemStackToSlot(EntityEquipmentSlot.LEGS,  null);
//			entity.setItemStackToSlot(EntityEquipmentSlot.CHEST, null);
//			entity.setItemStackToSlot(EntityEquipmentSlot.HEAD,  null);
//		}
//		scrollBy(slotHeight*selected);
	}

	@Override
	protected boolean isSelected(int slotIndex) {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}
}
