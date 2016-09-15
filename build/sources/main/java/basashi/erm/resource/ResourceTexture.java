package basashi.erm.resource;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import basashi.erm.core.ModCommon;
import basashi.erm.core.log.ModLog;
import basashi.erm.entity.EntityERMBase;
import basashi.erm.resource.parts.ERMDressParts;
import basashi.erm.resource.parts.ERMDressTextureInfo;
import basashi.erm.resource.parts.ModelOptionPartsInfo;
import basashi.erm.util.Util;

public class ResourceTexture implements IERMResource {
	public static final String DirName = "texture";
	private static final String prefixDress = "dress";
	private static final String prefixArmor = "armor";




	// png ファイルかどうか確認するフィルタ
	private static final FileFilter png = new FileFilter(){
		@Override
		public boolean accept(File pathname){
			try{
				if (pathname.isFile()){
					if (pathname.getName().endsWith(".png")){
						return true;
					}
				}
			}catch(Exception ex){
				ModLog.log().warn("[Texture Find Error] : "+pathname.getName() );
			}
			return false;
		}
	};

	// 拡張子がjsonのファイルを返すフィルタ
	private static final FileFilter json = new FileFilter(){
		@Override
		public boolean accept(File pathname){
			try{
				if (pathname.isFile() && pathname.getName().endsWith(".json")){
					return true;
				}
			}catch(Exception ex){
				ModLog.log().warn("[Texture Find Error] : "+pathname.getName() );
			}
			return false;
		}
	};



	// テクスチャのフォルダ
	private File textureDir;

	// テクスチャデータ
	private Map<String,ERMDressTextureInfo> bodySkin = new HashMap<String,ERMDressTextureInfo>();
	private Map<String,ERMDressTextureInfo> eyeSkin = new HashMap<String,ERMDressTextureInfo>();
	private Map<String,ERMDressTextureInfo> dressSkin = new HashMap<String,ERMDressTextureInfo>();
	private Map<String,ERMDressTextureInfo> headSkin = new HashMap<String, ERMDressTextureInfo>();
	private Map<String,ERMDressTextureInfo> otherSkin = new HashMap<String, ERMDressTextureInfo>();
	// オプションパーツデータ
	private Map<String,ModelOptionPartsInfo> optionParts = new HashMap<String,ModelOptionPartsInfo>();

	// コンストラクタ
	public ResourceTexture() throws Exception{

	}

	/***************************************************************************************************************/
	/** マップ操作                                                                                                **/
	/***************************************************************************************************************/
	private Map<String,ERMDressTextureInfo> getMap(ERMDressParts kind){
		Map<String,ERMDressTextureInfo> ret;
		switch(kind){
		case BODY:
			ret = bodySkin;
			break;
		case EYE:
			ret =eyeSkin;
			break;
		case HEAD:
			ret =headSkin;
			break;
		case DRESS:
			ret =dressSkin;
			break;
		default:
			ret =otherSkin;
			break;
		}
		return ret;
	}

	// マップにテクスチャを登録する
	private void addTexture(ERMDressTextureInfo texture){
		Map<String,ERMDressTextureInfo> map = getMap(texture.KindE());
		if (!map.containsKey(texture.Name())){
			map.put(texture.FileName(), texture);
		}else{
			ModLog.log().info("[Texture duplicate] : "+texture.Name());
		}
	}
	// 指定のマップからテクスチャを検索する
	public boolean searchTexture(ERMDressParts kind, String tex_name){
		Map<String,ERMDressTextureInfo> map = getMap(kind);
		return map.containsKey(tex_name);
	}

	// マップにモデルパーツを登録する
	private void addOptionParts(ModelOptionPartsInfo parts){
		if (!optionParts.containsKey(parts.getName())){
			// パーツのテクスチャを検索
			if (searchTexture(ERMDressParts.OPT1,parts.getTextureNmae())){
				parts.registtexture(ERMDressParts.OPT1);
			}else if (searchTexture(ERMDressParts.HEAD,parts.getTextureNmae())){
				parts.registtexture(ERMDressParts.HEAD);
			}else if (searchTexture(ERMDressParts.BODY,parts.getTextureNmae())){
				parts.registtexture(ERMDressParts.BODY);
			}else if (searchTexture(ERMDressParts.EYE,parts.getTextureNmae())){
				parts.registtexture(ERMDressParts.EYE);
			}else if (searchTexture(ERMDressParts.DRESS,parts.getTextureNmae())){
				parts.registtexture(ERMDressParts.DRESS);
			}else{ModLog.log().info("[ModelParts texture not found] : "+parts.getName() + "["+parts.getTextureNmae() + "]");}

			// テクスチャがないパーツは登録しない
			if ( parts.getTextureKind() != null){
				optionParts.put(parts.getName(), parts);
			}else{
				ModLog.log().info("[ModelParts texture not found] : "+parts.getName() + "["+parts.getTextureNmae() + "]");
			}
		}else{
			ModLog.log().info("[ModelParts duplicate] : " + parts.getName());
		}
	}



