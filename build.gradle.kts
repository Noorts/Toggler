import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

fun properties(key: String) = project.findProperty(key).toString()

// https://github.com/JetBrains/gradle-intellij-plugin
plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.8.21"
    id("org.jetbrains.intellij") version "1.13.3"
}

group = properties("pluginGroup")
version = properties("pluginVersion")

repositories {
    mavenCentral()
}

dependencies {
    // https://github.com/junit-team/junit5-samples/blob/main/junit5-jupiter-starter-gradle-kotlin/build.gradle.kts
    testImplementation(platform("org.junit:junit-bom:5.9.3"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

// Configure Gradle IntelliJ Plugin - read more: https://github.com/JetBrains/gradle-intellij-plugin
intellij {
    pluginName.set(properties("pluginName"))
    version.set(properties("platformVersion"))
//    type.set(properties("platformType"))

    // Plugin Dependencies. Uses `platformPlugins` property from the gradle.properties file.
    plugins.set(properties("platformPlugins").split(',').map(String::trim).filter(String::isNotEmpty))
}

tasks {
    // Set the JVM compatibility versions
    properties("javaVersion").let {
        withType<JavaCompile> {
            sourceCompatibility = it
            targetCompatibility = it
        }
        withType<KotlinCompile> {
            kotlinOptions.jvmTarget = it
        }
    }

    wrapper {
        gradleVersion = properties("gradleVersion")
    }

    runPluginVerifier {
        ideVersions.set(properties("pluginVerifierIdeVersions").split(',').map(String::trim).filter(String::isNotEmpty))
    }

    // Disable building searchableOptions as this isn't beneficial for this plugin.
    // From the FAQ: As a result of disabling building searchable options,
    // the configurables that your plugin provides won't be searchable in the Settings dialog.
    // https://github.com/JetBrains/gradle-intellij-plugin/blob/master/FAQ.md
    buildSearchableOptions {
        enabled = false
    }

    patchPluginXml {
        version.set(properties("pluginVersion"))
        sinceBuild.set(properties("pluginSinceBuild"))
        untilBuild.set(properties("pluginUntilBuild"))

        pluginDescription.set("""
      Quickly toggle words and symbols with a hotkey. Toggles can be configured from the settings menu.

      <br><br>
      Do you really like Toggler? Consider giving a star on <a href="https://github.com/Noorts/Toggler">GitHub</a> or leaving a review on the <a href="https://plugins.jetbrains.com/plugin/16166-toggler">JetBrains marketplace</a>.
      <br><br>
      Did you find a bug or do you have a feature request? Feel free to open an issue on <a href="https://github.com/Noorts/Toggler/issues">GitHub</a>.

      <br>
      <h3>Features</h3>
      <ul>
      <li>Customizable through the settings menu.</li>
      <li>Support for multiple cursors.</li>
      <li>Support for numerous toggles.</li>
      <li>Support for partial matches.</li>
      <li>Limited support for transferring word case.</li>
      <li>Easy importing and exporting of the JSON configuration.</li>
      <li>No external dependencies required.</li>
      </ul>

      <h3>Usage</h3>
      Select or place your cursor on a word/symbol and press the hotkey
      (by default <kbd>Ctrl+Shift+X</kbd> on Windows or <kbd>Cmd+Shift+X</kbd> on macOS) to
      toggle the word/symbol to the next toggle defined in the configuration file (wrapping around when it reaches the end).
      The shortcut can be changed from <kbd>Settings/Preferences -> Keymap -> Plug-ins -> Toggler</kbd>.
      The toggle action can also be found as <kbd>Toggle Word/Symbol</kbd> in the <kbd>Edit</kbd> menu.
      <br><br>
      The partial matching functionality allows for substrings of words/symbols to be toggled. E.g. <code>getName</code> could be
      toggled to <code>setName</code> by placing the cursor anywhere on <code>get</code> and then activating the toggle action.
      The largest match found is prioritised. This means that if you have the following toggles configured <code>["dev", "prod"],
      ["development", "production"]</code> and toggle <code>development</code>, then it will be replaced with <code>production</code>. The partial
      matching functionality can be bypassed by using your cursor to create a precise selection of the sub word/symbol you
      want to toggle. The partial matching functionality (which is enabled by default) can also be disabled completely in
      the configuration menu.

      <h3>Configuration</h3>
      You can configure the toggles in your IDE by going to <kbd>Settings/Preferences -> Tools -> Toggler</kbd>.
      <br><br>
      Toggles can be added and removed by modifying the JSON inside the configuration menu and subsequently
      pressing the <code>Apply</code> button. The <code>Import</code>, <code>Export</code> and <code>Reset to Defaults</code> buttons have been added for convenience.
      <br><br>
      The following <a href="https://github.com/Noorts/Toggler/blob/master/src/main/java/core/Config.java#L11">boundary characters</a>
      are used for word/symbol selection internally and thus can't be used inside the toggles
      <code>' ', ';', ':', '.', ',', '`', '"', ''', '(', ')', '[', ']', '{', '}', '\t'</code>.
      <br><br>
      Default <a href="https://github.com/Noorts/Toggler/blob/master/src/main/java/core/Config.java#L22">toggles</a>
      are included with every fresh installment.

      <h3>Roadmap</h3>
      The roadmap can be found on the <a href="https://github.com/users/Noorts/projects/2">Toggler board</a>.

      <h3>Contribute</h3>
      Want to contribute? Check out the <a href="https://github.com/Noorts/Toggler/tree/master#contribute">contribute</a> section over on GitHub.

      <h3>Alternatives</h3>
      If you're looking for more text manipulation features,
      then check out <a href="https://plugins.jetbrains.com/plugin/6149-shifter">Shifter</a>.
      Which also includes a dictionary (with custom symbols/words) just like Toggler
      and includes many other nice features.
      </ul>

      <h3>Acknowledgements</h3>
      This plugin has drawn a lot of inspiration from <a href="https://github.com/HiDeoo">HiDeoo</a> his
      original versions of toggle plugins/extensions
      for <a href="https://marketplace.visualstudio.com/items?itemName=hideoo.toggler">VSCode</a>
      and <a href="https://atom.io/packages/toggler">Atom</a>.
      """)

        changeNotes.set("""
      For all changes check out: <a href="https://github.com/Noorts/Toggler/commits">https://github.com/Noorts/Toggler/commits</a>
      <br><br>

      [1.2.12] - 2023-02-26
      <ul>
      <li>Extend plugin IDE compatibility range to 2021.1 to 2023.1.</li>
      <li>Upgrade Gradle to 7.6.1.</li>
      <li>Update internal plugins.</li>
      </ul><br>

      [1.2.11] - 2022-11-14
      <ul>
      <li>Extend plugin IDE compatibility range from 2022.2 to 2022.3.</li>
      <li>Update plugin description.</li>
      </ul><br>

      [1.2.10] - 2022-11-12
      <ul>
      <li>Add limited support for tabs as whitespace. See: <a href="https://github.com/Noorts/Toggler/issues/20">#20</a>.</li>
      </ul><br>

      [1.2.9] - 2022-08-21
      <ul>
      <li>Update plugin description.</li>
      <li>Upgrade Gradle to 7.5.1.</li>
      <li>Expand integration tests.</li>
      <li>Improve internal organization.</li>
      </ul><br>

      [1.2.8] - 2022-08-20
      <ul>
      <li>Fix Toggler configuration menu bug, which caused the menu to not show up due to incompatibility with external plugins. See: <a href="https://github.com/Noorts/Toggler/issues/5">#5</a>.</li>
      </ul><br>

      [1.2.7] - 2022-07-19
      <ul>
      <li>Extend plugin IDE compatibility range from 2022.1 to 2022.2.</li>
      </ul><br>

      [1.2.6] - 2022-07-17
      <ul>
      <li>Extend plugin IDE compatibility range from 2021.3 to 2022.1.</li>
      <li>Upgrade Gradle to 7.5 and migrate build configs.</li>
      <li>Expand tests.</li>
      </ul><br>

      [1.2.5] - 2022-02-13
      <ul>
      <li>Improve plugin description.</li>
      <li>Add alternatives section to plugin description.</li>
      </ul><br>

      [1.2.4] - 2021-12-29
      <ul>
      <li>Add confirmation dialog to the "Reset to Defaults" button in the configuration menu.</li>
      <li>Replace console system errors with error notifications.</li>
      <li>Improve code quality.</li>
      </ul><br>

      [1.2.3] - 2021-09-11
      <ul>
      <li>Removed deprecated API methods. This improves the plugin its compatibility for future IDE versions.</li>
      <li>Fixed a JsonParser bug that prevented the number 4 from being added to a toggle.</li>
      </ul><br>

      [1.2.2] - 2021-08-02
      <ul>
      <li>Bug caused by folded text in the editor has been fixed.</li>
      <li>The position of the Toggler action in the IDE menu has been adjusted.</li>
      <li>Handling of error when no text can be selected has been improved.</li>
      <li>The no match found notification has been changed slightly.</li>
      </ul><br>

      [1.2.1] - 2021-07-08
      <ul>
      <li>The plugin is now compatible with IDE builds from 2020.3 and onwards. Under the hood the notification system was updated which improves compatibility with future versions of the JetBrains products. This also means that older IDE versions (prior to 2020.3/203.4341) are no longer supported as of this update.</li>
      </ul><br>

      [1.2.0] - 2021-07-07
      <ul>
      <li>The partial matching functionality has finally been added! It is now possible to toggle a partial match such as 'add' inside of 'addClass'. The new functionality is enabled by default and can be disabled in the configuration.</li>
      <li>Unit tests were added to improve the development of Toggler.</li>
      <li>Plugin description updated.</li>
      </ul><br>

      [1.1.2] - 2021-04-15
      <ul>
      <li>Additional boundary characters have been added. They are now set to the following: <kbd>' ', ';', ':', '.', ',', '`', '"', ''', '(', ')', '[', ']', '{', '}'</kbd></li>
      </ul><br>

      [1.1.1] - 2021-03-17
      <ul>
      <li>Words inside of apostrophes will now be detected correctly.</li>
      <li>Boundary characters will now be detected inside of the configuration JSON to prevent invalid toggles.</li>
      <li>Default spacing inside of the configuration JSON is now set to a tab instead of 4 spaces to improve the configuration experience.</li>
      </ul><br>

      [1.1.0] - 2021-02-26
      <ul>
      <li>Total rewrite of the word/symbol selection system to allow for better selection of symbols.
      Note: the system hasn't been tested extensively yet. Use at your own risk.</li>
      <li>It is now possible to toggle multiple carets that are on the same line.</li>
      <li>Plugin description updated.</li>
      <li>Notification clarity improved.</li>
      </ul><br>

      [1.0.1] - 2021-02-25
      <ul>
      <li>Plugin description updated.</li>
      <li>Plugin now compatible with builds from 2019.1 until 2021.2.</li>
      <li>Miscellaneous developer tweaks.</li>
      </ul><br>

      [1.0.0] - 2021-02-22
      <ul>
      <li>Initial version.</li>
      </ul><br>

      """)
    }
}