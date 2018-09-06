package com.nicklyu.translator.processors

import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.lang.folding.NamedFoldingDescriptor
import com.intellij.openapi.util.TextRange
import com.intellij.psi.JavaDocTokenType
import com.intellij.psi.PsiElement
import com.intellij.psi.javadoc.PsiDocComment
import com.intellij.psi.javadoc.PsiDocToken
import com.intellij.psi.util.PsiTreeUtil

object JavadocCommentProcessor : CommentProcessor {
    override fun process(element: PsiElement): Array<FoldingDescriptor> {
        val descriptors = mutableListOf<FoldingDescriptor>()
        val javadocComments = mutableMapOf<PsiElement, PsiElement>()
        PsiTreeUtil.findChildrenOfType(element, PsiDocComment::class.java)
                .forEach { comment ->

                    val commentParts = comment.children
                    commentParts.forEachIndexed { index, psiElement ->
                        if (areElementsRepresentsJavadocLine(psiElement, commentParts.getOrNull(index + 1))) {
                            javadocComments[psiElement] = comment.children[index + 1]
                        }
                    }
                }

        javadocComments.forEach { asterisk, comment ->
            descriptors.add(
                    NamedFoldingDescriptor(
                            asterisk.node,
                            TextRange(comment.textRange.startOffset, comment.textRange.endOffset),
                            null,
                            "${comment.text} highlighted" //todo change
                    )
            )
        }
        return descriptors.toTypedArray()
    }

    private fun areElementsRepresentsJavadocLine(first: PsiElement, second: PsiElement?): Boolean =
            first is PsiDocToken &&
                    first.tokenType == JavaDocTokenType.DOC_COMMENT_LEADING_ASTERISKS &&
                    second != null &&
                    second is PsiDocToken &&
                    second.tokenType == JavaDocTokenType.DOC_COMMENT_DATA &&
                    second.text.trim().isNotEmpty()
}