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
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import basashi.erm.core.ModCommon;
import basashi.erm.core.Mod_ERM;
import basashi.erm.core.log.ModLog;
import basashi.erm.entity.EntityERMBase;
import basashi.erm.resource.textures.EnumTextureParts;
import basashi.erm.resource.textures.ImageMaker;
import basashi.erm.resource.textures.ModelOptionPartsList;
import basashi.erm.resource.textures.SettingCustomTexture;
import basashi.erm.resource.textures.SettingCustomTexture.SettingData;
import basashi.erm.resource.textures.TextureInfo;
import basashi.erm.util.Contena2i;
import basashi.erm.util.Util;
import basashi.erm.util.Values.KIND_RESOURCE;

public class ResourceTexture implements IERMResource {
    // カスタムテクスチャ用のディレクトリ
	public static final String DirName = "texture";
    // デフォルトリソースのパス
	private static final String ORIGINAL_RESOURCE = "assets/erm/textures/parts";

	// テクスチャのフォルダ
	private File textureDir;

	// カスタムテクスチャが使用可能かどうか
	private boolean canCustomTexture;

	// テクスチャデータ
	private Map<String,TextureInfo> bodySkin = new HashMap<String,TextureInfo>();
	private Map<String,TextureInfo> eyeSkin = new HashMap<String,TextureInfo>();
	private Map<String,TextureInfo> dressSkin = new HashMap<String,TextureInfo>();
	private Map<String,TextureInfo> headSkin = new HashMap<String, TextureInfo>();
	private Map<String,TextureInfo> otherSkin = new HashMap<String, TextureInfo>();
	private Map<String,TextureInfo> armorSkin = new HashMap<String, TextureInfo>();
	// オプションパーツデータ
	private Map<String,ModelOptionPartsList> optionParts = new HashMap<String,ModelOptionPartsList>();


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


