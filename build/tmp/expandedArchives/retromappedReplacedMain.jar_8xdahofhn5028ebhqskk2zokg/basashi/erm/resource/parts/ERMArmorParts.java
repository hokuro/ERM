package basashi.erm.resource.parts;

public enum ERMArmorParts
{
    HEAD(0,"head","head"),
    BODY(1,"body","body"),
    LEGS(2,"legs","legs"),
    FOOT(3,"foot","foot");

	private int partsNum;
	private String partsKind;
	private String partsName;

	private ERMArmorParts(int num, String partsKind, String parts){
		this.partsNum = num;
		this.partsKind = partsKind;
		this.partsName = parts;
	}

	public int getNum(){return partsNum;}
	public String getKind(){return partsKind;}
	public String getPartsName(){return partsName;}
}