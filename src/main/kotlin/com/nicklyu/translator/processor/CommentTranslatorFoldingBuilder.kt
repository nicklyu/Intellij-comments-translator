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
import com.intellij.psi.JavaTokenType.END_OF_LINE_COMMENT
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.javadoc.PsiDocComment
import com.intellij.psi.javadoc.PsiDocToken
import com.intellij.psi.util.PsiTreeUtil

class CommentTranslatorFoldingBuilder : FoldingBuilderEx() {
    private val foldingGroup = FoldingGroup.newGroup("CommentTranslatorFolding")

    override fun getPlaceholderText(node: ASTNode, range: TextRange): String {
        return ". . ."
    }

    override fun getPlaceholderText(node: ASTNode): String {
        return ". . ."
    }

    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> {
        val descriptors = mutableListOf<FoldingDescriptor>()

        descriptors.addAll(processJavadocComments(root))
        descriptors.addAll(processSingleLineComments(root))

        return descriptors.toTypedArray()
    }

    private fun processJavadocComments(analyzedElement: PsiElement): Array<FoldingDescriptor> {

        fun areElementsRepresentsJavadocLine(first: PsiElement, second: PsiElement?): Boolean =
                first is PsiDocToken &&
                        first.tokenType == DOC_COMMENT_LEADING_ASTERISKS &&
                        second != null &&
                        second is PsiDocToken &&
                        second.tokenType == DOC_COMMENT_DATA &&
                        second.text.trim().isNotEmpty()


        val descriptors = mutableListOf<FoldingDescriptor>()
        val javadocComments = mutableMapOf<PsiElement, PsiElement>()
        PsiTreeUtil.findChildrenOfType(analyzedElement, PsiDocComment::class.java)
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
                            foldingGroup,
                            "${comment.text} highlighted"
                    )
            )
        }
        return descriptors.toTypedArray()
    }

    private fun processSingleLineComments(analyzedElement: PsiElement): Array<FoldingDescriptor> {
        val descriptors = mutableListOf<FoldingDescriptor>()
        PsiTreeUtil.findChildrenOfType(analyzedElement, PsiComment::class.java)
                .forEach { comment->
                    if(comment.tokenType == END_OF_LINE_COMMENT){
                        descriptors.add(
                                NamedFoldingDescriptor(
                                        comment.node,
                                        TextRange(comment.textRange.startOffset, comment.textRange.endOffset),
                                        foldingGroup,
                                        "${comment.text} highlighted"
                                )
                        )
                    }
                }

        return descriptors.toTypedArray()
    }


    override fun isCollapsedByDefault(node: ASTNode) = true
}