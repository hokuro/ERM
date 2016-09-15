package basashi.erm.resource.textures;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import basashi.erm.core.Mod_ERM;
import basashi.erm.util.Contena2i;

public class ModelOptionPartsList {
	private static Gson gson_modelParts = new GsonBuilder().registerTypeAdapter(OptionPartsInfoList.class, new JsonSerializer_ModelOptionParts()).create();

	// ファイル名
	private String fileName;
	// ターゲットのクラス
	private Class targetClass;
	// パーツの情報
	private OptionPartsInfoList partsInfo;
	// モデルパーツ
	private ModelOptionParts model = null;

	public ModelOptionPartsList(String path, InputStream istream) throws Exception{
		String[] divpath = path.split("/");
		this.fileName = divpath[divpath.length-1];

		// パーツファイル名規則    モデル名_ファイル名.txt
		String[] work = this.fileName.split("_");
		if (work.length < 2){throw new IllegalArgumentException();}
		// エンティティの確認
		if ((targetClass=Mod_ERM.checkEntityName(work[0]))==null){throw new IllegalArgumentException();}

		try{
			// パーツ情報を取り出す
			partsInfo = readModelParts(istream);
			// ファイル名のターゲット名以外の部分をパーツ名として設定する
			partsInfo.setPartsName(this.fileName);
			// モデルを作る
			this.makeModel();
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
	public ModelOptionParts getParts(int texw, int texh, List<Contena2i> tex_idx){
		if ( model == null){this.makeModel();}
		this.setTextureSize(texw, texh);
		this.setTextureIndex(tex_idx);
		return model;
	}

	// パーツを描画
	public void render(float f5){
		if (model != null){
			model.rendere(f5);
		}
	}

	// Jsonからパーツの情報を読み込む
	private OptionPartsInfoList readModelParts(InputStream parts) throws Exception{
		InputStreamReader stream = new InputStreamReader(parts);
		OptionPartsInfoList list;
		try {
			 list = gson_modelParts.fromJson(stream, OptionPartsInfoList.class);
		}catch(Exception ex){
			throw ex;
		}finally{
			try {
				stream.close();
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}
		return list;
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
	public EnumTextureParts getTextureKind(){
		return partsInfo.getTextureKind();
	}

	// ターゲットのクラスを返す
	public Class Target(){
		return this.targetClass;
	}

	// パーツとテクスチャを紐づける
	public void registtexture(EnumTextureParts kind){
		 partsInfo.setTextureKind(kind);
	}

	// テクスチャのサイズを設定
	public void setTextureSize(int x, int y){
		if (model != null){
			model.setTextureSize(x, y);
		}
	}

	// テクスチャインデックスを設定
	public void setTextureIndex(List<Contena2i> index){
		if (model != null){
			model.setTextureIndex(index);
		}
	}

	// テクスチャの位置を更新する
	public void setTextureOffset(int x, int y){
		if (model != null){
			List<OptionPartsInfo> parts = partsInfo.getPartsInfo();
			List<Contena2i> index = new ArrayList<Contena2i>();
			for (int i = 0; i < parts.size(); i++){
				// テクスチャのインデックス情報を作成
				index.add(new Contena2i(x+parts.get(i).TexX(),y+parts.get(i).TexY()));
			}
			setTextureIndex(index);
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
