package basashi.erm.resource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import basashi.erm.ai.workmode.IERMAIBase;
import basashi.erm.core.log.ModLog;
import basashi.erm.entity.EntityERMBase;
import basashi.erm.resource.textures.EnumTextureParts;
import net.minecraft.client.Minecraft;

public class ERMResourceManager {
	private final String RESOURCE_COMMON = "config/ERMResource";
	private final String RESOURCE_SAVE = "ERMResource";


	// リソースマネージャのインスタンス
	private static final ERMResourceManager instance = new ERMResourceManager();
	// AIマネージャのインスタンス
	private static ResourceAI ai;
	// サウンドマネージャのインスタンス
	private static ResourceSound sounds;
	// テキストマネージャのインスタンス
	private static ResourceText text;
	// テクスチャマネージャのインスタンス
	private static ResourceTexture texture;

	// リソースベースのディレクトリ
	private File baseDir = null;
	// 各セーブデータ別のリソースディレクトリ
	private File saveDir = null;

	// ファイルリスト
	private List<File> fileList = new ArrayList<File>();
	// リソースの読み込みが完了しているか
	private boolean isResourceLoad = false;
	// 独自テクスチャが使用できる状態かどうか
	private boolean isUseCustomTexture = true;
	// リソースリスト
	private List<IERMResource> resources = new ArrayList<IERMResource>();


	// コンストラクタ
	private ERMResourceManager(){}
	// インスタンスの取得
	public static ERMResourceManager instance(){return instance;}

	// リソースの初期化
	public void init() throws Exception{
		(ai = new ResourceAI()).initResource();;
		resources.add(ai);
		(sounds = new ResourceSound()).initResource();;
		resources.add(sounds);
		(text = new ResourceText()).initResource();;
		resources.add(text);
		(texture = new ResourceTexture()).initResource();;
		resources.add(texture);
		isResourceLoad = true;
	}

	// リソースを使用可能かどうか
	public boolean CanUseResource(){return this.isResourceLoad;}

	// リソースの読み込み
	public boolean loadResource(){
		if (Minecraft.getMinecraft() != null && CanUseResource()){
			baseDir = new File(Minecraft.getMinecraft().mcDataDir,RESOURCE_COMMON);
			// リソース用のディレクトリがなければ生成
			if (!baseDir.exists()){
				ModLog.log().info("makedir ERMResource");
				try{
					baseDir.mkdir();
				}catch(Exception ex){
					// 失敗した場合外部リソース無しで起動
					ModLog.log().warn("fail ERMResource");
					ex.printStackTrace();
					baseDir = null;
					return false;
				}
			}
			for(IERMResource rc : resources){
				File rcf = new File(baseDir,rc.dirName());
				if (!rcf.exists()){
					rcf.mkdirs();
				}
			}

			// ファイルリストを取得
			getModFile();

			for (File fil : fileList){
				if (fil.isDirectory()){
					addDirResource(fil);
				}else{
					// TODO : zipからの読み込み対応
					//addZipResource(fil);
				}
			}
			return true;
		}
		return false;
	}

	// 個別リソースディレクトリセットアップ
	public boolean setSaveDir(File saveDir){
		this.saveDir = new File(saveDir,RESOURCE_SAVE);
		if (!this.saveDir.exists()){
			try{
				// 個別リソース用のディレクトリをセーブデータディレクトリの中に作る
				this.saveDir.mkdirs();
				ModLog.log().info("make resource saveDir" + this.saveDir.getPath());
				isUseCustomTexture = true;
			}catch(Exception ex){
				// 作るのに失敗した場合、個別リソース使用不可で起動する
				ModLog.log().warn("can't make saveDir");
				ex.printStackTrace();
				ModLog.log().warn("can't use customTexture");
				isUseCustomTexture = false;
				return false;
			}
		}
		return true;
	}

	// 個別リソースディレクトリを取得
	public File getSaveDir(){
		return saveDir;
	}


