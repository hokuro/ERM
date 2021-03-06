package basashi.erm.render;

import java.lang.reflect.Constructor;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import basashi.erm.model.ERMModelBase;
import basashi.erm.model.ERMModelBox;
import basashi.erm.model.ERMModelBoxBase;
import basashi.erm.model.IModelCaps;
import basashi.erm.model.ModelCapsHelper;
import basashi.erm.model.ModelERMBase;
import net.minecraft.client.model.TextureOffset;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;



public class ERMModelRenderer
{

	// ModelRenderer互換変数群
	public float textureWidth;
	public float textureHeight;
	private int textureOffsetX;
	private int textureOffsetY;
	public float rotationPointX;
	public float rotationPointY;
	public float rotationPointZ;
	public float rotateAngleX;
	public float rotateAngleY;
	public float rotateAngleZ;
	protected boolean compiled;
	protected int displayList;
	public boolean mirror;
	public boolean showModel;
	public boolean isHidden;
	/**
	 * パーツの親子関係に左右されずに描画するかを決める。
	 * アーマーの表示などに使う。
	 */
	public boolean isRendering;
	public List<ERMModelBoxBase> cubeList;
	public List<ERMModelRenderer> childModels;
	public final String boxName;
	protected ERMModelBase baseModel;
	public ERMModelRenderer pearent;
	public float offsetX;
	public float offsetY;
	public float offsetZ;
	public float scaleX;
	public float scaleY;
	public float scaleZ;


	public static final float radFactor = 180F / (float)Math.PI;
	public static final float degFactor = (float)Math.PI / 180F;

	// SmartMovingに合わせるために名称の変更があるかもしれません。
	public int rotatePriority;
	public static final int RotXYZ = 0;
	public static final int RotXZY = 1;
	public static final int RotYXZ = 2;
	public static final int RotYZX = 3;
	public static final int RotZXY = 4;
	public static final int RotZYX = 5;

	protected ItemStack itemstack;

	public boolean adjust;
	public FloatBuffer matrix;
	public boolean isInvertX;




	public ERMModelRenderer(ERMModelBase pModelBase, String pName) {
		textureWidth = 64.0F;
		textureHeight = 32.0F;
		compiled = false;
		displayList = 0;
		mirror = false;
		showModel = true;
		isHidden = false;
		isRendering = true;
		cubeList = new ArrayList<ERMModelBoxBase>();
		baseModel = pModelBase;
		pModelBase.boxList.add(this);
		boxName = pName;
		setTextureSize(pModelBase.textureWidth, pModelBase.textureHeight);

		rotatePriority = RotXYZ;
		itemstack = null;
		adjust = true;
		matrix = BufferUtils.createFloatBuffer(16);
		isInvertX = false;

		scaleX = 1.0F;
		scaleY = 1.0F;
		scaleZ = 1.0F;

		pearent = null;
	}

	public ERMModelRenderer(ERMModelBase pModelBase, int px, int py) {
		this(pModelBase, null);
		setTextureOffset(px, py);
	}

	public ERMModelRenderer(ERMModelBase pModelBase) {
		this(pModelBase, (String)null);
	}

	public ERMModelRenderer(ERMModelBase pModelBase, int px, int py, float pScaleX, float pScaleY, float pScaleZ) {
		this(pModelBase, px, py);
		this.scaleX = pScaleX;
		this.scaleY = pScaleY;
		this.scaleZ = pScaleZ;
	}

	public ERMModelRenderer(ERMModelBase pModelBase, float pScaleX, float pScaleY, float pScaleZ) {
		this(pModelBase);
		this.scaleX = pScaleX;
		this.scaleY = pScaleY;
		this.scaleZ = pScaleZ;
	}

	// ModelRenderer互換関数群

	public void addChild(ERMModelRenderer pModelRenderer) {
		if (childModels == null) {
			childModels = new ArrayList<ERMModelRenderer>();
		}
		childModels.add(pModelRenderer);
		pModelRenderer.pearent = this;
	}

