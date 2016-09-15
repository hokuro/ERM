package basashi.erm.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import basashi.erm.resource.PartsListSerializer;
import basashi.erm.resource.parts.ModelPartsInfoList;

public final class Util {

	private static Gson gson = new GsonBuilder().registerTypeAdapter(ModelPartsInfoList.class, new PartsListSerializer()).create();
	private static Random rand = null;
	private static String crlf = null;

	public static boolean StringisEmptyOrNull(String value){
		if (value == null || value.isEmpty()){
			return true;
		}
		return false;
	}

	// 乱数の取得
	public static int random(int n) {
		if ( rand == null){
			rand = new Random();
			rand.setSeed(System.currentTimeMillis() + Runtime.getRuntime().freeMemory());
		}

	    int adjusted_max = (Integer.MAX_VALUE + 1) - (Integer.MAX_VALUE + 1) % n;
	    int r;
	    do {
	       r = rand.nextInt();
	    } while (r >= adjusted_max);
	    return (int)(((double)r / adjusted_max) * n);
	}

	// 使用環境の改行コードを返す
	public static String ReturnCode(){
		if(crlf == null){
			crlf = System.getProperty("line.separator");
		}
		return crlf;
	}

	// Jsonからパーツの情報を読み込む
	public static ModelPartsInfoList readModelParts(File parts) throws Exception{
		InputStreamReader stream = new InputStreamReader(new FileInputStream(parts));
		ModelPartsInfoList list;
		try {
			 list = gson.fromJson(stream, ModelPartsInfoList.class);
		}catch(Exception ex){
			throw ex;
		}finally{
			try {
				stream.close();
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}
		return list;
	}

	// Jsonからパーツの情報を読み込む
	public static ModelPartsInfoList readModelParts(InputStream parts) throws Exception{
		InputStreamReader stream = new InputStreamReader(parts);
		ModelPartsInfoList list;
		try {
			 list = gson.fromJson(stream, ModelPartsInfoList.class);
		}catch(Exception ex){
			throw ex;
		}finally{
			try {
				stream.close();
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}
		return list;
	}


}
