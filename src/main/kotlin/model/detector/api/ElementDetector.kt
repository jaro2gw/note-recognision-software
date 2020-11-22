package model.detector.api

import model.element.api.AbstractElement
import org.opencv.core.Mat

interface ElementDetector<T : AbstractElement> : (Mat) -> Collection<T>