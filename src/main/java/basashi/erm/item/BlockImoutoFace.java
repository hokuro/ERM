package basashi.erm.item;

import com.google.common.base.Predicate;

import basashi.erm.core.ModCommon;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockImoutoFace extends BlockHorizontal{
	public static final String NAME = "imoutoface";
	    private BlockPattern snowmanBasePattern;
	    private BlockPattern snowmanPattern;
	    private BlockPattern golemBasePattern;
	    private BlockPattern golemPattern;
	    private static final Predicate<IBlockState> IS_PUMPKIN = new Predicate<IBlockState>()
	    {
	        public boolean apply(IBlockState p_apply_1_)
	        {
	            return p_apply_1_ != null && (p_apply_1_.getBlock() == Blocks.pumpkin || p_apply_1_.getBlock() == Blocks.lit_pumpkin);
	        }
	    };

	    protected BlockImoutoFace()
	    {
	        super(Material.gourd, MapColor.adobeColor);
			if (!ModCommon.norelease){
				// タブに登録しない
				this.setCreativeTab(null);
			}else{
				this.setCreativeTab(CreativeTabs.tabMaterials);
			}
	        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
	    }

	    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
	    {
	        super.onBlockAdded(worldIn, pos, state);
	    }

	    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
	    {
	        return worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos) && worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos, EnumFacing.UP);
	    }

	    /**
	     * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
	     * blockstate.
	     */
	    public IBlockState withRotation(IBlockState state, Rotation rot)
	    {
	        return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
	    }

//	    /**
//	     * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
//	     * blockstate.
//	     */
//	    public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
//	    {
//	        return state.withRotation(mirrorIn.toRotation((EnumFacing)state.getValue(FACING)));
//	    }

	    /**
	     * Called by ItemBlocks just before a block is actually set in the world, to allow for adjustments to the
	     * IBlockstate
	     */
	    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	    {
	        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	    }

	    /**
	     * Convert the given metadata into a BlockState for this Block
	     */
	    public IBlockState getStateFromMeta(int meta)
	    {
	        return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));
	    }

	    /**
	     * Convert the BlockState into the correct metadata value
	     */
	    public int getMetaFromState(IBlockState state)
	    {
	        return ((EnumFacing)state.getValue(FACING)).getHorizontalIndex();
	    }

	    protected BlockStateContainer createBlockState()
	    {
	        return new BlockStateContainer(this, new IProperty[] {FACING});
	    }
}
