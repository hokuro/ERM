package basashi.erm.entity.logic;

import java.util.ArrayList;
import java.util.List;

import basashi.erm.ai.workmode.IERMAIBase;
import basashi.erm.core.ModCommon;
import basashi.erm.core.log.ModLog;
import basashi.erm.util.Util;
import net.minecraft.nbt.NBTTagCompound;

/***********************************************************************************/
/** お手伝い                                                                                                                                                                                               **/
/***********************************************************************************/
public class IntarestWorkList{
	private List<AIIntarest> list;
	private int maxsize;
	private float ibase;
	private float imag;

	/**
	 * コンストラクタ
	 * @param maxMemory 記憶数
	 * @param unintarest 興味減少値
	 */
	public IntarestWorkList(int maxMemory, float intarest, float unintarest ){
		list = new ArrayList<AIIntarest>();
		for (int i = 0; i < maxMemory; i++){
			list.add(null);
		}
		maxsize = maxMemory;
		ibase = intarest;
		imag = unintarest;

		if(ModCommon.isDebug)ModLog.log().debug("D LOG IntarestList maxsize : " + maxsize);
		if(ModCommon.isDebug)ModLog.log().debug("D LOG IntarestList ibase : " + ibase);
		if(ModCommon.isDebug)ModLog.log().debug("D LOG IntarestList imag : " + imag);
	}

	/**
	 * お手伝いを拒むかどうか判定する
	 * @param work お手伝い
	 * @param maxint 興味の最大値
	 * @param rebelious お手伝いを拒む確率
	 * @return お手伝いを実行できるかどうｋ
	 */
	public boolean checkCanWork(IERMAIBase work, float maxint, float rebelious){
		boolean ret = true;
		boolean find = false;

		// 基本減少 最大値 /10 減少
		float reduce = maxint/10;
		if(ModCommon.isDebug)ModLog.log().debug("D LOG checkCanWork : " + work.AIName() + "(" + maxint + ")");
		for (int i = 0; i < maxsize; i++){
			// 同一のお手伝いか判定
			if (list.get(i).isSame(work)){
				find = true;
				AIIntarest w = list.get(i);
	    		if(ModCommon.isDebug)ModLog.log().debug("D LOG checkCanWork find ssame");

				// お手伝いに興味があるか判定
				// 直前に拒んでいるお手伝いであるかまたは
				// 興味がベース値の1/4以下の場合、確率でお手伝いを拒否
				// 興味が薄いほど拒む確率が高くなる
				if (w.isRevelious() ||
					((ibase/4)>=w.getIntarest() &&
						Util.random(100)<(rebelious + (ibase/4)+w.getIntarest()))){
					if (ModCommon.isDebug){
						ModLog.log().debug("D LOG : " + ibase + "/4 | " + w.getIntarest());
						ModLog.log().debug("D LOG : " + Util.randomPrevious() + " | " + rebelious + " + (" + ibase + "/4) + " + w.getIntarest());
					}
					w.Revelious();
					ret = false;
				}else{
					// お手伝いを実行する
					// お手伝いで減少する興味値を計算
					// ベース値/10 * 興味減少割合 * 仕事の近さ
					reduce += (imag * ((maxsize - i)/maxsize));
		    		if(ModCommon.isDebug)ModLog.log().debug("D LOG reduce intarest : " + reduce + " = " + maxint/10 + "(" + imag + "*" + "((" + maxsize + " - " + i + ")/" + maxsize+")") ;

					// 直前のお手伝いと一致しない場合、新しいお手伝いを記憶の先頭に持ってくる
    				if (i != 0){
    					list.remove(i);
    					list.add(0, w);
    				}
				}
				break;
			}
		}

		// 今までにやったことのないお手伝いの場合
		if (!find){
			// 一番古いお手伝いを忘れるて、新しいお手伝いを記憶する
			// 記憶するお手伝いの興味値は最大興味-減少値
			this.list.remove(this.maxsize-1);
			this.list.add(0,new AIIntarest(work,maxint - reduce));
    		if(ModCommon.isDebug)ModLog.log().debug("D LOG checkCanWork find fist");
		}

		// お手伝いを実行する場合、選択されなかったお手伝いの興味値を回復する
		if ( ret == false){
			for (int i = 1; i < this.maxsize; i++){
				reduce = (maxint/10) + (imag * i);
				list.get(i).setIntarest(reduce,maxint);

	    		if(ModCommon.isDebug)ModLog.log().debug("D LOG heal intarest : " + list.get(i).workName + "(" + reduce + ")");
			}
		}

		if(ModCommon.isDebug)ModLog.log().debug("D LOG Otetudai : " + (ret?"suru":"shinai"));
		return ret;
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
		tagCompound.setString(key, savetoNTB());
	}


