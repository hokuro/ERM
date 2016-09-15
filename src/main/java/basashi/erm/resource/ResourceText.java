package basashi.erm.resource;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import basashi.erm.core.log.ModLog;
import basashi.erm.entity.EntityERMBase;
import basashi.erm.resource.object.ResourceTag;

public class ResourceText implements IERMResource{
	// トーンディレクトリ名
	public static final String DirName = "text";
	// トーンディレクトリ
	private static File DirTone;
	// トーンファイルリスト
	private List<File> textFiles;

	// txt ファイルかどうか確認するフィルタ
	private static final FileFilter txt = new FileFilter(){
		@Override
		public boolean accept(File pathname){
			try{
				if (pathname.isFile()){
					if (pathname.getName().endsWith(".txt")){
						return true;
					}
				}
			}catch(Exception ex){
				ModLog.log().warn("[Text Find Error] : "+pathname.getName() );
			}
			return false;
		}
	};

	/*************************************************************************/
	/** リソース操作用                                                                                                                                                         */
	/*************************************************************************/
	/**
	 * トーンファイルのリストを返す
	 * @return
	 */
	public List<File> getNameList(){
		return textFiles;
	}

	/**
	 * モブのトーンを変更する
	 * @param entity
	 */
	public void updateTone(EntityERMBase entity){
		File tonePath = new File(DirTone,entity.settingVoice().getTone().ToneFile());
		Map<ResourceTag,List<String>> update = loadToneFile(tonePath);
		if (update!=null){
			entity.settingVoice().getTone().setToneMap(update);
		}else{
			ModLog.log().warn("tone can't update");
		}
	}

	/*************************************************************************/
	/** インタフェース                                                                                                                                                               */
	/*************************************************************************/
	@Override
	public void initResource() {}

	@Override
	public boolean loadResource(File directory) {
		if(this.DirName.equals(directory.getName())){
			if (DirTone == null){DirTone = new File(directory,DirName);}
			// テキストファイルクリア
			this.textFiles.clear();
			// text検索
			searchText(directory);
			return true;
		}
		return false;
	}

	@Override
	public String dirName() {
		return DirName;
	}

	@Override
	public boolean canUseResource() {
		return true;
	}

	/*************************************************************************/
	/** 内部用                                                                                                                                                               */
	/*************************************************************************/
	/**
	 * トーン用リソースディレクトリからトーンファイル一覧を作成する
	 * @param dir
	 * @return
	 */
	private boolean searchText(File dir){
		// txt リスト取得
		File[] texts = dir.listFiles(txt);
		for(File textfile : texts){
			this.textFiles.add(textfile);
		}
		return true;
	}

	/**
	 * トーンファイルをロードする
	 * @param tonefile
	 * @return
	 */
	private Map<ResourceTag,List<String>> loadToneFile(File tonefile){
		Map<ResourceTag,List<String>> ret = new HashMap<ResourceTag,List<String>>();
		List<String> tones;
		try{
			tones = Files.readAllLines(tonefile.toPath());
		}catch(Exception ex){
			ModLog.log().error("Can't read tonefile");
			ex.printStackTrace();
			return null;
		}
		ResourceTag kind =null;
		for(String text : tones){
			if (text.trim().startsWith("//")){
				// コメント
				continue;
			}else if(text.trim().startsWith("%") && text.trim().endsWith("%")){
				// 種別
				kind = new ResourceTag(text.trim().replace("%", "").trim(),"","");
			}else{
				// 種別が指定されていない文章は無視
				if (kind != null){
					if (!ret.containsKey(kind)){
						ret.put(kind, new ArrayList<String>());
					}
					// テキストを設定
					ret.get(kind).add(text.trim());
				}
			}
		}
		return ret;
	}
}
