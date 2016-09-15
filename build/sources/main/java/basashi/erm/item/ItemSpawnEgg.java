package basashi.erm.item;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import basashi.erm.entity.EntityLittleGirl;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSpawnEgg extends ItemMonsterPlacer {
	   public final int primaryColor;
	   public final int secondaryColor;

	    // このスポーンエッグから生成されるエンティティのリスト
	    public static Class[] spawnableEntities = {
	            EntityLittleGirl.class,
	    };

	    public ItemSpawnEgg(int par1, int par2)
	    {
	        this.setHasSubtypes(true);
	        this.primaryColor = par1;
	        this.secondaryColor = par2;
	    }

	    // スポーンエッグの配色。このサンプルでは全部同じ配色
	    @SideOnly(Side.CLIENT)
	    public int getColorFromItemStack(ItemStack par1ItemStack, int par2)
	    {
	        return par2 == 0 ? primaryColor : secondaryColor;
	    }

	    @Override
	    public String getItemStackDisplayName(ItemStack par1ItemStack)
	    {
	        String s = ("" + I18n.translateToLocal(this.getUnlocalizedName() + ".name")).trim();
	        Class c = spawnableEntities[par1ItemStack.getItemDamage()];
	        String s1 = (String) EntityList.classToStringMapping.get(c);

	        if (s1 != null)
	        {
	            s = s + " " + I18n.translateToLocal("entity." + s1 + ".name");
	        }

	        return s;
	    }

	    // ItemMonsterPlacerのspawnCreatureがstaticでオーバーライドできないので呼び出し側をコピペ
	    @Override
	    public EnumActionResult onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, BlockPos pos, EnumHand hand, EnumFacing par7, float par8, float par9, float par10)
	    //public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
	    {
	        if (par3World.isRemote)
	        {
	            return EnumActionResult.SUCCESS;
	        }
	        else
	        {
	        	IBlockState state = par3World.getBlockState(pos);
	            Block block = par3World.getBlockState(pos).getBlock();
	            pos.offset(par7);
	            double d0 = 0.0D;

	            if (par7 == EnumFacing.UP && block.getRenderType(state) == EnumBlockRenderType.INVISIBLE)
	            {
	                d0 = 0.5D;
	            }

	            Entity entity = spawnCreature(par3World, par1ItemStack.getItemDamage(), (double)pos.getX() + 0.5D, (double)pos.getY() + d0, (double)pos.getZ() + 0.5D);

	            if (entity != null)
	            {
	                if (entity instanceof EntityLivingBase && par1ItemStack.hasDisplayName())
	                {
	                    ((EntityLiving)entity).setCustomNameTag(par1ItemStack.getDisplayName());
	                }

	                if (!par2EntityPlayer.capabilities.isCreativeMode)
	                {
	                    --par1ItemStack.stackSize;
	                }
	            }

	            return EnumActionResult.SUCCESS;
	        }
	    }

	    // ItemMonsterPlacerのspawnCreatureがstaticでオーバーライドできないので呼び出し側をコピペ
	    @Override
	    public ActionResult<ItemStack> onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, EnumHand hand)
	    //public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	    {
	        if (par2World.isRemote)
	        {
	            return new ActionResult(EnumActionResult.SUCCESS, par1ItemStack);
	        }
	        else
	        {
	        	RayTraceResult movingobjectposition = this.getMovingObjectPositionFromPlayer(par2World, par3EntityPlayer, true);

	            if (movingobjectposition == null)
	            {
	            	 return new ActionResult(EnumActionResult.SUCCESS, par1ItemStack);
	            }
	            else
	            {
	                if (movingobjectposition.typeOfHit == RayTraceResult.Type.BLOCK)
	                {
	                    int i = movingobjectposition.getBlockPos().getX();
	                    int j = movingobjectposition.getBlockPos().getY();
	                    int k = movingobjectposition.getBlockPos().getZ();

	                    if (!par2World.canMineBlockBody(par3EntityPlayer, movingobjectposition.getBlockPos()))
	                    {
	                    	 return new ActionResult(EnumActionResult.SUCCESS, par1ItemStack);
	                    }

	                    if (!par3EntityPlayer.canPlayerEdit(movingobjectposition.getBlockPos(), movingobjectposition.sideHit, par1ItemStack))
	                    {
	                    	 return new ActionResult(EnumActionResult.SUCCESS, par1ItemStack);
	                    }

	                    if (par2World.getBlockState(movingobjectposition.getBlockPos()).getBlock() instanceof BlockLiquid)
	                    {
	                        Entity entity = spawnCreature(par2World, par1ItemStack.getItemDamage(), (double)i, (double)j, (double)k);

	                        if (entity != null)
	                        {
	                            if (entity instanceof EntityLivingBase && par1ItemStack.hasDisplayName())
	                            {
	                                ((EntityLiving)entity).setCustomNameTag(par1ItemStack.getDisplayName());
	                            }

	                            if (!par3EntityPlayer.capabilities.isCreativeMode)
	                            {
	                                --par1ItemStack.stackSize;
	                            }
	                        }
	                    }
	                }

	                return new ActionResult(EnumActionResult.SUCCESS, par1ItemStack);
	            }
	        }
	    }
	    // spawnableEntitiesのエンティティをスポーンさせるようにItemMonsterPlacerのspawnCreatureを改変
	    public static Entity spawnCreature(World par0World, int par1, double par2, double par4, double par6)
	    {
	        Class c = spawnableEntities[par1];
	        Entity entity = null;
	        try {
	            entity = (Entity)c.getConstructor(new Class[] {World.class}).newInstance(new Object[] {par0World});

	            EntityLiving entityliving = (EntityLiving)entity;
	            entity.setLocationAndAngles(par2, par4, par6, MathHelper.wrapAngleTo180_float(par0World.rand.nextFloat() * 360.0F), 0.0F);
	            entityliving.rotationYawHead = entityliving.rotationYaw;
	            entityliving.renderYawOffset = entityliving.rotationYaw;
                entityliving.onInitialSpawn(par0World.getDifficultyForLocation(new BlockPos(entityliving)), (IEntityLivingData)null);
	            par0World.spawnEntityInWorld(entity);
	            entityliving.playLivingSound();
	        } catch (InstantiationException e) {
	            e.printStackTrace();
	        } catch (IllegalAccessException e) {
	            e.printStackTrace();
	        } catch (InvocationTargetException e) {
	            e.printStackTrace();
	        } catch (NoSuchMethodException e) {
	            e.printStackTrace();
	        }

	        return entity;
	    }

	    // spawnableEntitiesの各エンティティをスポーンさせるスポーンエッグを登録
	    @SideOnly(Side.CLIENT)
	    @Override
	    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems)
	    {
	        for(int i = 0; i < spawnableEntities.length; ++i) {
	        	subItems.add(new ItemStack(itemIn, 1, i));
	        }
	    }
}
