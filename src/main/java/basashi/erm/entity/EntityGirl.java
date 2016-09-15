package basashi.erm.entity;

import java.util.List;
import java.util.Map;

import basashi.erm.entity.setting.EntityVoiceSetting.CallMeSetting;
import basashi.erm.model.ModelERMBase;
import basashi.erm.resource.object.ResourceTag;
import basashi.erm.resource.textures.EnumTextureParts;
import basashi.erm.resource.textures.SettingCustomTexture.SettingData;
import basashi.erm.util.Values.KIND_CANDYTAST;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityGirl extends EntityERMBase {
	// 登録名
	public static final String NAME = "Girl";

	/**
	 * コンストラクタ
	 * @param worldIn
	 */
	public EntityGirl(World worldIn) {
		super(worldIn);
	}

	@Override
	public float getBaseStamina() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public float getBaseIntarest() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public int getBaseRebelious() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public float getWeightLove() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public float getWeightIntarest() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public float getWorkFood() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public float getFoodWieght() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public int getSpdRefleshStamina() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public int getSpdTiredStamina() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public int getMemory() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public int getInventorySize() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public String MobDefaultName() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public KIND_CANDYTAST getCandyFavoritte(String candy) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public double getConbatSpeed() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public double getSneakingSpeed() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public List<EntityAIBase> getDefaultAI(EntityERMBase entity) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public Map<EnumTextureParts, SettingData> getDefaultTexture() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public Map<ResourceTag, List<String>> getDefaultTone() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public String getDressTextureFile() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public String getDressSettingFile() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public String getAromorTextureFile() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public String getSoundFile() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public CallMeSetting getVoiceSetting() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public float getWidth() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public float getHeight() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public ModelERMBase getMainModel() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public void applyEntityAttributes(EntityERMBase entity) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public int healCandyCounter() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public int healTalkCounter() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public int getDominantArm() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public boolean getIFF(Entity target) {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	public boolean pushOutOfBlocks(EntityERMBase entity, double x, double y, double z) {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	public void reduceLoveCauseDamage(EntityERMBase entity, DamageSource source, float damage) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public String getRegisterName() {
		return NAME;
	}
}
