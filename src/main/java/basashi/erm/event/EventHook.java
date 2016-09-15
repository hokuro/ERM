package basashi.erm.event;

import java.io.File;

import com.google.common.eventbus.Subscribe;

import basashi.erm.core.Mod_ERM;
import basashi.erm.resource.ERMResourceManager;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;

public class EventHook {
	public static File textureDir;
	public String saveFolder = "";

	@EventHandler
    public void serverStarting(FMLServerStartingEvent evt){
        if ( !saveFolder.equals(FMLCommonHandler.instance().getMinecraftServerInstance().getFolderName())){
        	saveFolder = FMLCommonHandler.instance().getMinecraftServerInstance().getFolderName();
	        // セーブデータフォルダをリソースに登録
	        ERMResourceManager.instance().setSaveDir(new File(Minecraft.getMinecraft().mcDataDir,"saves/"+saveFolder));
        }
	}

    @Subscribe
    public void serverStopped(FMLServerStoppedEvent evt){
    	// セーブフォルダ名初期化
    	saveFolder = "";
        // ペットエンティティを初期化
        Mod_ERM.clearEntity();
    }

//	// ワールド選択時に実行する処理
//	 @SubscribeEvent(priority = EventPriority.HIGHEST)
//	    public void onEntityJoinWorld(EntityJoinWorldEvent event)
//	    {
//	        if (!event.getWorld().isRemote)
//	        {
//	        	return;
//	        }
//	        if ( !saveFolder.equals(FMLCommonHandler.instance().getMinecraftServerInstance().getFolderName())){
//	        	saveFolder = FMLCommonHandler.instance().getMinecraftServerInstance().getFolderName();
//		        // セーブデータフォルダをリソースに登録
//		        ERMResourceManager.instance().setSaveDir(new File(Minecraft.getMinecraft().mcDataDir,"save/"+FMLCommonHandler.instance().getMinecraftServerInstance().getFolderName()));
//		        // ペットエンティティを初期化
//		        Mod_ERM.initEntity();
//	        }
//	    }
}
