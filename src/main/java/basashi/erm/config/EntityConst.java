//package basashi.erm.config;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import basashi.erm.entity.EntityImouto;
//import basashi.erm.entity.EntityLittleGirl;
//
//public class EntityConst {
//	// 懐き度最大値
//	public static final float LOVE_MAX = 500F;
//	// 空腹最大値
//	public static final float FOOD_MAX = 20.0F;
//	// キャンディ最大数
//	public static final int CANDY_MAX = 5;
//	// 会話最大数
//	public static final int TALK_MAX = 5;
//
//	// スタミナ基礎、興味基礎、わがまま基礎(お仕事キャンセルの確率)、
//	// 疲労回復(tic), 疲労蓄積(tic), 記憶(お仕事数)
//	// 懐きやすさ(倍率)、不信(倍率)、飽きっぽさ(倍率)
//	// 小さな女の子
//	public static final EntityBaseValue littleGirl = new EntityBaseValue(
//			80F,120F,90,
//			20,10,5,
//			1.5F,1.5F,2.0F);
//	// 少女
//	public static final EntityBaseValue girl = new EntityBaseValue(
//			100F,100F,60,
//			30,30,20,
//			0.8F,1.0F,1.5F);
//	// お嬢様
//	public static final EntityBaseValue littlelady = new EntityBaseValue(
//			60F,130F,80,
//			10,10,10,
//			0.6F,2.0F,1.8F);
//	// 妹
//	public static final EntityBaseValue littlsister = new EntityBaseValue(
//			120F,100F,60,
//			20,10,10,
//			0.8F,2.5F,1.0F);
//	// イモウト
//	public static final EntityBaseValue imouto = new EntityBaseValue(
//			200F,200F,0,
//			10,40,1,
//			7F,1.0F,0.5F);
//
//	// リスト
//	private static final Map<String, EntityBaseValue> EntityStatusFromString = new HashMap<String,EntityBaseValue>(){
//		{put(EntityLittleGirl.NAME,littleGirl);}
//		{put(EntityImouto.NAME,imouto);}
//	};
//
//
//	/**
//	 * エンティティの名前からステータスを取り出す
//	 * @param name
//	 * @return
//	 */
//	public static EntityBaseValue getEntityBaseValue(String name){
//		return EntityStatusFromString.get(name);
//	}
//
//	public static class EntityBaseValue{
//		// スタミナ
//		private float bstamina;
//		// 興味
//		private float bintarest;
//		// 我儘
//		private int brebellious;
//		// 信頼
//		private float mlove;
//		// 不信
//		private float munlove;
//		// 飽き
//		private float munitarest;
//		// スタミナ回復速度
//		private int reflesh;
//		// 疲労速度
//		private int tired;
//		// 記憶
//		private int memory;
//
//		/**
//		 * コンストラクタ
//		 * @param bstamina
//		 * @param bintarest
//		 * @param brebellious
//		 * @param reflesh
//		 * @param tired
//		 * @param memory
//		 * @param mlove
//		 * @param munlove
//		 * @param munintarest
//		 */
//		public EntityBaseValue(
//				float bstamina,
//				float bintarest,
//				int brebellious,
//				int   reflesh,
//				int   tired,
//				int   memory,
//				float mlove,
//				float munlove,
//				float munintarest){
//			this.bstamina = bstamina;
//			this.bintarest = bintarest;
//			this.brebellious = brebellious;
//			this.mlove = mlove;
//			this.munlove = munlove;
//			this.munitarest = munintarest;
//			this.reflesh = reflesh;
//			this.tired = tired;
//			this.memory = memory;
//		}
//
//
//		public float getbstamina(){return this.bstamina;}
//		public float getbintarest(){return this.bintarest;}
//		public int getbrebellious(){return this.brebellious;}
//		public float getmlove(){return this.mlove;}
//		public float getmunlove(){return this.munlove;}
//		public float getmunintarest(){return this.munitarest;}
//		public int   getreflesh(){return this.reflesh;}
//		public int   gettired(){return this.tired;}
//		public int   getmemoty(){return this.memory;}
//	}
//}
