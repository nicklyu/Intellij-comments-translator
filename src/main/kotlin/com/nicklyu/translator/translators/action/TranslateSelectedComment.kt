package com.nicklyu.translator.translators.action

import com.intellij.openapi.editor.actionSystem.EditorAction

class TranslateSelectedComment : EditorAction(TargetTranslationCommentHandler())