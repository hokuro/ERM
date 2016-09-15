package basashi.erm.ai;

import basashi.erm.entity.EntityERMBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathNavigate;

public class EntityAILittleGirlDefault extends EntityAIBase {

	private EntityERMBase littleGirl;
	private Entity theOwner;
	private float moveSpeed;
	private PathNavigate petPathfinder;
	private int field_48310_h;
	private double maxDist;
	private double minDist;
	protected double sprintDist;
	protected double toDistance;
	protected boolean isEnable;

	public EntityAILittleGirlDefault(EntityERMBase entity,
			float pSpeed, double pMin, double pMax, double pSprintDistSQ) {
		littleGirl = entity;
		moveSpeed = pSpeed;
		petPathfinder = entity.getNavigator();
		setMinDist(pMin);
		setMaxDist(pMax);
		sprintDist = pSprintDistSQ;
		isEnable = true;
		setMutexBits(3);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		if (!isEnable)
			return false;

		Entity entityliving = littleGirl.getOwner();
		if (entityliving == null) {
			return false;
		}

		if (littleGirl.isSitting()||littleGirl.isWait()) {
			return false;
		}

		toDistance = littleGirl.getDistanceSqToEntity(entityliving);
		if (toDistance < getMinDist() && !littleGirl.isInWater()) {
			return false;
		}
		theOwner = entityliving;
		return true;
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean continueExecuting() {
		toDistance = littleGirl.getDistanceSqToEntity(theOwner);
		return !littleGirl.getNavigator().noPath()
				&& (toDistance > getMaxDist())
				&& !littleGirl.isSitting();
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		field_48310_h = 0;
	}

	/**
	 * Resets the task
	 */
	public void resetTask() {
		littleGirl.setSprinting(false);
		theOwner = null;
		petPathfinder.clearPathEntity();
	}

	/**
	 * Updates the task
	 */
	public void updateTask() {
		if (toDistance - getMaxDist() > 1.0) {
			littleGirl.getLookHelper().setLookPositionWithEntity(theOwner, 10F, littleGirl.getVerticalFaceSpeed());
		}

		if (littleGirl.isSitting()) {
			return;
		}
		// 指定距離以上ならダッシュ
		if(!littleGirl.isInWater()){
			littleGirl.setSprinting(toDistance > sprintDist);
			if (--field_48310_h > 0) {
				return;
			}
		}

		field_48310_h = 10;

		PathEntity entity = littleGirl.getNavigator().getPathToEntityLiving(theOwner);
		littleGirl.getNavigator().setPath(entity, moveSpeed);
	}

	public double getMinDist() {
		return minDist;
	}

	public void setMinDist(double minDist) {
		this.minDist = minDist;
	}

	public double getMaxDist() {
		return maxDist;
	}

	public void setMaxDist(double maxDist) {
		this.maxDist = maxDist;
	}
}
