package basashi.erm.entity.status;

import basashi.erm.entity.EntityERMBase;
import basashi.erm.util.Values;
import basashi.erm.util.Values.KIND_COUNTER;

public class EntityERMStatusLove extends EntityERMStatus {
	public EntityERMStatusLove(float max, float min, float value) {
		super(max, min, value);
	}

	@Override
	public void onUpdate(EntityERMBase entity, EntityERMCounter counter) {
		long talk = counter.getCounter(Values.KIND_COUNTER.COUNTER_LOVEREDUCE_TALK);
		long candy = counter.getCounter(Values.KIND_COUNTER.COUNTER_LOVEREDUCE_CANDY);
		long waite = counter.getCounter(Values.KIND_COUNTER.COUNTER_LOVE_WATETIME);
		if (counter.onFire(KIND_COUNTER.COUNTER_LOVE_WATETIME,24000,true)){
			//TODO: 待機状態が1日以上続くと信頼減少
			// プレイヤーとの距離が離れているほど減少値が大きく(最低0, 最大10);

		}else if (counter.onFire(KIND_COUNTER.COUNTER_LOVEREDUCE_TALK, 24000 * 7, false) ||
				counter.onFire(KIND_COUNTER.COUNTER_LOVEREDUCE_CANDY, 24000 * 7, false)){
			// １週間会話がないか、キャンディをもらえなかった場合信頼減少
			// 現在の値の1/10*不信パラメータ
			setStatus(statusValue/10*entity.getWeightLove());
			// カウンタクリア
			counter.clearCounter(Values.KIND_COUNTER.COUNTER_LOVEREDUCE_TALK);
			counter.clearCounter(Values.KIND_COUNTER.COUNTER_LOVEREDUCE_CANDY);
		}
		if (!entity.settingAI().isWaite()){
			// 待機状態ではない場合、信頼減少カウンタをクリア
			counter.clearCounter(Values.KIND_COUNTER.COUNTER_LOVE_WATETIME);
		}
	}
}
