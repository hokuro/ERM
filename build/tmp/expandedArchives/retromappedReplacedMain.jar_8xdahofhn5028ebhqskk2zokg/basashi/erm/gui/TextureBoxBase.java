package basashi.erm.gui;

import basashi.erm.model.IModelCaps;

public abstract class TextureBoxBase {
	public String textureName;
	protected float modelHieght;
	protected float modelWidth;
	protected float modelYOffset;
	protected float modelMountedYOffset;
	protected boolean isUpdateSize;

	public void setModelSize(float height, float widht, float yoffset, float mountyoffset){
		modelHieght = height;
		modelWidth = widht;
		modelYOffset = yoffset;
		modelMountedYOffset = mountyoffset;
	}

	public float getHeight(IModelCaps pEntityCaps){
		return modelHieght;
	}
	public float getHeight(){
		return getHeight(null);
	}

	public float getWidth(IModelCaps pEntityCaps){
		return modelWidth;
	}
	public float getWidth(){
		return getWidth(null);
	}

	public float getYOffset(IModelCaps pEntityCaps){
		return modelYOffset;
	}
	public float getYOffset(){
		return getYOffset(null);
	}

	public float getMountedYOffset(IModelCaps pEntityCaps){
		return modelMountedYOffset;
	}

	public float getMountedYOffset(){
		return getMountedYOffset(null);
	}
}
