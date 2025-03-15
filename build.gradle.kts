import org.jetbrains.changelog.Changelog

fun properties(key: String) = project.findProperty(key).toString()

// https://github.com/JetBrains/gradle-intellij-plugin
plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.1.10"
    id("org.jetbrains.intellij") version "1.17.4"
    id("org.jetbrains.changelog") version "2.2.1"
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
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.12.1")
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

        val changelog = project.changelog // local variable for configuration cache compatibility
        // Get the latest available change notes from the changelog file
        changeNotes = providers.gradleProperty("pluginVersion").map { pluginVersion ->
            val changelogContent = with(changelog) {
                renderItem(
                    (getOrNull(pluginVersion) ?: getUnreleased())
                        .withHeader(true)
                        .withEmptySections(false),
                    Changelog.OutputType.HTML,
                )
            }

            "$changelogContent<br><br>" +
                    "<h2>Other Versions</h2>" +
                    "For all other versions, check out the " +
                    "<a href=\"https://github.com/Noorts/Toggler/blob/master/CHANGELOG.md\">changelog</a>."
        }
    }

    // https://github.com/JetBrains/gradle-changelog-plugin?tab=readme-ov-file#configuration
    changelog {

    }
}