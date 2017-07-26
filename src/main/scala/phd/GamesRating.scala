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
import EitherOnePlayerWinsOrItsADraw._

import scala.annotation.tailrec
import Games._

class GamesRating(gamesCfg: GamesCfg) extends scalax.chart.module.Charting with ScoringMethods {
  val allGames = gamesCfg.games.map(_.map(g => (g.a, g.b, (g.pa, g.pb))))
  val gamesBinary = allGames.map(_ map winOrLose)

  val glicko2Basic = new Glicko2[Symbol, EitherOnePlayerWinsOrItsADraw]

  def nameOfTheGame = gamesCfg.name
  def maxScore = gamesCfg.scoreMax

  def boardWithRulesAndPeriods[P](rules: ScoringRules[P], games: Seq[Seq[(ID, ID, P)]]): Leaderboard[ID] = {
    @tailrec
    def incorporateRatingPeriods(glicko: Glicko2[ID, P], board: Leaderboard[ID], ratingPeriods: List[RatingPeriod[ID, P]]): Leaderboard[ID] = ratingPeriods match {
      case Nil => board
      case x :: xs => incorporateRatingPeriods(glicko, glicko.updatedLeaderboard(board, x), xs)
    }

    val glicko = new Glicko2[ID, P](0.6d)(rules)
    val ratingPeriods = games.map(g => glicko.newRatingPeriod.withGames(g:_*)).to[List]
    incorporateRatingPeriods(glicko, glicko.newLeaderboard, ratingPeriods)
  }

  def printBoard(board: Leaderboard[ID]): Unit =
    board.rankedPlayers map formatPlayer foreach println

  def formatPlayer(p: RankedPlayer[ID]): String = {
    f"${p.rank}%1d. ${p.player.id}%-7s rated at ${p.player.rating}%2.0f ± ${p.player.deviation * 2.0}%3.0f (2 × RD), σ = ${p.player.volatility * 100.0}%2.2f%%"
  }
}