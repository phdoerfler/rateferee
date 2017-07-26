# Rateferee

Challenge your friends to a match or two, enter the results and frolick upon the graphical display of their skill estimation.

## Screenshot

![App](https://github.com/phdoerfler/rateferee/blob/gh-pages/app.png)

## What it does

This app reads the game results from a config file, calculates a rating for the players and displays it.
The rating system used is [glicko2](https://en.wikipedia.org/wiki/Glicko_rating_system).
The app then further calculates how the player's ratings have evolved over time and displays them along with their rating deviation in a chart.
Additionally the ratings are calculated using 3 different scoring rules and the result is shown in the terminal.

You can customize the type of game and what the maximum score is.
For instance, in foosball one typically has a maximum score of 10:

```hocon
{
  name: "Foosball",
  score-max: 10,
  games: [
    [ // 6th of July
      { a: "John",  b: "Ted",   pa: 10, pb: 7 },
      { a: "John",  b: "Ted",   pa: 10, pb: 2 },
      { a: "John",  b: "Ted",   pa: 10, pb: 9 }
    ]
  ]
}
```

A full example is provided in [example.conf](example.conf).

## Build and Run

Build via SBT:

`sbt pack`

Then run like this:

`target/pack/bin/rateferee example.conf`

If you like, install via

`cd target/pack && make install`

Then you can run it via

`~/bin/rateferee example.conf`

## Special Thanks

- to [asflierl](https://github.com/asflierl) for providing [sglicko2](https://github.com/asflierl/sglicko2),
- to [jmhofer](https://github.com/jmhofer) for coming up with the name *Rateferee*,
- to [wookietreiber](https://github.com/wookietreiber) for providing [scala-chart](https://github.com/wookietreiber/scala-chart),
- to [jfree](https://github.com/jfree) for providing [jfreechart](https://github.com/jfree/jfreechart) and
- to [etorreborre](https://github.com/etorreborre) for providing [specs2](https://github.com/etorreborre/specs2).
