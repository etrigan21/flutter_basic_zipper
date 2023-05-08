import 'dart:async';
import 'package:flutter/services.dart';
import 'package:flutter_zipper_basic/models/directory.type.model.dart';

class FlutterZipperBasic {
  static const MethodChannel _zipper = MethodChannel("flutter_zipper_basic");
  static const EventChannel _zipperEvents =
      EventChannel("flutter_zipper_events");
  static const EventChannel _unzipEvents = EventChannel("flutter_unzip_events");

  static Future<void> extractAll({required String destination, required String zipPath,  String? password, Function? getPercentage})async{
    if(getPercentage != null){
      _unzipEvents.receiveBroadcastStream().listen((event) { 
        print(event);
        getPercentage(event);
      });
      _zipper.invokeMethod("extractAll", {
        "destination": destination, 
        "zipPath": zipPath, 
        "password": password
      });
    }

  }

  static Future<void> createZip(
      {required List<String> files,
      required String zipPath,
      bool isPasswordProtected = false,
      String? password,
      Function? getPercentage}) async {
    if (getPercentage != null) {
      _zipperEvents.receiveBroadcastStream().listen((event) {
        print(event);
        getPercentage(event);
      });
    }
    _zipper.invokeMethod("createZipFile", {
      "files": files,
      "zipPath": zipPath,
      "isPasswordProtected": isPasswordProtected,
      "password": password
    });
  }

  static Future<void> createSplitZip({required List<String> files, required String zipPath, bool isPasswordProtected = false, String? password, Function? getPercentage, int splitSize = 2 * 1024 * 1024})async{
    if(getPercentage != null){
            _zipperEvents.receiveBroadcastStream().listen((event) {
        print(event);
        getPercentage(event);
      });
    }
    _zipper.invokeMethod("createSplitZip", {
      "files": files,
      "zipPath": zipPath,
      "isPasswordProtected": isPasswordProtected, 
      "splitSize": splitSize, 
      "password": password
    });
  }

  static Future<String> getPlatformVersion() async {
    var res = await _zipper.invokeMethod("getPlatformVersion");
    return res;
  }

  static Future<String> getExternalDirectory(
      {DirectoryType directoryType = DirectoryType.downloads}) async {
    final int dirType = DirectoryTypes.getType(dirType: directoryType);
    var res = await _zipper.invokeMethod("getDirectory", {"type": dirType});
    return res;
  }

  static Future<List<String>> getFilesInZip({required String zipPath})async{
    List<String> items = await _zipper.invokeMethod("getFileListInZip");
    return items;
  }

  static Future<bool> isPasswordProtected({required String zipPath})async{
    bool isPasswordProtected = await _zipper.invokeMethod("checkIfZipIsPasswordProtected");
    return isPasswordProtected;
  }

    static Future<bool> isZipValid({required String zipPath})async{
    bool isValid = await _zipper.invokeMethod("checkValidity");
    return isValid;
  }

    static Future<bool> isSplitZip({required String zipPath})async{
    bool isSplitZip = await _zipper.invokeMethod("isSplitZip");
    return isSplitZip;
  }
    static Future<void> mergeSplit({required String zipPath, required String mergedZipPath})async{
        await _zipper.invokeMethod("mergeSplit", {
          "zipPath": zipPath,
          "newZipPath": mergedZipPath
        });
  }
  
}