	public ERMModelRenderer setTextureOffset(int pOffsetX, int pOffsetY) {
		textureOffsetX = pOffsetX;
		textureOffsetY = pOffsetY;
		return this;
	}

	public ERMModelRenderer addBox(String pName, float pX, float pY, float pZ,
			int pWidth, int pHeight, int pDepth) {
		addParts(ERMModelBox.class, pName, pX, pY, pZ, pWidth, pHeight, pDepth, 0.0F);
		return this;
	}

	public ERMModelRenderer addBox(float pX, float pY, float pZ,
			int pWidth, int pHeight, int pDepth) {
		addParts(ERMModelBox.class, pX, pY, pZ, pWidth, pHeight, pDepth, 0.0F);
		return this;
	}

	public ERMModelRenderer addBox(float pX, float pY, float pZ,
			int pWidth, int pHeight, int pDepth, float pSizeAdjust) {
		addParts(ERMModelBox.class, pX, pY, pZ, pWidth, pHeight, pDepth, pSizeAdjust);
		return this;
	}

	public ERMModelRenderer setRotationPoint(float pX, float pY, float pZ) {
		rotationPointX = pX;
		rotationPointY = pY;
		rotationPointZ = pZ;
		return this;
	}

	// TODO:アップデート時はここをチェックすること
	public void render(float par1, boolean pIsRender) {
		if (isHidden) return;
		if (!showModel) return;

		if (!compiled) {
			compileDisplayList(par1);
		}

		GL11.glPushMatrix();
		GL11.glTranslatef(offsetX, offsetY, offsetZ);

		if (rotationPointX != 0.0F || rotationPointY != 0.0F || rotationPointZ != 0.0F) {
			GL11.glTranslatef(rotationPointX * par1, rotationPointY * par1, rotationPointZ * par1);
		}
		if (rotateAngleX != 0.0F || rotateAngleY != 0.0F || rotateAngleZ != 0.0F) {
			setRotation();
		}
		renderObject(par1, pIsRender);
		GL11.glPopMatrix();
	}
	public void render(float par1) {
		render(par1, true);
	}

	public void renderWithRotation(float par1) {
		if (isHidden) return;
		if (!showModel) return;

		if (!compiled) {
			compileDisplayList(par1);
		}

		GL11.glPushMatrix();
		GL11.glTranslatef(rotationPointX * par1, rotationPointY * par1, rotationPointZ * par1);

		setRotation();

		GL11.glCallList(displayList);
		GL11.glPopMatrix();
	}

	public void postRender(float par1) {
		if (isHidden) return;
		if (!showModel) return;

		if (!compiled) {
			compileDisplayList(par1);
		}

		if (pearent != null) {
			pearent.postRender(par1);
		}

		GL11.glTranslatef(offsetX, offsetY, offsetZ);

		if (rotationPointX != 0.0F || rotationPointY != 0.0F || rotationPointZ != 0.0F) {
			GL11.glTranslatef(rotationPointX * par1, rotationPointY * par1, rotationPointZ * par1);
		}
		if (rotateAngleX != 0.0F || rotateAngleY != 0.0F || rotateAngleZ != 0.0F) {
			setRotation();
		}
	}

	protected void compileDisplayList(float par1) {
		displayList = GLAllocation.func_74526_a(1);
		GL11.glNewList(displayList, GL11.GL_COMPILE);
		Tessellator tessellator = Tessellator.func_178181_a();

		for (int i = 0; i < cubeList.size(); i++) {
			cubeList.get(i).render(tessellator, par1);
		}

		GL11.glEndList();
		compiled = true;
	}

	public ERMModelRenderer setTextureSize(int pWidth, int pHeight) {
		textureWidth = (float)pWidth;
		textureHeight = (float)pHeight;
		return this;
	}


	// 独自追加分

	/**
	 * ModelBox継承の独自オブジェクト追加用
	 */
	public ERMModelRenderer addCubeList(ERMModelBoxBase pModelBoxBase) {
		cubeList.add(pModelBoxBase);
		return this;
	}

