import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

fun properties(key: String) = project.findProperty(key).toString()

// https://github.com/JetBrains/gradle-intellij-plugin
plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.7.10"
    id("org.jetbrains.intellij") version "1.7.0"
}

group = properties("pluginGroup")
version = properties("pluginVersion")

repositories {
    mavenCentral()
}

dependencies {
    // https://github.com/junit-team/junit5-samples/blob/main/junit5-jupiter-starter-gradle-kotlin/build.gradle.kts
    testImplementation(platform("org.junit:junit-bom:5.8.2"))
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
      <li>Easy importing and exporting of the settings configuration.</li>
      <li>No external dependencies required.</li>
      </ul>

      <h3>Usage</h3>
      Select or place your cursor on a word/symbol and press the default hotkey
      (<kbd>Ctrl+Shift+X</kbd> on Windows or <kbd>Cmd+Shift+X</kbd> on macOS) to
      toggle the word/symbol to the next toggle defined in the configuration file.
      The shortcut can be changed from <kbd>Settings/Preferences -> Keymap -> Plug-ins -> Toggler</kbd>.
      The toggle action can also be found as <kbd>Toggle Word/Symbol</kbd> in the <kbd>Edit</kbd> menu.

      <h3>Configuration</h3>
      Configure the toggles from <kbd>Settings/Preferences -> Tools -> Toggler</kbd>.
      Default toggles have been added to provide functionality right out of the gate.
      Toggles can be added and removed by modifying the JSON and applying the changes.
      The Import, Export and Reset to Defaults buttons have been added for convenience.
      The following characters are used for word/symbol selection and thus can't be used inside the toggles
      <kbd>' ', ';', ':', '.', ',', '`', '"', ''', '(', ')', '[', ']', '{', '}'</kbd>.
      The partial matching functionality (which is enabled by default) can be disabled in the configuration menu.

      <h3>Issues</h3>
      <ul>
      <li>None, currently. Feel free to send me a message or create an issue if you run
      into unexpected behavior or if you have a feature request.</li>
      </ul>
      
      <h3>Alternatives</h3>
      <ul>
      <li>If you're looking for more text manipulation features,
      then check out <a href="https://plugins.jetbrains.com/plugin/6149-shifter">Shifter</a>.
      Which also includes a dictionary (with custom symbols/words) just like Toggler
      and includes many other nice features.</li>
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