	/****************************************************************************************/
	/** 後悔しない関数                                                                     **/
	/****************************************************************************************/
	// ファイル検索
	private void getModFile(){
		// ファイル検索
		try{
			if (baseDir.isDirectory()){
				for (File f : baseDir.listFiles()){
					// mods/ERMResourceディレクトリ内を検索
					// zipファイルか、ディレクトリの場合リストに登録
					if (f.getName().endsWith(".zip")){
						// TODO: zipからの読み込み対応
						//fileList.add(f);
						//ModLog.log().info("find resource:" + f.getName());
					}else if(f.isDirectory()){
						fileList.add(f);
						ModLog.log().info("find resource:" + f.getName());
					}
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	// ディレクトリの中のリソースを探す
	private boolean addDirResource(File fil){
		if (fil == null){
			return false;
		}
		try{
			for (File f : fil.listFiles()){
				if(!f.isDirectory()){
					for (IERMResource resource: resources){
						if (resource.loadResource(fil)){
							break;
						}
					}
				}
			}
			return true;
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
	}


	// Zipファイル内のリソースを探す]
	private boolean addZipResource(File fil){
		// TODO: ZIP からの読み込み対応
//		if (fil == null || fil.isDirectory()) {
//			return false;
//		}
//		try {
//			FileInputStream fileinputstream = new FileInputStream(fil);
//			ZipInputStream zipinputstream = new ZipInputStream(fileinputstream);
//			ZipEntry zipentry;
//			do {
//				zipentry = zipinputstream.getNextEntry();
//				if(zipentry == null)
//				{
//					break;
//				}
//				if (!zipentry.isDirectory()){
//					File file = new File(zipentry.getName());
//
//					if( file.getParent().endsWith(ResourceAI.DirName)){
//
//					}else if(file.getParent().endsWith(ResourceSound.DirName)){
//
//					}else if(file.getParent().endsWith(ResourceText.DirName)){
//
//					}else if(file.getParent().endsWith(ResourceTexture.DirName)){
//						texture.addTextureZip(zipentry.getName());
//					}
//				}
//			} while(true);
//
//			zipinputstream.close();
//			fileinputstream.close();
//
//			return true;
//		} catch (Exception ex) {
//			ex.printStackTrace();
//			return false;
//		}
		return false;
	}


	/******************************************************************/
	/** AIリソース                                                                                                                                              **/
	/******************************************************************/
	/**
	 * 指定したモブ用のAIファイルリストを取り出す
	 * @return
	 */
	public List<IERMAIBase> getAIList(Class<? extends EntityERMBase> entity){
		ai.loadResource(new File(baseDir,ai.dirName()));
		return ai.getAIList(entity);
	}

	public IERMAIBase getAI(Class<? extends EntityERMBase> entity, String AIID){
		return ai.getAI(entity, AIID);
	}


	/******************************************************************/
	/** ボイスリソース                                                                                                                                         **/
	/******************************************************************/
	/**
	 * ボイス設定リストの一覧を取り出す
	 * @return
	 */
	public List<File> getSoundFileList(){
		sounds.loadResource(new File(baseDir,sounds.dirName()));
		return sounds.getNameList();
	}

	/**
	 * 音声設定ファイルをロードする
	 */
	public void loadSoundSetting(EntityERMBase entity){
		if(!sounds.loadVoice(entity)){
			ModLog.log().error("can't load soud setting file");
		}
	}

	/**
	 * 音声設定ファイルを保存する
	 * @param entity
	 */
	public void saveSoundSetting(EntityERMBase entity){
		if(!sounds.saveVoice(entity)){
			ModLog.log().error("can't save soud setting file");
		}
	}

	/******************************************************************/
	/** トーンリソース                                                                                                                                         **/
	/******************************************************************/
	/**
	 * トーンファイルの一覧を取り出す
	 * @return
	 */
	public List<File> getToneList(){
		// トーンリソース再読み込み後リストを戻す
		text.loadResource(new File(baseDir,text.dirName()));
		return text.getNameList();
	}

	/**
	 * モブのトーンを更新する
	 * @param entity
	 */
	public void updateTone(EntityERMBase entity){
		text.updateTone(entity);
	}

	/******************************************************************/
	/** テクスチャリソース                                                                                                                                  **/
	/******************************************************************/
	/**
	 * 指定したモブ用のテクスチャマップを取り出す
	 * @param entity
	 * @return
	 */
	public Map<EnumTextureParts,List<String>> getPartsMap(EntityERMBase entity){
		return texture.getPartsMap(entity);
	}

	/**
	 * モブのテクスチャを更新する
	 * @param entity
	 * @return
	 */
	public boolean updateCustomTexture(EntityERMBase entity){
		boolean ret = false;
		if (texture.canUseResource()){
			ret = texture.readOriginalTexture(entity);
		}
		return ret;
	}

	/****************************************************************************************/
	/** 各リソースへのアクセス                                                                                                                                                                                   **/
	/****************************************************************************************/
	public ResourceAI ai(){return ai;}
	public ResourceSound sound(){return sounds;}
	public ResourceText text(){return text;}
	public ResourceTexture texture(){return texture;}

	public File baseDir(){return baseDir;}
	public File saveDir(){return saveDir;}

	public boolean StartOn(){return saveDir!=null?true:false;}

}
