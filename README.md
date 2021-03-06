[![Build Status](https://travis-ci.org/nicklyu/Intellij-comments-translator.svg?branch=master)](https://travis-ci.org/nicklyu/Intellij-comments-translator)

# Intellij comments translator plugin
Plugin for intellij platform allowing to translate comments in code

Before: 

![Before translation](/doc/images/before.png)

After:  

![After translation](/doc/images/after.png)

### Languages
- [x] Java
- [ ] Kotlin
- [ ] Groovy
- [ ] C#
- [ ] Python

### How to use:

Firstly, you must configure plugin in Settings (File -> Settings -> Comments translator)

![Settings](/doc/images/settings.PNG)

After settings are applied, all opening files will be translated automatically.

To translate some region manually you can use our action (by default alt D)
It translate comments in selected region
![Action demonstration](/doc/images/selection-translation.gif)

##### Translator:
Currently, we support only yandex translator. Configuration and how to get API key are available here [here](https://translate.yandex.com/developers/keys) 
