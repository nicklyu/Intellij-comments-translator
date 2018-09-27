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
import com.nicklyu.translator.translators.caching.TranslationCacheManager
import com.nicklyu.translator.translators.exceptions.InvalidApiKeyException
import org.slf4j.LoggerFactory

object JavaSingleLineCommentProcessor : CommentProcessor {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun process(project: Project, element: PsiElement): Array<FoldingDescriptor> {
        logger.trace("Processing $element started")

        val descriptors = mutableListOf<FoldingDescriptor>()
        val translatorProvider = project.getComponent(TranslatorProvider::class.java)
        val cacheManager = project.getComponent(TranslationCacheManager::class.java)

        try {
            PsiTreeUtil.findChildrenOfType(element, PsiComment::class.java)
                    .forEach { comment ->
                        if (comment.tokenType == JavaTokenType.END_OF_LINE_COMMENT) {
                            descriptors.add(
                                    NamedFoldingDescriptor(
                                            comment.node,
                                            TextRange(comment.textRange.startOffset, comment.textRange.endOffset),
                                            null,
                                            cacheManager.get(comment.text)
                                                    ?: translatorProvider.translator.translate(comment.text))
                            )
                        }
                    }
            logger.trace("Processing $element finished")
        } catch (e: InvalidApiKeyException) {
            logger.debug("Yandex remote translator responded : ${e.message}")
            //todo: add popup
        }

        return descriptors.toTypedArray()
    }
}