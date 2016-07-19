cordova.define('cordova/plugin_list', function(require, exports, module) {
module.exports = [
    {
        "file": "plugins/com.mbientlab.metawear/www/MWDevice.js",
        "id": "com.mbientlab.metawear.MWDevice",
        "pluginId": "com.mbientlab.metawear",
        "clobbers": [
            "metawear.mwdevice"
        ]
    }
];
module.exports.metadata = 
// TOP OF METADATA
{
    "cordova-plugin-whitelist": "1.2.2",
    "com.mbientlab.metawear": "0.2.15"
}
// BOTTOM OF METADATA
});