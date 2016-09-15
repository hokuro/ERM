package basashi.erm.resource.textures;

import java.util.ArrayList;
import java.util.List;

public enum EnumTextureParts
{
	// ドレステクスチャ用
    HEAD( 0,"drs","head"),
    EYE(  1,"drs","eye"),
    BODY( 2,"drs","body"),
    DRESS(3,"drs","dress"),
    OPT1( 4,"drs","option","option1"),
    OPT2( 5,"drs","option","option2"),
    OPT3( 6,"drs","option","option3"),
    OPT4( 7,"drs","option","option4"),
    OPT5( 8,"drs","option","option5"),

    // アーマーテクスチャ用
	ARMOR(9,"arm","armor");

	private int partsNum;
	private String partsKind;
	private String partsRegion;
	private String partsName;

	private EnumTextureParts(int num, String partsKind, String partsRegion){
		this.partsNum = num;
		this.partsKind = partsKind;
		this.partsRegion = partsRegion;
		this.partsName = partsRegion;
	}
	private EnumTextureParts(int num, String partsKind, String partsRegion, String parts){
		this.partsNum = num;
		this.partsKind = partsKind;
		this.partsRegion = partsRegion;
		this.partsName = parts;
	}

	public int getNum(){return partsNum;}
	public String getKind(){return partsKind;}
	public String getRegion(){return partsRegion;}
	public String getPartsName(){return partsName;}
	public EnumTextureParts[] values(String kind){
		List<EnumTextureParts> ret = new ArrayList<EnumTextureParts>();
		for(EnumTextureParts member : this.values()){
			if (member.getKind().equals(kind)){
				ret.add(member);
			}
		}
		return (EnumTextureParts[])ret.toArray();
	}

	public static EnumTextureParts getFromStringName(String name){
		EnumTextureParts ret = null;
		for(EnumTextureParts kind : EnumTextureParts.values()){
			if(kind.name().equals(name)){
				ret = kind;
				break;
			}
		}
		return ret;
	}




	/**
	 * 基本パーツ用のテクスチャ部位リスト
	 * @return
	 */
	public static EnumTextureParts[] getBodyBaseValues(){
		return partList_BODYBASE;
	}

	/**
	 * オプション用のテクスチャ部位リスト
	 * @return
	 */
	public static EnumTextureParts[] getOptionValues(){
		return partList_OPTITON;
	}

	/**
	 * ドレス用のテクスチャ部位のリスト
	 * @return
	 */
	public static EnumTextureParts[] getDressValues(){
		return partList_DRESS;
	}



	/*******************************************************************************************/
	/** パーツリスト                                                                          **/
	/*******************************************************************************************/
	/**
	 * テクスチャ基本パーツリスト
	 */
	private static final EnumTextureParts[] partList_BODYBASE = new EnumTextureParts[]{
			BODY,EYE,DRESS,HEAD
	};
	/**
	 * オプションパーツリスト
	 */
	private static final EnumTextureParts[] partList_OPTITON = new EnumTextureParts[]{
			OPT1,OPT2,OPT3,OPT4,OPT5
	};
	/**
	 * ドレステクスチャリス
	 */
	private static final EnumTextureParts[] partList_DRESS = new EnumTextureParts[]{
			BODY,EYE,DRESS,HEAD,OPT1,OPT2,OPT3,OPT4,OPT5
	};
}