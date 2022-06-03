# Changelog

## [1.0.0] - First Release - 2022-06-03
### Cards
- Removed all template card contents

### Player
- Moved all logic from AbstractPlayerWithMinions to patches
- Removed CustomCharSelectInfo
- Improved code quality in MinionMove and MinionMoveGroup

### Minions
- Added animation support for Spine/G3DJ/Spriter animations
- Added animated movement with target X/Y
- Fixed a bug where calling `die()` in `takeTurn()` throws concurrent modification exception

### Patches
- Rewrote all patches to improve code quality
- Re-enabled ApplyPowerActionPatch with fixed logic
- Fixed a bug in minion attack intent damage logic when the minion is already dead
- Renamed patch fields to avoid confusion
- Added saving for base configurations of some fields (max minion count, attack intent and power chances)
- Forward more triggers to minions
  - onVictory
  - applyPowers
  - onAfterUseCard
  - onUseCard

### Misc
- Renamed FriendlyMinions to FriendlyMonsters to avoid name conflict
- Added documentation for most public methods and fields