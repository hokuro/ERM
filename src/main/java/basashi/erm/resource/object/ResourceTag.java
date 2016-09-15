package basashi.erm.resource.object;

public class ResourceTag {
	// タグの名前
	private String tagName;
	// タグの説明
	private String tagDisc;
	// AI名
	private String tagAiName;

	public ResourceTag(String name, String disc, String aiName){
		tagName = name;
		tagDisc = disc;
		tagAiName = aiName;
	}

	public String getTagName(){return tagName;}
	public String getTagDisc(){return tagDisc;}
	public String getTagAIName(){return tagAiName;}

	public boolean equals(String tagStr){
		return (this.tagAiName + ":" + this.tagName).equals(tagStr);
	}

	public boolean equals(String ai, String tag){
		return (this.tagAiName.equals(ai) && this.tagName.equals(tag));
	}

	public String getTag(){
		return this.tagAiName + ":" + this.tagName;
	}

}