	/***************************************************************************************************************/
	/** リソース登録                                                                                              **/
	/***************************************************************************************************************/
	// テクスチャ、モデルリストの作成の流れ
	/* テクスチャ、モデルリストの作成は初期化時に1度だけ行う
	 * (本当はお着換え画面を開くときに毎回読み直ししてmc再起動無しにテクスチャ追加できるようにしたいが、
	 * 削除されているといろいろと面倒)
	 *
	 * 処理の順序
	 * 1.デフォルトのテクスチャを読み込みマップに登録(デバッグの場合bin/assets/以下？　jarの場合mods)
	 * 2.デフォルトの追加パーツを読み込みマップに登録
	 * 3.mods/ERMResource/texture フォルダを検索してカスタムテクスチャを読み込みマップに登録
	 * 4.mods/ERMResource/texrure フォルダを検索してカスタムパーツを読み込みリストに登録
	 *
	 */

	@Override
	public void initResource() throws Exception {
		// デフォルトテクスチャ、デフォルトオプションパーツをマップに登録
		File dir;
		Class target = this.getClass();
		if (ModCommon.isDebug){
			// 自分のクラス自身のパス(URL)を取得する
			URL location = target.getResource("/" + "assets/erm/textures/parts");
			dir = new File(location.getPath());
			ModLog.log().info("[Default Resource Path]:" + location.getPath());

			// マップ作製
			this.searchPng(dir);
			this.searchOptionJson(dir);
		}else{
			// 自分のクラス自身のパス(URL)を取得する
			URL location = target.getResource("");
			ModLog.log().info(location.getPath());
			dir = new File(location.getPath().substring(location.getPath().indexOf(":")+1, location.getPath().lastIndexOf(".jar")+4));
			ModLog.log().info("[Default Resource Path]:" + location.getPath());
			ModLog.log().info("[ZipFile]: " + dir.getPath());

			//マップ作製
			ZipFile zip = null;
			try{
				if (dir.exists()){
					zip = new ZipFile(dir);
					for (Enumeration<? extends ZipEntry> e = zip.entries(); e.hasMoreElements();) {
						ZipEntry entry = e.nextElement();
						ModLog.log().info("[check target]:"+entry.getName());
						if (entry.isDirectory()) continue;

						// パーツフォルダの中身だけ検索
						if (entry.getName().startsWith("assets/erm/textures/parts/")){
							ModLog.log().info("[read start]:"+entry.getName());
							try(InputStream istream = zip.getInputStream(entry)){
								if (entry.getName().endsWith(".png")){
									// ping用
									ModLog.log().info("png");
									addTexture(new ERMDressTextureInfo(entry.getName(),istream));
								}else{
									// json用
									ModLog.log().info("json");
									addOptionParts(new ModelOptionPartsInfo(entry.getName(),istream));
								}
							}catch(Exception ex){
								ex.printStackTrace();
								ModLog.log().info("[Error add] : " + entry.getName());
							}
						}
					}
				}else{
					ModLog.log().fatal("[jarfile not found]");
					throw new FileNotFoundException();
				}
			}catch(Exception ex)
			{
				ModLog.log().fatal("[Default Texture Error]");
				ex.printStackTrace();
				throw ex;
			}finally{
				if(zip != null){
					try{
						zip.close();
					}catch(Exception ex){

					}
				}
			}
		}
	}


	@Override
	public boolean loadResource(File directory) {
		if(this.DirName.equals(directory.getName())){
			// png検索
			searchPng(directory);
			// json検索
			searchOptionJson(directory);
			return true;
		}
		return false;
	}

	// textureフォルダのpngファイルを検索してマップに登録する
	private boolean searchPng(File dir){
		// png リスト取得
		File[] pngs = dir.listFiles(png);
		for(File pngfile : pngs){
			try{
				try(InputStream istream = new FileInputStream(pngfile)){
					// テクスチャを追加
					addTexture(new ERMDressTextureInfo(pngfile.getName(), istream));
				}
			}catch(Exception ex){
				// テクスチャ名のフォーマットエラー
				ModLog.log().warn("[Texture Error] :" + pngfile.getName());
			}
		}
		return true;
	}

	// textureフォルダのJsonファイルを検索してリストに登録する
	private void searchOptionJson(File dir){
		// jsonリスト取得
		File[] parts = dir.listFiles(json);
		for(File partsfile : parts){
			try{
				try(InputStream istream = new FileInputStream(partsfile)){
					// パーツを追加
					addOptionParts(new ModelOptionPartsInfo(partsfile.getName(),istream));
				}
			}catch(Exception ex){
				// エラー
				ModLog.log().warn("[OptionParts Error] :" + partsfile.getName());
			}
		}
	}

	/*************************************************************************/
	/** インタフェース                                                       */
	/**
	 * @throws IOException
	 * @throws ZipException ***********************************************************************/


