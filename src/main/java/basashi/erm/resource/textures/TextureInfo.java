package basashi.erm.resource.textures;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import basashi.erm.core.Mod_ERM;
import basashi.erm.util.Contena2i;

public class TextureInfo {
	// ファイル名
	private String fileName;
	// モデル名
	private String modelName;
	// テクスチャ名
	private String textureName;
	// 部位名
	private String prefix;
	// ターゲットのクラス
	private Class targetEntity = null;
	// テクスチャ部位
	private EnumTextureParts kind = null;
	// テクスチャのサイズ
	private Contena2i texSize;
	// テクスチャイメージ
	BufferedImage img;

	public TextureInfo(String path, InputStream istream) throws Exception{
		try {
			String[] tex = path.split("/");
			fileName = tex[tex.length-1];
			String[] work = fileName.split("_");
			if (work.length >= 3){
				//ベーステクスチャ(ターゲットのモデル名_部位名称_テクスチャ自体の名称)
				// モデル名を確認
				if ((targetEntity=Mod_ERM.checkEntityName(work[0])) == null){
					optionAdd(path);
				}
				// 部位名称を確認
				if (!checkPartsh(work[1])){
					optionAdd(path);
				}
				modelName = work[0];
				prefix = work[1];
				StringBuilder tname = new StringBuilder();
				for (int i = 2; i < work.length; i++){
					tname.append(work[i]);
				}
				textureName = tname.toString().replace(".png", "");
			}else{
				optionAdd(path);
			}

			// サイズの取得
			img = ImageIO.read(istream);
			texSize = new Contena2i(img.getWidth(),img.getHeight());
		} catch (IOException e) {
			throw e;
		}

	}

	// フォーマットが合わないテクスチャをオプション用として設定する
	private void optionAdd(String path){
		// オプションテクスチャ
		modelName = "";
		kind =  EnumTextureParts.OPT1;
		prefix = kind.getRegion();
		textureName = path.replace(".png", "");
	}

    // テクスチャのファイル名を返す
	public String FileName(){
		return this.fileName;
	}

	// テクスチャのターゲットを返す
	public Class Target(){
		return targetEntity;
	}

	// テクスチャの部位を返す
	public String Kind(){
		return kind.getRegion();
	}

	// テクスチャの部位を返す
	public EnumTextureParts KindE(){
		return kind;
	}

	// テクスチャの名称を返す
	public String Name(){
		return textureName;
	}

	// テクスチャのサイズを返す
	public Contena2i Size(){
		return texSize;
	}

	// テクスチャの画像を返す
	public BufferedImage Image(){
		return img;
	}

	// テクスチャの部位を取得
	private boolean checkPartsh(String partsName){
		boolean check = false;
		for(EnumTextureParts dress : EnumTextureParts.values()){
			if(dress.getRegion().toLowerCase().equals(partsName.toLowerCase())){
				kind = dress;
				check = true;
				break;
			}
		}
		return check;
	}

	/***************************/
	/** デバッグ用            **/
	/***************************/
	public String toString(){
		StringBuilder resInf = new StringBuilder();
		resInf.append(this.fileName);
		resInf.append("\t");
		resInf.append(modelName);
		resInf.append("\t");
		resInf.append(textureName);
		resInf.append("\t");
		resInf.append(prefix);
		resInf.append("\t");
		resInf.append(targetEntity.getName());
		resInf.append("\t");
		resInf.append(kind.getRegion());
		resInf.append("\t");
		resInf.append(kind.name());
		resInf.append("\t");
		resInf.append(texSize.getX());
		resInf.append("\t");
		resInf.append(texSize.getY());
		return resInf.toString();
	}


}
