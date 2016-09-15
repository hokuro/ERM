package basashi.erm.resource.textures;

import basashi.erm.util.Contena3f;
import basashi.erm.util.Contena3i;

public class OptionPartsInfo {
	private String name;
	private int tex_x;
	private int tex_y;
	private Contena3i boxSize;
	private Contena3f boxOffset;
	private Contena3f boxCenter;
	private Contena3f rotation;


	public OptionPartsInfo(String name,
							int tex_x, int tex_y,
							int box_h, int box_w, int box_d,
							float box_x, float box_y, float box_z,
							float oft_x, float oft_y, float oft_z,
							float rot_x, float rot_y, float rot_z){
		this.name = name;
		this.tex_x = tex_x;
		this.tex_y = tex_y;
		boxSize = new Contena3i(box_h,box_w,box_d);
		boxOffset = new Contena3f(oft_x,oft_y,oft_z);
		boxCenter = new Contena3f(box_x,box_y,box_z);
		rotation = new Contena3f(rot_x,rot_y,rot_z);
	}

	// ボックス名を取得
	public String getName(){return name;}

	// テクスチャX座標を取得
	public int TexX(){
		return tex_x;
	}
	// テクスチャY座標を取得
	public int TexY(){
		return tex_y;
	}

	// テクスチャ座標を設定
	public void setTexturePos(int x, int y){
		tex_x = x;
		tex_y = y;
	}

	// ボックスサイズを取得
	public Contena3i getBoxSize(){
		return boxSize;
	}

	// ボックス位置を取得
	public Contena3f getBoxOffset(){
		return boxOffset;
	}

	// 回転の中心を取得
	public Contena3f getBoxCenter(){
		return boxCenter;
	}

	// 回転を取得
	public Contena3f getRotation(){
		return this.rotation;
	}

	/**************************************/
	/** デバッグ用                                                                   **/
	/**************************************/
	public String toString(){
		StringBuilder pInf = new StringBuilder();
		pInf.append(name);
		pInf.append("\t");
		pInf.append(tex_x);
		pInf.append("\t");
		pInf.append(tex_y);
		pInf.append("\t");
		pInf.append(boxSize.getX());
		pInf.append("\t");
		pInf.append(boxSize.getY());
		pInf.append("\t");
		pInf.append(boxSize.getZ());
		pInf.append("\t");
		pInf.append(boxOffset.getX());
		pInf.append("\t");
		pInf.append(boxOffset.getY());
		pInf.append("\t");
		pInf.append(boxOffset.getZ());
		pInf.append("\t");
		pInf.append(boxCenter.getX());
		pInf.append("\t");
		pInf.append(boxCenter.getY());
		pInf.append("\t");
		pInf.append(boxCenter.getZ());
		pInf.append("\t");
		pInf.append(rotation.getX());
		pInf.append("\t");
		pInf.append(rotation.getY());
		pInf.append("\t");
		pInf.append(rotation.getZ());
		return pInf.toString();
	}

}
