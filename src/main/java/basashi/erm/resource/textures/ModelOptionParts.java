package basashi.erm.resource.textures;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import basashi.erm.core.Mod_ERM;
import basashi.erm.util.Contena2i;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelOptionParts {
	// 対象モブ
	private Class target;
	// ボックスリスト
	private List<ModelRenderer> boxies = new ArrayList<ModelRenderer>();

	// コンストラクタ
	public ModelOptionParts(OptionPartsInfoList partsInfo, Class target){
		this.target = target;
		// ベースとなるモデルを取得
		ModelBase baseModel = Mod_ERM.getModel(target);
		for (OptionPartsInfo info : partsInfo.getPartsInfo()){
			ModelRenderer newBox = new ModelRenderer(baseModel,info.getName());
			newBox.setTextureOffset(info.TexX(),info.TexY());
			newBox.addBox(info.getBoxOffset().getX(), info.getBoxOffset().getY(), info.getBoxOffset().getZ(),
							info.getBoxSize().getX(), info.getBoxSize().getY(), info.getBoxSize().getZ());
			newBox.setRotationPoint(info.getBoxCenter().getX(), info.getBoxCenter().getY(), info.getBoxCenter().getZ());
			newBox.rotateAngleX = info.getRotation().getX();
			newBox.rotateAngleY = info.getRotation().getY();
			newBox.rotateAngleZ = info.getRotation().getZ();
			// リストに登録
			boxies.add(newBox);
		}
	}

	// パーツ描画
	public void rendere(float f5){
		Iterator<ModelRenderer> i = boxies.iterator();
		while(i.hasNext()){
			((ModelRenderer)i.next()).render(f5);
		}
	}

	// テクスチャのサイズを設定
	public void setTextureSize(int x, int y){
		Iterator<ModelRenderer> i = boxies.iterator();
		while(i.hasNext()){
			((ModelRenderer)i.next()).setTextureSize(x,y);
		}
	}

	// テクスチャインデックスを設定
	public void setTextureIndex(List<Contena2i> offset){
		for (int i = 0; i < boxies.size(); i++){
			boxies.get(i).setTextureOffset(offset.get(i).getX(), offset.get(i).getY());
		}
	}
}
