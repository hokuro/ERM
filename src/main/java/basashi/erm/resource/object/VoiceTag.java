package basashi.erm.resource.object;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.text.translation.I18n;

public class VoiceTag {
//	・つぶやき
	// 通常時のボイス
	public static final ResourceTag voice_living = new ResourceTag("living",I18n.translateToLocal("soundresource.living.disc"),"");
	// 待機時のボイス
	public static final ResourceTag voice_living_waite = new ResourceTag("living_waite",I18n.translateToLocal("sourndresource.living_waite.disc"),"");
	// 空腹時
	public static final ResourceTag voice_living_hungry = new ResourceTag("living_hungry",I18n.translateToLocal("sourndresource.living_hungry.disc"),"");

//	・挨拶
	// おはようボイス
	public static final ResourceTag voice_living_morning = new ResourceTag("living_morning",I18n.translateToLocal("soundresource.living_morning.disc"),"");
	// おやすみボイス
	public static final ResourceTag voice_living_night = new ResourceTag("living_night",I18n.translateToLocal("soundresource.living_night.disc"),"");

//	・お手伝い
	// お手伝い開始のボイス
	public static final ResourceTag voice_work_start = new ResourceTag("work_start",I18n.translateToLocal("soundresource.work_start.disc"),"");
	// 疲れてお手伝いを辞める時のボイス
	public static final ResourceTag voice_work_stop = new ResourceTag("work_stop",I18n.translateToLocal("soundresource.work_stop.disc"),"");
	// 作業に必要な道具がなくなった時のボイス
	public static final ResourceTag voice_work_tool_missing = new ResourceTag("work_tool_missing",I18n.translateToLocal("soundresource.work_tool_missing.disc"),"");
	// 作業に必要なアイテムがなくなった時のボイス
	public static final ResourceTag voice_work_item_missing = new ResourceTag("work_item_missing",I18n.translateToLocal("soundresource.work_item_missing.disc"),"");
	// お手伝いを断るときのボイス
	public static final ResourceTag voice_work_turndown_intarest = new ResourceTag("work_turndown_intarest",I18n.translateToLocal("soundresource.work_turndown_intarest.disc"),"");
	// 空腹でお手伝いを断るときのボイス
	public static final ResourceTag voice_work_turndown_hungry = new ResourceTag("work_turndown_hungry",I18n.translateToLocal("soundresource.work_turndown_hungry.disc"),"");
	// スタミナが足りなくてお手伝いを断るとき
	public static final ResourceTag voice_work_turndown_stamina = new ResourceTag("work_turndown_stamina",I18n.translateToLocal("soundresource.work_turndown_stamina.disc"),"");
	// スタミナ切れでお手伝いを辞める時のボイス
	public static final ResourceTag voice_work_stop_stamina = new ResourceTag("work_stop_stamina",I18n.translateToLocal("soundresource.work_stop_stamina.disc"),"");
	// 空腹でお手伝いを辞める時のボイス
	public static final ResourceTag voice_work_stop_hungry = new ResourceTag("work_stop_hungry",I18n.translateToLocal("soundresource.work_stop_hungry.disc"),"");
	// アイテム切れでお手伝いを辞める時のボイス
	public static final ResourceTag voice_work_stop_item = new ResourceTag("work_stop_item",I18n.translateToLocal("soundresource.work_stop_item.disc"),"");
	// ツール切れでお手伝いを辞める時のボイス
	public static final ResourceTag voice_work_stop_tool = new ResourceTag("work_stop_tool",I18n.translateToLocal("soundresource.work_stop_tool.disc"),"");


//	・ダメージ
	// ダメージを受けた時のボイス
	public static final ResourceTag voice_damage = new ResourceTag("damage",I18n.translateToLocal("soundresource.damage.disc"),"");
	// 燃えているときのボイス
	public static final ResourceTag voice_damage_fire = new ResourceTag("damage_fire",I18n.translateToLocal("soundresource.damage_fire.disc"),"");
	// 落下ダメージを受けた時のボイス
	public static final ResourceTag voice_damage_fall = new ResourceTag("damage_fall",I18n.translateToLocal("soundresource.damage_fall.disc"),"");
	// 主人に攻撃された際のボイス
	public static final ResourceTag voice_damage_owner = new ResourceTag("damage_owner",I18n.translateToLocal("soundresource.damage_owner.disc"),"");
	// 窒息ダメージを受けた時のボイス
	public static final ResourceTag voice_damage_suffocation = new ResourceTag("damage_suffocation",I18n.translateToLocal("soundresource.damage_suffocation.disc"),"");
	// サボテンによるダメージを受けた時のボイス
	public static final ResourceTag voice_damage_cactus = new ResourceTag("damage_cactus",I18n.translateToLocal("soundresource.damage_cactus.disc"),"");
	// 落雷によるダメージを受けた時のボイス
	public static ResourceTag voice_damage_lightning = new ResourceTag("damage_lightning",I18n.translateToLocal("soundresource.damage_lightning.disc"),"");

//	・戦闘
	// 敵性モブを発見した時のボイス
	public static final ResourceTag voice_detect_monster = new ResourceTag("detect_monster",I18n.translateToLocal("soundresource.detect_monster.disc"),"");
	// 攻撃するときのボイス
	public static final ResourceTag voice_attack = new ResourceTag("attack",I18n.translateToLocal("soundresource.attack.disc"),"");
	// 勝利した時のボイス
	public static final ResourceTag voice_win = new ResourceTag("win",I18n.translateToLocal("soundresource.win.disc"),"");
	// 死亡した時のボイス
	public static final ResourceTag voice_dead = new ResourceTag("dead",I18n.translateToLocal("soundresource.dead.disc"),"");
	// 主人に殺された時のボイス
	public static final ResourceTag voice_dead_owner = new ResourceTag("dead_owner", I18n.translateToLocal("soundresource.dead_owner.disc"),"");


//	・食事
	// 大好きなキャンディを貰った時のボイス
	public static final ResourceTag voice_candy_love = new ResourceTag("candy_love",I18n.translateToLocal("soundresource.candy_love.disc"),"");
	// 好きなキャンディを貰った時のボイス
	public static final ResourceTag voice_candy_yummy = new ResourceTag("candy_yummy",I18n.translateToLocal("soundresource.candy_yummy.disc"),"");
	// キャンディを貰った時のボイス
	public static final ResourceTag voice_candy_good = new ResourceTag("candy_good",I18n.translateToLocal("soundresource.candy_good.disc"),"");
	// あまり好きじゃないキャンディを貰った時のボイス
	public static final ResourceTag voice_candy_bad = new ResourceTag("candy_bad",I18n.translateToLocal("soundresource.candy_bad.disc"),"");
	// 食べ物を貰った時のボイス
	public static final ResourceTag voice_food_eat = new ResourceTag("food_eat",I18n.translateToLocal("soundresource.food_eat.disc"),"");
	// 食べ物を貰った時のボイス(満腹)
	public static final ResourceTag voice_food_satiety = new ResourceTag("food_satiety",I18n.translateToLocal("soundresource.food_satiety.disc"),"");



//	// チェストに物を入れる時のボイス
//	public static final ResourceTag voice_collect_chest = new ResourceTag("collect_chest",I18n.translateToLocal("soundresource.collect_chest.disc"),"");
//	// 木を切るときのボイス
//	public static final ResourceTag voice_woodcuttor = new ResourceTag("woodcuttor",I18n.translateToLocal("soundresource.woodcuttor.disc"),"");
//	// 作物を収穫するときのボイス
//	public static final ResourceTag voice_harvest = new ResourceTag("harvest",I18n.translateToLocal("soundresource.harvest.disc"),"");
//	// 釣竿を垂らすときのボイス
//	public static final ResourceTag voice_fissing_start = new ResourceTag("fissing_start",I18n.translateToLocal("soundresource.fissing_start.disc"),"");
//	// 魚を待っているときのボイス
//	public static final ResourceTag voice_fissing_waite = new ResourceTag("fissing_waite",I18n.translateToLocal("soundresource.fissing_waite.disc"),"");
//	// 釣竿を引き上げる時のボイス
//	public static final ResourceTag voice_fissing = new ResourceTag("fissing",I18n.translateToLocal("soundresource.fissing.disc"),"");
//	// 魚が釣れた時のボイス
//	public static final ResourceTag voice_fissing_fish = new ResourceTag("fissing_fish",I18n.translateToLocal("soundresource.fissing_fish.disc"),"");
//	// ゴミが釣れた時のボイス
//	public static final ResourceTag voice_fissing_trash = new ResourceTag("fissing_trash",I18n.translateToLocal("soundresource.fissing_trash.disc"),"");
//	// お宝が釣れた時のボイス
//	public static final ResourceTag voice_fissing_treasure = new ResourceTag("fissing_treasure",I18n.translateToLocal("soundresource.fissing_treasure.disc"),"");
//	// なにもつれなかった時のボイス
//	public static final ResourceTag voice_fissing_none = new ResourceTag("fissing_none",I18n.translateToLocal("soundresource.fissing_none.disc"),"");
//	// 動物をて名づける時のボイス
//	public static final ResourceTag voice_animal_tame = new ResourceTag("animal_tame",I18n.translateToLocal("soundresource.animal_tame.disc"),"");
//	// 動物を増やすときのボイス
//	public static final ResourceTag voice_animal_grow = new ResourceTag("animal_grow",I18n.translateToLocal("soundresource.animal_grow.disc"),"");
//	// 羊の毛を借るときのボイス
//	public static final ResourceTag voice_animal_cut = new ResourceTag("animal_cut",I18n.translateToLocal("soundresource.animal_cut.disc"),"");
//	// 牛の乳を搾るときのボイス
//	public static final ResourceTag voice_animal_milk = new ResourceTag("animal_milk",I18n.translateToLocal("soundresource.animal_milk.disc"),"");
//	// 雪玉を集めているときのボイス
//	public static final ResourceTag voice_snowball_collect = new ResourceTag("snowball_collect",I18n.translateToLocal("soundresource.snowball_collect.disc"),"");
//	// 雪玉を投げる時のボイス
//	public static final ResourceTag voice_snowball_throw = new ResourceTag("snowball_throw",I18n.translateToLocal("soundresource.snowball_throw.disc"),"");
//	// 雪玉を充てられた時のボイス
//	public static final ResourceTag voice_snowball_hit = new ResourceTag("snowball_hit",I18n.translateToLocal("soundresource.snowball_hit.disc"),"");
//	// かまどに材料を入れる時のボイス
//	public static final ResourceTag voice_fuencer_in = new ResourceTag("fuencer_in",I18n.translateToLocal("soundresource.fuencer_in.disc"),"");
//	// かまどに燃料を入れる時のボイス
//	public static final ResourceTag voice_fuencer_fuel = new ResourceTag("fuencer_in",I18n.translateToLocal("soundresource.fuencer_in.disc"),"");
//	// かまどからできたものを取り出すときのボイス
//	public static final ResourceTag voice_fuencer_out = new ResourceTag("fuencer_out",I18n.translateToLocal("soundresource.fuencer_out.disc"),"");
//	// 醸造台に材料を入れる時のボイス
//	public static final ResourceTag voice_potion_in = new ResourceTag("potion_in",I18n.translateToLocal("soundresource.potion_in.disc"),"");
//	// 醸造台にポーションを設置するときのボイス
//	public static final ResourceTag voice_potion_set = new ResourceTag("potion_set",I18n.translateToLocal("soundresource.potion_set.disc"),"");
//	// 醸造台からポーションを取り出すときのボイス
//	public static final ResourceTag voice_potion_out = new ResourceTag("potion_out",I18n.translateToLocal("soundresource.potion_out.disc"),"");
//	// 目的のポーションが完成した時のボイス
//	public static final ResourceTag voice_potion_comp = new ResourceTag("potion_comp",I18n.translateToLocal("soundresource.potion_comp.disc"),"");
//	// トーチを置くときのボイス
//	public static final ResourceTag voice_touch_set = new ResourceTag("touch_set",I18n.translateToLocal("soundresource.touch_set.disc"),"");

