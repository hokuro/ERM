package basashi.erm.resource.parts;

public enum ERMDressParts
{
    HEAD(0,"head","head"),
    EYE(1,"eye","eye"),
    BODY(2,"body","body"),
    DRESS(3,"dress","dress"),
    OPT1(4,"parts","option1"),
    OPT2(5,"parts","option2"),
    OPT3(6,"parts","option3"),
    OPT4(7,"parts","option4"),
    OPT5(8,"parts","option5");

	private int partsNum;
	private String partsKind;
	private String partsName;

	private ERMDressParts(int num, String partsKind, String parts){
		this.partsNum = num;
		this.partsKind = partsKind;
		this.partsName = parts;
	}

	public int getNum(){return partsNum;}
	public String getKind(){return partsKind;}
	public String getPartsName(){return partsName;}
}