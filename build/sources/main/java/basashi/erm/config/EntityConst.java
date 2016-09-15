package basashi.erm.config;

import basashi.erm.entity.EntityERMBase;
import basashi.erm.entity.EntityLittleGirl;

public class EntityConst {

	// 幼女
	public static final EntityBaseValue littleGirl = new EntityBaseValue(
			80F,120F,100F,
			1.5F,1.5F,20F);
	// 少女
	public static final EntityBaseValue girl = new EntityBaseValue(
			100F,100F,80F,
			0.8F,1.0F,10F);
	// お嬢様
	public static final EntityBaseValue littlelady = new EntityBaseValue(
			60F,130F,150F,
			0.6F,2.0F,30F);
	// 妹
	public static final EntityBaseValue littlsister = new EntityBaseValue(
			120F,100F,80F,
			0.8F,2.5F,10F);
	// イモウト
	public static final EntityBaseValue littlsisutaa = new EntityBaseValue(
			200F,200F,0F,
			7F,1.0F,5F);


	public static EntityBaseValue getEntityBaseValue(EntityERMBase entity){
		if (entity instanceof EntityLittleGirl){
			return littleGirl;
		}
//		else if(entity instanceof EntityGirl){
//			return girl;
//		}else if(entity instanceof EntityLittleLady){
//			return littlelady;
//		}else if(entity instanceof EntityLittleSister){
//			return littleSister;
//		}else if(entity instancof EntityLittleSisutaa){
//			return littlsisutaa;
//		}
		return girl;
	}

	public static class EntityBaseValue{
		private float bstamina;
		private float bintarest;
		private float brebellious;
		private float mlove;
		private float munlove;
		private float munitarest;

		public EntityBaseValue(
				float bstamina,
				float bintarest,
				float brebellious,
				float mlove,
				float munlove,
				float munintarest){
			this.bstamina = bstamina;
			this.bintarest = bintarest;
			this.brebellious = brebellious;
			this.mlove = mlove;
			this.munlove = munlove;
			this.munitarest = munintarest;
		}


		public float getbstamina(){return this.bstamina;}
		public float getbintarest(){return this.bintarest;}
		public float getbrebellious(){return this.brebellious;}
		public float getmlove(){return this.mlove;}
		public float getmunlove(){return this.munlove;}
		public float getmunintarest(){return this.munitarest;}
	}
}
