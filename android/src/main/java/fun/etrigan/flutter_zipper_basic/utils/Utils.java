package fun.etrigan.flutter_zipper_basic.utils;

import android.os.Build;
import android.os.Environment;

public class Utils {
    public String getExternalStoragePath (Integer source) throws CustomException {
        String dir = getEnv(source);
        String EXPORT_DIR_PATH = String.valueOf(Environment.getExternalStoragePublicDirectory(dir));
        return EXPORT_DIR_PATH;
    }

    String getEnv(Integer value) throws CustomException {
        if(value == 0){
            return Environment.DIRECTORY_DOWNLOADS;
        } else if (value == 1){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                return Environment.DIRECTORY_DOCUMENTS;
            } else {
                throw new CustomException("OS Version must be greater than kitkat");
            }
        } else if (value == 2){
            return Environment.DIRECTORY_PICTURES;
        } else if (value == 3){
            return Environment.DIRECTORY_MOVIES;
        } else if (value == 4){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                return Environment.DIRECTORY_SCREENSHOTS;
            } else {
                throw new CustomException("OS Version must be greater than or equal to Q for Screenshots");
            }
        } else if (value ==5){
            return Environment.DIRECTORY_MUSIC;
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                return Environment.DIRECTORY_RECORDINGS;
            } else {
                throw new CustomException("OS must be at least version S for recordings");
            }
        }
    }
}

