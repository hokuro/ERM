package basashi.erm.entity.logic;

import java.util.ArrayList;
import java.util.List;

import basashi.erm.core.ModCommon;
import basashi.erm.core.log.ModLog;
import basashi.erm.item.ItemCandy;
import basashi.erm.util.Util;
import basashi.erm.util.Values;
import net.minecraft.nbt.NBTTagCompound;

public class IntarestCandyList {
	private List<AIIntarest> list;
	private int maxsize;
	private float ibase;
	private float imag;

	/**
	 * コンストラクタ
	 * @param maxMemory 記憶数
	 * @param unintarest 興味減少値
	 */
	public IntarestCandyList(int maxMemory, float intarest, float unintarest ){
		list = new ArrayList<AIIntarest>();
		for (int i = 0; i < maxMemory; i++){
			list.add(null);
		}
		maxsize = maxMemory;
		ibase = intarest;
		imag = unintarest;

		if(ModCommon.isDebug)ModLog.log().debug("D LOG IntarestCandyList maxsize : " + maxsize);
		if(ModCommon.isDebug)ModLog.log().debug("D LOG IntarestCandyList ibase : " + ibase);
		if(ModCommon.isDebug)ModLog.log().debug("D LOG IntarestCandyList imag : " + imag);
	}

	/**
	 * キャンディの飽き補正を取得
	 * @param candyName
	 * @param favo
	 * @return
	 */
	public float candyIntarest(String candyName, Values.KIND_CANDYTAST favo){
		float ret = 1.0F;
		boolean find = false;

		float favoWeight = 1.0F;
		switch(favo){
		case YUMMY:
			favoWeight = 0.8F;
			break;
		case GOOD:
			favoWeight = 1.0F;
			break;
		case BAD:
			favoWeight = 0.4F;
			break;
		default:
			favoWeight = 1.5F;
			break;
		}

		if(ModCommon.isDebug)ModLog.log().debug("D LOG checkCandy : " + candyName);
		for (int i = 0; i < maxsize; i++){
			if (list.get(i) != null){
				// 同一のキャンディか判定
				if (list.get(i).isSame(candyName)){
					find = true;
					if (!(candyName == ItemCandy.CANDYNAME_NORMAL || favo == Values.KIND_CANDYTAST.LOVE)){
						// ノーマルキャンディと大好きなキャンディ以外の場合飽きの処理
						// 現在の興味を取得
						float w = list.get(i).getIntarest();
			    		if(ModCommon.isDebug)ModLog.log().debug("D LOG checkCandy find ssame");
			    		// 現在の興味の1/10×好み補正を飽き補正として返す(最大値は1.0)
			    		ret = Math.max(w/ibase * favoWeight, 1.0F);
			    		if(ModCommon.isDebug)ModLog.log().debug("D LOG checkCandy : " + ret);

			    		// 現在の興味をベースの1/10 × 飽き補正分減らす
						list.get(i).setIntarest(-1 * ibase/10,ibase);
					}else{
						ret = favoWeight;
					}
		    		// 記憶の位置を入れ替える
		    		AIIntarest inst = list.get(i);
		    		list.remove(i);
		    		list.add(0,inst);
				}else{
					// 現在の興味をベースの1/10分増やす
					list.get(i).setIntarest(ibase/10,ibase);
				}
			}
		}
		// 記憶にないキャンディなら記憶
		if (!find){
			list.add(0, new AIIntarest(candyName,ibase-ibase/10));
			list.remove(maxsize);
		}
		return ret;
	}


	/**
	 * 最近食べたキャンディを取り出す
	 * @return
	 */
	public AIIntarest getRecentCandy(){
		return list.get(0);
	}



	/**
	 * NBTから読み込む
	 * @param tagCompound
	 * @param key
	 */
	public void readNBT(NBTTagCompound tagCompound, String key){
		readFromNBT(tagCompound.getString(key));
	}

	/**
	 * NBTへ書き込む
	 * @param tagCompound
	 * @param key
	 */
	public void writeNBT(NBTTagCompound tagCompound, String key){
		tagCompound.setString(key, savetoNBT());
	}


