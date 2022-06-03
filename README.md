# FriendlyMonsters

Library for spawning friendly monsters in Modded Slay the Spire.

This is a fork/complete rewrite of [Kobting/STSFriendlyMinions](https://github.com/Kobting/STSFriendlyMinions), aiming
to fix all the issues, improve code quality and add important new features into the library.

# Features

**Comparison with Kobting/STSFriendlyMinions**

| Feature                                                        | Kobting/STSFriendlyMinions |       hlysine/FriendlyMonsters       |
|----------------------------------------------------------------|:--------------------------:|:------------------------------------:|
| Add minions                                                    |             ✅              |                  ✅                   |
| Select minion moves                                            |             ✅              |                  ✅                   |
| Configure max minions count                                    |             ✅              |                  ✅                   |
| Redirect powers to minions                                     | ✅<br/>(Configure globally) | ✅<br/>(Configure for each character) |
| Redirect enemy attack intents to minions                       | ✅<br/>(Configure globally) | ✅<br/>(Configure for each character) |
| Store configuration in save file †                             |             ❌              |                  ✅                   |
| Animated minions                                               |             ❌              |                  ✅                   |
| Minion death animation (when player dies)                      |             ❌              |                  ✅                   |
| Smooth minion movement                                         |             ❌              |                  ✅                   |
| Minion triggers                                                |     ✅<br/>(7 triggers)     |         ✅<br/>(12 triggers)          |
| Unified API between base game characters and custom characters |             ❌              |                  ✅                   |
| Sample monster card                                            |             ✅              |                 ❌ ‡                  |
| Documentation                                                  |             ❌              |                  ✅                   |

† Configurations include max minion count, the chance for enemy attacks to target minions, and the chance for powers to affect
minions. Their default values are stored in the save file, but they can still be temporarily modified in each combat.

‡ The sample monster card has been removed to not confuse players with a useless card.

**Issues fixed**

- Calling `AbstractFriendlyMonster.die()` in `takeTurn` crashes due to concurrent modification.
- Power redirect not working properly.
- Minion logic not matching between `AbstractPlayerWithMinions` and base game characters.
- Unoptimized reflection calls.
- Outdated STS, MTS and Basemod versions.