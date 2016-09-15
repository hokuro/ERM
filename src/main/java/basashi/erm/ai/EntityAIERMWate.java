package basashi.erm.ai;


import basashi.erm.entity.EntityERMBase;
import net.minecraft.entity.ai.EntityAISit;
import net.minecraft.entity.passive.EntityTameable;

public class EntityAIERMWate extends EntityAISit {

	public EntityERMBase theMaid;

	public EntityAIERMWate(EntityTameable entityIn) {
		super(entityIn);
	}


	@Override
	public boolean shouldExecute() {
		return theMaid.isWait();
	}

}
