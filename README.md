iBeatCon for Android
===============
This is a Android ported version of iBeatCon which simulates beatmaniaIIDX Controller on iOS Device

Original iOS Application and Sever Developer : @Harunene

Compatible Android Application Developer : @kuna

# Requirement
Android Device (least 4.0) / PC (with iBeatCon Server 0.9.2|1.0.0) / BMS Player (Like LR2 | Ruv-It!)

# HOW TO USE
* Please make sure your PC Firewall setting allow 2001(0.9.2) or 10070(1.0.0) port and both device (PC & Android) are on same network.
1. Download iBeatCon_Android.apk and Install your Android Device.
2. Download iBeatConServer.exe from http://kuna.tistory.com/1378 (0.9.2) or https://www.dropbox.com/s/xwjk6e7800fiws1/iBeatconServer.exe (1.0.0) and run iBeatConServer.exe as Administrator (for Socket Open).
3. Open iBeatCon for Android Application and Set your IP, select 2P Mode or Scratch Only Mode or Key Only Mode or New Server Compatible Mode (for 1.0.0) then Tab "Save and Apply".
4. Run Lunatic Rave 2 then goto System Setting -> Key Config bind TAB key as Start Button.
5. Enjoy It.

# Tested Device
* Galaxy S2 LTE
* Galaxy Note 10.1 (not 2014)
* Nexus S
* Optimus 2X

# ChangeLog
* 130126 - Scratch Feature Finished and Rewrite View Source Code. (@kuna)
* 130127 - Fix Scratch issue. (@kuna)
* 140128 - IP Save Feature Implement. (@xuserv)
* 140317 - Add Setting and Key-only mode Feature. (@xuserv)
* 140318 - Add Socket Timed out. (@xuserv)
* 140319 - Add Screen Size Detect Feature, Turntable is now complete, Start button added but not gonna work. (@xuserv)
* 140320 - Turntable is now *fully* functional! Start button is now works! Start button available only on Tablet. (Thx to @Harunene) (@xuserv)
* 140320-a - Fix Screen Size Detect Problem and Start button appear on 2P Mode and Key-only Mode (not yet on Phone.) (@xuserv)
* 140322 - Change IP Input Text Box as Phone (in Settings) (@xuserv)
* 140322-a - Add Force Fullscreen Mode Feature (@xuserv)
* 140323 - Remove Force Fullscreen Mode and Replace Automatic Fullscreen Feature, No Longer Support Under 4.0 (@xuserv)
* 140324 - Fix Settings Layout Crash, Add Bigger Resolution Screen Support, Add Start button on Phone. (Key Only Mode Only.) (@xuserv)
* 140328 - Add New Server Compatible Mode, Fix Infinite Activity Loop Issue. (@xuserv)
* 140328-a - Fix "New Server Compatible Mode" Checkbox State Save Issue. (@xuserv)
* 140329 - Add View Restoring Feature. (@xuserv)
* 140329-a - Add Scratch Only Mode, Adjust Controller Layout. (@xuserv)
* 140329-b - Add Korean, Japanese (thx to @Galpum) Support, Fix Restoring View Issue. (@xuserv)
* 140330 - Rewrite SurfaceView Code (thx to @Soreebreaker), Add Exception No Longer Need Data Delete or Reinstall. (@xuserv)
* 140330-a - Add Spanish Support (thx to @_brandNEWdays), Fix Some Bugs (@xuserv)
* 140330-b - Fix Controller Layout in DENSITY_XHIGH Device like Moto X or Nexus 5. (@xuserv)