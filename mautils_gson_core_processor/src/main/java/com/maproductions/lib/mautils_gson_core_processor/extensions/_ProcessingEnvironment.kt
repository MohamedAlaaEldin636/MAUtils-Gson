package com.maproductions.lib.mautils_gson_core_processor.extensions

import javax.annotation.processing.ProcessingEnvironment
import javax.tools.Diagnostic

fun ProcessingEnvironment.warning(msg: String) {
    messager.printMessage(
        Diagnostic.Kind.WARNING,
        msg
    )
}
