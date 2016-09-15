package basashi.erm.client;

import basashi.erm.config.ConfigValue;
import basashi.erm.core.CommonProxy;
import basashi.erm.core.Mod_ERM;
import basashi.erm.entity.EntityGirl;
import basashi.erm.entity.EntityImouto;
import basashi.erm.entity.EntityLittleGirl;
import basashi.erm.entity.EntityLittleLady;
import basashi.erm.entity.EntityLittleSister;
import basashi.erm.render.RenderEntityERMBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientProxy extends CommonProxy{


	public void registerEntityRender(){
		// 幼女
		RenderingRegistry.registerEntityRenderingHandler(EntityLittleGirl.class,  new IRenderFactory<EntityLittleGirl>() {
			@Override
			public Render<? super EntityLittleGirl> createRenderFor(RenderManager manager) {
				return new RenderEntityERMBase(manager, Mod_ERM.littleGirlModel, 0.5f);
			}
		});

		// 少女
		RenderingRegistry.registerEntityRenderingHandler(EntityGirl.class,  new IRenderFactory<EntityGirl>() {
			@Override
			public Render<? super EntityGirl> createRenderFor(RenderManager manager) {
				return new RenderEntityERMBase(manager, Mod_ERM.girlModl, 0.5f);
			}
		});

		// 妹
		RenderingRegistry.registerEntityRenderingHandler(EntityLittleSister.class,  new IRenderFactory<EntityLittleSister>() {
			@Override
			public Render<? super EntityLittleSister> createRenderFor(RenderManager manager) {
				return new RenderEntityERMBase(manager, Mod_ERM.littleSisterModel, 0.5f);
			}
		});

		// お嬢
		RenderingRegistry.registerEntityRenderingHandler(EntityLittleLady.class,  new IRenderFactory<EntityLittleLady>() {
			@Override
			public Render<? super EntityLittleLady> createRenderFor(RenderManager manager) {
				return new RenderEntityERMBase(manager, Mod_ERM.littleLadyModel, 0.5f);
			}
		});

		if (ConfigValue._imouto.canSister){
			// イモウト
			RenderingRegistry.registerEntityRenderingHandler(EntityImouto.class,  new IRenderFactory<EntityImouto>() {
				@Override
				public Render<? super EntityImouto> createRenderFor(RenderManager manager) {
					return new RenderEntityERMBase(manager, Mod_ERM.imoutoModel, 0.5f);
				}
			});
		}

		RenderManager manager = Minecraft.getMinecraft().getRenderManager();
		RenderingRegistry.loadEntityRenderers(manager, manager.entityRenderMap);
	}

	@SideOnly(Side.CLIENT)
	public void registerColorMap(){

	}
}
