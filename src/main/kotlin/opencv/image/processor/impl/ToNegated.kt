package opencv.image.processor.impl

import opencv.image.processor.api.AbstractImageProcessor
import org.opencv.core.Core

val ToNegated: AbstractImageProcessor = { matrix ->
    val result = matrix.clone()
    Core.bitwise_not(result, result)
    result
}