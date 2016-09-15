package basashi.erm.resource.parts;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import basashi.erm.core.Mod_ERM;
import net.minecraft.util.math.Vec3i;

public class ERMDressTextureInfo implements IERMTextureInfo {
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
	private ERMDressParts kind = null;
	// テクスチャのサイズ
	private Vec3i texSize;
	// テクスチャイメージ
	BufferedImage img;

	public ERMDressTextureInfo(String path, InputStream istream) throws Exception{
		try {
			String[] tex = path.split("/");
			fileName = tex[tex.length-1];
			String[] work = fileName.split("_");
			if (work.length >= 3){
				//ベーステクスチャ(ターゲットのモデル名_部位名称_テクスチャ自体の名称)
				// モデル名を確認
				if ((targetEntity=Mod_ERM.checkEntityName(work[0])) == null){
					throw new IllegalArgumentException();
				}
				// 部位名称を確認
				if (!checkPartsh(work[1])){
					throw new IllegalArgumentException();
				}
				modelName = work[0];
				prefix = work[1];
				StringBuilder tname = new StringBuilder();
				for (int i = 2; i < work.length; i++){
					tname.append(work[i]);
				}
				textureName = tname.toString().replace(".png", "");
			}else{
				// オプションテクスチャ
				modelName = "";
				kind =  ERMDressParts.OPT1;
				prefix = kind.getKind();
				textureName = path.replace(".png", "");
			}

			// サイズの取得
			img = ImageIO.read(istream);
			texSize = new Vec3i(img.getWidth(),img.getHeight(),0);
		} catch (IOException e) {
			throw e;
		}

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
		return kind.getKind();
	}

	// テクスチャの部位を返す
	public ERMDressParts KindE(){
		return kind;
	}

	// テクスチャの名称を返す
	public String Name(){
		return textureName;
	}

	// テクスチャのサイズを返す
	public Vec3i Size(){
		return texSize;
	}

	// テクスチャの画像を返す
	public BufferedImage Image(){
		return img;
	}

	// テクスチャの部位を取得
	private boolean checkPartsh(String partsName){
		boolean check = false;
		for(ERMDressParts dress : ERMDressParts.values()){
			if(dress.getKind().toLowerCase().equals(partsName.toLowerCase())){
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
		resInf.append(kind.getKind());
		resInf.append("\t");
		resInf.append(kind.name());
		resInf.append("\t");
		resInf.append(texSize.getX());
		resInf.append("\t");
		resInf.append(texSize.getY());
		return resInf.toString();
	}


}
