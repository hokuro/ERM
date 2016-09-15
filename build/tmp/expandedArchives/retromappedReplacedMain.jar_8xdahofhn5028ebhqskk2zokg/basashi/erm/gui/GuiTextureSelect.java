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
public void func_73866_w_(){
	selectPanel = new GuiTextureSlot(this);
	selectPanel.func_148134_d(4,5);
	field_146292_n.add(modeButton[0] = new GuiButton(100, field_146294_l / 2 - 55, field_146295_m - 55, 80, 20, "Texture"));
	field_146292_n.add(modeButton[1] = new GuiButton(101, field_146294_l / 2 + 30, field_146295_m - 30, 80, 20, "Armor"));
	field_146292_n.add(new GuiButton(200, field_146294_l / 2 - 10, field_146295_m - 30, 120, 20, "Select"));
	modeButton[0].field_146124_l = false;
}

@Override
protected void func_73869_a(char par1, int par2){
	if (par2 == 1){
		field_146297_k.func_147108_a(owner);
	}
}

@Override
protected void func_73864_a(int mouseX, int mouseY, int mouseButton) throws IOException{
	super.func_73864_a(mouseX, mouseY, mouseButton);
}

@Override
protected void func_146273_a(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick){
	super.func_146273_a(mouseX, mouseY, clickedMouseButton,timeSinceLastClick);
}

@Override
public void func_73863_a(int mouseX, int mouseY, float partialTicks){
	func_146276_q_();
	selectPanel.func_148128_a(mouseX,mouseY,partialTicks);
	func_73732_a(field_146297_k.field_71466_p, I18n.func_74838_a(screenTitle), field_146294_l / 2, 4, 0xffffff);

	super.func_73863_a(mouseX,mouseY,partialTicks);

	GlStateManager.func_179094_E();
	GlStateManager.func_179091_B();
	GlStateManager.func_179142_g();
	GlStateManager.func_179126_j();
	GlStateManager.func_179124_c(1.0F,1.0F,1.0F);
	RenderHelper.func_74520_c();
	OpenGlHelper.func_77475_a(OpenGlHelper.field_77476_b, 240.0F, 240.0F);
	// テクスチャボックス

	GlStateManager.func_179109_b(field_146294_l / 2 - 115F, field_146295_m - 5F, 100F);
	GlStateManager.func_179152_a(60F, -60F, 60F);

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


	GlStateManager.func_179101_C();
	GlStateManager.func_179121_F();
	RenderHelper.func_74518_a();
	GlStateManager.func_179101_C();
	OpenGlHelper.func_77473_a(OpenGlHelper.field_77476_b);
	GlStateManager.func_179090_x();
	OpenGlHelper.func_77473_a(OpenGlHelper.field_77478_a);
	RenderHelper.func_74520_c();
	GlStateManager.func_179091_B();
}

@Override
public void func_146269_k() throws IOException{
	super.func_146269_k();
}

@Override
public void func_146282_l() throws IOException{
	super.func_146282_l();
}

@Override
public void func_146274_d() throws IOException{
	super.func_146274_d();
	selectPanel.func_178039_p();
}


	@Override
	protected void func_146284_a(GuiButton par1GuiButton) {
		switch (par1GuiButton.field_146127_k) {
		case 100:
			modeButton[0].field_146124_l = false;
			modeButton[1].field_146124_l = true;
			selectPanel.setMode(false);
			break;
		case 101:
			modeButton[0].field_146124_l = true;
			modeButton[1].field_146124_l = false;
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
			field_146297_k.func_147108_a(owner);
			break;
		}
	}

}
