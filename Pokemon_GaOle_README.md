# Pokémon Ga-Olé!

A turn-based Pokémon battle-and-catch game for the terminal, written in Java. Sign up as a trainer, pick a stage, battle wild Pokémon with the help of two quick mini-games, then throw Poké Balls to grow your collection and climb the leaderboard.

## Features

- **Trainer accounts:** sign up to get a Player ID (`P01`, `P02`, …) and log back in whenever you like. Your score and Pokémon are saved to `player.txt`.
- **36 Pokémon:** six types (Fire, Water, Grass, Ground, Rock and Ice) and three grades, loaded from `pokemon.csv`.
- **Type effectiveness:** every attack is scaled by a 6×6 type chart.
- **Three stages:** Forest, Mountain and Ocean each make two of the types more likely to appear.
- **Two Swing mini-games:** *Attack Gauge* decides who strikes first, and *Spirit Boost* turns button-mashing into bonus damage.
- **Catching:** win a battle to draw a random Poké Ball and try to catch the wild Pokémon.
- **Leaderboard:** the top five trainers by total score are shown after every game.

## Requirements

- **JDK 21 or newer.** The code uses qualified enum constants in `switch` cases, which need Java 21. The Eclipse project is set to Java 22.
- **A desktop with a display.** The mini-games open Swing windows, so the game can't run headless (for example over plain SSH).
- **A terminal with UTF-8 and ANSI support** (macOS Terminal, Windows Terminal and most Linux terminals) so the bold text and ASCII art render correctly.

## Getting started

Clone or download this repository and open a terminal in the `Pokemon` folder. The game loads `pokemon.csv` and `player.txt` from the current directory, so it has to be started from inside `Pokemon/`.

```bash
cd Pokemon
javac -d bin -sourcepath src src/Main.java   # compile
java -cp bin Main                             # run
```

These commands are the same on macOS, Linux and Windows.

**Eclipse:** use *File → Import → Existing Projects into Workspace*, pick the `Pokemon` folder, then right-click `src/Main.java` and choose *Run As → Java Application*. The project targets Java 22, so adjust the JRE and compiler level under *Project → Properties* if you have a different JDK. Eclipse's console may show the bold ANSI codes as stray characters, so a terminal gives the best display.

## How to play

