package basashi.erm.gui;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import basashi.erm.model.IModelCaps;
import basashi.erm.model.ModelCapsHelper;
import basashi.erm.model.ModelERMBase;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class TextureBox extends TextureBoxBase {

public String packageName;
public Map<Integer,ResourceLocation> dresses;
public Map<String,Map<Integer,ResourceLocation>> armors;
public String modelName;
public ModelERMBase[] models;
public String[] textureDir;
public String fileName;


public TextureBox(){
	dresses = new HashMap<Integer,ResourceLocation>();
	armors = new TreeMap<String, Map<Integer, ResourceLocation>>();
	modelHieght = modelWidth = modelYOffset = modelMountedYOffset = 0.0F;
}

public TextureBox(String textureName, String[] search){
	this();
	this.textureName = textureName;
	fileName = textureName;
	int idx = textureName.lastIndexOf("_");
	if (idx > -1){
		packageName = textureName.substring(0,idx);
		modelName = textureName.substring(idx+1);
	}else{
		packageName = textureName;
		modelName = "";
	}
	textureDir = search;
}

public void setModels(String modelName, ModelERMBase[] models, ModelERMBase[] defModels){
	this.modelName = modelName;
	this.models = models == null ? defModels:models;
	textureName = (new StringBuilder()).append(packageName).append("_").append(modelName).toString();
	isUpdateSize = (models != null && this.models[0] != null) ? ModelCapsHelper.getCapsValueBoolean(this.models[0],IModelCaps.caps_isUpdateSize) : false;
}

public ResourceLocation getTextureName(int index){
	if(dresses.containsKey(index)){
		return dresses.get(index);
	}
	return null;
}

public ResourceLocation getArmorTextureName(int index, ItemStack itemstack){
	if ( itemstack == null == !(itemstack.getItem() instanceof ItemArmor)) return null;
	int renderIndex = ((ItemArmor)itemstack.getItem()).renderIndex;
	int l = 0;
	if (itemstack.getMaxDamage() > 0){
		l = (10 * itemstack.getItemDamage() / itemstack.getMaxDamage());
	}

	if (armors.isEmpty() || itemstack == null) return null;
	if(!(itemstack.getItem() instanceof ItemArmor)) return null;

//	if (renderIndex >= ERMResourceManager.armorFilenamePrefix.length && ERMResourceManager.armorFilenamePrefix.length > 0){
//		renderIndex = renderIndex % ERMResourceManager.armorFilenamePrefix.length;
//	}
//	return getArmorTextureName(index, ERMResourceManager.armorFilenamePrefix[renderIndex],1);
	return null;
}

public boolean hasArmor(){
	return !armors.isEmpty();
}

@Override
public float getHeight(IModelCaps pEntityCaps){
	return models != null ? models[0].getHeight(pEntityCaps) : modelHieght;
}

@Override
public float getWidth(IModelCaps pEntityCaps){
	return models != null ? models[0].getWidth(pEntityCaps) : modelWidth;
}

@Override
public float getYOffset(IModelCaps pEntityCaps){
	return models != null ? models[0].getYOffset(pEntityCaps) : modelYOffset;
}

@Override
public float getMountedYOffset(IModelCaps pEntityCaps){
	return models != null ? models[0].getMountedYOffset(pEntityCaps) : modelMountedYOffset;
}

public TextureBox duplicate(){
	TextureBox box = new TextureBox();
	box.textureName = textureName;
	box.packageName = packageName;
	box.fileName = fileName;
	box.modelName = modelName;
	box.textureDir = textureDir;
	box.dresses = dresses;
	box.armors = armors;
	box.models = models;
	box.isUpdateSize = box.isUpdateSize;
	return box;
}

public boolean addTexture(int index, String Location){
	String ls = "/assets/minecraft/";
	if (Location.startsWith(ls)){
		Location = Location.substring(ls.length());
	}
	boolean lflag = false;
//	switch((index & 0xFFF0)){
//	case ERMResourceManager.tx_armor1:
//	case ERMResourceManager.tx_armor2:
//	case ERMResourceManager.tx_armor1light:
//	case ERMResourceManager.tx_armor2light:
//	case ERMResourceManager.tx_oldarmor1:
//	case ERMResourceManager.tx_oldarmor2:
//		ls = Location.substring(Location.lastIndexOf("/") + 1, Location.lastIndexOf("_"));
//		Map<Integer, ResourceLocation> lmap;
//		if (armors.containsKey(ls)){
//			lmap = armors.get(ls);
//		}else{
//			lmap = new HashMap<Integer, ResourceLocation>();
//			armors.put(ls,lmap);
//		}
//		lmap.put(index,new ResourceLocation(Location));
//		break;
//	default:
//		dresses.put(index, new ResourceLocation(Location));
//		return true;
//	}
	return lflag;
}
}
