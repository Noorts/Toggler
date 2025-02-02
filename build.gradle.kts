fun properties(key: String) = project.findProperty(key).toString()

// https://github.com/JetBrains/gradle-intellij-plugin
plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.1.10"
    id("org.jetbrains.intellij") version "1.17.4"
}

group = properties("pluginGroup")
version = properties("pluginVersion")
var remoteRobotVersion = properties("remoteRobotVersion")

kotlin {
    jvmToolchain(Integer.parseInt(properties("javaVersion")))
}

repositories {
    mavenCentral()
    maven { url = uri("https://packages.jetbrains.team/maven/p/ij/intellij-dependencies") }
}

dependencies {
    // https://github.com/junit-team/junit5-samples/blob/main/junit5-jupiter-starter-gradle-kotlin/build.gradle.kts
    testImplementation(platform("org.junit:junit-bom:5.11.4"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    // UI Tests - Remote Robot
    testImplementation("com.intellij.remoterobot:remote-robot:$remoteRobotVersion")
    testImplementation("com.intellij.remoterobot:remote-fixtures:$remoteRobotVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.4")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.11.4")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.11.4")

    testImplementation("com.squareup.okhttp3:logging-interceptor:4.12.0") // Logging Network Calls
    implementation("com.automation-remarks:video-recorder-junit5:2.0") // Video Recording
}

// Configure Gradle IntelliJ Plugin - read more: https://github.com/JetBrains/gradle-intellij-plugin
intellij {
    pluginName.set(properties("pluginName"))
    version.set(properties("platformVersion"))
//    type.set(properties("platformType"))
    // Do not set an until build: https://intellij-support.jetbrains.com/hc/en-us/community/posts/14610689926674/comments/14625872002706
    updateSinceUntilBuild.set(false)

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

    downloadRobotServerPlugin {
        version.set(remoteRobotVersion)
    }

    runIdeForUiTests {
        // For the systemProperties, see:
        // https://github.com/JetBrains/intellij-ui-test-robot/blob/master/ui-test-example/README.md
        systemProperty("robot-server.port", "8082") // default port 8580
        systemProperty("ide.mac.message.dialogs.as.sheets", "false")
        systemProperty("jb.privacy.policy.text", "<!--999.999-->")
        systemProperty("jb.consents.confirmation.enabled", "false")
        systemProperty("ide.mac.file.chooser.native", "false")
        systemProperty("jbScreenMenuBar.enabled", "false")
        systemProperty("apple.laf.useScreenMenuBar", "false")
        systemProperty("idea.trust.all.projects", "true")
        systemProperty("ide.show.tips.on.startup.default.value", "false")
    }

    // Runs the tests in the "core" and "utils" test directories.
    test {
        useJUnit() // discover and execute JUnit4-based tests
    }

    // Runs the UI (remote robot) tests in the "ui" directory.
    // This defines a new gradle task.
    register<Test>("testUI") {
        useJUnitPlatform()
        description = "Runs the UI tests."
        group = "verification"
    }

    patchPluginXml {
        version.set(properties("pluginVersion"))
        sinceBuild.set(properties("pluginSinceBuild"))

        pluginDescription.set("""
      Quickly toggle words and symbols with a hotkey. Toggles can be configured from the settings menu.

      <br><br>
      Do you really like Toggler? Consider giving a star on <a href="https://github.com/Noorts/Toggler">GitHub</a> or leaving a review on the <a href="https://plugins.jetbrains.com/plugin/16166-toggler">JetBrains marketplace</a>.
      <br><br>
      Did you find a bug or do you have a feature request? Feel free to open an issue on <a href="https://github.com/Noorts/Toggler/issues">GitHub</a>.

      <h3>Usage</h3>
      Select or place your cursor on a word and press:
      <ul>
      <li><kbd>Ctrl + Shift + X</kbd> (Windows and Linux)</li>
      <li><kbd>Cmd + Shift + X</kbd> (macOS)</li>
      </ul>

      If the word matches one of the configured word combinations / toggles, then it will be replaced with the next configured word.

      <br><br>
      <code>
      true -> false<br>
      !== -> ===<br>
      && -> ||<br>
      left -> right<br>
      LEFT -> RIGHT<br>
      addClass -> removeClass<br>
      </code>
      <br>

      See the "Advanced Usage" section below for more details.

      <h3>Configuration</h3>
      Configure the word combinations / toggles and other Toggler settings by going to <kbd>Settings/Preferences -> Tools -> Toggler</kbd> in your IDE.
      <br><br>
      The toggle hotkey / keybind can be changed from <kbd>Settings/Preferences -> Keymap -> Plug-ins -> Toggler</kbd>.
      <br><br>
      Word combinations / toggles can be added and removed by modifying the JSON-like configuration and subsequently pressing the <code>Apply</code> button. The <code>Import</code>, <code>Export</code>, and <code>Reset to Defaults</code> buttons have been added for convenience.
      <br><br>
      Toggler has a custom mechanism for distinguishing / separating words from each other. Because of that the following <a href="https://github.com/Noorts/Toggler/blob/master/src/main/java/core/Config.java#L11">boundary characters</a> can't be used inside the toggles <code>' ', ';', ':', '.', ',', '`', '"', ''', '(', ')', '[', ']', '{', '}', '\t'</code>.
      <br><br>
      Default <a href="https://github.com/Noorts/Toggler/blob/master/src/main/java/core/Config.java#L22">toggles</a>
      are included with every fresh installment.

      <h3>Advanced Usage</h3>
      The configured toggles "wrap around", which means that if <code>["true", "false"]</code> is configured, then continually toggling will lead to <code>true -> false -> true -> ...</code>.
      <br><br>
      Word combinations / toggles can contain "any" number of words. Thus, <code>["public", "private", "protected"]</code> is also supported.
      <br><br>
      The normal toggle action discussed thus far toggles (i.e., replaces) forward (e.g., 1, 2, 3, 1, ...). Use the reverse toggle action to toggle backwards (e.g., 1, 3, 2, 1, ...). It uses the same keybind as above, but in addition make sure to also hold the <kbd>Alt</kbd> (Windows and Linux) or <kbd>Opt</kbd> (Linux) key.
      <br><br>
      Toggler supports toggling with multiple cursors (also known as carets) at the same time.
      <br><br>
      Toggler has limited support for transferring word case. It supports, full caps (<code>TRUE -> FALSE</code>) and first letter capitalized (<code>True -> False</code>). It defaults back to lowercase in all other cases (<code>true -> false</code>).
      <br><br>
      The partial matching functionality allows for substrings of words to be toggled. For example, <code>getName</code> can be toggled to <code>setName</code> by placing the cursor anywhere on <code>get</code> and then toggling. The largest match found is prioritised. This means that if you have the following toggles configured <code>["dev", "prod"] and ["development", "production"]</code>, and toggle <code>development</code>, then it will be replaced by <code>production</code>. The partial matching functionality can be bypassed by using your cursor to create a precise selection of the sub word you want to toggle (for example making a selection of <code>dev</code> and then toggling). The partial matching functionality is enabled by default and can be disabled in the configuration menu.

      <h3>Roadmap</h3>
      The roadmap can be found on the <a href="https://github.com/users/Noorts/projects/2">Toggler board</a>.

      <h3>Contribute</h3>
      Want to contribute? Check out the <a href="https://github.com/Noorts/Toggler/tree/master#contribute">contribute</a> section over on GitHub.

      <h3>Alternatives</h3>
      If you're looking for more text manipulation features,
      then check out <a href="https://plugins.jetbrains.com/plugin/6149-shifter">Shifter</a>.
      Which also includes a dictionary (with custom words) just like Toggler
      and includes many other nice features.

      <h3>Acknowledgements</h3>
      This plugin has drawn inspiration from similar toggle plugins / extensions.
      <ul>
      <li><a href="https://marketplace.visualstudio.com/items?itemName=hideoo.toggler">VSCode</a> (by <a href="https://github.com/HiDeoo">HiDeoo</a>)</li>
      <li><a href="https://atom.io/packages/toggler">Atom</a> (by <a href="https://github.com/HiDeoo">HiDeoo</a>)</li>
      <li><a href="https://github.com/gordio/ToggleWords">Sublime Text 4</a> (by <a href="https://github.com/gordio">gordio</a>)</li>
      </ul>

      <h3>License</h3>
      Toggler is licensed under the <a href="https://github.com/Noorts/Toggler/blob/master/LICENSE.md">MIT license</a>.
      """)

        changeNotes.set("""
      For all changes check out: <a href="https://github.com/Noorts/Toggler/commits">https://github.com/Noorts/Toggler/commits</a>
      <br><br>

      [1.5.0] - 2025-02-02
      <ul>
      <li>Enhanced "no match found" notification with direct link to settings panel.</li>
      <li>Slightly reworked notifications.</li>
      <li>Increased clarity of settings panel button tooltips.</li>
      <li>Reworked plugin description.</li>
      </ul><br>

      [1.4.1] - 2024-12-01
      <ul>
      <li>Addressed a deprecation. See: <a href="https://github.com/Noorts/Toggler/issues/80">#80</a>.</li>
      </ul><br>

      [1.4.0] - 2024-05-12
      <ul>
      <li>This update includes internal version bumps to stay compatible with the newest IDE versions.</li>
      <li>The minimum IDE version has been bumped from 2021.1.3 to 2022.3.1. The plugin is now compatible with any IDE
      versions from 2022.3.1 onwards.</li>
      <li>Addressed a deprecation. See: <a href="https://github.com/Noorts/Toggler/issues/64">#64</a>.</li>
      </ul><br>

      [1.3.1] - 2024-02-01
      <ul>
      <li>The plugin is now automatically compatible with any IDE versions from 2021.1 onwards.</li>
      </ul><br>

      [1.3.0] - 2024-01-31
      <ul>
      <li>The reverse toggle action has been added! Instead of forwards (e.g., 1, 2, 3, 1), it is now also possible to toggle backwards with the reverse toggle action (e.g., 1, 3, 2, 1). By default the new action is bound to <kbd>Ctrl/Cmd + Shift + Alt/Opt + X</kbd>. The keybind can be changed under <kbd>Settings/Preferences -> Keymap -> Plug-ins -> Toggler</kbd>.</li>
      <li>Update internal plugins.</li>
      <li>Plugin description updated.</li>
      </ul><br>

      [1.2.14] - 2023-11-12
      <ul>
      <li>Extend plugin IDE compatibility range from 2023.2 to 2023.3.</li>
      <li>Update internal plugins.</li>
      </ul><br>

      [1.2.13] - 2023-06-10
      <ul>
      <li>Extend plugin IDE compatibility range from 2023.1 to 2023.2.</li>
      <li>Update internal plugins.</li>
      </ul><br>

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