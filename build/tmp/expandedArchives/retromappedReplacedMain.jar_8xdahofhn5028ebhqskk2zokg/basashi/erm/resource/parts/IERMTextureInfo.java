package basashi.erm.resource.parts;

import java.awt.image.BufferedImage;

import net.minecraft.util.math.Vec3i;

public interface IERMTextureInfo {
	// テクスチャのターゲットを返す
	Class Target();
	// テクスチャ部位
	String Kind();
	// テクスチャの名称を返す
	String Name();
    // テクスチャのファイルを返す
	String FileName();
	// テクスチャのサイズを返す
	Vec3i Size();
	// イメージを返す
	BufferedImage Image();
}
