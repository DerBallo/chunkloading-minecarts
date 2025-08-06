# Changelog

## [1.0.2](https://github.com/DerBallo/chunkloading-minecarts) (2025-08-07)

### Changes:
* Activating and deactivating a chunkloading minecart will no longer change it's name tag.

## [1.0.1](https://github.com/DerBallo/chunkloading-minecarts) (2025-07-28)

### Several small performance improvements, visual changes and code cleanups

### Changes:
* Removed the mixin that globally runs for every dispenser activation. Instead, the required logic is only run when either a glowstone or amethyst shard is about to be dispensed from the inventory.
* Removed several unnecessary variables, comments and functions.
* Changed the particle type, which chunkloading minecarts emit, to the nether portal effect.
* Adjusted the position, spread and velocity of the particles.
* Increased particle spawn rate for better visibility.


## [1.0.0](https://github.com/DerBallo/chunkloading-minecarts) (2025-06-14)

### Complete rewrite of the codebase

### Changes:
* Now using a custom chunk loading ticket type.
* Added a gamerule to control the radius of chunks a chunkloader-minecart will load around itself.
* Chunkloading is now exclusively controlled by the minecart itself instead of a class which keeps track of all the chunkloaders.
* Chunkloading minecarts will now stop loading chunks on server restarts/crashes until they are manually loaded again.
* Chunk loading tickets will expire 5 ticks after their creation and will only be refreshed once expired.
* MASSIVE performance improvements.

### Bug Fixes
* The mod will now no longer remove any forceloaded chunks, including spawn chunks.


## 0.3.2

### Bug Fixes

* fixed this bug: (https://github.com/scriptcoded/scripts-chunk-loaders/pull/35)

### Features

* **version:** update to 1.21.4


## [0.3.1](https://github.com/scriptcoded/scripts-chunk-loaders/compare/v0.3.0...v0.3.1) (2024-06-21)

### Bug Fixes

* use Java 21 for releases ([cb1ce9d](https://github.com/scriptcoded/scripts-chunk-loaders/commit/cb1ce9dc46d318eef45b5811629e8567fd88e101))


## [0.3.0](https://github.com/scriptcoded/scripts-chunk-loaders/compare/v0.2.4...v0.3.0) (2024-06-21)

### Features

* update to 1.21 ([#22](https://github.com/scriptcoded/scripts-chunk-loaders/issues/22)) ([ddbe1d4](https://github.com/scriptcoded/scripts-chunk-loaders/commit/ddbe1d448c161b55967b73de184700fa39f8d262))


## [0.2.4](https://github.com/scriptcoded/scripts-chunk-loaders/compare/v0.2.3...v0.2.4) (2024-03-02)

### Bug Fixes

* **version:** update to Minecraft 1.20.4 ([19a3c69](https://github.com/scriptcoded/scripts-chunk-loaders/commit/19a3c69f1d76960acd450703e79d0be7dd890251))


### Documentation

* add readme ([4548036](https://github.com/scriptcoded/scripts-chunk-loaders/commit/45480366082e72ac082e866a103aad06b2c6121a))


## [0.2.3](https://github.com/scriptcoded/scripts-chunk-loaders/compare/v0.2.2...v0.2.3) (2024-03-02)

### Bug Fixes

* disable chunk loader if already a chunk loader ([11c655a](https://github.com/scriptcoded/scripts-chunk-loaders/commit/11c655ae4a3f60618ddf1e156e0d1548c5802d1a))
* **version:** update to Minecraft 1.20.3 ([16c4040](https://github.com/scriptcoded/scripts-chunk-loaders/commit/16c40407118483837946b74338de848026c1ba11))


## [0.2.2](https://github.com/scriptcoded/scripts-chunk-loaders/compare/v0.2.1...v0.2.2) (2024-03-02)

### Bug Fixes

* auto naming fixes ([f2e2357](https://github.com/scriptcoded/scripts-chunk-loaders/commit/f2e2357035e0044b4fdced921807c2954756ee0e))
* **version:** update to Minecraft 1.20.2 ([d524346](https://github.com/scriptcoded/scripts-chunk-loaders/commit/d524346c19b9c8cca5b46d11ebc1f4a2e33c8e4a))


## [0.2.1](https://github.com/scriptcoded/scripts-chunk-loaders/compare/v0.2.0...v0.2.1) (2024-02-27)

### Bug Fixes

* cleanup and make mod run in singleplayer ([5c1e1ce](https://github.com/scriptcoded/scripts-chunk-loaders/commit/5c1e1ce6dc81c7abac513301f4be6ab555a05ee0))
* **version:** Update to Minecraft 1.20.1 ([192702d](https://github.com/scriptcoded/scripts-chunk-loaders/commit/192702d300f4508a7549476a9a386fb3d82131b8))


## [0.2.0](https://github.com/scriptcoded/scripts-chunk-loaders/compare/v0.1.1...v0.2.0) (2023-06-10)

### Features

* update to 1.20 ([5b25f6b](https://github.com/scriptcoded/scripts-chunk-loaders/commit/5b25f6b2cb366437234bb28d37383f172348b626))


## [0.1.1](https://github.com/scriptcoded/scripts-chunk-loaders/compare/v0.1.0...v0.1.1) (2023-06-10)

### Miscellaneous Chores

* release 0.1.1 ([c057d6e](https://github.com/scriptcoded/scripts-chunk-loaders/commit/c057d6eeb68813314db825b70c1ef3162261d008))


## [0.1.0](https://github.com/scriptcoded/scripts-chunk-loaders/compare/v0.0.1...v0.1.0) (2023-06-10)

### Bug Fixes

* add icon ([fc126d7](https://github.com/scriptcoded/scripts-chunk-loaders/commit/fc126d746d95fbcfe02122e9aee60af7334d318f))


### Miscellaneous Chores

* release 0.1.0 ([f528bd1](https://github.com/scriptcoded/scripts-chunk-loaders/commit/f528bd153195a138a34b3238ff122addbe947610))


## 0.0.1 (2023-06-10)

### Features

* initial commit ([53330d8](https://github.com/scriptcoded/scripts-chunk-loaders/commit/53330d84c999d8702adec61ff5b088fd7fc33ce9))