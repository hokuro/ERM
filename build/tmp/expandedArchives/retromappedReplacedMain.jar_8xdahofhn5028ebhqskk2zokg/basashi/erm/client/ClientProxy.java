package basashi.erm.client;

import basashi.erm.core.CommonProxy;
import basashi.erm.core.Mod_ERM;
import basashi.erm.entity.EntityLittleGirl;
import basashi.erm.render.RenderEntityLittleGirl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientProxy extends CommonProxy{


	public void registerEntityRender(){
		RenderingRegistry.registerEntityRenderingHandler(EntityLittleGirl.class,  new IRenderFactory<EntityLittleGirl>() {
			@Override
			public Render<? super EntityLittleGirl> createRenderFor(RenderManager manager) {
				return new RenderEntityLittleGirl(manager, Mod_ERM.getModel(EntityLittleGirl.class), 0.5f);
			}
		});
		RenderManager manager = Minecraft.func_71410_x().func_175598_ae();
		RenderingRegistry.loadEntityRenderers(manager, manager.field_78729_o);
	}

	@SideOnly(Side.CLIENT)
	public void registerColorMap(){

	}
}
