package simplecolocalization.algorithms

import fiji.threshold.Auto_Local_Threshold
import ij.ImagePlus

/** This Module contains the local thresholding algorithms that can be used in the preprocessing step. **/

val localThresholder = Auto_Local_Threshold()

fun otsu(image: ImagePlus, radius: Int) {
    localThresholder.exec(image, "Otsu", radius, 0.0, 0.0, true)
}

fun bernsen(image: ImagePlus, radius: Int, contrastThreshold: Double) {
    localThresholder.exec(image, "Bernsen", radius, contrastThreshold, 0.0, true)
}

fun niblack(image: ImagePlus, radius: Int, kValue: Double, cValue: Double) {
    localThresholder.exec(image, "Niblack", radius, kValue, cValue, true)
}
