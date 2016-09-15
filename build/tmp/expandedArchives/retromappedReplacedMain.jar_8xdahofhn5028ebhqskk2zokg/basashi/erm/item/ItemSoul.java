package basashi.erm.item;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import basashi.erm.core.Mod_ERM;
import basashi.erm.core.log.ModLog;
import basashi.erm.entity.EntityERMBase;
import basashi.erm.entity.EntityLittleGirl;
import basashi.erm.entity.IEntityERMBase;
import net.minecraft.block.BlockFence;
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
	    public static Class[] spawnableEntities = {
	            EntityLittleGirl.class,
	    };

	    public ItemSoul()
	    {
	        this.func_77627_a(true);
	    }

	    @Override
	    public String func_77653_i(ItemStack par1ItemStack)
	    {
	        String s = ("" + I18n.func_74838_a(this.func_77658_a() + ".name")).trim();
	        Class c = spawnableEntities[par1ItemStack.func_77952_i()];
	        String s1 = (String) EntityList.field_75626_c.get(c);

	        if (s1 != null)
	        {
	            s = s + " " + I18n.func_74838_a("entity." + s1 + ".name");
	        }

	        return s;
	    }

	    // ItemMonsterPlacerのspawnCreatureがstaticでオーバーライドできないので呼び出し側をコピペ
	    @Override
	    public EnumActionResult func_180614_a(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	    {
	        if (worldIn.field_72995_K)
	        {
	            return EnumActionResult.SUCCESS;
	        }
	        else if (!playerIn.func_175151_a(pos.func_177972_a(facing), facing, stack))
	        {
	            return EnumActionResult.FAIL;
	        }
	        else
	        {
	            IBlockState iblockstate = worldIn.func_180495_p(pos);

	            pos = pos.func_177972_a(facing);
	            double d0 = 0.0D;

	            if (facing == EnumFacing.UP && iblockstate.func_177230_c() instanceof BlockFence)
	            {
	                d0 = 0.5D;
	            }

	            Entity entity = spawnCreature(worldIn, func_185080_h(stack), (double)pos.func_177958_n() + 0.5D, (double)pos.func_177956_o() + d0, (double)pos.func_177952_p() + 0.5D,playerIn);

	            if (entity != null)
	            {
	                if (entity instanceof EntityLivingBase && stack.func_82837_s())
	                {
	                    entity.func_96094_a(stack.func_82833_r());
	                }

	                func_185079_a(worldIn, playerIn, stack, entity);

	                if (!playerIn.field_71075_bZ.field_75098_d)
	                {
	                    --stack.field_77994_a;
	                }
	            }

	            return EnumActionResult.SUCCESS;
	        }
	    }

	    // ItemMonsterPlacerのspawnCreatureがstaticでオーバーライドできないので呼び出し側をコピペ
	    @Override
	    public ActionResult<ItemStack> func_77659_a(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
	    {
	        if (worldIn.field_72995_K)
	        {
	            return new ActionResult(EnumActionResult.PASS, itemStackIn);
	        }
	        else
	        {
	            RayTraceResult raytraceresult = this.func_77621_a(worldIn, playerIn, true);

	            if (raytraceresult != null && raytraceresult.field_72313_a == RayTraceResult.Type.BLOCK)
	            {
	                BlockPos blockpos = raytraceresult.func_178782_a();

	                if (!(worldIn.func_180495_p(blockpos).func_177230_c() instanceof BlockLiquid))
	                {
	                    return new ActionResult(EnumActionResult.PASS, itemStackIn);
	                }
	                else if (worldIn.func_175660_a(playerIn, blockpos) && playerIn.func_175151_a(blockpos, raytraceresult.field_178784_b, itemStackIn))
	                {
	                    Entity entity = spawnCreature(worldIn, func_185080_h(itemStackIn), (double)blockpos.func_177958_n() + 0.5D, (double)blockpos.func_177956_o() + 0.5D, (double)blockpos.func_177952_p() + 0.5D,playerIn);

	                    if (entity == null)
	                    {
	                        return new ActionResult(EnumActionResult.PASS, itemStackIn);
	                    }
	                    else
	                    {
	                        if (entity instanceof EntityLivingBase && itemStackIn.func_82837_s())
	                        {
	                            entity.func_96094_a(itemStackIn.func_82833_r());
	                        }

	                        func_185079_a(worldIn, playerIn, itemStackIn, entity);

	                        if (!playerIn.field_71075_bZ.field_75098_d)
	                        {
	                            --itemStackIn.field_77994_a;
	                        }

	                        playerIn.func_71029_a(StatList.func_188057_b(this));
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
	        Entity entity = null;
	        try {
	            if (Mod_ERM.isAliveLittelGirl(c)){
	            	// スポーン済みならスポーン済みの個体をテレポート
	            	entity = Mod_ERM.getEntity(c);
	            	entity.func_70012_b(x, y+1, z, MathHelper.func_76142_g(par0World.field_73012_v.nextFloat() * 360.0F), 0.0F);
	            }else{
	            	// 未スポーンなら新規にスポーンさせる
    	            entity = (Entity)c.getConstructor(new Class[] {World.class}).newInstance(new Object[] {par0World});
		            EntityLiving entityliving = (EntityLiving)entity;
		            entity.func_70012_b(x, y+1, z, MathHelper.func_76142_g(par0World.field_73012_v.nextFloat() * 360.0F), 0.0F);
		            entityliving.field_70759_as = entityliving.field_70177_z;
		            entityliving.field_70761_aq = entityliving.field_70177_z;
	                entityliving.func_180482_a(par0World.func_175649_E(new BlockPos(entityliving)), (IEntityLivingData)null);
		            par0World.func_72838_d(entity);
		            entityliving.func_70642_aH();
		            ((IEntityERMBase)entityliving).initOwner(owner);
		            Mod_ERM.setEntityInstance(((EntityERMBase)entityliving));
		            ModLog.log().debug(entityliving.field_70165_t+","+entityliving.field_70163_u+","+entityliving.field_70161_v);
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
	    public void func_150895_a(Item itemIn, CreativeTabs tab, List<ItemStack> subItems)
	    {
	        for(int i = 0; i < spawnableEntities.length; ++i) {
				ItemStack itemstack = new ItemStack(itemIn, 1,i);
	            func_185078_a(itemstack, Integer.toString(i));
	            subItems.add(itemstack);
	        }
	    }
}
