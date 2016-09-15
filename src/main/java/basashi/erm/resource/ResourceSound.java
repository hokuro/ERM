package basashi.erm.resource;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import basashi.erm.core.ModCommon;
import basashi.erm.core.log.ModLog;
import basashi.erm.entity.EntityERMBase;
import basashi.erm.resource.object.ResourceTag;
import basashi.erm.resource.sounds.SoundMap;
import basashi.erm.resource.sounds.SoundTag;
import basashi.erm.util.Util;

public class ResourceSound implements IERMResource {
	/**
	 * ボイスリソースディレクトリ
	 */
	public static final String DirName = "sound";

	// ボイスディレクトリ
	private static File DirSound;
	// ボイスファイルリスト
	private List<File> soundFiles;

	// ogg ファイルかどうか確認するフィルタ
	private static final FileFilter ogg = new FileFilter(){
		@Override
		public boolean accept(File pathname){
			try{
				if (pathname.isFile()){
					if (pathname.getName().endsWith(".ogg")){
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
	/** リソースの操作用                                                                                                                                                      */
	/*************************************************************************/
	/**
	 * サウンドファイルのリストを取り出す
	 * @return
	 */
	public List<File> getNameList(){
		return soundFiles;
	}

	/**
	 * サウンド設定ファイルを読み込む
	 * @param entity
	 * @return
	 */
	public boolean loadVoice(EntityERMBase entity){
		boolean ret = false;
		int cnt;
		if (!Util.StringisEmptyOrNull(entity.settingVoice().getSound().SoundFile())){
			File settingfile = new File(ERMResourceManager.instance().getSaveDir(),entity.settingVoice().getSound().SoundFile());
			if (settingfile.exists()){
				boolean change = false;
				try{
					List<String> load = Files.readAllLines(settingfile.toPath());
					ResourceTag tag;
					for(String str : load){
						if(ModCommon.isDebug)ModLog.log().debug("D log sounds load string :" + str);
						String[] split = str.split("\t");
						if(ModCommon.isDebug)ModLog.log().debug("D log sounds load tagname :" + split[0]);
						if ((tag = searchTag(split[0])) != null){
							List<File> filelist = new ArrayList<File>();
							String files = split[1];
							while(true){
								if(ModCommon.isDebug)ModLog.log().debug("D log sounds load lines :" + files);
								// ogg ファイルがなければループを抜ける
								if(!files.contains(".ogg")){break;}
								// ogg ファイルを取り出す
								int idx = files.indexOf(".ogg");
								String fname = files.substring(0, idx+4);
								if(ModCommon.isDebug)ModLog.log().debug("D log sounds load ogg :" + fname);
								// 取り出したファイルを抜いた文字列を作る
								files = files.substring(idx+5);
								// リソースがあるかどうか確認
								for (cnt = 0; cnt < soundFiles.size(); cnt++){
									if (soundFiles.get(cnt).getName().equals(fname)){
										filelist.add(soundFiles.get(cnt));
										break;
									}
								}
								if (cnt >= soundFiles.size()){
									if(ModCommon.isDebug)ModLog.log().debug("D log sounds load recouce no file :" + fname);
									change = true;
								}
							}
							// サウンドを更新する
							entity.settingVoice().getSound().setSoundMap(tag,filelist);
							break;
						}else{
							if(ModCommon.isDebug)ModLog.log().debug("D log sounds load delete tag :" + split[0]);
							change = true;
						}
					}
					// リソースの内容に変わりがある場合、ファイルを保存しなおす
					if (change){
						saveVoice(entity);
					}
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}else{
				// ない場合現在の設定でファイルを作る
				ret = saveVoice(entity);
			}
		}
		return ret;
	}
	/**
	 * サウンド設定ファイルを保存する
	 * @param entity
	 * @return
	 */
	public boolean saveVoice(EntityERMBase entity){
		// サウンドファイルを保存する
		File settingfile = new File(ERMResourceManager.instance().getSaveDir(),entity.settingVoice().getSound().SoundFile());
		SoundMap map = entity.settingVoice().getSound().getSoundMap();
		StringBuilder str = new StringBuilder();
		for (ResourceTag tag : SoundTag.instance.getResourceTag()){
			List<File> files = map.getAllSound(tag);
				str.append(tag.getTag()).append("\t");
			if (files != null){
				for (File fil : files){
					str.append(fil.getName());
				}

			}
			str.append(Util.ReturnCode());
		}
		try{
			Files.write(settingfile.toPath(), str.toString().getBytes(), StandardOpenOption.CREATE , StandardOpenOption.WRITE);
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
		return true;
	}


	/*************************************************************************/
	/** インタフェース                                                       */
	/*************************************************************************/
	@Override
	public void initResource() {}

	/**
	 * サウンドリソースを読み込む
	 */
	@Override
	public boolean loadResource(File directory) {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	/**
	 * サウンドリソースフォルダを取り出す
	 */
	@Override
	public String dirName() {
		return DirName;
	}

	/**
	 * リソースが使用できるかどうかを取得する
	 */
	@Override
	public boolean canUseResource() {
		return true;
	}

	/*************************************************************************/
	/** 内部用                                                                                                                                                                        */
	/*************************************************************************/
	/**
	 * サウンド用リソースディレクトリからoggファイル一覧を作成する
	 * @param dir
	 * @return
	 */
	private boolean searchSound(File dir){
		// ogg リスト取得
		File[] sounds = dir.listFiles(ogg);
		for(File soundfile : sounds){
			this.soundFiles.add(soundfile);
		}
		return true;
	}

	/**
	 * サウンドタグがあるかどうか確認する
	 * @return
	 */
	private ResourceTag searchTag(String strtag){
		for (ResourceTag tag : SoundTag.instance.getResourceTag()){
			if (tag.equals(strtag)){
				return tag;
			}
		}
		return null;
	}

	/**
	 * トーンファイルをロードする
	 * @param tonefile
	 * @return
	 */
	private Map<String,List<String>> loadToneFile(File tonefile){
		Map<String,List<String>> ret = new HashMap<String,List<String>>();
		List<String> tones;
		try{
			tones = Files.readAllLines(tonefile.toPath());
		}catch(Exception ex){
			ModLog.log().error("Can't read tonefile");
			ex.printStackTrace();
			return null;
		}
		String kind ="";
		for(String text : tones){
			if (text.trim().startsWith("//")){
				// コメント
				continue;
			}else if(text.trim().startsWith("%") && text.trim().endsWith("%")){
				// 種別
				kind = text.trim().replace("%", "").trim();
			}else{
				// 種別が指定されていない文章は無視
				if (kind != ""){
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
