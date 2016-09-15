package basashi.erm.message;

import java.io.UnsupportedEncodingException;

import basashi.erm.entity.EntityERMBase;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageCandy implements IMessageHandler<MessageCandy, IMessage>, IMessage{

	private String entityKind;
	private float candyValue;
	private float foodValue;
	private float loveValue;

	public MessageCandy(){}

	public MessageCandy(EntityERMBase entity, float count, float food, float love){
		entityKind = entity.getRegisterName();
		candyValue = count;
		foodValue = food;
		loveValue = love;
	}


	@Override
	public void fromBytes(ByteBuf buf) {
		candyValue = buf.readFloat();
		foodValue = buf.readFloat();
		loveValue = buf.readFloat();
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
		buf.writeFloat(candyValue);
		buf.writeFloat(foodValue);
		buf.writeFloat(loveValue);
		try{
			buf.writeInt(entityKind.getBytes("UTF-8").length);
			buf.writeBytes(entityKind.getBytes("UTF-8"));
		}catch(UnsupportedEncodingException e){
			buf.writeInt(entityKind.getBytes().length);
			buf.writeBytes(entityKind.getBytes());
		}
	}

	@Override
	public IMessage onMessage(MessageCandy message, MessageContext ctx) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

}
