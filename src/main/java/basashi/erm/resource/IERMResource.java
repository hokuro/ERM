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
	 * リソースの読み込みを行う
	 * @return 成否
	 */
	boolean loadResource(File directory);

	/**
	 * カスタムリソースが使用可能かどうかを取得する
	 * @return 使用可否
	 */
	boolean canUseResource();

}
