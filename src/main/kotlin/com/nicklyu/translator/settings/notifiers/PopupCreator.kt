package com.nicklyu.translator.settings.notifiers

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.MessageType
import com.intellij.openapi.ui.popup.Balloon
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.wm.WindowManager
import com.intellij.ui.awt.RelativePoint

object PopupCreator {
    fun createPopup(project: Project, message: String, messageType: MessageType) {
        val statusBar = WindowManager.getInstance().getStatusBar(project)
        JBPopupFactory.getInstance()
                .createHtmlTextBalloonBuilder(message, messageType, null)
                .setFadeoutTime(5000)
                .createBalloon()
                .show(RelativePoint.getSouthEastOf(statusBar.component), Balloon.Position.atRight)
    }
}