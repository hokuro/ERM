package basashi.erm.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import basashi.erm.render.ERMModelRenderer;
import net.minecraft.client.model.TextureOffset;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.util.math.MathHelper;

public class ERMModelBase {

	public static final float PI = (float)Math.PI;

	public Render render;

	// ModelBaseとある程度互換
	public int textureWidth = 64;
	public int textureHeight = 32;
	public float onGrounds[] = new float[] {0.0F, 0.0F};
	public int dominantArm = 0;
	public boolean isRiding = false;
	public boolean isChild = true;
	public List<ERMModelRenderer> boxList = new ArrayList<ERMModelRenderer>();
	private Map<String, TextureOffset> modelTextureMap = new HashMap<String, TextureOffset>();



	// ModelBase互換関数群

	public void render(IModelCaps pEntityCaps, float par2, float par3,
			float ticksExisted, float pheadYaw, float pheadPitch, float par7, boolean pIsRender) {
	}

	public void setRotationAngles(float par1, float par2, float pTicksExisted,
			float pHeadYaw, float pHeadPitch, float par6, IModelCaps pEntityCaps) {
	}

	public void setLivingAnimations(IModelCaps pEntityCaps, float par2, float par3, float pRenderPartialTicks) {
	}

	public ERMModelRenderer getRandomModelBox(Random par1Random) {
		// 膝に矢を受けてしまってな・・・
		int li = par1Random.nextInt(this.boxList.size());
		ERMModelRenderer lmr = this.boxList.get(li);
		for (int lj = 0; lj < boxList.size(); lj++) {
			if (!lmr.cubeList.isEmpty()) {
				break;
			}
			// 箱がない
			if (++li >= boxList.size()) {
				li = 0;
			}
			lmr = this.boxList.get(li);
		}
		return lmr;
	}

	protected void setTextureOffset(String par1Str, int par2, int par3) {
		modelTextureMap.put(par1Str, new TextureOffset(par2, par3));
	}

	/**
	 * 推奨されません。
	 */
	public TextureOffset getTextureOffset(String par1Str) {
		// このままだと意味ないな。
		return modelTextureMap.get(par1Str);
	}


	// MathHelperトンネル関数群

	public static final float mh_sin(float f) {
		f = f % 6.283185307179586F;
		f = (f < 0F) ? 360 + f : f;
		return MathHelper.func_76126_a(f);
	}

	public static final float mh_cos(float f) {
		f = f % 6.283185307179586F;
		f = (f < 0F) ? 360 + f : f;
		return MathHelper.func_76134_b(f);
	}

	public static final float mh_sqrt_float(float f) {
		return MathHelper.func_76129_c(f);
	}

	public static final float mh_sqrt_double(double d) {
		return MathHelper.func_76133_a(d);
	}

	public static final int mh_floor_float(float f) {
		return MathHelper.func_76141_d(f);
	}

	public static final int mh_floor_double(double d) {
		return MathHelper.func_76128_c(d);
	}

	public static final long mh_floor_double_long(double d) {
		return MathHelper.func_76124_d(d);
	}

	public static final float mh_abs(float f) {
		return MathHelper.func_76135_e(f);
	}

	public static final double mh_abs_max(double d, double d1) {
		return MathHelper.func_76132_a(d, d1);
	}

	public static final int mh_bucketInt(int i, int j) {
		return MathHelper.func_76137_a(i, j);
	}

	public static final boolean mh_stringNullOrLengthZero(String s) {
		return s==null||s=="";
	}

	public static final int mh_getRandomIntegerInRange(Random random, int i, int j) {
		return MathHelper.func_76136_a(random, i, j);
	}


    /**
     * Copies the angles from one object to another. This is used when objects should stay aligned with each other, like
     * the hair over a players head.
     */
    public static void copyModelAngles(ERMModelRenderer source, ERMModelRenderer dest)
    {
        dest.rotateAngleX = source.rotateAngleX;
        dest.rotateAngleY = source.rotateAngleY;
        dest.rotateAngleZ = source.rotateAngleZ;
        dest.rotationPointX = source.rotationPointX;
        dest.rotationPointY = source.rotationPointY;
        dest.rotationPointZ = source.rotationPointZ;
    }

    public void setModelAttributes(ERMModelBase model)
    {
        this.isRiding = model.isRiding;
        this.isChild = model.isChild;
    }
}
