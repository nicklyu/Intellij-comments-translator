package com.nicklyu.translator.builders

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.nicklyu.translator.processors.CommentProcessor

abstract class CommentTranslatorFoldingBuilder : FoldingBuilderEx() {
    override fun getPlaceholderText(node: ASTNode) = ". . ."//todo research

    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> {
        return getProcessors().process(
                project = root.project,
                element = root
        )
    }

    /**
     * While plugin is active we need to collapse comments to display translation
     */
    override fun isCollapsedByDefault(node: ASTNode) = true

    protected abstract fun getProcessors(): Array<CommentProcessor>

    private fun Array<CommentProcessor>.process(project: Project, element: PsiElement): Array<FoldingDescriptor> {
        val descriptors = mutableListOf<FoldingDescriptor>()
        this.forEach { processor ->
            descriptors.addAll(processor.process(project, element))
        }
        return descriptors.toTypedArray()
    }
}