	private String savetoNBT(){
		StringBuilder saveStr = new StringBuilder();
		for ( int i = 0; i < maxsize; i++ ){
			if(list.get(i) != null){
				saveStr.append("[").append(list.get(i).toString()).append("]");
				if(ModCommon.isDebug)ModLog.log().debug("D LOG savetoNTB work [" + i + "] : " + list.get(i).toString());
			}else{break;}
		}
		return saveStr.toString();
	}

	private void readFromNBT(String data){
		if(savetoNBT().equals(data)){return;}
		char[] cdata = data.toCharArray();
		int istart = 0;
		int tab = 0;
		list.clear();
		for (int i = 0; i < cdata.length; i++){
			if (cdata[i] == '\t'){
				tab++;
			}else if(cdata[i] == ']'){
				if (tab == 2){
					String sub = data.substring(istart+1,i-1);
					istart = i+1;
					tab = 0;
					if(ModCommon.isDebug)ModLog.log().debug("D LOG readNTB work " + sub);
					list.add(new AIIntarest(sub));
				}
			}
		}
		int lpmax = this.maxsize - list.size();
		for ( int i = 0; i < lpmax; i++){
			list.add(null);
		}
	}

	public String debugLog(){
		StringBuilder deb = new StringBuilder();
		int cnt = 0;
    	deb.append("==============================================================").append(Util.ReturnCode());
    	deb.append("== Candy                                                    ==").append(Util.ReturnCode());
		for ( int i = 0; i< maxsize; i++){
			if (list.get(i) != null){
				deb.append(list.get(i).toString()).append(Util.ReturnCode());
				cnt ++;
			}
		}
		deb.append("Count Memory         :" + Integer.toString(cnt)).append(Util.ReturnCode());
    	deb.append("==============================================================").append(Util.ReturnCode());
		return deb.toString();
	}


	public static class AIIntarest{
		// キャンディ名
		public final String candyName;
		// キャンディに対する興味
		private float candyIntarest;

		/**
		 * コンストラクタ
		 * @param work お手伝い
		 * @param intarest 初期興味
		 */
		public AIIntarest(String name, float intarest){
			this.candyName = name;
			this.candyIntarest = intarest;
			if(ModCommon.isDebug)ModLog.log().debug("D LOG AIIntarest CandyName : " + this.candyName);
			if(ModCommon.isDebug)ModLog.log().debug("D LOG AIIntarest CandyIntarest : " + Float.toString(this.candyIntarest));
		}

		/**
		 * コンストラクタ
		 * @param data
		 */
		public AIIntarest(String data){
			String[] sep = data.split("\t");
			candyName = sep[0];
			candyIntarest = Float.parseFloat(sep[1]);
			if(ModCommon.isDebug)ModLog.log().debug("D LOG AIIntarest CandyName : " + candyName);
			if(ModCommon.isDebug)ModLog.log().debug("D LOG AIIntarest CandyIntarest : " + Float.toString(candyIntarest));
		}

		/**
		 * 現在の興味値を取得
		 * @return
		 */
		public float getIntarest(){
			return this.candyIntarest;
		}

		/**
		 * 興味値を加算
		 */
		public void setIntarest(float intarest, float max){
			this.candyIntarest += intarest;
			if (candyIntarest < 0){candyIntarest = 0;}
			else if(candyIntarest > max){candyIntarest = max;}
			if(ModCommon.isDebug)ModLog.log().debug("D LOG setCandyIntarest intarest : " + Float.toString(candyIntarest) +
					"(" + Float.toString(intarest) + ")");
		}

		/**
		 * 同じキャンディか確認
		 * @param candy 確認するキャンディ
		 * @return 一致するかどうか
		 */
		public boolean isSame(String candy){
			boolean ret = false;
			if (this.candyName.equals(candy)){
    			if(ModCommon.isDebug)ModLog.log().debug("D LOG isSame true");
				ret = true;
			}
			return ret;
		}

		public String toString(){
			StringBuilder str = new StringBuilder();
			str.append(candyName).append("\t");
			str.append(candyIntarest).append("\t");
			if(ModCommon.isDebug)ModLog.log().debug("D LOG AIIntarest toString : " + str.toString());
			return str.toString();
		}
	}
}
