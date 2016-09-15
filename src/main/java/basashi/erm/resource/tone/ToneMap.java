package basashi.erm.resource.tone;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import basashi.erm.resource.object.ResourceTag;
import basashi.erm.util.Util;

public class ToneMap {
	/*
	 * トーンリスト
	 */
	private Map<ResourceTag,List<String>> toneMap = null;

	/**
	 * デフォルトトーンマップ
	 */
	private Map<ResourceTag,List<String>> toneMapDefault = null;

	/**
	 * コンストラクタ
	 */
	public ToneMap(Map<ResourceTag,List<String>> def){
		// カスタムテキストにデフォルトのテキストを設定
		toneMap = new HashMap<ResourceTag,List<String>>();
		toneMap.putAll(def);
		// デフォルトのテキストを設定
		toneMapDefault = new HashMap<ResourceTag,List<String>>();
		toneMapDefault.putAll(def);
	}

	/**
	 * トーンマップの内容を更新する
	 * @param path
	 * @return
	 */
	public void updateTone(Map<ResourceTag,List<String>> newTone){
		// 現在のトーンをクリアして新しいトーンを設定
		toneMap.clear();
		toneMap.putAll(newTone);
	}

	/**
	 * 会話テキストを取り出す
	 * @param key 取り出すテキストの種類
	 * @return テキスト
	 */
	public String getText(ResourceTag key){
		if (toneMap.containsKey(key)){
			// カスタム会話テキスト
			List<String> tones = toneMap.get(key);
			int idx = Util.random(tones.size());
			return tones.get(idx);
		}else if (toneMapDefault.containsKey(key)){
			// デフォルトテキスト
			List<String> tones = toneMapDefault.get(key);
			int idx = Util.random(tones.size());
			return tones.get(idx);
		}
		// カスタムにもデフォルトにもないテキストの種類
		return "";
	}
}
