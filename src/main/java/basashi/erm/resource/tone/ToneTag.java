package basashi.erm.resource.tone;


import basashi.erm.entity.EntityERMBase;
import basashi.erm.resource.object.ResourceTag;
import basashi.erm.resource.object.VoiceTag;
import basashi.erm.util.Util;
import net.minecraft.util.text.translation.I18n;

public class ToneTag extends VoiceTag {
	/****************************************************/
	/** 置換情報                                                                                                         **/
	/****************************************************/
	// x座標
	public static final String REP_POS_X = "{pos_x}";
	// y座標
	public static final String REP_POS_Y = "{pos_y}";
	// z座標
	public static final String REP_POS_Z = "{pos_z}";
	// プレイヤーの呼び方
	public static final String REP_CALL_PLAYER = "{player}";
	// 自分の呼び方
	public static final String REP_CALL_ME = "{me}";
	// 天候
	public static final String REP_WETHER ="{wether}";
	// 時間
	public static final String REP_TIME = "{time}";
	// 改行
	public static final String REP_NEWLINE ="{n}";
	// AI名
	public static final String REP_DISPLAY_AI_NAME = "{AINAME}";
	// 作業用ツール
	public static final String REP_DISPLAY_AI_WORKTOOL = "{WORKTOOL}";
	// 作業用アイテム
	public static final String REP_DISPLAY_AI_WORKITEM = "{WORKITEM}";
	// 攻撃者
	public static final String REP_DAMAGE_ATTACKER = "{ATTACKER}";
	// 食べ物
	public static final String REP_DISPLAY_FOOD_NAME = "{FOODNAME}";
	// キャンディ
	public static final String REP_DISPLAY_CANDY_NAME = "{DANDYNAME}";


	/****************************************************/
	/** 条件情報                                                                                                         **/
	/****************************************************/
	public static final String IF_WETHER ="ifWether=";
	public static final String IF_TIME = "ifTime=";
	public static final String IF_DIM = "ifDim=";



	// 会話
	public static final ResourceTag voice_talk = new ResourceTag("talk","","");
	// 会話回数が尽きた後の会話
	public static ResourceTag voice_talk_nocnt = new ResourceTag("talk_nocnt","","");
	// 自分の位置を知らせる
	public static final ResourceTag voice_callposition = new ResourceTag("call_position","","");


	// インスタンス
	public static final ToneTag instance = new ToneTag();

	/**
	 * コンストラクタ
	 */
	public ToneTag(){
		super();
		getResourceTag().add(voice_talk);
		getResourceTag().add(voice_talk_nocnt);
		getResourceTag().add(voice_callposition);
	}


	/**
	 * 文字列を置換して返す
	 * @param entity
	 * @param baseStr
	 * @return
	 */
	public static String makeText(EntityERMBase entity, String baseStr){
		String customStr = "";
		customStr = baseStr.replace(REP_POS_X, Integer.toString(Util.RoundScaleToInt(entity.posX,0)))
				.replace(REP_POS_Y, Integer.toString(Util.RoundScaleToInt(entity.posY,0)))
				.replace(REP_POS_Z, Integer.toString(Util.RoundScaleToInt(entity.posZ,0)))
				.replace(REP_CALL_PLAYER, entity.getVoiceSetting().callYou)
				.replace(REP_CALL_PLAYER, entity.getVoiceSetting().callMe)
				.replace(REP_WETHER,
						entity.worldObj.isRaining()?
								(entity.worldObj.canSnowAt(entity.getPosition(), true)?I18n.translateToLocal("chat.wether.yuki"):I18n.translateToLocal("chat.wether.ame")):												// 雪 or 雨
									entity.worldObj.isThundering()?I18n.translateToLocal("chat.wether.arashi"):																						// カミナリ
										I18n.translateToLocal("chat.wether.hare"))																													// 晴れ
				.replace(REP_TIME, String.format("%04d",Util.getWorldTime(entity.worldObj.getWorldTime())))                                          // 時間
				.replace(REP_NEWLINE, Util.ReturnCode());                                                                                                                   // 改行
		return customStr;
	}

}
