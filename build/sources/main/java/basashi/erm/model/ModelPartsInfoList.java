package basashi.erm.model;

import java.util.ArrayList;
import java.util.List;

import basashi.erm.util.Util;

public class ModelPartsInfoList {
	// テクスチャファイル名
	private String textureFileName;
	// モデルパーツ情報
	private List<ModelPartsInfo> info;

	// コンストラクタ
	public ModelPartsInfoList(){
		textureFileName = null;
		info = new ArrayList();
	}

	// テクスチャファイル名を設定
	public void setTexture(String texture)throws IllegalArgumentException{
		if (Util.StringisEmptyOrNull(texture)){throw new IllegalArgumentException();}
		textureFileName = texture;
	}
	// テクスチャファイル名を取得する
	public String getTexture(){
		return textureFileName;
	}

	// パーツを追加する
	public void AddParts(ModelPartsInfo value){
		info.add(value);
	}
	// パーツのリストを取得する
	public List<ModelPartsInfo> getPartsInfo(){
		return info;
	}
}
