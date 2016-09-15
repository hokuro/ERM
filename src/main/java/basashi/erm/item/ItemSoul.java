package basashi.erm.item;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import basashi.erm.config.ConfigValue;
import basashi.erm.core.Mod_ERM;
import basashi.erm.core.log.ModLog;
import basashi.erm.entity.EntityERMBase;
import basashi.erm.entity.EntityGirl;
import basashi.erm.entity.EntityImouto;
import basashi.erm.entity.EntityLittleGirl;
import basashi.erm.entity.EntityLittleLady;
import basashi.erm.entity.EntityLittleSister;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSoul extends ItemMonsterPlacer {
	public static String NAME = "soul";

	    // このスポーンエッグから生成されるエンティティのリスト
	    public static final Class[] spawnableEntities = {
	    		EntityImouto.class,
	            EntityLittleGirl.class,
	            EntityGirl.class,
	            EntityLittleSister.class,
	            EntityLittleLady.class
	    };

	    public static final String[] spawnableEntityNames = {
	    		"imouto",
	    		"littlegirl"
	    };

	    public ItemSoul()
	    {
	        this.setHasSubtypes(true);
	        this.setCreativeTab(CreativeTabs.tabMisc);
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
	    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	    {
	        if (worldIn.isRemote)
	        {
	            return EnumActionResult.SUCCESS;
	        }
	        else if (!playerIn.canPlayerEdit(pos.offset(facing), facing, stack))
	        {
	            return EnumActionResult.FAIL;
	        }
	        else
	        {
	            IBlockState iblockstate = worldIn.getBlockState(pos);

	            pos = pos.offset(facing);
	            double d0 = 0.0D;

	            if (facing == EnumFacing.UP && iblockstate.getBlock() instanceof BlockFence)
	            {
	                d0 = 0.5D;
	            }

	            Entity entity = spawnCreature(worldIn, getEntityIdFromItem(stack), (double)pos.getX() + 0.5D, (double)pos.getY() + d0, (double)pos.getZ() + 0.5D,playerIn);

	            if (entity != null)
	            {
	                if (entity instanceof EntityLivingBase && stack.hasDisplayName())
	                {
	                    entity.setCustomNameTag(stack.getDisplayName());
	                }

	                applyItemEntityDataToEntity(worldIn, playerIn, stack, entity);

	                if (!playerIn.capabilities.isCreativeMode)
	                {
	                    --stack.stackSize;
	                }
	            }

	            return EnumActionResult.SUCCESS;
	        }
	    }

	    // ItemMonsterPlacerのspawnCreatureがstaticでオーバーライドできないので呼び出し側をコピペ
	    @Override
	    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
	    {
	        if (worldIn.isRemote)
	        {
	            return new ActionResult(EnumActionResult.PASS, itemStackIn);
	        }
	        else
	        {
	            RayTraceResult raytraceresult = this.getMovingObjectPositionFromPlayer(worldIn, playerIn, true);

	            if (raytraceresult != null && raytraceresult.typeOfHit == RayTraceResult.Type.BLOCK)
	            {
	                BlockPos blockpos = raytraceresult.getBlockPos();

	                if (!(worldIn.getBlockState(blockpos).getBlock() instanceof BlockLiquid))
	                {
	                    return new ActionResult(EnumActionResult.PASS, itemStackIn);
	                }
	                else if (worldIn.isBlockModifiable(playerIn, blockpos) && playerIn.canPlayerEdit(blockpos, raytraceresult.sideHit, itemStackIn))
	                {
	                    Entity entity = spawnCreature(worldIn, getEntityIdFromItem(itemStackIn), (double)blockpos.getX() + 0.5D, (double)blockpos.getY() + 0.5D, (double)blockpos.getZ() + 0.5D,playerIn);

	                    if (entity == null)
	                    {
	                        return new ActionResult(EnumActionResult.PASS, itemStackIn);
	                    }
	                    else
	                    {
	                        if (entity instanceof EntityLivingBase && itemStackIn.hasDisplayName())
	                        {
	                            entity.setCustomNameTag(itemStackIn.getDisplayName());
	                        }

	                        applyItemEntityDataToEntity(worldIn, playerIn, itemStackIn, entity);

	                        if (!playerIn.capabilities.isCreativeMode)
	                        {
	                            --itemStackIn.stackSize;
	                        }

	                        playerIn.addStat(StatList.func_188057_b(this));
	                        return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
	                    }
	                }
	                else
	                {
	                    return new ActionResult(EnumActionResult.FAIL, itemStackIn);
	                }
	            }
	            else
	            {
	                return new ActionResult(EnumActionResult.PASS, itemStackIn);
	            }
	        }
	    }

	    // spawnableEntitiesのエンティティをスポーンさせるようにItemMonsterPlacerのspawnCreatureを改変
	    public static Entity spawnCreature(World par0World, String id, double x, double y, double z, EntityPlayer owner)
	    {
            ModLog.log().debug(x+","+y+","+z);
	        Class c = spawnableEntities[Integer.parseInt(id)];

	        // イモウト使用の許可がない場合イモウトは召喚しない
	        if (!ConfigValue._imouto.canSister && c == EntityImouto.class){return null;}
	        Entity entity = null;
	        try {
	            if (Mod_ERM.isAliveEntity(c)){
	            	// スポーン済みならスポーン済みの個体をテレポート
	            	entity = Mod_ERM.getEntity(c);
	            	entity.setLocationAndAngles(x, y+1, z, MathHelper.wrapAngleTo180_float(par0World.rand.nextFloat() * 360.0F), 0.0F);
	            	entity = null;
	            }else{
	            	// 未スポーンなら新規にスポーンさせる
    	            entity = (Entity)c.getConstructor(new Class[] {World.class}).newInstance(new Object[] {par0World});
    	            EntityERMBase entityliving = (EntityERMBase)entity;
		            entity.setLocationAndAngles(x, y+1, z, MathHelper.wrapAngleTo180_float(par0World.rand.nextFloat() * 360.0F), 0.0F);
		            entityliving.rotationYawHead = entityliving.rotationYaw;
		            entityliving.renderYawOffset = entityliving.rotationYaw;
	                entityliving.onInitialSpawn(par0World.getDifficultyForLocation(new BlockPos(entityliving)), (IEntityLivingData)null);
		            par0World.spawnEntityInWorld(entity);
		            entityliving.playLivingSound();
		            entityliving.initOwner(owner);
		            Mod_ERM.setEntity(((EntityERMBase)entityliving));
		            ModLog.log().debug(entityliving.posX+","+entityliving.posY+","+entityliving.posZ);
	            }
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
	        	// イモウト未使用の場合、イモウト召喚用のアイテムは登録しない
	        	if (!ConfigValue._imouto.canSister && i == 0){continue;}
				ItemStack itemstack = new ItemStack(itemIn, 1,i);
	            applyEntityIdToItemStack(itemstack, Integer.toString(i));
	            subItems.add(itemstack);
	        }
	    }
}
