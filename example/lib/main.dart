import 'package:flutter/material.dart';
import 'package:flutter_zipper_basic/flutter_zipper_basic.dart';
import 'package:file_picker/file_picker.dart';
import 'package:flutter_zipper_basic/models/directory.type.model.dart';
import 'package:permission_handler/permission_handler.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  void getStoragePermission() async {
    var status = await Permission.manageExternalStorage.request();
    var state = await Permission.storage.request();
  }

  @override
  void initState() {
    super.initState();
    getStoragePermission();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
            child: Column(
          children: [
            ElevatedButton(
              onPressed: () async {
                FilePickerResult? result =
                    await FilePicker.platform.pickFiles(allowMultiple: true);
                List<String> fileList = [];
                if (result != null) {
                  for (var i in result.files) {
                    fileList.add(i.path!);
                  }
                  var externalStorage =
                      await FlutterZipperBasic.getExternalDirectory(
                          directoryType: DirectoryType.documents);

                  FlutterZipperBasic.createSplitZip(
                      files: fileList,
                      password: "password",
                      isPasswordProtected: true,
                      zipPath: externalStorage + "/test.zip",
                      getPercentage: (value) {
                        print(value);
                      });
                }
              },
              child: Text("test"),
            ),
            ElevatedButton(
              onPressed: () async {
                var externalStorage =
                    await FlutterZipperBasic.getExternalDirectory(
                        directoryType: DirectoryType.documents);
                FlutterZipperBasic.extractAll(destination: externalStorage + "/test", zipPath: externalStorage+"/test.zip", getPercentage: (values){
                  print(values);
                });
              },
              child: Text("unzip"),
            ),
          ],
        )),
      ),
    );
  }
}
