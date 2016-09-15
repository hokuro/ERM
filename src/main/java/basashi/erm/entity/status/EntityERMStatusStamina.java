package basashi.erm.entity.status;

import basashi.erm.core.ModCommon;
import basashi.erm.core.log.ModLog;
import basashi.erm.entity.EntityERMBase;
import basashi.erm.util.Values;

public class EntityERMStatusStamina extends EntityERMStatus {
	public EntityERMStatusStamina(float max, float min, float value) {
		super(max, min, value);
	}

	@Override
	public void onUpdate(EntityERMBase entity, EntityERMCounter counter) {
		// スタミナ回復
		long ticks = counter.getCounter(Values.KIND_COUNTER.COUNTER_STAMINA);
		// 空腹補正
		float food = entity.getFoodWieght();
		int reflesh = entity.getSpdRefleshStamina();
		int tired = entity.getSpdTiredStamina();

		if(ModCommon.isDebug)ModLog.log().debug("D LOG cnt " + ticks + " Tired " + tired + " Reflesh " +reflesh);
		if ((ticks%tired) == 0){
			if(entity.settingAI().doWork()){
				// 作業中
				// 作業に必要なスタミナが減少
				addStatus(- 1 * Math.abs(entity.settingAI().reduceStamina()) * food);
			}
		}
		if ((ticks%reflesh) == 0){
			if (entity.settingAI().doWork()){
				// 作業中
				// スタミナの回復は通常状態の半分
				addStatus((entity.MaxStamina()/20)/food);
			}else{
				// 前回回復から一定時間経過で回復(最大値の1/10)
				addStatus((entity.MaxStamina()/10)/food);
			}
		}
		// 疲労加算、スタミナ回復の両方が行われるタイミングでカウントリセット
		if (ticks == (reflesh * tired)){counter.clearCounter(Values.KIND_COUNTER.COUNTER_STAMINA);}
	}

}
