package basashi.erm.item;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ERMItem {

	public static final ItemSoul soul = (ItemSoul) new ItemSoul()
			.setUnlocalizedName(ItemSoul.NAME).setRegistryName(ItemSoul.NAME).setCreativeTab(CreativeTabs.tabMisc);

	public static void registerItem(FMLPreInitializationEvent event){
		GameRegistry.register(soul);
		//テクスチャ・モデル指定JSONファイル名の登録。
		if(event.getSide().isClient()){
			//1IDで複数モデルを登録するなら、上のメソッドで登録した登録名を指定する。
			ModelLoader.setCustomModelResourceLocation(soul, 0, new ModelResourceLocation(soul.getRegistryName(), "inventory"));
		}
	}
}