	@Override
	public boolean chkDir(String directory) {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	public boolean chkDir(File directory) {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	public String dirName() {
		return DirName;
	}




	/***********************************************************************************************/
	/** モデル用テクスチャ処理                                                                                                                                                                                                    **/
	/***********************************************************************************************/
	// テクスチャ作成の流れ
	/* テクスチャ作成は以下の条件で行われる
	 * a.Entityの初期化
	 * b.テクスチャ選択画面でテクスチャが決定された場合
	 * c.NBTに登録されている内容と、現状の内容が食い違っている場合(NBTの内容を優先)
	 *
	 * 処理順序
	 * 1.ドレス内容ファイルがあるか確認
	 * 2.ある場合は読み込む4へ
	 * 3.ない場合は現在の設定内容でドレス内容ファイルを作成
	 * 4.テクスチャファイルがあるか確認
	 * 5.ある場合は読み込む
	 * 6.ない場合はドレス内容ファイルの内容を基に作成
	 */

	// テクスチャ銘ファイルを読み込むか、なければ作る
	public boolean readOrMakeTexFile(EntityERMBase entity, File texD){
		Map<ERMDressParts,String> texture = entity.getTextureDresses();
		// 衣装テクスチャ銘ファイル
		if (!texD.exists()){
			// テクスチャセットファイルが存在しない場合作成する
			updateTexFile(entity,texD);
		}else{
			// ある場合は読み込む
			try {
				List<String> read = Files.readAllLines(Paths.get(texD.toURI()),StandardCharsets.UTF_8);
				for ( String str : read){
					String[] work = str.split("\t");
					for (ERMDressParts parts : ERMDressParts.values()){
						if (work[0].equals(parts.getPartsName())){
							// 部位名称しかない場合は空白
							texture.put(parts, work.length==2?work[1]:"");
							break;
						}
					}
				}
				// 読み込んだテクスチャ銘を設定
				entity.setTextureDress(texture);
			} catch (Exception e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
				// 読み込めない
				return false;
			}
		}
		return true;
	}


	// ドレステクスチャを読み込むか、なければ作る
	public boolean readOrMakeDressFile(EntityERMBase entity, File dress){
		Map<ERMDressParts,String> texture = entity.getTextureDresses();
		if (!dress.exists()){
			makeTexture(texture);
		}else{
			// テクスチャを設定する
			entity.setTextureDress(dress.getPath());
			// モデルを更新する
		}
		return true;
	}




	// テクスチャを作成し、モデルを更新する
	public boolean makeTexture(Map<ERMDressParts,String> textureData){
		// テクスチャを準備する

		// モデルを更新する
		return false;
	}

	// モデルを更新する
	public void modelUpdate(Map<ERMDressParts,String> textureData){

	}



	// テクスチャ名ファイルを更新する
	public boolean updateTexFile(EntityERMBase entity, File texD){
		Map<ERMDressParts,String> texture = entity.getTextureDresses();
		// 衣装テクスチャ銘ファイル
		if (!texD.getParentFile().exists()){
			texD.getParentFile().mkdirs();
		}

		// テクスチャセットファイルが存在しない場合作成する
		try {
			FileWriter stream = new FileWriter(texD);
			for (ERMDressParts parts : ERMDressParts.values()){
				stream.write(parts.getPartsName() + "\t" + texture.get(parts) + Util.ReturnCode());
			}
			stream.close();
		} catch (Exception e) {
			e.printStackTrace();
			// ファイルが作成できなければ失敗を返す
			return false;
		}
		return true;
	}




	/************************************/
	/** デバッグ用                                                              **/
	/************************************/
	public void DebugInfo(){

	    try (FileWriter stream = new FileWriter(new File(ERMResourceManager.instance().baseDir(),"ERM.debug.log"))){

			for (String key: bodySkin.keySet()){
				try{
					stream.write(bodySkin.get(key).toString() + Util.ReturnCode());
				}catch(Exception ex){
					ModLog.log().debug("{logError}"+key);
				}
			}
			for (String key: eyeSkin.keySet()){
				try{
					stream.write(eyeSkin.get(key).toString() + Util.ReturnCode());
				}catch(Exception ex){
					ModLog.log().debug("{logError}"+key);
				}
			}
			for (String key: dressSkin.keySet()){
				try{
					stream.write(dressSkin.get(key).toString() + Util.ReturnCode());
				}catch(Exception ex){
					ModLog.log().debug("{logError}"+key);
				}
			}
			for (String key: headSkin.keySet()){
				try{
					stream.write(headSkin.get(key).toString() + Util.ReturnCode());
				}catch(Exception ex){
					ModLog.log().debug("{logError}"+key);
				}
			}
			for (String key: otherSkin.keySet()){
				try{
					stream.write(otherSkin.get(key).toString() + Util.ReturnCode());
				}catch(Exception ex){
					ModLog.log().debug("{logError}"+key);
				}
			}

			for (String key : optionParts.keySet()){
				try{
					stream.write(optionParts.get(key).toString() + Util.ReturnCode());
				}catch(Exception ex){
					ModLog.log().debug("{logError}"+key);
				}
			}

	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }

	}


}
