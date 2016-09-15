package basashi.erm.entity.setting;

import java.util.Iterator;

import basashi.erm.ai.workmode.IERMAIBase;
import basashi.erm.core.ModCommon;
import basashi.erm.core.log.ModLog;
import basashi.erm.entity.EntityERMBase;
import basashi.erm.entity.logic.IntarestWorkList;
import basashi.erm.resource.ERMResourceManager;
import basashi.erm.resource.object.VoiceTag;
import basashi.erm.util.Util;
import basashi.erm.util.Values.KIND_STATUS;
import basashi.erm.util.Values.KIND_WORK_CANCEL;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.translation.I18n;

public class EntityAISetting {
	// AI ファイル名
	private String aiFile;
	// 現在のAI
	private IERMAIBase ai;
	// 待機中かどうか
	private boolean isWaite;
	// ターゲットクラス
	private EntityERMBase target;
	// 前回作業
	private IntarestWorkList memoryWork;


	/**
	 * コンストラクタ
	 * @param entity
	 * @param memory
	 * @param intBase
	 * @param intWeight
	 */
	public EntityAISetting(EntityERMBase entity,
			int memory, float intBase, float intWeight){
		aiFile = "";
		ai = null;
		isWaite = true;
		target = entity;
		memoryWork = new IntarestWorkList(memory, intBase, intWeight);
	}

	/************************************************/
	/** 設定中のリソースの内容に関する処理                                         **/
	/************************************************/
	/**
	 * AIファイル名を取り出す
	 * @return
	 */
	public String FileName(){
		return aiFile;
	}

	/**
	 * AIを識別する文字列を取り出す
	 * @return
	 */
	public String AIID(){
		if (ai != null){
			return aiFile + "\t" + ai.getClass().getName();
		}
		return "";
	}

	/************************************************/
	/** 設定中の状態に関する処理                                                           **/
	/************************************************/
	/**
	 * AIの名前を返す
	 * @return
	 */
	public String AIName(){
		if(ai != null){
			return Util.StringisEmptyOrNull(ai.AIName())?"No Name":ai.AIName();
		}else{
			return I18n.translateToLocal("entity.ainame.default");
		}
	}

	/**
	 * お手伝い中かどうか
	 * @return
	 */
	public boolean doWork(){
		// AIが設定されていればお手伝い中
		// AIが設定されていなければ自由行動
		return (ai!=null);
	}

	/**
	 * 待機状態を取得
	 * @return
	 */
	public boolean isWaite(){
		return isWaite;
	}

	/**
	 * 待機ボイスが発生するかどうか
	 * @return
	 */
	public boolean canSpeak() {
		return ai!=null?ai.canSpeak():true;
	}

	/**
	 * 待機状態を設定
	 * @param waite
	 */
	public void setWaite(boolean waite){
		if(ModCommon.isDebug){ModLog.log().debug("-D log ai wait " + (isWaite?"待機":"アクティブ") + "→" + (waite?"待機":"アクティブ"));}
		isWaite = waite;
	}

	/**
	 * スタミナ減少値
	 * @return
	 */
	public float reduceStamina(){
		return ai.workStamina();
	}

	/**
	 * 記憶している仕事を取得
	 * @return
	 */
	public IntarestWorkList getMemoryList(){
		return memoryWork;
	}

	/**
	 * お手伝いに必要な道具を取得
	 * @return
	 */
	public ItemStack getNextTool(){
		if(ai != null){
			return ai.workTool();
		}
		return null;
	}
	/**
	 * お手伝いに必要なアイテムを取得
	 */
	public ItemStack[] getNextItem() {
		if(ai != null){
			return ai.workItem();
		}
		return null;
	}

	public KIND_WORK_CANCEL doWorkCheck() {
		if(ai != null){
			return ai.isFinishWork();
		}
		return KIND_WORK_CANCEL.CANCEL_NO;
	}


	/************************************************/
	/** リソースの更新                                                                                      **/
	/************************************************/

	/**
	 * AI選択GUIを開く
	 * @return
	 */
	public void openSelectAIGui(){
		// AI選択GUIを開く
	}


	/**
	 * 設定しているAIを更新
	 * @param aiFileName
	 * @param nextAI
	 * @return
	 */
	public boolean setAI(String aiFileName, IERMAIBase nextAI){
		return setAI(aiFileName, nextAI,true);
	}

	/**
	 * 設定しているAIを更新
	 * @param fname
	 * @param ai
	 * @param checkMemory
	 */
	public boolean setAI(String aiFileName,IERMAIBase nextAI, boolean checkMemory){
		if ( nextAI == ai){
			if (nextAI==null){
				// モードに応じてAIを切り替える
				target.clearAI();
				// 設定するAIファイル名が空文字の場合、自由行動(デフォルトのAIでうごく)
				aiFile = "";
				ai = null;
				// デフォルトの行動を設定する
				int pri = 1;
				for(Iterator<EntityAIBase> it = target.getDefaultAI(target).iterator(); it.hasNext();){
					target.tasks.addTask(pri,it.next());
					pri++;
				}

				// TODO: モブの状態を変更
			}else{
				// スタミナ確認
				if (target.getStatus(KIND_STATUS.STATUS_STAMINA).getStatus() <= nextAI.workStamina()){
					// スタミナが足りない
					target.settingVoice().setToneWork(nextAI, VoiceTag.voice_work_turndown_stamina);
					return false;
				}
				// 空腹確認
				if (target.getStatus(KIND_STATUS.STATUS_FOOD).getStatus() < target.getWorkFood()){
					// おなかがすいて力が出ない
					target.settingVoice().setToneWork(nextAI, VoiceTag.voice_work_turndown_hungry);
					return false;
				}

				if(checkMemory && checkWorkIntarest(nextAI,target.MaxIntarest(),target.Rebelious())){
					// モードに応じてAIを切り替える
					target.clearAI();
					if (checkMemory){
						// お手伝いを始めるよ
						target.settingVoice().setToneWork(nextAI, VoiceTag.voice_work_start);
					}
					// 新規AIを実行
					aiFile = aiFileName;
					ai = nextAI;
					ai.excute(target);

					// TODO: モブの状態を変更

				}else{
					// 反抗！ そのお手伝いはいまやりたくない
					// 抗議の声をあげるよ
					target.settingVoice().setToneWork(nextAI, VoiceTag.voice_work_turndown_intarest);
					// デフォルトAIに変更
					setAI("",null);

					// TODO: モブの状態を変更
				}
			}
			return true;
		}else{
			// 以前と同じなので更新しない
			return false;
		}
	}

