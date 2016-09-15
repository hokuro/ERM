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
	        this.func_77627_a(true);
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
	    public EnumActionResult func_180614_a(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, BlockPos pos, EnumHand hand, EnumFacing par7, float par8, float par9, float par10)
	    //public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
	    {
	        if (par3World.field_72995_K)
	        {
	            return EnumActionResult.SUCCESS;
	        }
	        else
	        {
	        	IBlockState state = par3World.func_180495_p(pos);
	            Block block = par3World.func_180495_p(pos).func_177230_c();
	            pos.func_177972_a(par7);
	            double d0 = 0.0D;

	            if (par7 == EnumFacing.UP && block.func_149645_b(state) == EnumBlockRenderType.INVISIBLE)
	            {
	                d0 = 0.5D;
	            }

	            Entity entity = spawnCreature(par3World, par1ItemStack.func_77952_i(), (double)pos.func_177958_n() + 0.5D, (double)pos.func_177956_o() + d0, (double)pos.func_177952_p() + 0.5D);

	            if (entity != null)
	            {
	                if (entity instanceof EntityLivingBase && par1ItemStack.func_82837_s())
	                {
	                    ((EntityLiving)entity).func_96094_a(par1ItemStack.func_82833_r());
	                }

	                if (!par2EntityPlayer.field_71075_bZ.field_75098_d)
	                {
	                    --par1ItemStack.field_77994_a;
	                }
	            }

	            return EnumActionResult.SUCCESS;
	        }
	    }

	    // ItemMonsterPlacerのspawnCreatureがstaticでオーバーライドできないので呼び出し側をコピペ
	    @Override
	    public ActionResult<ItemStack> func_77659_a(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, EnumHand hand)
	    //public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	    {
	        if (par2World.field_72995_K)
	        {
	            return new ActionResult(EnumActionResult.SUCCESS, par1ItemStack);
	        }
	        else
	        {
	        	RayTraceResult movingobjectposition = this.func_77621_a(par2World, par3EntityPlayer, true);

	            if (movingobjectposition == null)
	            {
	            	 return new ActionResult(EnumActionResult.SUCCESS, par1ItemStack);
	            }
	            else
	            {
	                if (movingobjectposition.field_72313_a == RayTraceResult.Type.BLOCK)
	                {
	                    int i = movingobjectposition.func_178782_a().func_177958_n();
	                    int j = movingobjectposition.func_178782_a().func_177956_o();
	                    int k = movingobjectposition.func_178782_a().func_177952_p();

	                    if (!par2World.canMineBlockBody(par3EntityPlayer, movingobjectposition.func_178782_a()))
	                    {
	                    	 return new ActionResult(EnumActionResult.SUCCESS, par1ItemStack);
	                    }

	                    if (!par3EntityPlayer.func_175151_a(movingobjectposition.func_178782_a(), movingobjectposition.field_178784_b, par1ItemStack))
	                    {
	                    	 return new ActionResult(EnumActionResult.SUCCESS, par1ItemStack);
	                    }

	                    if (par2World.func_180495_p(movingobjectposition.func_178782_a()).func_177230_c() instanceof BlockLiquid)
	                    {
	                        Entity entity = spawnCreature(par2World, par1ItemStack.func_77952_i(), (double)i, (double)j, (double)k);

	                        if (entity != null)
	                        {
	                            if (entity instanceof EntityLivingBase && par1ItemStack.func_82837_s())
	                            {
	                                ((EntityLiving)entity).func_96094_a(par1ItemStack.func_82833_r());
	                            }

	                            if (!par3EntityPlayer.field_71075_bZ.field_75098_d)
	                            {
	                                --par1ItemStack.field_77994_a;
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
	            entity.func_70012_b(par2, par4, par6, MathHelper.func_76142_g(par0World.field_73012_v.nextFloat() * 360.0F), 0.0F);
	            entityliving.field_70759_as = entityliving.field_70177_z;
	            entityliving.field_70761_aq = entityliving.field_70177_z;
                entityliving.func_180482_a(par0World.func_175649_E(new BlockPos(entityliving)), (IEntityLivingData)null);
	            par0World.func_72838_d(entity);
	            entityliving.func_70642_aH();
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
	        	subItems.add(new ItemStack(itemIn, 1, i));
	        }
	    }
}
