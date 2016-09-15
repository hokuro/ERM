package basashi.erm.model;

import basashi.erm.render.ERMModelRenderer;
import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.client.model.TexturedQuad;
import net.minecraft.client.renderer.Tessellator;

public abstract class ERMModelBoxBase {

	protected PositionTextureVertex[] vertexPositions;
	protected TexturedQuad[] quadList;
	public float posX1;
	public float posY1;
	public float posZ1;
	public float posX2;
	public float posY2;
	public float posZ2;
	public String boxName;


	/**
	 * こちらを必ず実装すること。
	 * @param pMRenderer
	 * @param pArg
	 */
	public ERMModelBoxBase(ERMModelRenderer pMRenderer, Object ... pArg) {
	}

	public void render(Tessellator par1Tessellator, float par2) {
		for (int var3 = 0; var3 < quadList.length; ++var3) {
			quadList[var3].func_178765_a(par1Tessellator.func_178180_c(), par2);
		}
	}

	public ERMModelBoxBase setBoxName(String pName) {
		boxName = pName;
		return this;
	}

}
