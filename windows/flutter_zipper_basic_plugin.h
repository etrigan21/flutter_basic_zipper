#ifndef FLUTTER_PLUGIN_FLUTTER_ZIPPER_BASIC_PLUGIN_H_
#define FLUTTER_PLUGIN_FLUTTER_ZIPPER_BASIC_PLUGIN_H_

#include <flutter/method_channel.h>
#include <flutter/plugin_registrar_windows.h>

#include <memory>

namespace flutter_zipper_basic {

class FlutterZipperBasicPlugin : public flutter::Plugin {
 public:
  static void RegisterWithRegistrar(flutter::PluginRegistrarWindows *registrar);

  FlutterZipperBasicPlugin();

  virtual ~FlutterZipperBasicPlugin();

  // Disallow copy and assign.
  FlutterZipperBasicPlugin(const FlutterZipperBasicPlugin&) = delete;
  FlutterZipperBasicPlugin& operator=(const FlutterZipperBasicPlugin&) = delete;

  // Called when a method is called on this plugin's channel from Dart.
  void HandleMethodCall(
      const flutter::MethodCall<flutter::EncodableValue> &method_call,
      std::unique_ptr<flutter::MethodResult<flutter::EncodableValue>> result);
};

}  // namespace flutter_zipper_basic

#endif  // FLUTTER_PLUGIN_FLUTTER_ZIPPER_BASIC_PLUGIN_H_
