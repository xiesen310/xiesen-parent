package com.github.xiesen.junit.template;

import com.github.xiesen.junit.utils.RespDto;
import com.github.xiesen.junit.utils.RespHelper;

/**
 * @author 谢森
 * @Description TODO
 * @Email xiesen310@163.com
 * @Date 2021/1/27 11:31
 */
public abstract class BaseResourceManagement {

    public RespDto uploadResource(String file, String uploadPath, String type) {
        try {
            /**
             * 1. 判断资源类型
             */
            judgeResourceType(file);

            /**
             * 2. 丰富上传路径
             */
            richResourceUploadPath(uploadPath);

            /**
             * 3. 上传文件到临时文件夹
             */
            String oldSource = uploadResource2Tmp(file, uploadPath);

            /**
             * 4. 判断安装包中是否有特有的 json 文件
             */
            String newPath = getUpdatePath(file);

            /**
             * 5. 将上传的文件转移到对应的目录下
             */
            return moveResource(oldSource, newPath);
        } catch (Exception e) {
            e.printStackTrace();
            return RespHelper.fail(-1, "资源上传失败");
        }
    }

    private RespDto moveResource(String oldSource, String newPath) {
        return RespHelper.ok("将上传的文件转移到对应的目录下");
    }

    protected abstract String getUpdatePath(String file);

    protected abstract String uploadResource2Tmp(String file, String uploadPath);

    private void richResourceUploadPath(String uploadPath) {
        System.out.println("丰富上传路径");
    }

    private void judgeResourceType(String file) {
        System.out.println("判断资源类型");
    }


}
