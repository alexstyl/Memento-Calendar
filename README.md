# Memento Calendar for Android  [![alt text](https://travis-ci.org/alexstyl/Memento-Calendar.svg?branch=master "Check the build status on Travis CI")](https://travis-ci.org/alexstyl/Memento-Calendar)

<img src="https://github.com/alexstyl/Memento-Calendar/blob/master/android_common/src/main/res/mipmap-xxxhdpi/ic_launcher.png?raw=true" width="200" align="right" hspace="20">

Memento Calendar is a modern namedays app for Android.
This repository contains the source code of Memento Calendar.

You can get started by having a look at the project's wiki. It contains some information about how to get Memento up and running on your machine and other useful info.

This repo is open for PRs and they are more than welcome! Have a look [at the wiki page to see how to contribute](https://github.com/alexstyl/Memento-Calendar/wiki/How-to-contribute).


[![alt text](http://developer.android.com/images/brand/en_app_rgb_wo_60.png "Download Memento Calendar from the Play Store")](https://play.google.com/store/apps/details?id=com.alexstyl.specialdates)

## Project Goal
Memento Calendar is my pet project/playground in which I experiment with various platform features development patterns and share my foundings with the community my foundings via blog posts and talks. Memento started off as a side project app back in 2014 and has been on development on and off. Current goal of the project is to split out the business logic of the app from the app logic so that it could potentially be ported into other platforms with the help of Kotlin.

## Modules
The app is split into multiple modules. 
The business logic of the app can be found in the **memento** module. There are three other Android Modules: 

### android_common
This is the shared resources across all Android specific modules. It depends on *memento*.

### android_wear
This is the Android Wear module. It depends on *android_common*.

### android_mobile
This is the Android mobile app module. It depends on *android_common*.

## Architecture
The Model-View-Presenter is used in order to architecture the app. 

**Presenters** are platform agnostic and live in the **memento** module, in order to be able to be used across all platforms. They contain the core logic of forwarding *Models* to the *Views*. It is up for the specific platform component to create a View

**Views** are responsible displaying information back to the user. For each view there is one interface that lives in the memento module. A view is not to be confused with Android's View classes, Activities or Fragments. 

**Models** contain the minimum amount of information needed to render the information on the screen.

I did a talk in the GDG Android Athens about the structure of Memento Calendar. The talk is in Greek, but the slides contain more information about the structure [(see the slides)](https://speakerdeck.com/alexstyl/the-journey-towards-a-platform-agnostic-codebase).



## License
```
MIT License

Copyright (c) 2016 Alex Styl

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
