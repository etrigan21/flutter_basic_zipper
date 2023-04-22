# flutter_zipper_basic

A Flutter plugin designed to create and extract files from zip files. Currently, this plugin only includes the android APIs. 

## Getting Started

### Installing the plugin to your pubspec.yaml

```Yaml
dependencies:
  flutter:
    sdk: flutter

  flutter_zipper_basic:
    git:
      url: git@github.com:etrigan21/flutter_basic_zipper.git
      ref: main # branch name

```

### Creation of zip files

#### 1. Creating zip file

The following is used to create a regular zip file.

```Dart
    FlutterZipperBasic.createZip(
        files: [file1, file2, file3], 
        zipPath: zipPath)
```

In order to create password protected zip files, add the following parameters

```Dart
FlutterZipperBasic.createZip(
    files: [file1, file, file3],
    zipPath: zipPath
    isPasswordProtected: true, 
    password: "String password"
)
```

To listen to the percent completion of the zipping process, add the following params

```Dart
    FlutterZipperBasic.createZip(
        files: [file1, file, file3],
        zipPath: zipPath
        isPasswordProtected: true, 
        password: "String password"
        getPercentage: (value){}
    )
```

### 2. Creating a split zip file

To create a split zip file, use the following:

```Dart
FlutterZipperBasic.createSplitZip(
    files: fileList,
    zipPath: externalStorage + "/test.zip",
);
```

To add password to the zip file

```Dart
FlutterZipperBasic.createSplitZip(
    files: fileList,
    isPasswordProtected: true
    password: "String password",
    zipPath: externalStorage + "/test.zip",
);
```

To listen for percent progression: 

```Dart
FlutterZipperBasic.createSplitZip(
    files: fileList,
    isPasswordProtected: true
    password: "String password",
    zipPath: externalStorage + "/test.zip",
    getPercentage: (value) {
      print(value);
    }
);
```


## ToDo: 

1. Create a better example
2. Update functionalities