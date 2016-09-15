package basashi.erm.resource.sounds;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import basashi.erm.core.log.ModLog;
import basashi.erm.resource.object.ResourceTag;
import basashi.erm.util.Util;

public class SoundMap {

	// サウンドマップ
	private Map<ResourceTag,List<File>> soundmap;


	/**
	 * コンストラクタ
	 */
	public SoundMap(){
		soundmap = new HashMap<ResourceTag,List<File>>();
	}

	/**
	 * サウンドマップを更新する
	 * @param map
	 */
	public void updateSoundMap(Map<ResourceTag,List<File>> map){
		soundmap.clear();
		// 有効なリソースのみ登録
		for (ResourceTag keytag : map.keySet()){
			soundmap.put(keytag, checkFile(map.get(keytag)));
		}
	}

	/**
	 * 特定のタグのサウンドマップを更新する
	 * @param tag
	 * @param files
	 */
	public void updateSoundMap(ResourceTag tag, List<File> files){
		soundmap.get(tag).clear();
		soundmap.get(tag).addAll(checkFile(files));
	}

	/**
	 * 指定したタグのサウンドをランダムに取得する
	 * @param tag
	 * @return
	 */
	public File getSound(ResourceTag tag){
		File ret = null;
		if (soundmap.containsKey(tag)){
			List<File> files = checkFile(soundmap.get(tag));
			if (files.size() == soundmap.get(tag).size()){
				// 有効なファイルに変更がある場合、リストを更新する
				soundmap.get(tag).clear();
				soundmap.get(tag).addAll(files);
				ModLog.log().info("find delete sound file. update sound map.");
			}
			int idx = Util.random(files.size());
			ret = files.get(idx);
		}
		return ret;
	}

	/**
	 * 指定したタグと番号のサウンドを取得する
	 * @param tag
	 * @param idx
	 * @return
	 */
	public File getSound(ResourceTag tag, int idx){
		File ret = null;
		List<File> files = checkFile(soundmap.get(tag));
		if (files.size() <= idx){
			ret = files.get(idx);
		}
		return ret;
	}

	/**
	 * 指定したタグと名前のサウンドを取得する
	 * @param tag
	 * @param file
	 * @return
	 */
	public File getSound(ResourceTag tag, String file){
		File ret = null;
		List<File> files = checkFile(soundmap.get(tag));
		for(File fl : files){
			if (fl.getName().equals(file)){
				ret = fl;
				break;
			}
		}
		return ret;
	}

	/**
	 * 指定したタグのすべてのサウンドを取得する
	 * @param tag
	 * @return
	 */
	public List<File> getAllSound(ResourceTag tag){
		return soundmap.get(tag);
	}

	/**
	 * 設定されているファイルリストから、有効なファイルのみのリストを作成
	 * @param files
	 * @return
	 */
	private List<File> checkFile(List<File> files){
		int size = files.size();
		List<File> ret = new ArrayList<File>();
		for (int i =0; i < size; i++){
			if ( files.get(i).isFile() && files.get(i).exists() ){
				ret.add(files.get(i));
			}
		}
		return ret;
	}


}
