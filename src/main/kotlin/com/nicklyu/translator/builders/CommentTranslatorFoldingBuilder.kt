package com.nicklyu.translator.builders

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.psi.PsiElement
import com.nicklyu.translator.processors.CommentProcessor
import org.slf4j.LoggerFactory

abstract class CommentTranslatorFoldingBuilder : FoldingBuilderEx() {
    private val logger = LoggerFactory.getLogger(CommentTranslatorFoldingBuilder::class.java)

    override fun getPlaceholderText(node: ASTNode) = ". . ."//todo research

    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> {
        if (!quick) {
            logger.trace("Folding requested for $root :: $document")
            return getProcessors().process(
                    element = root
            )
        }
        return arrayOf()
    }

    /**
     * While plugin is active we need to collapse comments to display translation
     */
    override fun isCollapsedByDefault(node: ASTNode) = true

    protected abstract fun getProcessors(): Array<CommentProcessor>

    private fun Array<CommentProcessor>.process(element: PsiElement): Array<FoldingDescriptor> {
        val descriptors = mutableListOf<FoldingDescriptor>()
        this.forEach { processor ->
            descriptors.addAll(processor.process(element.project, element))
        }
        return descriptors.toTypedArray()
    }
}