package basashi.erm.custommodel;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.commons.io.IOUtils;

import com.google.common.base.Charsets;

import basashi.erm.core.ModCommon;
import basashi.erm.item.ItemImoutoBody;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.animation.ModelBlockAnimation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CustomModelLoader implements ICustomModelLoader {
	public static final CustomModelLoader instance = new CustomModelLoader();


	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {
	}

	@Override
	public boolean accepts(ResourceLocation modelLocation) {
		if(!modelLocation.getResourceDomain().equals(ModCommon.MOD_ID)){
			return false;
		}

		if (modelLocation.getResourcePath().contains(ItemImoutoBody.NAME)){
			return true;
		}
		return false;
	}

	@Override
	public IModel loadModel(ResourceLocation modelLocation) throws Exception {
		if (modelLocation.getResourcePath().contains(ItemImoutoBody.NAME)){
	        String modelPath = modelLocation.getResourcePath();
	        if(modelLocation.getResourcePath().startsWith("models/"))
	        {
	            modelPath = modelPath.substring("models/".length());
	        }
	        ResourceLocation armatureLocation = new ResourceLocation(modelLocation.getResourceDomain(), "armatures/" + modelPath + ".json");
	        ModelBlockAnimation animation = ModelBlockAnimation.loadVanillaAnimation( Minecraft.getMinecraft().getResourceManager(), armatureLocation);
	        ModelBlock model = loadModels(modelLocation);
	        IModel iModel = new ModelImoutoBody(modelLocation, model, false,animation);
	        return iModel;
		}
		return null;
	}


    protected ModelBlock loadModels(ResourceLocation p_177594_1_) throws IOException
    {
        Reader reader = null;
        IResource iresource = null;
        ModelBlock lvt_5_1_;

        try
        {
            String s = p_177594_1_.getResourcePath();
            iresource = Minecraft.getMinecraft().getResourceManager().getResource(this.getModelLocation(p_177594_1_));
            reader = new InputStreamReader(iresource.getInputStream(), Charsets.UTF_8);
            lvt_5_1_ = ModelBlock.deserialize(reader);
            lvt_5_1_.name = p_177594_1_.toString();
            ModelBlock modelblock1 = lvt_5_1_;
            return modelblock1;
        }
        finally
        {
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly((Closeable)iresource);
        }

    }

    protected ResourceLocation getModelLocation(ResourceLocation model)
    {
        return new ResourceLocation(model.getResourceDomain(), model.getResourcePath() + ".json");
    }


    @Override
    public String toString()
    {
        return "VanillaLoader.INSTANCE";
    }


}
