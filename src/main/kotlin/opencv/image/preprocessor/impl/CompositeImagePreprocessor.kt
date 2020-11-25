package opencv.image.preprocessor.impl

//class CompositeImagePreprocessor(private vararg val preprocessors: AbstractImagePreprocessor) :
//    AbstractImagePreprocessor() {
//    private fun results(matrix: Mat): List<Mat> =
//        preprocessors.runningFold(matrix) { acc, preprocessor -> preprocessor(acc) }
//
//    override fun invoke(matrix: Mat): Mat = results(matrix).last()
//}