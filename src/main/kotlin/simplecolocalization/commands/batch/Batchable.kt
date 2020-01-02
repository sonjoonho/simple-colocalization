package simplecolocalization.commands.batch

import ij.ImagePlus
import java.io.File
import simplecolocalization.preprocessing.PreprocessingParameters

interface Batchable {
    fun process(inputImages: List<ImagePlus>, outputFormat: String, outputFile: File, preprocessingParameters: PreprocessingParameters)
}
