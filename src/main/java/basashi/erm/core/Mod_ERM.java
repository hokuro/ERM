package basashi.erm.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import basashi.erm.config.ConfigValue;
import basashi.erm.entity.EntityERMBase;
import basashi.erm.entity.EntityGirl;
import basashi.erm.entity.EntityImouto;
import basashi.erm.entity.EntityImoutoWanderer;
import basashi.erm.entity.EntityLittleGirl;
import basashi.erm.entity.EntityLittleLady;
import basashi.erm.entity.EntityLittleSister;
import basashi.erm.event.EventHook;
import basashi.erm.item.ERMItem;
import basashi.erm.message.MessageCandy;
import basashi.erm.message.MessageEat;
import basashi.erm.model.ModelERMBase;
import basashi.erm.model.ModelGirl;
import basashi.erm.model.ModelImouto;
import basashi.erm.model.ModelLittleGirl;
import basashi.erm.model.ModelLittleLady;
import basashi.erm.model.ModelLittleSister;
import basashi.erm.resource.ERMResourceManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = ModCommon.MOD_ID, name = ModCommon.MOD_NAME, version = ModCommon.MOD_VERSION)
public class Mod_ERM {
	// モッドインスタンス
	@Mod.Instance(ModCommon.MOD_ID)
	public static Mod_ERM instance;

	// プロキシ
	@SidedProxy(clientSide = ModCommon.MOD_PACKAGE + ModCommon.MOD_CLIENT_SIDE, serverSide = ModCommon.MOD_PACKAGE + ModCommon.MOD_SERVER_SIDE)
	public static CommonProxy proxy;

	// 通信用チャンネル
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(ModCommon.MOD_CHANEL);

	// イベントハンドラ
	public EventHook mcEvent;

	// 幼女インスタン
	private static EntityLittleGirl littleGirl = null;
	public static final ModelERMBase littleGirlModel = new ModelLittleGirl();
	// 少女
	private static EntityGirl girl = null;
	public static final ModelERMBase girlModl = new ModelGirl();
	// 妹
	private static EntityLittleSister littleSister = null;
	public static final ModelERMBase littleSisterModel = new ModelLittleSister();
	// イモウト
	private static EntityImouto imouto = null;
	public static final ModelERMBase imoutoModel = new ModelImouto();
	// お嬢様
	private static EntityLittleLady littleLady = null;
	public static final ModelERMBase littleLadyModel = new ModelLittleLady();

	// EntityClassList
	public static final List<Class> ERMEntityClassList =
			new ArrayList<Class>(){{
			add(EntityImouto.class);
			add(EntityLittleGirl.class);
			add(EntityGirl.class);
			add(EntityLittleSister.class);
			add(EntityLittleLady.class);
			}};

	// 名前-クラスマップ
	public static final Map<String, Class> mobMapStringToClass = new HashMap<String,Class>(){
		{put(EntityImouto.NAME.toLowerCase(), EntityImouto.class);}
		{put(EntityLittleGirl.NAME.toLowerCase(),EntityLittleGirl.class);}
		{put(EntityGirl.NAME.toLowerCase(),EntityGirl.class);}
		{put(EntityLittleSister.NAME.toLowerCase(), EntityLittleSister.class);}
		{put(EntityLittleLady.NAME.toLowerCase(),EntityLittleSister.class);}
	};

	// クラス-モデルマップ
	public static final Map<Class,ModelERMBase> mobModelMap = new HashMap<Class,ModelERMBase>(){
		{put(EntityImouto.class, imoutoModel);}
		{put(EntityLittleGirl.class, littleGirlModel);}
		{put(EntityGirl.class,girlModl);}
		{put(EntityLittleSister.class, littleSisterModel);}
		{put(EntityLittleLady.class,littleLadyModel);}
	};

