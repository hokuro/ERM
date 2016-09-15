package basashi.erm.resource.ai;

import java.util.ArrayList;
import java.util.List;

import basashi.erm.ai.workmode.IERMAIBase;
import basashi.erm.entity.EntityERMBase;

public class AIMap {

	// AI リスト
	private List<IERMAIBase> ailist;
	// リストに登録するAIのターゲットI
	private Class< ? extends EntityERMBase> targetClass;

	/**
	 * リストと、ターゲットのモブを初期化する
	 * @param tag
	 */
	public AIMap(Class<? extends EntityERMBase> tag){
		ailist = new ArrayList<IERMAIBase>();
		if (tag != null){
			targetClass = tag;
		}else{
			throw new NullPointerException("target entity is null");
		}
	}

	/**
	 * AIリストを取り出す
	 * @return
	 */
	public List<IERMAIBase> getAIList(){
		return ailist;
	}

	/**
	 * AIリストを設定する
	 * @param values
	 */
	public void setAIList(List<IERMAIBase> values){
		for(IERMAIBase ai : values){
			if (ai.getTarget() == EntityERMBase.class ||
					ai.getTarget() == targetClass){
				ailist.add(ai);
			}
		}
	}
}
