package basashi.erm.entity.status;

import basashi.erm.entity.EntityERMBase;
import net.minecraft.nbt.NBTTagCompound;

public class  EntityERMStatus {

	protected float statusValueMax;
	protected float statusValueMin;
	protected float statusValue;

	public EntityERMStatus(float max, float min, float value){
		statusValueMax = max;
		statusValueMin = min;
		statusValue = value;
	}

	/**
	 * ステータスを加算する
	 * @param addValue
	 */
	public void addStatus(float addValue){
		setStatus(statusValue+addValue);
	}

	/**
	 * ステータスを設定する
	 * @param value
	 * @return
	 */
	public void setStatus(float value){
		statusValue = value;
		if (statusValueMin > statusValue){statusValue = statusValueMin;}
		if (statusValueMax < statusValue){statusValue = statusValueMax;}
	}

	/**
	 * ステータスを取り出す
	 * @return
	 */
	public float getStatus(){
		return statusValue;
	}

	/**
	 * 最大値を取り出す
	 * @return
	 */
	public float getMax() {
		return this.statusValueMax;
	}

	/**
	 * 最小値を取り出す
	 * @return
	 */
	public float getMin(){
		return this.statusValueMin;
	}

	/**
	 * ステータスは最大か?
	 * @return
	 */
	public boolean isMax(){
		return statusValue==statusValueMax;
	}

	/**
	 * ステータスは最小か?
	 * @return
	 */
	public boolean isMin(){
		return statusValue==statusValueMin;
	}

	/**
	 * アップデート処理
	 * @param entity
	 * @param counter
	 */
	public void onUpdate(EntityERMBase entity, EntityERMCounter counter){

	}

	/**
	 * NBT読み込み
	 * @param compound
	 */
	public void readNBT(NBTTagCompound compound, String key) {
		statusValue = compound.getFloat(key);
	}

	/**
	 * NBT書き込み
	 * @param compound
	 */
	public void writeNBT(NBTTagCompound compound, String key){
		compound.setFloat(key, statusValue);
	}


}
