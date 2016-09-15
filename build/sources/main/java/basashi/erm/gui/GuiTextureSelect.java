package basashi.erm.gui;

import java.io.IOException;

import basashi.erm.entity.EntityERMBase;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.text.translation.I18n;

public class GuiTextureSelect extends GuiScreen {

private String screenTitle = "Dress up";
protected GuiScreen owner;
protected GuiTextureSlot selectPanel;
protected GuiButton modeButton[] = new GuiButton[2];
protected EntityERMBase target;
public int canSelectColor;
public int selectColor;
protected boolean toServer;

public GuiTextureSelect(GuiScreen owner, EntityERMBase target, int color, boolean toServer){
	this.owner = owner;
	this.target = target;
	this.toServer = toServer;
}

@Override
public void initGui(){
	selectPanel = new GuiTextureSlot(this);
	selectPanel.registerScrollButtons(4,5);
	buttonList.add(modeButton[0] = new GuiButton(100, width / 2 - 55, height - 55, 80, 20, "Texture"));
	buttonList.add(modeButton[1] = new GuiButton(101, width / 2 + 30, height - 30, 80, 20, "Armor"));
	buttonList.add(new GuiButton(200, width / 2 - 10, height - 30, 120, 20, "Select"));
	modeButton[0].enabled = false;
}

@Override
protected void keyTyped(char par1, int par2){
	if (par2 == 1){
		mc.displayGuiScreen(owner);
	}
}

@Override
protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException{
	super.mouseClicked(mouseX, mouseY, mouseButton);
}

@Override
protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick){
	super.mouseClickMove(mouseX, mouseY, clickedMouseButton,timeSinceLastClick);
}

@Override
public void drawScreen(int mouseX, int mouseY, float partialTicks){
	drawDefaultBackground();
	selectPanel.drawScreen(mouseX,mouseY,partialTicks);
	drawCenteredString(mc.fontRendererObj, I18n.translateToLocal(screenTitle), width / 2, 4, 0xffffff);

	super.drawScreen(mouseX,mouseY,partialTicks);

	GlStateManager.pushMatrix();
	GlStateManager.enableRescaleNormal();
	GlStateManager.enableColorMaterial();
	GlStateManager.enableDepth();
	GlStateManager.color(1.0F,1.0F,1.0F);
	RenderHelper.enableGUIStandardItemLighting();
	OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
	// テクスチャボックス

	GlStateManager.translate(width / 2 - 115F, height - 5F, 100F);
	GlStateManager.scale(60F, -60F, 60F);

//	selectPanel.entity.renderYawOffset = -25F;
//	selectPanel.entity.rotationYawHead = -10F;
//
//	ResourceLocation ltex[];
//	if (selectPanel.mode) {
//		selectPanel.entity.textureData.textureBox[0] = GuiTextureSlot.getBlankBox();
//		selectPanel.entity.textureData.textureBox[1] = lbox;
//		selectPanel.entity.setTextureNames("default");
//	} else {
//		selectPanel.entity.textureData.textureBox[0] = lbox;
//		selectPanel.entity.textureData.textureBox[1] = GuiTextureSlot.getBlankBox();
//		selectPanel.entity.setColor(selectColor);
//		selectPanel.entity.setTextureNames();
//	}

//	mc.getRenderManager().doRenderEntity(selectPanel.entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);


	GlStateManager.disableRescaleNormal();
	GlStateManager.popMatrix();
	RenderHelper.disableStandardItemLighting();
	GlStateManager.disableRescaleNormal();
	OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
	GlStateManager.disableTexture2D();
	OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
	RenderHelper.enableGUIStandardItemLighting();
	GlStateManager.enableRescaleNormal();
}

@Override
public void handleInput() throws IOException{
	super.handleInput();
}

@Override
public void handleKeyboardInput() throws IOException{
	super.handleKeyboardInput();
}

@Override
public void handleMouseInput() throws IOException{
	super.handleMouseInput();
	selectPanel.handleMouseInput();
}


	@Override
	protected void actionPerformed(GuiButton par1GuiButton) {
		switch (par1GuiButton.id) {
		case 100:
			modeButton[0].enabled = false;
			modeButton[1].enabled = true;
			selectPanel.setMode(false);
			break;
		case 101:
			modeButton[0].enabled = true;
			modeButton[1].enabled = false;
			selectPanel.setMode(true);
			break;
		case 200:
			if (selectPanel.texsel[0] > -1) {
				target.setTextureDress(selectPanel.getSelectedBox(false).textureName);
			}
			if (selectPanel.texsel[1] > -1) {
				target.setTextureArmor(selectPanel.getSelectedBox(true).textureName);
			}

			System.out.println(String.format("select: %d(%s), %d(%s)",
					selectPanel.texsel[0], target.getTextureBox()[0].textureName,
					selectPanel.texsel[1], target.getTextureBox()[1].textureName));
			mc.displayGuiScreen(owner);
			break;
		}
	}

}