	// タグ一覧
	private List<ResourceTag> voiceTags = new ArrayList<ResourceTag>();

	/**
	 * コンストラクタ
	 */
	public VoiceTag(){
		voiceTags.add(voice_attack);
		voiceTags.add(voice_candy_bad);
		voiceTags.add(voice_candy_good);
		voiceTags.add(voice_candy_love);
		voiceTags.add(voice_candy_yummy);
		voiceTags.add(voice_damage);
		voiceTags.add(voice_damage_cactus);
		voiceTags.add(voice_damage_fall);
		voiceTags.add(voice_damage_fire);
		voiceTags.add(voice_damage_lightning);
		voiceTags.add(voice_damage_owner);
		voiceTags.add(voice_damage_suffocation);
		voiceTags.add(voice_dead);
		voiceTags.add(voice_dead_owner);
		voiceTags.add(voice_detect_monster);
		voiceTags.add(voice_food_eat);
		voiceTags.add(voice_food_satiety);
		voiceTags.add(voice_living);
		voiceTags.add(voice_living_hungry);
		voiceTags.add(voice_living_morning);
		voiceTags.add(voice_living_night);
		voiceTags.add(voice_living_waite);
		voiceTags.add(voice_win);
		voiceTags.add(voice_work_item_missing);
		voiceTags.add(voice_work_start);
		voiceTags.add(voice_work_stop);
		voiceTags.add(voice_work_stop_hungry);
		voiceTags.add(voice_work_stop_item);
		voiceTags.add(voice_work_stop_stamina);
		voiceTags.add(voice_work_stop_tool);
		voiceTags.add(voice_work_tool_missing);
		voiceTags.add(voice_work_turndown_hungry);
		voiceTags.add(voice_work_turndown_intarest);
		voiceTags.add(voice_work_turndown_stamina);


//		soundTags.add(voice_collect_chest);
//		soundTags.add(voice_woodcuttor);
//		soundTags.add(voice_harvest);
//		soundTags.add(voice_fissing_start);
//		soundTags.add(voice_fissing_waite);
//		soundTags.add(voice_fissing);
//		soundTags.add(voice_fissing_fish);
//		soundTags.add(voice_fissing_trash);
//		soundTags.add(voice_fissing_treasure);
//		soundTags.add(voice_fissing_none);
//		soundTags.add(voice_animal_tame);
//		soundTags.add(voice_animal_grow);
//		soundTags.add(voice_animal_cut);
//		soundTags.add(voice_animal_milk);
//		soundTags.add(voice_snowball_collect);
//		soundTags.add(voice_snowball_throw);
//		soundTags.add(voice_snowball_hit);
//		soundTags.add(voice_fuencer_in);
//		soundTags.add(voice_fuencer_fuel);
//		soundTags.add(voice_fuencer_out);
//		soundTags.add(voice_potion_in);
//		soundTags.add(voice_potion_set);
//		soundTags.add(voice_potion_out);
//		soundTags.add(voice_potion_comp);
//		soundTags.add(voice_touch_set);
	}

	/**
	 * リソースタグを追加
	 * @param tag
	 */
	public void addResourceTag(ResourceTag tag){
		voiceTags.add(tag);
	}

	/**
	 * リソースタグの一覧を取り出す
	 * @return
	 */
	public List<ResourceTag> getResourceTag(){
		return voiceTags;
	}
}
