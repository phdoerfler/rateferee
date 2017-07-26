/*
 * ISC License
 *
 * Copyright (c) 2017, Philipp Dörfler
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package phd

import java.awt.{BasicStroke, Color}

import sglicko2._
import org.jfree.chart.axis.NumberAxis
import org.jfree.chart.renderer.xy.DeviationRenderer

import scala.annotation.tailrec
import scalax.chart.XYChart

trait RatingsChart { this: GamesRating with Evolution =>

  lazy val data: Vector[(ID, (Int, Double, Double))] = for {
    (board, idx) <- history
    rp <- board.rankedPlayers
  } yield playerAndIdxAndDeviation(rp, idx)

  def playerAndIdxAndDeviation(rp: RankedPlayer[ID], idx: Int) =
    rp.player.id -> (idx, rp.player.rating, rp.player.deviation)

  lazy val series: Vector[(String, Vector[(Int, Double, Double)])] =
    data.groupBy(_._1).mapValues(_.map(_._2)).to[Vector].map(t => (t._1.toString, t._2))

  lazy val seriesWithRd = series.map { case (id, seriesData) => (id, seriesData.map {
    case (gameNr, rating, rd) => (gameNr, rating, rating - rd * 2, rating + rd * 2)})}

  lazy val domainMarks = allGames.map(_.size).init
  lazy val shiftedMarks = shiftMarks(domainMarks.to[List], List.empty).reverse

  @tailrec
  private def shiftMarks(lengths: List[Int], sums: List[Int]): List[Int] = lengths match {
    case Nil => sums
    case x :: xs => shiftMarks(xs, x + sums.headOption.getOrElse(0) :: sums)
  }

  lazy val chart = constructChart(seriesWithRd, shiftedMarks)

  def showChart(): Unit = chart.show()

  def constructChart[A: ToIntervalXYDataset](data: A, domainMarks: Seq[Int]) = {
    val dataset = ToIntervalXYDataset[A].convert(data)

    val domainAxis = new NumberAxis()
    domainAxis.setAutoRangeIncludesZero(false)

    val rangeAxis = new NumberAxis()

    val renderer = new DeviationRenderer(true, false)
    renderer.setAlpha(0.2f)

    val plot = new XYPlot(dataset, domainAxis, rangeAxis, renderer)
    domainMarks map { mark =>
      val m = new ValueMarker(mark)
      m.setPaint(Color.BLACK)
      m
    } foreach plot.addDomainMarker

    val chart = XYChart(plot, title = s"$nameOfTheGame Ratings", legend = true)(ChartTheme.Default)

    for {
      i <- 0 to dataset.getSeriesCount
    } {
      renderer.setSeriesFillPaint(i, renderer.lookupSeriesPaint(i))
      renderer.setSeriesStroke(i, new BasicStroke(4))
    }

    chart
  }
}
