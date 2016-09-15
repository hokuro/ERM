package basashi.erm.resource.parts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import basashi.erm.core.Mod_ERM;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.math.Vec3i;

public class ModelOptionParts {
	// 対象モブ
	private Class target;
	// ボックスリスト
	private List<ModelRenderer> boxies = new ArrayList<ModelRenderer>();

	// コンストラクタ
	public ModelOptionParts(ModelPartsInfoList partsInfo, Class target){
		this.target = target;
		// ベースとなるモデルを取得
		ModelBase baseModel = Mod_ERM.getModel(target);
		for (ModelPartsInfo info : partsInfo.getPartsInfo()){
			ModelRenderer newBox = new ModelRenderer(baseModel,info.TexX(),info.TexY());
			newBox.func_78789_a(info.getBoxPos().getX(), info.getBoxPos().getY(), info.getBoxPos().getZ(),
							info.getBoxSize().getX(), info.getBoxSize().getY(), info.getBoxSize().getZ());
			newBox.func_78793_a(info.getRotationPos().getX(), info.getRotationPos().getY(), info.getRotationPos().getZ());
			newBox.field_78795_f = info.getRotation().getX();
			newBox.field_78796_g = info.getRotation().getY();
			newBox.field_78808_h = info.getRotation().getZ();
			// リストに登録
			boxies.add(newBox);
		}
	}

	// パーツ描画
	public void rendere(float f5){
		Iterator<ModelRenderer> i = boxies.iterator();
		while(i.hasNext()){
			((ModelRenderer)i.next()).func_78785_a(f5);
		}
	}

	// テクスチャのサイズを設定
	public void setTextureSize(int x, int y){
		Iterator<ModelRenderer> i = boxies.iterator();
		while(i.hasNext()){
			((ModelRenderer)i.next()).func_78787_b(x,y);
		}
	}

	// テクスチャインデックスを設定
	public void setTextureIndex(List<Vec3i> texture){
		Iterator<ModelRenderer> i = boxies.iterator();
		Iterator<Vec3i> ii = texture.iterator();
		while(i.hasNext() && ii.hasNext()){
			Vec3i xy = ii.next();
			((ModelRenderer)i.next()).func_78784_a(xy.func_177958_n(),xy.func_177956_o());
		}
	}
}
