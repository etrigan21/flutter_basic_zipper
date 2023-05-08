package fun.etrigan.flutter_zipper_basic;

import android.app.Activity;

import androidx.annotation.NonNull;

import net.lingala.zip4j.exception.ZipException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fun.etrigan.flutter_zipper_basic.utils.CustomException;
import fun.etrigan.flutter_zipper_basic.utils.Unzip;
import fun.etrigan.flutter_zipper_basic.utils.Utils;
import fun.etrigan.flutter_zipper_basic.utils.ZipFileList;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/** FlutterZipperBasicPlugin */
public class FlutterZipperBasicPlugin implements FlutterPlugin, MethodCallHandler, ActivityAware {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;
  private final ZipFileList _zipFileList = new ZipFileList();
  private final Utils _utils = new Utils();
  private final Unzip _unzip = new Unzip();
  Activity _activity = null;
  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "flutter_zipper_basic");
    new EventChannel(flutterPluginBinding.getBinaryMessenger(), "flutter_zipper_events").setStreamHandler(_zipFileList);
    new EventChannel(flutterPluginBinding.getBinaryMessenger(), "flutter_unzip_events").setStreamHandler(_unzip);
    channel.setMethodCallHandler(this);
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    switch (call.method) {
      case "getPlatformVersion":
        result.success("Android " + android.os.Build.VERSION.RELEASE);
        break;
      case "createZipFile": {
        ArrayList<String> filesToAdd = call.argument("files");
        String zipPath = call.argument("zipPath");
        boolean isPasswordProtected = call.argument("isPasswordProtected");
        String password = isPasswordProtected ? call.argument("password") : null;
        try {
          _zipFileList.createZip(filesToAdd, zipPath, _activity, isPasswordProtected, password);
        } catch (IOException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      }
      break;
      case "getDirectory": {
        Integer type = call.argument("type");
        try {
          String res = _utils.getExternalStoragePath(type);
          result.success(res);
        } catch (CustomException e) {
          throw new RuntimeException(e);
        }
      }
      break;
      case "createSplitZip": {
        ArrayList<String> filesToAdd = call.argument("files");
        String zipPath = call.argument("zipPath");
        boolean isPasswordProtected = call.argument("isPasswordProtected");
        int splitSize = call.argument("splitSize");
        String password = isPasswordProtected? call.argument("password") : null;
        _zipFileList.createSplitZip(zipPath, filesToAdd, splitSize, _activity, isPasswordProtected, password);
      }
      break;
      case "extractAll":
      {
        String password = call.argument("password");
        String zipPath = call.argument("zipPath");
        String destination = call.argument("destination");
        _unzip.extractAllFilesFromZip(zipPath, destination,_activity ,password);
      }
      break;
      case "createSplitZipFromDirectory": {
        String zipPath = call.argument("zipPath");
        String directoryPath = call.argument("directory");
        int splitSize = call.argument("splitSize");
        _zipFileList.createSplitZipFromDirectory(zipPath, directoryPath, splitSize, _activity);
      }
      break;
      case "getFileListInZip": {
        String zipPath = call.argument("zipPath");
        try {
          List<String> files = _zipFileList.getFileListInZip(zipPath);
          result.success(files);
        } catch (ZipException e) {
          throw new RuntimeException(e);
        }
      } break;
      case "checkIfZipIsPasswordProtected": {
        String zipPath = call.argument("zipPath");
        try {
          Boolean isProtected = _zipFileList.checkIfZipIsPasswordProtected(zipPath);
          result.success(isProtected);
        } catch (ZipException e) {
          throw new RuntimeException(e);
        }
      } break;
      case "checkValidity": {
        String zipPath = call.argument("zipPath");
        try {
          Boolean isValid = _zipFileList.checkIfZipIsValid(zipPath);
          result.success(isValid);
        } catch (ZipException e) {
          throw new RuntimeException(e);
        }
      } break;
      case "isSplitZip": {
        String zipPath = call.argument("zipPath");
        try {
          Boolean isSplit = _zipFileList.checkIfSplitZip(zipPath);
          result.success(isSplit);
        } catch (ZipException e) {
          throw new RuntimeException(e);
        }
      } break;
      case "mergeSplit": {
        String zipPath = call.argument("zipPath");
        String newZip = call.argument("newZipPath");
        try {
          _zipFileList.mergeSplitZip(zipPath, newZip, _activity);
        } catch (ZipException e) {
          throw new RuntimeException(e);
        }
      } break;
      default: {
        result.notImplemented();
      }
      break;
    }  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    _activity = null;
    channel.setMethodCallHandler(null);
  }

  @Override
  public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
    _activity = binding.getActivity();
  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {
    _activity = null;
  }

  @Override
  public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
    _activity = binding.getActivity();
  }

  @Override
  public void onDetachedFromActivity() {
    _activity = null;
  }
}
