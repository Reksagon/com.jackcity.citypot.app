-keep class yourpackage.** { *; }
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose
-keep class com.appsflyer.** { *; }

-obfuscationdictionary dictionary.txt
-packageobfuscationdictionary dictionary.txt
-classobfuscationdictionary dictionary.txt