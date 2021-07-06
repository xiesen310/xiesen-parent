package com.github.xiesen.metadata;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @author 谢森
 * @since 2021/4/9
 */
public class CompressFileUtils {
    public static final String STR = ".";
    public static final String ZIP1 = "zip";
    public static final int LEN4096 = 4096;
    public static final String ZIP = ".zip";
    public static final String REGEX = "\r|\n";
    public static final String OS_NAME = "os.name";
    public static final String LINUX1 = "linux";
    public static final String MAC = "mac";
    private static String WINDOWS = "windows";
    public static final String SEGMENTATION = "/";
    private static Logger logger = LoggerFactory.getLogger(CompressFileUtils.class);
    private static String slash = judgeFileSlash();
    public static final String STR_JAR = ".jar";

    public static String judgeFileSlash() {
        String slash = "/";
        Properties properties = System.getProperties();
        String os = properties.getProperty(OS_NAME);
        if (os != null && os.toLowerCase().indexOf(LINUX1) > -1) {
            slash = "/";
        } else if (os != null && os.toLowerCase().indexOf(MAC) > -1) {
            slash = "/";
        } else {
            slash = "\\";
        }
        return slash;
    }

    /**
     * 判断是否为目录
     *
     * @param dir 目录
     * @return boolean
     */
    public static boolean isDirectory(String dir) {
        File tem = new File(dir);
        boolean existStatus = false;
        if (tem.exists()) {
            if (tem.isDirectory()) {
                existStatus = true;
            }
        }
        return existStatus;
    }

    /**
     * 判断是否为文件
     *
     * @param dir
     * @return
     */
    public static boolean isFile(String dir) {
        File tem = new File(dir);
        boolean existStatus = false;
        if (tem.exists()) {
            if (tem.isFile()) {
                existStatus = true;
            }
        }
        return existStatus;
    }

