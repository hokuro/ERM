package basashi.erm.ai.workmode;

import java.util.ArrayList;
import java.util.List;

import basashi.erm.entity.EntityERMBase;
import basashi.erm.resource.object.ResourceTag;
import basashi.erm.util.Values.KIND_WORK_CANCEL;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

public class DefaultAIForLittleGirl implements IERMAIBase {

	// サウンドタグ
	private static final List<ResourceTag> soundTags = new ArrayList<ResourceTag>();
	// 表示名
	private static final String NAME_DISPLAY = "Default";
	// 識別名
	private static final String NAME_IDENTIFY = "DefaultForLittleGirl";
	// タスク
	private EntityAITasks[] aitasks;

	public DefaultAIForLittleGirl(){

	}

	@Override
	public List<ResourceTag> getSoundTag() {
		return soundTags;
	}

	@Override
	public String DisplayName() {
		return NAME_DISPLAY;
	}

	@Override
	public String AIName() {
		return NAME_IDENTIFY;
	}

	@Override
	public boolean canSpeak() {
		return true;
	}

	@Override
	public float workStamina() {
		return 0;
	}

	@Override
	public ItemStack[] workItem() {
		return null;
	}

	@Override
	public ItemStack workTool() {
		return null;
	}

	@Override
	public EntityAITasks[] AITasks(EntityAITasks[] tsk, EntityERMBase entity) {
		if (aitasks == null){
			aitasks = new EntityAITasks[2];

			// ノーマル
			aitasks[0] = new EntityAITasks(entity.worldObj != null && entity.worldObj.theProfiler != null ? entity.worldObj.theProfiler :null);
			aitasks[0].addTask(1, new EntityAIWander(entity,1.0));
			aitasks[0].addTask(2, new EntityAILookIdle(entity));


//			// default
//			aitasks[0].addTask(1, new EntityAIERMSwiming(entity));
//			aitasks[0].addTask(2, new EntityAIERMWate(entity));
//			aitasks[0].addTask(3, aiJumpTo);
//			aitasks[0].addTask(4, aiFindBlock);
//			aitasks[0].addTask(5, aiAttack);
//			aitasks[0].addTask(6, aiShooting);
//			//ltasks[0].addTask(8, aiPanic);
//			aitasks[0].addTask(10, aiBeg);
//			aitasks[0].addTask(11, aiBegMove);
//			aitasks[0].addTask(20, aiAvoidPlayer);
//			aitasks[0].addTask(21, aiFreeRain);
//			aitasks[0].addTask(22, aiCollectItem);
//			// 移動用AI
//			aitasks[0].addTask(30, aiTracer);
//			aitasks[0].addTask(31, aiFollow);
//			aitasks[0].addTask(32, aiWander);
//			aitasks[0].addTask(33, new EntityAILeapAtTarget(this, 0.3F));
//			// Mutexの影響しない特殊行動
//			aitasks[0].addTask(40, aiCloseDoor);
//			aitasks[0].addTask(41, aiOpenDoor);
//			aitasks[0].addTask(42, aiRestrictRain);
//			// 首の動き単独
//			aitasks[0].addTask(51, aiWatchClosest);
//			aitasks[0].addTask(52, new EntityAILookIdle(this));
//
//			// ターゲット
//			aitasks[1] = new EntityAITasks(entity.worldObj != null && entity.worldObj.theProfiler != null ? entity.worldObj.theProfiler :null);

		}
		return aitasks;
	}

	@Override
	public KIND_WORK_CANCEL isFinishWork() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public Class<? extends EntityERMBase> getTarget() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public void excute(EntityERMBase entity) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void start(EntityERMBase entity) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void end(EntityERMBase entity) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void damege(DamageSource par1DamageSource, int par2) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void fall() {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void kill() {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void entityCollision(Entity par1Entity) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void inPortal() {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void inWeb() {
		// TODO 自動生成されたメソッド・スタブ

	}

}
