import org.jim2mov.core.*;
import org.jim2mov.utils.MovieUtils;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;

/**
 * 图片文件转视频
 * 注意：图片尺寸要一致，否则会图片变形模糊等异常情况
 *
 * @author zhangsl
 * @date 2018/9/7
 */


public class FilesToMov implements ImageProvider, FrameSavedListener {
    // 文件数组
    private ArrayList<String> fileArray = null;
    // 文件类型
    private int type = MovieInfoProvider.TYPE_QUICKTIME_JPEG;

    // 主函数
    public static void main(String[] args) throws MovieSaveException {
        //读取图片文件
        URL url = FilesToMov.class.getClassLoader().getResource("images");
        File file = new File(url.getFile());

        //视频文件保存目录 请使用绝对路径
        String path = "/Volumes/FinalCut/images.mov";
        // 图片宽度
        int width = 1920;
        // 图片高度
        int height = 1080;
        new FilesToMov(file, path, width, height);
    }

    /**
     * 图片转视频
     *
     * @param filePaths 文件路径数组
     * @param type      格式
     * @param path      文件名
     * @throws MovieSaveException
     */
    public FilesToMov(File file, String path, int width, int height) throws MovieSaveException {
        File[] listFiles = file.listFiles();
        fileArray = new ArrayList();
        for (int i = 0; i < listFiles.length; i++) {
            fileArray.add(listFiles[i].getAbsolutePath());
        }
        // 指定生成视频帧图片格式
        this.type = MovieInfoProvider.TYPE_QUICKTIME_JPEG;
        DefaultMovieInfoProvider dmip = new DefaultMovieInfoProvider(path);
        // 设置帧频率 为了演示效果设定 1帧/秒
        dmip.setFPS(1);
        // 设置帧数 也就是图片的数量
        dmip.setNumberOfFrames(fileArray.size());
        // 设置视频宽度
        dmip.setMWidth(width);
        // 设置视频高度
        dmip.setMHeight(height);
        //开始转换
        new Jim2Mov(this, dmip, this).saveMovie(this.type);
    }

    @Override
    public void frameSaved(int frameNumber) {
        System.out.println("第 " + (frameNumber + 1) + " 帧已生成。");
    }

    @Override
    public byte[] getImage(int frame) {
        try {
            return MovieUtils.convertImageToJPEG(new File(fileArray.get(frame)), 1.0f);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

