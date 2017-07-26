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

import java.nio.file.Paths

import phd.Games.GamesCfg
import pureconfig.error.ConfigReaderFailures
import pureconfig.loadConfig

object Rateferee extends App {
  if (args.isEmpty) System.err.println("One argument required: path to games config")
  else HandleConfigErrors(loadConfig[GamesCfg](Paths.get(args(0))))
}

object HandleConfigErrors {
  def apply(cfg: Either[ConfigReaderFailures, GamesCfg]): Unit = cfg match {
    case Left(failures) =>
      System.err.println("Error: \n" + failures.toList.mkString("\n"))
    case Right(conf) =>
      val rating = new GamesRating(conf) with Evolution with ComparisonOfRules with RatingsChart
      rating.showEvolution()
      rating.showPerformanceOfHistory()
      rating.showComparisonOfRules()
      rating.showChart()
  }
}