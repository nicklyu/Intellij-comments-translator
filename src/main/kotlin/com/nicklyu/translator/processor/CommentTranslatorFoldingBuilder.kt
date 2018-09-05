package com.nicklyu.translator.processor

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.lang.folding.NamedFoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.FoldingGroup
import com.intellij.openapi.util.TextRange
import com.intellij.psi.JavaDocTokenType.DOC_COMMENT_DATA
import com.intellij.psi.JavaDocTokenType.DOC_COMMENT_LEADING_ASTERISKS
import com.intellij.psi.PsiElement
import com.intellij.psi.javadoc.PsiDocComment
import com.intellij.psi.javadoc.PsiDocToken
import com.intellij.psi.util.PsiTreeUtil

class CommentTranslatorFoldingBuilder : FoldingBuilderEx() {
    override fun getPlaceholderText(node: ASTNode, range: TextRange): String {
        return ". . ."
    }

    override fun getPlaceholderText(node: ASTNode): String {
        return ". . ."
    }

    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> {
        val foldingGroup = FoldingGroup.newGroup("CommentTranslatorFolding")
        val descriptors = mutableListOf<FoldingDescriptor>()

        val javadocComments = mutableMapOf<PsiElement, PsiElement>()
        PsiTreeUtil.findChildrenOfType(root, PsiDocComment::class.java)
                .forEach { comment ->

                    val commentParts = comment.children
                    commentParts.forEachIndexed { index, psiElement ->
                        if (areElementsRepresentsJavadocLine(psiElement, commentParts.getOrNull(index + 1))) {
                            javadocComments[psiElement] = comment.children[index + 1]
                        }
                    }
                }

        javadocComments.forEach { asterisks, comment ->
            descriptors.add(
                    NamedFoldingDescriptor(
                            asterisks.node,
                            TextRange(comment.textRange.startOffset, comment.textRange.endOffset),
                            foldingGroup,
                            comment.text
                    )
            )
        }

        return descriptors.toTypedArray()
    }

    private fun areElementsRepresentsJavadocLine(first: PsiElement, second: PsiElement?): Boolean =
            first is PsiDocToken &&
                    first.tokenType == DOC_COMMENT_LEADING_ASTERISKS &&
                    second != null &&
                    second is PsiDocToken &&
                    second.tokenType == DOC_COMMENT_DATA &&
                    second.text.trim().isNotEmpty()


    override fun isCollapsedByDefault(node: ASTNode) = true
}