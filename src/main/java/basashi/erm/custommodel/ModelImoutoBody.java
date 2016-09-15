package basashi.erm.custommodel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockPart;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.client.renderer.block.model.BlockPartRotation;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemModelGenerator;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.block.model.SimpleBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.ModelStateComposition;
import net.minecraftforge.client.model.SimpleModelState;
import net.minecraftforge.client.model.animation.ModelBlockAnimation;
import net.minecraftforge.common.model.IModelPart;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.Properties;
public class ModelImoutoBody implements net.minecraftforge.client.model.IModel {

    private final ResourceLocation location;
    private final ModelBlock models;
    private final boolean uvlock;
    private final FaceBakery faceBakery = new FaceBakery();
    private final ModelBlockAnimation anime;
    private static final String EMPTY_MODEL_RAW = "{    \'elements\': [        {   \'from\': [0, 0, 0],            \'to\': [16, 16, 16],            \'faces\': {                \'down\': {\'uv\': [0, 0, 16, 16], \'texture\': \'\' }            }        }    ]}".replaceAll("\'", "\"");
    protected static final ModelBlock MODEL_GENERATED = ModelBlock.deserialize(EMPTY_MODEL_RAW);

    private static final List<ResourceLocation> textures = new ArrayList<ResourceLocation>(){
    	{add(new ResourceLocation("erm:items/imoutobody_03"));}
    	{add(new ResourceLocation("erm:items/imoutobody_02"));}
    	{add(new ResourceLocation("erm:items/imoutobody_01"));}
    };

    public ModelImoutoBody(ResourceLocation location, ModelBlock model, boolean uvlock, ModelBlockAnimation anime)
    {
        this.location = location;
        this.models = model;
        this.uvlock = uvlock;
        this.anime = anime;
    }

    public Collection<ResourceLocation> getDependencies()
    {
    	return Collections.emptyList();
    }

    public Collection<ResourceLocation> getTextures()
    {
    	ImmutableSet.Builder<ResourceLocation> builder = ImmutableSet.builder();

        if(hasItemModel(models))
        {
            for(String s : ItemModelGenerator.LAYERS)
            {
                String r = models.resolveTextureName(s);
                ResourceLocation loc = new ResourceLocation(r);
                if(!r.equals(s))
                {
                    builder.add(loc);
                }
            }
        }
        for(String s : models.textures.values())
        {
            if(!s.startsWith("#"))
            {
                builder.add(new ResourceLocation(s));
            }
        }
        return builder.build();
    }
    protected boolean hasItemModel(ModelBlock p_177581_1_)
    {
        return p_177581_1_ == null ? false : p_177581_1_.getRootModel() == MODEL_GENERATED;
    }

