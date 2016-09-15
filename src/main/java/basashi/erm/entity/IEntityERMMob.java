package basashi.erm.entity;

import java.util.List;
import java.util.Map;

import basashi.erm.entity.setting.EntityVoiceSetting.CallMeSetting;
import basashi.erm.model.ModelERMBase;
import basashi.erm.resource.object.ResourceTag;
import basashi.erm.resource.textures.EnumTextureParts;
import basashi.erm.resource.textures.SettingCustomTexture.SettingData;
import basashi.erm.util.Values;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.DamageSource;

public interface IEntityERMMob {
	// 懐き度最大値
	public static final float LOVE_MAX = 500F;
	// 満腹度最大値
	public static final float FOOD_MAX = 20.0F;
	// キャンディ残数最大値
	public static final int CANDY_MAX = 5;
	// 会話残数最大値
	public static final int TALK_MAX =5;

	/***************************************************************************/
	/** ステータス基本値の取り出し                                                                                                                                    **/
	/***************************************************************************/
	/**
	 * スタミナの基礎値を取り出す
	 * @return スタミナ基礎値
	 */
	float getBaseStamina();
	/**
	 * 興味の基礎値を取り出す
	 * @return 興味基礎値
	 */
	float getBaseIntarest();
	/**
	 * わがまま度の基礎値を取り出す
	 * @return わがまま度基礎値
	 */
	int getBaseRebelious();
	/**
	 * 懐き度の増減にかかわる重みを取り出す
	 * @return 懐き度のウェイト
	 */
	float getWeightLove();
	/**
	 * 興味の増減にかかわる重みを取り出す
	 * @return 興味のウェイト
	 */
	float getWeightIntarest();
	/**
	 * お手伝いをお願いできる空腹度
	 * @return
	 */
	float getWorkFood();
	/**
	 * 満腹度によるスタミナ減少補正を取り出す
	 * @return
	 */
	float getFoodWieght();
	/**
	 * スタミナが回復する速度(tick数)を取り出す
	 * @return スタミナが回復するまでにかかるtick数
	 */
	int getSpdRefleshStamina();
	/**
	 * スタミナが減少する速度(tick数)を取り出す
	 * @return スタミナが減少するtick数
	 */
	int getSpdTiredStamina();
	/**
	 * 記憶力を取り出す
	 * @return 食べたキャンディや、実行したお手伝いを覚えておく和
	 */
	int getMemory();
	/**
	 * メインインベントリのサイズを取得する
	 * @return
	 */
	int getInventorySize();

	/**
	 * モブの種類名前を取り出す
	 * @return モブの種類
	 */
	String EntityName();

	/**
	 * ネームタグでつけられた名前を取り出す
	 * @return
	 */
	String MobDefaultName();

	/**
	 * キャンディの好みを取り出す
	 * @param candy キャンディ名
	 * @return 好み
	 */
	Values.KIND_CANDYTAST getCandyFavoritte(String candy);

	/***************************************************************************/
	/** 属性                                                                                                                                                   **/
	/***************************************************************************/
	double getConbatSpeed();
	double getSneakingSpeed();

	/***************************************************************************/
	/** リソースの取り出し                                                                                                                                                        **/
	/***************************************************************************/
	/**
	 * デフォルトのAI設定を取り出す
	 * @param mob
	 * @return デフォルトAI
	 */
	List<EntityAIBase> getDefaultAI(EntityERMBase entity);

	/**
	 * デフォルトテクスチャマップを取り出す
	 * @return デフォルトテクスチャマップ
	 */
	Map<EnumTextureParts,SettingData> getDefaultTexture();

	/**
	 * デフォルトトーンマップを取り出す
	 * @return デフォルトトーンマップ
	 */
	Map<ResourceTag,List<String>> getDefaultTone();

	/**
	 * ドレス用テクスチャファイルを取り出す
	 * @return ドレス用テクスチャファイル
	 */
	String getDressTextureFile();

	/**
	 * ドレス用テクスチャファイル設定を取り出す
	 * @return ドレス用テクスチャ設定ファイル
	 */
	String getDressSettingFile();

	/**
	 * アーマー用テクスチャファイルを取り出す
	 * @return アーマー用テクスチャファイル
	 */
	String getAromorTextureFile();

	/**
	 * サウンド設定ファイル名を取り出す
	 * @return サウンド設定ファイル名
	 */
	String getSoundFile();

	/**
	 * 音声設定を取り出す
	 * @return 音声設定
	 */
	CallMeSetting getVoiceSetting();

	/**
	 * 視線の高さを取り出す
	 * @return 視線の高さ
	 */
	float getEyeHeight();

	/**
	 * 幅を取り出す
	 * @return 幅
	 */
	float getWidth();

	/**
	 * 身長を取り出す
	 * @return 身長
	 */
	float getHeight();

	/***************************************************************************/
	/** 固有設定                                                                                                                                                                             **/
	/***************************************************************************/
	/**
	 * メインモデルを取り出す
	 * @return メインモデル
	 */
	ModelERMBase getMainModel();

	/***************************************************************************/
	/** ロジック                                                                                                                                                                            **/
	/***************************************************************************/
	/**
	 * 属性を設定する
	 */
	void applyEntityAttributes(EntityERMBase entity);

    /**
     * キャンディ使用残回数を回復する
     */
	int healCandyCounter();

	/**
	 * 会話鹿野山回数を回復する
	 */
    int healTalkCounter();

	/**
	 * 利き腕
	 * @return
	 */
	int getDominantArm();

	/**
	 * 敵味方識別
	 * @param target
	 * @return
	 */
	boolean getIFF(Entity target);

	/**
	 * 埋葬対策
	 * @param tagCompound
	 */
	boolean pushOutOfBlocks(EntityERMBase entity, double x, double y, double z);

	/**
	 * ダメージによる信頼減少
	 * @param entity
	 * @param source
	 * @param damage
	 */
	void reduceLoveCauseDamage(EntityERMBase entity, DamageSource source, float damage);


}
