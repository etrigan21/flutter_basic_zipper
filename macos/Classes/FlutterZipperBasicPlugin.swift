import Cocoa
import FlutterMacOS

public class FlutterZipperBasicPlugin: NSObject, FlutterPlugin {
  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "flutter_zipper_basic", binaryMessenger: registrar.messenger)
    let instance = FlutterZipperBasicPlugin()
    registrar.addMethodCallDelegate(instance, channel: channel)
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    switch call.method {
    case "getPlatformVersion":
      result("macOS " + ProcessInfo.processInfo.operatingSystemVersionString)
    default:
      result(FlutterMethodNotImplemented)
    }
  }
}
