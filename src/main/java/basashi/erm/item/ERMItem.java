package basashi.erm.item;

import basashi.erm.core.ModCommon;
import basashi.erm.custommodel.CustomModelLoader;
import basashi.erm.item.ItemCandy.Candy;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ERMItem {

	// イモウト素材
	public static ToolMaterial materialImout;
	// 心
	public static final Item soul =  new ItemSoul()
			.setUnlocalizedName(ItemSoul.NAME).setRegistryName(ItemSoul.NAME);
	// キャンディ
	public static final Item candy = new ItemCandy(1,0.5F,false).
			setRegistryName(ItemCandy.NAME).setUnlocalizedName(ItemCandy.NAME);

	// 脳髄 剣
	public static Item imoutoBrain;
	// 頭蓋 ヘルメット → ブロック
	public static Block imoutoFace;
	// 体液 ポーション
	public static Item imoutoBlood;
	// 両腕 スコップ
	public static Item imoutoArms;
	// 胴体 盾
	public static Item imoutoBody;
	// 心臓 食べ物
	public static Item imoutoHart;
	// 両足 斧
	public static Item imoutoLegs;
	// 秘密 → 流浪のイモウト召喚
	public static Item imoutoUterus;
	// 臓物 リード
	public static Item imoutoViscera;

	public static void registerItem(FMLPreInitializationEvent event){
		if (ModCommon.norelease){
			imoutoFace = new BlockImoutoFace().
					setRegistryName(BlockImoutoFace.NAME).setUnlocalizedName(BlockImoutoFace.NAME);
			ItemBlock imoutFaceBlock = new ItemBlock(imoutoFace);

			materialImout  = EnumHelper.addToolMaterial("imouto", 3, 021, 999.90F, 999.9F, 0);
			imoutoBrain = new ItemImoutoBrain().
					setRegistryName(ItemImoutoBrain.NAME).setUnlocalizedName(ItemImoutoBrain.NAME);
			imoutoBlood = new ItemImoutoBlood().
					setRegistryName(ItemImoutoBlood.NAME).setUnlocalizedName(ItemImoutoBlood.NAME);
			imoutoArms = new ItemImoutoArms().
					setRegistryName(ItemImoutoArms.NAME).setUnlocalizedName(ItemImoutoArms.NAME);
			imoutoBody = new ItemImoutoBody().
					setRegistryName(ItemImoutoBody.NAME).setUnlocalizedName(ItemImoutoBody.NAME);
			imoutoHart = new ItemImoutoHart().
					setRegistryName(ItemImoutoHart.NAME).setUnlocalizedName(ItemImoutoHart.NAME);
			imoutoLegs = new ItemImoutoLegs().
					setRegistryName(ItemImoutoLegs.NAME).setUnlocalizedName(ItemImoutoLegs.NAME);
			imoutoUterus = new ItemImoutoUterus().
					setRegistryName(ItemImoutoUterus.NAME).setUnlocalizedName(ItemImoutoUterus.NAME);
			imoutoViscera = new ItemImoutoViscera().
					setRegistryName(ItemImoutoViscera.NAME).setUnlocalizedName(ItemImoutoViscera.NAME);

			GameRegistry.register(imoutoBrain);
			GameRegistry.register(imoutoBlood);
			GameRegistry.register(imoutoArms);
			GameRegistry.register(imoutoBody);
			GameRegistry.register(imoutoHart);
			GameRegistry.register(imoutoLegs);
			GameRegistry.register(imoutoUterus);
			GameRegistry.register(imoutoViscera);

			//テクスチャ・モデル指定JSONファイル名の登録。
			if(event.getSide().isClient()){
				GameRegistry.register(imoutoFace);
		        GameRegistry.register(imoutFaceBlock,imoutoFace.getRegistryName());
		        ModelLoader.setCustomModelResourceLocation(imoutFaceBlock, 0,new ModelResourceLocation(imoutoFace.getRegistryName(), "inventory"));
				ModelLoader.setCustomModelResourceLocation(imoutoBrain, 0, new ModelResourceLocation(imoutoBrain.getRegistryName(), "inventory"));
				ModelLoader.setCustomModelResourceLocation(imoutoBlood, 0, new ModelResourceLocation(imoutoBlood.getRegistryName(), "inventory"));
				ModelLoader.setCustomModelResourceLocation(imoutoArms, 0, new ModelResourceLocation(imoutoArms.getRegistryName(), "inventory"));

				ModelLoaderRegistry.registerLoader(CustomModelLoader.instance);
				ModelLoader.setCustomModelResourceLocation(imoutoBody, 0, new ModelResourceLocation(imoutoBody.getRegistryName(), "inventory"));
				ModelLoader.setCustomModelResourceLocation(imoutoBody, 1, new ModelResourceLocation(imoutoBody.getRegistryName()+"_blocking", "inventory"));

				ModelLoader.setCustomModelResourceLocation(imoutoHart, 0, new ModelResourceLocation(imoutoHart.getRegistryName(), "inventory"));
				ModelLoader.setCustomModelResourceLocation(imoutoLegs, 0, new ModelResourceLocation(imoutoLegs.getRegistryName(), "inventory"));
				ModelLoader.setCustomModelResourceLocation(imoutoUterus, 0, new ModelResourceLocation(imoutoUterus.getRegistryName(), "inventory"));
				ModelLoader.setCustomModelResourceLocation(imoutoViscera, 0, new ModelResourceLocation(imoutoViscera.getRegistryName(), "inventory"));
			}
		}

		GameRegistry.register(candy);
		GameRegistry.register(soul);


		//テクスチャ・モデル指定JSONファイル名の登録。
		if(event.getSide().isClient()){
			int meta = 0;
			for (Candy can : ((ItemCandy)candy).candys){
				ModelLoader.setCustomModelResourceLocation(candy, meta, new ModelResourceLocation(candy.getRegistryName() + "_" + can.CandyName() , "inventory"));
				meta++;
			}
			meta = 0;
			for (String name : ((ItemSoul)soul).spawnableEntityNames){
				ModelLoader.setCustomModelResourceLocation(soul, meta, new ModelResourceLocation(soul.getRegistryName() + "_" + name, "inventory"));
				meta++;
			}
		}
	}

	public static void registerRecipe(){
		// candy normal
        GameRegistry.addShapelessRecipe(new ItemStack(candy,3,0),
                Items.sugar
        );
		// candy apple
        GameRegistry.addShapelessRecipe(new ItemStack(candy,3,1),
                Items.sugar,Items.apple
        );
		// candy watermelon
        GameRegistry.addShapelessRecipe(new ItemStack(candy,3,2),
                Items.sugar,Items.melon
        );
		// candy choco
        GameRegistry.addShapelessRecipe(new ItemStack(candy,3,3),
                Items.sugar,new ItemStack(Blocks.cocoa)
        );
		// candy pumpkin
        GameRegistry.addShapelessRecipe(new ItemStack(candy,3,4),
                Items.sugar,new ItemStack(Blocks.pumpkin)
        );
		// candy squid
        GameRegistry.addShapelessRecipe(new ItemStack(candy,3,5),
                Items.sugar,new ItemStack(Items.dye,1,0)
        );
		// candy callot
        GameRegistry.addShapelessRecipe(new ItemStack(candy,3,6),
                Items.sugar,Items.carrot
        );
		// candy cactus
        GameRegistry.addShapelessRecipe(new ItemStack(candy,3,7),
                Items.sugar,new ItemStack(Blocks.cactus)
        );

        // soul littlegirl
        GameRegistry.addRecipe(new ItemStack(soul,1,1),
                " E ",
                "GBG",
                " W ",
                'B',Items.glass_bottle,
                'E',Items.egg,
                'G',Items.gold_nugget,
                'W',Items.water_bucket
        );
        if (ModCommon.norelease){
        	GameRegistry.addRecipe(new ItemStack(soul,1,0),
        		"ABC",
        		"DEF",
        		"GHI",
        		'A',imoutoBrain,
        		'B',imoutoFace,
        		'C',imoutoBlood,
        		'D',imoutoArms,
        		'E',imoutoBody,
        		'F',imoutoHart,
        		'G',imoutoLegs,
        		'H',imoutoUterus,
        		'I',imoutoViscera);
        }
	}
}
