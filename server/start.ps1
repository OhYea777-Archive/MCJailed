$prefix = "[MCJailed]"

$modules = "Rankup", "ScoreboardManager"
$location = ".\plugins\MCJailed\ModuleJars"
$classes = ".\plugins\MCJailed\ModuleClasses"

function msg($msg, $indent = "") {
    echo "$prefix $indent$msg"
}

msg "Copying plugin..."
    copy-Item "../Core/build/libs/Core-*.jar" plugins/MCJailed.jar -recurse -force
msg "Copied plugin!"
echo ""

if (!(test-path -path plugins/MCJailed)) {
    msg "Directory: 'MCJailed' - does not exist. Creating..."
    new-item -itemtype directory -path plugins/MCJailed | out-null
    msg "Directory: 'MCJailed' - created!"
} else {
    msg "Directory: 'MCJailed' - exists"
    
    if (test-path -path plugins/MCJailed/config.json) {
        echo ""
        msg "Reading config.json ..."

        $config = (get-content plugins/MCJailed/config.json) -join "`n" | ConvertFrom-Json
        
        if ($config.moduleJars) {
            msg "Found 'moduleJars'!"

            $location = $config.moduleJars -replace "{dataFolder}", "plugins/MCJailed"
        }

        if ($config.moduleClasses) {
            msg "Found 'moduleClasses'!"

            $location = $config.moduleClasses -replace "{dataFolder}", "plugins/MCJailed"
        }
    }
}

if (test-path -path ../Modules/build/classes/main) {
    $files = Get-ChildItem "../Modules/build/classes/main"

    if (!(test-path -path $classes)) {
        msg "Directory: '$classes' - does not exist. Creating..."
        new-item -itemtype directory -path $classes | out-null
        msg "Directory: '$classes' - created!"
    }

    for ($i = 0; $i -lt $files.Count; $i ++) {
        $filename = $files[$i].BaseName + ".class"

        msg "Copying module..."
        copy-Item $files[$i].FullName ($classes + "/" + $filename) -recurse -force
        msg ("Copied module '$filename'!")
    }
}

echo ""

if (!(test-path -path $location)) {
    msg "Directory: '$location' - does not exist. Creating..."
    new-item -itemtype directory -path $location | out-null
    msg "Directory: '$location' - created!"
} else {
    msg "Directory: '$location' - exists"
}

echo ""
msg "Copying modules..."

    echo ""

    foreach ($module in $modules) {
        msg "Copying module - $module"
            copy-Item "../$module/build/libs/$module-*.jar" "$location/$module.jar" -recurse -force
        msg "Copied module - $module"
        echo ""
    }

msg "Copied modules!"

echo ""

msg "Launching Spigot..."

java -Xms4G -XX:MaxPermSize=256M -jar spigot.jar