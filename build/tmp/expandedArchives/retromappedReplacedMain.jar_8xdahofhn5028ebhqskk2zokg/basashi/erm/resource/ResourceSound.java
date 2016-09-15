package basashi.erm.resource;

import java.io.File;

public class ResourceSound implements IERMResource {
	public static final String DirName = "sound";
	/*************************************************************************/
	/** インタフェース                                                       */
	/*************************************************************************/
	@Override
	public void initResource() {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public boolean chkDir(String directory) {
		return DirName.equals(directory);
	}

	@Override
	public boolean chkDir(File directory) {
		return DirName.equals(directory.getName());
	}

	@Override
	public boolean loadResource(File directory) {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	public String dirName() {
		return DirName;
	}
}
