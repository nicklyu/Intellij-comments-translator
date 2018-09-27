package com.nicklyu.translator.processors.jvm.java

import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.lang.folding.NamedFoldingDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.JavaDocTokenType.DOC_COMMENT_DATA
import com.intellij.psi.PsiElement
import com.intellij.psi.javadoc.PsiDocComment
import com.intellij.psi.javadoc.PsiDocToken
import com.intellij.psi.util.PsiTreeUtil
import com.nicklyu.translator.processors.CommentProcessor
import com.nicklyu.translator.translators.TranslatorProvider
import com.nicklyu.translator.translators.caching.TranslationCacheManager
import com.nicklyu.translator.translators.exceptions.InvalidApiKeyException
import org.slf4j.LoggerFactory

object JavadocCommentProcessor : CommentProcessor {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun process(project: Project, element: PsiElement): Array<FoldingDescriptor> {
        logger.trace("Processing $element started")
        val descriptors = mutableListOf<FoldingDescriptor>()
        val translatorProvider = project.getComponent(TranslatorProvider::class.java)
        val cacheManager = project.getComponent(TranslationCacheManager::class.java)

        try {
            PsiTreeUtil.findChildrenOfType(element, PsiDocComment::class.java)
                    .forEach { comment ->
                        comment.children.forEach { commentPart ->
                            commentPart.runIfElementIsJavadocLine {
                                descriptors.add(
                                        NamedFoldingDescriptor(
                                                commentPart.node,
                                                TextRange(commentPart.textRange.startOffset, commentPart.textRange.endOffset),
                                                null,
                                                cacheManager.get(commentPart.text)
                                                        ?: translatorProvider.translator.translate(commentPart.text)
                                        )
                                )
                            }
                        }
                    }
            logger.trace("Processing $element finished")
        } catch (e: InvalidApiKeyException) {
            logger.debug("Yandex remote translator responded : ${e.message}")
            //todo: add popup
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