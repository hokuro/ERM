package basashi.erm.entity.setting;

import java.io.File;
import java.util.List;

import basashi.erm.entity.EntityERMBase;
import basashi.erm.resource.ERMResourceManager;
import basashi.erm.resource.object.ResourceTag;
import basashi.erm.resource.sounds.SoundMap;

public class EntitySoundSetting {

	// 音声設定ファイル
	private String soundFile;
	// 音声設定
	protected SoundMap soundSetting;
	// 更新されているか
	private boolean isUpdate;

	/********************************************************************************/
	/** 初期化                                                                                                                                                                                        **/
	/********************************************************************************/
	/**
	 * コンストラクタ
	 * @param name
	 */
	public EntitySoundSetting(EntityERMBase entity, String name){
		soundFile = name==null?"":name;
		soundSetting = new SoundMap();
		isUpdate = true;
	}

	/********************************************************************************/
	/** ファイル                                                                                                                                                                                          **/
	/********************************************************************************/
	/**
	 * サウンドファイル名を取得する
	 * @return
	 */
	public String SoundFile(){
		return soundFile;
	}

	/**
	 * サウンドファイル名を設定する
	 * @param name
	 */
	public void SoundFile(String name){
		if (!soundFile.equals(name)){isUpdate = true;}
		soundFile = name==null?"":name;
	}

	/********************************************************************************/
	/** リソースの設定                                                                                                                                                                           **/
	/********************************************************************************/
	/**
	 * 音声設定を設定する
	 * @param tag
	 * @param files
	 */
	public void setSoundMap(ResourceTag tag, List<File> files){
		if (soundSetting == null){
			soundSetting = new SoundMap();
		}
		soundSetting.updateSoundMap(tag,files);
	}

	/**
	 * 音声設定を取り出す
	 * @return
	 */
	public SoundMap getSoundMap(){
		return soundSetting;
	}

	/********************************************************************************/
	/** リソースの更新                                                                                                                                                                           **/
	/********************************************************************************/
	/**
	 * 音声設定ファイルの内容を展開する
	 * @param entity
	 */
	public void loadSound(EntityERMBase entity){
		ERMResourceManager.instance().loadSoundSetting(entity);
		isUpdate = false;
	}

	/**
	 * 音声設定ファイルの内容を更新する
	 * @param entity
	 */
	public void saveSound(EntityERMBase entity){
		ERMResourceManager.instance().saveSoundSetting(entity);
	}

	/**
	 * 音声設定が更新されているかどうか
	 * @return
	 */
	public boolean isUpdate(){
		return isUpdate;
	}

	/********************************************************************************/
	/** リソースの使用                                                                                                                                                                            **/
	/********************************************************************************/
	/**
	 * 指定したサウンドを取得する
	 * @param tag
	 * @return
	 */
	public File getSound(ResourceTag tag){
		if(soundSetting == null){return null;}
		return soundSetting.getSound(tag);
	}

	/**
	 * 指定したサウンドを取得する
	 * @param tag
	 * @param idx
	 * @return
	 */
	public File getSound(ResourceTag tag, int idx){
		if(soundSetting == null){return null;}
		return soundSetting.getSound(tag, idx);
	}

	/**
	 * 指定したサウンドを取得する
	 * @param tag
	 * @param name
	 * @return
	 */
	public File getSound(ResourceTag tag, String name){
		if(soundSetting == null){return null;}
		return soundSetting.getSound(tag, name);
	}
}
