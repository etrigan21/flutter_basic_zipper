#include "include/flutter_zipper_basic/flutter_zipper_basic_plugin_c_api.h"

#include <flutter/plugin_registrar_windows.h>

#include "flutter_zipper_basic_plugin.h"

void FlutterZipperBasicPluginCApiRegisterWithRegistrar(
    FlutterDesktopPluginRegistrarRef registrar) {
  flutter_zipper_basic::FlutterZipperBasicPlugin::RegisterWithRegistrar(
      flutter::PluginRegistrarManager::GetInstance()
          ->GetRegistrar<flutter::PluginRegistrarWindows>(registrar));
}
