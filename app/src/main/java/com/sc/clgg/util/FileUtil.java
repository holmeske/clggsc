package com.sc.clgg.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sc.clgg.config.ConstantValue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

/**
 * 文件夹操作
 * 
 * @author Michael.Zhang 2014-2-20 下午5:57:35
 */
public class FileUtil {
	public static FileUtil mFileUtil;

	/**
	 * 文件复制
	 * 
	 * @author ZhangYi 2014年7月1日 下午6:04:45
	 * @param srFile
	 *            源文件路径
	 * @param dtFile
	 *            目标文件路径
	 */
	public static void copyfile(String srFile, String dtFile) {
		try {
			File f1 = new File(srFile);
			File f2 = new File(dtFile);
			InputStream in = new FileInputStream(f1);
			OutputStream out = new FileOutputStream(f2);
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// 获得图片路径
	public static String getBitmapPath(String path, String fileName) {

		return SDCARD_ROOT + path + File.separator + fileName;
	}

	public static FileUtil getInstance() {
		if (null == mFileUtil) {
			mFileUtil = new FileUtil();
		}
		return mFileUtil;
	}

	/**
	 * @Description描述:ArrayList转JSONArray
	 * @param map
	 * @return
	 * @throws JSONException
	 * @return JSONObject
	 */
	@SuppressWarnings("unchecked")
	public static JSONArray getJSONArray(ArrayList<Object> list) throws JSONException {
		JSONArray array = new JSONArray();
		for (Object value : list) {
			if (value instanceof HashMap<?, ?>) {
				value = getJSONObject((HashMap<String, Object>) value);
			} else if (value instanceof ArrayList<?>) {
				value = getJSONArray((ArrayList<Object>) value);
			}
			array.put(value);
		}
		return array;
	}

	/**
	 * @Description描述:HashMap转JSONObject
	 * @param map
	 * @return
	 * @throws JSONException
	 * @return JSONObject
	 */
	@SuppressWarnings("unchecked")
	public static JSONObject getJSONObject(HashMap<String, Object> map) throws JSONException {
		JSONObject json = new JSONObject();
		for (Entry<String, Object> entry : map.entrySet()) {
			Object value = entry.getValue();
			if (value instanceof HashMap<?, ?>) {
				value = getJSONObject((HashMap<String, Object>) value);
			} else if (value instanceof ArrayList<?>) {
				value = getJSONArray((ArrayList<Object>) value);
			}
			json.put(entry.getKey(), value);
		}
		return json;
	}

	public static String getTime(long currentTime) {

		// 格式化对象
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 执行格式化
		return simpleDateFormat.format(currentTime);
	}

	// SharedPreferences存储
	private SharedPreferences sp = null;

	// SD卡根目录
	public static final String SDCARD_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;

	// 静态代码块
	static {
		mFileUtil = new FileUtil();
	}

	// 在SD卡上创建文件夹
	public boolean createFileInsdCard(String path) throws Exception {

		File file = new File(SDCARD_ROOT + path);

		if (file.exists()) {

			return true;
		}

		return file.mkdir();
	}

	// 在SD卡上创建文件
	public boolean createFileInsdCard(String path, String fileName) throws Exception {

		File file = new File(SDCARD_ROOT + path + File.separator + fileName);

		if (file.exists()) {

			return true;
		}

		return file.createNewFile();
	}

	/**
	 * 创建指定名称的文件夹
	 * 
	 * @author Michael.Zhang 2013-10-31 下午6:11:24
	 * @param name
	 *            传null为根目录
	 */
	public void createFiles(String name) {
		String path = ConstantValue.SDCARD_PATH;
		if (!Tools.isNull(name)) {
			path = ConstantValue.SDCARD_PATH + "/" + name;
		}

		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	public void deleteFile(File file) {

		// 判断文件是否存在
		if (file.exists()) {

			// 判断是否是文件
			if (file.isFile()) {

				file.delete();

				// 否则如果它是一个目录
			} else if (file.isDirectory()) {

				// 声明目录下所有的文件 files[];
				File files[] = file.listFiles();

				// 遍历目录下所有的文件
				for (int i = 0; i < files.length; i++) {

					// 把每个文件 用这个方法进行迭代
					this.deleteFile(files[i]);
				}
			}

			file.delete();
		}
	}

	/**
	 * 获取指定位置的文件
	 * 
	 * @author Michael.Zhang 2013-10-31 下午6:08:24
	 * @param name
	 * @return
	 */
	public Object getObject(String name) {
		Object obj = null;
		try {
			FileInputStream fis = new FileInputStream(ConstantValue.SDCARD_PATH + "/" + name);
			ObjectInputStream ois = new ObjectInputStream(fis);
			obj = ois.readObject();
			ois.close();
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return obj;
	}

	// 判断SD卡是否存在
	public boolean isSDcardExist() {

		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);

	}

	// 对象集合转成JSON字符串
	public <T> String parseListToString(List<T> list, TypeToken<ArrayList<T>> typeToken, Gson gson) {
		return gson.toJson(list, typeToken.getType());
	}

	// 对象转JSON字符串
	public <T> String parseObjectToString(Object object, Class<T> t, Gson gson) {
		return gson.toJson(object, t);
	}

	// JSON字符串转成对象集合
	public <T> ArrayList<T> parseStringToList(String json, TypeToken<ArrayList<T>> typeToken, Gson gson) {
		return gson.fromJson(json, typeToken.getType());
	}

	// JSON字符串转对象
	public <T> Object parseStringToObject(String json, Class<T> t, Gson gson) {
		return gson.fromJson(json, t);
	}

	// 读取文件数据
	public String read(Context context, String fileName) throws Exception {

		FileInputStream inStream = context.openFileInput(fileName);

		ByteArrayOutputStream outStream = new ByteArrayOutputStream();

		byte[] buffer = new byte[1024];

		// 防止数据量过大，超过了1024
		int len = 0;

		while ((len = inStream.read(buffer)) != -1) {

			// 防止每次读取覆盖前一次数据
			outStream.write(buffer, 0, len);
		}

		byte[] data = outStream.toByteArray();

		return new String(data);
	}

	// 保存文件数据
	public void save(Context context, String fileName, String fileContent) throws Exception {

		// Context.MODE_PRIVATE --私有操作模式：创建出来的文件只能被本应用访问，其它应用无法访问该文件
		// 另外采用私有操作模式创建的文件，写入文件中的内容会覆盖原文件内容
		// 如果想把新写入的内容追加到原文件中，可以使用Context.MODE_APPEND
		FileOutputStream outStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);

		outStream.write(fileContent.getBytes());

		outStream.close();
	}

	/**
	 * 保存文件到指定位置
	 * 
	 * @author Michael.Zhang 2013-10-31 下午6:09:49
	 * @param obj
	 * @param name
	 */
	public void saveObject(Object obj, String name) {
		try {
			File file = new File(ConstantValue.SDCARD_PATH + "/" + name);
			if (!file.exists()) {
				file.createNewFile();
			}

			FileOutputStream fos = new FileOutputStream(ConstantValue.SDCARD_PATH + "/" + name);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(obj);
			oos.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 把数据写入SD卡
	public File writeSDFromInput(String path, String fileName, InputStream input) {

		try {

			// 必须先创建文件夹
			FileUtil.getInstance().createFileInsdCard(path);

			// 然后再创建文件
			FileUtil.getInstance().createFileInsdCard(path, fileName);

		} catch (Exception e1) {
			e1.printStackTrace();
		}

		OutputStream output = null;

		File file = new File(SDCARD_ROOT + path + File.separator + fileName);

		try {

			output = new FileOutputStream(file);

			// 自定义缓冲区
			byte[] buffer = new byte[4 * 1024];

			int length = 0;

			// 复制流的过程
			while ((length = input.read(buffer)) != -1) {

				output.write(buffer, 0, length);
			}

			// 刷新
			output.flush();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {

			try {

				// 关闭流
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return file;
	}

}
