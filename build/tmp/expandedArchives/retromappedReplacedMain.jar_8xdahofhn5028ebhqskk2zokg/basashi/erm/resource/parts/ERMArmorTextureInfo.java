package basashi.erm.resource.parts;

import java.awt.image.BufferedImage;
import java.io.File;

import net.minecraft.util.math.Vec3i;

public class ERMArmorTextureInfo implements IERMTextureInfo {
	// モデル名
	private String modelName;
	// テクスチャ名
	private String textureName;
	// 部位名
	private String prefix;
	// テクスチャファイルパス
	private File texture;
	// 追加パーツ情報
	private File parts;

	// ターゲットのクラス
	private Class targetEntity = null;
	// テクスチャ部位
	private ERMArmorParts kind = null;
	// 追加パーツがあるかどうか
	private boolean findParts = false;
	// テクスチャのサイズ
	private Vec3i texSize;
	// テクスチャイメージ
	BufferedImage img;


	@Override
	public Class Target() {
		return targetEntity;
	}

	@Override
	public String Kind() {
		return kind.getKind();
	}

	@Override
	public String Name() {
		return textureName;
	}

	@Override
	public String FileName() {
		return texture.getName();
	}


	@Override
	public Vec3i Size() {
		return texSize;
	}

	@Override
	public BufferedImage Image(){
		return img;
	}

}
