package basashi.erm.core;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import basashi.erm.entity.EntityERMBase;
import basashi.erm.entity.EntityLittleGirl;
import basashi.erm.event.EventHook;
import basashi.erm.model.ModelLittleGirl;
import basashi.erm.resource.ERMResourceManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
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

	// EntityClassList
	public static final List<Class> ERMEntityClassList =
			new ArrayList<Class>(){{add(EntityLittleGirl.class);
			/*
			add(EntityGirl.class);
			add(EntityLittleSister.class);
			add(EntityLittleLady.class);
			*/
			}};

	// 幼女インスタンス
	private static EntityLittleGirl littleGirl = null;
	private static ModelBase littleGirlModel = new ModelLittleGirl();
	/*
	// 少女インスタンス
	private static EntityGirl girl = null;
	private static ModelBase girlModl = new ModelGirl();
	// イモウトインスタンス
	private static EntityLittleSister littleSister = null;
	private static ModelBase littleSisterModel = new ModelLittleSister();
	// お嬢様インスタンス
	private static EntityLittleLady littleLady = null;
	private static ModelBase littleLadyModel = new ModellLittleLady();
	*/



	// クラス-モブマップ
	public static final Map<Class,EntityERMBase> mobMap = new HashMap<Class,EntityERMBase>(){
		{put(EntityLittleGirl.class, littleGirl);}
		/*
		{put(EntityGirl.class,girl);}
		{put(EntityLittleSister.class, littleSister);}
		{put(EntityLittleLady.class,littleLady);}
		*/
	};
	public static final Map<String, Class> mobMapStringToClass = new HashMap<String,Class>(){
		{put(EntityLittleGirl.NAME.toLowerCase(),EntityLittleGirl.class);}
		/*
		{put(EntityGirl.NAME.toLowerCase(),EntityGirl.class);}
		{put(EntityLittleSister.NAME.toLowerCase(), EntityLittleSister.class);}
		{put(EntityLittleLady.NAME.toLowerCase(),cEntityLittleSister.lass);}
		*/
	};
	// クラス-モデルマップ
	public static final Map<Class,ModelBase> mobModelMap = new HashMap<Class,ModelBase>(){
		{put(EntityLittleGirl.class, littleGirlModel);}
		/*
		{put(EntityGirl.class,girlModl);}
		{put(EntityLittleSister.class, littleSisterModel);}
		{put(EntityLittleLady.class,littleLadyModel);}
		*/
	};

	@EventHandler
	public void construct(FMLConstructionEvent event) throws Exception {
		// デバッグモード設定
		//ModCommon.isDebug = true;
		// デフォルトリソース初期化
		ERMResourceManager.instance().init();
		// カスタムリソース初期化
		ERMResourceManager.instance().loadResource();
		//if ( ModCommon.isDebug ){
			ERMResourceManager.instance().texture().DebugInfo();
		//}
	}

//	@EventHandler
//	public void preInit(FMLPreInitializationEvent event) {
//		// アイテムを登録
//		ERMItem.registerItem(event);
//	}
//
//	@EventHandler
//	public void init(FMLInitializationEvent event) {
//		// エンティティを登録
//		this.registreEntity();
//		// レンダーを登録
//		proxy.registerEntityRender();
//
//		// イベントハンドラを登録
//		mcEvent = new EventHook();
//		MinecraftForge.EVENT_BUS.register(mcEvent);
//	}
//
//	@EventHandler
//	public void postInit(FMLPostInitializationEvent event){
//		// リソースの読み込み
//		ERMResourceManager.instance().loadResource();
//	}


	// 追加モブを登録する
	private void registreEntity(){
		// ちいさなおんなのこ
		EntityRegistry.registerModEntity(EntityLittleGirl.class,
				EntityLittleGirl.NAME, 0, this, 10, 1, false);

		/*
		// 少女
		EntityRegistry.registerEgg(EntityGirl.class,
				EntityGirl.NAME, 1, this, 10, 1, false);
		// イモウト
		EntityRegistry.registerEgg(EntityLittleSister.class,
				EntityLittleSister.NAME, 1, this, 10, 1, false);
		// お嬢様
		EntityRegistry.registerEgg(EntityLittleLeady.class,
				EntityLittleLeady.NAME, 1, this, 10, 1, false);
		// 流浪のいもうと
		EntityRegistry.registerEgg(EntityLittleSisterWandller.class,
				EntityLittleSisterWandller.NAME, 1, this, 10, 1, false);
		*/
	}

	// モブのインスタンスを初期化
	@SideOnly(Side.CLIENT)
	public static void initEntity(){
		littleGirl = null;
		/*
		girl = null;
		littleSister = null;
		littleLady = null;
		*/

	}

	// モブのインスタンスを取得
	@SideOnly(Side.CLIENT)
	public static EntityLivingBase getEntity(Class entityc) throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, SecurityException{
		Entity entity = (Entity)entityc.getConstructor(new Class[] {World.class}).newInstance(new Object[] {Minecraft.func_71410_x().field_71441_e});
		return getEntity(entity);
	}

	// モブがスポーンしているかどうか
	@SideOnly(Side.CLIENT)
	public static boolean isAliveLittelGirl(Class entityc) throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, SecurityException{
		Entity entity = (Entity)entityc.getConstructor(new Class[] {World.class}).newInstance(new Object[] {Minecraft.func_71410_x().field_71441_e});
		return isAliveEntity(entity);
	}

	// モブのインスタンスを取り出す
	public static EntityLivingBase getEntity(Entity entity) throws IllegalArgumentException{
		if (mobMap.containsKey(entity.getClass())){
			return mobMap.get(entity.getClass());
		}
		throw new IllegalArgumentException("unknown entity");
	}

	// モブがスポンしているかどうか判定する
	public static boolean isAliveEntity(Entity entity) throws IllegalArgumentException{
		if (mobMap.containsKey(entity.getClass())){
			return mobMap.get(entity.getClass())!=null?true:false;
		}
		throw new IllegalArgumentException("unknown entity");
	}

	// モブのインスタンスを設定する
	public static void setEntityInstance(EntityERMBase entity){
		if (mobMap.containsKey(entity.getClass())){
			EntityERMBase mob = mobMap.get(entity.getClass());
			mob = entity;
		}
	}

	// モブのモデルを取得する
	public static ModelBase getModel(Class target) throws IllegalArgumentException {
		// TODO 自動生成されたメソッド・スタブ
		if (mobModelMap.containsKey(target)){
			return mobModelMap.get(target);
		}
		throw new IllegalArgumentException("unknown entity");
	}


	// 指定した名前のモブのクラスを取得する
	public static Class checkEntityName(String name){
		return mobMapStringToClass.get(name.toLowerCase());
	}



}
