package basashi.erm.render;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;

import basashi.erm.entity.EntityERMBase;
import basashi.erm.model.ModelERMBase;
import basashi.erm.resource.ERMResourceManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

public class RenderEntityERMBase extends RenderLiving<EntityERMBase> {

	/**
	 * デフォルトテクスチャ
	 */
	protected ResourceLocation defaultTexture;
	/**
	 * カスタムテクスチャ
	 */
	protected ResourceLocation dynamicTexture = null;

	public RenderEntityERMBase(RenderManager rendermanagerIn, ModelERMBase modelbaseIn, float shadowsizeIn) {
		super(rendermanagerIn, modelbaseIn, shadowsizeIn);
		defaultTexture = modelbaseIn.getDefaultResource();
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityERMBase entity){
		if (entity != null && entity.settingTexture().canUseCustomTexture() && entity.settingTexture().doUpdateTexture()){
			try {
				// 特製テクスチャを読み込む
				InputStream istream = new FileInputStream(new File(ERMResourceManager.instance().saveDir(),entity.settingTexture().TextureFile()));
				dynamicTexture = Minecraft.getMinecraft().renderEngine.getDynamicTextureLocation(entity.EntityName() + "_texture",new DynamicTexture(ImageIO.read(istream)));
				istream.close();
				entity.settingTexture().checkUpdateTextureFlag();
			} catch (Exception e) {
				// 読み込めない場合デフォルトテクスチャを使用する
				dynamicTexture = null;
			}
		}
		return dynamicTexture!=null?dynamicTexture:defaultTexture;
	}

	@Override
	public void doRender(EntityERMBase entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		this.bindTexture(getEntityTexture(entity));
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}
}
