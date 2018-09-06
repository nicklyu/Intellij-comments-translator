package com.nicklyu.translator.builders

import com.nicklyu.translator.processors.CommentProcessor
import com.nicklyu.translator.processors.jvm.java.JavadocCommentProcessor
import com.nicklyu.translator.processors.jvm.java.JavaSingleLineCommentProcessor

class JvmCommentProcessorFoldingBuilder : CommentTranslatorFoldingBuilder() {
    override fun getProcessors(): Array<CommentProcessor> = arrayOf(
            JavadocCommentProcessor,
            JavaSingleLineCommentProcessor
    )
}