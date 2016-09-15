package basashi.erm.model;

import net.minecraft.util.math.Vec3d;

public class ModelPartsInfo {
	private String name;
	private Vec3d texturePos;
	private Vec3d boxSize;
	private Vec3d boxPos;
	private Vec3d rotationPos;
	private Vec3d rotation;

	public ModelPartsInfo(String name,
							int tex_x, int tex_y,
							int box_h, int box_w, int box_d,
							float box_x, float box_y, float box_z,
							float oft_x, float oft_y, float oft_z,
							float rot_x, float rot_y, float rot_z){
		this.name = name;
		texturePos = new Vec3d(tex_x,tex_y,0);
		boxSize = new Vec3d(box_h,box_w,box_d);
		boxPos = new Vec3d(oft_x,oft_y,oft_z);
		rotationPos = new Vec3d(box_x,box_y,box_z);
		rotation = new Vec3d(rot_x,rot_y,rot_z);
	}

	// ボックス名を取得
	public String getName(){return name;}

	// テクスチャ座標を取得
	public Vec3d getTexturePos(){
		return texturePos;
	}

	// テクスチャ座標を設定
	public void setTexturePos(int x, int y){
		texturePos = new Vec3d(x,y,0);
	}

	// ボックスサイズを取得
	public Vec3d getBoxSize(){
		return boxSize;
	}

	// ボックス位置を取得
	public Vec3d getBoxPos(){
		return boxPos;
	}

	// 回転の中心を取得
	public Vec3d getRotationPos(){
		return rotationPos;
	}

	// 回転を取得
	public Vec3d getRotation(){
		return this.rotation;
	}

}
