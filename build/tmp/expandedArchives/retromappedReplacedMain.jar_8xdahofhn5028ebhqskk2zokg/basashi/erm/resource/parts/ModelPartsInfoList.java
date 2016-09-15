package basashi.erm.resource.parts;

import java.util.ArrayList;
import java.util.List;

import basashi.erm.util.Util;

public class ModelPartsInfoList {
	// パーツ名
	private String partsName;
	// テクスチャファイル名
	private String textureFileName;
	// パーツに使用するテクスチャの部位
	private ERMDressParts kind;
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
		// 拡張子がない場合追加する
		if (texture.endsWith(".png")){textureFileName = texture;
		}else{textureFileName = texture + ".png";}
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

	// パーツ名を設定する
	public void setPartsName(String name){
		this.partsName = name;
	}
	// パーツ名を取得する
	public String getName(){
		return this.partsName;
	}

	// テクスチャー部位を設定する
	public void setTextureKind(ERMDressParts kind){
		this.kind = kind;
	}

	// テクスチャ部位を取得する
	public ERMDressParts getTextureKind(){
		return this.kind;
	}


	/*****************************************/
	/** デバッグ用                          **/
	/*****************************************/
	public String toString(){
		StringBuilder pInf = new StringBuilder();
		pInf.append(this.partsName);
		pInf.append("\t");
		pInf.append(textureFileName);
		pInf.append("\t");
		pInf.append(this.kind.name());
		pInf.append("\t");
		for(ModelPartsInfo i : info){
			pInf.append(i.toString());
			pInf.append("\t");
		}
		return pInf.toString();
	}
}
