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
