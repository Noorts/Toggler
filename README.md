<h1 align="center">
  <br>
  <a href="https://github.com/Noorts/Toggler"><img src="src/main/resources/META-INF/pluginIcon_dark.svg" width="120" height="120" alt="Logo"></a>
  <br>
  Toggler
  <br>
</h1>
<h4 align="center">
  A JetBrains IDE plugin that allows you to quickly toggle words with a hotkey.<br>
  Toggles can be configured from the settings menu.
</h4>
<p align="center">
  <a href="https://plugins.jetbrains.com/plugin/16166-toggler"><img src="https://img.shields.io/jetbrains/plugin/d/16166" alt="Plugin downloads"></a>
  <a href="https://plugins.jetbrains.com/plugin/16166-toggler"><img src="https://img.shields.io/jetbrains/plugin/r/rating/16166" alt="Plugin rating"></a>
  <a href="https://plugins.jetbrains.com/plugin/16166-toggler"><img src="https://img.shields.io/jetbrains/plugin/v/16166" alt="Plugin version"></a>
</p>
<p align="center">
  <a href="#installation">Installation</a> •
  <a href="#usage">Usage</a> •
  <a href="#configuration">Configuration</a> •
  <a href="#roadmap">Roadmap</a> •
  <a href="#contribute">Contribute</a> •
  <a href="#alternatives">Alternatives</a> •
  <a href="#acknowledgements">Acknowledgements</a> •
  <a href="#license">License</a>
  <br><br>
  <img src="TogglerUsage.gif" alt="Toggler Usage Example">
</p>
<br>

