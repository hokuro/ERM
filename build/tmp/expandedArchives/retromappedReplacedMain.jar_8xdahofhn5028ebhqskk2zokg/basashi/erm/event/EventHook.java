package basashi.erm.event;

import java.io.File;

import basashi.erm.core.Mod_ERM;
import net.minecraft.client.Minecraft;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHook {
	public static File textureDir;

	// ワールド選択時に実行する処理
	 @SubscribeEvent(priority = EventPriority.HIGHEST)
	    public void onEntityJoinWorld(EntityJoinWorldEvent event)
	    {
	        if (!event.getWorld().field_72995_K)
	        {
	        	return;
	        }
	        // ペットエンティティを初期化
	        Mod_ERM.initEntity();
	        // ロードされたセーブデータはいかにERM用のデータフォルダを作る
	        File SavePath = new File(Minecraft.func_71410_x().field_71412_D,"save/"+FMLCommonHandler.instance().getMinecraftServerInstance().func_71270_I()+"/ERMSave");
	        if (!SavePath.exists()){
	        	SavePath.mkdirs();
	        }
	        // 実際のテクスチャを保存するフォルダ
	        textureDir = new File(SavePath,"/texture");
	        if (!textureDir.exists()){
	        	textureDir.mkdirs();
	        }
	    }
}
