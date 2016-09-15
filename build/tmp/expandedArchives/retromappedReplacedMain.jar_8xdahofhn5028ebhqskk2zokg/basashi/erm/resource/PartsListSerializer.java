package basashi.erm.resource;

import java.lang.reflect.Type;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import basashi.erm.resource.parts.ModelPartsInfo;
import basashi.erm.resource.parts.ModelPartsInfoList;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class PartsListSerializer implements JsonDeserializer<ModelPartsInfoList> {

	@Override
	public ModelPartsInfoList deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		// TODO 自動生成されたメソッド・スタブ
		// テクスチャ名を取得
		JsonObject jsonobject = JsonUtils.func_151210_l(json, "entry");

		// 追加パーツ情報を作成
		ModelPartsInfoList result = new ModelPartsInfoList();
		// 追加パーツのテクスチャ名を設定
		result.setTexture(JsonUtils.func_151200_h(jsonobject, "texture"));

		// ボックス情報を取得
		boolean flag = true;
		int boxnum = 1;
		while(flag){
			JsonObject box = null;
			try{
				// box+番号で検索(連番になっていないボックスは読み込まれない)
				box = JsonUtils.func_152754_s(jsonobject, "box"+boxnum);

				// テクスチャ位置
				JsonArray warray = JsonUtils.func_151214_t(box,"tex_pos");
				// ボックスサイズ
				JsonArray warray2 = JsonUtils.func_151214_t(box,"box_siz");
				// ボックス位置
				JsonArray warray3 = JsonUtils.func_151214_t(box,"box_pos");
				// 回転座標
				JsonArray warray4 = JsonUtils.func_151214_t(box,"rot_pos");
				// 回転
				JsonArray warray5 = JsonUtils.func_151214_t(box,"rotation");
				// オブジェクトを作成
				ModelPartsInfo newparts = new ModelPartsInfo(
						JsonUtils.func_151200_h(box,"boxname"),  												// ボックス名
						warray.get(0).getAsInt(), warray.get(1).getAsInt(),										// テクスチャ座標
						warray2.get(0).getAsInt(), warray2.get(1).getAsInt(), warray2.get(2).getAsInt(), 		// ボックスサイズ
						warray3.get(0).getAsFloat(), warray3.get(1).getAsFloat(), warray3.get(2).getAsFloat(),	// ボックスオフセット
						warray4.get(0).getAsFloat(), warray4.get(1).getAsFloat(), warray4.get(2).getAsFloat(),	// ボックス回転位置
						warray5.get(0).getAsFloat(), warray5.get(1).getAsFloat(), warray5.get(2).getAsFloat()	// ボックス回転
						);
				result.AddParts(newparts);
				boxnum ++;
			}catch(JsonSyntaxException ex){
				// ボックスがひとつもない物は読み込まない
				if (boxnum == 1){throw ex;}
				// ボックス検索終了
				flag = false;
			}
		}
		return result;
	}
}