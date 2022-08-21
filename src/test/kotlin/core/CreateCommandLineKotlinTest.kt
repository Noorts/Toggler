// Copyright 2000-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package core

import com.intellij.remoterobot.RemoteRobot
import com.intellij.remoterobot.fixtures.CommonContainerFixture
import com.intellij.remoterobot.fixtures.ContainerFixture
import com.intellij.remoterobot.search.locators.byXpath
import com.intellij.remoterobot.stepsProcessing.step
import com.intellij.remoterobot.utils.keyboard
import com.intellij.remoterobot.utils.waitFor
import core.pages.*
import org.assertj.swing.core.MouseButton
import core.pages.*
import core.steps.JavaExampleSteps
import core.utils.RemoteRobotExtension
import core.utils.StepsLogger
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.awt.event.KeyEvent.*
import java.time.Duration.ofMinutes
import java.time.Duration.ofSeconds

@ExtendWith(RemoteRobotExtension::class)
class CreateCommandLineKotlinTest {
    init {
        StepsLogger.init()
    }

    @AfterEach
    fun closeProject(remoteRobot: RemoteRobot) = with(remoteRobot) {
        idea {
            if (remoteRobot.isMac()) {
                keyboard {
                    hotKey(VK_SHIFT, VK_META, VK_A)
                    enterText("Close Project")
                    enter()
                }
            } else {
                menuBar.select("File", "Close Project")
            }
        }
    }

    @Test
    fun createCommandLineApp(remoteRobot: RemoteRobot) = with(remoteRobot) {
        val sharedSteps = JavaExampleSteps(this)

        welcomeFrame {
            createNewProjectLink.click()
            dialog("New Project") {
                findText("Java").click()
                checkBox("Add sample code").select()
                button("Create").click()
            }
        }
        idea {
            waitFor(ofMinutes(5)) { isDumbMode().not() }
            step("Create App file") {
                with(projectViewTree) {
                    if (hasText("src").not()) {
                        findText(projectName).doubleClick()
                        waitFor { hasText("src") }
                    }
                    findText("src").click(MouseButton.RIGHT_BUTTON)
                }
                actionMenu("New").click()
                actionMenuItem("Java Class").click()
                keyboard { enterText("App"); enter() }
            }
            with(textEditor()) {
                step("Write a code") {
                    Thread.sleep(1_000)
                    editor.findText("App").click()
                    keyboard {
                        key(VK_END)
                        enter()
                    }
                    sharedSteps.autocomplete("main")
                    sharedSteps.autocomplete("sout")
                    keyboard { enterText("\""); enterText("Hello from UI test") }
                }
                step("Launch application") {
                    waitFor(ofSeconds(20)) { button(byXpath("//div[@class='TrafficLightButton']")).hasText("Analyzing...").not() }
                    menuBar.select("Build", "Build Project")
                    waitFor { gutter.getIcons().isNotEmpty() }
                    gutter.getIcons().first { it.description.contains("run.svg") }.click()
                    this@idea.find<CommonContainerFixture>(
                        byXpath("//div[@class='HeavyWeightWindow']"), ofSeconds(4)
                    ).button(byXpath("//div[@disabledicon='execute.svg']"))
                        .click()
                }
            }

            val consoleLocator = byXpath("ConsoleViewImpl", "//div[@class='ConsoleViewImpl']")
            step("Wait for Console appears") {
                waitFor(ofMinutes(1)) { findAll<ContainerFixture>(consoleLocator).isNotEmpty() }
            }
            step("Check the message") {
                waitFor(ofMinutes(1)) { find<ContainerFixture>(consoleLocator).hasText("Hello from UI test") }
            }
        }
    }
}