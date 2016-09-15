package basashi.erm.entity.setting;

import basashi.erm.entity.EntityERMBase;
import basashi.erm.resource.ERMResourceManager;
import basashi.erm.resource.textures.SettingCustomTexture;
import basashi.erm.util.Values.KIND_RESOURCE;
import net.minecraft.nbt.NBTTagCompound;

public class EntityTextureSetting {
	/**
	 * テクスチャファイル名
	 */
	private String texFileName;
	/**
	 * アーマーファイル名
	 */
	private String armFileName;
	/**
	 * テクスチャ設定ファイル名
	 */
	private String settingFileName;
	/**
	 * カスタムテクスチャを使用するかどうか
	 */
	private boolean useCustomTexture;
	/**
	 * カスタムテクスチャを更新するかどうか
	 */
	private boolean updateCustomTexture;
	/**
	 * カスタムテクスチャ設定ファイルが更新されているかどうか
	 */
	private boolean updateSettingFile;

	// カスタムテクスチャ設定
	protected SettingCustomTexture texSetting;
	// デフォルトテクスチャ設定
	protected SettingCustomTexture defaultSetting;

	/********************************************************************************/
	/** 初期化                                                                                                                                                                                        **/
	/********************************************************************************/
	/**
	 * コンストラクタ
	 * @param tex
	 */
	public EntityTextureSetting(EntityERMBase entity, String texFile, String armFile, String settingFile, SettingCustomTexture tex){
		// ファイル名
		texFileName = texFile;
		settingFileName = settingFile;
		armFileName = armFile;

		// テクスチャ設定
		texSetting = tex.clone();
		defaultSetting = tex.clone();

		// フラグ類
		useCustomTexture = false;
		updateCustomTexture = false;
		updateSettingFile = false;
	}

	/**
	 * テクスチャ設定を初期化する
	 * @param def
	 */
	private void initTexture(EntityERMBase entity,SettingCustomTexture def){

	}

	/********************************************************************************/
	/** ファイル                                                                                                                                                                                         **/
	/********************************************************************************/
	/**
	 * テクスチャ設定ファイル名を取得する
	 * @return
	 */
	public String TextureSettingFile(){
		return settingFileName;
	}

	/**
	 * テクスチャ設定ファイル名を設定する
	 * @param name
	 */
	public void TextureSettingFile(String name){
		settingFileName = name;
	}

	/**
	 * テクスチャファイル名を取得する
	 * @return
	 */
	public String TextureFile(){
		return texFileName;
	}

	/**
	 * テクスチャファイル名を設定する
	 * @param name
	 */
	public void TextureFile(String name){
		texFileName = name;
		updateCustomTexture = true;
	}

	/**
	 * 防具用テクスチャを返す
	 * @return
	 */
	public String ArmarFile(){
		return armFileName;
	}

	/**
	 * 防具用テクスチャを設定する
	 * @param name
	 */
	public void ArmarFile(String name){
		armFileName = name;
	}

	/********************************************************************************/
	/** フラグ操作                                                                                                                                                                                   **/
	/********************************************************************************/
	/**
	 * カスタムテクスチャが使用できるかどうか確認する
	 * @return
	 */
	public boolean canUseCustomTexture(){
		return useCustomTexture;
	}

	/**
	 * カスタムテクスチャを更新するかどうか確認する
	 * @return
	 */
	public boolean doUpdateTexture(){
		return updateCustomTexture;
	}

	/**
	 * カスタムテクスチャ設定を更新するかどうか確認する
	 * @return
	 */
	public boolean doUpdateSetting(){
		return updateSettingFile;
	}

	/**
	 * テクスチャが更新されたことをチェックする
	 */
	public void checkUpdateTextureFlag(){
		updateCustomTexture = false;
	}

	/**
	 * テクスチャ設定が更新されたことをチェックする
	 */
	public void checkUpdateTextureSettingFlag(){
		updateSettingFile = false;
	}

	/********************************************************************************/
	/** テクスチャ操作                                                                                                                                                                            **/
	/********************************************************************************/
	/**
	 * カスタムテクスチャ設定を取り出す
	 * @param kind
	 * @return
	 */
	public SettingCustomTexture getTextureSetting(KIND_RESOURCE kind){
		if((kind == KIND_RESOURCE.CUSTOM) && (texSetting != null)){
			return texSetting;
		}
		return defaultSetting;
	}

	/**
	 * カスタムテクスチャ設定を設定する
	 * @param cust
	 */
	public void setCustomTexture(SettingCustomTexture cust){
		texSetting=cust.clone();
	}

	/**
	 * カスタムテクスチャを作成する
	 * @param entity
	 */
	public void customTexture(EntityERMBase entity){
		useCustomTexture = ERMResourceManager.instance().updateCustomTexture(entity);
		if(useCustomTexture){
			updateCustomTexture = true;
		}
	}

	/**
	 * NBTから読み込む
	 * @param tagCompound
	 * @param key
	 */
	public void readNBT(NBTTagCompound tagCompound){

        tagCompound.setString ("DRESS",   TextureFile());         // テクスチャファイル
        tagCompound.setString ("SETTING", TextureSettingFile());  // テクスチャ設定ファイル
        tagCompound.setString ("AROMOR",  ArmarFile());           // アーマーファイル
        String work;
        work = tagCompound.getString ("DRESS");         // テクスチャファイル
        if (!texFileName.equals(work)){
        	TextureFile(work);
        }

        work = tagCompound.getString ("SETTING");       // テクスチャ設定ファイル
        if (!settingFileName.equals(work)){
        	TextureSettingFile(work);
        }

        work = tagCompound.getString("ARMOR");
        if (!armFileName.equals(work)){
        	ArmarFile(work);
        }
	}

	/**
	 * NBTへ書き込む
	 * @param tagCompound
	 * @param key
	 */
	public void writeNBT(NBTTagCompound tagCompound){
        tagCompound.setString ("DRESS",   texFileName);         // テクスチャファイル
        tagCompound.setString ("SETTING", settingFileName);     // テクスチャ設定ファイル
        tagCompound.setString ("AROMOR",  armFileName);         // アーマーファイル

	}
}
