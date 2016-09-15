package basashi.erm.resource.textures;

import java.util.HashMap;
import java.util.Map;

import basashi.erm.util.Contena2i;
import basashi.erm.util.Util;

public class SettingCustomTexture {
	private String fileName;
	private Contena2i tex_size;
	private Map<EnumTextureParts,SettingData> setting=null;
	private static final SettingData nothing = new SettingData("",0,0,0,0);

	// パーツ情報を作成
	public SettingCustomTexture(String fileName, String[] saveText){
		int width, height;
		width = height = 0;
		this.fileName = fileName;
		setting = new HashMap<EnumTextureParts,SettingData>();
		for(String txt : saveText){
			try{
				if (!Util.StringisEmptyOrNull(txt)){	// 空行は無視
					String[] info = txt.split("\t");
					EnumTextureParts kind = EnumTextureParts.getFromStringName(info[0]);
					if (kind == null){continue;}		// 無効データは無視
					if (info.length == 1){continue;}	// パーツ設定なし

					// テクスチャ、インデックス取り出し
					SettingData dat =  new SettingData(info);
					setting.put(kind,dat);


					// テクスチャの高さを更新
					if (height < dat.tex_height){
						height = dat.tex_height;
					}

					// テクスチャの幅を更新
					if (EnumTextureParts.OPT1.getRegion().equals(kind.getRegion())){
						width += dat.tex_width;
					}else if(EnumTextureParts.BODY == kind){
						width+=dat.tex_width;
					}
				}
			}catch(Exception ex){
				// フォーマット異常はとりあえず無視
			}
		}

		// 必須パーツの有無を確認
		if(setting.containsKey(EnumTextureParts.BODY) &&
				setting.containsKey(EnumTextureParts.EYE) &&
				setting.containsKey(EnumTextureParts.HEAD) &&
				setting.containsKey(EnumTextureParts.DRESS)){
			// テクスチャサイズを設定
			this.tex_size =new Contena2i(width,height);
		}else{
			throw new IllegalArgumentException("SettingEntityParts[File fromat Error. Essential object is missing .]");
		}
	}

	public SettingCustomTexture(String fileName, Map<EnumTextureParts,SettingData> setting){
		this.fileName = fileName;
		this.setting = new HashMap<EnumTextureParts,SettingData>();
		UpdateAll(setting);
	}

	// 全データ一括アップデート
	public void UpdateAll(Map<EnumTextureParts,SettingData> setting){
		if(setting.containsKey(EnumTextureParts.BODY) &&
				setting.containsKey(EnumTextureParts.EYE) &&
				setting.containsKey(EnumTextureParts.HEAD) &&
				setting.containsKey(EnumTextureParts.DRESS)){
			int height,width;
			height = setting.get(EnumTextureParts.BODY).tex_height;
			width = setting.get(EnumTextureParts.BODY).tex_width;
			// 設定ををコピーする
			for(EnumTextureParts kind : setting.keySet()){
				this.setting.put(kind, setting.get(kind).clone());

				// テクスチャの高さを更新
				if (height < setting.get(kind).tex_height){
					height = setting.get(kind).tex_height;
				}
				// テクスチャの幅を更新
				if (EnumTextureParts.OPT1.getRegion().equals(kind.getRegion())){
					width += setting.get(kind).tex_width;
				}
			}
			// テクスチャサイズを設定
			this.tex_size = new Contena2i(width,height);
		}else{
			throw new IllegalArgumentException("SettingEntityParts[Essential object is missing .]");
		}
	}

	// ファイル名を取得する
	public String getFileName(){
		return this.fileName;
	}

	/**
	 * 指定した部位のテクスチャ情報が設定されているかどうか確認する
	 * @param parts
	 * @return
	 */
	public boolean hasParts(EnumTextureParts parts){
		return setting.containsKey(parts);
	}

	// パーツ情報を取得する
	public SettingData getInformation(EnumTextureParts kind){
		if (setting.containsKey(kind)){
			return setting.get(kind);
		}
		return null;
	}


	// パーツ情報保存テキスト用の文字列を返す
	public String getSaveString(){
		StringBuilder saveStr = new StringBuilder();
		for(EnumTextureParts kind : setting.keySet()){
			saveStr.append(kind.name());
			// 部位情報がある場合、部位情報をテキストに書き込む
			if(setting.containsKey(kind)){
				saveStr.append("\t");
				saveStr.append(setting.get(kind).getSaveString());
			}
			// 改行
			saveStr.append(Util.ReturnCode());
		}
		return saveStr.toString();
	}

	public static class SettingData{
		private String name;
		private int tex_width;
		private int tex_height;
		private int xOffset;
		private int yOffset;

		// コンストラクタ(セーブテキストからの復元)
		public SettingData(String[] text){
			// フォーマット異常は無視する
			name = text[1];
			tex_width = Integer.parseInt(text[2]);
			tex_height = Integer.parseInt(text[3]);
			xOffset = Integer.parseInt(text[4]);
			yOffset = Integer.parseInt(text[5]);
		}

		// コンストラクタ(パーツ名 or テクスチャ名及び、テクスチャのオフセットを設定する)
		public SettingData(String name, int width, int height, int xOffset, int yOffset){
			this.name = name;
			this.tex_width = width;
			this.tex_height = height;
			this.xOffset = xOffset;
			this.yOffset = yOffset;
		}

		// パーツ名またはテクスチャ名を取得する
		public String getName(){
			return this.name;
		}


		// テクスチャの幅を取得すｒ
		public int getWidth(){
			return tex_width;
		}
		// テクスチャの高さを取得する
		public int getHeight(){
			return tex_height;
		}
		// テクスチャのオフセットを取得する
		public Contena2i getTexOffset(){
			return new Contena2i(this.xOffset,this.yOffset);
		}

		// テクスチャオフセットX座標を取得する
		public int getXOffset(){
			return this.xOffset;
		}

		// テクスチャオフセットY座標を取得する
		public int getYOffset(){
			return this.yOffset;
		}

		// テクスチャのオフセットを設定する
		public void setTexOffset(int x, int y){
			this.xOffset = x;
			this.yOffset = y;
		}

		// テクスチャのオフセットを設定する
		public void setTexOffset(Contena2i offset){
			this.xOffset = offset.getX();
			this.yOffset = offset.getY();
		}

		// テキストデータ保存用の文字列を返す
		public String getSaveString(){
			return (new StringBuilder().append(name).append("\t")
					.append(this.tex_width).append("\t").append(this.tex_height).append("\t")
					.append(this.xOffset).append("\t").append(this.yOffset)).toString();
		}

		// 自身の情報を使って別オブジェクトを返す
		public SettingData clone(){
			return new SettingData(this.name,this.tex_width,this.tex_height,this.xOffset,this.yOffset);
		}
	}

	/**
	 * 同一内容の別オブジェクトを作成する
	 */
	public SettingCustomTexture clone() {
		Map<EnumTextureParts,SettingData> newset = new HashMap<EnumTextureParts,SettingData>();
		for(EnumTextureParts parts : EnumTextureParts.values()){
			if (setting.containsKey(parts)){
				SettingData data = setting.get(parts);
				newset.put(parts, new SettingData(data.getName(),data.getWidth(),data.getHeight(),data.getXOffset(),data.getYOffset()));
			}
		}
		SettingCustomTexture ret = new SettingCustomTexture(this.fileName,newset);
		return ret;
	}
}
