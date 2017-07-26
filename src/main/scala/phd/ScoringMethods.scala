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

import sglicko2._, EitherOnePlayerWinsOrItsADraw._

import scala.math.{sin, Pi => π}

trait ScoringMethods { this: GamesRating =>
  val winOrLose: PartialFunction[(ID, ID, Result), (ID, ID, EitherOnePlayerWinsOrItsADraw)] = {
    case (a, b, p: (Int, Int)) =>
      if (p._1 > p._2) (a, b, Player1Wins)
      else if (p._1 < p._2) (a, b, Player2Wins)
      else (a, b, Draw)
  }

  trait Scoring {
    val scoreForTwoPlayers = (pair: Pairing) => Score(rateAVersusB(pair _1, pair _2))
    def rateAVersusB(a: Double, b: Double): Double
  }

  object Linear {
    val rules: ScoringRules[Pairing] = new ScoringRules[Pairing] with Scoring {
      def rateAVersusB(a: Double, b: Double) = (a - b) / maxScore / 2 + 0.5
    }
  }

  object GW2 { // As seen in the sglicko2 tests
    val rules: ScoringRules[Pairing] = new ScoringRules[Pairing] with Scoring {
      def rateAVersusB(a: Double, b: Double) = (sin((a / (a + b) - 0.5d) * π) + 1d) * 0.5d
    }
  }
}
