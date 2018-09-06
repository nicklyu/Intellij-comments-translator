package com.nicklyu.translator.processors

import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.psi.PsiElement

interface CommentProcessor {
    fun process(element: PsiElement) : Array<FoldingDescriptor>
}