    /**
     * 创建目录
     *
     * @param dir 目录
     */
    public static void mkdirDir(String dir) {
        try {
            String dirTemp = dir;
            File dirPath = new File(dirTemp);
            if (!dirPath.exists()) {
                dirPath.mkdirs();
            }
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }

    /**
     * 对于linux创建文件进行赋权
     *
     * @param filePath
     * @throws IOException
     */
    public static void execChmod(String filePath) throws IOException {
        String os = System.getProperty(OS_NAME);
        //拼接完整连接
        if (!os.toLowerCase().contains(WINDOWS)) {
            Runtime.getRuntime().exec("chmod 555 -R " + filePath);
        }
    }

    /**
     * 新建文件
     *
     * @param dir      文件所在的目录 例如:/home/LogHelpersource
     * @param filename 文件名称  例如： kcbp.LogHelper
     * @param reCreate 是否重新创建
     */
    public static void createFile(String dir, String filename, boolean reCreate) {
        try {
            mkdirDir(dir);
            File f = new File(dir + slash + filename);
            boolean create = false;
            if (reCreate) {
                create = true;
            } else if (!f.exists()) {
                create = true;
            }
            if (create) {
                if (!f.createNewFile()) {
                    logger.error("创建文件失败");
                }
                execChmod(f.getPath());
            }
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }

    /**
     * 利用printStream写文件
     *
     * @param filePath
     * @param fileContent
     */
    public static boolean printStream(String filePath, String fileContent) {
        boolean finshed = false;
        PrintStream p = null;
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filePath);
            p = new PrintStream(out);
            p.println(fileContent);
            finshed = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (p != null) {
                    p.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return finshed;
    }

    /**
     * @param fileName
     * @param isDirectory
     * @return
     */
    public static File buildFile(String fileName, boolean isDirectory) {
        File target = new File(fileName);
        if (isDirectory) {
            target.mkdirs();
        } else {
            if (!target.getParentFile().exists()) {
                target.getParentFile().mkdirs();
                target = new File(target.getAbsolutePath());
            }
        }
        return target;
    }

    /**
     * 删除文件
     *
     * @param fileName 包含路径的文件名
     */
    public static void delFile(String fileName) {
        try {
            String filePath = fileName;
            File delFile = new File(filePath);
            delFile.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除文件夹
     *
     * @param folderPath 文件夹路径
     */
    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath);
            String filePath = folderPath;
            File myFilePath = new File(filePath);
            myFilePath.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除文件夹里面的所有文件
     *
     * @param path 文件夹路径
     */
    public static void delAllFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        if (!file.isDirectory()) {
            return;
        }
        String[] childFiles = file.list();
        File temp = null;
        for (int i = 0; i < childFiles.length; i++) {
            if (path.endsWith(slash)) {
                temp = new File(path + childFiles[i]);
            } else {
                temp = new File(path + slash + childFiles[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                // 先删除文件夹里面的文件
                delAllFile(path + "/" + childFiles[i]);
                // 再删除空文件夹
                delFolder(path + "/" + childFiles[i]);
            }
        }
    }

    /**
     * 压缩文件
     *
     * @param srcDir  压缩前存放的目录
     * @param destDir 压缩后存放的目录
     * @throws Exception
     */
    public static void yaSuoZip(String srcDir, String destDir) throws Exception {
        String tempFileName = null;
        byte[] buf = new byte[1024 * 2];
        int len;
        File[] files = new File(srcDir).listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    FileInputStream fis = new FileInputStream(file);
                    BufferedInputStream bis = new BufferedInputStream(fis);
                    if (destDir.endsWith(slash)) {
                        tempFileName = destDir + file.getName() + ZIP;
                    } else {
                        tempFileName = destDir + "/" + file.getName() + ZIP;
                    }
                    FileOutputStream fos = new FileOutputStream(tempFileName);
                    BufferedOutputStream bos = new BufferedOutputStream(fos);
                    ZipOutputStream zos = new ZipOutputStream(bos);
                    ZipEntry ze = new ZipEntry(file.getName());
                    zos.putNextEntry(ze);
                    while ((len = bis.read(buf)) != -1) {
                        zos.write(buf, 0, len);
                        zos.flush();
                    }
                    bis.close();
                    zos.close();

                }
            }
        }
    }

    /**
     * 压缩
     *
     * @param zipFileName  压缩产生的zip包文件名--带路径,如果为null或空则默认按文件名生产压缩文件名
     * @param relativePath 相对路径，默认为空
     * @param directory    文件或目录的绝对路径
     * @throws FileNotFoundException
     * @throws IOException
     */

    public static void zipPackage(String zipFileName, String relativePath, String directory) throws FileNotFoundException, IOException {
        String fileName = zipFileName;
        if (fileName == null || "".equals(fileName.trim())) {
            File temp = new File(directory);
            if (temp.isDirectory()) {
                fileName = directory + ZIP;
            } else {
                if (directory.indexOf(STR) > 0) {
                    fileName = directory.substring(0, directory
                            .lastIndexOf(STR))
                            + ZIP1;
                } else {
                    fileName = directory + ZIP;
                }
            }
        }
        ZipOutputStream zos = new ZipOutputStream(
                new FileOutputStream(fileName));
        try {
            zip(zos, relativePath, directory);
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (null != zos) {
                zos.close();
            }
        }
    }

    /**
     * 压缩
     *
     * @param zos          压缩输出流
     * @param relativePath 相对路径
     * @param absolutPath  文件或文件夹绝对路径
     * @throws IOException
     */
    private static void zip(ZipOutputStream zos, String relativePath, String absolutPath) throws IOException {
        File file = new File(absolutPath);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                File tempFile = files[i];
                if (tempFile.isDirectory()) {
                    String newRelativePath = relativePath + tempFile.getName()
                            + slash;
                    createZipNode(zos, newRelativePath);
                    zip(zos, newRelativePath, tempFile.getPath());
                } else {
                    zipFile(zos, tempFile, relativePath);
                }
            }
        } else {
            zipFile(zos, file, relativePath);
        }
    }

    /**
     * 压缩文件
     *
     * @param zos          压缩输出流
     * @param file         文件对象
     * @param relativePath 相对路径
     * @throws IOException
     */
    private static void zipFile(ZipOutputStream zos, File file, String relativePath) throws IOException {
        ZipEntry entry = new ZipEntry(relativePath + file.getName());
        zos.putNextEntry(entry);
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            int bufferSize = 2 << 10;
            int length = 0;
            byte[] buffer = new byte[bufferSize];
            while ((length = is.read(buffer, 0, bufferSize)) >= 0) {
                zos.write(buffer, 0, length);
            }
            zos.flush();
            zos.closeEntry();
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (null != is) {
                is.close();
            }
        }
    }

    /**
     * 创建目录
     *
     * @param zos          zip输出流
     * @param relativePath 相对路径
     * @throws IOException
     */
    private static void createZipNode(ZipOutputStream zos, String relativePath)
            throws IOException {
        ZipEntry zipEntry = new ZipEntry(relativePath);
        zos.putNextEntry(zipEntry);
        zos.closeEntry();
    }

    /**
     * 解压缩zip包
     *
     * @param zipFilePath zip文件路径
     * @param targetPath  解压缩到的位置，如果为null或空字符串则默认解压缩到跟zip包同目录跟zip包同名的文件夹下
     * @throws IOException
     */
    public static void unzip(String zipFilePath, String targetPath)
            throws IOException {
        OutputStream os = null;
        InputStream is = null;
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(zipFilePath);
            String directoryPath = "";
            if (null == targetPath || "".equals(targetPath)) {
                directoryPath = zipFilePath.substring(0, zipFilePath
                        .lastIndexOf(STR));
            } else {
                directoryPath = targetPath;
            }
            Enumeration entryEnum = zipFile.entries();
            if (null != entryEnum) {
                ZipEntry zipEntry = null;
                while (entryEnum.hasMoreElements()) {
                    zipEntry = (ZipEntry) entryEnum.nextElement();
                    if (zipEntry.isDirectory()) {
                        String temDirectoryPath = directoryPath + slash
                                + zipEntry.getName();
                        buildFile(temDirectoryPath, true);
                    }
                    if (zipEntry.getSize() > 0) {
                        // 文件
                        File targetFile = buildFile(directoryPath
                                + slash + zipEntry.getName(), false);
                        os = new BufferedOutputStream(new FileOutputStream(targetFile));
                        is = zipFile.getInputStream(zipEntry);
                        byte[] buffer = new byte[LEN4096];
                        int readLen = 0;
                        while ((readLen = is.read(buffer, 0, LEN4096)) >= 0) {
                            os.write(buffer, 0, readLen);
                        }

                        os.flush();
                        os.close();
                    } else {
                        // 空目录
                        buildFile(directoryPath + slash
                                + zipEntry.getName(), true);
                    }
                }
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (null != zipFile) {
                zipFile = null;
            }
            if (null != is) {
                is.close();
            }
            if (null != os) {
                os.close();
            }
        }
    }

    /**
     * 复制文件夹
     *
     * @param oldPath 源文件夹路径 如：E:/phsftp/src
     * @param newPath 目标文件夹路径 如：E:/phsftp/dest
     */
    public static void copyFolder(String oldPath, String newPath) {
        try {
            mkdirDir(newPath);
            File file = new File(oldPath);
            String[] files = file.list();
            File temp = null;
            for (int i = 0; i < files.length; i++) {
                if (oldPath.endsWith(slash)) {
                    temp = new File(oldPath + files[i]);
                } else {
                    temp = new File(oldPath + slash + files[i]);
                }

                if (temp.isFile()) {
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(newPath
                            + "/" + (temp.getName()));
                    byte[] buffer = new byte[1024 * 2];
                    int len;
                    while ((len = input.read(buffer)) != -1) {
                        output.write(buffer, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if (temp.isDirectory()) {
                    copyFolder(oldPath + "/" + files[i], newPath + "/" + files[i]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 移动文件到指定目录
     *
     * @param oldPath 包含路径的文件名 如：E:/phsftp/src/ljq.txt
     * @param newPath 目标文件目录 如：E:/phsftp/dest
     */
    public static void moveFile(String oldPath, String newPath) throws Exception {
        copyFile(oldPath, newPath);
        delFile(oldPath);
    }

    /**
     * 截取文件名字
     *
     * @param fileName 绝对路径
     * @return 截取后的文件
     */
    public static String getFileName(String fileName) {
        String fileNameNow = fileName.substring(fileName.lastIndexOf(SEGMENTATION) + 1);
        return fileNameNow;
    }

    /**
     * 复制单个文件
     *
     * @param srcFile 包含路径的源文件 如：E:/phsftp/src/abc.txt
     * @param dirDest 目标文件目录；若文件目录不存在则自动创建  如：E:/phsftp/dest
     * @throws IOException
     */
    public static void copyFile(String srcFile, String dirDest) throws Exception {
        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            in = new FileInputStream(new File(srcFile));
            String fileNameNow = getFileName(srcFile);
            mkdirDir(dirDest);
            String srcFilePath = dirDest + File.separator + fileNameNow;
            out = new FileOutputStream(new File(srcFilePath));
            int len;
            byte[] buffer = new byte[1024];
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
            out.close();
            in.close();
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     * 复制单个文件
     *
     * @param srcFile 包含路径的源文件 如：E:/phsftp/src/abc.txt
     * @param dirDest 目标文件目录；若文件目录不存在则自动创建  如：E:/phsftp/dest
     * @throws IOException
     */
    public static void copyFile(String srcFile, String dirDest, String newFile) {
        try {
            FileInputStream in = new FileInputStream(srcFile);
            mkdirDir(dirDest);
            FileOutputStream out = new FileOutputStream(dirDest + "/" + newFile);
            int len;
            byte[] buffer = new byte[1024];
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 移动文件到指定目录，不会删除文件夹
     *
     * @param oldPath 源文件目录  如：E:/phsftp/src
     * @param newPath 目标文件目录 如：E:/phsftp/dest
     */
    public static void moveFiles(String oldPath, String newPath) {
        copyFolder(oldPath, newPath);
        delAllFile(oldPath);
    }

    /**
     * 移动文件到指定目录，会删除文件夹
     *
     * @param oldPath 源文件目录  如：E:/phsftp/src
     * @param newPath 目标文件目录 如：E:/phsftp/dest
     */
    public static void moveFolder(String oldPath, String newPath) {
        copyFolder(oldPath, newPath);
        delFolder(oldPath);
    }

    /**
     * 匹配特定的文件
     *
     * @param dir
     * @param choice
     * @return
     */
    public static String getIndexPath(String dir, String choice) {
        File[] files = new File(dir).listFiles();
        for (File file : files) {
            String path = file.getAbsolutePath();
            if (path.endsWith(choice)) {
                return path;
            }
        }
        return null;
    }


    /**
     * 渎职指定路径中文件内容
     *
     * @param filePath
     * @return
     */
    public static String readFileToString(String filePath) {
        String encoding = "UTF-8";
        File file = new File(filePath);
        Long filelength = file.length();
        String fileContent = null;
        byte[] filecontent = new byte[filelength.intValue()];
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            in.read(filecontent);
            fileContent = new String(filecontent, encoding).replaceAll(REGEX, "");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return fileContent;
    }

    @SuppressWarnings("all")
    public static void readZipJsonFile(String file) throws Exception {
        java.util.zip.ZipFile zf = new java.util.zip.ZipFile(file);
        InputStream in = new BufferedInputStream(new FileInputStream(file));
        ZipInputStream zin = new ZipInputStream(in);
        java.util.zip.ZipEntry ze;
        StringBuffer buffer = new StringBuffer();
        while ((ze = zin.getNextEntry()) != null) {
            if (ze.toString().endsWith("json")) {
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(zf.getInputStream(ze)));
                String line;
                while ((line = br.readLine()) != null) {
                    buffer.append(line.toString());
                    System.out.println(line.toString());
                }
                br.close();
            }
            System.out.println();
        }
        if (null != zf) {
            zf.close();
        }
        if (null != in) {
            in.close();
        }
        if (null != zin) {
            zin.closeEntry();
        }
        System.out.println(buffer.toString());
        JSONObject jsonObject = JSONObject.parseObject(buffer.toString());
        System.out.println(jsonObject.toJSONString());
    }


    /**
     * 读取压缩包中的指定文件
     *
     * @param zipFilePath  zip 压缩包
     * @param jsonFileName json 文件名
     * @return {@link JSONObject}
     * @throws Exception
     */
    @SuppressWarnings("all")
    public static JSONObject readZipJsonFile(String zipFilePath, String jsonFileName) throws Exception {
        java.util.zip.ZipFile zf = new java.util.zip.ZipFile(zipFilePath);
        InputStream in = new BufferedInputStream(new FileInputStream(zipFilePath));
        ZipInputStream zin = new ZipInputStream(in);
        java.util.zip.ZipEntry ze;
        StringBuffer buffer = new StringBuffer();
        while ((ze = zin.getNextEntry()) != null) {
            if (ze.toString().endsWith(jsonFileName)) {
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(zf.getInputStream(ze)));
                String line;
                while ((line = br.readLine()) != null) {
                    buffer.append(line.toString());
                }
                br.close();
            }
        }
        if (null != zf) {
            zf.close();
        }
        if (null != in) {
            in.close();
        }
        if (null != zin) {
            zin.closeEntry();
        }

        return JSONObject.parseObject(buffer.toString());
    }


    private static List<String> getJarList(File file) {
        List<String> list = new ArrayList<>();
        File[] fs = file.listFiles();
        for (File f : fs) {
            if (f.isFile()) {
                String fileName = f.getName();
                String suffixName = fileName.substring(fileName.lastIndexOf("."));
                if (STR_JAR.equalsIgnoreCase(suffixName)) {
                    list.add(f.getName());
                }
            }
        }
        return list;
    }

    /**
     * 获取文件名称
     *
     * @param dir
     * @return
     */
    public static List<String> getJarList(String dir) {
        return getJarList(new File(dir));
    }

}
