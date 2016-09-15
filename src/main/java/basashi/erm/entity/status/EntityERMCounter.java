package basashi.erm.entity.status;

import java.util.HashMap;
import java.util.Map;

import basashi.erm.core.ModCommon;
import basashi.erm.core.log.ModLog;
import basashi.erm.util.Util;
import basashi.erm.util.Values;
import basashi.erm.util.Values.KIND_COUNTER;
import net.minecraft.nbt.NBTTagCompound;

public class EntityERMCounter {

	// カウンタがロード済みかどうか
	private boolean loadCounter;

	// カウンタ
	private Map<Values.KIND_COUNTER,Long> ticks;

	/**
	 * コンストラクタ
	 */
	public EntityERMCounter(){
		ticks = new HashMap<Values.KIND_COUNTER,Long>();
		for(Values.KIND_COUNTER cnt : Values.KIND_COUNTER.values()){
			ticks.put(cnt, cnt.cntDefault);
		}
		loadCounter = false;
	}


	/**
	 * カウンタを進める
	 */
	public void updateTicks(){
		for(Values.KIND_COUNTER cnt : Values.KIND_COUNTER.values()){
			if(cnt.isUpCounter){
				ticks.put(cnt, ticks.get(cnt)+1);
			}else{
				ticks.put(cnt, ticks.get(cnt)-1);
			}
		}
	}

	/**
	 * カウンタを取り出す
	 * @param cnt
	 * @return
	 */
	public long getCounter(Values.KIND_COUNTER cnt){
		if(ModCommon.isDebug){ModLog.log().debug("-D Log get counter " + cnt.toString() + "  value = " + ticks.get(cnt));}
		return ticks.get(cnt);
	}

	/**
	 * カウンタをクリアする
	 * @param cnt
	 */
	public void clearCounter(Values.KIND_COUNTER cnt){
		ticks.put(cnt, cnt.cntDefault);
	}

	/**
	 * カウンタをクリアする
	 * @param cnt
	 */
	public void clearCounter(Values.KIND_COUNTER cnt, long value){
		if(cnt.isUpCounter){
			ticks.put(cnt, cnt.cntDefault);
			if(ModCommon.isDebug){ModLog.log().debug("-D Log is UpCounter " + cnt.toString() + "  value = " + 0);}
		}else{
			ticks.put(cnt, value);
			if(ModCommon.isDebug){ModLog.log().debug("-D Log is UpCounter " + cnt.toString() + "  value = " + value);}
		}
	}

	/**
	 * タイマーが発火しているか確認する
	 * @param kind
	 * @param value
	 * @param autoClear
	 * @return
	 */
	public boolean onFire(KIND_COUNTER kind, long value, boolean autoClear){
		boolean ret = false;
		if (value >= getCounter(kind)){
			ret = true;
			if (autoClear){
				clearCounter(kind);
			}
		}
		return ret;
	}



	/**
	 * NBT読み込み
	 * @param compound
	 */
	public void readNBT(NBTTagCompound compound, String key){
		String counter = compound.getString(key);
		if(!loadCounter){
			String[] count = counter.split("\t");
			int i = 0;
			for(Values.KIND_COUNTER cnt : Values.KIND_COUNTER.values()){
				ticks.put(cnt, Long.parseLong(count[i]));
				i++;
			}
			loadCounter = true;
		}
	}

	/**
	 * NBT書き込み
	 * @param compound
	 */
	public void writeNBT(NBTTagCompound compound, String key){
		StringBuilder wirte = new StringBuilder();
		for(Values.KIND_COUNTER cnt : Values.KIND_COUNTER.values()){
			wirte.append(ticks.get(cnt)).append("\t");
		}
		compound.setString(key, wirte.toString());
	}

    /*****************************************************************************************/
    /** デバッグ                                                                            **/
    /*****************************************************************************************/
    public String debugCounter(){
    	StringBuilder deb = new StringBuilder();
    	deb.append("==============================================================").append(Util.ReturnCode());
    	deb.append("== Counter                                                  ==").append(Util.ReturnCode());
    	for (KIND_COUNTER kind : KIND_COUNTER.values()){
    		Long cnt = ticks.get(kind);
    		deb.append(kind.toString()).append(":").append(Long.toString(cnt)).append(Util.ReturnCode());
    	}
    	deb.append("==============================================================").append(Util.ReturnCode());
    	return deb.toString();
    }
}
