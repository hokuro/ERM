package basashi.erm.ai;

import basashi.erm.entity.EntityERMBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class EntityAIERMSwiming extends EntityAIBase {

	private EntityERMBase entity;

	public EntityAIERMSwiming(EntityERMBase entity){
        this.entity = entity;
        this.setMutexBits(4);
        ((PathNavigateGround)entity.getNavigator()).setCanSwim(true);
	}

	@Override
	public boolean shouldExecute() {
		return entity.isInWater() || entity.isInsideOfMaterial(Material.water) || entity.isInLava();
	}



	@Override
	public void updateTask() {
        if (this.entity.getRNG().nextFloat() < 0.8F)
        {
            this.entity.getJumpHelper().setJumping();
        }

		double totalmotionY = 0d;
		if(entity.isInLava()){
			entity.getJumpHelper().setJumping();
			return;
		}
		if(entity.isInWater()){
			double xd = entity.posX;
			double yd = entity.getEntityBoundingBox().minY;
			double zd = entity.posZ;
			int x = MathHelper.floor_double(xd);
			int z = MathHelper.floor_double(zd);
			int y = MathHelper.floor_double(yd);
			totalmotionY += 0.03D * MathHelper.cos(entity.ticksExisted/16f);

			PathPoint pathPoint = null;
			PathEntity pathEntity = entity.getNavigator().getPath();

			// Main AI
			if(pathEntity!=null){
				pathPoint = pathEntity.getFinalPathPoint();
				entity.motionX = ((pathPoint.xCoord>x)?1:(pathPoint.xCoord<x)?-1:0) * entity.getAIMoveSpeed()/5d;
				entity.motionZ = ((pathPoint.zCoord>z)?1:(pathPoint.zCoord<z)?-1:0) * entity.getAIMoveSpeed()/5d;
				totalmotionY +=		((pathPoint.yCoord>=y)?1:-1) * entity.getAIMoveSpeed()/3d;
			}

			if(entity.isInWater()){
				IBlockState iState;

				// Going ashore
				if (pathPoint != null && Math.abs(pathPoint.yCoord - yd) < 3d && Math.pow(pathPoint.xCoord - xd, 2) + Math.pow(pathPoint.zCoord - zd, 2) < 9d &&
						(iState = entity.worldObj.getBlockState(new BlockPos(pathPoint.xCoord, pathPoint.yCoord + 1, pathPoint.zCoord)))
						.getBlock().getMaterial(iState) != Material.water) {
					totalmotionY += 0.05D;
				}
				entity.motionY = totalmotionY;
			}
			// Breathing
			if (entity.getAir() <= 0) {
				entity.motionY += 0.1D;
			}
		}
	}
}
