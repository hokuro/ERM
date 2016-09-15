package basashi.erm.util;

public class Values {
	/**
	 * ステータスの識別子
	 * @author rayarc
	 *
	 */
	public static enum KIND_STATUS{
		/** 空腹度 */
		STATUS_FOOD,
		/** スタミナ */
		STATUS_STAMINA,
		/** 信頼度 */
		STATUS_LOVE,
		/** キャンディ残数 */
		STATUS_CANDY,
		/** 会話残数 */
		STATUS_TALK,
		/** 経験値 */
		STATUS_EXP
	}


	/**
	 * お手伝いを断るまたは中断する理由
	 * @author rayarc
	 *
	 */
	public static enum KIND_WORK_CANCEL{
		/** キャンセルしない **/
		CANCEL_NO,
		/** スタミナ切れ **/
		CANCEL_STAMINA,
		/** 空腹 **/
		CANCEL_FOOD,
		/** 作業用アイテムがなくなった **/
		CANCEL_WORKITEM,
		/** 作業用ツールがなくなった **/
		CANCEL_WORKTOOL,
		/** 興味がなくなった **/
		CANCEL_INTAREST,
		/** みっしょんこんぷりーと **/
		CANCEL_FINISH
	}


	/**
	 * キャンディの好み
	 * @author rayarc
	 *
	 */
	public static enum KIND_CANDYTAST{
		LOVE,	// 大好き
		YUMMY,	// 好き
		GOOD,	// 普通
		BAD		// あまり好きじゃない
	}


	/**
	 * カウンター
	 * @author nesuken
	 *
	 */
	public static enum KIND_COUNTER{
		// スタミナ回復カウンタ
		COUNTER_STAMINA,
		// 体力自然回復カウンタ
		COUNTER_FOOD_HELTHHEAL,
		// 空腹減少カウンタ
		COUNTER_FOOD_REDUCE,
		// つぶやきインターバルカウンタ
		COUNTER_SPEAK,
		// 会話がないことによる信頼減少
		COUNTER_LOVEREDUCE_TALK,
		// キャンディをもらえないことによる信頼減少
		COUNTER_LOVEREDUCE_CANDY,
		// 放置による信頼減少
		COUNTER_LOVE_WATETIME,
		// 会話回数回復
		COUNTER_HEALTALK,
		// キャンディ回数回復
		COUNTER_HEALCANDY,
		// 最後にダメージを受けてからのカウント
		COUNTER_LASTDAMAGE(false);

		// アップカウンタかどうか
		public final boolean isUpCounter;

		// 初期値
		public final long cntDefault;

		private KIND_COUNTER(){
			this(true,0L);
		}

		private KIND_COUNTER(boolean isUp){
			this(isUp,0L);
		}
		/**
		 * コンストラクタ
		 * @param isUpCounter
		 */
		private KIND_COUNTER(boolean isUp, long def){
			isUpCounter = isUp;
			cntDefault = def;
		}
	}


	/**
	 * 検索するポーションの種類
	 * @author nesuken
	 *
	 */
	public static enum KIND_SEARCHIPOTION{
		// 両方
		ALL,
		// 通常
		NORMAL,
		// スプラッシュ
		SPLASH,
		//
		LINGERING
	}

	/**
	 * リソースの種類
	 * @author rayarc
	 *
	 */
	public enum KIND_RESOURCE {
		// デフォルトリソース
		DEFAULT,
		// カスタムリソース
		CUSTOM
	}

}
