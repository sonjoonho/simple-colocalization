package simplecolocalization.services.colocalizer

import ij.ImagePlus
import ij.gui.Overlay
import ij.gui.PolygonRoi
import ij.gui.Roi

/**
 * The representation of a positioned cell is a set of points on a
 * two-dimensional coordinate system belonging which form the cell.
 */
class PositionedCell(val points: Set<Pair<Int, Int>>, val outline: Set<Pair<Int, Int>>? = null) {

    val center: Pair<Double, Double>

    init {
        var xSum = 0.0
        var ySum = 0.0
        points.forEach { point ->
            xSum += point.first
            ySum += point.second
        }
        center = Pair(xSum / points.size, ySum / points.size)
    }

    fun getMeanIntensity(grayScaleImage: ImagePlus): Float {
        // ImagePlus.getPixel returns size 4 array
        // for grayscale, intensity will be at index 0
        return points.map { grayScaleImage.getPixel(it.first, it.second)[0].toFloat() }.sum() / points.size
    }

    companion object {
        fun fromRoi(roi: Roi): PositionedCell {
            return PositionedCell(
                roi.containedPoints.map { Pair(it.x, it.y) }.toSet(),
                (roi.floatPolygon.xpoints.map { it.toInt() } zip roi.floatPolygon.ypoints.map { it.toInt() }).toSet()
            )
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PositionedCell

        if (points != other.points) return false

        return true
    }

    override fun hashCode(): Int {
        return points.hashCode()
    }

    fun toRoi(): Roi {
        if (outline == null) {
            throw RuntimeException("Cannot convert PositionedCell to ImageJ ROI: no cell outline provided.")
        }

        return PolygonRoi(
            outline.map { it.first }.toIntArray(),
            outline.map { it.second }.toIntArray(),
            outline.size,
            Roi.TRACED_ROI
        )
    }
}

fun showCells(imp: ImagePlus, cells: List<PositionedCell>) {
    val rois: Collection<Roi> = cells.map { it.toRoi() }
    val overlay = Overlay()
    rois.forEach { overlay.add(it) }
    val ic = imp.canvas
    if (ic == null) {
        imp.overlay = overlay
        return
    }
    ic.showAllList = overlay
    imp.draw()
}
