package basashi.erm.config;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ConfigValue{

	private static Class<?>[] cls = new Class<?>[]{
		General.class,
		CfLittleGirl.class,
		CfImount.class,
		CfLittleSister.class,
		CfLittleLady.class};

	public static final General _general = new General();
	public static final CfLittleGirl _littlegirl = new CfLittleGirl();
	public static final CfImount _imouto = new CfImount();
	public static final CfLittleSister _sister = new CfLittleSister();
	public static final CfLittleLady _lady = new CfLittleLady();

	public static void init(FMLPreInitializationEvent event){
		ModConfig.config.init(cls, event);
	}

	public static void reloadConfig(){
		ModConfig.config.reloadConfig();
	}

	public static void saveConfig(){
		ModConfig.config.saveConfig();
	}

	/***************************************************************************************************/
	/*           General                                                                               */
	/***************************************************************************************************/
	public static class General{
	}

	public static class CfLittleGirl{
		// 通常会話発生率(待機時会話が発生する確率)
		@ConfigProperty(category="LittleGirl",comment="conf.talkfreq", max="100", min="0")
		public static int TalkFreq = 100;
		// 通常会話発頻度(待機時会話が発生するtic数)
		@ConfigProperty(category="LittleGirl",comment="conf.talkspan", max="9223372036854775807", min="0")
		public static long TalkSpan = 600;
		// プレイヤーの呼び方
		@ConfigProperty(category="LittleGirl",comment="conf.callplayer")
		public static String CallPlayer = "";
		// 自分の呼び方
		@ConfigProperty(category="LittleGirl",comment="conf.callme")
		public static String CallMe = "";
	}

	public static class CfGirl{
		// 通常会話発生率(待機時会話が発生する確率)
		@ConfigProperty(category="Girl",comment="conf.talkfreq", max="100", min="0")
		public static int TalkFreq = 100;
		// 通常会話発頻度(待機時会話が発生するtic数)
		@ConfigProperty(category="Girl",comment="conf.talkspan", max="9223372036854775807",min="0")
		public static long TalkSpan = 600;
		// プレイヤーの呼び方
		@ConfigProperty(category="Girl",comment="conf.callplayer")
		public static String CallPlayer = "";
		// 自分の呼び方
		@ConfigProperty(category="Girl",comment="conf.callme")
		public static String CallMe = "";
	}

	public static class CfImount{
		// 通常会話発生率(待機時会話が発生する確率)
		@ConfigProperty(category="Imouto",comment="conf.cansister")
		public static boolean canSister = false;
		// 通常会話発生率(待機時会話が発生する確率)
		@ConfigProperty(category="Imouto",comment="conf.talkfreq", max="100", min="0")
		public static int TalkFreq = 100;
		// 通常会話発頻度(待機時会話が発生するtic数)
		@ConfigProperty(category="Imouto",comment="conf.talkspan", max="9223372036854775807", min="0")
		public static long TalkSpan = 600;
		// プレイヤーの呼び方
		@ConfigProperty(category="Imouto",comment="conf.callplayer")
		public static String CallPlayer = "";
		// 自分の呼び方
		@ConfigProperty(category="Imouto",comment="conf.callme")
		public static String CallMe = "";
	}

	public static class CfLittleSister{
		// 通常会話発生率(待機時会話が発生する確率)
		@ConfigProperty(category="LittleSyster",comment="conf.talkfreq", max="100", min="0")
		public static int TalkFreq = 100;
		// 通常会話発頻度(待機時会話が発生するtic数)
		@ConfigProperty(category="LittleSyster",comment="conf.talkspan",  max="9223372036854775807", min="0")
		public static long TalkSpan = 600;
		// プレイヤーの呼び方
		@ConfigProperty(category="LittleSyster",comment="conf.callplayer")
		public static String CallPlayer = "";
		// 自分の呼び方
		@ConfigProperty(category="LittleSyster",comment="conf.callme")
		public static String CallMe = "";
	}

	public static class CfLittleLady{
		// 通常会話発生率(待機時会話が発生する確率)
		@ConfigProperty(category="LittleLady",comment="conf.talkfreq", max="100", min="0")
		public static int TalkFreq = 100;
		// 通常会話発頻度(待機時会話が発生するtic数)
		@ConfigProperty(category="LittleLady",comment="conf.talkspan", max="9223372036854775807", min="0")
		public static long TalkSpan = 600;
		// プレイヤーの呼び方
		@ConfigProperty(category="LittleLady",comment="conf.callplayer")
		public static String CallPlayer = "";
		// 自分の呼び方
		@ConfigProperty(category="LittleLady",comment="conf.callme")
		public static String CallMe = "";
	}


}