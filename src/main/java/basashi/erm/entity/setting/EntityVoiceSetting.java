package basashi.erm.entity.setting;

import java.io.File;

import basashi.erm.ai.workmode.IERMAIBase;
import basashi.erm.entity.EntityERMBase;
import basashi.erm.item.ItemCandy;
import basashi.erm.resource.object.ResourceTag;
import basashi.erm.resource.object.VoiceTag;
import basashi.erm.resource.tone.ToneTag;
import basashi.erm.util.Util;
import basashi.erm.util.Values.KIND_CANDYTAST;
import basashi.erm.util.Values.KIND_COUNTER;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextComponentString;

public class EntityVoiceSetting {
	// サウンド
	private final EntitySoundSetting sound;
	// トーン
	private final EntityToneSetting  tone;
	// ボヤキの設定
	private final CallMeSetting speakSetting;
	// ターゲット
	private final EntityERMBase target;

	// 次の再生用タグ
	private File nextFile;
	// 次のテキスト
	private String nextText;

    /**
     * コンストラクタ
     * @param entity
     * @param sound
     * @param tone
     * @param clSetting
     */
	public EntityVoiceSetting(EntityERMBase entity, EntitySoundSetting sound, EntityToneSetting tone, CallMeSetting clSetting){
		this.target = entity;
		this.sound = sound;
		this.tone = tone;
		speakSetting = clSetting;
	}

	/*******************************************************/
	/** リソース操作                                                                                                             **/
	/*******************************************************/
	/**
	 * サウンド設定を取り出す
	 * @return
	 */
	public EntitySoundSetting getSound(){
		return sound;
	}

	/**
	 * トーン設定を取り出す
	 * @return
	 */
	public EntityToneSetting getTone(){
		return tone;
	}

	/*******************************************************/
	/** セリフの再生                                                                                                           **/
	/*******************************************************/
	public void setToneWork(IERMAIBase nextAI, ResourceTag tag){
		nextFile = sound.getSound(tag);
		nextText = ToneTag.makeText(target, tone.getText(tag))
				.replace(ToneTag.REP_DISPLAY_AI_NAME, nextAI.DisplayName())
				.replace(ToneTag.REP_DISPLAY_AI_WORKTOOL, (nextAI.workTool()!=null?nextAI.workTool().getDisplayName():""));
		StringBuilder rep = new StringBuilder();
		for (ItemStack stack : nextAI.workItem()){
			if (target.getInventory().getSlotFor(stack) >= 0){
				rep.append(stack.getDisplayName()).append(",");
			}
		}
		if(rep.length() != 0){
			rep.deleteCharAt(rep.length()-1);
		}
		nextText = nextText.replace(ToneTag.REP_DISPLAY_AI_WORKITEM, rep.toString());
	}

	/**
	 * ダメージを受けた際のサウンドまたはテキスト再生を行う
	 * @param source
	 */
	public void damageVoice(DamageSource source, Entity entity){
		ResourceTag tag = VoiceTag.voice_damage;
		if (source == DamageSource.inFire || source == DamageSource.onFire ||
				source == DamageSource.lava){
			// 火
			tag = VoiceTag.voice_damage_fire;
		}else if(source == DamageSource.fall){
			// 落下
			tag = VoiceTag.voice_damage_fall;
		}else if (source == DamageSource.cactus){
			// サボテン
			tag = VoiceTag.voice_damage_cactus;
		}else if(source == DamageSource.drown || source == DamageSource.inWall){
			// 窒息
			tag = VoiceTag.voice_damage_suffocation;
		}else if (source == DamageSource.lightningBolt){
			// カミナリ
			tag = VoiceTag.voice_damage_lightning;
		}else if( entity instanceof EntityLivingBase && target.isOwner((EntityLivingBase)entity)){
			tag = VoiceTag.voice_damage_owner;
		}
		nextFile = sound.getSound(tag);
		nextText = ToneTag.makeText(target, tone.getText(tag));
		if (tag != VoiceTag.voice_damage && nextFile == null && Util.StringisEmptyOrNull(nextText)){
			// 対応するタグに対するボイスがない場合、通常ダメージボイスに変換
			damageVoice(DamageSource.generic,entity);
		}
		if ( entity instanceof EntityLivingBase && target.isOwner((EntityLivingBase)entity)){
			nextText = nextText.replace(ToneTag.REP_DAMAGE_ATTACKER,speakSetting.callYou);
		}else if (entity != null){
			nextText = nextText.replace(ToneTag.REP_DAMAGE_ATTACKER, Util.StringisEmptyOrNull(entity.getCustomNameTag())?entity.getCustomNameTag():entity.getName());
		}
	}

	/**
	 * 死亡時のサウンドまたはテキスト再生を行う
	 * @param source
	 */
	public void dethVoice(DamageSource source, EntityLivingBase attacker){
		if (target.isOwner(attacker)){
			nextFile = sound.getSound(VoiceTag.voice_dead_owner);
			nextText = ToneTag.makeText(target,tone.getText(VoiceTag.voice_dead_owner));
		}else{
			nextFile = sound.getSound(VoiceTag.voice_dead);
			nextText = ToneTag.makeText(target, tone.getText(VoiceTag.voice_dead));
			if (attacker != null){
				nextText = nextText.replace(ToneTag.REP_DAMAGE_ATTACKER, Util.StringisEmptyOrNull(attacker.getCustomNameTag())?attacker.getCustomNameTag():attacker.getName());
			}
		}
	}

