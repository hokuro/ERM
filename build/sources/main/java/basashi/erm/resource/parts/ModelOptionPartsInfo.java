package basashi.erm.resource.parts;

import java.io.InputStream;
import java.util.List;

import basashi.erm.core.Mod_ERM;
import basashi.erm.util.Util;
import net.minecraft.util.math.Vec3i;

public class ModelOptionPartsInfo {
	// ファイル名
	private String fileName;
	// ターゲットのクラス
	private Class targetClass;
	// パーツの情報
	private ModelPartsInfoList partsInfo;
	// モデルパーツ
	private ModelOptionParts model = null;

	public ModelOptionPartsInfo(String path, InputStream istream) throws Exception{
		String[] divpath = path.split("/");
		this.fileName = divpath[divpath.length-1];

		// パーツファイル名規則    モデル名_ファイル名.txt
		String[] work = this.fileName.split("_");
		if (work.length < 2){throw new IllegalArgumentException();}
		// エンティティの確認
		if ((targetClass=Mod_ERM.checkEntityName(work[0]))==null){throw new IllegalArgumentException();}

		try{
			// パーツ情報を取り出す
			partsInfo = Util.readModelParts(istream);
			// ファイル名のターゲット名以外の部分をパーツ名として設定する
			partsInfo.setPartsName(this.fileName.substring(fileName.indexOf("_")+1).replace(".json",""));
		}catch(Exception ex){
			ex.printStackTrace();
			throw ex;
		}
	}

	// ファイル名を取得
	public String FineName(){
		return this.fileName;
	}

	// モデルパーツを取得
	public ModelOptionParts getParts(){
		if ( model == null){this.makeModel();}
		return model;
	}

	// モデルパーツを取得
	public ModelOptionParts getParts(int texw, int texh, List<Vec3i> tex_idx){
		if ( model == null){this.makeModel();}
		this.setTextureSize(texw, texh);
		this.setTextureIndex(tex_idx);
		return model;
	}

	// パーツを描画
	public void render(float f5){
		if (model == null){
			model.rendere(f5);
		}
	}

	/*********************************************************/
	/** モデルパーツ情報のトンネル用                        **/
	/*********************************************************/
	// モデルパーツ名を取得
	public String getName(){
		return partsInfo.getName();
	}

	// テクスチャ名を取得
	public String getTextureNmae(){
		return partsInfo.getTexture();
	}

	// テクスチャ部位を取得
	public ERMDressParts getTextureKind(){
		return partsInfo.getTextureKind();
	}

	// パーツとテクスチャを紐づける
	public void registtexture(ERMDressParts kind){
		 partsInfo.setTextureKind(kind);
	}

	// テクスチャのサイズを設定
	public void setTextureSize(int x, int y){
		if (model == null){
			model.setTextureSize(x, y);
		}
	}

	// テクスチャインデックスを設定
	public void setTextureIndex(List<Vec3i> texture){
		if (model == null){
			model.setTextureIndex(texture);
		}
	}

	// モデルを作る
	private void makeModel(){
		model = new ModelOptionParts(partsInfo,targetClass);
	}


	/*******************************/
	/** デバッグ用                                                 **/
	/*******************************/
	public String toString(){
		StringBuilder pInf = new StringBuilder();
		pInf.append(this.fileName);
		pInf.append("\t");
		pInf.append(targetClass.getName());
		pInf.append("\t");
		pInf.append(partsInfo.toString());
		return pInf.toString();
	}
}
