<h1 align="center">
  <br>
  <a href="https://github.com/Noorts/Toggler"><img src="src/main/resources/META-INF/pluginIcon_dark.svg" width="120" height="120" alt="Toggler"></a>
  <br>
  Toggler
  <br>
</h1>
<h4 align="center">
  Toggler is a Jetbrains IDE Plugin that allows you to quickly toggle words and symbols with a hotkey. 
Toggles can be configured from the settings menu. 
</h4>
<p align="center">
  <a href="https://plugins.jetbrains.com/plugin/16166-toggler"><img src="https://img.shields.io/jetbrains/plugin/d/16166" alt="Plugin downloads"></a>
  <a href="https://plugins.jetbrains.com/plugin/16166-toggler"><img src="https://img.shields.io/jetbrains/plugin/v/16166" alt="Plugin version"></a>
</p>
<br>

![](TogglerUsage.gif)

## Features

- Customizable through the settings menu.
- Support for multiple cursors.
- Support for numerous toggles.
- Limited support for transferring word case.
- Easy importing and exporting of the settings configuration.
- No external dependencies required.

## Usage
Select or place your cursor on a word/symbol and press the default hotkey
(<code>Ctrl+Shift+X</code> on Windows or <code>Cmd+Shift+X</code> on MacOS) to
toggle the word/symbol to the next toggle defined in the configuration file.
The toggle action can also be found as <code>Toggle Word/Symbol</code> in the <code>Edit</code> menu.

## Installation
There are four ways to install Toggler:
- Install directly from the plugin marketplace in your Jetbrains product. Go to <code>Settings/Preferences -> Plugins -> Marketplace</code> and search for <code>Toggler</code>.
- Install from the [Jetbrains marketplace](https://plugins.jetbrains.com/plugin/16166-toggler).
- Download the jar from a [release](https://github.com/Noorts/Toggler/releases) and install manually. 
Go to the <code>Settings/Preferences -> Plugins</code> menu, press the cog in the top right and choose <code>Install Plugin from Disk...</code>.
- Download this repo, build the plugin and install it manually.

## Roadmap

- Improve performance (optimise data structure).
- Create Jetbrains "native" UI.
- Improve transferring word case.
- Improve JsonParser error handling.

## Acknowledgements
This plugin has drawn a lot of inspiration from <a href="https://github.com/HiDeoo">HiDeoo</a> his
original versions of toggle plugins/extensions
for <a href="https://marketplace.visualstudio.com/items?itemName=hideoo.toggler">VSCode</a>
and <a href="https://atom.io/packages/toggler">Atom</a>.

## License

Toggler is licensed under the [MIT license](LICENSE.md).
