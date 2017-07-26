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

import sglicko2._

trait ComparisonOfRules { this: GamesRating =>

  lazy val leaderboards = Vector(
    ("Linear", boardWithRulesAndPeriods(Linear.rules, allGames)),
    ("GW2",    boardWithRulesAndPeriods(GW2.rules, allGames)),
    ("Basic",  boardWithRulesAndPeriods(EitherOnePlayerWinsOrItsADraw.rules, allGames.map(_ map winOrLose)))
  )

  def showComparisonOfRules(): Unit = {
    for {
      (method, board) <- leaderboards
    } {
      println
      println(s"Ratings for $nameOfTheGame using method $method:")
      println("--------------------------------------------------")
      printBoard(board)
    }
  }

}