Do you really like Toggler? Consider giving a star on [GitHub](https://github.com/Noorts/Toggler) or leaving a review on the [JetBrains marketplace](https://plugins.jetbrains.com/plugin/16166-toggler).

Did you find a bug or do you have a feature request? Feel free to open an issue on [GitHub](https://github.com/Noorts/Toggler/issues).

## Installation
- (Recommended) Install directly from the plugin marketplace in your JetBrains IDE.
  - Go to <kbd>Settings/Preferences -> Plugins -> Marketplace -> Search for "Toggler" -> Install Plugin</kbd>.
- Install manually
  - from the [JetBrains marketplace](https://plugins.jetbrains.com/plugin/16166-toggler).
  - from GitHub. Download the jar from a GitHub [release](https://github.com/Noorts/Toggler/releases).
  Then go to the <kbd>Settings/Preferences -> Plugins -> Press the ⚙️ (in the top right) -> Choose "Install Plugin from Disk..."</kbd>.
  - Download this repo, build the plugin and install it manually. Also see the ["Contribute"](#contribute) section.

## Usage
Select or place your cursor on a word and press:
- <kbd>Ctrl + Shift + X</kbd> (Windows and Linux)
- <kbd>Cmd + Shift + X</kbd> (macOS)

If the word matches one of the configured word combinations / toggles, then it will be replaced with the next configured word.

```
    true -> false
     !== -> ===
      && -> ||
    left -> right
    LEFT -> RIGHT
addClass -> removeClass
```

See the ["Configuration"](#configuration) section below to learn more about the standard word combinations / toggles and how to configure them.

### Advanced Usage
The configured toggles "wrap around", which means that if `["true", "false"]` is configured, then continually toggling will lead to `true -> false -> true -> ...`.

Word combinations / toggles can contain "any" number of words. Thus, `["public", "private", "protected"]` is also supported.

The normal toggle action discussed thus far toggles (i.e., replaces) forward (e.g., 1, 2, 3, 1, ...). Use the reverse toggle action to toggle backwards (e.g., 1, 3, 2, 1, ...). It uses the same keybind as above, but in addition make sure to also hold the <kbd>Alt</kbd> (Windows and Linux) or <kbd>Opt</kbd> (Linux) key.

Toggler supports toggling with multiple cursors (also known as carets) at the same time.

Toggler has limited support for transferring word case. It supports, full caps (`TRUE -> FALSE`) and first letter capitalized (`True -> False`). It defaults back to lowercase in all other cases (`true -> false`).

The partial matching functionality allows for substrings of words to be toggled. For example, `getName` can be toggled to `setName` by placing the cursor anywhere on `get` and then toggling. The largest match found is prioritised. This means that if you have the following toggles configured `["dev", "prod"] and ["development", "production"]`, and toggle `development`, then it will be replaced by `production`. The partial matching functionality can be bypassed by using your cursor to create a precise selection of the sub word you want to toggle (for example making a selection of `dev` and then toggling). The partial matching functionality is enabled by default and can be disabled in the configuration menu.

## Configuration
Configure the word combinations / toggles and other Toggler settings by going to <kbd>Settings/Preferences -> Tools -> Toggler</kbd> in your IDE.

The toggle hotkey / keybind can be changed from <kbd>Settings/Preferences -> Keymap -> Plug-ins -> Toggler</kbd>.

<img src="TogglerConfigurationMenu.png" alt="The Toggler Configuration Menu">

Word combinations / toggles can be added and removed by modifying the JSON-like configuration and subsequently pressing the `Apply` button. The `Import`, `Export` and `Reset to Defaults` buttons have been added for convenience.

Toggler has a custom mechanism for distinguishing / separating words from each other. Because of that the following [boundary characters](https://github.com/Noorts/Toggler/blob/master/src/main/java/core/Config.java#L11)
can't be used inside the toggles
<code>' ', ';', ':', '.', ',', '`', '"', ''', '(', ')', '[', ']', '{', '}', '\t'</code>.

The [toggles](https://github.com/Noorts/Toggler/blob/master/src/main/java/core/Config.java#L22) displayed below
are the default ones included with every fresh installment.
```JSON
[
  ["public", "private", "protected"],
  ["class", "interface"],
  ["extends", "implements"],
  ["import", "export"],
  ["byte", "short", "int", "long", "float", "double"],
  ["String", "Character"],

  ["get", "set"],
  ["add", "remove"],
  ["min", "max"],
  ["pop", "push"],

  ["true", "false"],
  ["yes", "no"],
  ["on", "off"],
  ["0", "1"],
  ["x", "y"],
  ["enable", "disable"],
  ["enabled", "disabled"],
  ["open", "close"],

  ["up", "down"],
  ["left", "right"],
  ["top", "bottom"],
  ["start", "end"],
  ["first", "last"],
  ["before", "after"],
  ["ceil", "floor"],
  ["read", "write"],
  ["show", "hide"],
  ["input", "output"],
  ["dev", "prod"],
  ["development", "production"],
  ["row", "column"],
  ["req", "res"],

  ["&&", "||"],
  ["&", "|"],
  ["<", ">"],
  ["+", "-"],
  ["*", "/"],
  ["++", "--"],
  ["+=", "-="],
  ["*=", "/="],
  ["&=", "|="],
  ["<<=", ">>="],
  ["<=", ">="],
  ["==", "!="],
  ["===", "!=="]
]
```

## Roadmap
The roadmap can be found on the [Toggler board](https://github.com/users/Noorts/projects/2).

## Contribute
1. [Fork](https://docs.github.com/en/get-started/quickstart/fork-a-repo) and
[clone](https://docs.github.com/en/repositories/creating-and-managing-repositories/cloning-a-repository) the repository.
2. Set up your development environment (I use [IntelliJ IDEA](https://www.jetbrains.com/idea/) for Toggler's development).
   * Open the project and load the Gradle project. A notification should show up in the bottom right indicating
"Gradle build scripts found".
   * Install Java 17 (see `javaVersion` in `gradle.properties`) and make sure it is set as the SDK and language level in the
[project structure settings](https://www.jetbrains.com/help/idea/project-settings-and-structure.html).
3. Verify whether you're ready to start development by running the development IDE through Gradle's `runIde` task.
This task should be available under `Toggler/Tasks/intellij` in
the [Gradle sidebar](https://www.jetbrains.com/help/idea/work-with-gradle-tasks.html#gradle_tasks).
This [task](https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html#runide-task)
will build the plugin and open a development IDE, allowing you to test out the plugin directly.
4. Make your desired changes in the code.
5. Verify your changes
   * by running the `test` Gradle task.
   * (optionally) by running the `runPluginVerifier` Gradle task to verify whether the
plugin is still compatible with the IDE build range configured in `gradle.properties`.
6. [Commit](https://github.com/git-guides/git-commit) your changes to your fork.
7. Create a [pull request](https://docs.github.com/en/pull-requests/collaborating-with-pull-requests/proposing-changes-to-your-work-with-pull-requests/creating-a-pull-request-from-a-fork)
from your fork to the [Noorts/Toggler](https://github.com/Noorts/Toggler) repository.

## Alternatives
- If you're looking for more text manipulation features, then check out [Shifter](https://plugins.jetbrains.com/plugin/6149-shifter). Which also includes a dictionary (with custom words) just like Toggler and includes many other nice features.

## Acknowledgements
This plugin has drawn inspiration from similar toggle plugins/extensions.
- <a href="https://marketplace.visualstudio.com/items?itemName=hideoo.toggler">VSCode</a> (by <a href="https://github.com/HiDeoo">HiDeoo</a>)
- <a href="https://atom.io/packages/toggler">Atom</a> (by <a href="https://github.com/HiDeoo">HiDeoo</a>)
- <a href="https://github.com/gordio/ToggleWords">Sublime Text 4</a> (by <a href="https://github.com/gordio">gordio</a>)

## License
Toggler is licensed under the [MIT license](LICENSE.md).
