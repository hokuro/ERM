package basashi.erm.entity.status;

import basashi.erm.entity.EntityERMBase;
import basashi.erm.util.Values;
import basashi.erm.util.Values.KIND_COUNTER;

public class EntityERMStatusFood extends EntityERMStatus {

	public EntityERMStatusFood(float max, float min, float value) {
		super(max, min, value);
	}

	@Override
	public void onUpdate(EntityERMBase entity, EntityERMCounter counter) {
		// 空腹減少
        if (counter.onFire(KIND_COUNTER.COUNTER_FOOD_REDUCE, 24000, true)){
        	this.addStatus(-1.0F);
        }

		// 体力回復
        if (statusValue > 3.0F && entity.shudHeal())
        {
        	// 空腹状態ではない場合、5tic ごとにHP回復
            if (counter.onFire(KIND_COUNTER.COUNTER_FOOD_HELTHHEAL, 5, true))
            {
                entity.heal(1.0F);
            }
        }
        else
        {
        	counter.clearCounter(Values.KIND_COUNTER.COUNTER_FOOD_HELTHHEAL);
        }

	}
}
