package basashi.erm.resource;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import basashi.erm.ai.workmode.IERMAIBase;
import basashi.erm.core.log.ModLog;
import basashi.erm.entity.EntityERMBase;
import basashi.erm.resource.object.ResourceTag;
import basashi.erm.resource.sounds.SoundTag;

public class ResourceAI implements IERMResource {
	// AIディレクトリ
	public static final String DirName = "AI";
    // デフォルトリソースのパス
	private static final String ORIGINAL_RESOURCE = "assets/erm/ai";

	// class ファイルかどうか確認するフィルタ
	private static final FileFilter cls = new FileFilter(){
		@Override
		public boolean accept(File pathname){
			try{
				if (pathname.isFile()){
					if (pathname.getName().endsWith(".cls")){
						return true;
					}
				}
			}catch(Exception ex){
				ModLog.log().warn("[AI Find Error] : "+pathname.getName() );
			}
			return false;
		}
	};

	private Map<String,IERMAIBase> ailist;
	/*************************************************************************/
	/** リソース操作                                                                                                                                                               */
	/*************************************************************************/
	/**
	 * 指定したモブ用のAIを取り出す
	 * @param entity
	 * @return
	 */
	public List<IERMAIBase> getAIList(Class<? extends EntityERMBase> entity){
		List<IERMAIBase> ret = new ArrayList<IERMAIBase>();
		for (IERMAIBase ai : ailist.values()){
			if ((ai.getTarget() == EntityERMBase.class) ||
					(ai.getTarget() == entity)){
				ret.add(ai);
			}
		}
		return ret;
	}

	/**
	 * 指定したAIを取り出す
	 * @param entity
	 * @param AIID
	 * @return
	 */
	public IERMAIBase getAI(Class<? extends EntityERMBase> entity, String AIID){
		IERMAIBase ret =ailist.get(AIID);
		if (ret != null && ret.getTarget() == entity){
			return ret;
		}
		return null;
	}


	/*************************************************************************/
	/** インタフェース                                                                                                                                                               */
	/*************************************************************************/
	@Override
	public void initResource() {
		ailist = new HashMap<String,IERMAIBase>();
		// デフォルトAIを登録
	}

	@Override
	public boolean loadResource(File directory) {
		return loadAIFile(directory);
	}

	@Override
	public String dirName() {
		return DirName;
	}

	@Override
	public boolean canUseResource() {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	/*************************************************************************/
	/** 内部用                                                                                                                                                                        */
	/*************************************************************************/
	private boolean loadAIFile(File directory)
	{
		try{
			// ローダ作成
			ClassLoader classloader = net.minecraft.client.Minecraft.class.getClassLoader();
			Method method = (java.net.URLClassLoader.class).getDeclaredMethod("addURL", new Class[]
	                {
	                    java.net.URL.class
	                });
	        method.setAccessible(true);
	        method.invoke(classloader, new Object[]{directory.toURI().toURL()});

	        // AIロード
	        for(File file : directory.listFiles(cls)){
	        	try{
	        		IERMAIBase ai = aiLoad(classloader,file);
	        		if ((!basashi.erm.util.Util.StringisEmptyOrNull(ai.DisplayName())) &&
	        				ai.getTarget() != null){
	        			// リソースタグを登録
	        			for (ResourceTag tag :  ai.getSoundTag()){
	        				SoundTag.instance.addResourceTag(tag);
	        			}
	        		}
	        		ailist.put(file.getName() + "\t" + ai.AIName(), ai);
	        	}catch(Exception ex){
	        		ModLog.log().warn("can't load ai file :" + file.getName());
	        	}
			}
		}catch(Exception ex){
			ex.printStackTrace();
			ModLog.log().error("can't load ai");
			return false;
		}
		return true;
    }

	/**
	 * @param folder
	 * @param file
	 * @return
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws MalformedURLException
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * aiのクラスファイルを取得し、それから作られたインスタンスを返します。
	 */
	private IERMAIBase aiLoad(ClassLoader classloader, File file) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, MalformedURLException, ClassNotFoundException, InstantiationException{
		Class c = classloader.loadClass(file.getName().replace(".class", ""));
		return (IERMAIBase)c.newInstance();
	}

}
