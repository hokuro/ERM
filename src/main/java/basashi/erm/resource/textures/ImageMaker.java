package basashi.erm.resource.textures;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

public class ImageMaker {
	// ドレス用画像を作成して専用フォルダに保存する
	public static boolean makeDressImage(File savePath, Map<EnumTextureParts,TextureInfo> texture, SettingCustomTexture info) throws Exception{
		/* ベースの順番
		 * 1 Body
		 * 2 Eye
		 * 3 Dress
		 * 4 Head
		 * 5 Option1
		 * 6 Option2
		 * 7 Option3
		 * 8 Option4
		 * 9 Option5
		 */
		BufferedImage imgBody;
		BufferedImage imageEye;
		BufferedImage imageDress;
		BufferedImage imageHead;
		List<BufferedImage> imageOpt = new ArrayList<BufferedImage>();
		// ベース用テクスチャの読み込み
		imgBody = texture.get(EnumTextureParts.BODY).Image();
		imageEye = texture.get(EnumTextureParts.EYE).Image();
		Transparent(imageEye, imageEye.getRGB(0, 0));

		imageDress = texture.get(EnumTextureParts.DRESS).Image();
		Transparent(imageDress, imageDress.getRGB(0, 0));

		imageHead = texture.get(EnumTextureParts.HEAD).Image();
		Transparent(imageHead, imageHead.getRGB(0, 0));

		int texWidth = imgBody.getWidth();
		int texHeight = imgBody.getHeight();
		// オプション用テクスチャの読み込み
		if (texture.containsKey(EnumTextureParts.OPT1)){
			BufferedImage img = texture.get(EnumTextureParts.OPT1).Image();
			imageOpt.add(img);
			texWidth += img.getWidth();
			if (img.getHeight() >= texHeight){
				texHeight = img.getHeight();
			}
		}
		if (texture.containsKey(EnumTextureParts.OPT2)){
			BufferedImage img = texture.get(EnumTextureParts.OPT2).Image();
			imageOpt.add(img);
			texWidth += img.getWidth();
			if (img.getHeight() >= texHeight){
				texHeight = img.getHeight();
			}
		}
		if (texture.containsKey(EnumTextureParts.OPT3)){
			BufferedImage img = texture.get(EnumTextureParts.OPT3).Image();
			imageOpt.add(img);
			texWidth += img.getWidth();
			if (img.getHeight() >= texHeight){
				texHeight = img.getHeight();
			}
		}
		if (texture.containsKey(EnumTextureParts.OPT4)){
			BufferedImage img = texture.get(EnumTextureParts.OPT4).Image();
			imageOpt.add(img);
			texWidth += img.getWidth();
			if (img.getHeight() >= texHeight){
				texHeight = img.getHeight();
			}
		}
		if (texture.containsKey(EnumTextureParts.OPT5)){
			BufferedImage img = texture.get(EnumTextureParts.OPT5).Image();
			imageOpt.add(img);
			texWidth += img.getWidth();
			if (img.getHeight() >= texHeight){
				texHeight = img.getHeight();
			}
		}

		// 画像の作成
		BufferedImage newTexture = new BufferedImage(texWidth, texHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics gl = newTexture.createGraphics();
		// 基本部分描画
		gl.drawImage(imgBody, info.getInformation(EnumTextureParts.BODY).getXOffset(), info.getInformation(EnumTextureParts.BODY).getYOffset(), null);	    // body 描画
		gl.drawImage(imageEye, info.getInformation(EnumTextureParts.EYE).getXOffset(), info.getInformation(EnumTextureParts.EYE).getYOffset(), null);         // Eye 描画
		gl.drawImage(imageDress, info.getInformation(EnumTextureParts.DRESS).getXOffset(), info.getInformation(EnumTextureParts.DRESS).getYOffset(), null);	// dress 描画
		gl.drawImage(imageHead, info.getInformation(EnumTextureParts.HEAD).getXOffset(), info.getInformation(EnumTextureParts.HEAD).getYOffset(), null);	    // hair 描画
		// オプション部描画
		int offsetX = imgBody.getWidth();
		for (BufferedImage img : imageOpt){
			gl.drawImage(img, offsetX, 0, null);
			offsetX += img.getWidth();
		}
		gl.dispose();

		// 画像出力
		ImageIO.write(newTexture, "png", savePath);
		return true;
	}

    // 透明にする
    private static void Transparent(BufferedImage img, int c)
    {
        int w = img.getWidth();     // Image の幅
        int h = img.getHeight();    // Image の高さ

        //RGB値を0(透明色)に置換
        for(int y = 0; y < h; y++)
        {   for(int x = 0; x < w; x++)
            {   if (img.getRGB(x,y)==c) img.setRGB(x,y,0);  }
        }
    }
}
