package com.nicklyu.translator.processors.jvm.java

import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.lang.folding.NamedFoldingDescriptor
import com.intellij.openapi.util.TextRange
import com.intellij.psi.JavaDocTokenType
import com.intellij.psi.JavaDocTokenType.DOC_COMMENT_DATA
import com.intellij.psi.PsiElement
import com.intellij.psi.javadoc.PsiDocComment
import com.intellij.psi.javadoc.PsiDocToken
import com.intellij.psi.util.PsiTreeUtil
import com.nicklyu.translator.processors.CommentProcessor

object JavadocCommentProcessor : CommentProcessor {
    override fun process(element: PsiElement): Array<FoldingDescriptor> {
        val descriptors = mutableListOf<FoldingDescriptor>()

        PsiTreeUtil.findChildrenOfType(element, PsiDocComment::class.java)
                .forEach { comment ->
                    comment.children.forEach { commentPart ->
                        commentPart.runIfElementIsJavadocLine {
                            descriptors.add(
                                    NamedFoldingDescriptor(
                                            commentPart.node,
                                            TextRange(commentPart.textRange.startOffset, commentPart.textRange.endOffset),
                                            null,
                                            "${commentPart.text} highlighted" //todo change
                                    )
                            )
                        }
                    }
                }
        return descriptors.toTypedArray()
    }


    private inline fun PsiElement.runIfElementIsJavadocLine(block: () -> Unit) {
        if (this is PsiDocToken &&
                this.tokenType == DOC_COMMENT_DATA &&
                this.text.isNotBlank()) {
            block.invoke()
        }
    }
}