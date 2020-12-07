package com.fighter.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * How to use.
 * Trace.d(TAG, " d");
 * Trace.i(TAG, " i");
 * Trace.w(TAG, " w");
 * Trace.e(TAG, " e = " + null);
 * Trace.d(TAG, " %s,%d", "raise", 1);
 * Trace.i(TAG, " %s,%d", "raise", 1);
 * Trace.w(TAG, " %s,%d", "raise", 1);
 * Trace.e(TAG, new NullPointerException("fu*k null pointer exception."));
 * Trace.json(TAG, "{\"name\":\"BeJson\",\"url\":\"http://www.bejson.com\",\"page\":88,\"isNonProfit\":true}");
 * Trace.array(TAG, new String[]{"value1", "value2"});
 * Trace.list(TAG, Arrays.asList("list1", "list2", "list3"));
 * Trace.xml(TAG,"<student><age>12</age><name>jack</name><skill><language>chinese</language><run>22</run></skill></student>");
 * Trace.file(TAG, new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/test.log"));
 */
public class Trace {

    public static final int VERBOSE = 2;
    public static final int DEBUG = 3;
    public static final int INFO = 4;
    public static final int WARN = 5;
    public static final int ERROR = 6;
    public static final int NONE = 7; // 关闭日志

    private static int s_offset;
    private static int s_level; // 日志级别
    private static boolean s_show_code_position; // 是否显示 调用位置
    private static boolean s_write_file; // 是否写文件
    private static boolean s_is_encrypt; // 是否加密
    private static String s_log_path; // 日志文件的绝对路径
    private static int s_log_size; // 日志文件大小，单位：kb
    private static String s_global_tag; // 日志全局tag
    private static int s_file_backup_count; // 日志文件备份数量

    private static final ListenerHandlerThread mHandlerThread;

    // 用于缓存日志文件大小
    private static final String SP_KEY_LOG_SIZE = "common:sp_key_log_size";

    static {
        s_level = DEBUG;
        s_show_code_position = false;
        s_is_encrypt = false;
        s_write_file = true;
        s_file_backup_count = 2;
        s_global_tag = "lancomm";
        String fileName = "lancomm_log.txt";
        s_log_path = getDefaultFolder() + File.separator + fileName;
        s_log_size = getLogSize() > 500 ? getLogSize() : 500;

        // handlerThread不需要end()
        mHandlerThread = ListenerHandlerThread.create("Trace", false);
        mHandlerThread.start();
    }

    /**
     * 设置全局TAG
     *
     * @param globalTag 默认：lancomm
     */
    public static void setGlobalTag(@Nullable String globalTag) {
        s_global_tag = globalTag;
    }

    /**
     * 设置显示代码行数(仅Android Studio的Logcat体现)
     *
     * @param showPosition 默认：false
     */
    public static void setShowPosition(boolean showPosition) {
        s_show_code_position = showPosition;
    }

    /**
     * 设置日志文件加密
     *
     * @param encrypt 默认:false
     */
    public static void setWriteEncrypt(boolean encrypt) {
        s_is_encrypt = encrypt;
    }

    /**
     * 设置打印日志级别
     * 设置为{@link Trace#NONE}不输出日志
     *
     * @param level 默认：{@link Trace#DEBUG}
     */
    public static void setLevel(int level) {
        s_level = level;
    }

    /**
     * 设置写日志文件
     *
     * @param writeFile 默认：true
     */
    public static void writeFile(boolean writeFile) {
        s_write_file = writeFile;
    }

    /**
     * 日志文件大小，单位：KB,超过此小会重新读写文件,默认值 500
     */
    public static void setLogSize(int logSize) {
        s_log_size = logSize;
        SPFUtil.putInt(SP_KEY_LOG_SIZE, logSize);
    }

    private static int getLogSize() {
        return SPFUtil.getInt(SP_KEY_LOG_SIZE, -1);
    }

    /**
     * 设置日志保存路径
     *
     * @param absolutePath /sdcard/lancomm.log
     */
    public static void setLogAbsolutePath(@NonNull String absolutePath) {
        Trace.s_log_path = absolutePath;
    }

    /**
     * 日志文件的绝对路径:/sdcard/xxx/lancomm_log.txt
     */
    public static String getLogPath() {
        return Trace.s_log_path;
    }

    public static void setFileBackupCount(int count) {
        s_file_backup_count = count;
    }

    public static void v(String tag, String msg) {
        println(2, tag, msg);
    }

    public static void v(String tag, String msg, Object... args) {
        println(2, tag, String.format(msg, args));
    }

    public static void d(String tag, String msg) {
        println(3, tag, msg);
    }

    public static void d(String tag, String msg, Object... args) {
        println(3, tag, String.format(msg, args));
    }

    public static void i(String tag, String msg) {
        println(4, tag, msg);
    }

    public static void i(String tag, String msg, Object... args) {
        println(4, tag, String.format(msg, args));
    }

    public static void w(String tag, String msg, Object... args) {
        println(5, tag, String.format(msg, args));
    }

    public static void w(String tag, String msg) {
        println(5, tag, msg);
    }

    public static void e(String tag, String msg) {
        println(6, tag, msg);
    }

    public static void e(String tag, String msg, Throwable tr) {
        println(6, tag, msg + '\n' + getStackTraceString(tr));
    }

    public static void e(String tag, Throwable tr) {
        println(6, tag, getStackTraceString(tr));
    }

    public static <T> void array(String tag, T[] array) {
        s_offset = 1;
        if (array == null) {
            e(tag, "array is null");
        } else {
            d(tag, Arrays.toString(array));
        }
    }

    public static void list(String tag, List<?> lists) {
        s_offset = 1;
        if (lists == null) {
            e(tag, "lists is null");
        } else {

            int iMax = lists.size() - 1;
            if (iMax == -1) {
                d(tag, "{}");
            } else {
                StringBuilder b = new StringBuilder();
                b.append('{');
                for (int i = 0; i < lists.size(); i++) {
                    b.append(String.valueOf(lists.get(i)));
                    if (i == iMax) {
                        b.append('}');
                    }
                    b.append(", ");
                }
                d(tag, b.toString());
            }
        }
    }

    public static void json(String tag, String json) {
        s_offset = 1;
        if (TextUtils.isEmpty(json)) {
            e(tag, "Empty/Null json content");
            return;
        }
        try {
            json = json.trim();
            if (json.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(json);
                String message = jsonObject.toString(2);
                d(tag, message);
                return;
            }
            if (json.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(json);
                String message = jsonArray.toString(2);
                d(tag, message);
                return;
            }
            e(tag, "Invalid Json");
        } catch (JSONException e) {
            e(tag, "Invalid Json");
        }
    }

    public static void xml(String tag, String xml) {
        s_offset = 1;
        if (TextUtils.isEmpty(xml)) {
            e(tag, "Empty/Null xml content");
            return;
        }
        try {
            Source xmlInput = new StreamSource(new StringReader(xml));
            StreamResult xmlOutput = new StreamResult(new StringWriter());
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(xmlInput, xmlOutput);
            xml = xmlOutput.getWriter().toString().replaceFirst(">", ">\n");
            d(tag, xml);
        } catch (Exception e) {
            e(tag, "Invalid Xml");
        }
    }

    public static void file(String tag, File file) {
        s_offset = 1;
        if (file == null || !file.exists()) {
            e(tag, "Empty/Null file");
            return;
        }
        try {
            FileInputStream fis = new FileInputStream(file);
            byte[] bytes = new byte[1024 * 4];
            fis.read(bytes);
            String content = new String(bytes, Charset.defaultCharset());
            String result = String.format("file name:%s,file size:%s\n%s", file.getName(), file.length(), content);
            d(tag, result);
        } catch (Exception e) {
            e(tag, "Invalid file");
        }
    }

    /**
     * 同步磁盘日志文件
     * 场景：调用reboot()重启车机前调用； 因为很多车机实现关机都不会同步文件，或者强行断电
     */
    public static void syncLogFile() {
        File log_file = new File(s_log_path);
        try {
            FileOutputStream os = new FileOutputStream(log_file);
            os.getFD().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void println(int level, String tag, String message) {
        if (level >= s_level) {//过滤级别
            if (s_write_file) {
                write_log(tag, message);
            }
            if (s_show_code_position) {
                message += getCodePosition();
            }
            printAndroidLog(level, s_global_tag + "/" + tag, message);
        }
    }

    private static void printAndroidLog(int level, String tag, String message) {
        switch (level) {
            case VERBOSE:
                Log.v(tag, message);
                break;
            case DEBUG:
                Log.d(tag, message);
                break;
            case INFO:
                Log.i(tag, message);
                break;
            case WARN:
                Log.w(tag, message);
                break;
            case ERROR:
                Log.e(tag, message);
                break;
        }
    }


    /**
     * Handy function to get a loggable stack trace from a Throwable
     *
     * @param tr An exception to log
     */
    private static String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        }

        // This is to reduce the amount of log spew that apps do in the non-error
        // condition of the network being unavailable.
        Throwable t = tr;
        while (t != null) {
//            if (t instanceof UnknownHostException) {
//                return "";
//            }
            t = t.getCause();
        }

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }

    /**
     * 显示当前打印日志代码位置
     */
    private static String getCodePosition() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        int index = 5 + s_offset;
        s_offset = 0;
        if (stackTrace.length < 5) {
            return ".(Error:-1) error()";
        }
        String className = stackTrace[index].getFileName();
        String methodName = stackTrace[index].getMethodName();
        int lineNumber = stackTrace[index].getLineNumber();
        return String.format(".(%s:%s) %s()", className, lineNumber, methodName);
    }

    private static boolean check_log_file() {
        File log_file = new File(s_log_path);
        // 日志文件备份
        if (log_file.exists()
                && log_file.length() > 1024 * s_log_size
                && !UIThreadUtil.isMainThread()) {
            for (int i = s_file_backup_count; i > 0; i--) {
                // 需要备份文件，日志文件向后移动
                File backupFile = new File(String.format("%s(%s)", s_log_path, i));
                if (!backupFile.exists()) continue;
                if (i == s_file_backup_count) {
                    // 最末尾的日志文件，直接放弃
                    backupFile.delete();
                } else {
                    File newBackupFile = new File(String.format("%s(%s)", s_log_path, i + 1));
                    backupFile.renameTo(newBackupFile);
                }
            }
            boolean renameTo = log_file.renameTo(new File(s_log_path + "(1)"));
            Log.w("Trace", "The log file was backup:" + renameTo);
        }

        if (!log_file.exists()) {
            if (!log_file.getParentFile().exists()) {
                boolean mkdirs = log_file.getParentFile().mkdirs();//父目录不存在创建
                if (!mkdirs) {
                    Log.e(s_global_tag, getStackTraceString(new IOException("Can't create the directory of trace. Please check the trace path:"
                            + log_file.getParent())));
                    return false;
                }
            }
            FileOutputStream fos = null;
            try {
                log_file.createNewFile();
                // 日志文件第一行写入app信息,日志是否加密等
                fos = new FileOutputStream(log_file);
                String firstLineStr = getFirstLineString();
                fos.write(firstLineStr.getBytes());
                fos.flush();
            } catch (IOException e) {
                Log.e("Trace", getStackTraceString(e));
                return false;
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return true;
    }

    private static String getFirstLineString() {
        String encrypt = "" + s_is_encrypt;
        String versionCode = "" + AppInfoUtil.getVersionCode();
        return encrypt + "|" +
                versionCode + "\n";
    }

    private static void write_log(String tag, String msg) {
        if (!check_log_file()) {
            return;
        }
        final String text = getFormatLog(tag, msg);
        mHandlerThread.post(new Runnable() {
            @Override
            public void run() {
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(s_log_path, true);
                    fos.write(text.getBytes());
                    fos.write("\n".getBytes());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private static String getFormatLog(String tag, String msg) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String dateStr = sdf.format(new Date());
        return dateStr +
                " " +
                String.format("%s ", convertThreadId((int) Thread.currentThread().getId())) +
                String.format("%s: ", tag) +
                msg;
    }

    private static String convertThreadId(int value) {
        int limit = 5;
        String src = String.valueOf(value);
        int i = limit - src.length();
        if (i < 0) {
            src = src.substring(-i);
        }
        for (; i > 0; i--) {
            src = "0" + src;
        }
        return src;
    }

    private static String getDefaultFolder() {
        Context context = ContextVal.getContext();
        String path;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //sd卡挂载
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //大于6.0 存放于Android/data/data/包名/cache 此路径不需要申请权限
                if (context.getExternalCacheDir() == null) {
                    path = context.getCacheDir().getAbsolutePath();
                } else {
                    path = context.getExternalCacheDir().getAbsolutePath();
                }
            } else {
                //小于6.0 存放于内置存储卡根目录
                path = Environment.getExternalStorageDirectory().getAbsolutePath();
            }
        } else {
            //sd卡不可用
            path = context.getCacheDir().getAbsolutePath();
        }
        return path;
    }
}
