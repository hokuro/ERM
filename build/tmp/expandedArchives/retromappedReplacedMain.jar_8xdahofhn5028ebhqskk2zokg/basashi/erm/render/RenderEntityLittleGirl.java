package basashi.erm.render;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;

import basashi.erm.entity.EntityERMBase;
import basashi.erm.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

//public class RenderEntityLittleGirl extends RenderBiped<EntityTameable> {
public class RenderEntityLittleGirl extends RenderLiving<EntityERMBase> {
	private final ResourceLocation defaultTexture = new ResourceLocation("erm:textures/entity/LittleGirlDefault.png");
	private ResourceLocation dynamicTexture = null;

	public RenderEntityLittleGirl(RenderManager renderManagerIn, ModelBase modelbaseIn, float shadowsizeIn) {
		super(renderManagerIn, modelbaseIn, shadowsizeIn);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	protected ResourceLocation func_110775_a(EntityERMBase entity){
		// テクスチャが作成されていなければデフォルトテクスチャを使用する
		// ドレスの更新が必要か？
		if (((dynamicTexture == null)&&(!Util.StringisEmptyOrNull(entity.getTextureDress()))) ||
				entity.isUpdateDress()){
			try {
				// 特製テクスチャを読み込む
				InputStream istream = new FileInputStream(entity.getTextureDress());
				dynamicTexture = Minecraft.func_71410_x().field_71446_o.func_110578_a("littlegirl",new DynamicTexture(ImageIO.read(istream)));
				istream.close();
			} catch (Exception e) {
				// 読み込めない場合デフォルトテクスチャを使用する
				dynamicTexture = null;
			}
		}
		return dynamicTexture!=null?dynamicTexture:defaultTexture;
	}

	public int tic = 0;

	@Override
	public void func_76986_a(EntityERMBase entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		this.func_110776_a(func_110775_a(entity));
		super.func_76986_a(entity, x, y, z, entityYaw, partialTicks);
	}
}
