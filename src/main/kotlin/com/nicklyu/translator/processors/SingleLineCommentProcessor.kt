package com.nicklyu.translator.processors

import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.lang.folding.NamedFoldingDescriptor
import com.intellij.openapi.util.TextRange
import com.intellij.psi.JavaTokenType
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

object SingleLineCommentProcessor : CommentProcessor {
    override fun process(element: PsiElement): Array<FoldingDescriptor> {
        val descriptors = mutableListOf<FoldingDescriptor>()
        PsiTreeUtil.findChildrenOfType(element, PsiComment::class.java)
                .forEach { comment->
                    if(comment.tokenType == JavaTokenType.END_OF_LINE_COMMENT){
                        descriptors.add(
                                NamedFoldingDescriptor(
                                        comment.node,
                                        TextRange(comment.textRange.startOffset, comment.textRange.endOffset),
                                        null,
                                        "${comment.text} highlighted" //todo change
                                )
                        )
                    }
                }
        return descriptors.toTypedArray()
    }
}