	// コンストラクタ
	public ResourceTexture() throws Exception{
		this.canCustomTexture = true;
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



	/*************************************************************************/
	/** リソース操作用                                                                                                                                                         */
	/*************************************************************************/
	/**
	 * 指定クラスのモブのパーツテクスチャのリストを取得する
	 * @param cls
	 * @return
	 */
	public Map<EnumTextureParts,List<String>> getPartsMap(EntityERMBase entity ){
		Class cls = entity.getClass();
		Map<EnumTextureParts,List<String>> ret = new HashMap<EnumTextureParts,List<String>>();

		// BODY
		ret.put(EnumTextureParts.BODY, new ArrayList<String>());
		for(String parts : bodySkin.keySet()){
			if(bodySkin.get(parts).Target() == cls){
				ret.get(EnumTextureParts.BODY).add(parts);
			}
		}

		// EYE
		ret.put(EnumTextureParts.EYE, new ArrayList<String>());
		for(String parts : eyeSkin.keySet()){
			if(eyeSkin.get(parts).Target() == cls){
				ret.get(EnumTextureParts.EYE).add(parts);
			}
		}

		//DRESS
		ret.put(EnumTextureParts.DRESS, new ArrayList<String>());
		for(String parts : dressSkin.keySet()){
			if(dressSkin.get(parts).Target() == cls){
				ret.get(EnumTextureParts.DRESS).add(parts);
			}
		}

		// HEAD
		ret.put(EnumTextureParts.HEAD, new ArrayList<String>());
		for(String parts : headSkin.keySet()){
			if(headSkin.get(parts).Target() == cls){
				ret.get(EnumTextureParts.HEAD).add(parts);
			}
		}

		// OPTION
		ret.put(EnumTextureParts.OPT1, new ArrayList<String>());
		for(String parts : optionParts.keySet()){
			if(optionParts.get(parts).Target() == cls){
				ret.get(EnumTextureParts.OPT1).add(parts);
			}
		}

		// Armor
		ret.put(EnumTextureParts.ARMOR, new ArrayList<String>());
		for(String parts : armorSkin.keySet()){
			if(armorSkin.get(parts).Target() == cls){
				ret.get(EnumTextureParts.ARMOR).add(parts);
			}
		}

		return ret;
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

	// 独自テクスチャを取得する
	// 1.テクスチャ名ファイルを読み込む(なければ作成する)
	// 2.モデル情報を更新する(独自パーツ、テクスチャサイズ、テクスチャインデックス)の更新
	// 3.テクスチャを読み込む(なければ作成する)
	public boolean readOriginalTexture(EntityERMBase target){
		if (!this.canCustomTexture){return false;}
		File settingFile = new File(ERMResourceManager.instance().getSaveDir(),target.settingTexture().TextureSettingFile());	// カスタムテクスチャ設定ファイル
		File textureFile = new File(ERMResourceManager.instance().getSaveDir(),target.settingTexture().TextureFile());          // カスタムテクスチャファイル
		try{
			// テクスチャ名ファイルの読み込み
			if (readOrMakeTexFile(target,settingFile)){
				// モデル情報の更新
				if (updateModel(target)){
					// テクスチャ読み込み
					if (makeTexture(target,textureFile)){
						return true;
					}
				}
			}
		}catch(Exception ex){
			ModLog.log().warn("BADBADBAD----------------------------------------------------------------------");
			ex.printStackTrace();
		}
		// カスタムテクスチャ使用不可
		ModLog.log().warn("[Can't using custom texture]");
		this.canCustomTexture = false;
		return false;
	}

	/*************************************************************************/
	/** インタフェース                                                                                                                                                               */
	/*************************************************************************/
	/**
	 * リソースディレクトリの名前を取得する
	 */
	@Override
	public String dirName() {
		return DirName;
	}

	/**
	 * カスタムテクスチャが使用可能かどうかを返却する
	 */
	@Override
	public boolean canUseResource(){
		return this.canCustomTexture;
	}

	/**
	 * デフォルトのテクスチャとオプションパーツをマップに登録する
	 * @see basashi.erm.resource.IERMResource#initResource()
	 */
	@Override
	public void initResource() throws Exception {
		// デフォルトテクスチャ、デフォルトオプションパーツをマップに登録
		File dir;
		Class target = this.getClass();
		List<TextureInfo> optioninfo = new ArrayList<TextureInfo>();
		if (ModCommon.isDevelop){
			// 自分のクラス自身のパス(URL)を取得する
			URL location = target.getResource("/" + ORIGINAL_RESOURCE);
			dir = new File(location.getPath());
			ModLog.log().info("[Default Resource Path]:" + location.getPath());

			// マップ作製
			this.searchPng(dir);
			this.searchOptionJson(dir);
		}else{
			// 自分のクラス自身のパス(URL)を取得する
			URL location = target.getResource("");
			ModLog.log().debug(location.getPath());
			dir = new File(location.getPath().substring(location.getPath().indexOf(":")+1, location.getPath().lastIndexOf(".jar")+4));
			if (ModCommon.isDebug)ModLog.log().debug("[Default Resource Path]:" + location.getPath());
			if (ModCommon.isDebug)ModLog.log().debug("[ZipFile]: " + dir.getPath());

			//マップ作製
			ZipFile zip = null;
			try{
				if (dir.exists()){
					zip = new ZipFile(dir);
					for (Enumeration<? extends ZipEntry> e = zip.entries(); e.hasMoreElements();) {
						ZipEntry entry = e.nextElement();
						if (ModCommon.isDebug)ModLog.log().debug("[check target]:"+entry.getName());
						if (entry.isDirectory()) continue;

						// パーツフォルダの中身だけ検索
						if (entry.getName().startsWith(ORIGINAL_RESOURCE)){
							if (ModCommon.isDebug)ModLog.log().debug("[read start]:"+entry.getName());
							try(InputStream istream = zip.getInputStream(entry)){
								if (entry.getName().endsWith(".png")){
									// ping用
									if (ModCommon.isDebug)ModLog.log().debug("png");
									TextureInfo info = new TextureInfo(entry.getName(),istream);
									if (info.KindE() == EnumTextureParts.OPT1){
										// オプションの場合はオプション用リストへ
										optioninfo.add(info);
									}
									addTexture(info);
								}else{
									// json用
									ModLog.log().info("json");
									addOptionParts(new ModelOptionPartsList(entry.getName(),istream));
								}
							}catch(Exception ex){
								ex.printStackTrace();
								ModLog.log().info("[Error add] : " + entry.getName());
							}
						}
					}
					// 無駄なオプションテクスチャを破棄
					for (TextureInfo info : optioninfo){
						boolean find = false;
						for (String key: optionParts.keySet()){
							if (info.FileName().equals(optionParts.get(key).getTextureNmae())){
								find = true;
								break;
							}
						}
						if(!find){
							otherSkin.remove(info.FileName());
							ModLog.log().info("[Texture can't read] : " + info.FileName());
						}
					}
					optioninfo.clear();
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

	/**
	 * カスタムテクスチャとオプションパーツをロードする
	 * @see basashi.erm.resource.IERMResource#loadResource(java.io.File)
	 */
	@Override
	public boolean loadResource(File directory) {
		if(this.DirName.equals(directory.getName())){
			// png検索
			searchPng(directory);
			// json検索
			searchOptionJson(directory);

			// 無駄なオプションテクスチャを破棄
			for (String key : otherSkin.keySet()){
				boolean find = false;
				for (String key2: optionParts.keySet()){
					if (otherSkin.get(key).FileName().equals(optionParts.get(key2).getTextureNmae())){
						find = true;
						break;
					}
				}
				if(!find){
					otherSkin.remove(key);
					ModLog.log().info("[Texture can't read] : " + key);
				}
			}
			this.canCustomTexture = true;
			return true;
		}
		return false;
	}


	/***************************************************************************************************************/
	/** リソース登録                                                                                                                                                                                                                                                                  **/
	/***************************************************************************************************************/
	/**
	 * textureフォルダのpngファイルを検索してマップに登録する
	 * @param dir
	 * @return
	 */
	private boolean searchPng(File dir){
		// png リスト取得
		File[] pngs = dir.listFiles(png);
		for(File pngfile : pngs){
			try{
				try(InputStream istream = new FileInputStream(pngfile)){
					// テクスチャを追加
					addTexture(new TextureInfo(pngfile.getName(), istream));
				}
			}catch(Exception ex){
				// テクスチャエラー
				ModLog.log().warn("[Texture Error] :" + pngfile.getName());
			}
		}
		return true;
	}

	/**
	 * textureフォルダのJsonファイルを検索してリストに登録する
	 * @param dir
	 */
	private void searchOptionJson(File dir){
		// jsonリスト取得
		File[] parts = dir.listFiles(json);
		for(File partsfile : parts){
			try{
				try(InputStream istream = new FileInputStream(partsfile)){
					// パーツを追加
					addOptionParts(new ModelOptionPartsList(partsfile.getName(),istream));
				}
			}catch(Exception ex){
				// エラー
				ModLog.log().warn("[OptionParts Error] :" + partsfile.getName());
			}
		}
	}

	/*****************************************************************************************/
	/** カスタムテクスチャ設定ファイル 関連処理                                                                                                                                                   **/
	/*****************************************************************************************/
	/**
	 * カスタムテクスチャ設定を更新する
	 * @param entity
	 * @param settingF
	 * @return
	 */
	private SettingCustomTexture updateTexFile(EntityERMBase entity, File settingF){
		SettingCustomTexture texture = entity.settingTexture().getTextureSetting(KIND_RESOURCE.CUSTOM);
		SettingCustomTexture customtex;
		if ((customtex = makeCustomTexSetting(entity, texture))==null){
			if (ModCommon.isDebug)ModLog.log().debug("D LOG can't make cusutom setting");
			return null;
		}
		// テクスチャセットファイルが存在しない場合作成する
		try {
			FileWriter stream = new FileWriter(settingF);
			stream.write(customtex.getSaveString());
			stream.close();
		} catch (Exception e) {
			e.printStackTrace();
			// ファイルが作成できなければ失敗を返す
			return null;
		}
		return customtex;
	}

	/**
	 * カスタムテクスチャ設定情報を作成する
	 * @param entity
	 * @param texsetting
	 * @return
	 */
	private SettingCustomTexture makeCustomTexSetting(EntityERMBase entity, SettingCustomTexture texsetting){
		Map<EnumTextureParts,TextureInfo> texinfo = new HashMap<EnumTextureParts,TextureInfo>();
		// ファイルがあるのか検索
		for(EnumTextureParts kind : EnumTextureParts.values()){
			if (texsetting.hasParts(kind)){
				TextureInfo info = searchResource(entity,kind,texsetting.getInformation(kind).getName());
				if (info != null){
					// テクスチャ情報を設定
					texinfo.put(kind, info);
				}
			}
		}

		// 設定情報を作成
		Map<EnumTextureParts,SettingData> map = new HashMap<EnumTextureParts,SettingData>();
		int width,height,xoffset,yoffset=0;
		Contena2i size =  texinfo.get(EnumTextureParts.BODY).Size();
		// 基本の横幅、x方向オフセットを設定
		width = xoffset = size.getX();
		// 基本の縦幅を設定
		height = size.getY();
		// 基本パーツ設定
		for(EnumTextureParts parts : EnumTextureParts.getBodyBaseValues()){
			map.put(parts, new SettingData(texinfo.get(parts).FileName(),
					texsetting.getInformation(parts).getWidth(),texsetting.getInformation(parts).getHeight(),
					texsetting.getInformation(parts).getXOffset(),texsetting.getInformation(parts).getYOffset()));
		}

		//  オプションパーツ設定
		for(EnumTextureParts opt : EnumTextureParts.getOptionValues()){
			if(texinfo.containsKey(opt)){
				size = texinfo.get(opt).Size();
				width = size.getX();
				height = size.getY();
				map.put(opt, new SettingData(texsetting.getInformation(opt).getName(),width, height, xoffset, yoffset));

				if(ModCommon.isDebug)ModLog.log().debug(
						(new StringBuilder()).append("D LOG Custom Option Info KIND:").append(opt.name()).append(" ")
						.append("Width:").append(width).append(" ")
						.append("Height:").append(height).append(" ")
						.append("offsetx:").append(xoffset).append(" ")
						.append("offsety").append(yoffset).append(" ").toString());
				xoffset += width;
			}
		}
		return new SettingCustomTexture(entity.settingTexture().TextureSettingFile(),map);
	}


	/***************************************************************************************************************/
	/** カスタムテクスチャ、モデル作成                                                                                                                                                                                                                                                                 **/
	/***************************************************************************************************************/
	/**
	 * テクスチャ設定ファイルを読み込む(なければ作成する)
	 * @param entity
	 * @param settingF
	 * @return
	 */
	private boolean readOrMakeTexFile(EntityERMBase entity,File settingF){
		SettingCustomTexture customtex = null;
		// 衣装テクスチャ銘ファイルがないか、または衣装設定が更新されている場合ファイルを新規に作る
		if (!settingF.exists() || entity.settingTexture().doUpdateSetting()){
			entity.settingTexture().checkUpdateTextureSettingFlag();
			if (ModCommon.isDebug)ModLog.log().debug("D LOG not find customtexturesetting : " + settingF.getName());
			// テクスチャセットファイルが存在しない場合作成する
			if ((customtex = updateTexFile(entity,settingF)) == null){
				// カスタムテクスチャ設定が使用できない
				if (ModCommon.isDebug)ModLog.log().debug("D LOG can't update TexFile");
				return false;
			}
		}else{
			// ある場合は読み込む
			try {
				List<String> read = Files.readAllLines(Paths.get(settingF.toURI()),StandardCharsets.UTF_8);
				// カスタムテクスチャ設定情報を作成
				customtex = new SettingCustomTexture(settingF.getName(),(String[])read.toArray(new String[0]));

				// テクスチャがちゃんとあるか確認
				for(EnumTextureParts kind : EnumTextureParts.values()){
					if(!customtex.hasParts(kind)){continue;}
					// データを取得
					SettingData data = customtex.getInformation(kind);
					// リソースを検索
					TextureInfo tex = searchResource(entity, kind, data.getName());
					// テクスチャが見つからなければ例外を発生
					if (tex == null){throw new FileNotFoundException("not found texture " + data.getName());}

					Contena2i size = tex.Size();
					if(data.getWidth() != size.getX() || data.getHeight() != size.getY()){
						// テクスチャのサイズが記録されているものと違う→別のテクスチャに差し替えられている
						// テクスチャ情報を再作成する
						ModLog.log().warn("[Texture is different form previous.] : "+ tex.FileName());
						if ((customtex = updateTexFile(entity,settingF)) == null){
							// カスタムテクスチャ設定が使用できない
							if (ModCommon.isDebug)ModLog.log().debug("D LOG can't remake TexFile");
							return false;
						}
						if (ModCommon.isDebug)ModLog.log().debug("D LOG compleat remake texfile");
						break;
					}
				}
			} catch (Exception e) {
				ModLog.log().warn("[Can't read customtexsetting. start make new file]");
				e.printStackTrace();
				// 読み込みこめない or テクスチャ情報のフォーマットが違う or 対応するテクスチャがない場合は作成し直す
				try{settingF.delete();}catch(Exception ex){}
				// テクスチャセットファイルが存在しない場合作成する
				// (見つからないテクスチャはデフォルトテクスチャに置き換えられ、置き換えができなければカスタムテクスチャ自体が使用不可になる)
				if ((customtex = updateTexFile(entity,settingF)) == null){
					// カスタムテクスチャ設定が使用できない
					if (ModCommon.isDebug)ModLog.log().debug("D LOG can't make TexFile");
					return false;
				}
			}
		}
		// ターゲットにカスタム情報を設定
		entity.settingTexture().setCustomTexture(customtex);
		return true;
	}

	/**
	 * モデル更新
	 * @param entity
	 * @return
	 */
	private boolean updateModel(EntityERMBase entity){
		List<ModelOptionPartsList> optionparts = new ArrayList<ModelOptionPartsList>();
		SettingCustomTexture customtex = entity.settingTexture().getTextureSetting(KIND_RESOURCE.CUSTOM);
		for(EnumTextureParts kind : EnumTextureParts.values()){
			if (EnumTextureParts.OPT1.getRegion().equals(kind.getRegion())){
				if(customtex.hasParts(kind)){
					// オプション情報を取り出す
					SettingData data = customtex.getInformation(kind);
					// オプションモデル情報を取り出す
					ModelOptionPartsList info = this.optionParts.get(data.getName());
					if (info != null){
						// テクスチャサイズの更新
						info.setTextureSize(data.getWidth(), data.getHeight());
						// テクスチャインデックスの更新
						info.setTextureOffset(data.getXOffset(),data.getYOffset());
						// リストに登録
						optionparts.add(info);

						if (ModCommon.isDebug)ModLog.log().debug("D LOG option name :" + data.getName());
						if (ModCommon.isDebug)ModLog.log().debug("D LOG texture name :" + info.FineName());
					}
					// カスタムテクスチャファイルの読み込み/作成時にテクスチャがあることを確認しているので
					// テクスチャがないかも～とか心配いらない
				}
			}
		}
		// モデルにパーツを設定
		Mod_ERM.getModel(entity.getClass()).SetParts(optionparts);
		return true;
	}

	/**
	 * テクスチャを作成し、モデルを更新する
	 * @param entity
	 * @param textureF
	 * @return
	 */
	private boolean makeTexture(EntityERMBase entity, File textureF){
		boolean ret = true;
		// テクスチャを準備する
		SettingCustomTexture texture = entity.settingTexture().getTextureSetting(KIND_RESOURCE.CUSTOM);
		Map<EnumTextureParts,TextureInfo> map = new HashMap<EnumTextureParts,TextureInfo>();
		for (EnumTextureParts kind : EnumTextureParts.values()){
			if (texture.hasParts(kind)){
				SettingData data = texture.getInformation(kind);
				map.put(kind, searchResource(entity, kind, data.getName()));
			}
		}
		try{
			ImageMaker.makeDressImage(textureF, map, texture);
		}catch(Exception ex){
			ModLog.log().warn("[Can't create dresstexture]");
			ex.printStackTrace();
			ret = false;
		}
		return ret;
	}

	/**
	 * テクスチャマップからテクスチャを検索してテクスチャ情報を返す
	 * @param entity
	 * @param kind
	 * @param texName
	 * @return
	 */
	private TextureInfo searchTexture(EntityERMBase entity, EnumTextureParts kind, String texName){
		TextureInfo ret = null;
		Map<String, TextureInfo> texmap = getMap(kind);
		if (texmap.containsKey(texName) &&
				texmap.get(texName).Target() == entity.getClass()){
			ret = texmap.get(texName);
		}
		return ret;
	}

	/***************************************************************************************************************/
	/** マップ操作                                                                                                **/
	/***************************************************************************************************************/
	/**
	 * 指定したパーツ用のマップを取り出す
	 * @param kind
	 * @return
	 */
	private Map<String,TextureInfo> getMap(EnumTextureParts kind){
		Map<String,TextureInfo> ret;
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
		case ARMOR:
			ret = armorSkin;
		default:
			ret =otherSkin;
			break;
		}
		return ret;
	}

	/**
	 * マップにテクスチャを登録する
	 * @param texture
	 */
	private void addTexture(TextureInfo texture){
		Map<String,TextureInfo> map = getMap(texture.KindE());
		if (!map.containsKey(texture.Name())){
			map.put(texture.FileName(), texture);
		}else{
			ModLog.log().info("[Texture duplicate] : " + texture.Name());
		}
	}

	/**
	 * 指定マップからテクスチャを検索する
	 * @param kind
	 * @param tex_name
	 * @return
	 */
	private boolean searchTexture(EnumTextureParts kind, String tex_name){
		Map<String,TextureInfo> map = getMap(kind);
		return map.containsKey(tex_name);
	}

	/**
	 * マップにモデルパーツを設定する
	 * @param parts
	 */
	private void addOptionParts(ModelOptionPartsList parts){
		if (!optionParts.containsKey(parts.getName())){
			// パーツのテクスチャを検索
			if (searchTexture(EnumTextureParts.OPT1,parts.getTextureNmae())){
				parts.registtexture(EnumTextureParts.OPT1);
			}else if (searchTexture(EnumTextureParts.HEAD,parts.getTextureNmae())){
				parts.registtexture(EnumTextureParts.HEAD);
			}else if (searchTexture(EnumTextureParts.BODY,parts.getTextureNmae())){
				parts.registtexture(EnumTextureParts.BODY);
			}else if (searchTexture(EnumTextureParts.EYE,parts.getTextureNmae())){
				parts.registtexture(EnumTextureParts.EYE);
			}else if (searchTexture(EnumTextureParts.DRESS,parts.getTextureNmae())){
				parts.registtexture(EnumTextureParts.DRESS);
			}else{parts.registtexture(null);}

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

	/**
	 * 指定したリソースがあるか確認する
	 * @param entity
	 * @param kind
	 * @param resourceName
	 * @return
	 */
	private TextureInfo searchResource(EntityERMBase entity, EnumTextureParts kind, String resourceName){
		// ファイルがあるのか検索
		TextureInfo info = null;
		if (EnumTextureParts.OPT1.getRegion().equals(kind.getRegion())){
			if (ModCommon.isDebug)ModLog.log().debug("D LOG search resource Option : " + resourceName);
			// パーツファイル
			if (this.optionParts.containsKey(resourceName) &&
					(this.optionParts.get(resourceName).Target() == entity.getClass())){
				// パーツファイルがある場合テクスチャ検索
				ModelOptionPartsList parts = this.optionParts.get(resourceName);
				// テクスチャを検索、ない場合オプションパーツ設定をしない
				info = searchTexture(entity,parts.getTextureKind(),parts.getTextureNmae());
				if (ModCommon.isDebug){ModLog.log().debug("D LOG find partsinfo");}
				if (ModCommon.isDebug &&  info == null){ModLog.log().debug("D LOG not found texture");}
			}
		}else{
			if (ModCommon.isDebug)ModLog.log().debug("D LOG search resource " + kind.getRegion() + " : "+ resourceName);
			// テクスチャファイル
			 info = searchTexture(entity, kind, resourceName);
			 if (info == null){
				if (ModCommon.isDebug)ModLog.log().debug("D LOG can't found texture");
				 // 指定されたテクスチャがない場合デフォルトパーツテクスチャを指定
				 info = searchTexture(entity, kind ,entity.settingTexture().getTextureSetting(KIND_RESOURCE.DEFAULT).getInformation(kind).getName());
				 if (info == null){
					if (ModCommon.isDebug)ModLog.log().debug("D LOG can't found default texture");
					 // デフォルトパーツテクスチャがない場合カスタムテクスチャ作成をあきらめる
					 return null;
				 }
			 }
		}
		return info;
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