	@EventHandler
	public void construct(FMLConstructionEvent event) throws Exception {
		// リリースモード
		ModCommon.norelease = true;
		// デバッグモード設定
		ModCommon.isDebug = true;
		// 開発モード
		ModCommon.isDevelop = true;
		// デフォルトリソース初期化
		ERMResourceManager.instance().init();

		if (ModCommon.norelease)
		// カスタムリソース初期化
		ERMResourceManager.instance().loadResource();
		if ( ModCommon.isDebug ){
			ERMResourceManager.instance().texture().DebugInfo();
		}
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		// コンフィグ読み込み
		ConfigValue.init(event);
		if (ModCommon.norelease){
			ConfigValue._imouto.canSister = true;
		}
		// アイテムを登録
		ERMItem.registerItem(event);

		// ネットワーク設定
		INSTANCE.registerMessage(MessageEat.class, MessageEat.class, 0, Side.SERVER);
		INSTANCE.registerMessage(MessageCandy.class, MessageCandy.class, 1, Side.SERVER);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		// レシピ登録
		ERMItem.registerRecipe();
		// エンティティを登録
		this.registreEntity();
		// レンダーを登録
		proxy.registerEntityRender();

		// イベントハンドラを登録
		mcEvent = new EventHook();
		MinecraftForge.EVENT_BUS.register(mcEvent);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event){
	}

	@EventHandler
    public void serverStarting(FMLServerStartingEvent evt){
        this.mcEvent.serverStarting(evt);
	}

    @EventHandler
    public void serverStopped(FMLServerStoppedEvent evt){
       this.mcEvent.serverStopped(evt);
    }



	// 追加モブを登録する
	private void registreEntity(){
		if (ConfigValue._imouto.canSister){
			// 流浪のイモウト
			EntityRegistry.registerModEntity(EntityImoutoWanderer.class,
					EntityImoutoWanderer.NAME, 100, this,30,1,false);
			// イモウト
			EntityRegistry.registerModEntity(EntityImouto.class,
					EntityImouto.NAME, 0, this, 10, 1, false);
		}
		// ちいさなおんなのこ
		EntityRegistry.registerModEntity(EntityLittleGirl.class,
				EntityLittleGirl.NAME, 1, this, 10, 1, false);

		// 少女
		EntityRegistry.registerModEntity(EntityGirl.class,
				EntityGirl.NAME, 2, this, 10, 1, false);

		// 妹
		EntityRegistry.registerModEntity(EntityLittleSister.class,
				EntityLittleSister.NAME, 3, this, 10, 1, false);

		// お嬢様
		EntityRegistry.registerModEntity(EntityLittleLady.class,
				EntityLittleLady.NAME, 4, this, 10, 1, false);
	}

	// モブのインスタンスを初期化
	@SideOnly(Side.CLIENT)
	public static void clearEntity(){
		imouto = null;
		littleGirl = null;
		girl = null;
		littleSister = null;
		littleLady = null;
	}

	/**
	 * 指定したクラスに対応するモブのモデルを取得する
	 * @param target クラス
	 * @return モデル
	 * @throws IllegalArgumentException
	 */
	public static ModelERMBase getModel(Class target) throws IllegalArgumentException {
		if (mobModelMap.containsKey(target)){
			return mobModelMap.get(target);
		}
		throw new IllegalArgumentException("unknown entity");
	}

	/**
	 * 指定した名称のモブのクラスを取得する
	 * @param name クラス名
	 * @return クラス
	 */
	public static Class checkEntityName(String name){
		return mobMapStringToClass.get(name.toLowerCase());
	}

    /**
     * モブがスポーンしているかどうかを取得する
     * @param cls
     * @return
     */
    public static boolean isAliveEntity(Class<? extends EntityERMBase> cls){
    	return getStaticEntity(cls) != null?true:false;
    }

	/**
	 * モブのインスタンスを設定する
	 * @param entity
	 */
	public static void setEntity(EntityERMBase entity){
		if (EntityLittleGirl.class == entity.getClass()){
			if (littleGirl==null){littleGirl = (EntityLittleGirl)entity;}
		}
	}

    /**
     * モブのインスタンスを取得する
     * @param cls クラス
     * @return インスタンス
     */
    public static EntityERMBase getEntity(Class<? extends EntityERMBase> cls){
    	return getStaticEntity(cls);
    }

    /**
     * モブのインスタンスを返す
     * @param cls クラス
     * @return インスタンス
     */
    private static EntityERMBase getStaticEntity(Class<? extends EntityERMBase> cls){
    	if (EntityLittleGirl.class == cls){
    		return littleGirl;
    	}else if(EntityGirl.class == cls){
    		return girl;
    	}else if(EntityLittleSister.class == cls){
    		return littleSister;
    	}else if (EntityLittleLady.class == cls){
    		return littleLady;
    	}
    	throw new IllegalArgumentException("unknown entity");
    }
}