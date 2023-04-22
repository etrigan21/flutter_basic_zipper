package fun.etrigan.flutter_zipper_basic.utils;
import android.app.Activity;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.progress.ProgressMonitor;

import io.flutter.plugin.common.EventChannel;
public class Unzip implements EventChannel.StreamHandler{

    EventChannel.EventSink sink;

    public void extractAllFilesFromZip(String zipDirectory, String destinationDirectory, Activity activity, String password){
        Thread thread = new Thread(()->{
            ZipFile zipFile = password != null? new ZipFile(zipDirectory, password.toCharArray()) : new ZipFile(zipDirectory);
            ProgressMonitor progressMonitor = zipFile.getProgressMonitor();
            try {
                 zipFile.extractAll(destinationDirectory);
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
            } catch (ZipException e) {
                throw new RuntimeException(e) ;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();
    }
    @Override
    public void onListen(Object arguments, EventChannel.EventSink events) {
        sink = events;
    }

    @Override
    public void onCancel(Object arguments) {
        sink = null;
    }
}