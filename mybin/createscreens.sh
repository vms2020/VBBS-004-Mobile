#!/bin/bash

# Path to the presentation directory
BASE_DIR="app/src/main/java/io/bbs/seva/vbbs004mobile/presentation/screens"

# Associative array of Destination Name -> Title
declare -A DESTINATIONS=(
    ["Home"]="Home"
    ["Weather"]="Weather"
    ["CurrencyRates"]="Currency Rates"
    ["Signup"]="Sign Up"
    ["EditProfile"]="Edit Profile"
    ["Blogs"]="Blogs"
    ["Shops"]="Shops"
    ["Chats"]="Chats"
)

echo "Starting screen generation..."

# Loop through each destination
for dest in "${!DESTINATIONS[@]}"; do
    title="${DESTINATIONS[$dest]}"

    # Convert CamelCase to snake_case for directory name (e.g., EditProfile -> edit_profile)
    dir_name=$(echo "$dest" | sed 's/\([a-z0-9]\)\([A-Z]\)/\1_\2/g' | tr '[:upper:]' '[:lower:]')

    # Create the directory
    dir_path="$BASE_DIR/$dir_name"
    mkdir -p "$dir_path"

    file_name="${dest}Screen.kt"
    file_path="$dir_path/$file_name"

    # Check if file already exists so we don't overwrite your work!
    if [ -f "$file_path" ]; then
        echo "SKIP: $file_path already exists."
        continue
    fi

    # Write the Kotlin boilerplate to the file
    cat <<EOF > "$file_path"
package io.bbs.seva.vbbs004mobile.presentation.screens.${dir_name}

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun ${dest}Screen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$title",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}
EOF

    echo "CREATED: $file_path"

done

echo "Generation complete!"
