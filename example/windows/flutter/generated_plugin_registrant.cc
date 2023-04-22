//
//  Generated file. Do not edit.
//

// clang-format off

#include "generated_plugin_registrant.h"

#include <flutter_zipper_basic/flutter_zipper_basic_plugin_c_api.h>
#include <permission_handler_windows/permission_handler_windows_plugin.h>

void RegisterPlugins(flutter::PluginRegistry* registry) {
  FlutterZipperBasicPluginCApiRegisterWithRegistrar(
      registry->GetRegistrarForPlugin("FlutterZipperBasicPluginCApi"));
  PermissionHandlerWindowsPluginRegisterWithRegistrar(
      registry->GetRegistrarForPlugin("PermissionHandlerWindowsPlugin"));
}
