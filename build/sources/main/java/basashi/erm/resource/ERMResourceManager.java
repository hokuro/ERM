package basashi.erm.resource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import basashi.erm.core.log.ModLog;
import basashi.erm.entity.EntityERMBase;
import net.minecraft.client.Minecraft;

public class ERMResourceManager {
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
	private File baseDir;

	// ファイルリスト
	private List<File> fileList = new ArrayList<File>();
	// リソースの読み込みが完了しているか
	private boolean isResourceLoad = false;
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
			baseDir = new File(Minecraft.getMinecraft().mcDataDir,"mods/ERMResource");
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
	/** テクスチャリソース                                                                                                                                  **/
	/******************************************************************/
	// モブのテクスチャ関係を初期化する
	public boolean initEntityTexture(EntityERMBase entity, File dress, File texD, File Armor, File texA){
		// テクスチャ銘ファイル
		if (!this.texture.readOrMakeTexFile(entity,texD)){return false;}
		// 衣装テクスチャファイル
		if (!this.texture.readOrMakeDressFile(entity,texD)){return false;}
		return true;
	}


	/****************************************************************************************/
	/** 各リソースへのアクセス                                                                                                                                                                                   **/
	/****************************************************************************************/
	public ResourceAI ai(){return ai;}
	public ResourceSound sound(){return sounds;}
	public ResourceText text(){return text;}
	public ResourceTexture texture(){return texture;}

	public File baseDir(){return baseDir;}

}
