package basashi.erm.resource;

import java.io.File;

public interface IERMResource {
	/*
	 * リソースフォルダ
	 */
	String dirName();
	/*
	 * リソースの初期化処理
	 * リストの初期化や、デフォルトリソースの登録など
	 */
	void initResource() throws Exception;

	/**
	 * 対応するリソースが入っているディレクトリかどうか確認
	 */
	boolean chkDir(String directory);
	boolean chkDir(File directory);

	/**
	 * リソースの読み込みを行う
	 * @return 成否
	 */
	boolean loadResource(File directory);

}
