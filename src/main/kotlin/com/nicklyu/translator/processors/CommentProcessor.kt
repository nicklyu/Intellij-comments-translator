package com.nicklyu.translator.processors

import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement

interface CommentProcessor {
    fun process(project: Project, element: PsiElement): Array<FoldingDescriptor>
}