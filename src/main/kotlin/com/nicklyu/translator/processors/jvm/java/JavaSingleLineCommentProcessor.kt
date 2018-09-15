package com.nicklyu.translator.processors.jvm.java

import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.lang.folding.NamedFoldingDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.JavaTokenType
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.nicklyu.translator.processors.CommentProcessor
import com.nicklyu.translator.translators.TranslatorProvider

object JavaSingleLineCommentProcessor : CommentProcessor {
    override fun process(project: Project, element: PsiElement): Array<FoldingDescriptor> {
        val descriptors = mutableListOf<FoldingDescriptor>()
        val translatorProvider = project.getComponent(TranslatorProvider::class.java)

        PsiTreeUtil.findChildrenOfType(element, PsiComment::class.java)
                .forEach { comment ->
                    if (comment.tokenType == JavaTokenType.END_OF_LINE_COMMENT) {
                        descriptors.add(
                                NamedFoldingDescriptor(
                                        comment.node,
                                        TextRange(comment.textRange.startOffset, comment.textRange.endOffset),
                                        null,
                                        translatorProvider.translator.translate(comment.text)
                                )
                        )
                    }
                }
        return descriptors.toTypedArray()
    }
}