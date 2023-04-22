import 'package:flutter_test/flutter_test.dart';
import 'package:flutter_zipper_basic/flutter_zipper_basic.dart';
import 'package:flutter_zipper_basic/flutter_zipper_basic_platform_interface.dart';
import 'package:flutter_zipper_basic/flutter_zipper_basic_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockFlutterZipperBasicPlatform
    with MockPlatformInterfaceMixin
    implements FlutterZipperBasicPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final FlutterZipperBasicPlatform initialPlatform = FlutterZipperBasicPlatform.instance;

  test('$MethodChannelFlutterZipperBasic is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelFlutterZipperBasic>());
  });

  test('getPlatformVersion', () async {
    FlutterZipperBasic flutterZipperBasicPlugin = FlutterZipperBasic();
    MockFlutterZipperBasicPlatform fakePlatform = MockFlutterZipperBasicPlatform();
    FlutterZipperBasicPlatform.instance = fakePlatform;

    expect(await flutterZipperBasicPlugin.getPlatformVersion(), '42');
  });
}
