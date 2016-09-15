package basashi.erm.entity.status;

import basashi.erm.entity.EntityERMBase;
import basashi.erm.util.Values;
import basashi.erm.util.Values.KIND_COUNTER;
import net.minecraft.nbt.NBTTagCompound;

public class EntityERMStatusCounter extends EntityERMStatus {

	private Values.KIND_COUNTER cntKey;
	public EntityERMStatusCounter(float max, float min, float value, Values.KIND_COUNTER cnt) {
		super(max, min, value);
		cntKey = cnt;
	}

	@Override
	public void onUpdate(EntityERMBase entity, EntityERMCounter counter) {
		if (counter.getCounter(cntKey) >= 240000){
			addStatus(-1.0F);
			counter.clearCounter(cntKey);
		}
	}

	@Override
	public void readNBT(NBTTagCompound compound, String key) {
		statusValue = compound.getFloat(key);
	}

	@Override
	public void writeNBT(NBTTagCompound compound, String key) {
		compound.setFloat(key, statusValue);
	}

}