	/**
	 * 仕事に飽きているかどうか判定
	 * @param nextAI
	 * @return
	 */
	private boolean checkWorkIntarest(IERMAIBase nextAI, float maxintarest, float rebelious){
		boolean dowork = false;
		dowork = memoryWork.checkCanWork(nextAI, maxintarest, rebelious);
		return dowork;
	}


//	public boolean setMaidMode(IERMAIBase nextAI) {
//
//
////		// モード切替に応じた処理系を確保
////		maidAvatar.stopActiveHand();
////		setSitting(false);
////		setSneaking(false);
////		setActiveModeClass(null);
////		aiJumpTo.setEnable(true);
//////		aiFollow.setEnable(true);
////		aiAttack.setEnable(true);
////		aiShooting.setEnable(false);
////		aiAvoidPlayer.setEnable(true);
//////		aiWander.setEnable(maidFreedom);
////		setBloodsuck(false);
////		clearTilePosAll();
////		for (int li = 0; li < maidEntityModeList.size(); li++) {
////			EntityModeBase iem = maidEntityModeList.get(li);
////			if (iem.setMode(maidMode)) {
////				setActiveModeClass(iem);
////				aiFollow.setMinDist(iem.getRangeToMaster(0));
////				aiFollow.setMaxDist(iem.getRangeToMaster(1));
////				break;
////			}
////		}
////		getNextEquipItem();
////
////		return true;
//	}

	/**
	 * ダメージを受けた際の処理
	 * @param source
	 */
	public void Damage(DamageSource source) {
		if(source == DamageSource.inFire || source == DamageSource.onFire || source == DamageSource.lava){
			// TODO:火災ダメージ
			// 待機状態を解除して水を探す
			// 実行中のお手伝いも中断し、消化後はデフォルトAIに戻る
		}else if(source == DamageSource.cactus){
			// TODO:サボテンダメージ
			// 待機状態を解除してサボテンから離れる
			// サボテンから離れた後は再び待機状態に
			// 実行中のお手伝いは中断しない
		}else if(source == DamageSource.inWall){
			// TODO:窒息ダメージ
			// 待機状態を解除して、周りのブロックを破壊
			// ダメージを受けない位置に移動して再度待機
			// 実行中のお手伝いは中断しない
		}else if(source == DamageSource.generic){
			// TODO:モンスターの攻撃？
			// 待機状態を解除
		}else if(source == DamageSource.wither){
			// TODO:ウィザー
			// 牛乳を持っている場合、牛乳で回復
			// 持っていない場合放置してお手伝い続行

		}else{
			// その他
			// 特に何もしない
		}
	}


	/************************************************/
	/** NBT関連の処理                                                                                  **/
	/************************************************/
	/**
	 * NBTから読み込む
	 * @param tagCompound
	 * @param key
	 */
	public void readNBT(NBTTagCompound tagCompound){
		this.isWaite = tagCompound.getBoolean("WATE");       // 待機状態
		this.aiFile  = tagCompound.getString("AIFILE");      // AIファイル名
		String aiName= tagCompound.getString("AINAME");      // AI名

		// デフォルトから別のAI に代わる場合のみ実行
		if (ai==null && Util.StringisEmptyOrNull(aiFile)){
			IERMAIBase nextAI = ERMResourceManager.instance().getAI(target.getClass(), aiFile + "\t" + aiName);
			// NBTからのAI設定の場合、お手伝いの拒否判定はしない
			setAI(aiFile,nextAI,false);
		}
		 this.memoryWork.readNBT(tagCompound, "MEMORY_WORK"); // お仕事記憶
	}

	/**
	 * NBTへ書き込む
	 * @param tagCompound
	 * @param key
	 */
	public void writeNBT(NBTTagCompound tagCompound){
        tagCompound.setBoolean("WATE",    this.isWaite);                              // 待機状態
        tagCompound.setString("AIFILE",   this.aiFile);                               // AIファイル名
        tagCompound.setString ("AINAME",  ai==null?"":ai.getClass().getName());       // AI名
        this.memoryWork.writeNBT(tagCompound, "MEMORY_WORK");                         // お手伝いの記録
	}


	/*******************************************************************************************************/
	/** AI                                                                                                **/
	/*******************************************************************************************************/
	public String debugLog(){
		StringBuilder deb = new StringBuilder();
    	deb.append("==============================================================").append(Util.ReturnCode());
    	deb.append("== AI                                                       ==").append(Util.ReturnCode());
    	deb.append("Wate/Active :").append(this.isWaite?"Wate":"Active").append(Util.ReturnCode());
    	deb.append("AI          :").append(ai!=null?ai.DisplayName():"デフォルト").append((Util.ReturnCode()));
    	deb.append(this.memoryWork.debugLog());
    	deb.append("==============================================================").append(Util.ReturnCode());
    	return deb.toString();
	}




}