	/**
	 * NBTに書き込むデータを作成する
	 * @return
	 */
	private String savetoNTB(){
		StringBuilder saveStr = new StringBuilder();
		for ( int i = 0; i < maxsize; i++ ){
			if(list.get(i) != null){
				saveStr.append("[").append(list.get(i).toString()).append("]");
				if(ModCommon.isDebug)ModLog.log().debug("D LOG savetoNTB work [" + i + "] : " + list.get(i).toString());
			}else{break;}
		}
		return saveStr.toString();
	}

	/**
	 * NBTから取り出したデータを設定する
	 * @param data
	 */
	private void readFromNBT(String data){
		if (savetoNTB().equals(data)){return;}
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

	/*************************************************************************************/
	/** デバッグ                                                                        **/
	/*************************************************************************************/
	public String debugLog(){
		StringBuilder deb = new StringBuilder();
		int cnt = 0;
		for ( int i = 0; i< maxsize; i++){
			if (list.get(i) != null){
				deb.append(list.get(i).toString()).append(Util.ReturnCode());
				cnt++;
			}
		}
		deb.append("Count Memory         :" + Integer.toString(cnt)).append(Util.ReturnCode());
		return deb.toString();
	}


	public static class AIIntarest{
		// お手伝い内容
		private String workName;
		// お手伝いに対する興味
		private float workIntarest;
		// お手伝いを拒否したかどうか
		private boolean isRevelious;

		/**
		 * コンストラクタ
		 * @param work お手伝い
		 * @param intarest 初期興味
		 */
		public AIIntarest(IERMAIBase work, float intarest){
			this.workName = work.AIName();
			this.workIntarest = intarest;
			this.isRevelious = false;
			if(ModCommon.isDebug)ModLog.log().debug("D LOG AIIntarest workName : " + workName);
			if(ModCommon.isDebug)ModLog.log().debug("D LOG AIIntarest workIntarest : " + Float.toString(workIntarest));
		}

		/**
		 * コンストラクタ
		 * @param data
		 */
		public AIIntarest(String data){
			String[] sep = data.split("\t");
			workName = sep[0];
			workIntarest = Float.parseFloat(sep[1]);
			isRevelious = Boolean.parseBoolean(sep[2]);
			if(ModCommon.isDebug)ModLog.log().debug("D LOG AIIntarest workName : " + workName);
			if(ModCommon.isDebug)ModLog.log().debug("D LOG AIIntarest workName : " + Float.toString(workIntarest));
			if(ModCommon.isDebug)ModLog.log().debug("D LOG AIIntarest isRevelious : " + Boolean.toString(isRevelious));
		}

		/**
		 * 現在の興味値を取得
		 * @return
		 */
		public float getIntarest(){
			return this.workIntarest;
		}

		/**
		 * 興味値を加算
		 */
		public void setIntarest(float intarest, float max){
			this.workIntarest += intarest;
			if (workIntarest < 0){workIntarest = 0;}
			else if(workIntarest > max){workIntarest = max;}
			isRevelious = false;
			if(ModCommon.isDebug)ModLog.log().debug("D LOG setIntarest intarest : " + Float.toString(workIntarest) +
					"(" + Float.toString(intarest) + ")");
		}

		/**
		 * 直前に拒否したお手伝いかどうかを取得する
		 */
		public boolean isRevelious(){
			return isRevelious;
		}

		/**
		 * お手伝いを拒否した
		 */
		public void Revelious(){
			this.isRevelious = true;
		}

		/**
		 * 同一の作業かどうか確認
		 * @param work 確認する作業
		 * @return 一致するかどうか
		 */
		public boolean isSame(IERMAIBase work){
			boolean ret = false;
			if (work != null && this.workName.equals(work.AIName())){
    			if(ModCommon.isDebug)ModLog.log().debug("D LOG isSame true");
				ret = true;
			}
			return ret;
		}

		public String toString(){
			StringBuilder str = new StringBuilder();
			str.append(workName).append("\t");
			str.append(workIntarest).append("\t");
			str.append(isRevelious).append("\t");
			if(ModCommon.isDebug)ModLog.log().debug("D LOG AIIntarest toString : " + str.toString());
			return str.toString();
		}
	}
}