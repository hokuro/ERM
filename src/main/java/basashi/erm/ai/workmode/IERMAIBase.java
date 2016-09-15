package basashi.erm.ai.workmode;

import java.util.List;

import basashi.erm.entity.EntityERMBase;
import basashi.erm.resource.object.ResourceTag;
import basashi.erm.util.Values.KIND_WORK_CANCEL;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

public interface IERMAIBase {
	/**
	 * AIで追加するボイス識別子の一覧を取り出す
	 * @return
	 */
	public List<ResourceTag> getSoundTag();

	/**
	 * 画面上に表示するAI名を取り出す
	 * @return
	 */
	public String DisplayName();

	/**
	 * AIを識別する名前を取り出す
	 * @return
	 */
	public String AIName();

	/**
	 * 通常の待機ボイスを発生させるかどうか
	 * @return
	 */
	public boolean canSpeak();

	/**
	 * 作業に必要なスタミナ
	 * @return
	 */
	public float workStamina();

	/**
	 * お手伝いに必要なアイテム
	 * @return
	 */
	public ItemStack[] workItem();

	/**
	 * お手伝いに必要なツール
	 * @return
	 */
	public ItemStack workTool();

	/**
	 * AI設定を取り出す
	 * @return
	 */
	public EntityAITasks[] AITasks(EntityAITasks[] tsk, EntityERMBase entity);

	/**
	 * お手伝いを終了する理由を取り出す
	 * @return
	 */
	public KIND_WORK_CANCEL isFinishWork();


	/**
	 * 対象のターゲットを取得する
	 * 全てのモブで共通にするには EntityERMBase.class を返し
	 * 特定のモブ様にするには EntityERMBase を継承したクラスを指定する
	 * @return
	 */
	public Class<? extends EntityERMBase> getTarget();

    /**
     * 毎回実行されます
     */
    public void excute(EntityERMBase entity);

    /**
     * AI開始時に一度だけ呼ばれます
     */
    public void start(EntityERMBase entity);

    /**
     * AI終了時に一度だけ呼ばれます
     */
    public void end(EntityERMBase entity);

	/**
	 * ダメージを受けた時に呼ばれます
	 */
	public void damege(DamageSource par1DamageSource, int par2);

	/**
	 * 落ちているときに呼ばれます
	 */
	public void fall();

	/**
	 * 死んだ時に呼ばれます
	 */
	public void kill();

	/**
	 * Entityと接触した時に呼ばれます
	 */
	public void entityCollision(Entity par1Entity);

	/**
	 * ポータルに入っているときに呼ばれます
	 */
	public void inPortal();

	/**
	 * 蜘蛛の巣に入っているときに呼ばれます
	 */
	public void inWeb();
}