	protected ERMModelBoxBase getModelBoxBase(Class pModelBoxBase, Object ... pArg) {
		try {
			Constructor<? extends ERMModelBoxBase> lconstructor =
					pModelBoxBase.getConstructor(ERMModelRenderer.class, Object[].class);
			return lconstructor.newInstance(this, pArg);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	protected Object[] getArg(Object ... pArg) {
		Object lobject[] = new Object[pArg.length + 2];
		lobject[0] = textureOffsetX;
		lobject[1] = textureOffsetY;
		for (int li = 0; li < pArg.length; li++) {
			lobject[2 + li] = pArg[li];
		}
		return lobject;
	}

	public ERMModelRenderer addParts(Class pModelBoxBase, String pName, Object ... pArg) {
		pName = (new StringBuilder()).append(boxName).append(".").append(pName).toString();
		TextureOffset ltextureoffset = baseModel.getTextureOffset(pName);
		setTextureOffset(ltextureoffset.field_78783_a, ltextureoffset.field_78782_b);
		addCubeList(getModelBoxBase(pModelBoxBase, getArg(pArg)).setBoxName(pName));
		return this;
	}

	public ERMModelRenderer addParts(Class pModelBoxBase, Object ... pArg) {
		addCubeList(getModelBoxBase(pModelBoxBase, getArg(pArg)));
		return this;
	}

	/**
	 * 自分でテクスチャの座標を指定する時に使います。
	 * コンストラクタへそのまま値を渡します。
	 */
	public ERMModelRenderer addPartsTexture(Class pModelBoxBase, String pName, Object ... pArg) {
		pName = (new StringBuilder()).append(boxName).append(".").append(pName).toString();
		addCubeList(getModelBoxBase(pModelBoxBase, pArg).setBoxName(pName));
		return this;
	}

	/**
	 * 自分でテクスチャの座標を指定する時に使います。
	 * コンストラクタへそのまま値を渡します。
	 */
	public ERMModelRenderer addPartsTexture(Class pModelBoxBase, Object ... pArg) {
		addCubeList(getModelBoxBase(pModelBoxBase, pArg));
		return this;
	}


	public ERMModelRenderer addPlate(float pX, float pY, float pZ,
			int pWidth, int pHeight, int pFacePlane) {
		addParts(ERMModelPlate.class, pX, pY, pZ, pWidth, pHeight, pFacePlane, 0.0F);
		return this;
	}

	public ERMModelRenderer addPlate(float pX, float pY, float pZ,
			int pWidth, int pHeight, int pFacePlane, float pSizeAdjust) {
		addParts(ERMModelPlate.class, pX, pY, pZ, pWidth, pHeight, pFacePlane, pSizeAdjust);
		return this;
	}

	public ERMModelRenderer addPlate(String pName, float pX, float pY, float pZ,
			int pWidth, int pHeight, int pFacePlane) {
		addParts(ERMModelPlate.class, pName, pX, pY, pZ, pWidth, pHeight, pFacePlane, 0.0F);
		return this;
	}

	/**
	 * 描画用のボックス、子供をクリアする
	 */
	public void clearCubeList() {
		cubeList.clear();
		compiled = false;
		if (childModels != null) {
			childModels.clear();
		}
	}

	// TODO:このあたりは要修正
	public boolean renderItems(ModelERMBase pModelMulti, ModelERMBase pEntityCaps, boolean pRealBlock, int pIndex) {
		ItemStack[] litemstacks = (ItemStack[])ModelCapsHelper.getCapsValue(pEntityCaps, IModelCaps.caps_Items);
		if (litemstacks == null) return false;
		EnumAction[] lactions = (EnumAction[])ModelCapsHelper.getCapsValue(pEntityCaps, IModelCaps.caps_Actions);
		EntityLivingBase lentity = (EntityLivingBase)pEntityCaps.getCapsValue(IModelCaps.caps_Entity);

		renderItems(lentity, pModelMulti.render, pRealBlock, lactions[pIndex], litemstacks[pIndex]);
		return true;
	}

	public void renderItemsHead(ModelERMBase pModelMulti, IModelCaps pEntityCaps) {
		ItemStack lis = (ItemStack)pEntityCaps.getCapsValue(IModelCaps.caps_HeadMount);
		EntityLivingBase lentity = (EntityLivingBase)pEntityCaps.getCapsValue(IModelCaps.caps_Entity);

		renderItems(lentity, pModelMulti.render, true, null, lis);
	}

	protected void renderItems(EntityLivingBase pEntityLiving, Render pRender,
			boolean pRealBlock, EnumAction pAction, ItemStack pItemStack) {
		itemstack = pItemStack;
		renderItems(pEntityLiving, pRender, pRealBlock, pAction);
	}

	protected void renderItems(EntityLivingBase pEntityLiving, Render pRender, boolean pRealBlock, EnumAction pAction) {
		if (itemstack == null) return;

		// アイテムのレンダリング
		GL11.glPushMatrix();

		// アイテムの種類による表示位置の補正
		if (adjust) {

			if (pRealBlock && (itemstack.func_77973_b() instanceof ItemBlock)) {
				float f2 = 0.625F;
				GL11.glScalef(f2, -f2, -f2);
				GL11.glRotatef(270F, 0F, 1F, 0);
			} else if (pRealBlock && (itemstack.func_77973_b() instanceof ItemSkull)) {
				float f2 = 1.0625F;
				GL11.glScalef(f2, -f2, -f2);
			} else {
				float var6;
				/*
				if (itemstack.getItem().rendere) {
					var6 = 0.5F;
					GL11.glTranslatef(0.0F, 0.1875F, -0.2125F);
					var6 *= 0.75F;
					GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
					GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
					GL11.glScalef(var6, -var6, var6);
				} else*/
				if (itemstack.func_77973_b() instanceof ItemBow) {
					var6 = 0.625F;
					GL11.glTranslatef(-0.05F, 0.125F, 0.3125F);
					GL11.glRotatef(-20.0F, 0.0F, 1.0F, 0.0F);
					GL11.glScalef(var6, -var6, var6);
					GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
					GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
				} else if (itemstack.func_77973_b().func_77662_d()) {
					var6 = 0.625F;

					if (itemstack.func_77973_b().func_77629_n_()) {
						GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
						GL11.glTranslatef(0.0F, -0.125F, 0.0F);
					}

					if (pAction == EnumAction.BLOCK) {
						GL11.glTranslatef(0.05F, 0.0F, -0.1F);
						GL11.glRotatef(-50.0F, 0.0F, 1.0F, 0.0F);
						GL11.glRotatef(-10.0F, 1.0F, 0.0F, 0.0F);
						GL11.glRotatef(-60.0F, 0.0F, 0.0F, 1.0F);
					}

					GL11.glTranslatef(0.0F, 0.1875F, 0.1F);
					GL11.glScalef(var6, -var6, var6);
					GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
					GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
				} else {
					var6 = 0.375F;
					GL11.glTranslatef(0.15F, 0.15F, -0.05F);
					GL11.glScalef(var6, var6, var6);
					GL11.glRotatef(60.0F, 0.0F, 0.0F, 1.0F);
					GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
					GL11.glRotatef(20.0F, 0.0F, 0.0F, 1.0F);
				}
			}
		}

		if (pRealBlock && itemstack.func_77973_b() instanceof ItemSkull) {
			String lsowner = "";
			if (itemstack.func_77942_o() && itemstack.func_77978_p().func_74764_b("SkullOwner")) {
				lsowner = itemstack.func_77978_p().func_74779_i("SkullOwner");
			}
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			TileEntitySkullRenderer.field_147536_b.func_188190_a(-0.5F, 0.0F, -0.5F, EnumFacing.func_82600_a(1), 180.0F, itemstack.func_77952_i(), null,0,0);
		} else if (pRealBlock && itemstack.func_77973_b() instanceof ItemBlock) {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			int var4 = pEntityLiving.func_70070_b(0.0F);
			int var5 = var4 % 65536;
			int var6 = var4 / 65536;
			OpenGlHelper.func_77475_a(OpenGlHelper.field_77476_b, var5 / 1.0F, var6 / 1.0F);
		} else {
			// アイテムに色付け
		}

		GL11.glPopMatrix();
	}

	/**
	 *  回転変換を行う順序を指定。
	 * @param pValue
	 * Rot???を指定する
	 */
	public void setRotatePriority(int pValue) {
		rotatePriority = pValue;
	}

	/**
	 * 内部実行用、座標変換部
	 */
	protected void setRotation() {
		// 変換順位の設定
		switch (rotatePriority) {
		case RotXYZ:
			if (rotateAngleZ != 0.0F) {
				GL11.glRotatef(rotateAngleZ * radFactor, 0.0F, 0.0F, 1.0F);
			}
			if (rotateAngleY != 0.0F) {
				GL11.glRotatef(rotateAngleY * radFactor, 0.0F, 1.0F, 0.0F);
			}
			if (rotateAngleX != 0.0F) {
				GL11.glRotatef(rotateAngleX * radFactor, 1.0F, 0.0F, 0.0F);
			}
			break;
		case RotXZY:
			if (rotateAngleY != 0.0F) {
				GL11.glRotatef(rotateAngleY * radFactor, 0.0F, 1.0F, 0.0F);
			}
			if (rotateAngleZ != 0.0F) {
				GL11.glRotatef(rotateAngleZ * radFactor, 0.0F, 0.0F, 1.0F);
			}
			if (rotateAngleX != 0.0F) {
				GL11.glRotatef(rotateAngleX * radFactor, 1.0F, 0.0F, 0.0F);
			}
			break;
		case RotYXZ:
			if (rotateAngleZ != 0.0F) {
				GL11.glRotatef(rotateAngleZ * radFactor, 0.0F, 0.0F, 1.0F);
			}
			if (rotateAngleX != 0.0F) {
				GL11.glRotatef(rotateAngleX * radFactor, 1.0F, 0.0F, 0.0F);
			}
			if (rotateAngleY != 0.0F) {
				GL11.glRotatef(rotateAngleY * radFactor, 0.0F, 1.0F, 0.0F);
			}
			break;
		case RotYZX:
			if (rotateAngleX != 0.0F) {
				GL11.glRotatef(rotateAngleX * radFactor, 1.0F, 0.0F, 0.0F);
			}
			if (rotateAngleZ != 0.0F) {
				GL11.glRotatef(rotateAngleZ * radFactor, 0.0F, 0.0F, 1.0F);
			}
			if (rotateAngleY != 0.0F) {
				GL11.glRotatef(rotateAngleY * radFactor, 0.0F, 1.0F, 0.0F);
			}
			break;
		case RotZXY:
			if (rotateAngleY != 0.0F) {
				GL11.glRotatef(rotateAngleY * radFactor, 0.0F, 1.0F, 0.0F);
			}
			if (rotateAngleX != 0.0F) {
				GL11.glRotatef(rotateAngleX * radFactor, 1.0F, 0.0F, 0.0F);
			}
			if (rotateAngleZ != 0.0F) {
				GL11.glRotatef(rotateAngleZ * radFactor, 0.0F, 0.0F, 1.0F);
			}
			break;
		case RotZYX:
			if (rotateAngleX != 0.0F) {
				GL11.glRotatef(rotateAngleX * radFactor, 1.0F, 0.0F, 0.0F);
			}
			if (rotateAngleY != 0.0F) {
				GL11.glRotatef(rotateAngleY * radFactor, 0.0F, 1.0F, 0.0F);
			}
			if (rotateAngleZ != 0.0F) {
				GL11.glRotatef(rotateAngleZ * radFactor, 0.0F, 0.0F, 1.0F);
			}
			break;
		}
	}

	/**
	 * 内部実行用、レンダリング部分。
	 */
	protected void renderObject(float par1, boolean pRendering) {
		// レンダリング、あと子供も
		GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, matrix);
		if (pRendering && isRendering) {
			GL11.glPushMatrix();
			GL11.glScalef(scaleX, scaleY, scaleZ);
			GL11.glCallList(displayList);
			GL11.glPopMatrix();
		}

		if (childModels != null) {
			for (int li = 0; li < childModels.size(); li++) {
				childModels.get(li).render(par1, pRendering);
			}
		}
	}

	/**
	 * パーツ描画時点のマトリクスを設定する。 これ以前に設定されたマトリクスは破棄される。
	 */
	public ERMModelRenderer loadMatrix() {
		GL11.glLoadMatrix(matrix);
		if (isInvertX) {
			GL11.glScalef(-1F, 1F, 1F);
		}
		return this;
	}


	// ゲッター、セッター

	public boolean getMirror() {
		return mirror;
	}

	public ERMModelRenderer setMirror(boolean flag) {
		mirror = flag;
		return this;
	}

	public boolean getVisible() {
		return showModel;
	}

	public void setVisible(boolean flag) {
		showModel = flag;
	}


	// Deg付きは角度指定が度数法

	public float getRotateAngleX() {
		return rotateAngleX;
	}

	public float getRotateAngleDegX() {
		return rotateAngleX * radFactor;
	}

	public float setRotateAngleX(float value) {
		return rotateAngleX = value;
	}

	public float setRotateAngleDegX(float value) {
		return rotateAngleX = value * degFactor;
	}

	public float addRotateAngleX(float value) {
		return rotateAngleX += value;
	}

	public float addRotateAngleDegX(float value) {
		return rotateAngleX += value * degFactor;
	}

	public float getRotateAngleY() {
		return rotateAngleY;
	}

	public float getRotateAngleDegY() {
		return rotateAngleY * radFactor;
	}

	public float setRotateAngleY(float value) {
		return rotateAngleY = value;
	}

	public float setRotateAngleDegY(float value) {
		return rotateAngleY = value * degFactor;
	}

	public float addRotateAngleY(float value) {
		return rotateAngleY += value;
	}

	public float addRotateAngleDegY(float value) {
		return rotateAngleY += value * degFactor;
	}

	public float getRotateAngleZ() {
		return rotateAngleZ;
	}

	public float getRotateAngleDegZ() {
		return rotateAngleZ * radFactor;
	}

	public float setRotateAngleZ(float value) {
		return rotateAngleZ = value;
	}

	public float setRotateAngleDegZ(float value) {
		return rotateAngleZ = value * degFactor;
	}

	public float addRotateAngleZ(float value) {
		return rotateAngleZ += value;
	}

	public float addRotateAngleDegZ(float value) {
		return rotateAngleZ += value * degFactor;
	}

	public ERMModelRenderer setRotateAngle(float x, float y, float z) {
		rotateAngleX = x;
		rotateAngleY = y;
		rotateAngleZ = z;
		return this;
	}

	public ERMModelRenderer setRotateAngleDeg(float x, float y, float z) {
		rotateAngleX = x * degFactor;
		rotateAngleY = y * degFactor;
		rotateAngleZ = z * degFactor;
		return this;
	}

	public float getRotationPointX() {
		return rotationPointX;
	}

	public float setRotationPointX(float value) {
		return rotationPointX = value;
	}

	public float addRotationPointX(float value) {
		return rotationPointX += value;
	}

	public float getRotationPointY() {
		return rotationPointY;
	}

	public float setRotationPointY(float value) {
		return rotationPointY = value;
	}

	public float addRotationPointY(float value) {
		return rotationPointY += value;
	}

	public float getRotationPointZ() {
		return rotationPointZ;
	}

	public float setRotationPointZ(float value) {
		return rotationPointZ = value;
	}

	public float addRotationPointZ(float value) {
		return rotationPointZ += value;
	}

	public ERMModelRenderer setScale(float pX, float pY, float pZ) {
		scaleX = pX;
		scaleY = pY;
		scaleZ = pZ;
		return this;
	}

	public float setScaleX(float pValue) {
		return scaleX = pValue;
	}

	public float setScaleY(float pValue) {
		return scaleY = pValue;
	}

	public float setScaleZ(float pValue) {
		return scaleZ = pValue;
	}
}