import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:flutter_zipper_basic/flutter_zipper_basic_method_channel.dart';

void main() {
  MethodChannelFlutterZipperBasic platform = MethodChannelFlutterZipperBasic();
  const MethodChannel channel = MethodChannel('flutter_zipper_basic');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await platform.getPlatformVersion(), '42');
  });
}
