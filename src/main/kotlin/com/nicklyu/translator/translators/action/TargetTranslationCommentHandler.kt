package com.nicklyu.translator.translators.action

import com.intellij.codeInsight.folding.impl.actions.BaseFoldingHandler
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.editor.Caret
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.FoldRegion

class TargetTranslationCommentHandler : BaseFoldingHandler() {
    override fun doExecute(editor: Editor, caret: Caret?, dataContext: DataContext?) {
        editor.foldingModel.runBatchFoldingOperation {
            selectedRegionFoldings(editor.caretModel.currentCaret).forEach { region ->
                region.isExpanded = false
            }
        }
    }

    /**
     * Foldings, which contains in selected region
     */
    private fun selectedRegionFoldings(caret: Caret): MutableList<FoldRegion> {
        val foldings = mutableListOf<FoldRegion>()

        for (region in caret.editor.foldingModel.allFoldRegions) {
            if (region.startOffset >= caret.selectionStart && region.endOffset <= caret.selectionEnd) {
                foldings.add(region)
            }
        }

        return foldings
    }
}