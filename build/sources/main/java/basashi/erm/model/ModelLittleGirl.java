package basashi.erm.model;
//Date: 平成 28/5/24 21:44:01
//Template version 1.1
//Java generated by Techne
//Keep in mind that you still need to fill in some blanks
//- ZeuX

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import basashi.erm.resource.parts.ModelOptionPartsInfo;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelLittleGirl extends ModelBase
{
	// 描画フラグ (念のため)
	public static boolean isflag = true;
	public boolean hideSideShortL = false;
	public boolean hideSideShortR = false;
	public boolean hideSideLongL = false;
	public boolean hideSideLongR = false;
	public boolean hideTailLong = false;
	public boolean hideTailShort = false;
	public boolean hideBackHair = false;

	//fields
 ModelRenderer bottom;
 ModelRenderer head;
 ModelRenderer body;
 ModelRenderer rightArm;
 ModelRenderer rightLeg;
 ModelRenderer leftArm;
 ModelRenderer leftLeg;
	List<ModelOptionPartsInfo> OptionParts = new ArrayList<ModelOptionPartsInfo>();

	public ModelLittleGirl()
	{
		textureWidth = 96;
		textureHeight = 48;

		bottom = new ModelRenderer(this, 0, 29);
		bottom.addBox(-3.5F, -1F, -3F, 7, 6, 6);
		bottom.setRotationPoint(0F, 15F, 0F);
		bottom.setTextureSize(96, 48);
		bottom.mirror = true;
		setRotation(bottom, 0F, 0F, 0F);
		head = new ModelRenderer(this, 0, 0);
		head.addBox(-4F, -8F, -4F, 8, 8, 8);
		head.setRotationPoint(0F, 8F, 0F);
		head.setTextureSize(96, 48);
		head.mirror = true;
		setRotation(head, 0F, 0F, 0F);
		body = new ModelRenderer(this, 0, 16);
		body.addBox(-3F, 0F, -2F, 6, 7, 4);
		body.setRotationPoint(0F, 8F, 0F);
		body.setTextureSize(96, 48);
		body.mirror = true;
		setRotation(body, 0F, 0F, 0F);
		rightArm = new ModelRenderer(this, 32, 0);
		rightArm.addBox(-2F, -1F, -1F, 2, 8, 2);
		rightArm.setRotationPoint(-3F, 9.5F, 0F);
		rightArm.setTextureSize(96, 48);
		rightArm.mirror = true;
		setRotation(rightArm, 0F, 0F, 0.1570796F);
		rightLeg = new ModelRenderer(this, 20, 16);
		rightLeg.addBox(-2F, 0F, -2F, 3, 9, 4);
		rightLeg.setRotationPoint(-1F, 15F, 0F);
		rightLeg.setTextureSize(96, 48);
		rightLeg.mirror = true;
		setRotation(rightLeg, 0F, 0F, 0F);
		leftArm = new ModelRenderer(this, 40, 0);
		leftArm.addBox(0F, -1F, -1F, 2, 8, 2);
		leftArm.setRotationPoint(3F, 9.5F, 0F);
		leftArm.setTextureSize(96, 48);
		leftArm.mirror = true;
		setRotation(leftArm, 0F, 0F, -0.1570796F);
		leftLeg = new ModelRenderer(this, 34, 16);
		leftLeg.addBox(-1F, 0F, -2F, 3, 9, 4);
		leftLeg.setRotationPoint(1F, 15F, 0F);
		leftLeg.setTextureSize(96, 48);
		leftLeg.mirror = true;
		setRotation(leftLeg, 0F, 0F, 0F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		// パーツ設定中は描画しない（必要ないかも）
		if(!isflag){return;}
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5,entity);
		head.render(f5);
		body.render(f5);
		rightArm.render(f5);
		rightLeg.render(f5);
		leftArm.render(f5);
		leftLeg.render(f5);
		bottom.render(f5);
		// 追加パーツの描画
		Iterator i = OptionParts.iterator();
		while(i.hasNext()){
			((ModelOptionPartsInfo)i.next()).render(f5);
		}
	}

	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	@Override
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
	{
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
	}

	// 追加パーツをクリア
	public void cleanParts(){
		OptionParts.clear();
	}

	// オプションパーツを追加
	public void SetParts(List<ModelOptionPartsInfo> newOption){
		isflag = false;
		cleanParts();
		OptionParts.addAll(newOption);
		isflag = true;
	}

	// オプションパーツを追加
	public void AddParts(ModelOptionPartsInfo parts){
		Iterator i = OptionParts.iterator();
		while(i.hasNext()){
			// 同じ名前のパーツは追加しない
			((ModelOptionPartsInfo)i.next()).getName().equals(parts.getName());
			return;
		}
		OptionParts.add(parts);
	}

	// オプションパーツを削除
	public void RemoveParts(String Name){
		Iterator i = OptionParts.iterator();
		while(i.hasNext()){
			// 同じ名前のパーツを削除
			((ModelOptionPartsInfo)i.next()).getName().equals(Name);
			i.remove();
			return;
		}
	}
}