1. **Main menu:** choose *Sign up* (pick a one-word username and note the Player ID you're given) or *Login* with an existing ID.
2. **Pick a stage:** Forest, Mountain or Ocean.
3. **Catch a starter:** three wild Pokémon appear. Choose one and it joins your inventory.
4. **Choose your team:** select up to two Pokémon from your inventory. If you only own one, the game rents you a Charizard as a teammate.
5. **Battle two wild Pokémon:**
   - *Attack Gauge:* mash **SMASH!** to fill your gauge before the opponent's reaches 100%. Whoever gets there first attacks first, then rounds alternate.
   - *Your round:* each of your Pokémon picks a target, then you get 5 seconds to mash the **Spirit Boost** button. Every click adds bonus damage.
   - *Foe's round:* each wild Pokémon attacks a random member of your team.
6. **Catch them:** if you win, draw a random Poké Ball and try to catch each wild Pokémon.
7. **Save:** your progress is written to `player.txt`, the leaderboard is shown, and you return to the main menu.

## Game mechanics

### Damage

```text
damage = floor((ATK × move power ÷ DEF) × type multiplier + spirit)
```

`ATK × move power ÷ DEF` is integer division. *Spirit* is the number of Spirit Boost clicks for your Pokémon, and a random value from 10 to 29 for wild Pokémon. A Pokémon faints when its HP reaches 0, and the battle ends when one side has no Pokémon left.

### Type effectiveness

The multiplier applied when a move of the row's type hits a Pokémon of the column's type:

| Move type \ Target type | Fire | Water | Grass | Ground | Rock | Ice |
|---|---|---|---|---|---|---|
| **Fire** | 1× | ½× | 2× | 1× | ½× | 2× |
| **Water** | 2× | ½× | ½× | 2× | 2× | 1× |
| **Grass** | ½× | 2× | ½× | 2× | 2× | 1× |
| **Ground** | 2× | 1× | ½× | 1× | 2× | 1× |
| **Rock** | 2× | 1× | 1× | ½× | 1× | 2× |
| **Ice** | ½× | ½× | 2× | 2× | 1× | ½× |

### Defense boosts

From round 2 onwards, the side that is about to be attacked gets a chance to harden up:

- **You:** *Defense Roulette.* Flip up to two coins; two heads in a row gives your team +10% DEF.
- **Wild Pokémon:** a 1-in-4 chance of +10% DEF.

### Scoring

You earn **5 / 10 / 20 points** for every grade 1 / 2 / 3 Pokémon you defeat, and the same again for every one you catch. Your total score is saved and used for the leaderboard.

### Catching

After a victory you draw one ball at random and throw it at each wild Pokémon in turn. The chance of success is `min(1, catch rate ÷ (grade × 3))`, and the Master Ball never fails.

| Ball | Draw chance | vs. Grade 1 | vs. Grade 2 | vs. Grade 3 |
|---|---|---|---|---|
| Poké Ball | 60% | 33% | 17% | 11% |
| Great Ball | 30% | 67% | 33% | 22% |
| Ultra Ball | 9% | 100% | 50% | 33% |
| Master Ball | 1% | 100% | 100% | 100% |

### Stages

Each stage makes grade 1–2 Pokémon of its two types twice as likely to appear:

| Stage | Boosted types |
|---|---|
| Forest | Grass, Rock |
| Mountain | Fire, Ground |
| Ocean | Water, Ice |

Grade 3 Pokémon never appear as starters, but they can show up as wild opponents.

## Data files

**`pokemon.csv`** holds one Pokémon per line. The header row is skipped, and every data line's ID starts with `#`.

| Column | Meaning |
|---|---|
| `id` | Unique ID, for example `#010` |
| `name` | Pokémon name |
| `type` | `FIRE`, `WATER`, `GRASS`, `GROUND`, `ROCK` or `ICE` |
| `hp`, `atk`, `def` | Base stats |
| `grade` | 1 to 3. Higher grades are stronger, worth more points and harder to catch |
| `move_name`, `move_type`, `move_damage` | The Pokémon's single move, its type and its power |

**`player.txt`** holds one block per trainer, separated by a blank line: a header line `id,name,totalScore`, then one Pokémon ID per line for the inventory.

```text
P01,Trainer,135
#007
#001
```

To start with a clean leaderboard, empty `player.txt`.

## Project structure

```text
.
├── README.md
└── Pokemon/
    ├── .classpath, .project, .settings/   # Eclipse project files
    ├── pokemon.csv                        # Pokémon roster (36 entries)
    ├── player.txt                         # Saved trainers and their inventories
    └── src/
        ├── Main.java                      # Entry point
        ├── core/                          # Game loop, Menu, Battle, Player, DataBase (file I/O)
        ├── pokemon/                       # Pokemon, six type subclasses, Move, Type enum
        ├── pokeball/                      # PokeBall + Great / Ultra / Master Ball
        ├── stage/                         # Stage + Forest / Mountain / Ocean
        └── ui/                            # Swing mini-games: AttackGaugeUI, SpiritBoostUI
```

## Object-oriented design

- **Inheritance:** `FirePokemon`, `WaterPokemon`, `GrassPokemon`, `GroundPokemon`, `RockPokemon` and `IcePokemon` extend `Pokemon`; `GreatBall`, `UltraBall` and `MasterBall` extend `PokeBall`; `ForestStage`, `MountainStage` and `OceanStage` extend `Stage`.
- **Polymorphism:** each stage overrides `getSpawnablePokemon()` to reshape its spawn pool, and `Game` works through a `Stage` or `PokeBall` reference without caring which subclass it holds.
- **Encapsulation:** fields are private and reached through getters and setters, and `Player` validates score updates.
- **Overloading:** `PokeBall` and `Player` each provide more than one constructor.
- **Composition:** a `Pokemon` has a `Move`, a `Player` owns an inventory of `Pokemon`, and `Game` coordinates the `Menu`, `Stage`, `Battle` and `Player`.
- **Enums:** `Type` holds the type-effectiveness chart behind a single `getEffectiveness` lookup.

## Disclaimer

Pokémon and all related names are trademarks of Nintendo, Creatures Inc. and GAME FREAK inc. This is an unofficial fan project made for learning and is not affiliated with or endorsed by them.
