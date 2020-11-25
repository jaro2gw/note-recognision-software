package model.detector.api

import model.element.api.AbstractElement
import org.opencv.core.Mat

abstract class AbstractDetector<T : AbstractElement> : (Mat) -> Collection<T>