package fun.etrigan.flutter_zipper_basic.utils;
import android.app.Activity;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.AesKeyStrength;
import net.lingala.zip4j.model.enums.EncryptionMethod;
import net.lingala.zip4j.progress.ProgressMonitor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.flutter.Log;
import io.flutter.plugin.common.EventChannel;
public class ZipFileList implements EventChannel.StreamHandler{
    EventChannel.EventSink sink = null;

    @Override
    public void onListen(Object arguments, EventChannel.EventSink events) {
        sink = events;
    }

    @Override
    public void onCancel(Object arguments) {
        sink = null;
    }

    public void createZip(ArrayList<String> items, String generatedZipPath, Activity activity, boolean isPasswordProtected, String password ) throws IOException, InterruptedException {
        Thread thread = new Thread(()->{
            List<File> filesToAdd = new ArrayList<>();
            ZipParameters zipParameters = new ZipParameters();
            if(isPasswordProtected){
                zipParameters.setEncryptFiles(true);
                zipParameters.setEncryptionMethod(EncryptionMethod.AES);
                zipParameters.setAesKeyStrength(AesKeyStrength.KEY_STRENGTH_256);
            }
            ZipFile zipFile;
            if(isPasswordProtected){
                zipFile = new ZipFile(generatedZipPath, password.toCharArray());
            } else{
                zipFile = new ZipFile(generatedZipPath);
            }
            ProgressMonitor progressMonitor =  zipFile.getProgressMonitor();
            zipFile.setRunInThread(true);
            for (String file: items){
                filesToAdd.add(new File(file));
            }
            try {
                zipFile.addFiles(filesToAdd, zipParameters);
                if(sink != null){
                    while (!progressMonitor.getState().equals(ProgressMonitor.State.READY)) {
                        activity.runOnUiThread(()-> sink.success(progressMonitor.getPercentDone()));
                        Thread.sleep(100);
                    }

                    if (progressMonitor.getResult().equals(ProgressMonitor.Result.SUCCESS)) {
                        activity.runOnUiThread(()->{
                            sink.success(progressMonitor.getPercentDone());
                        });
                    } else if (progressMonitor.getResult().equals(ProgressMonitor.Result.ERROR)) {
                        activity.runOnUiThread(()->{
                            sink.error(progressMonitor.getException().getLocalizedMessage(), progressMonitor.getException().getMessage(), progressMonitor.getException().getCause());
                        });
                    } else if (progressMonitor.getResult().equals(ProgressMonitor.Result.CANCELLED)) {
                        activity.runOnUiThread(()->{
                            sink.error("cancelled", "cancelled", "cancelled");
                        });
                    }
                }
            } catch (ZipException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();
    }

    public void createSplitZipFromDirectory(String zipPath, String directoryPath, int splitSize, Activity activity){
        Thread thread = new Thread(()->{
            ZipFile zipFile = new ZipFile(zipPath);
            ZipParameters zipParams = new ZipParameters();
            ProgressMonitor progressMonitor = zipFile.getProgressMonitor();
            zipFile.setRunInThread(true);
            try{
                zipFile.createSplitZipFileFromFolder(new File(directoryPath), zipParams, true, splitSize);
                while (!progressMonitor.getState().equals(ProgressMonitor.State.READY)) {
                    activity.runOnUiThread(()-> sink.success(progressMonitor.getPercentDone()));
                    Thread.sleep(100);
                }

                if (progressMonitor.getResult().equals(ProgressMonitor.Result.SUCCESS)) {
                    activity.runOnUiThread(()->{
                        sink.success(progressMonitor.getPercentDone());
                    });
                } else if (progressMonitor.getResult().equals(ProgressMonitor.Result.ERROR)) {
                    activity.runOnUiThread(()->{
                        sink.error(progressMonitor.getException().getLocalizedMessage(), progressMonitor.getException().getMessage(), progressMonitor.getException().getCause());
                    });
                } else if (progressMonitor.getResult().equals(ProgressMonitor.Result.CANCELLED)) {
                    activity.runOnUiThread(()->{
                        sink.error("cancelled", "cancelled", "cancelled");
                    });
                }
            }catch(ZipException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();
    }

    public void createSplitZip(String zipPath, ArrayList<String> files, int splitSize, Activity activity, boolean isPasswordProtected, String password){
        Thread thread = new Thread(()->{
            List<File> filesToAdd = new ArrayList<>();
            ZipFile zipFile;
            zipFile  = isPasswordProtected? new ZipFile(zipPath, password.toCharArray()) : new ZipFile(zipPath);
            ZipParameters zipParams = new ZipParameters();
            ProgressMonitor progressMonitor = zipFile.getProgressMonitor();
            zipFile.setRunInThread(true);
            for (String s: files){
                filesToAdd.add(new File(s));
            }
            try {
                zipFile.createSplitZipFile(filesToAdd, zipParams, true, splitSize);
                if(sink != null){
                    while (!progressMonitor.getState().equals(ProgressMonitor.State.READY)) {
                        activity.runOnUiThread(()-> sink.success(progressMonitor.getPercentDone()));
                        Thread.sleep(100);
                    }

                    if (progressMonitor.getResult().equals(ProgressMonitor.Result.SUCCESS)) {
                        activity.runOnUiThread(()->{
                            sink.success(progressMonitor.getPercentDone());
                        });
                    } else if (progressMonitor.getResult().equals(ProgressMonitor.Result.ERROR)) {
                        activity.runOnUiThread(()->{
                            sink.error(progressMonitor.getException().getLocalizedMessage(), progressMonitor.getException().getMessage(), progressMonitor.getException().getCause());
                        });
                    } else if (progressMonitor.getResult().equals(ProgressMonitor.Result.CANCELLED)) {
                        activity.runOnUiThread(()->{
                            sink.error("cancelled", "cancelled", "cancelled");
                        });
                    }
                }
            } catch (ZipException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();
    }

    public List<String> getFileListInZip(String zipPath) throws ZipException {
        List<String> filenames = new ArrayList<>();
        List<FileHeader> fileHeaders = new ZipFile(zipPath).getFileHeaders();
        fileHeaders.stream().forEach(fileHeader -> {
            Log.d("File-header", fileHeader.getFileName());
            filenames.add(fileHeader.getFileName());
        });
        return filenames;
    }

    public Boolean checkIfZipIsPasswordProtected(String zipPath) throws ZipException {
        Boolean isProtected = new ZipFile(zipPath).isEncrypted();
        return  isProtected;
    }

    public Boolean checkIfZipIsValid(String zipPath) throws ZipException{
        Boolean isValid = new ZipFile(zipPath).isValidZipFile();
        return isValid;
    }

    public Boolean checkIfSplitZip(String zipPath) throws ZipException{
        Boolean isSplit = new ZipFile(zipPath).isSplitArchive();
        return isSplit;
    }

    public void mergeSplitZip(String zipPath, String newZipPath, Activity activity) throws ZipException {
        Thread thread = new Thread(()->{
            ZipFile zipFile = new ZipFile(zipPath);
            ProgressMonitor progressMonitor =  zipFile.getProgressMonitor();
            zipFile.setRunInThread(true);
            try {
                zipFile.mergeSplitFiles(new File(newZipPath));
                if(sink != null){
                    while (!progressMonitor.getState().equals(ProgressMonitor.State.READY)) {
                        activity.runOnUiThread(()-> sink.success(progressMonitor.getPercentDone()));
                        Thread.sleep(100);
                    }

                    if (progressMonitor.getResult().equals(ProgressMonitor.Result.SUCCESS)) {
                        activity.runOnUiThread(()->{
                            sink.success(progressMonitor.getPercentDone());
                        });
                    } else if (progressMonitor.getResult().equals(ProgressMonitor.Result.ERROR)) {
                        activity.runOnUiThread(()->{
                            sink.error(progressMonitor.getException().getLocalizedMessage(), progressMonitor.getException().getMessage(), progressMonitor.getException().getCause());
                        });
                    } else if (progressMonitor.getResult().equals(ProgressMonitor.Result.CANCELLED)) {
                        activity.runOnUiThread(()->{
                            sink.error("cancelled", "cancelled", "cancelled");
                        });
                    }
                }
            } catch (ZipException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();

    }
}