	/**
	 * 食事をした際のサウンドまたはテキスト再生を行う
	 * @param food
	 */
	public void eatFood(ItemStack food, ResourceTag tag){
		// ごちそうさまでした
		nextFile = sound.getSound(tag);
		nextText = ToneTag.makeText(target, tone.getText(tag)).replace(ToneTag.REP_DISPLAY_FOOD_NAME, food.getDisplayName());
	}

	/**
	 * キャンディを食べた際のサウンドまたはテキスト再生を行う
	 * @param fav
	 */
	public void eatCandy(KIND_CANDYTAST fav, ItemStack candy){
		ResourceTag tag = VoiceTag.voice_candy_good;
		switch(fav){
		case LOVE:
			tag = VoiceTag.voice_candy_love;
			break;
		case YUMMY:
			tag = VoiceTag.voice_candy_yummy;
			break;
		case GOOD:
			tag = VoiceTag.voice_candy_good;
			break;
		case BAD:
			tag = VoiceTag.voice_candy_bad;
			break;
		}

		nextFile = sound.getSound(tag);
		nextText = ToneTag.makeText(target, tone.getText(tag)).replace(ToneTag.REP_DISPLAY_CANDY_NAME, ((ItemCandy)candy.getItem()).getCandyName(candy));
	}

	/**
	 * モブを殺した隊のサウンドまたはテキスト再生を行う
	 * @param taget
	 */
	public void killEntity(EntityLivingBase taget){
		nextFile = sound.getSound(VoiceTag.voice_win);
		nextText = ToneTag.makeText(target, tone.getText(VoiceTag.voice_win))
					.replace(ToneTag.REP_DAMAGE_ATTACKER, Util.StringisEmptyOrNull(taget.getCustomNameTag())?taget.getCustomNameTag():taget.getName());
	}

	/**
	 * ボイスを再生する
	 * @param entity
	 */
	public void speakVoice(EntityERMBase entity){
		// 次の再生ボイスが決まっているなら、そちらを優先
		if (playNextVoice()){ return; }

		// つぶやき再生
		if (speakSetting.speakFreq == 0 || !entity.settingAI().canSpeak()){ return; }
		// 周期を過ぎているか確認
		if (entity.getTickCounter().onFire(KIND_COUNTER.COUNTER_SPEAK, speakSetting.speakSpan, true)){
			// つぶやきが発生するか確認
			if (Util.random(100) <= speakSetting.speakFreq){
				if (entity.settingAI().isWaite()){
					// 待機中の鳴き声
					playVoice(VoiceTag.voice_living_waite);
				}else{
					// 通常の鳴き声
					playVoice(VoiceTag.voice_living);
				}
			}
		}
	}

	/**
	 * サウンドを再生する
	 * @param key
	 */
	protected void playVoice(ResourceTag tag){
		nextFile = sound.getSound(tag);
		nextText = ToneTag.makeText(target, tone.getText(tag));
		playNextVoice();
	}

	/**
	 * 内部設定されているボイスを再生
	 * @return
	 */
	public boolean playNextVoice(){
		boolean ret = false;
		// サウンド再生優先
		if (nextFile != null){
			// TODO: サウンド再生
			ret = true;
		}else if(Util.StringisEmptyOrNull(nextText)){
			// テキスト再生
			target.addChatMessage(new TextComponentString(nextText));
			ret = true;
		}
		nextFile = null;
		nextText = null;
		return ret;
	}




	/****************************************************************************************************/
	/** 音声再生                                                                                       **/
	/****************************************************************************************************/
	/**
	 * 音声再生用。
	 * 通常の再生ではネットワーク越しになるのでその対策。
	 */
	public void playLittleMaidSound(boolean force) {
		// 音声の再生
		if (target.worldObj.isRemote) {
			// Client
			target.worldObj.playSound(target.posX, target.posY, target.posZ,
					new SoundEvent(new ResourceLocation(nextFile.getName())),
					target.getSoundCategory(),
					1.0F, 1.0F, false);
		}
	}


	/****************************************************************************************************/
	/** 音声再生                                                                                       **/
	/****************************************************************************************************/
	/**
	 * NBTから読み込む
	 * @param tagCompound
	 * @param key
	 */
	public void readNBT(NBTTagCompound tagCompound){
        String work;
        work = tagCompound.getString ("VOICE");         // テクスチャファイル
        if (!sound.SoundFile().equals(work)){
        	sound.SoundFile(work);
        }

        work = tagCompound.getString ("SPEECH");       // テクスチャ設定ファイル
        if (!tone.ToneFile().equals(work)){
        	tone.ToneFile(work);
        }
	}

	/**
	 * NBTへ書き込む
	 * @param tagCompound
	 * @param key
	 */
	public void writeNBT(NBTTagCompound tagCompound){
	    tagCompound.setString ("VOICE",   sound.SoundFile());  // AI音声
	    tagCompound.setString ("SPEECH",  tone.ToneFile());    // セリフテキスト

	}


	public static class CallMeSetting{
		public int speakFreq;
		public long speakSpan;
		public final String callMe;
		public final String callYou;

		public CallMeSetting(int freq, long talkSpan, String me, String you){
			speakFreq = freq;
			speakSpan = talkSpan;
			callMe = me;
			callYou = you;
		}
	}

	public String debugLog(){
		StringBuilder deb = new StringBuilder();
    	deb.append("==============================================================").append(Util.ReturnCode());
    	deb.append("== Voicd                                                    ==").append(Util.ReturnCode());
    	deb.append("nextFile :").append(this.nextFile!=null?nextFile.getName():"Null").append(Util.ReturnCode());
    	deb.append("nextText :").append(this.nextText!=null?nextText:"").append((Util.ReturnCode()));
    	deb.append("==============================================================").append(Util.ReturnCode());
    	return deb.toString();
	}
}
