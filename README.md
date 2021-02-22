# Toggler

Toggler is a Jetbrains IDE Plugin that allows you to quickly toggle words and symbols with a hotkey. 
Toggles can be configured from the settingsmenu.


# Features

- Customizable through the settings menu.
- Support for multiple cursors.
- Support for numerous toggles.
- Limited support for transferring word case.
- Easy importing and exporting of the settings configuration.
- No external dependencies required.

[comment]: <> (# Installation)


# Usage
Select or place your cursor on a word/symbol and press the default hotkey
(<code>Ctrl+Shift+X</code> on Windows or <code>Cmd+Shift+X</code> on MacOS) to
toggle the word/symbol to the next toggle defined in the configuration file.<br>
The toggle action can also be found as <code>Toggle Word/Symbol</code> in the <code>Edit</code> menu.

# Roadmap

- Improve performance (optimise data structure).
- Create Jetbrains "native" UI.
- Improve transferring word case.
- Improve JsonParser error handling.

# Acknowledgements
This plugin has drawn a lot of inspiration from <a href="https://github.com/HiDeoo">HiDeoo</a> his
original versions of toggle plugins/extensions
for <a href="https://marketplace.visualstudio.com/items?itemName=hideoo.toggler">VSCode</a>
and <a href="https://atom.io/packages/toggler">Atom</a>.

# License

Toggler is licensed under the [MIT license](LICENSE.md).