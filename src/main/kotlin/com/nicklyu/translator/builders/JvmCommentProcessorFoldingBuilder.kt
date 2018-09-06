package com.nicklyu.translator.builders

import com.nicklyu.translator.processors.CommentProcessor
import com.nicklyu.translator.processors.JavadocCommentProcessor
import com.nicklyu.translator.processors.SingleLineCommentProcessor

class JvmCommentProcessorFoldingBuilder : CommentTranslatorFoldingBuilder() {
    override fun getProcessors(): Array<CommentProcessor> = arrayOf(
            JavadocCommentProcessor,
            SingleLineCommentProcessor
    )
}