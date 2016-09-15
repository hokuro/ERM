package basashi.erm.message;

import java.io.UnsupportedEncodingException;

import basashi.erm.core.Mod_ERM;
import basashi.erm.entity.EntityERMBase;
import basashi.erm.util.Values.KIND_STATUS;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageEat implements IMessageHandler<MessageEat, IMessage>, IMessage {

	private float foodValue;
	private String entityKind;

	public MessageEat(){}

	public MessageEat(EntityERMBase entity, float value){
		foodValue = value;
		entityKind = entity.getRegisterName();
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		foodValue = buf.readFloat();
		int length = buf.readInt();
		byte[] kind = new byte[length];
		buf.readBytes(kind);
		try{
			entityKind = new String(kind,"UTF-8");
		}catch(UnsupportedEncodingException e){
			entityKind = new String(kind);
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeFloat(foodValue);
		try{
			buf.writeInt(entityKind.getBytes("UTF-8").length);
			buf.writeBytes(entityKind.getBytes("UTF-8"));
		}catch(UnsupportedEncodingException e){
			buf.writeInt(entityKind.getBytes().length);
			buf.writeBytes(entityKind.getBytes());
		}
	}

	@Override
	public IMessage onMessage(MessageEat message, MessageContext ctx) {
		try{
			EntityERMBase entity = Mod_ERM.getEntity(Mod_ERM.mobMapStringToClass.get(message.entityKind.toLowerCase()));
			entity.getStatus(KIND_STATUS.STATUS_FOOD).setStatus(message.foodValue);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

}