	@Override
    public IBakedModel bake(IModelState state, final VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
    {
        ModelBlock model = this.models;

        List<TRSRTransformation> newTransforms = Lists.newArrayList();
        for(int i = 0; i < model.getElements().size(); i++)
        {
            BlockPart part = model.getElements().get(i);
            newTransforms.add(anime.getPartTransform(state, part, i));
        }

        ItemCameraTransforms transforms = model.getAllTransforms();
        Map<TransformType, TRSRTransformation> tMap = Maps.newHashMap();
        tMap.putAll(IPerspectiveAwareModel.MapWrapper.getTransforms(transforms));
        tMap.putAll(IPerspectiveAwareModel.MapWrapper.getTransforms(state));
        IModelState perState = new SimpleModelState(ImmutableMap.copyOf(tMap));
        return bakeNormal(model, perState, state, newTransforms, format, bakedTextureGetter, uvlock);
    }

	private IBakedModel bakeNormal(final ModelBlock model, IModelState perState, final IModelState modelState, List<TRSRTransformation> newTransforms, final VertexFormat format, final Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter, boolean uvLocked)
    {
        final TRSRTransformation baseState = modelState.apply(Optional.<IModelPart>absent()).or(TRSRTransformation.identity());
        TextureAtlasSprite particle = bakedTextureGetter.apply(new ResourceLocation(model.resolveTextureName("particle")));
        SimpleBakedModel.Builder builder = (new SimpleBakedModel.Builder(model, model.createOverrides())).setTexture(particle);
        for(int i = 0; i < model.getElements().size(); i++)
        {
            BlockPart part = model.getElements().get(i);
            TRSRTransformation transformation = baseState;
            if(newTransforms.get(i) != null)
            {
                transformation = transformation.compose(newTransforms.get(i));
                BlockPartRotation rot = part.partRotation;
                if(rot == null) rot = new BlockPartRotation(new org.lwjgl.util.vector.Vector3f(), EnumFacing.Axis.Y, 0, false);
                part = new BlockPart(part.positionFrom, part.positionTo, part.mapFaces, rot, part.shade);
            }
            for(Map.Entry<EnumFacing, BlockPartFace> e : part.mapFaces.entrySet())
            {
                TextureAtlasSprite textureatlassprite1 = bakedTextureGetter.apply(new ResourceLocation(model.resolveTextureName(e.getValue().texture)));

                if (e.getValue().cullFace == null || !TRSRTransformation.isInteger(transformation.getMatrix()))
                {
                    builder.addGeneralQuad(makeBakedQuad(part, e.getValue(), textureatlassprite1, e.getKey(), transformation, uvLocked));
                }
                else
                {
                    builder.addFaceQuad(baseState.rotate(e.getValue().cullFace), makeBakedQuad(part, e.getValue(), textureatlassprite1, e.getKey(), transformation, uvLocked));
                }
            }
        }

        return new ModelImoutoBodyBaked(builder.makeBakedModel(), perState, format, bakedTextureGetter)
        {

            private ItemOverrideList overrides = new ItemOverrideList(models.getOverrides()){
                @Override
                public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity)
                {
                    net.minecraft.item.Item item = stack.getItem();
                    if (item != null && item.hasCustomProperties())
                    {
                        ResourceLocation location = applyOverride(stack, world, entity);
                        if (location != null)
                        {
                        	try{
//                        	ModelImoutoBodyBaked org = (ModelImoutoBodyBaked)originalModel;
//                            IModel mdl =  CustomModelLoader.instance.loadModel(new ResourceLocation(location.getResourceDomain(),"models/"+location.getResourcePath()));
//                            return mdl.bake(org.state,org.format,org.bakedTextureGetter);
                            return net.minecraft.client.Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager().getModel(new ModelResourceLocation(new ResourceLocation("erm","imoutobody_blocking"), "inventory"));
                        	}catch(Exception ex){}
                        }
                    }
                    return originalModel;
                }
            };

            @Override
            public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand)
            {
                if(state instanceof IExtendedBlockState)
                {
                    IExtendedBlockState exState = (IExtendedBlockState)state;
                    if(exState.getUnlistedNames().contains(Properties.AnimationProperty))
                    {
                        IModelState newState = exState.getValue(Properties.AnimationProperty);
                        IExtendedBlockState newExState = exState.withProperty(Properties.AnimationProperty, null);
                        if(newState != null)
                        {
                            return ModelImoutoBody.this.bake(new ModelStateComposition(modelState, newState), format, bakedTextureGetter).getQuads(newExState, side, rand);
                        }
                    }
                }
                return super.getQuads(state, side, rand);
            }

            @Override
            public ItemOverrideList getOverrides()
            {
                return overrides;
            }
        };
    }

	  protected BakedQuad makeBakedQuad(BlockPart p_177589_1_, BlockPartFace p_177589_2_, TextureAtlasSprite p_177589_3_, EnumFacing p_177589_4_, net.minecraftforge.common.model.ITransformation p_177589_5_, boolean p_177589_6_)
	    {
	        return this.faceBakery.makeBakedQuad(p_177589_1_.positionFrom, p_177589_1_.positionTo, p_177589_2_, p_177589_3_, p_177589_4_, p_177589_5_, p_177589_1_.partRotation, p_177589_6_, p_177589_1_.shade);
	    }


	@Override
    public IModelState getDefaultState()
    {
        return ModelRotation.X0_Y0;
    }

}
