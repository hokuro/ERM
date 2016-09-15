package basashi.erm.entity;

import basashi.erm.gui.TextureBoxBase;
import net.minecraft.entity.player.EntityPlayer;

public interface IEntityERMBase {
	String REGISTER_NAME();
	void initOwner(EntityPlayer player);
	TextureBoxBase[] getTextureBox();
	String getTextureDress();
	String getTextureArmor();
	void setTextureDress(String path);
	void setTextureArmor(String path);
}
