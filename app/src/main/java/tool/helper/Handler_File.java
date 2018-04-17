//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package tool.helper;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.sc.clgg.application.App;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Handler_File {

    public Handler_File() {
    }

    public static StringBuilder readFile(String filePath) {
        File file = new File(filePath);
        StringBuilder fileContent = new StringBuilder("");
        if (file != null && file.isFile()) {
            BufferedReader reader = null;

            StringBuilder var6;
            try {
                reader = new BufferedReader(new FileReader(file));

                for (String e = null; (e = reader.readLine()) != null; fileContent.append(e)) {
                    if (!fileContent.toString().equals("")) {
                        fileContent.append("\r\n");
                    }
                }

                reader.close();
                var6 = fileContent;
            } catch (IOException var13) {
                throw new RuntimeException("IOException occurred. ", var13);
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException var12) {
                        throw new RuntimeException("IOException occurred. ", var12);
                    }
                }

            }

            return var6;
        } else {
            return null;
        }
    }

    public static boolean writeFile(String filePath, String content, boolean append) {
        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter(filePath, append);
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException var12) {
            throw new RuntimeException("IOException occurred. ", var12);
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException var11) {
                    throw new RuntimeException("IOException occurred. ", var11);
                }
            }

        }

        return true;
    }

    public static boolean writeFile(String filePath, InputStream stream) {
        FileOutputStream o = null;

        try {
            o = new FileOutputStream(filePath);
            byte[] e = new byte[1024];
            boolean length = true;

            int length1;
            while ((length1 = stream.read(e)) != -1) {
                o.write(e, 0, length1);
            }

            o.flush();
            return true;
        } catch (FileNotFoundException var13) {
            throw new RuntimeException("FileNotFoundException occurred. ", var13);
        } catch (IOException var14) {
            throw new RuntimeException("IOException occurred. ", var14);
        } finally {
            if (o != null) {
                try {
                    o.close();
                    stream.close();
                } catch (IOException var12) {
                    throw new RuntimeException("IOException occurred. ", var12);
                }
            }

        }
    }

    public static List<String> readFileToList(String filePath) {
        File file = new File(filePath);
        ArrayList fileContent = new ArrayList();
        if (file != null && file.isFile()) {
            BufferedReader reader = null;

            try {
                reader = new BufferedReader(new FileReader(file));
                String e = null;

                while ((e = reader.readLine()) != null) {
                    fileContent.add(e);
                }

                reader.close();
                ArrayList var6 = fileContent;
                return var6;
            } catch (IOException var13) {
                throw new RuntimeException("IOException occurred. ", var13);
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException var12) {
                        throw new RuntimeException("IOException occurred. ", var12);
                    }
                }

            }
        } else {
            return null;
        }
    }

    public static String getFileNameWithoutExtension(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        } else {
            int extenPosi = filePath.lastIndexOf(".");
            int filePosi = filePath.lastIndexOf(File.separator);
            return filePosi == -1 ? (extenPosi == -1 ? filePath : filePath.substring(0, extenPosi)) : (extenPosi == -1 ? filePath.substring(filePosi + 1) : (filePosi < extenPosi ? filePath.substring(filePosi + 1, extenPosi) : filePath.substring(filePosi + 1)));
        }
    }

    public static String getFileName(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        } else {
            int filePosi = filePath.lastIndexOf(File.separator);
            return filePosi == -1 ? filePath : filePath.substring(filePosi + 1);
        }
    }

    public static String getFolderName(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        } else {
            int filePosi = filePath.lastIndexOf(File.separator);
            return filePosi == -1 ? "" : filePath.substring(0, filePosi);
        }
    }

    public static String getFileExtension(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        } else {
            int extenPosi = filePath.lastIndexOf(".");
            int filePosi = filePath.lastIndexOf(File.separator);
            return extenPosi == -1 ? "" : (filePosi >= extenPosi ? "" : filePath.substring(extenPosi + 1));
        }
    }

    public static boolean makeDirs(String filePath) {
        String folderName = getFolderName(filePath);
        if (TextUtils.isEmpty(folderName)) {
            return false;
        } else {
            File folder = new File(folderName);
            return folder.exists() && folder.isDirectory() ? true : folder.mkdirs();
        }
    }

    public static boolean makeFolders(String filePath) {
        return makeDirs(filePath);
    }

    public static boolean isFileExist(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return false;
        } else {
            File file = new File(filePath);
            return file.exists() && file.isFile();
        }
    }

    public static boolean isFolderExist(String directoryPath) {
        if (TextUtils.isEmpty(directoryPath)) {
            return false;
        } else {
            File dire = new File(directoryPath);
            return dire.exists() && dire.isDirectory();
        }
    }

    public static boolean deleteFile(String path) {
        if (TextUtils.isEmpty(path)) {
            return true;
        } else {
            File file = new File(path);
            if (!file.exists()) {
                return true;
            } else if (file.isFile()) {
                return file.delete();
            } else if (!file.isDirectory()) {
                return false;
            } else {
                File[] var5;
                int var4 = (var5 = file.listFiles()).length;

                for (int var3 = 0; var3 < var4; ++var3) {
                    File f = var5[var3];
                    if (f.isFile()) {
                        f.delete();
                    } else if (f.isDirectory()) {
                        deleteFile(f.getAbsolutePath());
                    }
                }

                return file.delete();
            }
        }
    }

    public static long getFileSize(String path) {
        if (TextUtils.isEmpty(path)) {
            return -1L;
        } else {
            File file = new File(path);
            return file.exists() && file.isFile() ? file.length() : -1L;
        }
    }

    public static void copyFile(File sourceFile, File targetFile) {
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;

        try {
            inBuff = new BufferedInputStream(new FileInputStream(sourceFile));
            outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));
            byte[] e = new byte[5120];

            int len;
            while ((len = inBuff.read(e)) != -1) {
                outBuff.write(e, 0, len);
            }

            outBuff.flush();
            sourceFile.delete();
            sourceFile.deleteOnExit();
        } catch (Exception var14) {
            var14.printStackTrace();
        } finally {
            try {
                if (inBuff != null) {
                    inBuff.close();
                }

                if (outBuff != null) {
                    outBuff.close();
                }
            } catch (IOException var13) {
                ;
            }

        }

    }

    public static File getExternalCacheDir(Context context, String dirs_name) {
        File dataDir = new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data");
        File appCacheDir = new File(new File(new File(dataDir, context.getPackageName()), "cache"), dirs_name);
        if (!appCacheDir.exists()) {
            try {
                (new File(dataDir, ".nomedia")).createNewFile();
            } catch (IOException var5) {
                Log.w("创建目录", "Can\'t create \".nomedia\" file in application external cache directory", var5);
            }

            if (!appCacheDir.mkdirs()) {
                Log.w("创建目录", "Unable to create external cache directory");
                return null;
            }
        }

        return appCacheDir;
    }

    public static void write(File file, String data) {
        BufferedWriter out = null;

        try {
            out = new BufferedWriter(new FileWriter(file), 1024);
            out.write(data);
        } catch (IOException var12) {
            LogHelper.d("write " + file.getAbsolutePath() + " data failed!");
            var12.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException var11) {
                    var11.printStackTrace();
                }
            }

        }

    }

    public static String getAsString(File file) {
        if (!file.exists()) {
            return null;
        } else {
            BufferedReader in = null;

            try {
                in = new BufferedReader(new FileReader(file));

                String e;
                String currentLine;
                for (e = ""; (currentLine = in.readLine()) != null; e = e + currentLine) {
                    ;
                }

                String var5 = e;
                return var5;
            } catch (IOException var13) {
                var13.printStackTrace();
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException var12) {
                        var12.printStackTrace();
                    }
                }

            }

            return null;
        }
    }

    public static void saveObject(String fileName, Serializable object) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;

        try {
            fos = App.getInstance().openFileOutput(fileName, 0);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
        } catch (Exception var13) {
            var13.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }

                if (oos != null) {
                    oos.close();
                }
            } catch (IOException var12) {
                ;
            }

        }

    }

    public static <T> T getObject(String fileName) {
        FileInputStream fis = null;
        ObjectInputStream ois = null;

        try {
            fis = App.getInstance().openFileInput(fileName);
            ois = new ObjectInputStream(fis);
            Object var5 = ois.readObject();
            return (T) var5;
        } catch (Exception var13) {
            var13.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }

                if (ois != null) {
                    ois.close();
                }
            } catch (IOException var12) {
                ;
            }

        }

        return null;
    }
}
