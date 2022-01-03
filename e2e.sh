#!/bin/sh

# disable animations or test may not stable
adb shell settings put global window_animation_scale 0.0
adb shell settings put global transition_animation_scale 0.0
adb shell settings put global animator_duration_scale 0.0

rm -rf DCIM
adb shell rm /storage/emulated/0/DCIM/*.png
./gradlew :app:uninstallAll :app:connectedNoAnalyticsDebugAndroidTest -x lint -PdisablePreDex
if [ "$?" != "0" ]; then
  adb pull /storage/emulated/0/DCIM/ .
  if [ "$1" != "--CI" ]; then
    open DCIM/*.png
  fi

  exit 1
fi