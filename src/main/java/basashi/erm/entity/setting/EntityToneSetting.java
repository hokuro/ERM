package basashi.erm.entity.setting;

import java.util.List;
import java.util.Map;

import basashi.erm.entity.EntityERMBase;
import basashi.erm.resource.ERMResourceManager;
import basashi.erm.resource.object.ResourceTag;
import basashi.erm.resource.tone.ToneMap;

public class EntityToneSetting {
	// トーンファイル
	private String toneFile;
	// ターゲット
	private EntityERMBase target;
	// トーン設定
	private ToneMap toneSetting;
	// 更新されているか
	private boolean isUpdate;

	/********************************************************************************/
	/** 初期化                                                                                                                                                                                        **/
	/********************************************************************************/
	/**
	 * コンストラクタ
	 * @param tone
	 */
	public EntityToneSetting(EntityERMBase entity,String name,Map<ResourceTag, List<String>> tone){
		toneFile = name;
		toneSetting = new ToneMap(tone);
		isUpdate = true;
		target = entity;
	}

	/********************************************************************************/
	/** ファイル                                                                                                                                                                                         **/
	/********************************************************************************/
	/**
	 * トーンファイル名をと取得する
	 * @return
	 */
	public String ToneFile(){
		return toneFile;
	}

	/**
	 * トーンファイル名を設定する
	 * @param name
	 */
	public void ToneFile(String name){
		toneFile = name;
	}

	/********************************************************************************/
	/** リソース更新                                                                                                                                                                               **/
	/********************************************************************************/
	/**
	 * トーンマップを更新する
	 * @param tone
	 */
	public void setToneMap(Map<ResourceTag, List<String>> tone){
		toneSetting.updateTone(tone);
	}

	/**
	 * トーン設定を更新する
	 * @param entity
	 */
	public void updateTone(EntityERMBase entity){
		ERMResourceManager.instance().updateTone(entity);
		isUpdate = false;
	}

	/**
	 * 更新する必要があるかどうか確認する
	 * @return
	 */
	public boolean isUpdate(){
		return isUpdate;
	}

	/********************************************************************************/
	/** リソースの利用                                                                                                                                                                            **/
	/********************************************************************************/
	/**
	 * 指定したキーのセリフを取り出す
	 * @param key
	 * @return
	 */
	public String getText(ResourceTag key){
		return toneSetting.getText(key);
	}

}
