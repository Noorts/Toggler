# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Changed

- Improved clarity of settings panel error messages.
- Improved performance.

## [1.5.0] - 2025-02-02

### Added

- Enhanced "no match found" notification with direct link to settings panel.

### Changed

- Slightly reworked notifications.
- Increased clarity of settings panel button tooltips.
- Reworked plugin description.

## [1.4.1] - 2024-12-01

### Fixed

- Addressed a deprecation ([#80](https://github.com/Noorts/Toggler/issues/80)).

## [1.4.0] - 2024-05-12

### Changed

- This update includes internal version bumps to stay compatible with the newest IDE versions.
- The minimum IDE version has been bumped from 2021.1.3 to 2022.3.1. The plugin is now compatible with any IDE
versions from 2022.3.1 onwards.

### Fixed

- Addressed a deprecation ([#64](https://github.com/Noorts/Toggler/issues/64)).

## [1.3.1] - 2024-02-01

### Changed

- The plugin is now automatically compatible with any IDE versions from 2021.1 onwards.

## [1.3.0] - 2024-01-31

### Added

- The reverse toggle action has been added! Instead of forwards (e.g., 1, 2, 3, 1), it is now also possible to toggle backwards with the reverse toggle action (e.g., 1, 3, 2, 1). By default the new action is bound to `Ctrl/Cmd + Shift + Alt/Opt + X`. The keybind can be changed under `Settings/Preferences -> Keymap -> Plug-ins -> Toggler`.

### Changed

- Update internal plugins.
- Plugin description updated.

## [1.2.14] - 2023-11-12

### Changed

- Extend plugin IDE compatibility range from 2023.2 to 2023.3.
- Update internal plugins.

## [1.2.13] - 2023-06-10

### Changed

- Extend plugin IDE compatibility range from 2023.1 to 2023.2.
- Update internal plugins.

## [1.2.12] - 2023-02-26

### Changed

- Extend plugin IDE compatibility range to 2021.1 to 2023.1.
- Upgrade Gradle to 7.6.1.
- Update internal plugins.

## [1.2.11] - 2022-11-14

### Changed

- Extend plugin IDE compatibility range from 2022.2 to 2022.3.
- Update plugin description.

## [1.2.10] - 2022-11-12

### Added

- Add limited support for tabs as whitespace ([#20](https://github.com/Noorts/Toggler/issues/20)).

## [1.2.9] - 2022-08-21

### Changed

- Update plugin description.
- Upgrade Gradle to 7.5.1.
- Expand integration tests.
- Improve internal organization.

## [1.2.8] - 2022-08-20

### Fixed

- Fix Toggler configuration menu bug, which caused the menu to not show up due to incompatibility with external plugins ([#5](https://github.com/Noorts/Toggler/issues/5)).

## [1.2.7] - 2022-07-19

### Changed

- Extend plugin IDE compatibility range from 2022.1 to 2022.2.

## [1.2.6] - 2022-07-17

### Changed

- Extend plugin IDE compatibility range from 2021.3 to 2022.1.
- Upgrade Gradle to 7.5 and migrate build configs.
- Expand tests.

## [1.2.5] - 2022-02-13

### Changed

- Improve plugin description.
- Add alternatives section to plugin description.

## [1.2.4] - 2021-12-29

### Added

- Add confirmation dialog to the "Reset to Defaults" button in the configuration menu.

### Changed

- Replace console system errors with error notifications.
- Improve code quality.

## [1.2.3] - 2021-09-11

### Fixed

- Removed deprecated API methods. This improves the plugin its compatibility for future IDE versions.
- Fixed a JsonParser bug that prevented the number 4 from being added to a toggle.

## [1.2.2] - 2021-08-02

### Changed

- The position of the Toggler action in the IDE menu has been adjusted.
- Handling of error when no text can be selected has been improved.
- The no match found notification has been changed slightly.

### Fixed

- Bug caused by folded text in the editor has been fixed.

## [1.2.1] - 2021-07-08

### Changed

- The plugin is now compatible with IDE builds from 2020.3 and onwards. Under the hood the notification system was updated which improves compatibility with future versions of the JetBrains products. This also means that older IDE versions (prior to 2020.3/203.4341) are no longer supported as of this update.

## [1.2.0] - 2021-07-07

### Added

- The partial matching functionality has finally been added! It is now possible to toggle a partial match such as 'add' inside of 'addClass'. The new functionality is enabled by default and can be disabled in the configuration.

### Changed

- Unit tests were added to improve the development of Toggler.
- Plugin description updated.

## [1.1.2] - 2021-04-15

### Changed

- Additional boundary characters have been added. They are now set to the following: ```' ', ';', ':', '.', ',', '`', '"', ''', '(', ')', '[', ']', '{', '}'```.

## [1.1.1] - 2021-03-17

### Added

- Boundary characters will now be detected inside of the configuration JSON to prevent invalid toggles.

### Changed

- Default spacing inside of the configuration JSON is now set to a tab instead of 4 spaces to improve the configuration experience.

### Fixed

- Words inside of apostrophes will now be detected correctly.

## [1.1.0] - 2021-02-26

### Added

- It is now possible to toggle multiple carets that are on the same line.

### Changed

- Total rewrite of the word/symbol selection system to allow for better selection of symbols. Note: the system hasn't been tested extensively yet. Use at your own risk.
- Plugin description updated.
- Notification clarity improved.

## [1.0.1] - 2021-02-25

### Changed

- Plugin description updated.
- Plugin now compatible with builds from 2019.1 until 2021.2.
- Miscellaneous developer tweaks.

## [1.0.0] - 2021-02-22

### Added

- Initial version.

[unreleased]: https://github.com/Noorts/Toggler/compare/v1.5.0...HEAD
[1.5.0]: https://github.com/Noorts/Toggler/compare/v1.4.1...v1.5.0
[1.4.1]: https://github.com/Noorts/Toggler/compare/v1.4.0...v1.4.1
[1.4.0]: https://github.com/Noorts/Toggler/compare/v1.3.1...v1.4.0
[1.3.1]: https://github.com/Noorts/Toggler/compare/v1.3.0...v1.3.1
[1.3.0]: https://github.com/Noorts/Toggler/compare/v1.2.14...v1.3.0
[1.2.14]: https://github.com/Noorts/Toggler/compare/v1.2.13...v1.2.14
[1.2.13]: https://github.com/Noorts/Toggler/compare/v1.2.12...v1.2.13
[1.2.12]: https://github.com/Noorts/Toggler/compare/v1.2.11...v1.2.12
[1.2.11]: https://github.com/Noorts/Toggler/compare/v1.2.10...v1.2.11
[1.2.10]: https://github.com/Noorts/Toggler/compare/v1.2.9...v1.2.10
[1.2.9]: https://github.com/Noorts/Toggler/compare/v1.2.8...v1.2.9
[1.2.8]: https://github.com/Noorts/Toggler/compare/v1.2.7...v1.2.8
[1.2.7]: https://github.com/Noorts/Toggler/compare/v1.2.6...v1.2.7
[1.2.6]: https://github.com/Noorts/Toggler/compare/v1.2.5...v1.2.6
[1.2.5]: https://github.com/Noorts/Toggler/compare/v1.2.4...v1.2.5
[1.2.4]: https://github.com/Noorts/Toggler/compare/v1.2.3...v1.2.4
[1.2.3]: https://github.com/Noorts/Toggler/compare/v1.2.2...v1.2.3
[1.2.2]: https://github.com/Noorts/Toggler/compare/v1.2.1...v1.2.2
[1.2.1]: https://github.com/Noorts/Toggler/compare/v1.2.0...v1.2.1
[1.2.0]: https://github.com/Noorts/Toggler/compare/v1.1.2...v1.2.0
[1.1.2]: https://github.com/Noorts/Toggler/compare/v1.1.1...v1.1.2
[1.1.1]: https://github.com/Noorts/Toggler/compare/v1.1.0...v1.1.1
[1.1.0]: https://github.com/Noorts/Toggler/compare/v1.0.1...v1.1.0
[1.0.1]: https://github.com/Noorts/Toggler/compare/v1.0.0...v1.0.1
[1.0.0]: https://github.com/Noorts/Toggler/releases/tag/v1.0.0
